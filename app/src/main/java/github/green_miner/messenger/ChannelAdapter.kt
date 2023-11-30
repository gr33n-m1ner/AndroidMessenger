package github.green_miner.messenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChannelAdapter(
    private val channels: List<String>,
    private val onClick: (String) -> Unit
    ) : RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder?>() {

    class ChannelViewHolder(root : View) : RecyclerView.ViewHolder(root) {
        val textView: TextView = root.findViewById(R.id.channel)
        fun bind(channel: String) {
            textView.text = channel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val holder = ChannelViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.channel_item, parent, false)
        )
        holder.itemView.setOnClickListener {
            onClick(holder.textView.text.toString())
        }
        return holder
    }

    override fun getItemCount() = channels.size

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.bind(channels[position])
    }
}