package fanap.pod.chat.boilerplateapp.data.remote.models

import com.google.gson.annotations.SerializedName


  class HandShakeRes {

    @SerializedName("result")
    val result: Result? = null

    @SerializedName("referenceNumber")
     val referenceNumber: String? = null

    @SerializedName("count")
     val count = 0

    @SerializedName("errorCode")
     val errorCode = 0

    @SerializedName("hasError")
     val hasError = false

    class Result {
        @SerializedName("keyFormat")
        var keyFormat: String? = null

        @SerializedName("keyId")
        var keyId: String? = null

        @SerializedName("publicKey")
        var publicKey: String? = null

        @SerializedName("expire_in")
        var expireIn = 0

        @SerializedName("algorithm")
        var algorithm: String? = null
    }
}