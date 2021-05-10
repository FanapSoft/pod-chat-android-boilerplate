package fanap.pod.chat.boilerplateapp.ui

import com.fanap.podchat.chat.ChatListener


interface MainViewListener:ChatListener {

}
interface ViewModelAdapter{
   fun connect()
   fun close()
   fun sendMessage()
   fun getThread()
   fun getThreadParticipant()
}