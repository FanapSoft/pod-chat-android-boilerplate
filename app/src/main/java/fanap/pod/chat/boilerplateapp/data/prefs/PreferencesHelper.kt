package fanap.pod.chat.boilerplateapp.data.prefs

interface PreferencesHelper {

 fun changeLoginState(isLogin: Boolean)
 fun getLoginState(): Boolean
 fun saveRefreshToken(refreshToken: String?)
 fun getRefreshToken() :String?

}