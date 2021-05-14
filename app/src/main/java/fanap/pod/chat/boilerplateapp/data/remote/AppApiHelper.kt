package fanap.pod.chat.boilerplateapp.data.remote

import fanap.pod.chat.boilerplateapp.data.remote.models.HandShakeRes
import fanap.pod.chat.boilerplateapp.data.remote.models.LoginResult
import fanap.pod.chat.boilerplateapp.data.remote.models.SSoTokenResult
import io.reactivex.Observable


class AppApiHelper :ApiHelper {
    private val client : ApiHelper = RetrofitClient.getInstance().create(ApiHelper::class.java)
    override fun handShake(
        deviceName: String,
        deviceOs: String,
        deviceOsVersion: String,
        deviceType: String,
        deviceUID: String
    ): Observable<HandShakeRes?> {
        return client.handShake(deviceName,deviceOs,deviceOsVersion,deviceType,deviceUID)
    }

    override fun login(identity: String, keyId: String): Observable<LoginResult?> {
        return client.login(identity,keyId)
    }

    override fun verifyNumber(
        keyId: String,
        number: String,
        verifyCode: String
    ): Observable<SSoTokenResult?> {
        return client.verifyNumber(keyId,number,verifyCode)
    }

    override fun getOTPToken(code: String): Observable<SSoTokenResult?> {
        return client.getOTPToken(code)
    }

    override fun refreshToken(refreshToken: String): Observable<SSoTokenResult?> {
        return client.refreshToken(refreshToken)
    }


}