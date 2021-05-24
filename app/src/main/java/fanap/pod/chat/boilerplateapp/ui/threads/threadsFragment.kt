package fanap.pod.chat.boilerplateapp.ui.threads

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fanap.podchat.requestobject.RequestThread
import fanap.pod.chat.boilerplateapp.R
import fanap.pod.chat.boilerplateapp.factory.ViewModelFactory
import fanap.pod.chat.boilerplateapp.ui.main.MainViewListener
import fanap.pod.chat.boilerplateapp.ui.main.MainViewModel
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class threadsFragment : Fragment(), MainViewListener {

    private lateinit var loading: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private var columnCount = 1
    private lateinit var mainViewModel: MainViewModel
    private var mAdapter: ThreadItemRecyclerViewAdapter = ThreadItemRecyclerViewAdapter(
        ArrayList()
    )

    private var chatReady: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_threads_item, container, false)

        loading = view.findViewById<ProgressBar>(R.id.loading)
        recyclerView = view.findViewById<RecyclerView>(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mAdapter

        setup()
        return view
    }


    fun setup() {
        loading.visibility = View.VISIBLE
        mainViewModel = ViewModelProvider(this, ViewModelFactory())
            .get(MainViewModel::class.java)
        mainViewModel.setViewModelListener(this)
        getThread()
        mainViewModel.observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Log.d("CHAT_TEST_UI", "UI ERROR") }
            .subscribe {
                if (it == "CHAT_READY") {
                    chatReady = true
                    getThread()
                } else {

                }
            }

        mainViewModel.threadsObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Log.d("CHAT_TEST_UI", "UI ERROR") }
            .subscribe {
                loading.visibility = View.INVISIBLE
                if (it.size > 0) {
                    mAdapter.updateList(it)
                }
            }

    }


    private fun getThread() {
        val requestThread = RequestThread
            .Builder()
            .count(10)
            .build()
        mainViewModel.getThread(requestThread)
    }

    override fun connectWithOTP(token: String) {
        TODO("Not yet implemented")
    }


}