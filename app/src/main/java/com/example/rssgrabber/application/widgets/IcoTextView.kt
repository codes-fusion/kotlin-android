package com.example.rssgrabber.application.widgets

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import com.example.rssgrabber.AppInstance
import com.example.rssgrabber.utils.Fonts

class IcoTextView : TextView {
    var attrs: AttributeSet? = null
    var sTypeface: Typeface? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setText(text: CharSequence?, type: BufferType?) {
        typeface = AppInstance.fonts?.get(Fonts.AWESOME)
        super.setText(text, type)
    }
}
