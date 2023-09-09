package com.example.event


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.event.api.CollectionList


private const val TAG = "GetCollectionFragment"

class GetCollectionFragment: Fragment() {

    private lateinit var getCollection_recycler_view: RecyclerView
    private val collectionViewModel: CollectionViewModel by lazy {
        ViewModelProvider(this)[CollectionViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val collectionListTitle = getString(R.string.collection_list_title)
        requireActivity().title  = collectionListTitle
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_get_collection, container, false)
        Log.d(TAG, "View**** $view")
        this.getCollection_recycler_view =
            view.findViewById(R.id.getCollection_recycler_view) as RecyclerView
        getCollection_recycler_view.layoutManager = LinearLayoutManager(context)
        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.collectionViewModel.collectionListLiveData.observe(
            this.viewLifecycleOwner
        ) { collection ->
            Log.d(TAG, "ViewModel has noticed new collections $collection")
            val adapter = CollectionAdapter(collection)
            getCollection_recycler_view.adapter = adapter
        }
    }



   private inner class CollectionAdapter(var collectionList: List<CollectionList>) :
        RecyclerView.Adapter<CollectionAdapter.PersonHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonHolder {
            val view = layoutInflater.inflate(R.layout.collectionlist, parent, false)
            Log.d(TAG, "recycler*** $view")
            return PersonHolder(view)
        }

        override fun onBindViewHolder(holder: PersonHolder, position: Int) {
            val person = collectionList[position]
            holder.bind(person)
        }

        override fun getItemCount(): Int = collectionList.size

        private inner class PersonHolder(view: View) : RecyclerView.ViewHolder(view) {
            private lateinit var collectionList: CollectionList
            private val nameTextView: TextView = this.itemView.findViewById(R.id.collectionName)


            fun bind(collectionList: CollectionList) {
                this.collectionList = collectionList
                this.nameTextView.text = this.collectionList.name
                Log.d(TAG, "nameTextView.text*** $collectionList")
            }
        }
    }
}