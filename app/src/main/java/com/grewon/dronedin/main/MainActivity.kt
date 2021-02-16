package com.grewon.dronedin.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.clientjobs.ClientJobsFragment
import com.grewon.dronedin.message.MessageFragment
import com.grewon.dronedin.notifications.NotificationsFragment
import com.grewon.dronedin.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(ClientJobsFragment())
        setClicks()
    }

    private fun setClicks() {
        bottom_navigation.onNavigationItemSelectedListener = this
    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_contain, fragment)
            .commit()
    }

    fun showFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_contain, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_contain)
        fragment!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.im_jobs -> {
                showFragment(ClientJobsFragment())
                return true
            }
            R.id.im_messages -> {
                showFragment(MessageFragment())
                return true
            }
            R.id.im_notifications -> {
                showFragment(NotificationsFragment())
                return true
            }
            R.id.im_settings -> {
                showFragment(SettingsFragment())
                return true
            }
        }
        return false
    }

}