package fanap.pod.chat.boilerplateapp.data

import fanap.pod.chat.boilerplateapp.data.chat.ChatCallBackHelper
import fanap.pod.chat.boilerplateapp.data.chat.ChatHelper
import fanap.pod.chat.boilerplateapp.data.prefs.PreferencesHelper
import fanap.pod.chat.boilerplateapp.data.remote.ApiHelper

interface DataManager : PreferencesHelper, ApiHelper, ChatHelper {
    fun addListener(listener: ChatCallBackHelper)
}