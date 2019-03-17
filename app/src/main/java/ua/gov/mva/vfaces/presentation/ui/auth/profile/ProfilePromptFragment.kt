package ua.gov.mva.vfaces.presentation.ui.auth.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.base.OnBackPressedCallback
import ua.gov.mva.vfaces.presentation.ui.base.SimpleBaseFragment

class ProfilePromptFragment : SimpleBaseFragment(), OnBackPressedCallback {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile_prompt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.button_fill_profile).setOnClickListener {
            transaction.replaceFragment(ProfileFragment.newInstance())
        }
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