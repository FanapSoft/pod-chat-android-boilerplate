package fanap.pod.chat.boilerplateapp.ui.main

import com.fanap.podchat.chat.ChatListener
import com.fanap.podchat.requestobject.RequestConnect
import com.fanap.podchat.requestobject.RequestThread


interface MainViewListener:ChatListener
{
    fun connectWithOTP(token: String)
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
    fun getThread(requestThread: RequestThread)
    fun checkRefreshToken()
    fun close()
    fun checkLogin()
    fun logOut()
}