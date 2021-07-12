package fanap.pod.chat.boilerplateapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fanap.podchat.chat.ChatHandler
import com.fanap.podchat.mainmodel.MessageVO
import com.fanap.podchat.mainmodel.Thread
import com.fanap.podchat.model.*
import com.fanap.podchat.requestobject.RequestConnect
import com.fanap.podchat.requestobject.RequestGetHistory
import com.fanap.podchat.requestobject.RequestMessage
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

    // all logic app is in AppDataManager
    private var dataManager: AppDataManager = App.factory.getAppDataManager()
    private var listener: MainViewListener? = null
    private var mCompositeDisposable: CompositeDisposable? = null
    private var chatState: String? = ""
    var threadsObservable: PublishSubject<List<Thread>> = PublishSubject.create()
    var threadUpdatedObservable: PublishSubject<Thread> = PublishSubject.create()


    var threadHistoryObservable: PublishSubject<MutableList<MessageVO>> = PublishSubject.create()
    var newMessageObservable: PublishSubject<MessageVO> = PublishSubject.create()

    var observable: PublishSubject<String> = PublishSubject.create()

    /*
       *     this method for logout acknowledge to views
       *     after logout user should be clear all list and ...
       * */
    var logoutObservable: PublishSubject<Boolean> = PublishSubject.create()
    private val _loginState = MutableLiveData<Boolean>()
    val loginState: LiveData<Boolean> = _loginState


    private val _selectedThread = MutableLiveData<Thread>()
    val selectedThread: LiveData<Thread> = _selectedThread

    private val _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean> = _navigate

    //    private val _errorMessage = MutableLiveData<String?>()
//    val errorMessage: LiveData<String?> = _errorMessage
    var errorMessageObservable: PublishSubject<String> = PublishSubject.create()

    private val _needsGetThreadInMain = MutableLiveData<Boolean>()
    val needsGetThreadInMain: LiveData<Boolean> = _needsGetThreadInMain

    var historyInCache: MutableList<MessageVO> = mutableListOf()
   var lastMessageVOInCach : MessageVO? = null
    init {
        dataManager.addListener(this)
        mCompositeDisposable = CompositeDisposable()
        checkLogin()
    }

    fun setNavigation(state: Boolean) {
        _navigate.value = state
    }


    fun setGetThreadInMain(state : Boolean) {
        _needsGetThreadInMain.value = state
    }

    fun setViewModelListener(mListener: MainViewListener) {
        listener = mListener
    }

    fun selectThread(thread: Thread) {
        _selectedThread.value = thread
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

    override fun getThread(requestThread: RequestThread, listener: ChatHandler?): String {
        return dataManager.getThread(requestThread, listener)
    }

    override fun getThreadHistory(
        requestGetHistory: RequestGetHistory,
        listener: ChatHandler?
    ): String {
        lastMessageVOInCach==null
        historyInCache.clear()
        return dataManager.getThreadHistory(requestGetHistory, listener)
    }

    override fun getUserInfo(listener: ChatHandler?): String {
        return dataManager.getUserInfo(listener)
    }

    override fun sendTextMessage(
        requestTextMessage: RequestMessage,
        listener: ChatHandler?
    ): String {
        return dataManager.sendTextMessage(requestTextMessage,listener)
    }

    //when user leaved the app.
    //we should be clear all data and destroy all abservables
    override fun close() {
        dataManager.destroy()
        mCompositeDisposable?.dispose()
    }

    //check login state if the user logined already we should connect to chat server
    override fun checkLogin() {
        if (dataManager.getLoginState()) {
            setLogin(true)
            getUserInfo(null)
        } else
            setLogin(false)
    }

    // if user is login we lets go to connect to chat
    override fun checkRefreshToken() {
        if (dataManager.getRefreshToken() != null) {
            refreshToken(dataManager.getRefreshToken()!!)
        } else
            setLogin(false)
    }

    override fun onNewMessage(content: String?, response: ChatResponse<ResultNewMessage>?) {
        super.onNewMessage(content, response)
        Log.e("onNewMessage", "onNewMessage: ${response?.result?.messageVO?.message}" )
        if(lastMessageVOInCach==null || response?.result?.messageVO?.uniqueId != lastMessageVOInCach?.uniqueId) {
            lastMessageVOInCach = response?.result?.messageVO
            newMessageObservable.onNext(response?.result?.messageVO)
        }
    }

    //this method observe to changes in chat state
    //and then send state to view
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

    var lastId: Long = -1
    var lastMsg: Long = -1
    override fun onGetThread(content: String?, thread: ChatResponse<ResultThreads>?) {
        super.onGetThread(content, thread)
        if (thread?.result?.threads!!.size == 0 && thread?.result?.contentCount != 0L)
            threadsObservable.onNext(thread?.result?.threads)
        else if (lastId != thread?.result?.threads?.get(0)?.id) {
            lastId = thread?.result?.threads?.get(0)?.id!!
            threadsObservable.onNext(thread?.result?.threads)
        } else {
            threadsObservable.onNext(emptyList())
        }

    }

    override fun onGetHistory(content: String?, history: ChatResponse<ResultHistory>?) {
        super.onGetHistory(content, history)
        if (history?.result?.history?.isEmpty() == true) {
            threadHistoryObservable.onNext(mutableListOf())
            return
        }
        if (historyInCache.isEmpty() || (history?.result?.history?.containsAll(historyInCache) == false && !historyInCache.containsAll(
                history.result.history!!
            ))
        ) {
            historyInCache.addAll(history?.result?.history!!)
            threadHistoryObservable.onNext(historyInCache)
        }
    }

    override fun onUserInfo(content: String?, response: ChatResponse<ResultUserInfo>?) {
        super.onUserInfo(content, response)

        App.user = response?.result?.user
    }

    override fun onThreadInfoUpdated(content: String?, response: ChatResponse<ResultThread>?) {
        super.onThreadInfoUpdated(content, response)
        threadUpdatedObservable.onNext(response?.result?.thread)
    }

    override fun onError(content: String?, error: ErrorOutPut?) {
        super.onError(content, error)
        Log.e("TAG", "onError: ${error?.errorCode}  mmmmmmmmm   : ${error?.errorMessage}")
        if (error!!.errorCode == 21L) {
            checkRefreshToken()
        }
        errorMessageObservable.onNext(error.uniqueId)
    }


    //refresh token implementation
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

    //after arrived aouth tokens
    //we save tokens in our local storage and then connect to chat server
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
        dataManager.clearCache()
        logoutObservable.onNext(true)
        setLogin(false)
    }

    fun setLogin(state: Boolean) {
        _loginState.value = state
    }


}