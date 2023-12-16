package com.example.uasppk

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uasppk.client.ApiClient
import com.example.uasppk.model.response.ProfileResponse
import com.example.uasppk.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TOKEN = "token"
private const val EMAIL = "email"
private const val ROLE = "role"

class ProfileFragment : Fragment() {
    private var isPasswordVisible = false

    private var token: String? = null
    private var email: String? = null
    private var role: String? = null

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = ApiClient.apiService
        Log.d("Cek", "masuk onCreate")
        arguments?.let {
            token = it.getString(TOKEN)
            email = it.getString(EMAIL)
            role = it.getString(ROLE)
        }
        Log.d("Role", "role $role")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d("Cek", "masuk OnCreateView")
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Token", token!!)
        Log.d("email", email!!)
        Log.d("Cek", "masuk OnViewCreated")
        getProfile()

        val updateButton = view.findViewById<Button>(R.id.toUpdateButton)
        val deleteButton = view.findViewById<Button>(R.id.deleteButton)

        deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(this@ProfileFragment.activity)
            builder.setMessage("Apakah Anda yakin ingin menghapus akun?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    deleteAkun()
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        updateButton.setOnClickListener {
            val fragment = ChangePasswordFragment.newInstance(token!!, email!!)
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    private fun getProfile() {
        val call = apiService.profile("Bearer " + token)

        call.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>,
            ) {
                if (response.isSuccessful) {
                    val profileResponse = response.body()
                    val name = this@ProfileFragment.view?.findViewById<TextView>(R.id.profile_name)
                    val email =
                        this@ProfileFragment.view?.findViewById<TextView>(R.id.profile_email)
                    val nim = this@ProfileFragment.view?.findViewById<TextView>(R.id.profile_nim)
                    val kelas =
                        this@ProfileFragment.view?.findViewById<TextView>(R.id.profile_kelas)
                    val textview1 = this@ProfileFragment.view?.findViewById<TextView>(R.id.textView)
                    val textview5 =
                        this@ProfileFragment.view?.findViewById<TextView>(R.id.textView5)
                    nim?.text = profileResponse?.nim
                    name?.text = profileResponse?.name
                    email?.text = profileResponse?.email
                    kelas?.text = profileResponse?.kelas

                    fun getmatkull(): String {
                        return profileResponse?.matkulAmpu!!.joinToString("\n")
                    }
                    if (role!! == "Dosen") {
                        textview1?.text = "NIP"
                        textview5?.text = "Matkul Ampu"
                        nim?.text = profileResponse?.nip
                        kelas?.text = getmatkull()
                    }
                } else {
                    Toast.makeText(
                        this@ProfileFragment.activity,
                        "Get profile failed",
                        Toast.LENGTH_SHORT,
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(
                    this@ProfileFragment.activity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT,
                )
                    .show()
            }
        })
    }

    fun deleteAkun() {
        val call = apiService.deleteAkun("Bearer " + token)

        call.enqueue(
            object : Callback<ProfileResponse> {
                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>,
                ) {
                    if (response.isSuccessful) {
                        val profileResponse = response.body()
                        Toast.makeText(
                            this@ProfileFragment.activity,
                            "Akun berhasil dihapus",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val switchIntent =
                            Intent(this@ProfileFragment.activity, LoginActivity::class.java)
                        startActivity(switchIntent)
                    } else {
                        Toast.makeText(
                            this@ProfileFragment.activity,
                            "Akun gagal dihapus",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Toast.makeText(
                        this@ProfileFragment.activity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            },
        )
    }

    companion object {
        private const val TOKEN = "token"
        private const val EMAIL = "email"

        @JvmStatic
        fun newInstance(token: String, email: String, role: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(TOKEN, token)
                    putString(EMAIL, email)
                    putString(ROLE, role)
                }
            }
    }
}
