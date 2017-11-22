package io.github.zwieback.familyfinance.widget;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import io.github.zwieback.familyfinance.widget.filter.DecimalNumberInputFilter;

public class DecimalNumberTextEdit extends TextInputEditText {

    public DecimalNumberTextEdit(Context context) {
        super(context);
        init();
    }

    public DecimalNumberTextEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DecimalNumberTextEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
        setFilters(new InputFilter[]{new DecimalNumberInputFilter()});
    }
}
