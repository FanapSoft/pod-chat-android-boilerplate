package fanap.pod.chat.boilerplateapp.factory

import android.content.Context
import com.fanap.podchat.chat.Chat
import fanap.pod.chat.boilerplateapp.App
import fanap.pod.chat.boilerplateapp.data.AppDataManager
import fanap.pod.chat.boilerplateapp.data.chat.AppChatHelper
import fanap.pod.chat.boilerplateapp.data.prefs.AppPreferencesHelper
import fanap.pod.chat.boilerplateapp.data.remote.AppApiHelper

class Factory {
    private var api: AppApiHelper? = null
    private var prefs: AppPreferencesHelper? = null
    private var chatHelper: AppChatHelper? = null
    private var dataManager: AppDataManager? = null

    fun getAppDataManager(): AppDataManager {
        if (dataManager == null)
            dataManager = App.getInstance().applicationContext?.let { prepareAppChatHelper(it) }?.let {
                AppDataManager(
                    prepareApiHelper(),
                    prepareAppPreferencesHelper(),
                    it
                )
            }
        return dataManager as AppDataManager
    }

    private fun prepareApiHelper(): AppApiHelper {
        if (api == null)
            api = AppApiHelper()
        return api as AppApiHelper
    }

    private fun prepareAppPreferencesHelper(): AppPreferencesHelper {
        if (prefs == null)
            prefs = App.getInstance().applicationContext?.let { AppPreferencesHelper(it,"mypref") }
        return prefs as AppPreferencesHelper
    }

    private fun prepareAppChatHelper(context: Context): AppChatHelper {
        if (chatHelper == null)
            chatHelper = AppChatHelper(Chat.init(context))
        return chatHelper as AppChatHelper
    }

    fun clear() {
        api = null
        prefs = null
        chatHelper = null
        dataManager = null
    }
}