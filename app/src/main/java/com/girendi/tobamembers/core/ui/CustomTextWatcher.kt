package com.girendi.tobamembers.core.ui

import android.text.Editable
import android.text.TextWatcher
import java.util.Timer
import java.util.TimerTask

class CustomTextWatcher(private val onTextChangeDelayed: (String) -> Unit) : TextWatcher {
    private var timer: Timer? = null

    override fun afterTextChanged(s: Editable?) {
        timer?.cancel()
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                onTextChangeDelayed(s.toString())
            }
        }, 500)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        timer?.cancel()
    }
}