package fanap.pod.chat.boilerplateapp.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fanap.podchat.mainmodel.MessageVO
import com.fanap.podchat.mainmodel.UserInfo
import fanap.pod.chat.boilerplateapp.App
import fanap.pod.chat.boilerplateapp.databinding.FragmentHistoryBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class HistoryItemRecyclerViewAdapter(
    private val histories: MutableList<MessageVO>
) : RecyclerView.Adapter<HistoryItemRecyclerViewAdapter.ViewHolder>() {

    var user: UserInfo? = App.user

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    fun updateList(values: MutableList<MessageVO>) {
        this.histories.addAll(values)
    }

    fun addNewMessage(newMessage: MessageVO) {
        if (!this.histories.contains(newMessage))
            if(newMessage.id >this.histories.get(0).id){
                this.histories.add(0,newMessage)
            }else
                this.histories.add(newMessage)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = histories[position]


        if (item.participant != null && item.participant.id == user?.id) {
            holder.la_selfviews.visibility = View.VISIBLE
            holder.la_partnerviews.visibility = View.GONE
            holder.contentView.text = item.message
        } else {
            holder.la_selfviews.visibility = View.GONE
            holder.la_partnerviews.visibility = View.VISIBLE
            holder.txt_my.text = item.message
        }
    }

    override fun getItemCount(): Int = histories.size

    inner class ViewHolder(binding: FragmentHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content
        val txt_my: TextView = binding.txtMy
        val la_selfviews: LinearLayout = binding.laPartnerviews
        val la_partnerviews: LinearLayout = binding.laSelfmessages

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}