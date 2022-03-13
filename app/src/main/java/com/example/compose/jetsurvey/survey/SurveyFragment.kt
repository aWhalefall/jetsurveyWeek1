package com.example.compose.jetsurvey.survey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.example.compose.jetsurvey.R
import com.example.compose.jetsurvey.signinsignup.BaseFragment
import com.example.compose.jetsurvey.theme.JetsurveyTheme

class SurveyFragment : BaseFragment() {

    private val viewModel: SurveyViewModel by viewModels {
        SurveyViewModelFactory(PhotoUriManager(requireContext().applicationContext))
    }


    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { photoSaved ->
            if (photoSaved) {
                viewModel.onImageSaved()
            }

        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return ComposeView(requireContext()).apply {
            id = R.id.survey_fragment
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            setContent {
                JetsurveyTheme {
                    viewModel.uiState.observeAsState().value?.let {
                        surveyState ->

                        when (surveyState) {
                            is SurveyState.Questions -> SurveyQuestionScreen(

                            )
//                             is SurveyState.Result -> SurveyResultScreen(
//                                result = surveyState,
//                                onDonePressed = {
//                                    activity?.onBackPressedDispatcher?.onBackPressed()
//                                }
//                            )
                        }
                    }
                }
            }

        }
    }


}