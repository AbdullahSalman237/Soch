package com.example.soch.tflite;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Build;
import android.util.Log;

import com.example.soch.ObjectRecognizer;
//import com.example.soch.env.Logger;
import com.example.soch.env.Utils;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.Tensor;
import org.tensorflow.lite.gpu.GpuDelegate;
import org.tensorflow.lite.nnapi.NnApiDelegate;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Vector;


public class YoloV5Classifier implements Classifier {


    // Float model
    private final float IMAGE_MEAN = 0;

    private final float IMAGE_STD = 255.0f;

    //config yolo
    private int INPUT_SIZE = -1;

    private  int output_box;


    // Number of threads in the java app
    private static final int NUM_THREADS = 1;
    private static boolean isNNAPI = false;
    private static boolean isGPU = false;

    private boolean isModelQuantized;


    GpuDelegate gpuDelegate = null;
    NnApiDelegate nnapiDelegate = null;

    private MappedByteBuffer tfliteModel;



    // Config values.

    // Pre-allocated buffers.
    private Vector<String> labels = new Vector<String>();
    private int[] intValues;

    private ByteBuffer imgData;
    private ByteBuffer outData;

    private Interpreter tfLite;
    private float inp_scale;
    private int inp_zero_point;
    private float oup_scale;
    private int oup_zero_point;
    private int numClass;
    private YoloV5Classifier() {
    }


    public static YoloV5Classifier create(
            final AssetManager assetManager,
            final String modelFilename,
            final String labelFilename,
            final boolean isQuantized,
            final int inputSize)

