package com.johnpetitto.validator

import android.text.Editable
import android.text.TextWatcher

class TextWatcherValidator(private val layout: ValidatingTextInputLayout) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        // do nothing
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        // do nothing
    }

    override fun afterTextChanged(s: Editable) {
        layout.validate()
    }
}
