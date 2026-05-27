package com.example.demo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.demo.ui.chat.ChatBottomSheet
import com.example.demo.ui.course.CourseFragment
import com.example.demo.ui.dashboard.DashboardFragment
import com.example.demo.ui.graph.GraphFragment
import com.example.demo.ui.lab.LabFragment
import com.example.demo.ui.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        val bottomNav = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_nav)

        // Show dashboard by default
        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.nav_home
            loadFragment(DashboardFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_home -> DashboardFragment()
                R.id.nav_course -> CourseFragment()
                R.id.nav_lab -> LabFragment()
                R.id.nav_graph -> GraphFragment()
                R.id.nav_me -> ProfileFragment()
                else -> return@setOnItemSelectedListener false
            }
            loadFragment(fragment)
            true
        }

        // AI Mentor floating button
        findViewById<android.view.View>(R.id.fab_ai_mentor).setOnClickListener {
            ChatBottomSheet().show(supportFragmentManager, "ChatBottomSheet")
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
