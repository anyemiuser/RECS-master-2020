package com.anyemi.recska.ui_components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Anuprog on 10/13/2016.
 */

public class MyEditTextArial extends EditText {

    public MyEditTextArial(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyEditTextArial(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEditTextArial(Context context) {
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
