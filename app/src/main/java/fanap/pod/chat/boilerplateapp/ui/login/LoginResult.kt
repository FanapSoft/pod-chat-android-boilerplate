package fanap.pod.chat.boilerplateapp.ui.login

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: Boolean = false,
    val message: Int? = null,
    val state: Int? = null
)