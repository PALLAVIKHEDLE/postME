package com.example.event

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.view.Menu
import android.widget.MediaController
import android.widget.VideoView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.event.api.CollectionList
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity: AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        title = "Collection List"

        lateinit var bottomNav : BottomNavigationView

        loadFragment(GetCollectionFragment())
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.get_collection_fragment_container -> {
                    loadFragment(GetCollectionFragment())
                    true
                }

                R.id.create_collection_fragment_container -> {
                    loadFragment(CreateCollectionFragment())
                    true
                }
                R.id.profile -> {
                    loadFragment(profileFragment())
                    true
                }
                else -> false
            }
        }
    }
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.my_menu,menu)
//        return super.onCreateOptionsMenu(menu)
//    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

}