package com.example.compose.jetsurvey.signinsignup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.jetsurvey.R
import com.example.compose.jetsurvey.theme.JetsurveyTheme
import com.example.compose.jetsurvey.util.supportWideScreen


sealed class SignUpEvent {
    data class SignUp(val email: String, val password: String) : SignUpEvent()
    object SignIn : SignUpEvent()
    object SignInAsGuest : SignUpEvent()
    object NavigateBack : SignUpEvent()
}


@Preview
@Composable
fun SignUpPreView() {
    JetsurveyTheme {
        SignUp {

        }
    }
}

@Composable
fun SignUp(onNavigationEvent: (SignUpEvent) -> Unit) {
    Scaffold(topBar = {
        SignInSignUpTopAppBar(
            topAppBarText = stringResource(id = R.string.create_account),
            onBackPressed = {
                onNavigationEvent(SignUpEvent.NavigateBack)
            })
    }) {
        SignInSignUpScreen(onSignedInAsGuest = {
            onNavigationEvent(SignUpEvent.SignInAsGuest)
        }, modifier = Modifier.supportWideScreen()) {
            Column {
                SignUpContent(onSignInSubmitted = { email, password ->
                    onNavigationEvent(SignUpEvent.SignUp(email, password))
                })
            }

        }

    }

}

@Composable
fun SignUpContent(onSignInSubmitted: (email: String, password: String) -> Unit) {

    Column(modifier = Modifier.fillMaxWidth()) {
        val passwordFocusRequest = remember { FocusRequester() }
        val confirmationPasswordFocusRequest = remember { FocusRequester() }
        val emailState = remember { EmailState() }
        Email(emailState, onImeAction = { passwordFocusRequest.requestFocus() })
        Spacer(modifier = Modifier.height(16.dp))
        val passwordState = remember { PasswordState() }
        PassWord(lable = stringResource(id = R.string.password),
            passwordState = passwordState,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequest),
            imeAction = ImeAction.Next,
            onImeAction = {
                confirmationPasswordFocusRequest.requestFocus()
            })
        Spacer(modifier = Modifier.height(16.dp))
        val confirmationPasswordState = remember { ConfirmPassWorldState(passwordState) }
        PassWord(lable = stringResource(id = R.string.confirm_password),
            passwordState = confirmationPasswordState,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(confirmationPasswordFocusRequest),
            imeAction = ImeAction.Next,
            onImeAction = {
               onSignInSubmitted(emailState.text,passwordState.text)
            })
        Spacer(modifier = Modifier.height(16.dp))
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = stringResource(id = R.string.terms_and_conditions),
                style = MaterialTheme.typography.caption
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onSignInSubmitted(emailState.text, passwordState.text) },
            modifier = Modifier.fillMaxWidth(),
            enabled = emailState.isValid &&
                    passwordState.isValid && confirmationPasswordState.isValid
        ) {
            Text(text = stringResource(id = R.string.create_account))
        }
    }


}
