package com.example.event

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.ViewModelProvider


class MainActivity : AppCompatActivity() {

private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sessionManager = SessionManager(this)
        Log.d("TEST","Inside MainActivityl!!!")
//        private val collectionViewModel: CollectionViewModel by lazy {
            PreferencesRepository.initialize(this)
//            ViewModelProvider(this)[CollectionViewModel::class.java]
//        }

        val button = findViewById<Button>(R.id.button_next)

        val button2 = findViewById<Button>(R.id.button_next2)


        if(sessionManager.getSessionEmail()!=null){
            Log.d("TEST","Inside getSessionEmail!!!")
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("message", "Hello from Login to Home!")
            startActivity(intent)
            finish()
        }
        else {
            button.setOnClickListener {
                val intent = Intent(this, SignUpActivity::class.java)
                intent.putExtra("message", "Hello from MainActivity!")
                startActivity(intent)
            }
            button2.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("message", "Hello from MainActivity to Login!")
                startActivity(intent)
            }
            val button3 = findViewById<Button>(R.id.button_next3)
            button3.setOnClickListener {
                val intent = Intent(this, videoActivity::class.java)
                intent.putExtra("message", "Hello from MainActivity to video !")
                startActivity(intent)
            }
        }
    }
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putString(COLLECTION_NAME, this.collectionViewModel.getCollectionName())
    }
    private val collectionViewModel: CollectionViewModel by lazy {
//        val context = requireContext() // use the requireContext() method to get the valid Context object
        PreferencesRepository.initialize(this)
        ViewModelProvider(this)[CollectionViewModel::class.java]
    }
}