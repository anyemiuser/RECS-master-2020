package com.anyemi.recska.ui_components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by rahul on 6/8/16.
 */
public class MyTextViewGothamBook extends TextView {
    public MyTextViewGothamBook(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextViewGothamBook(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewGothamBook(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/Gotham-Book.otf");
        setTypeface(tf);
    }
}
