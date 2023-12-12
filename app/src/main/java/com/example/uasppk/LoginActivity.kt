package com.example.uasppk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uasppk.databinding.ActivityMainBinding
import com.example.uasppk.model.request.LoginRequest
import com.example.uasppk.model.response.LoginResponse
import com.example.uasppk.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        apiService = ApiClient.apiService


        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.loginButton)
        val registButton: Button = findViewById(R.id.registbutton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                Log.d("email", email)
                Log.d("password", password)
                performLogin(email, password)
            } else {
                Toast.makeText(
                    this,
                    "email dan password tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        registButton.setOnClickListener{
            val switchIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(switchIntent)
        }
    }



    private fun performLogin(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)
        val call = apiService.login(loginRequest)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Toast.makeText(
                        this@LoginActivity,
                        "Login successful. name: ${loginResponse?.name}",
                        Toast.LENGTH_SHORT
                    ).show()
                    val switchIntent = Intent(this@LoginActivity, MainActivity::class.java)
                    switchIntent.putExtra("name", "Halo "+ loginResponse?.name + " 👋🏻")
                    switchIntent.putExtra("token", loginResponse?.token)
                    startActivity(switchIntent)
                    // Handle successful login, e.g., save token to preferences, navigate to the next screen, etc.
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}