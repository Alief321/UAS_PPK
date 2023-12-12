package com.example.uasppk

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uasppk.model.response.LoginResponse
import com.example.uasppk.model.response.ProfileResponse
import com.example.uasppk.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TOKEN = "token"
class ProfileFragment : Fragment() {
    private var isPasswordVisible = false


    private var token: String? = null

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = ApiClient.apiService
        Log.d("Cek", "masuk onCreate")
        arguments?.let {
            token = it.getString(TOKEN)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Cek", "masuk OnCreateView")
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Token", token!!)
        Log.d("Cek", "masuk OnViewCreated")

//        val toggle = view.findViewById<Button>(R.id.togglePasswordVisibilityButton)
//        val passwordEditText = view.findViewById<Button>(R.id.passwordEditText)
//
//        toggle.setOnClickListener {
//            isPasswordVisible = !isPasswordVisible
//            updatePasswordVisibility()
//        }
//
//        // Set initial password visibility
//        updatePasswordVisibility()
        getProfile()
    }
//
//    private fun updatePasswordVisibility() {
//        val passwordEditText = view?.findViewById<Button>(R.id.passwordEditText)
//        if (isPasswordVisible) {
//            passwordEditText?.inputType =
//                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
//        } else {
//            passwordEditText?.inputType =
//                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
//        }
//        // Move cursor to the end of the text
////        passwordEditText.setSelection(passwordEditText.text.length)
//    }

    private fun getProfile(){
        val call = apiService.profile("Bearer "+ token)

        call.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                if (response.isSuccessful) {
                    val profileResponse = response.body()
                    val name = this@ProfileFragment.view?.findViewById<TextView>(R.id.profile_name)
                    val email = this@ProfileFragment.view?.findViewById<TextView>(R.id.profile_email)
                    val nim = this@ProfileFragment.view?.findViewById<TextView>(R.id.profile_nim)
                    val kelas = this@ProfileFragment.view?.findViewById<TextView>(R.id.profile_kelas)

                    nim?.text = profileResponse?.nim
                    name?.text = profileResponse?.name
                    email?.text = profileResponse?.email
                    kelas?.text = profileResponse?.kelas

                } else {
                    Toast.makeText(this@ProfileFragment.activity, "Get profile failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(this@ProfileFragment.activity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
    companion object {
        private const val TOKEN = "token"

        @JvmStatic
        fun newInstance(token: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(TOKEN, token)
                }
            }
    }
}
