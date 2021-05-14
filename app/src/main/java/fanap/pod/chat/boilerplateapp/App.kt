package fanap.pod.chat.boilerplateapp

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import fanap.pod.chat.boilerplateapp.factory.Factory

class App : MultiDexApplication() {

    companion object {
        var factory: Factory = Factory()
        private var instance: App? = null
        var deviceId :String? = null
        fun getInstance(): App? {
            return instance
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    @SuppressLint("HardwareIds")
    override fun onCreate() {
        super.onCreate()
        instance = this
        deviceId=Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getDeviceId(): String? {
        return deviceId
    }

}