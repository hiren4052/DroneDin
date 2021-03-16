package com.grewon.dronedin.utils

import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout

class TextChangeListeners {
    companion object {

        fun editErrorTextRemover(editText: AppCompatEditText, textInputLayout: TextInputLayout) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if (textInputLayout.error != null) {
                        textInputLayout.error = null
                        textInputLayout.isErrorEnabled = false
                    }
                }
            })
        }


        fun addInputTextError(errorText: String, textInputLayout: TextInputLayout) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = errorText
        }


    }
}