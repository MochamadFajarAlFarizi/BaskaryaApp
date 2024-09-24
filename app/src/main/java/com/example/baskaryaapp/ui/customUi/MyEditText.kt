package com.example.baskaryaapp.ui.customUi

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.example.baskaryaapp.R

class MyEditText : AppCompatEditText{

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                when (id) {
                    R.id.emailEditText -> emailValidation(s.toString())
                    R.id.passwordEditText -> passwordValidation(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun nameValidation(text: String) {
        if (text.isNullOrEmpty()) {
            error = resources.getString(R.string.error)
        } else {
            error = null
        }
    }


    private fun emailValidation(text: String) {
        if (text.isNullOrEmpty()) {
            error = resources.getString(R.string.error)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            error = resources.getString(R.string.error_email)
        } else {
            error = null
        }
    }

    private fun passwordValidation(text: String) {
        if (text.isNullOrEmpty()) {
            error = resources.getString(R.string.error)
        } else if (text.toString().length < 8) {
            error = resources.getString(R.string.error_password)
        } else {
            error = null
        }
    }
}
