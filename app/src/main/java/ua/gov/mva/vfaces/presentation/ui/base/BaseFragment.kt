package ua.gov.mva.vfaces.presentation.ui.base

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import ua.gov.mva.vfaces.R

/**
 * Base class for all Fragments.
 * Extend this class in all your Fragment subclasses.
 *
 * This class has basic observing of [LiveData] for
 * displaying of progress and text messages.
 *
 * @see [BaseViewModel] for more details.
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseFragment<VIEWMODEL : BaseViewModel> : Fragment() {

    private lateinit var viewmodel: VIEWMODEL
    private var progressBar: ProgressBar? = null

    /**
     * This method must be implemented in every subclass.
     */
    protected abstract fun initViewModel(): VIEWMODEL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = initViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.getProgressLiveData().observe(viewLifecycleOwner, Observer { progress ->
            if (progress) {
                showProgressBar()
            } else {
                hideProgressBar()
            }
        })
        viewmodel.getMessageLiveData().observe(viewLifecycleOwner, Observer { message ->
            showMessage(message.text, message.type)
        })
    }

    override fun onDestroy() {
        hideProgressBar()
        super.onDestroy()
    }

    /**
     * Progress bar
     */
    private fun showProgressBar() {
        Log.d(TAG, "showProgressBar")
        if (progressBar != null) {
            Log.d(TAG, "Progress bar is currently visible. Skipping...")
            return
        }
        val activity = activity!!
        val contentView = activity.findViewById<View>(android.R.id.content).rootView as ViewGroup
        progressBar = ProgressBar(activity, null, android.R.attr.progressBarStyle)
        progressBar!!.isIndeterminate = true
        progressBar!!.visibility = View.VISIBLE

        val params = LinearLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )

        val layout = LinearLayout(activity)
        layout.gravity = Gravity.CENTER
        layout.addView(progressBar)

        contentView.addView(layout, params)
        enableTouch(false)
    }

    private fun hideProgressBar() {
        Log.d(TAG, "hideProgressBar")
        progressBar?.visibility = View.GONE
        progressBar = null
        enableTouch(true)
    }
    /**
     * Progress bar
     */

    /**
     * Method enables/disables touch event for entire window.
     * Used with Progress Bar to allow/disallow user to interact with UI components.
     *
     * @param enable - whether touch events should be enabled.
     */
    private fun enableTouch(enable: Boolean) {
        Log.d(TAG, "enableTouch = $enable")
        val window = activity!!.window
        if (enable) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }
    }

    /**
     * Snack messages
     */
    private fun showMessage(text: String, messageType: MessageType) {
        when (messageType) {
            MessageType.TEXT -> {
                showMessage(text)
                return
            }
            MessageType.WARNING -> {
                showWarningMessage(text)
                return
            }
            MessageType.ERROR -> {
                showErrorMessage(text)
                return
            }
        }
    }

    /**
     * Simple message.
     */
    protected fun showMessage(@StringRes resId: Int) {
        showMessage(getString(resId))
    }

    protected fun showMessage(message: String) {
        val contentView = activity!!.findViewById<View>(android.R.id.content)
        showMessage(contentView, message, R.drawable.ic_done)
    }
    /**
     * Simple message.
     */

    /**
     * Warning message.
     */
    protected fun showWarningMessage(@StringRes resId: Int) {
        val contentView = activity!!.findViewById<View>(android.R.id.content)
        showMessage(contentView, getString(resId), R.drawable.ic_warning)
    }

    protected fun showWarningMessage(message: String) {
        val contentView = activity!!.findViewById<View>(android.R.id.content)
        showMessage(contentView, message,  R.drawable.ic_warning)
    }
    /**
     * Warning message.
     */

    /**
     * Error message.
     */
    protected fun showErrorMessage(@StringRes resId: Int) {
        val contentView = activity!!.findViewById<View>(android.R.id.content)
        showMessage(contentView, getString(resId), R.drawable.ic_error)
    }

    protected fun showErrorMessage(message: String) {
        val contentView = activity!!.findViewById<View>(android.R.id.content)
        showMessage(contentView, message, R.drawable.ic_error)
    }
    /**
     * Error message.
     */

    /**
     * Customisable message.
     *
     * @param color - White by default.
     */
    protected fun showMessage(rootView: View, message: String, @DrawableRes icon: Int, @ColorInt color: Int = Color.WHITE) {
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
        val sbView = snackbar.view
        val textView = sbView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
        textView.compoundDrawablePadding = resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)
        textView.setTextColor(color)
        snackbar.show()
    }

    private companion object {
        const val TAG = "BaseFragment"
    }
}