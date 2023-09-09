
package com.example.event

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.Manifest
import android.content.ActivityNotFoundException
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class profileFragment : Fragment() {

    private val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private lateinit var sessionManager: SessionManager
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var imageView: ImageView
    private val collectionViewModel: CollectionViewModel by lazy {
        ViewModelProvider(this)[CollectionViewModel::class.java]
    }

    fun generatePdf(arrayData: Array<String>, fileName: String) {
        // Create a new PdfDocument
        val document = PdfDocument()

// Define the page dimensions
        val pageWidth = 8.5 * 72 // 8.5 inches in points
        val pageHeight = 11 * 72 // 11 inches in points

// Define the page margins
        val marginLeft = 50 // 50 points
        val marginRight = 50 // 50 points
        val marginTop = 50 // 50 points
        val marginBottom = 50 // 50 points

// Calculate the usable page width and height
        val usablePageWidth = pageWidth - marginLeft - marginRight
        val usablePageHeight = pageHeight - marginTop - marginBottom

// Define the text size and line spacing
        val textSize = 12f
        val lineSpacing = textSize * 1.5f

// Create a Paint object for drawing text
        val paint = Paint()
        paint.textSize = textSize

// Create a PageInfo object for the first page
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth.toInt(), pageHeight.toInt(), 1).create()

// Start a new page
        var page = document.startPage(pageInfo)

// Get the Canvas for the page
        var canvas = page.canvas

// Loop through the array of data and draw each item on the page
        var y = marginTop.toFloat()
        for (item in arrayData) {
            canvas.drawText(item, marginLeft.toFloat(), y, paint)
            y += lineSpacing
            if (y + marginBottom > usablePageHeight) {
                // If the item would extend beyond the bottom margin,
                // start a new page and continue drawing on the next page
                document.finishPage(page)
                val newPageInfo = PdfDocument.PageInfo.Builder(pageWidth.toInt(), pageHeight.toInt(), document.pages.size + 1).create()
                val newPage = document.startPage(newPageInfo)
                val newCanvas = newPage.canvas
                y = marginTop.toFloat()
                newCanvas.drawText(item, marginLeft.toFloat(), y, paint)
                y += lineSpacing
                newCanvas.drawText(item, marginLeft.toFloat(), y, paint)
                y += lineSpacing
                page = newPage
                canvas = newCanvas
            }
        }

// Finish the page
        document.finishPage(page)

// Save the document to a file

        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val filePath = File(directory, fileName).path
        try {
            val fileOutputStream = FileOutputStream(filePath)
            Toast.makeText(requireContext(), "PDF GENERATED SUCCESSFULLY", Toast.LENGTH_SHORT).show()

            Log.d("CHECK!!", "fileOutputStream $fileOutputStream")

            document.writeTo(fileOutputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }

// Close the document
        document.close()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val profileTitle = getString(R.string.profile_title)
        requireActivity().title  = profileTitle


        sessionManager = SessionManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
//        imageView = view.findViewById(R.id.imageDPView)

        // Check for write external storage permission
        if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), 1)
            return view
        }
        val signup = sessionManager.getSessionEmail()
        val name = sessionManager.getSessionName()
        val phone = sessionManager.getSessionPhone()
        val emailUserCR = sessionManager.getSessionEmail().toString()


         databaseHelper = DatabaseHelper(requireContext())
        loadProfilePhoto(emailUserCR)

        Log.d("emailUserCR profile", "PROFILE PHOTO*** $emailUserCR")


        Log.d("CHECK!!", "EMAIL GET BRO*** $signup, $name, $phone")
        Log.d("CHECK2!!", "Name and Phone GET BRO*** $name, $phone")

        val textView = view?.findViewById<TextView>(R.id.signup_email)
        val textViewName = view?.findViewById<TextView>(R.id.signup_name)
        val textViewPhone = view?.findViewById<TextView>(R.id.signup_phone)




        textView?.setText(signup)
        val textViewValue = textView?.text
        Log.d("Text1", "****nameTextView.text*** $textViewValue")

        textViewName?.setText(name)
        val textViewNameValue = textViewName?.text
        Log.d("Text2", "****nameTextViewname*** $textViewNameValue")

        textViewPhone?.setText(phone)
        val textViewPhoneValue = textViewPhone?.text
        Log.d("Text3", "****nameTextView.phone*** $textViewPhoneValue")

        val button2 = view.findViewById<Button>(R.id.logout_button)
        button2.setOnClickListener {
            sessionManager.clearSession()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }



        val generatePdf= view.findViewById<Button>(R.id.idBtnGeneratePdf)
        generatePdf.setOnClickListener {
                this.collectionViewModel.collectionListLiveData.observe(
                    this.viewLifecycleOwner
                ) { collection ->
                    Log.d("", "generated pdf $collection")
                    val arrayData: Array<String> = collection.map { it.name }.toTypedArray()
                    generatePdf(arrayData, "Collection_List.pdf")
                }
        }
        val shareButton= view.findViewById<Button>(R.id.share_button)

        shareButton.setOnClickListener {

            val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(directory, "Collection_List.pdf")
            val packageName = requireContext().packageName
            val uri = FileProvider.getUriForFile(requireContext(), "$packageName.provider", file)

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "application/pdf"
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            // Set the package name of WhatsApp to restrict the app chooser to only WhatsApp
            shareIntent.setPackage("com.whatsapp")

            try {
                startActivity(Intent.createChooser(shareIntent, "Share PDF using"))
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "WhatsApp not installed", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
    private fun loadProfilePhoto(email: String) {
        val user = databaseHelper.getUserByEmail(email)
        Log.d("", "*befor user if $user ")
            val profilePhotoByteArray = user?.profilephoto

//        if (user != null) {
//            Log.d("", "*after user if $user")
//
//
//            val profilePhotoByteArray = user.profilephoto
//            Log.d("/////////NOT Null", "*profilePhotoByteArray $profilePhotoByteArray,  $profilePhotoByteArray.size")
//            val profilePhotoBitmap = BitmapFactory.decodeByteArray(profilePhotoByteArray, 0, profilePhotoByteArray.size)
//            imageView.setImageBitmap(profilePhotoBitmap)
//
//
//        } else {
//            // handle error if user not found
//        }
//        if (profilePhotoByteArray != null && profilePhotoByteArray.isNotEmpty()) {
//            try { Log.d("", "*profilePhotoByteArray if $profilePhotoByteArray")
//                val bitmapOptions = BitmapFactory.Options().apply {
//                    // specify additional options for decoding, such as image size or format
//                    profilePhotoByteArray.size
//                }
////                val profilePhotoBitmap = BitmapFactory.decodeByteArray(profilePhotoByteArray, 0, profilePhotoByteArray.size)
////                imageView.setImageBitmap(profilePhotoBitmap)
//                val inputStream = ByteArrayInputStream(profilePhotoByteArray)
//                val profilePhotoBitmap = BitmapFactory.decodeStream(inputStream, null, bitmapOptions)
//                imageView.setImageBitmap(profilePhotoBitmap)
//            } catch (e: Exception) {
//                Log.e("IMAGE_LOAD_ERROR", "Error loading profile photo: ${e.message}")
//            }
//        } else {
//            Log.e("IMAGE_LOAD_ERROR", "Profile photo data is null or empty.")
//        }
    }
}