            throws IOException {
        final YoloV5Classifier d = new YoloV5Classifier();

        String actualFilename = labelFilename.split("file:///android_asset/")[1];
        //Reading the labels file
        InputStream labelsInput = assetManager.open(actualFilename);
        BufferedReader br = new BufferedReader(new InputStreamReader(labelsInput));
        String line;
        while ((line = br.readLine()) != null) {
//            LOGGER.w(line);
            d.labels.add(line);
        }
        br.close();
////////Setting the configuration
        try {
            Interpreter.Options options = (new Interpreter.Options());
            options.setNumThreads(NUM_THREADS);
            if (isNNAPI) {
                d.nnapiDelegate = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                    d.nnapiDelegate = new NnApiDelegate();
                    options.addDelegate(d.nnapiDelegate);
                    options.setNumThreads(NUM_THREADS);

                }
            }
            if (isGPU) {
                GpuDelegate.Options gpu_options = new GpuDelegate.Options();
                gpu_options.setPrecisionLossAllowed(true); // It seems that the default is true
                gpu_options.setInferencePreference(GpuDelegate.Options.INFERENCE_PREFERENCE_SUSTAINED_SPEED);
                d.gpuDelegate = new GpuDelegate(gpu_options);
                options.addDelegate(d.gpuDelegate);
            }
            d.tfliteModel = Utils.loadModelFile(assetManager,modelFilename);
            d.tfLite = new Interpreter(d.tfliteModel, options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        d.isModelQuantized = isQuantized;
        // Pre-allocate buffers.
        int numBytesPerChannel;
        if (isQuantized) {
            numBytesPerChannel = 1; // Quantized
        } else {
            numBytesPerChannel = 4; // Floating point
        }
        d.INPUT_SIZE = inputSize;
        d.imgData = ByteBuffer.allocateDirect(1 * d.INPUT_SIZE * d.INPUT_SIZE * 3 * numBytesPerChannel);
        d.imgData.order(ByteOrder.nativeOrder());
        d.intValues = new int[d.INPUT_SIZE * d.INPUT_SIZE];
/////////////////////////////////////Maimum possible detections
        d.output_box = (int) ((Math.pow((inputSize / 32), 2) + Math.pow((inputSize / 16), 2) + Math.pow((inputSize / 8), 2)) * 3);


        if (d.isModelQuantized){
            Tensor inpten = d.tfLite.getInputTensor(0);
            d.inp_scale = inpten.quantizationParams().getScale();
            d.inp_zero_point = inpten.quantizationParams().getZeroPoint();
            Tensor oupten = d.tfLite.getOutputTensor(0);
            d.oup_scale = oupten.quantizationParams().getScale();
            d.oup_zero_point = oupten.quantizationParams().getZeroPoint();
        }

        int[] shape = d.tfLite.getOutputTensor(0).shape();
        int numClass = shape[shape.length - 1];
        d.numClass = numClass;
        d.outData = ByteBuffer.allocateDirect(d.output_box * (numClass + 5) * numBytesPerChannel);
        d.outData.order(ByteOrder.nativeOrder());
        return d;
    }

    public int getInputSize() {
        return INPUT_SIZE;
    }
    @Override
    public void enableStatLogging(final boolean logStats) {
    }

    @Override
    public String getStatString() {
        return "";
    }

    @Override
    public void close() {
        tfLite.close();
        tfLite = null;
        if (gpuDelegate != null) {
            gpuDelegate.close();
            gpuDelegate = null;
        }
        if (nnapiDelegate != null) {
            nnapiDelegate.close();
            nnapiDelegate = null;
        }
        tfliteModel = null;
    }

    public void setNumThreads(int num_threads) {
        if (tfLite != null) tfLite.setNumThreads(num_threads);
    }

    @Override
    public void setUseNNAPI(boolean isChecked) {
//        if (tfLite != null) tfLite.setUseNNAPI(isChecked);
    }


    @Override
    public float getObjThresh() {
        return ObjectRecognizer.MINIMUM_CONFIDENCE_TF_OD_API;
    }



    //non maximum suppression
    protected ArrayList<Recognition> nms(ArrayList<Recognition> list) {
        ArrayList<Recognition> nmsList = new ArrayList<Recognition>();

        for (int k = 0; k < labels.size(); k++) {
            //1.find max confidence per class
            PriorityQueue<Recognition> pq =
                    new PriorityQueue<Recognition>(
                            50,
                            new Comparator<Recognition>() {
                                @Override
                                public int compare(final Recognition lhs, final Recognition rhs) {
                                    // Intentionally reversed to put high confidence at the head of the queue.
                                    return Float.compare(rhs.getConfidence(), lhs.getConfidence());
                                }
                            });

            for (int i = 0; i < list.size(); ++i) {
                if (list.get(i).getDetectedClass() == k) {
                    pq.add(list.get(i));
                }
            }

            //2.do non maximum suppression
            while (pq.size() > 0) {
                //insert detection with max confidence
                Recognition[] a = new Recognition[pq.size()];
                Recognition[] detections = pq.toArray(a);
                Recognition max = detections[0];
                nmsList.add(max);
                pq.clear();

                for (int j = 1; j < detections.length; j++) {
                    Recognition detection = detections[j];
                    RectF b = detection.getLocation();
                    if (box_iou(max.getLocation(), b) < mNmsThresh) {
                        pq.add(detection);
                    }
                }
            }
        }
        return nmsList;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////    Functions required for non-maximum suppression
///////////////////////////////////////////////////////////////////////////////////////////////////////
    protected float mNmsThresh = 0.6f;/////////////thresh for non-maximum suppression
////////////////////////////////////////////intersection over union
    protected float box_iou(RectF a, RectF b) {
        return box_intersection(a, b) / box_union(a, b);
    }

    protected float box_intersection(RectF a, RectF b) {
        float w = overlap((a.left + a.right) / 2, a.right - a.left,
                (b.left + b.right) / 2, b.right - b.left);
        float h = overlap((a.top + a.bottom) / 2, a.bottom - a.top,
                (b.top + b.bottom) / 2, b.bottom - b.top);
        if (w < 0 || h < 0) return 0;
        float area = w * h;
        return area;
    }

    protected float box_union(RectF a, RectF b) {
        float i = box_intersection(a, b);
        float u = (a.right - a.left) * (a.bottom - a.top) + (b.right - b.left) * (b.bottom - b.top) - i;
        return u;
    }

    /////////////////////////////////////////////////overlap is required for intersection

    protected float overlap(float x1, float w1, float x2, float w2) {
        float l1 = x1 - w1 / 2;
        float l2 = x2 - w2 / 2;
        float left = l1 > l2 ? l1 : l2;
        float r1 = x1 + w1 / 2;
        float r2 = x2 + w2 / 2;
        float right = r1 < r2 ? r1 : r2;
        return right - left;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {

        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());


        imgData.rewind();
        for (int i = 0; i < INPUT_SIZE; ++i) {
            for (int j = 0; j < INPUT_SIZE; ++j) {
                int pixelValue = intValues[i * INPUT_SIZE + j];
                if (isModelQuantized) {
                    // Quantized model
                    imgData.put((byte) ((((pixelValue >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD / inp_scale + inp_zero_point));
                    imgData.put((byte) ((((pixelValue >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD / inp_scale + inp_zero_point));
                    imgData.put((byte) (((pixelValue & 0xFF) - IMAGE_MEAN) / IMAGE_STD / inp_scale + inp_zero_point));
                } else { // Float model (fp16)
                    imgData.putFloat((((pixelValue >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                    imgData.putFloat((((pixelValue >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                    imgData.putFloat(((pixelValue & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                }
            }
        }
        return imgData;
    }
//////Makes the list of all recognised objects
    public ArrayList<Recognition> recognizeImage(Bitmap bitmap) {


        Map<Integer, Object> outputMap = new HashMap<>();
        outData.rewind();
        outputMap.put(0, outData);
        Log.d("YoloV5Classifier", "mObjThresh: " + getObjThresh());

        Object[] inputArray = {convertBitmapToByteBuffer(bitmap)};
        ////////////////////////detection on the input array of the image
        tfLite.runForMultipleInputsOutputs(inputArray, outputMap);

        ByteBuffer byteBuffer = (ByteBuffer) outputMap.get(0);
        byteBuffer.rewind();

        ArrayList<Recognition> detections = new ArrayList<Recognition>();

        float[][][] out = new float[1][output_box][numClass + 5];
        Log.d("YoloV5Classifier", "out[0] detect start");
        for (int i = 0; i < output_box; ++i) {
            for (int j = 0; j < numClass + 5; ++j) {
                if (isModelQuantized){
                    out[0][i][j] = oup_scale * (((int) byteBuffer.get() & 0xFF) - oup_zero_point);
                }
                else {////fp-16
                    out[0][i][j] = byteBuffer.getFloat();
                }
            }
            // Denormalize xywh(cordinates for Bounding box)
            for (int j = 0; j < 4; ++j) {
                out[0][i][j] *= getInputSize();
            }
        }
        ////////////////Recognition of objects////////////////////////

        for (int i = 0; i < output_box; ++i){
            final int offset = 0;
            final float confidence = out[0][i][4];
            int detectedClass = -1;
            float maxClass = 0;

            final float[] classes = new float[labels.size()];
            for (int c = 0; c < labels.size(); ++c) {
                classes[c] = out[0][i][5 + c];
            }
//////////getting the object with the most probability

            for (int c = 0; c < labels.size(); ++c) {
                if (classes[c] > maxClass) {
                    detectedClass = c;
                    maxClass = classes[c];
                }
            }


            final float confidenceInClass = maxClass * confidence;//////////calculating the overall confidence
            if (confidenceInClass > getObjThresh()) {
                final float xPos = out[0][i][0];///////////X-axis
                final float yPos = out[0][i][1];///////////Y-axis

                final float w = out[0][i][2];///////////////width
                final float h = out[0][i][3];//////////////height
                Log.d("YoloV5Classifier",
                        Float.toString(xPos) + ',' + yPos + ',' + w + ',' + h);

                final RectF rect =
                        new RectF(
                                Math.max(0, xPos - w / 2),
                                Math.max(0, yPos - h / 2),
                                Math.min(bitmap.getWidth() - 1, xPos + w / 2),
                                Math.min(bitmap.getHeight() - 1, yPos + h / 2));
                detections.add(new Recognition("" + offset, labels.get(detectedClass),
                        confidenceInClass, rect, detectedClass));
            }
        }

        Log.d("YoloV5Classifier", "detect end");

        ///////////Applying the non-maximum supression
        final ArrayList<Recognition> recognitions = nms(detections);

        return recognitions;
    }



}
