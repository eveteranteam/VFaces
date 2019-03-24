package ua.gov.mva.vfaces.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

import androidx.viewpager.widget.ViewPager

/**
 * Simple wrapper class over original [ViewPager],
 * to allow user to modify whether ViewPager is swipeable or not.
 *
 * @see isSwipeEnabled
 */
class LockableViewPager : ViewPager {

    /**
     * Enable/disable intercepting of touch events by this view.
     */
    var isSwipeEnabled: Boolean = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.isSwipeEnabled = true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (isSwipeEnabled) {
            super.onInterceptTouchEvent(event)
        } else {
            false
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (isSwipeEnabled) {
            super.onInterceptTouchEvent(event)
        } else {
            false
        }
    }
}