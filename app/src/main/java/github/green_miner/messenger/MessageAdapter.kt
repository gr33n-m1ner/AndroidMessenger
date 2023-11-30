package github.green_miner.messenger

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.text.format.DateFormat
import com.squareup.picasso.Picasso
import java.util.*

class MessageAdapter(
    private val messages: List<Message>
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        private val senderView: TextView = root.findViewById(R.id.sender)
        private val receiverView: TextView = root.findViewById(R.id.receiver)
        private val timeView: TextView = root.findViewById(R.id.time)
        private val messageView: TextView = root.findViewById(R.id.message)
        val imageView: ImageView = root.findViewById(R.id.image)

        fun bind(message: Message) {
            senderView.text = message.from
            receiverView.text = message.to
            timeView.text = DateFormat.format("dd-MM-yyyy kk:mm:ss", Date(message.time))
            if (message.data.Text != null) {
                messageView.text = message.data.Text.text
            } else {
                messageView.text = ""
            }
            imageView.visibility = ImageView.GONE
            if (message.data.Image != null) {
                imageView.visibility = ImageView.VISIBLE
                Picasso.get().load("http://213.189.221.170:8008/thumb/${message.data.Image.link}")
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background).into(imageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val holder = MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        )
        holder.imageView.setOnClickListener {
            val intent = Intent(MyApp.instance, FullImageActivity::class.java)
            intent.putExtra("link", messages[holder.absoluteAdapterPosition].data.Image!!.link)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            MyApp.instance.startActivity(intent)
        }
        return holder
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount() = messages.size
}