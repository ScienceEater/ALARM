package com.example.alarm.alarm_card;

import android.widget.Button;

public class Model {
    private int image;
    private String title;
    private String desc;
//    private Button button;
// 여기 모델 객체에 수정
    public Model(int image, String title, String desc) {
        this.image = image;
        this.title = title;
        this.desc = desc;
//        this.button = button;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

//    public Button getButton() { return button; }
//
//    public void setButton(Button button) { this.button = button; }
}
