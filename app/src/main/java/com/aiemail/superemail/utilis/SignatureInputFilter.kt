package com.aiemail.superemail.utilis


import android.text.InputFilter
import android.text.Spanned

class SignatureInputFilter(private val signature: String) : InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence {
        // Remove the existing signature from the text
        val textWithoutSignature = removeSignature(dest.toString())

        // Insert the entered text at the specified position
        val modifiedText = StringBuilder(textWithoutSignature).apply {
            insert(dstart, source)
        }

        // Append the signature at the end
        val textWithSignature = appendSignature(modifiedText)

        return textWithSignature
    }

    private fun removeSignature(text: String): String {
        val signatureStartIndex = text.indexOf(signature)
        return if (signatureStartIndex >= 0) {
            text.substring(0, signatureStartIndex)
        } else {
            text
        }
    }

    private fun appendSignature(text: StringBuilder): CharSequence {
        if (!text.endsWith(signature)) {
            text.append(signature)
        }
        return text
    }
}

