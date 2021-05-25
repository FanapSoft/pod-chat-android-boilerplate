package fanap.pod.chat.boilerplateapp.ui.threads

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fanap.podchat.mainmodel.Thread
import com.fanap.podchat.requestobject.RequestThread
import fanap.pod.chat.boilerplateapp.R
import fanap.pod.chat.boilerplateapp.factory.ViewModelFactory
import fanap.pod.chat.boilerplateapp.ui.main.MainViewModel
import fanap.pod.chat.boilerplateapp.utils.Utility
import fanap.pod.chat.boilerplateapp.utils.Utility.showProgressBar
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class ThreadsFragment : Fragment() {

    //views

    private lateinit var recyclerView: RecyclerView
    private lateinit var nested: NestedScrollView
    private lateinit var mainViewModel: MainViewModel


    //pagination configs
    private var isLoading = false
    private var offset: Long = 0
    private var count: Long = 50

    //adapter for show threads
    private var mAdapter: ThreadItemRecyclerViewAdapter = ThreadItemRecyclerViewAdapter(
        ArrayList()
    )

    //state of chat connection
    private var chatReady: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_threads_item, container, false)
        initViews(view)
        return view
    }

    //initialize views
    private fun initViews(view: View) {
        nested = view.findViewById(R.id.nested)
        recyclerView = view.findViewById(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mAdapter
        setup()

        getScrollView().setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
            if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) &&
                scrollY > oldScrollY
            ) {
                checkLoadMore()
            }
        })
    }

    private fun checkLoadMore() {
        val mLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        val visibleItemCount = mLayoutManager.childCount
        val totalItemCount = mLayoutManager.itemCount
        val pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition()

        if (!isLoading) {
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                offset += 50
                handler.sendMessage(Message())
                Log.e("TAG", "checkLoadMore: ")
            }
        }
    }

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            getThread()
        }
    }

    private fun getScrollView(): NestedScrollView {
        return nested
    }

    // prepare view model and get thread data for lazy loading
    // sync thread data from server ands cache
    //set observables for check chat connection state , update threads, logout state
    private fun setup() {
        context?.showProgressBar()
        mainViewModel = ViewModelProvider(this, ViewModelFactory())
            .get(MainViewModel::class.java)
        getThread()
        mainViewModel.observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Log.d("CHAT_TEST_UI", "UI ERROR") }
            .subscribe {
                if (it == "CHAT_READY") {
                    chatReady = true
                    getThread()
                }
            }
        mainViewModel.logoutObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Log.d("CHAT_TEST_UI", "UI ERROR") }
            .subscribe {
                if (it) {
                    mAdapter.clearList()
                }
            }
        mainViewModel.threadsObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Log.d("CHAT_TEST_UI", "UI ERROR") }
            .subscribe {
                Utility.hideProgressBar()
                if (it.isNotEmpty()) {
                    mAdapter.updateList(it as MutableList<Thread>)
                }
            }
    }

    //prepare getThread request and send it to chat server for update threads
    //call backs in mainViewModel.threadsObservable
    private fun getThread() {
//        addTread()
        val requestThread = RequestThread
            .Builder()
            .offset(offset + 1)
            .count(count)
            .build()
        mainViewModel.getThread(requestThread)


    }

}

