package github.green_miner.messenger

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call

class ChannelFragment : Fragment() {
    private val tag = "ChannelFragment"
    private lateinit var recyclerView: RecyclerView
    private val myApp = MyApp.instance

    private var calls = ArrayList<Call<List<String>>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.i(tag, "onCreateView")

        val view = inflater.inflate(R.layout.channel_fragment, container, false)
        recyclerView = view.findViewById(R.id.channelRecyclerView)

        val viewManager = LinearLayoutManager(requireActivity())
        viewManager.stackFromEnd = true
        recyclerView.layoutManager = viewManager
        recyclerView.adapter = ChannelAdapter(myApp.channels) {
            (activity as MainActivity).channelSelection(it)
        }

        updateChannels()
        return view
    }

    private fun updateChannels() {
        val call = myApp.service.getChannels()
        calls.add(call)
        call.enqueue(
            ChannelCallback(recyclerView)
        )
    }

    override fun onDestroy() {
        Log.i(tag, "onDestroy")
        calls.forEach {
            it.cancel()
        }
        super.onDestroy()
    }
}