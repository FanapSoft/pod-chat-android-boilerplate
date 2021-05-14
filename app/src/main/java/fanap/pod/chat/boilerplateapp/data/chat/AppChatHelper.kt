package fanap.pod.chat.boilerplateapp.data.chat

import com.fanap.podchat.chat.Chat
import com.fanap.podchat.chat.ChatAdapter
import com.fanap.podchat.chat.assistant.model.AssistantVo
import com.fanap.podchat.model.ChatResponse

class AppChatHelper(val chat: Chat):ChatHelper, ChatAdapter() {

    private var listeners: MutableList<ChatCallBackHelper> = mutableListOf()

    fun addListener(listener:ChatCallBackHelper){
        listeners.add(listener)
    }

    override fun connectChat() {
        chat.setListener(this);
      //  chat.connect(null)
    }

    override fun destroy() {
        clone()
    }

    override fun onAssistantBlocks(response: ChatResponse<MutableList<AssistantVo>>?) {
        super.onAssistantBlocks(response)
        callListener(response)
    }

    private fun callListener(response: ChatResponse<MutableList<AssistantVo>>?){
        if(!listeners.isEmpty())
            for (i in listeners)
                i.onAssistantBlocks(response)
    }

    private fun clone() {
        listeners.clear()
        chat.closeChat()
        chat.killChat()

    }
}