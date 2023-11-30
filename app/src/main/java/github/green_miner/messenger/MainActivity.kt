package github.green_miner.messenger

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private val myApp = MyApp.instance
    private var channelFragment: ChannelFragment? = null
    private var messengerFragment: MessengerFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(tag, "onCreate")

        channelFragment = ChannelFragment()
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i(tag, "Portrait")
            supportFragmentManager.beginTransaction().replace(
                R.id.mainChannelFragment, channelFragment!!
            ).commit()
        } else {
            Log.i(tag, "Landscape")

            messengerFragment = MessengerFragment()
            supportFragmentManager.beginTransaction().replace(
                R.id.channelFragment, channelFragment!!
            ).commit()
            supportFragmentManager.beginTransaction()
                .replace(R.id.messageFragment, messengerFragment!!).commit()

        }
    }

    fun channelSelection(newChannel: String) {
        Log.i(tag, "Channel selection")
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (!myApp.isRunning) {
                myApp.switchChannel(newChannel)
                messengerFragment!!.refreshRecycler()
            } else {
                myApp.nextChannel = newChannel
            }
            return
        }
        val flag = messengerFragment == null
        if (flag) {
            Log.i(tag, "Null fragment")
            messengerFragment = MessengerFragment()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainChannelFragment, messengerFragment!!).addToBackStack(null).commit()
        if (!myApp.isRunning) {
            myApp.switchChannel(newChannel)
        } else {
            myApp.nextChannel = newChannel
        }
    }

    override fun onDestroy() {
        myApp.loadToCache()
        super.onDestroy()
    }
}