package fanap.pod.chat.boilerplateapp.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.fanap.podchat.chat.ChatHandler
import com.fanap.podchat.model.ChatResponse
import com.fanap.podchat.model.ResultThreads
import com.fanap.podchat.requestobject.RequestThread
import fanap.pod.chat.boilerplateapp.App
import fanap.pod.chat.boilerplateapp.R
import fanap.pod.chat.boilerplateapp.databinding.ActivityMainBinding
import fanap.pod.chat.boilerplateapp.ui.login.LoginActivity
import fanap.pod.chat.boilerplateapp.utils.Utility
import fanap.pod.chat.boilerplateapp.utils.Utility.showProgressBar
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class MainActivity : AppCompatActivity(), MainViewListener {
    private val LAUNCH_LOGIN_ACTIVITY = 1
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var toolbar: Toolbar
    private lateinit var customToolbar: ConstraintLayout
    private lateinit var customTitle: TextView
    private var mainServer = true
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
    private var isMain = true

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setup() {
        toolbar = binding.topToolbar
        customToolbar = binding.customToolbar
        customTitle = binding.toolbarTitle

        mainViewModel = App.getInstance().getViewModel()
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
                chatReady = it == "CHAT_READY"
                updateView()
            }

        toolbar?.setNavigationOnClickListener {
            if (!isMain) {
                onBackPressed()
            }

        }

        mainViewModel.selectedThread.observe(this, Observer {
            val mThread = it ?: return@Observer
            customTitle.setText(mThread.title)
        })

        mainViewModel.navigate.observe(this@MainActivity, Observer {
            val isNavigate = it ?: return@Observer
            isMain = isNavigate
            if (!isMain) {
                customToolbar.visibility = View.VISIBLE
                toolbar.setNavigationIcon(getDrawable(R.drawable.ic_baseline_arrow_back_24));
            } else {
                customToolbar.visibility = View.GONE
                toolbar.setNavigationIcon(getDrawable(R.drawable.ic_baseline_menu_24));
            }
        })

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

    fun updateView() {
        if (chatReady) {
            toolbar.setTitle("Podchat")
        } else {
            toolbar.setTitle("Waiting for ...")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_LOGIN_ACTIVITY)
            if (resultCode == Activity.RESULT_CANCELED)
                finish()
            else {
                this.showProgressBar()
                mainViewModel.setGetThreadInMain(true)
                mainViewModel.checkRefreshToken()
            }

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