package fanap.pod.chat.boilerplateapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fanap.podchat.chat.assistant.model.AssistantVo
import com.fanap.podchat.model.ChatResponse
import fanap.pod.chat.boilerplateapp.App
import fanap.pod.chat.boilerplateapp.R
import fanap.pod.chat.boilerplateapp.data.AppDataManager
import fanap.pod.chat.boilerplateapp.data.chat.ChatCallBackHelper
import fanap.pod.chat.boilerplateapp.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun setup() {
        super.setup()
        mainViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                .create(MainViewModel::class.java)
        mainViewModel.setViewModelListener(this)
        mainViewModel.connect()
    }

    override fun onAssistantBlocks(response: ChatResponse<MutableList<AssistantVo>>?) {
        super.onAssistantBlocks(response)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearResource()
    }

    fun clearResource() {
        App.factory.clear()
    }
}