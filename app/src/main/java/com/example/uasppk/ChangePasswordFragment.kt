package com.example.uasppk

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uasppk.client.ApiClient
import com.example.uasppk.model.request.ChangePasswordRequest
import com.example.uasppk.model.response.ChangePasswordResponse
import com.example.uasppk.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val TOKEN = "token"
private const val EMAIL = "email"

class ChangePasswordFragment : Fragment() {
    private var isPasswordVisible = false
    private var token: String? = null
    private var email: String? = null

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
        return inflater.inflate(R.layout.change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Token", token!!)
        Log.d("Cek", "masuk OnViewCreated")
        val checkbox : CheckBox = view.findViewById(R.id.checkPass)
        val oldPass: EditText = view.findViewById(R.id.OldpasswordEditText)
        val newPass : EditText = view.findViewById(R.id.NewpasswordEditText)
        val konPass : EditText = view.findViewById(R.id.KonpasswordEditText)

        checkbox.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            updatePasswordVisibility(newPass, konPass, oldPass)
        }


        val simpan : Button = view.findViewById(R.id.buttonSimpan)

        if(newPass.text.toString() !== konPass.text.toString()){
            Toast.makeText(this@ChangePasswordFragment.activity,"Password baru dan konfirmasi password harus sama", Toast.LENGTH_SHORT).show()
            simpan.isEnabled = false
            simpan.isClickable = false
        }else{
            simpan.isEnabled = true
            simpan.isClickable = true
        }

        val oldPasstext = oldPass.text.toString()
        val newPassText = newPass.text.toString()

        simpan.setOnClickListener(){
            updatePassword(email!!,oldPasstext, newPassText)
        }

    }

    private fun updatePasswordVisibility(newPass: EditText, konPass: EditText, oldPass: EditText) {
        if (isPasswordVisible) {
            newPass?.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            konPass?.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            oldPass?.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            newPass?.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            konPass?.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            oldPass?.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }

    private fun updatePassword(email: String, oldPassword: String, newPasword: String) {
        val changeRequest = ChangePasswordRequest(email,oldPassword,newPasword)
        val call = apiService.changePassword( "Bearer "+ token, changeRequest)

        call.enqueue(object : Callback<ChangePasswordResponse> {
            override fun onResponse(
                call: Call<ChangePasswordResponse>,
                response: Response<ChangePasswordResponse>
            ) {
                if (response.isSuccessful) {
                    val changeResponse = response.body()
                    Toast.makeText(
                        this@ChangePasswordFragment.activity,
                        "${changeResponse?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    val switchIntent = Intent(this@ChangePasswordFragment.activity, MainActivity::class.java)
                    switchIntent.putExtra("token", token)
                    switchIntent.putExtra("email", email)
                    startActivity(switchIntent)
                } else {
                    Toast.makeText(this@ChangePasswordFragment.activity, "Change password failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                Toast.makeText(this@ChangePasswordFragment.activity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
    companion object {
        private const val TOKEN = "token"
        private const val EMAIL = "email"

        @JvmStatic
        fun newInstance(token: String, email: String) =
            ChangePasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(TOKEN, token)
                    putString(EMAIL, email)
                }
            }
    }
}