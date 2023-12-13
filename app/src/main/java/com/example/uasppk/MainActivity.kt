package com.example.uasppk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.uasppk.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.internal.and

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")?: ""
        val token = intent.getStringExtra("token")?: ""
        val email = intent.getStringExtra("email")?: ""
        val id = intent.getIntExtra("id", 0)

        Log.d("email", email)
        val homeFragment = HomeFragment.newInstance(name, token)
        val profileFragment = ProfileFragment.newInstance(token, email)
        val matkulFragment = MatkulFragment.newInstance(token, id)
//      default
        replaceFragment(homeFragment)
        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.navigation_home -> replaceFragment(homeFragment)
                R.id.navigation_nilai -> replaceFragment(profileFragment)
                R.id.navigation_courses -> replaceFragment(matkulFragment)
                R.id.navigation_grades -> replaceFragment(HomeFragment())
                R.id.navigation_about -> replaceFragment(AboutFragment())

                else ->{
                    replaceFragment(homeFragment)
                }

            }
            true
        }

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            // Tampilkan navigasi melayang dengan menu_navigation.xml sebagai sumber
            PopupMenu(this, fab).apply {
                menuInflater.inflate(R.menu.menu_float, menu)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.navigation_nilai -> {
                            replaceFragment(profileFragment)
                            true
                        }
                        R.id.navigation_logout -> {
                            Toast.makeText(this@MainActivity, "Logout sukses", Toast.LENGTH_LONG).show()
                            val switchIntent = Intent(this@MainActivity, LoginActivity::class.java)
                            startActivity(switchIntent)
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }


    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()


    }
}