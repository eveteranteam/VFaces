package ua.gov.mva.vfaces.presentation.ui.auth.register.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.BaseFragment

class ProfileFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
}
