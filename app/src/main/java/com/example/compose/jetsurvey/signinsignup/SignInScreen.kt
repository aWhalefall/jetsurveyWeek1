package com.example.compose.jetsurvey.signinsignup

import android.widget.Space
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.jetsurvey.R
import com.example.compose.jetsurvey.theme.JetsurveyTheme
import com.example.compose.jetsurvey.theme.snackbarAction
import com.example.compose.jetsurvey.util.supportWideScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


sealed class SignInEvent {
    data class SignIn(val email: String, val password: String) : SignInEvent()
    object SignUp : SignInEvent()
    object SignInAsGuest : SignInEvent()
    object NavigateBack : SignInEvent()
}


@Preview
@Composable
fun SignInPreView() {
    JetsurveyTheme {
        SignIn {

        }
    }
}

@Composable
fun SignIn(onNavigationEvent: (SignInEvent) -> Unit) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()
    val snackbarErrorText = stringResource(id = R.string.feature_not_available)
    val snackbarActionLabel = stringResource(id = R.string.dismiss)
    Scaffold(topBar = {
        SignInSignUpTopAppBar(
            topAppBarText = stringResource(id = R.string.sign_in),
            onBackPressed = {
                onNavigationEvent(SignInEvent.NavigateBack)
            })
    }, content = {
        SignInSignUpScreen(
            modifier = Modifier.supportWideScreen(),
            onSignedInAsGuest = {
                onNavigationEvent(SignInEvent.SignInAsGuest)
            }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                SignInContent(onSignInSubmitted = { email, password ->
                    onNavigationEvent(SignInEvent.SignIn(email, password))
                })
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = {
                    onNavigationEvent(SignInEvent.SignUp)
                  //showSnack(scope, snackBarHostState, snackbarErrorText, snackbarActionLabel)
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = stringResource(id = R.string.forgot_password))
                }

            }

        }
    })

    Box(modifier = Modifier.fillMaxWidth()) {
        ErrorSnackBar(
            snackbarHostState = snackBarHostState,onDismiss = {
                snackBarHostState.currentSnackbarData?.dismiss()
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private  fun showSnack(
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    snackbarErrorText: String,
    snackbarActionLabel: String
) {
//    scope.launch {
//        snackBarHostState.showSnackbar(
//            message = snackbarErrorText, actionLabel = snackbarActionLabel
//        )
//    }
}

@Composable
fun ErrorSnackBar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(modifier = Modifier.padding(16.dp),
                content = {
                    Text(text = data.message, style = MaterialTheme.typography.body2)

                }, action = {
                    data.actionLabel?.let {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = stringResource(id = R.string.dismiss),
                                color = MaterialTheme.colors.snackbarAction
                            )
                        }
                    }
                })
        }, modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom)
    )

}

@Composable
fun SignInContent(onSignInSubmitted: (email: String, password: String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val focusRequester = remember {
            FocusRequester()
        }
        val emailState = remember {
            EmailState()
        }
        Email(emailState, onImeAction = {
            focusRequester.requestFocus()
        })
        Spacer(modifier = Modifier.height(16.dp))
        val passwordState = remember { PasswordState() }
        PassWord(
            lable = stringResource(id = R.string.password),
            passwordState = passwordState,
            modifier = Modifier.focusRequester(focusRequester),
            onImeAction = {
                onSignInSubmitted(emailState.text, passwordState.text)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onSignInSubmitted(emailState.text, passwordState.text) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = emailState.isValid && passwordState.isValid
        ) {
            Text(
                text = stringResource(id = R.string.sign_in)
            )
        }
    }
}




