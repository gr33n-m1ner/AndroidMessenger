package github.green_miner.messenger

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetCallback(
    private val recyclerView: RecyclerView,
    private var channel: String
) : Callback<List<Message>> {
    private val tag = "GetCallback"
    private val context = MyApp.instance

    override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
        val body: List<Message>? = response.body()
        if (response.isSuccessful && body != null) {
            if (body.isEmpty()) {
                updateRecycler()
                return
            }
            Log.i(tag, "onResponse")

            for (element in body) {
                context.currentMessages.add(element)
            }

            if (context.nextChannel != "" && context.nextChannel != context.currentChannel) {
                Log.i(tag, "Changed channel on spot")
                context.switchChannel(context.nextChannel)
                channel = StringBuilder(context.nextChannel).toString()
                context.nextChannel = ""
            }

            context.service.getMessages(context.currentChannel, context.lastId())
                .enqueue(GetCallback(recyclerView, channel))

        } else {
            Log.i(tag, "Failed loading messages")
            updateRecycler()
        }
    }

    override fun onFailure(call: Call<List<Message>>, t: Throwable) {
        Log.i(tag, "onFailure")
        updateRecycler()
        if (!call.isCanceled) {
            Log.i(tag, "Failed loading messages ${t.message}")
        }
    }

    private fun updateRecycler() {
        Log.i(tag, "End of getting")
        context.isRunning = false
        recyclerView.adapter!!.notifyItemRangeInserted(
            context.oldSize, context.currentMessages.size - context.oldSize
        )
        recyclerView.scrollToPosition(context.currentMessages.size - 1)
        context.oldSize = context.currentMessages.size
    }
}