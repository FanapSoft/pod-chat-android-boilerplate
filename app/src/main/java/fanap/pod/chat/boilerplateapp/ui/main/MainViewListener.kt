package fanap.pod.chat.boilerplateapp.ui.main

import com.fanap.podchat.chat.ChatListener


interface MainViewListener:ChatListener
interface ViewModelAdapter{
    fun connect()
    fun close()
    fun checkLogin()
    fun logOut()
}