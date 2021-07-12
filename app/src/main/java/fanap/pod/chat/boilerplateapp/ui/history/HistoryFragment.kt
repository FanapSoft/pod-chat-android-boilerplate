package fanap.pod.chat.boilerplateapp.ui.history

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fanap.podasync.model.AsyncMessageType
import com.fanap.podchat.mainmodel.ChatMessage
import com.fanap.podchat.mainmodel.Thread
import com.fanap.podchat.mainmodel.UserInfo
import com.fanap.podchat.requestobject.RequestGetHistory
import com.fanap.podchat.requestobject.RequestMessage
import com.fanap.podchat.util.TextMessageType
import fanap.pod.chat.boilerplateapp.App
import fanap.pod.chat.boilerplateapp.R
import fanap.pod.chat.boilerplateapp.ui.main.MainViewModel
import fanap.pod.chat.boilerplateapp.utils.Utility
import fanap.pod.chat.boilerplateapp.utils.Utility.showProgressBar
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * A fragment representing a list of Items.
 */
class HistoryFragment : Fragment() {
    var thread: Thread? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageButton: ImageButton
    private lateinit var editTextTextPersonName: EditText
    private lateinit var nested: NestedScrollView
    private lateinit var mainViewModel: MainViewModel

    private var offset: Long = 0
    private var count: Long = 15
    private var chatReady: Boolean = false
    var isLoading = false
    var isEnd = false
    var uniqId: String = ""
    var user: UserInfo? = null

    private var mAdapter: HistoryItemRecyclerViewAdapter =
        HistoryItemRecyclerViewAdapter(ArrayList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.history_item_list, container, false)

        initViews(view)
        setup()
        return view
    }

    //initialize views
    private fun initViews(view: View) {
        offset = 0
        count = 15
        nested = view.findViewById(R.id.nested)
        editTextTextPersonName = view.findViewById(R.id.editTextTextPersonName)
        imageButton = view.findViewById(R.id.imageButton)
        recyclerView = view.findViewById(R.id.list)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true


        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = mAdapter


        setup()
        getScrollView().setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
            if (scrollY == 0) {
                if (!isEnd)
                    offset += 7
                handler.sendMessage(Message())
            }
        })

    }

    private fun getScrollView(): NestedScrollView {
        return nested
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.setNavigation(true)
    }

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            getThreadHistory()
        }
    }

    var first = false
    private fun setup() {
        mainViewModel = App.getInstance().getViewModel()
        mainViewModel.lastMsg = -1

        imageButton.setOnClickListener{
            val req  = RequestMessage.Builder(editTextTextPersonName.text.toString(),thread?.id!!).messageType(TextMessageType.Constants.TEXT).build()
            mainViewModel.sendTextMessage(req,null)
            editTextTextPersonName.setText("")
        }

        first = true
        mainViewModel.selectedThread.observe(viewLifecycleOwner, Observer {
            val mThread = it ?: return@Observer
            mThread.also {
                thread = it
                getThreadHistory()
                getThreadHistory()
            }
        })


        mainViewModel.threadHistoryObservable
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
                    mAdapter.updateList(it)
                    mAdapter.notifyDataSetChanged()
                    if (first) {
                        first = false
                        nested.post(Runnable { nested.fullScroll(View.FOCUS_DOWN) })
                    }
                }
                Utility.hideProgressBar()
            }

        mainViewModel.newMessageObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Log.d("CHAT_TEST_UI", "UI ERROR") }
            .subscribe {
                mAdapter.addNewMessage(it)
                mAdapter.notifyDataSetChanged()

                nested.post(Runnable { nested.fullScroll(View.FOCUS_DOWN) })

                Log.e("onNewMessage", "onNewMessage in History: ${it.message}" )
            }

    }


    private fun getThreadHistory() {
        if (!isLoading) {
            val threadID = thread?.id
            context?.showProgressBar()
            isLoading = true
            val requestThread = threadID?.let {
                RequestGetHistory
                    .Builder(it)
                    .offset(offset)
                    .count(count)
                    .build()
            }
            uniqId = requestThread?.let { mainViewModel.getThreadHistory(it, null) }.toString()
        }
    }


}