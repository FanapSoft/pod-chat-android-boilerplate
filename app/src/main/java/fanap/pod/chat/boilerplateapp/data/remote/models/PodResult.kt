package fanap.pod.chat.boilerplateapp.data.remote.models

import com.google.gson.annotations.SerializedName

open class PodResult {

    /**
     * error : invalid_request
     * error_description : Verification code is either wrong or expired
     */
    @SerializedName("error")
    private var error: String? = null

    @SerializedName("error_description")
    private var errorDescription: String? = null

    fun getError(): String? {
        return error
    }

    fun setError(error: String?) {
        this.error = error
    }

    fun getErrorDescription(): String? {
        return errorDescription
    }

    fun setErrorDescription(errorDescription: String?) {
        this.errorDescription = errorDescription
    }
}