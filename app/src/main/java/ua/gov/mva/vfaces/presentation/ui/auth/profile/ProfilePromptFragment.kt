package ua.gov.mva.vfaces.presentation.ui.auth.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.auth.AuthHostActivity
import ua.gov.mva.vfaces.presentation.ui.base.activity.OnBackPressedCallback
import ua.gov.mva.vfaces.presentation.ui.base.fragment.SimpleBaseFragment
import ua.gov.mva.vfaces.utils.Preferences

class ProfilePromptFragment : SimpleBaseFragment(), OnBackPressedCallback {

    override val TAG = "ProfilePromptFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile_prompt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.button_fill_profile).setOnClickListener {
            transaction.replaceFragment(ProfileFragment.newInstance())
        }
        view.findViewById<View>(R.id.text_view_switch_account).setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        Preferences.putBoolean(ProfileFragment.PROFILE_SAVED_KEY, false)
        AuthHostActivity.start(context!!)
        activity!!.finish()
    }

    /**
     * Always return true.
     * Never allow user to return back from this Fragment.
     */
    override fun onBackPressed(): Boolean {
        return true
    }

    companion object {
        fun newInstance() : ProfilePromptFragment {
            return ProfilePromptFragment()
        }
    }
}