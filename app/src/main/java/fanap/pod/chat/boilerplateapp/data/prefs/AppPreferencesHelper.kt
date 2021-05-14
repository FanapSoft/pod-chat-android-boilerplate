package fanap.pod.chat.boilerplateapp.data.prefs

import android.content.Context
import android.content.SharedPreferences

class AppPreferencesHelper(context: Context, prefFileName: String) : PreferencesHelper {
    private val PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE"
    private val PREF_KEY_USER_REFRESH_TOKEN = "PREF_KEY_USER_REFRESH_TOKEN"
    val mPrefs: SharedPreferences?

    init {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
    }

    override fun changeLoginState(isLogin: Boolean) {
        mPrefs!!.edit().putBoolean(PREF_KEY_USER_LOGGED_IN_MODE, isLogin)
            .apply()
    }

    override fun getLoginState(): Boolean {
        return mPrefs!!.getBoolean(PREF_KEY_USER_LOGGED_IN_MODE, false)

    }

    override fun saveRefreshToken(refreshToken: String?) {
        mPrefs!!.edit().putString(PREF_KEY_USER_REFRESH_TOKEN, refreshToken)
            .apply()
    }

    override fun getRefreshToken(): String? {
        return mPrefs!!.getString(PREF_KEY_USER_REFRESH_TOKEN, null)
    }

}