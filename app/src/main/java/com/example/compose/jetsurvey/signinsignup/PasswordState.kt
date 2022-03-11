package com.example.compose.jetsurvey.signinsignup

class PasswordState :
    TextFieldState(validator = ::isPasswordValid, errorFor = ::passwordValidationError)


class ConfirmPassWorldState(private val passWorldState: PasswordState) : TextFieldState() {
    override val isValid: Boolean
        get() = passwordAndConfirmationValid(passWorldState.text, text)

    override fun getError(): String? {
        return if (showErrors()) {
            passwordConfirmationError()
        } else {
            null
        }
    }

}

private fun passwordAndConfirmationValid(passWorld: String, confirmPassWorld: String): Boolean {
    return isPasswordValid(passWorld) && passWorld == confirmPassWorld
}

@Suppress("UNUSED_PARAMETER")
fun passwordValidationError(password: String): String {
    return "Invalid password"
}

private fun passwordConfirmationError(): String {
    return "Passwords don't match"
}

fun isPasswordValid(password: String): Boolean {
    return password.length > 3
}
