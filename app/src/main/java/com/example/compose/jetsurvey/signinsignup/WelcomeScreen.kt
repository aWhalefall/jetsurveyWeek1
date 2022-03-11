package com.example.compose.jetsurvey.signinsignup

import android.provider.ContactsContract
import android.view.View
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType.Companion.Email
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.jetsurvey.R
import com.example.compose.jetsurvey.theme.JetsurveyTheme
import com.example.compose.jetsurvey.util.supportWideScreen

/**
 * @author kuaidao@reworldgame.com
 * @date 2022/3/11 10:24
 * 欢迎密封类
 */
sealed class WelcomeEvent {
    data class SignInSignUp(val email: String) : WelcomeEvent()
    object SignInAsGuest : WelcomeEvent()
}

@Preview
@Composable
fun WelcomeScreenPreview() {
    JetsurveyTheme {
        WelcomeScreen {}
    }
}

@Composable
fun WelcomeScreen(onEvent: (WelcomeEvent) -> Unit) {
    var brandingBottom by remember { mutableStateOf(0f) }
    var showBranding by remember { mutableStateOf(true) }
    var heightWithBranding by remember { mutableStateOf(0) }
    val currentOffsetHolder = remember { mutableStateOf(0f) }

    currentOffsetHolder.value = if (showBranding) 0f else -brandingBottom

    val currentOffsetHolderDp = with(LocalDensity.current) {
        currentOffsetHolder.value.toDp()
    }

    val heightDp = with(LocalDensity.current) {
        heightWithBranding.toDp()
    }

    Surface(modifier = Modifier.supportWideScreen()) {
        val offset by animateDpAsState(targetValue = currentOffsetHolderDp)
        Column(modifier = Modifier
            .fillMaxWidth()
            .brandingPreferredHeight(showBranding, heightDp)
            .offset(y = offset)
            .onSizeChanged {
                if (showBranding) {
                    heightWithBranding = it.height
                }
            }) {
            //品牌推广
            Branding(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .onGloballyPositioned {
                    if (brandingBottom == 0f) {
                        brandingBottom = it.boundsInParent().bottom
                    }
                })

            //账号
            SignInCreateAccount(
                onEvent, onFocusChange = { focused ->
                    showBranding = !focused

                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

        }
    }

}

@Composable
fun SignInCreateAccount(
    onEvent: (WelcomeEvent) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    modifier: Modifier
) {
    val emailState = remember {
        EmailState()
    }
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = stringResource(id = R.string.sign_in_create_account),
                style = MaterialTheme.typography.subtitle2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 64.dp, bottom = 12.dp)
            )
        }
        val onSubmit = {
            if (emailState.isValid) {
                onEvent(WelcomeEvent.SignInSignUp(emailState.text))
            } else {
                emailState.enableShowErrors()
            }
        }
        onFocusChange(emailState.isFocused)
        Email(emailState = emailState, imeAction = ImeAction.Done, onImeAction = onSubmit)
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp, bottom = 3.dp)
        ) {
            Text(
                text = stringResource(id = R.string.user_continue),
                style = MaterialTheme.typography.subtitle2
            )
        }

        OrSignInAsGuest(
            onSignedInAsGuest = {
                onEvent(WelcomeEvent.SignInAsGuest)
            }, modifier = Modifier.fillMaxWidth()
        )

    }
}

@Composable
fun Email(emailState: EmailState, imeAction: ImeAction, onImeAction: () -> Unit) {
    OutlinedTextField(
        value = emailState.text,
        onValueChange = {
            emailState.text = it
        },
        label = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = stringResource(id = R.string.email),
                    style = MaterialTheme.typography.body2
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                emailState.onFocusChange(focusState.isFocused)
                if (!focusState.isFocused) {
                    emailState.enableShowErrors()
                }
            },
        textStyle = MaterialTheme.typography.body2,
        isError = emailState.showErrors(),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = KeyboardActions(onDone = {
            onImeAction()
        })
    )
    emailState.getError()?.let { error ->
        TextFieldError(textError = error)
    }
}

@Composable
fun TextFieldError(textError: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = textError,
            modifier = Modifier.fillMaxWidth(),
            style = LocalTextStyle.current.copy(color = MaterialTheme.colors.error)
        )

    }
}

@Composable
fun Branding(modifier: Modifier = Modifier) {
    Column(modifier = modifier.wrapContentHeight(align = Alignment.CenterVertically)) {
        Logo(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 76.dp)
        )
        Text(
            text = stringResource(id = R.string.app_tagline),
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun Logo(modifier: Modifier = Modifier, lightTheme: Boolean = MaterialTheme.colors.isLight) {
    val assetId = if (lightTheme) {
        R.drawable.ic_logo_light
    } else {
        R.drawable.ic_logo_dark
    }
    Image(
        painter = painterResource(id = assetId),
        modifier = modifier,
        contentDescription = null
    )
}

private fun Modifier.brandingPreferredHeight(showBranding: Boolean, heightDp: Dp): Modifier {
    return if (showBranding) {
        this
    } else {
        this
            .wrapContentHeight(unbounded = true)
            .height(heightDp)
    }
}
