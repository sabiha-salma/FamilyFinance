package io.github.zwieback.familyfinance.widget.adapter

import android.text.Editable
import android.text.TextWatcher
import io.github.zwieback.familyfinance.widget.listener.TextWatcherListener

class TextWatcherAdapter(private val listener: TextWatcherListener) : TextWatcher {

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        listener.onTextChanged(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        // pass
    }

    override fun afterTextChanged(s: Editable) {
        // pass
    }
}
