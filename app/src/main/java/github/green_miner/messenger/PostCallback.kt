package github.green_miner.messenger

import android.util.Log
import android.widget.Toast
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostCallback : Callback<ResponseBody> {
    private val tag = "PostCallback"

    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
        Log.i(tag, "onResponse")
        if (!response.isSuccessful || response.body() == null) {
            if (response.code() >= 500) {
                Toast.makeText(MyApp.instance, "Failed POST: Server error", Toast.LENGTH_LONG)
                    .show()
            } else if (response.code() == 413) {
                Toast.makeText(MyApp.instance, "The image is too big!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(MyApp.instance, "Failed sending message. Error!", Toast.LENGTH_LONG)
                    .show()
                when (response.code()) {
                    422 -> Log.e(tag, "Error with sending data")
                    415 -> Log.e(tag, "Wrong content type")
                    411 -> Log.e(tag, "No content length")
                    409 -> Log.e(tag, "Name collision")
                    404 -> Log.e(tag, "Not found")
                    400 -> Log.e(tag, "Server didn't understand us")
                }
            }
        }
    }

    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
        if (!call.isCanceled) {
            Log.i(tag, "Failed sending message: ${t.message}")
            Toast.makeText(MyApp.instance, "Failed sending message. Error!", Toast.LENGTH_LONG)
                .show()
        }
    }
}