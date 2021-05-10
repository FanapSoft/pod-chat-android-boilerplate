package fanap.pod.chat.boilerplateapp.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import fanap.pod.chat.boilerplateapp.ui.MainViewListener
import fanap.pod.chat.boilerplateapp.ui.MainViewModel

open class BaseActivity : AppCompatActivity(), MainViewListener,BaseView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
    }

    override fun setup() {

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }


}