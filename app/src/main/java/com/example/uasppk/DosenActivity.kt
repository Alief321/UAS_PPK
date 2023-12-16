package com.example.uasppk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.uasppk.databinding.DosenActivityBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DosenActivity : AppCompatActivity() {
    private lateinit var binding: DosenActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DosenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name") ?: ""
        val token = intent.getStringExtra("token") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val id = intent.getIntExtra("id", 0)
        val role = intent.getStringExtra("roles") ?: "mahasiswa"

        Log.d("email", email)
        val homeFragment = HomeFragment.newInstance(name, token)
        val matkulFragment = MatkulFragment.newInstance(token, id)
        val nilaiFragment = NilaiFragment.newInstance(token, id)
        val ipFragment = IPFragment.newInstance(token, id)
        val profileFragment = ProfileFragment.newInstance(token, email, role)
//      default
        replaceFragment(homeFragment)
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> replaceFragment(homeFragment)
                R.id.navigation_nilai -> replaceFragment(nilaiFragment)
                R.id.navigation_courses -> replaceFragment(matkulFragment)
                R.id.navigation_profile -> replaceFragment(profileFragment)
                R.id.navigation_about -> replaceFragment(AboutFragment())

                else -> {
                    replaceFragment(homeFragment)
                }
            }
            true
        }
        val fab: FloatingActionButton = findViewById(R.id.fab2)
        fab.setOnClickListener {
            Toast.makeText(this@DosenActivity, "Logout sukses", Toast.LENGTH_LONG)
                .show()
            val switchIntent = Intent(this@DosenActivity, LoginActivity::class.java)
            startActivity(switchIntent)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}
