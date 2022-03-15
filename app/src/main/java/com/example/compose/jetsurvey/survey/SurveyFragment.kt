package com.example.compose.jetsurvey.survey

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
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
import com.google.android.material.datepicker.MaterialDatePicker

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
                    viewModel.uiState.observeAsState().value?.let { surveyState ->
                        when (surveyState) {
                            is SurveyState.Questions -> SurveyQuestionScreen(
                                questions = surveyState,
                                shouldAskPermissions = viewModel.askForPermissions,
                                onAction = { id, action -> handleSurveyAction(id, action) },
                                onDoNotAskForPermissions = { viewModel.doNotAskForPermissions() },
                                onDonePressed = { viewModel.computeResult(surveyState) },
                                onBackPressed = {
                                    activity?.onBackPressedDispatcher?.onBackPressed()
                                },
                                openSettings = {
                                    activity?.startActivity(
                                        Intent(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", context.packageName, null)
                                        )
                                    )
                                }
                            )
                            is SurveyState.Result -> SurveyResultScreen(
                                result = surveyState,
                                onDonePressed = {
                                    activity?.onBackPressedDispatcher?.onBackPressed()
                                }
                            )
                        }
                    }
                }
            }

        }
    }


    private fun handleSurveyAction(questionId: Int, actionType: SurveyActionType) {
        when (actionType) {
            SurveyActionType.PICK_DATE -> showDatePicker(questionId)
            SurveyActionType.TAKE_PHOTO -> takeAPhoto()
            SurveyActionType.SELECT_CONTACT -> selectContact(questionId)
        }
    }

    private fun showDatePicker(questionId: Int) {
        val date = viewModel.getCurrentDate(questionId = questionId)
        val picker = MaterialDatePicker.Builder.datePicker()
            .setSelection(date)
            .build()
        activity?.let {
            picker.show(it.supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                viewModel.onDatePicked(questionId, picker.selection)
            }
        }
    }

    private fun takeAPhoto() {
        takePicture.launch(viewModel.getUriToSaveImage())
    }

    @Suppress("UNUSED_PARAMETER")
    private fun selectContact(questionId: Int) {
        // TODO: unsupported for now
    }


}