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
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar

/**
 *
 */
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
        val contentView = activity!!.findViewById<View>(android.R.id.content)
        when (messageType) {
            MessageType.TEXT -> {
                showSnackMessage(contentView, text, Color.WHITE)
                return
            }
            MessageType.WARNING -> {
                showSnackMessage(contentView, text, Color.YELLOW)
                return
            }
            MessageType.ERROR -> {
                showSnackMessage(contentView, text, Color.RED)
                return
            }
        }
    }

    protected fun showSnackMessage(@StringRes resId: Int) {
        showSnackMessage(getString(resId))
    }

    protected fun showSnackMessage(message: String) {
        val contentView = activity!!.findViewById<View>(android.R.id.content)
        showSnackMessage(contentView, message, Color.WHITE)
    }

    protected fun showSnackMessage(rootView: View, @StringRes resId: Int) {
        showSnackMessage(rootView, getString(resId), Color.WHITE)
    }

    protected fun showSnackMessage(rootView: View, message: String) {
        showSnackMessage(rootView, message, Color.WHITE)
    }

    protected fun showSnackMessage(rootView: View, @StringRes resId: Int, @ColorInt color: Int) {
        showSnackMessage(rootView, getString(resId), color)
    }

    protected fun showSnackMessage(rootView: View, message: String, @ColorInt color: Int) {
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
        val sbView = snackbar.view
        val textView = sbView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(color)
        snackbar.show()
    }
    /**
     * Snack messages
     */

    private companion object {
        const val TAG = "BaseFragment"
    }
}