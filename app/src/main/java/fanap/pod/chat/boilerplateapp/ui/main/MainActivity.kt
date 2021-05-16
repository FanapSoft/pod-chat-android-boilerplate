package fanap.pod.chat.boilerplateapp.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fanap.podchat.requestobject.RequestThread
import fanap.pod.chat.boilerplateapp.App
import fanap.pod.chat.boilerplateapp.R
import fanap.pod.chat.boilerplateapp.databinding.ActivityMainBinding
import fanap.pod.chat.boilerplateapp.factory.ViewModelFactory
import fanap.pod.chat.boilerplateapp.ui.login.LoginActivity
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class MainActivity : AppCompatActivity(), MainViewListener {
    private val LAUNCH_LOGIN_ACTIVITY = 1
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var toolbar: Toolbar
    private var mainServer = false
    private var chatReady: Boolean = false
    private val ssoHost = App.getInstance().getString(R.string.sandbox_ssoHost)
    private val serverName = "chat-server"
    private val sand_appId = "POD-Chat"
    private val typeCode: String? = "default"


    /**
     *
     * SandBox Config:
     *
     */

    private val sandSocketAddress = App.getInstance().getString(R.string.sandbox_socketAddress)
    private val sandPlatformHost = App.getInstance().getString(R.string.sandbox_platformHost)
    private val sandFileServer = App.getInstance().getString(R.string.sandbox_fileServer)


    /**
     *
     * MainServer Config:
     *
     *
     */


    private val main_socketAddress = App.getInstance().getString(R.string.socketAddress)
    private val main_platformHost = App.getInstance().getString(R.string.platformHost)
    private val main_fileServer = App.getInstance().getString(R.string.fileServer)
    private val podSpaceUrl = App.getInstance().getString(R.string.podspace_file_server_main)


    private var TOKEN = "7d46feef96d8476d930e7ea749333dad"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    private fun setup() {
        toolbar = binding.topToolbar
        mainViewModel = ViewModelProvider(this, ViewModelFactory())
            .get(MainViewModel::class.java)
        mainViewModel.setViewModelListener(this)
        mainViewModel.loginState.observe(this@MainActivity, Observer {
            val loginState = it ?: return@Observer
            if (!loginState) {
                startActivityForResult(
                    Intent(this, LoginActivity::class.java),
                    LAUNCH_LOGIN_ACTIVITY
                )
            } else
                mainViewModel.checkRefreshToken()
        })

        mainViewModel.observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Log.d("CHAT_TEST_UI", "UI ERROR") }
            .subscribe {
                if (it == "CHAT_READY") {
                    chatReady = true
                    toolbar.setTitle("connected")
                    getThread()
                } else {
                    toolbar.setTitle("connecting ...")
                }
            }

        toolbar?.setNavigationOnClickListener {
            Log.e("TAG", "setup: ")
        }

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.itemLogout -> {
                    mainViewModel.logOut()
                }
                R.id.itemSandBoxServer -> {
                    Log.e("TAG", "itemSandBoxServer: ")
                }
                R.id.itemMainServer -> {
                    Log.e("TAG", "itemMainServer: ")
                }
            }
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_LOGIN_ACTIVITY)
            if (resultCode == Activity.RESULT_CANCELED)
                finish()
            else
                mainViewModel.checkRefreshToken()

    }

    override fun onDestroy() {
        super.onDestroy()
        clearResource()
    }

    private fun clearResource() {
        App.factory.clear()
        mainViewModel.close()
    }


    override fun connectWithOTP(token: String) {
        TOKEN = token
        connect()
    }

    private fun getThread() {
        val requestThread = RequestThread
            .Builder()
            .build()
        mainViewModel.getThread(requestThread)
    }

    fun connect() {
        if (mainServer) {
            mainViewModel.connect(
                main_socketAddress,
                sand_appId,
                serverName,
                TOKEN,
                ssoHost,
                main_platformHost,
                main_fileServer,
                podSpaceUrl,
                typeCode
            )
        } else {
            //sandBox
            mainViewModel.connect(
                sandSocketAddress,
                sand_appId,
                serverName,
                TOKEN,
                ssoHost,
                sandPlatformHost,
                sandFileServer,
                podSpaceUrl,
                typeCode
            )
        }
    }

}