package fanap.pod.chat.boilerplateapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.fanap.podchat.chat.assistant.model.AssistantVo
import com.fanap.podchat.model.ChatResponse
import fanap.pod.chat.boilerplateapp.App
import fanap.pod.chat.boilerplateapp.data.AppDataManager
import fanap.pod.chat.boilerplateapp.data.chat.ChatCallBackHelper

class MainViewModel(application: Application) : ChatCallBackHelper,ViewModelAdapter, AndroidViewModel(application) {

   private var dataManager: AppDataManager
   private var listener: MainViewListener? =null

    init {
        dataManager = App.factory.getAppDataManager(application)
        dataManager.addListener(this)
    }

    fun setViewModelListener(listene: MainViewListener){
        listener= listene
    }

    override fun onAssistantBlocked(response: ChatResponse<MutableList<AssistantVo>>?) {
        super.onAssistantBlocked(response)
        listener?.onAssistantBlocked(response)
    }

    override fun connect() {
       dataManager.connectChat()
    }

    override fun close() {

    }

    override fun sendMessage() {

    }

    override fun getThread() {

    }

    override fun getThreadParticipant() {

    }

}