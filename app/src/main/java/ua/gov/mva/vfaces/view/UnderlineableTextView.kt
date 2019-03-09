package ua.gov.mva.vfaces.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.TextView
import androidx.annotation.Nullable

/**
 * Custom implementation of [TextView] class.
 * Extends base class to change paintFlags to [Paint.UNDERLINE_TEXT_FLAG]
 * and back to normal [Typeface.NORMAL] in [onTouchEvent] method.
 */
class UnderlineableTextView : TextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    /**
     * Overridden to change paintFlags.
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
                return true
            }
            MotionEvent.ACTION_UP -> {
                paintFlags = Typeface.NORMAL
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}