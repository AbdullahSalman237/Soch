package com.example.soch;


public class Image {

    private String imageId;
    private String option2=" ",option3=" ",option4= " ";
    private byte[] imageByteArray;

    public String getImageId() {
        return imageId;
    }
    public String getOption2() {return option2;}
    public String getOption3() {return option3;}
    public String getOption4() {return option4;}
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
    public void setOption2(String option2){this.option2=option2;}
    public void setOption3(String option3){this.option3=option3;}
    public void setOption4(String option4){this.option4=option4;}
    public byte[] getImageByteArray() {
        return imageByteArray;
    }
    public void setImageByteArray(byte[] imageByteArray) {
        this.imageByteArray = imageByteArray;
    }

}