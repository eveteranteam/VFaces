package ua.gov.mva.vfaces.presentation.ui.auth.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.BaseFragment

class ForgotPasswordFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.text_view_back).setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
    }
}