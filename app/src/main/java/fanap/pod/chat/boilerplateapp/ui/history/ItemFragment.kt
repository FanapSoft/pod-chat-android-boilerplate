package fanap.pod.chat.boilerplateapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fanap.podchat.mainmodel.Thread
import fanap.pod.chat.boilerplateapp.App
import fanap.pod.chat.boilerplateapp.R
import fanap.pod.chat.boilerplateapp.ui.history.placeholder.PlaceholderContent
import fanap.pod.chat.boilerplateapp.ui.main.MainViewModel

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {
    var thread: Thread? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var nested: NestedScrollView
    private var columnCount = 1
    private lateinit var mainViewModel: MainViewModel

    private var mAdapter: MyItemRecyclerViewAdapter =
        MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = 1
        }
    }

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

        nested = view.findViewById(R.id.nested)
        recyclerView = view.findViewById(R.id.list)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = mAdapter

        setup()

//        getScrollView().setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
//            if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) &&
//                scrollY > oldScrollY
//            ) {
//                checkLoadMore()
//            }
//        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.setNavigation(true)
    }

    private fun setup() {
        mainViewModel = App.getInstance().getViewModel()

        mainViewModel.selectedThread.observe(viewLifecycleOwner, Observer {
            val mThread = it ?: return@Observer
            mThread.also { thread = it }
        })

    }

}