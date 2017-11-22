/**
 * Copyright 2017 Alex Yanchenko
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.droidparts.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;

import org.droidparts.adapter.widget.TextWatcherAdapter;
import org.droidparts.adapter.widget.TextWatcherAdapter.TextWatcherListener;

import static io.github.zwieback.familyfinance.util.StringUtils.isTextEmpty;
import static io.github.zwieback.familyfinance.util.StringUtils.isTextNotEmpty;

/**
 * A clear icon can be changed via
 * <p>
 * <pre>
 * android:drawable(Right|Left)="@drawable/custom_icon"
 * </pre>
 */
public class ClearableEditText extends TextInputEditText
        implements OnTouchListener, OnFocusChangeListener, TextWatcherListener {

    private Location location = Location.RIGHT;
    private Drawable clearIcon;
    private OnClearTextListener onClearTextListener;
    private OnTouchListener onTouchListener;
    private OnFocusChangeListener onFocusChangeListener;

    public ClearableEditText(Context context) {
        super(context);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(new TextWatcherAdapter(this, this));
        initIcon();
        setClearIconVisible(false);
    }

    private void initIcon() {
        clearIcon = null;
        if (location != null) {
            clearIcon = getCompoundDrawables()[location.idx];
        }
        if (clearIcon == null) {
            clearIcon = ContextCompat.getDrawable(getContext(),
                    android.R.drawable.presence_offline);
        }
        clearIcon.setBounds(0, 0, clearIcon.getIntrinsicWidth(), clearIcon.getIntrinsicHeight());
        int min = getPaddingTop() + clearIcon.getIntrinsicHeight() + getPaddingBottom();
        if (getSuggestedMinimumHeight() < min) {
            setMinimumHeight(min);
        }
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getDisplayedDrawable() != null && isClickedOnClearIcon(event)) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                setText("");
            }
            return true;
        }
        if (onTouchListener != null) {
            return onTouchListener.onTouch(v, event);
        }
        return false;
    }

    boolean isClickedOnClearIcon(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int left = (location == Location.LEFT)
                ? 0
                : getWidth() - getPaddingRight() - clearIcon.getIntrinsicWidth();
        int right = (location == Location.LEFT)
                ? getPaddingLeft() + clearIcon.getIntrinsicWidth()
                : getWidth();
        return x >= left && x <= right && y >= 0 && y <= (getBottom() - getTop());
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(isTextNotEmpty(getText()));
        } else {
            setClearIconVisible(false);
        }
        if (onFocusChangeListener != null) {
            onFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public void onTextChanged(EditText view, String text) {
        if (isFocusable()) {
            if (isFocused()) {
                setClearIconVisible(isTextNotEmpty(text));
            }
        } else {
            setClearIconVisible(isTextNotEmpty(text));
        }
        if (isTextEmpty(text) && onClearTextListener != null) {
            onClearTextListener.onTextCleared();
        }
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        initIcon();
    }

    private Drawable getDisplayedDrawable() {
        return location != null ? getCompoundDrawables()[location.idx] : null;
    }

    protected void setClearIconVisible(boolean visible) {
        if (!isEnabled()) {
            return;
        }
        Drawable[] drawables = getCompoundDrawables();
        Drawable displayed = getDisplayedDrawable();
        boolean wasVisible = (displayed != null);
        if (visible != wasVisible) {
            Drawable x = visible ? clearIcon : null;
            super.setCompoundDrawables((location == Location.LEFT) ? x : drawables[0], drawables[1],
                    (location == Location.RIGHT) ? x : drawables[2], drawables[3]);
        }
    }

    /**
     * null disables the icon
     */
    public void setIconLocation(Location loc) {
        this.location = loc;
        initIcon();
    }

    public void setOnClearTextListener(OnClearTextListener onClearTextListener) {
        this.onClearTextListener = onClearTextListener;
    }

    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }

    public enum Location {
        LEFT(0), RIGHT(2);

        final int idx;

        Location(int idx) {
            this.idx = idx;
        }
    }

    public interface OnClearTextListener {

        void onTextCleared();
    }
}
