package com.example.event


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

private const val TAG = "CreateCollectionFragment"
const val COLLECTION_NAME = "collectionName"


class CreateCollectionFragment: Fragment() {
//    private lateinit var collectionViewModel: CollectionViewModel
    private lateinit var collectionName: EditText
    private lateinit var api_name: EditText
    private lateinit var api_type: EditText

    private lateinit var collectionButton: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val createCollectionTitle = getString(R.string.create_collection_title)
        requireActivity().title  = createCollectionTitle

//        this.collectionViewModel = ViewModelProvider(this.requireActivity())[CollectionViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        PreferencesRepository.initialize(requireContext())
        val view = inflater.inflate(R.layout.fragment_create_collection, container, false)
        this.collectionName = view.findViewById(R.id.collection_name)
        this.api_name = view.findViewById(R.id.item_name)
        this.api_type = view.findViewById(R.id.api_type)

        this.collectionButton = view.findViewById(R.id.createCollection)
        collectionButton.setOnClickListener {
            Log.v(TAG, "Enter caption button clicked")
            val text = this.collectionName.text
            val text1 = this.api_name.text
            val text2 = this.api_type.text

            if (text == null) {
                Log.e(TAG, "Cannot collection because the text is null")
            }  else {
                Log.v(TAG, "Enter caption button clicked else text $text")
//                collectionViewModel.loadCollectionName()

                Log.d("", "Persistance ${collectionViewModel.getCollectionName()}")
//                collectionName.setText(collectionViewModel.getCollectionName().toString())
//                collectionViewModel.setCollectionName(text.toString())
                this.collectionViewModel.createCollection(

                            collectionName = text.toString(),
                            api_name = text1.toString(),
                            api_type = text2.toString()

                ).observe(
                    this.viewLifecycleOwner
                ) { CreateCollectionResponseData ->
                    Log.d(
                        TAG,
                        "Fragment has been notified that a collection has been created: ${CreateCollectionResponseData}"
                    )

                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(TAG, "onViewCreated called")
    }


    override fun onDestroy() {
        super.onDestroy()
    }
    override fun onStart() {
        super.onStart()
    }
    override fun onStop() {
        super.onStop()
    }
    override fun onResume() {
        super.onResume()
    }
    override fun onPause() {
        super.onPause()
    }


    private val collectionViewModel: CollectionViewModel by lazy {
//        val context = requireContext() // use the requireContext() method to get the valid Context object
//        PreferencesRepository.initialize(context)
        ViewModelProvider(this)[CollectionViewModel::class.java]
    }

}
