package com.example.uasppk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uasppk.model.request.RegisterRequest
import com.example.uasppk.model.response.RegisterResponse
import com.example.uasppk.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        apiService = ApiClient.apiService


        val emailEditText = findViewById<EditText>(R.id.regist_email)
        val passwordEditText= findViewById<EditText>(R.id.regist_password)
        val nameEditText = findViewById<EditText>(R.id.regist_name)
        val nimEditText = findViewById<EditText>(R.id.regist_nim)
        val registerButton: Button = findViewById(R.id.registerButton)

        registerButton.setOnClickListener{
            val nim = nimEditText.text.toString()
            val email = emailEditText.text.toString()
            val name = nameEditText.text.toString()
            val password = passwordEditText.text.toString()

            Log.d("email", email)
            Log.d("name", name)
            Log.d("password", password)
            Log.d("nim", nim)
            if (email.isNotEmpty() && password.isNotEmpty() && nim.isNotEmpty() && name.isNotEmpty()) {
                Log.d("MyTag", "Masuk")
                performRegister(nim, email, name, password)
            } else {
                Toast.makeText(
                    this,
                    "Semua field harus diisi",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun performRegister(nim: String, email: String, name: String, password: String) {
        val registerRequest = RegisterRequest(nim, email, name, password)
        val call = apiService.register(registerRequest)

        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    Toast.makeText(
                        this@RegisterActivity,
                        "Register akun sukses",
                        Toast.LENGTH_SHORT
                    ).show()
                    val switchIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(switchIntent)
                    // Handle successful login, e.g., save token to preferences, navigate to the next screen, etc.
                } else {
                    Toast.makeText(this@RegisterActivity, "Register failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}

