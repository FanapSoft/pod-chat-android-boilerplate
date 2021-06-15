package fanap.pod.chat.boilerplateapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fanap.pod.chat.boilerplateapp.App
import fanap.pod.chat.boilerplateapp.R
import fanap.pod.chat.boilerplateapp.factory.ViewModelFactory
import fanap.pod.chat.boilerplateapp.ui.history.placeholder.PlaceholderContent
import fanap.pod.chat.boilerplateapp.ui.main.MainViewModel

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

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.setNavigation(true)
    }

    private fun setup() {
        mainViewModel = App.getInstance().getViewModel()

        val x = mainViewModel.t
        mainViewModel.selectedThread.observe(viewLifecycleOwner, Observer {
            val state = it ?: return@Observer
            if (state != null)
            ;
        })

    }

}