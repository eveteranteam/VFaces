package ua.gov.mva.vfaces.presentation.ui.auth.register.finish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.BaseFragment

class FinishRegistrationFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_finish_registration, container, false)
    }
}
