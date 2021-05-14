package fanap.pod.chat.boilerplateapp.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val phoneNumberError: Int? = null,
    val verifyCodeError: Int? = null,
    val isDataValid: Boolean = false,
    val type: Int? = null
)