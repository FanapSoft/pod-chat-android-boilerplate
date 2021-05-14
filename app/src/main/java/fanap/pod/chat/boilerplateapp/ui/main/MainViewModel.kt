package fanap.pod.chat.boilerplateapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fanap.podchat.chat.assistant.model.AssistantVo
import com.fanap.podchat.model.ChatResponse
import fanap.pod.chat.boilerplateapp.App
import fanap.pod.chat.boilerplateapp.data.AppDataManager
import fanap.pod.chat.boilerplateapp.data.chat.ChatCallBackHelper
import io.reactivex.disposables.CompositeDisposable

class MainViewModel : ChatCallBackHelper, ViewModelAdapter, ViewModel() {

    private var dataManager: AppDataManager = App.factory.getAppDataManager()
    private var listener: MainViewListener? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    private val _loginState = MutableLiveData<Boolean>()
    val loginState: LiveData<Boolean> = _loginState

    init {
        dataManager.addListener(this)
        mCompositeDisposable = CompositeDisposable()
        if (dataManager.getLoginState()) {
            setLogin(true)
        } else
            setLogin(false)
    }

    fun setViewModelListener(mListener: MainViewListener) {
        listener = mListener
    }

    override fun onAssistantBlocked(response: ChatResponse<MutableList<AssistantVo>>?) {
        super.onAssistantBlocked(response)
        listener?.onAssistantBlocked(response)
    }

    override fun connect() {
        dataManager.connectChat()
    }

    override fun close() {
        dataManager.destroy()
        mCompositeDisposable?.dispose()
    }

    override fun checkLogin() {
        setLogin(true)
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