package com.anyemi.recska.ui_components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextViewBoldGotham extends TextView {

    public MyTextViewBoldGotham(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextViewBoldGotham(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewBoldGotham(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/Gotham-Bold.otf");
        setTypeface(tf);
    }

}
