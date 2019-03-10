package ua.gov.mva.vfaces.presentation.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.BaseFragment

class RegisterFragment : BaseFragment<RegisterViewModel>() {

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.button_register).setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.profileFragment)
        }
        view.findViewById<View>(R.id.text_view_register_back).setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
    }

    override fun initViewModel(): RegisterViewModel {
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        return viewModel
    }
}
