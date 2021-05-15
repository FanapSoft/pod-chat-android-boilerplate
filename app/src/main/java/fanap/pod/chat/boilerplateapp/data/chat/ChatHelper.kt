package fanap.pod.chat.boilerplateapp.data.chat

import com.fanap.podchat.chat.ChatListener
import com.fanap.podchat.requestobject.RequestConnect
import com.fanap.podchat.requestobject.RequestThread


interface ChatHelper {
    fun connectChat(request : RequestConnect)
    fun getThread(requestThread: RequestThread)

    fun destroy()
    fun setToken(token : String)
}