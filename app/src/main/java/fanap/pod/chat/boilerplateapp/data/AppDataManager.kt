package fanap.pod.chat.boilerplateapp.data

import fanap.pod.chat.boilerplateapp.data.chat.AppChatHelper
import fanap.pod.chat.boilerplateapp.data.chat.ChatCallBackHelper
import fanap.pod.chat.boilerplateapp.data.prefs.AppPreferencesHelper
import fanap.pod.chat.boilerplateapp.data.remote.AppApiHelper

class AppDataManager(
    private val api: AppApiHelper,
    private val prefs: AppPreferencesHelper,
    private val chatHelper: AppChatHelper
) : DataManager {

    override fun addListener(listener: ChatCallBackHelper) {
        chatHelper.addListener(listener)
    }

    override fun connectChat() {
        chatHelper.connectChat()
    }

    override fun destroy() {
        chatHelper.destroy()
    }

}