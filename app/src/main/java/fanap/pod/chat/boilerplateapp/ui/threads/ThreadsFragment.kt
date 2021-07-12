package fanap.pod.chat.boilerplateapp.ui.threads

import android.icu.util.TimeUnit
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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fanap.podchat.mainmodel.Thread
import com.fanap.podchat.requestobject.RequestThread
import fanap.pod.chat.boilerplateapp.App
import fanap.pod.chat.boilerplateapp.R
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
    var uniqId: String = ""

    private lateinit var recyclerView: RecyclerView
    private lateinit var nested: NestedScrollView
    private lateinit var mainViewModel: MainViewModel

    private var offset: Long = 0
    private var count: Long = 10

    var isLoading = false
    var isEnd = false

    //adapter for show threads
    private var mAdapter: ThreadItemRecyclerViewAdapter = ThreadItemRecyclerViewAdapter(
        ArrayList()
    )

    //state of chat connection
    private var chatReady: Boolean = false
    private var needsUpdate = false
    var isNavigate = false

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
        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
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

        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
            if (!isEnd)
                offset += 10
            handler.sendMessage(Message())
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

        mAdapter.listener = object : AdapterCallBack {
            override fun onItemClick(thread: Thread) {
                navigateToHistory(thread)
            }
            override fun onAddNew(thread: Thread) {
                offset = +1
            }
        }

        mainViewModel =  App.getInstance().getViewModel()

        mainViewModel.observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Log.d("CHAT_TEST_UI", "UI ERROR") }
            .subscribe {
                chatReady = it == "CHAT_READY"
                if (needsUpdate && chatReady) {
                    getThreadAfterLogin()
                    mainViewModel.setGetThreadInMain(false)
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

                if (isLoading) {
                    isLoading = false
                    if (it.isEmpty()) {
                        Utility.hideProgressBar()
                        return@subscribe
                    }

                    isEnd = it.size == 0
                    mAdapter.updateList(it as MutableList<Thread>)
                    Handler(Looper.getMainLooper()).post {
                        mAdapter.notifyItemRangeInserted(
                            offset.toInt(),
                            mAdapter.itemCount - 1
                        )
                    }
                }
                Utility.hideProgressBar()
            }

        mainViewModel.loginState.observe(viewLifecycleOwner, Observer {
            val loginState = it ?: return@Observer
            if (isNavigate) {
                isNavigate = false
                return@Observer
            }

            if (loginState && !needsUpdate)
                getThread()
        })

        mainViewModel.needsGetThreadInMain.observe(viewLifecycleOwner, Observer {
            needsUpdate = it ?: return@Observer
        })

        mainViewModel.errorMessageObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Log.d("CHAT_TEST_UI", "UI ERROR") }
            .delay(2500, java.util.concurrent.TimeUnit.MILLISECONDS)
            .subscribe {
                if (uniqId == it)
                    if (isLoading ) {
                        isLoading = false
                        Utility.hideProgressBar()
                    }
            }

        mainViewModel.threadUpdatedObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Log.d("CHAT_TEST_UI", "UI ERROR") }
            .subscribe {
                mAdapter.updateItem(it)
            }
    }


    fun navigateToHistory(thread: Thread) {
        mainViewModel.setNavigation(false)
        mainViewModel.selectThread(thread)
        findNavController(this).navigate(R.id.action_threadsFragment_to_itemFragment)
        isNavigate = true
    }

    //prepare getThread request and send it to chat server for update threads
    //call backs in mainViewModel.threadsObservable
    private fun getThread() {
        if (!isLoading) {
            context?.showProgressBar()
            isLoading = true
            val requestThread = RequestThread
                .Builder()
                .offset(offset)
                .count(count)
                .build()
            uniqId = mainViewModel.getThread(requestThread, null)
        }
    }

    //prepare getThread request and send it to chat server for update threads
    //call backs in mainViewModel.threadsObservable
    private fun getThreadAfterLogin() {
        if (!isLoading) {
            isLoading = true
            val requestThread = RequestThread
                .Builder()
                .offset(offset)
                .count(count)
                .build()
            mainViewModel.getThread(requestThread, null)
        }
    }
}

