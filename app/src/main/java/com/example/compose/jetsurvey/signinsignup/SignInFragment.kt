package com.example.compose.jetsurvey.signinsignup

import SignInViewModel
import SignInViewModelFactor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.example.compose.jetsurvey.R
import com.example.compose.jetsurvey.Screen
import com.example.compose.jetsurvey.navigate
import com.example.compose.jetsurvey.theme.JetsurveyTheme

class SignInFragment : BaseFragment() {

    private val viewModel: SignInViewModel by viewModels { SignInViewModelFactor() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.navigateTo.observe(viewLifecycleOwner) { navigateToEvent ->
            navigateToEvent.getContentIfNotHandled()?.let {
                navigate(it, Screen.SignIn)
            }
        }

        return ComposeView(requireContext()).apply {
            id = R.id.sign_in_fragment
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )

            setContent {
                JetsurveyTheme {
                    SignIn(
                        onNavigationEvent = { event ->
                            when (event) {
                                is SignInEvent.SignIn -> {
                                    viewModel.signIn(event.email, event.password)
                                }
                                SignInEvent.SignUp -> {
                                    viewModel.signUp()
                                }
                                SignInEvent.SignInAsGuest -> {
                                    viewModel.signInAsGuest()
                                }
                                SignInEvent.NavigateBack -> {
                                    activity?.onBackPressedDispatcher?.onBackPressed()
                                }
                            }
                        }
                    )
                }
            }
        }
    }

}