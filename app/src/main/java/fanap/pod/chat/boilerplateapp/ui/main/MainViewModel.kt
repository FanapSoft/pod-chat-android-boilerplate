package fanap.pod.chat.boilerplateapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fanap.podchat.chat.assistant.model.AssistantVo
import com.fanap.podchat.model.ChatResponse
import com.fanap.podchat.model.ErrorOutPut
import com.fanap.podchat.model.ResultThreads
import com.fanap.podchat.requestobject.RequestConnect
import com.fanap.podchat.requestobject.RequestThread
import com.fanap.podchat.util.ChatStateType
import fanap.pod.chat.boilerplateapp.App
import fanap.pod.chat.boilerplateapp.data.AppDataManager
import fanap.pod.chat.boilerplateapp.data.chat.ChatCallBackHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import rx.exceptions.MissingBackpressureException
import rx.exceptions.OnErrorNotImplementedException
import rx.subjects.PublishSubject

class MainViewModel : ChatCallBackHelper, ViewModelAdapter, ViewModel() {

    private var dataManager: AppDataManager = App.factory.getAppDataManager()
    private var listener: MainViewListener? = null
    private var mCompositeDisposable: CompositeDisposable? = null
    private var chatState: String? = ""
    var observable: PublishSubject<String> = PublishSubject.create()
    private val _loginState = MutableLiveData<Boolean>()
    val loginState: LiveData<Boolean> = _loginState

    init {
        dataManager.addListener(this)
        mCompositeDisposable = CompositeDisposable()
        checkLogin()
    }

    fun setViewModelListener(mListener: MainViewListener) {
        listener = mListener
    }


    override fun connect(
        socketAddress: String,
        appId: String,
        severName: String,
        token: String,
        ssoHost: String,
        platformHost: String,
        fileServer: String,
        podSpaceUrl: String,
        typeCode: String?
    ) {
        val rb: RequestConnect = RequestConnect.Builder(
            socketAddress,
            appId,
            severName,
            token,
            ssoHost,
            platformHost,
            fileServer,
            podSpaceUrl
        ).build()
        dataManager.connectChat(rb)
    }

    override fun getThread(requestThread: RequestThread) {
        dataManager.getThread(requestThread)
    }

    override fun close() {
        dataManager.destroy()
        mCompositeDisposable?.dispose()
    }

    override fun checkLogin() {
        if (dataManager.getLoginState()) {
            setLogin(true)
        } else
            setLogin(false)
    }

    override fun checkRefreshToken() {
        if (dataManager.getRefreshToken() != null) {
            refreshToken(dataManager.getRefreshToken()!!)
        } else
            setLogin(false)
    }

    override fun onChatState(state: String?) {
        super.onChatState(state)

        try {
            observable.onNext(state)
            chatState = state
        } catch (e: Exception) {
            observable.onError(e)
        } catch (be: MissingBackpressureException) {
            observable.onError(be)
        } catch (il: IllegalStateException) {
            observable.onError(il)
        } catch (oe: OnErrorNotImplementedException) {
            observable.onError(oe)
        }

    }

    override fun onGetThread(content: String?, thread: ChatResponse<ResultThreads>?) {
        super.onGetThread(content, thread)
    }

    override fun onError(content: String?, error: ErrorOutPut?) {
        super.onError(content, error)
        Log.e("TAG", "onError: "+error?.errorCode )
        if (error!!.errorCode == 21L) {
            checkRefreshToken()
        }
    }

    fun refreshToken(refreshToken: String) {
        mCompositeDisposable?.add(dataManager.refreshToken(refreshToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                dataManager.saveRefreshToken(it?.result?.refreshToken)
                it?.result?.accessToken?.let { it1 -> handleTokenReceived(it1) }
            }
            ) {
                Log.e("mTAG", "throwable: ")
            })
    }

    private fun handleTokenReceived(token: String) {
        if (chatState!! == ChatStateType.ChatSateConstant.ASYNC_READY) {
            dataManager.setToken(token)
        } else {
            listener?.connectWithOTP(token)
        }
    }

    override fun logOut() {
        dataManager.saveRefreshToken(null)
        dataManager.changeLoginState(false)
        setLogin(false)
    }

    fun setLogin(state: Boolean) {
        _loginState.value = state
    }


}