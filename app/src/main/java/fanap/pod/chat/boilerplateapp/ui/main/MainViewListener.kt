package fanap.pod.chat.boilerplateapp.ui.main

import com.fanap.podchat.chat.ChatHandler
import com.fanap.podchat.chat.ChatListener
import com.fanap.podchat.requestobject.RequestConnect
import com.fanap.podchat.requestobject.RequestGetHistory
import com.fanap.podchat.requestobject.RequestMessage
import com.fanap.podchat.requestobject.RequestThread


interface MainViewListener:ChatListener
{
    fun connectWithOTP(token: String){}
}
interface ViewModelAdapter{
    fun connect( socketAddress: String,
                 appId: String,
                 severName: String,
                 token: String,
                 ssoHost: String,
                 platformHost: String,
                 fileServer: String,
                 podSpaceUrl: String,
                 typeCode: String?)
    fun getThread(requestThread: RequestThread, listener : ChatHandler?) :String
    fun getThreadHistory(requestGetHistory: RequestGetHistory, listener : ChatHandler?) :String
    fun getUserInfo(listener : ChatHandler?) : String
    fun sendTextMessage(requestTextMessage: RequestMessage, listener : ChatHandler?) : String
    fun checkRefreshToken()
    fun close()
    fun checkLogin()
    fun logOut()
}