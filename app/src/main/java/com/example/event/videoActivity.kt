package com.example.event

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.widget.*
import android.widget.VideoView

class videoActivity : AppCompatActivity() {

    lateinit var videoView: VideoView

//    var videoUrl="https://www.youtube.com/watch?v=p6f58wTPGf8&list=PLM-7VG-sgbtA-MUiVgE-SwK_RkYgesikH&index=3"
    var videoUrl ="https://voyager.postman.com/video/postman-about-us-world.mp4"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        val videoTitle = getString(R.string.video_title)
        title = videoTitle

        videoView = findViewById(R.id.videoView);
//

        val uri: Uri = Uri.parse(videoUrl)
        videoView.setVideoURI(uri)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        mediaController.setMediaPlayer(videoView)
        videoView.setMediaController(mediaController)
        videoView.start()
    }
}