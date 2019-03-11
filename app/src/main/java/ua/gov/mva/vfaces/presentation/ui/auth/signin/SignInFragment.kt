package ua.gov.mva.vfaces.presentation.ui.auth.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.base.BaseFragment

class SignInFragment : BaseFragment<SignInViewModel>() {

    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.text_view_forget_password).setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.forgotPasswordFragment)
        }
        view.findViewById<View>(R.id.text_view_register).setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.registerFragment)
        }
    }

    override fun initViewModel(): SignInViewModel {
        viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
        return viewModel
    }
}
