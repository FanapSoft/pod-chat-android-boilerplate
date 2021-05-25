package fanap.pod.chat.boilerplateapp.data.chat


import android.util.Log
import com.fanap.podchat.chat.Chat
import com.fanap.podchat.chat.ChatAdapter
import com.fanap.podchat.chat.thread.public_thread.ResultJoinPublicThread
import com.fanap.podchat.model.*
import com.fanap.podchat.requestobject.RequestConnect
import com.fanap.podchat.requestobject.RequestThread
import com.fanap.podchat.util.NetworkUtils.NetworkPingSender


class AppChatHelper(val chat: Chat):ChatHelper, ChatAdapter(),Chat.IClearMessageCache {

    init {
        val networkStateConfig = NetworkPingSender.NetworkStateConfig()
            .setHostName("msg.pod.ir")
            .setPort(443)
            .setDisConnectionThreshold(2)
            .setInterval(5000)
            .setConnectTimeout(3500)
            .build()

        chat.setNetworkStateConfig(networkStateConfig)

        chat.isLoggable(true)

        chat.isSentryLogActive(true)

        chat.isCacheables(true)

        chat.isSentryResponseLogActive(true)

        chat.addListener(this)
    }

    private var listeners: MutableList<ChatCallBackHelper> = mutableListOf()

    fun addListener(listener:ChatCallBackHelper){
        listeners.add(listener)
    }

    override fun connectChat(request :RequestConnect) {
        chat.connect(request)
    }

    override fun getThread(requestThread: RequestThread) {
        chat.getThreads(requestThread,null)
    }

    override fun clearCache() {
        chat.clearCacheDatabase(this)
    }

    override fun destroy() {
        clone()
    }

    override fun setToken(token: String) {
        chat.setToken(token)
    }

    override fun onChatState(state: String?) {
        super.onChatState(state)
        if(!listeners.isEmpty())
            for (i in listeners)
                i.onChatState(state)
    }


    override fun onGetThread(content: String?, thread: ChatResponse<ResultThreads>?) {
        super.onGetThread(content, thread)
        if(!listeners.isEmpty())
            for (i in listeners)
                i.onGetThread(content,thread)
    }

    override fun onGetContacts(content: String?, outPutContact: ChatResponse<ResultContact>?) {
        super.onGetContacts(content, outPutContact)
        if(!listeners.isEmpty())
            for (i in listeners)
                i.onGetContacts(content,outPutContact)
    }

    override fun onGetHistory(content: String?, history: ChatResponse<ResultHistory>?) {
        super.onGetHistory(content, history)
        if(!listeners.isEmpty())
            for (i in listeners)
                i.onGetHistory(content,history)
    }

    override fun onCreateThread(response: ChatResponse<ResultThread>?) {
        super.onCreateThread(response)
        if(!listeners.isEmpty())
            for (i in listeners)
                i.onCreateThread(response)
    }


    override fun onGetThreadParticipant(outPutParticipant: ChatResponse<ResultParticipant>?) {
        super.onGetThreadParticipant(outPutParticipant)
        if(!listeners.isEmpty())
            for (i in listeners)
                i.onGetThreadParticipant(outPutParticipant)
    }

    override fun onGetThreadParticipant(
        content: String?,
        outPutParticipant: ChatResponse<ResultParticipant>?
    ) {
        super.onGetThreadParticipant(content, outPutParticipant)
        if(!listeners.isEmpty())
            for (i in listeners)
                i.onGetThreadParticipant(content,outPutParticipant)
    }

    override fun onError(content: String?, error: ErrorOutPut?) {
        super.onError(content, error)
        if(!listeners.isEmpty())
            for (i in listeners)
                i.onError(content,error)
    }

    override fun onJoinPublicThread(response: ChatResponse<ResultJoinPublicThread>?) {
        super.onJoinPublicThread(response)
        if(!listeners.isEmpty())
            for (i in listeners)
                i.onJoinPublicThread(response)
    }

    override fun onThreadLeaveParticipant(
        content: String?,
        response: ChatResponse<ResultLeaveThread>?
    ) {
        super.onThreadLeaveParticipant(content, response)
        if(!listeners.isEmpty())
            for (i in listeners)
                i.onThreadLeaveParticipant(content, response)
    }

    override fun onSent(content: String?, chatResponse: ChatResponse<ResultMessage>?) {
        super.onSent(content, chatResponse)
        if(!listeners.isEmpty())
            for (i in listeners)
                i.onSent(content, chatResponse)
    }

    override fun onDeliver(content: String?, chatResponse: ChatResponse<ResultMessage>?) {
        super.onDeliver(content, chatResponse)
        if(!listeners.isEmpty())
            for (i in listeners)
                i.onDeliver(content, chatResponse)
    }

    private fun clone() {
        listeners.clear()
        chat.closeChat()
        chat.killChat()

    }

    override fun onCacheDatabaseCleared() {
        Log.e("TAG", "onCacheDatabaseCleared: " )
    }

    override fun onExceptionOccurred(cause: String?) {
        Log.e("TAG", "onExceptionOccurred: ")
    }
}