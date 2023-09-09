package com.example.event

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager
    private lateinit var SignUpButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val loginTitle = getString(R.string.login_title)
        title = loginTitle

        sessionManager = SessionManager(this)

        emailEditText = findViewById(R.id.login_email)
        passwordEditText = findViewById(R.id.login_password)
        loginButton = findViewById(R.id.login_button)
        SignUpButton = findViewById(R.id.textViewSignUp)
        databaseHelper = DatabaseHelper(this)
        SignUpButton.paintFlags = SignUpButton.paintFlags or Paint.UNDERLINE_TEXT_FLAG


        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show()
            } else {
                val user = databaseHelper.getUserByEmail(email)
                if (user == null || user.password != password) {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                } else {
                    //Home page with Logout button!
                    sessionManager.createSession(email)
                    Toast.makeText(this, "Welcome ${user.name}!", Toast.LENGTH_SHORT).show()
                    sessionManager.createDataSession(user.name, user.phone)
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("message", "Hello from Login to Home!")
                    startActivity(intent)
                    finish()
                }
            }
        }

        SignUpButton.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra("message", "Hello from Login to SignUp!")
            startActivity(intent)
            finish()
        }
    }
}