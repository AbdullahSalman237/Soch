package com.example.soch;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

//import com.example.soch.env.ImageUtils;
//import com.example.soch.env.Logger;
import com.example.soch.env.Utils;
import com.example.soch.tflite.Classifier;
import com.example.soch.tflite.YoloV5Classifier;
//import com.example.soch.tracking.MultiBoxTracker;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class ObjectRecognizer extends Fragment {
    View view;
    Button r;
    private DBHandler dbHandler;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.55f;

    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private Button mButtonSpeak;

    private boolean hasCameraPermission() {
        int cameraPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        return cameraPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, do something
            } else {
                // Permission denied, show a message or disable functionality that depends on this permission
            }
        }
    }


//    private static final Logger LOGGER = new Logger();

    public static final int TF_OD_API_INPUT_SIZE = 320;

    private static final boolean TF_OD_API_IS_QUANTIZED = false;

    private static final String TF_OD_API_MODEL_FILE = "best-fp16.tflite";

    private static final String TF_OD_API_LABELS_FILE = "file:///android_asset/labels.txt";

    // Minimum detection confidence to track a detection.
    private static final boolean MAINTAIN_ASPECT = true;
    private Integer sensorOrientation = 90;
    private android.speech.tts.TextToSpeech mTTS;
    private Classifier detector;

    private Bitmap sourceBitmap;
    private Bitmap cropBitmap;

    private ImageView imageView;

    private void initBox() {

        try {
            detector =
                    YoloV5Classifier.create(
                            getContext().getAssets(),
                            TF_OD_API_MODEL_FILE,
                            TF_OD_API_LABELS_FILE,
                            TF_OD_API_IS_QUANTIZED,
                            TF_OD_API_INPUT_SIZE);
        } catch (final IOException e) {
            e.printStackTrace();
//            LOGGER.e(e, "Exception initializing classifier!");
            Toast toast =
                    Toast.makeText(
                            getContext(), "Object Detector could not be initialized", Toast.LENGTH_SHORT);
            toast.show();
            //finish();
        }
    }

    private void handleResult(Bitmap bitmap, List<Classifier.Recognition> results) {
        final Canvas canvas = new Canvas(bitmap);
        final Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);

        int x = results.size();
//        Toast.makeText(getContext(),String.valueOf(x), Toast.LENGTH_SHORT).show();

        final List<Classifier.Recognition> mappedRecognitions =
                new LinkedList<Classifier.Recognition>();

        for (final Classifier.Recognition result : results) {
            final RectF location = result.getLocation();

            if (location != null && result.getConfidence() >= MINIMUM_CONFIDENCE_TF_OD_API) {
//                Toast.makeText(this, result.getTitle().toString() , Toast.LENGTH_SHORT).show();
                float r,l;
                r=result.getLocation().right;
                l=result.getLocation().left;
                float bb=r-l;
                if (bb>50) {
                    canvas.drawRect(location, paint);
                    getObject(result.getTitle().toString());
                }
//                cropToFrameTransform.mapRect(location);
//
//                result.setLocation(location);
//                mappedRecognitions.add(result);
            }
        }


        imageView.setImageBitmap(bitmap);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_object_recognizer, container, false);
        // By ID we can get each component which id is assigned in XML file get Buttons and imageview.
        imageView = view.findViewById(R.id.imageView12);
        dbHandler = new DBHandler(getContext());

        Dialog dialog_instructions = new Dialog(getContext());
        //user is shown a cancellation dialogbox
        dialog_instructions.setContentView(R.layout.object_instructions);
        dialog_instructions.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_instructions.setCancelable(false);
        r=dialog_instructions.findViewById(R.id.doit);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_instructions.dismiss();
            }
        });
        dialog_instructions.show();
        mTTS = new android.speech.tts.TextToSpeech(getContext(), new android.speech.tts.TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == android.speech.tts.TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(new Locale("ur"));
                    if (result == android.speech.tts.TextToSpeech.LANG_MISSING_DATA
                            || result == android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
//                        mButtonSpeak.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasCameraPermission())
                dispatchTakePictureIntent();
                else requestCameraPermission();


            }
        });


///////////////////////////////////////////////////////////////////////////////////////////////////////////
//        this.sourceBitmap = Utils.getBitmapFromAsset(getContext(), "glasses(437).jpg");
//
//
//        this.cropBitmap = Utils.processBitmap(sourceBitmap, TF_OD_API_INPUT_SIZE);
//
//        this.imageView.setImageBitmap(cropBitmap);

        initBox();
        ActivityManager activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();










////////////////////////////////////////////////////////////////////////////////////////////////////////

        return view;
    }
    private void getObject(String obj) {
        String text ="";
//
        Toast.makeText(getContext(),obj,Toast.LENGTH_SHORT).show();
        Cursor c=dbHandler.getObjName(obj);
        if (c.moveToNext()){
            text=c.getString(1);
        }

        mTTS.speak(text, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null);
    }



    public void detect(){
        Handler handler = new Handler();
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        cropBitmap = Utils.processBitmap(bitmap, TF_OD_API_INPUT_SIZE);


        imageView.setImageBitmap(cropBitmap);
        new Thread(() -> {
            final List<Classifier.Recognition> results = detector.recognizeImage(cropBitmap);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    handleResult(cropBitmap, results);
                }
            });
        }).start();

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            sourceBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(sourceBitmap);
            detect();
        }
    }
}


