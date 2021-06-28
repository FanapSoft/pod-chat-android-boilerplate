package fanap.pod.chat.boilerplateapp.ui.history

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import fanap.pod.chat.boilerplateapp.databinding.FragmentHistoryBinding

import fanap.pod.chat.boilerplateapp.ui.history.placeholder.PlaceholderContent.PlaceholderItem


/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyItemRecyclerViewAdapter(
    private val values: List<PlaceholderItem>
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    var x = 0
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
//        holder.idView.text = item.id
        holder.contentView.text = item.content

        if (x == 0) {
            holder.la_selfviews.visibility = View.GONE
            holder.la_partnerviews.visibility = View.VISIBLE
            x = 1
        } else {
            holder.la_selfviews.visibility = View.VISIBLE
            holder.la_partnerviews.visibility = View.GONE
            x = 0
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content
        val la_selfviews: LinearLayout = binding.laPartnerviews
        val la_partnerviews: LinearLayout = binding.laSelfmessages

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}