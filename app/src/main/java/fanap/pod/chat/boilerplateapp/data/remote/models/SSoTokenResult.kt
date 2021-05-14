package fanap.pod.chat.boilerplateapp.data.remote.models

import com.google.gson.annotations.SerializedName

class SSoTokenResult : PodResult() {
    @SerializedName("result")
    var result: Result? = null

    inner class Result {
        @SerializedName("access_token")
        var accessToken: String? = null

        @SerializedName("expires_in")
        var expiresIn: Long = 0
            set(expiresIn) {
                field = System.currentTimeMillis() + expiresIn * 1000
            }

        @SerializedName("id_token")
        var idToken: String? = null

        @SerializedName("refresh_token")
        var refreshToken: String? = null

        @SerializedName("scope")
        var scope: String? = null

        @SerializedName("token_type")
        var tokenType: String? = null
    }
}