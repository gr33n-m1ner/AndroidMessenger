package github.green_miner.messenger

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call

class MessengerFragment : Fragment() {
    private val tag = "MessageFragment"
    private val name = "FinalCountdown"

    private lateinit var recyclerView: RecyclerView
    private lateinit var writeView: EditText
    private lateinit var sendButton: Button
    private lateinit var updateButton: Button
    private lateinit var imageButton: Button

    private var myApp = MyApp.instance
    private var calls = ArrayList<Call<List<Message>>>()

    private val getPicture =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri == null) {
                return@registerForActivityResult
            }
            val inputStream = myApp.contentResolver.openInputStream(uri)!!
            val filePart: MultipartBody.Part
            inputStream.use { stream ->
                val input = stream.readBytes()
                filePart = MultipartBody.Part.createFormData(
                    "pic", "${System.currentTimeMillis()}.jpg", input.toRequestBody(
                        myApp.contentResolver.getType(uri)?.toMediaType(), 0, input.size
                    )
                )
            }

            val messagePart =
                "{\"from\": \"${name}\", \"to\": \"${myApp.currentChannel}\"}".toRequestBody("application/json".toMediaType())
            myApp.service.postImage(messagePart, filePart).enqueue(PostCallback())
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        Log.i(tag, "onCreateView")

        val view = inflater.inflate(R.layout.messenger_fragment, container, false)

        writeView = view.findViewById(R.id.write_message)
        updateButton = view.findViewById(R.id.update)
        sendButton = view.findViewById(R.id.send)
        imageButton = view.findViewById(R.id.send_image)

        recyclerView = view.findViewById(R.id.messages)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = MessageAdapter(myApp.currentMessages)
        recyclerView.scrollToPosition(myApp.currentMessages.size - 1)

        updateButton.setOnClickListener {
            Log.i(tag, "Update")
            if (!myApp.isRunning) {
                myApp.isRunning = true
                myApp.getMessages()
                val call = myApp.service.getMessages(myApp.currentChannel, myApp.lastId())
                calls.add(call)
                call.enqueue(GetCallback(recyclerView, java.lang.StringBuilder(myApp.currentChannel).toString()))
            }
        }
        updateButton.callOnClick()

        sendButton.setOnClickListener {
            Log.i(tag, "Send")
            if (writeView.text.toString() != "") {
                myApp.service.postMessage(
                    Message(
                        name, myApp.currentChannel, System.currentTimeMillis(), MessageData(
                            Text(writeView.text.toString()), null
                        ), 0
                    )
                ).enqueue(PostCallback())
                writeView.text.clear()
            }
        }

        imageButton.setOnClickListener {
            Log.i(tag, "Image")
            try {
                getPicture.launch("image/*")
            } catch (e : java.lang.IllegalStateException) {
                Log.e(tag, "Too fast sending")
            }

        }

        return view
    }

    override fun onDestroy() {
        Log.i(tag, "onDestroy")
        calls.forEach {
            it.cancel()
        }
        myApp.loadToCache()
        super.onDestroy()
    }

    fun refreshRecycler() {
        recyclerView.adapter!!.notifyDataSetChanged()
        updateButton.callOnClick()
    }
}