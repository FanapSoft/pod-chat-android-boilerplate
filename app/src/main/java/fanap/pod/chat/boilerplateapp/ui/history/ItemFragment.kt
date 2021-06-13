package fanap.pod.chat.boilerplateapp.ui.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import fanap.pod.chat.boilerplateapp.R
import fanap.pod.chat.boilerplateapp.factory.ViewModelFactory
import fanap.pod.chat.boilerplateapp.ui.history.placeholder.PlaceholderContent
import fanap.pod.chat.boilerplateapp.ui.login.LoginActivity
import fanap.pod.chat.boilerplateapp.ui.main.MainViewModel
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {

    private var columnCount = 1
    private lateinit var mainViewModel: MainViewModel

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

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS)
            }
        }
        setup()
        return view
    }

    private fun setup() {
        mainViewModel = ViewModelProvider(this, ViewModelFactory())
            .get(MainViewModel::class.java)

        val x = mainViewModel.t
        mainViewModel.selectedThread.observe(viewLifecycleOwner, Observer {
            val state = it ?: return@Observer
            if (state != null)
            ;
        })

    }

}