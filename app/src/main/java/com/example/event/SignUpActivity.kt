package com.example.event

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class SignUpActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var profilePhotoImageView: ImageView
    private lateinit var UploadButton: Button
    private lateinit var signUpButton: Button
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager
    private val REQUEST_IMAGE_GALLERY = 1 // Arbitrary request code

    private var profilePhoto: ByteArray? = null


    val PICK_IMAGE_REQUEST = 1

    fun getPathFromUri(uri: Uri): ByteArray? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            return it.getBlob(columnIndex)
        }
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val signUPTitle = getString(R.string.signUp_title)
        title = signUPTitle

        nameEditText = findViewById(R.id.signup_name)
        emailEditText = findViewById(R.id.signup_email)
        phoneEditText = findViewById(R.id.signup_phone)
        passwordEditText = findViewById(R.id.signup_password)
        confirmPasswordEditText = findViewById(R.id.signup_confirm_password)
        profilePhotoImageView = findViewById(R.id.profile_photo)
        signUpButton = findViewById(R.id.signup_button)
        UploadButton = findViewById(R.id.upload_button)
        sessionManager = SessionManager(this)
        databaseHelper = DatabaseHelper(this)

        // Set up button click listener
        UploadButton.setOnClickListener {
            // Create an intent to open the gallery
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)

        }

        signUpButton.setOnClickListener {

            // Function to get file path from URI
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val profilephoto = profilePhoto.toString()
            Log.d("Text6", "**SHOWFILEPATH** $profilephoto")

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                val user = databaseHelper.getUserByEmail(email)
                if (user != null) {
                    Toast.makeText(this, "Email is already taken", Toast.LENGTH_SHORT).show()
                } else {
                    val id = databaseHelper.addUser(name, email, phone, password, profilephoto)
                    if (id == -1L) {
                        Toast.makeText(this, "Error adding user", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("sad**", "$profilePhotoImageView")
                        Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("if","Before IF $requestCode, $resultCode")

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) {
            // Get selected image URI
            val selectedImageUri: Uri = data.data ?: return
            Log.d("Text4", "ImagePath*** $selectedImageUri")

            // Get image file path from URI
            profilePhoto = getPathFromUri(selectedImageUri)
            if(profilePhoto !=null) {
                Log.d("K2","INSIDE CONDITION")
                profilePhotoImageView.setImageURI(selectedImageUri)
            }
            Log.d("TextTest", "FilePathCHECK** $profilePhoto")
        }
    }

}

