package com.example.uasppk

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_fragment)

        val Halouser : TextView = findViewById(R.id.haloUser)
        Halouser.text = getString(R.string.HaloUser, intent.getStringExtra("name"))

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> loadFragment(HomeFragment())
                R.id.navigation_profile -> loadFragment(ProfileFragment())
//                R.id.navigation_courses -> loadFragment(CoursesFragment())
//                R.id.navigation_grades -> loadFragment(GradesFragment())
//                R.id.navigation_about -> loadFragment(GpaFragment())
            }
            true
        }

        // Default fragment on startup
        loadFragment(ProfileFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(com.google.android.material.R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}