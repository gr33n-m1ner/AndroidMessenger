package github.green_miner.messenger

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class FullImageActivity : AppCompatActivity() {
    private lateinit var fullImage: ImageView
    private val tag = "FullImageActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(tag, "onCreate")
        setContentView(R.layout.activity_image)

        fullImage = findViewById(R.id.imageView)

        val url = intent.getStringExtra("link")
        Picasso.get().load("http://213.189.221.170:8008/img/${url}")
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background).into(fullImage)
        fullImage.setOnClickListener {
            finish()
        }
    }
}