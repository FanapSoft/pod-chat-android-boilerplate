package fanap.pod.chat.boilerplateapp.ui.threads

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fanap.podchat.mainmodel.Thread
import com.squareup.picasso.Picasso
import fanap.pod.chat.boilerplateapp.R
import fanap.pod.chat.boilerplateapp.databinding.FragmentThreadsBinding
import java.lang.Exception as Exception1


/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class ThreadItemRecyclerViewAdapter(
    private var values: MutableList<Thread>

) : RecyclerView.Adapter<ThreadItemRecyclerViewAdapter.ViewHolder>() {

    var listener: AdapterCallBack? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentThreadsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    var mContext: Context? = null
    fun updateList(values: MutableList<Thread>) {
        this.values.addAll(values)
//        notifyDataSetChanged()
//        notifyItemInserted(this.values.size)

    }

    fun clearList() {
        this.values.clear()
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        if (item.lastMessage != null) {
            if (item.lastMessage?.length!! > 20)
                holder.idView.text = item.lastMessage.subSequence(0, 20).toString() + " ... "
            else
                holder.idView.text = item.lastMessage
        }
        holder.contentView.text = item.title

        if (item.image != null) {
            Picasso.get()
                .load(item.image)
                .placeholder(R.drawable.app_icon)
                .error(R.drawable.app_icon)
                .into(holder.imageProfile)
        }
        holder.itemView.setOnClickListener {
            listener!!.onItemClick(item)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentThreadsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content
        val imageProfile: ImageView = binding.imgProfile

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}

interface AdapterCallBack {
    fun onItemClick(thread: Thread)
}

