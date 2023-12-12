package com.example.uasppk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.uasppk.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")?: ""
        val token = intent.getStringExtra("token")?: ""

        val homeFragment = HomeFragment.newInstance(name, token)
        val profileFragment = ProfileFragment.newInstance(token)
//      default
        replaceFragment(homeFragment)
        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.navigation_home -> replaceFragment(homeFragment)
                R.id.navigation_profile -> replaceFragment(profileFragment)
                R.id.navigation_courses -> replaceFragment(HomeFragment())
                R.id.navigation_grades -> replaceFragment(HomeFragment())
                R.id.navigation_about -> replaceFragment(AboutFragment())

                else ->{
                    replaceFragment(homeFragment)
                }

            }

            true

        }


    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()


    }
}