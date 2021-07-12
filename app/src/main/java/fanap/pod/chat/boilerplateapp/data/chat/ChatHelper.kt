package fanap.pod.chat.boilerplateapp.data.chat

import com.fanap.podchat.chat.ChatHandler
import com.fanap.podchat.chat.ChatListener
import com.fanap.podchat.model.ResultUserInfo
import com.fanap.podchat.requestobject.RequestConnect
import com.fanap.podchat.requestobject.RequestGetHistory
import com.fanap.podchat.requestobject.RequestMessage
import com.fanap.podchat.requestobject.RequestThread


interface ChatHelper {
    fun connectChat(request : RequestConnect)
    fun getThread(requestThread: RequestThread,listener : ChatHandler?) : String
    fun getThreadHistory(requestGetHistory: RequestGetHistory,listener : ChatHandler?) : String
    fun getUserInfo(listener : ChatHandler?) : String
    fun sendTextMessage(requestTextMessage: RequestMessage,listener : ChatHandler?) : String
    fun clearCache()

    fun destroy()
    fun setToken(token : String)
}