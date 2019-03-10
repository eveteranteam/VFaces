package ua.gov.mva.vfaces.presentation.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import ua.gov.mva.vfaces.R
import ua.gov.mva.vfaces.presentation.ui.BaseFragment

class RegisterFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.button_register).setOnClickListener {
            register()
        }
        view.findViewById<View>(R.id.text_view_register_back).setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
    }

    private fun register() {

    }
}
