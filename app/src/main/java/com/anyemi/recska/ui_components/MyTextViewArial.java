package com.anyemi.recska.ui_components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextViewArial extends TextView {

    public MyTextViewArial(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextViewArial(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewArial(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/akshar.ttf");
        setTypeface(tf);
    }

//
//    private void init() {
//        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/Dosis-Light.ttf");
//        setTypeface(tf);
//    }

}
