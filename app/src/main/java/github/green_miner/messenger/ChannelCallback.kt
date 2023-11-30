package github.green_miner.messenger

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChannelCallback(private val recyclerView: RecyclerView) : Callback<List<String>> {
    private val tag = "ChannelCallback"
    private val context = MyApp.instance

    override fun onResponse(
        call: Call<List<String>>, response: Response<List<String>?>
    ) {
        val body: List<String>? = response.body()
        if (response.isSuccessful && body != null) {
            Log.i(tag, "onResponse")
            context.channels.clear()
            for (element in body) {
                context.channels.add(element)
            }
            recyclerView.adapter!!.notifyDataSetChanged()
        } else {
            Log.i(tag, "Failed loading channels")
        }
    }

    override fun onFailure(call: Call<List<String>>, t: Throwable) {
        if (!call.isCanceled) {
            Log.i(tag, "Failed loading channels ${t.message}")
        }
    }
}