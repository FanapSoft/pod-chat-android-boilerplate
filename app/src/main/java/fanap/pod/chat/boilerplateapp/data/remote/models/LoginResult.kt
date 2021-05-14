package fanap.pod.chat.boilerplateapp.data.remote.models

import com.google.gson.annotations.SerializedName

class LoginResult : PodResult() {

    @SerializedName("result")
    private var result: Result? = null

    fun getResult(): Result? {
        return result
    }

    fun setResult(result: Result?) {
        this.result = result
    }

    class Result {
        @SerializedName("expires_in")
         val expiresIn = 0

        @SerializedName("identity")
         val identity: String? = null

        @SerializedName("type")
         val type: String? = null

        @SerializedName("user_id")
         val userId = 0
    }
}
