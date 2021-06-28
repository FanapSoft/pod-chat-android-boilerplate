package fanap.pod.chat.boilerplateapp.data

import com.fanap.podchat.chat.ChatHandler
import com.fanap.podchat.requestobject.RequestConnect
import com.fanap.podchat.requestobject.RequestThread
import fanap.pod.chat.boilerplateapp.data.chat.AppChatHelper
import fanap.pod.chat.boilerplateapp.data.chat.ChatCallBackHelper
import fanap.pod.chat.boilerplateapp.data.prefs.AppPreferencesHelper
import fanap.pod.chat.boilerplateapp.data.remote.AppApiHelper
import fanap.pod.chat.boilerplateapp.data.remote.models.HandShakeRes
import fanap.pod.chat.boilerplateapp.data.remote.models.LoginResult
import fanap.pod.chat.boilerplateapp.data.remote.models.SSoTokenResult
import io.reactivex.Observable

class AppDataManager(
    private val api: AppApiHelper,
    private val prefs: AppPreferencesHelper,
    private val chatHelper: AppChatHelper
) : DataManager {
    override fun addListener(listener: ChatCallBackHelper) {
        chatHelper.addListener(listener)
    }

    override fun changeLoginState(isLogin: Boolean) {
        prefs.changeLoginState(isLogin)
    }

    override fun getLoginState(): Boolean {
       return prefs.getLoginState()
    }

    override fun saveRefreshToken(refreshToken: String?) {
       prefs.saveRefreshToken(refreshToken)
    }

    override fun getRefreshToken(): String? {
       return prefs.getRefreshToken()
    }

    override fun handShake(
        deviceName: String,
        deviceOs: String,
        deviceOsVersion: String,
        deviceType: String,
        deviceUID: String
    ): Observable<HandShakeRes?> {
        return api.handShake(deviceName,deviceOs,deviceOsVersion,deviceType,deviceUID)
    }

    override fun login(identity: String, keyId: String): Observable<LoginResult?> {
        return api.login(identity,keyId)
    }

    override fun verifyNumber(
        keyId: String,
        number: String,
        verifyCode: String
    ): Observable<SSoTokenResult?> {
        return api.verifyNumber(keyId,number,verifyCode)
    }

    override fun getOTPToken(code: String): Observable<SSoTokenResult?> {
        return api.getOTPToken(code)
    }

    override fun refreshToken(refreshToken: String): Observable<SSoTokenResult?> {
        return api.refreshToken(refreshToken)
    }

    override fun connectChat(request : RequestConnect) {
        chatHelper.connectChat(request)
    }

    override fun getThread(requestThread: RequestThread,listener : ChatHandler?) {
        chatHelper.getThread(requestThread,listener)
    }

    override fun clearCache() {
        chatHelper.clearCache()
    }

    override fun destroy() {
        chatHelper.destroy()
    }

    override fun setToken(token: String) {
        chatHelper.setToken(token)
    }
}