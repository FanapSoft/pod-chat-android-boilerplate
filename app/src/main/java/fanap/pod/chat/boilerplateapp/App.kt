package fanap.pod.chat.boilerplateapp

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.lifecycle.ViewModelProvider
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import fanap.pod.chat.boilerplateapp.factory.Factory
import fanap.pod.chat.boilerplateapp.factory.ViewModelFactory
import fanap.pod.chat.boilerplateapp.ui.main.MainViewModel

class App : MultiDexApplication() {
    var viewModel : MainViewModel?= null
    companion object {
        var factory: Factory = Factory()
        private lateinit var  instance: App
        var deviceId :String? = null
        fun getInstance(): App {
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

    @JvmName("getViewModel1")
    fun getViewModel() : MainViewModel{
        if(viewModel == null)
            viewModel = MainViewModel()
        return viewModel as MainViewModel
    }

    fun getDeviceId(): String? {
        return deviceId
    }

}