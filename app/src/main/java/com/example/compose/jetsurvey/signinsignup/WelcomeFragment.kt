package com.example.compose.jetsurvey.signinsignup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.compose.jetsurvey.Screen
import com.example.compose.jetsurvey.navigate

class WelcomeFragment : BaseFragment() {

    private val viewModel: WelcomeViewModel by viewModels {
        WelcomeViewModel.WelcomeViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        viewModel.navigateTo.observe(viewLifecycleOwner) { navigateToEvent ->
            navigateToEvent.getContentIfNotHandled()?.let { navigateTo ->
                navigate(navigateTo, Screen.Welcome)
            }
        }

        val view = TextView(activity)
        view.text = "hello world "
        return view
    }
}