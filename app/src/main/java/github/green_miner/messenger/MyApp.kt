package github.green_miner.messenger

import android.app.Application
import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MyApp : Application() {
    private val tag = "MyApp"
    private lateinit var moshi: Moshi
    private lateinit var retrofit: Retrofit
    private lateinit var adapter: JsonAdapter<List<Message>>

    var nextChannel = ""
    var currentChannel = "2@channel"
    var isRunning = false
    var oldSize = 0

    var channels : ArrayList<String> = ArrayList()
    var currentMessages: ArrayList<Message> = ArrayList()
    lateinit var service: IService

    companion object {
        lateinit var instance: MyApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(tag, "onCreate")

        instance = this
        moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        adapter = moshi.adapter(
            Types.newParameterizedType(
                List::class.java,
                Message::class.java
            )
        )
        retrofit = Retrofit.Builder()
            .baseUrl("http://213.189.221.170:8008")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        service = retrofit.create(IService::class.java)
    }

    fun lastId() : Int {
        return if (currentMessages.isEmpty()) {
            0
        } else {
            currentMessages.last().id
        }
    }

    fun switchChannel(channel : String){
        Log.i(tag, "Switched from $currentChannel to $channel")
        clearMessages()
        currentChannel = channel
        getMessages()
    }

    fun clearMessages(channel: String = currentChannel) {
        Log.i(tag, "Cleared messages")
        loadToCache(channel)
        currentMessages.clear()
        oldSize = 0
    }

    fun getMessages(channel: String = currentChannel) {
        getFromCache(channel)
    }


    private fun getFromCache(channel: String = currentChannel) {
        try {
            val cacheFile = File(cacheDir.absolutePath + "/$channel.json")
            if (cacheFile.exists()) {
                val input = FileInputStream(cacheFile)
                val string = input.readBytes().decodeToString()
                val cachedMessages = adapter.fromJson(string)
                if (cachedMessages == null) {
                    Log.e(tag, "Failed loading messages from cache")
                    return
                }
                currentMessages += cachedMessages
                oldSize = currentMessages.size
                cacheFile.delete()
                Log.i(tag, "Messages loaded from cache successfully")
            }
        } catch (e: Exception) {
            Log.e(tag, "Failed loading messages from cache: ${e.message}")
        }
    }

    fun loadToCache(channel: String = currentChannel) {
        try {
            val output = FileOutputStream(cacheDir.absolutePath + "/$channel.json")
            output.write(adapter.toJson(currentMessages).toByteArray())
            output.close()
            Log.i(tag, "Messages loaded to cache successfully")
        } catch (e: Exception) {
            Log.e(tag, "Failed loading messages to cache: ${e.message}")
        }
    }

}