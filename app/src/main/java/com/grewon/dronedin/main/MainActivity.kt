package com.grewon.dronedin.main

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.clientjobs.ClientJobsFragment
import com.grewon.dronedin.message.MessageFragment
import com.grewon.dronedin.notifications.NotificationsFragment
import com.grewon.dronedin.pilotfindjobs.PilotFindJobsFragment
import com.grewon.dronedin.pilotmyjobs.PilotMyJobsFragment
import com.grewon.dronedin.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        setClicks()
    }

    private fun initView() {
        if (isPilotAccount()) {
            loadFragment(PilotFindJobsFragment())


            bottom_navigation.menu.getItem(0).icon =
                ContextCompat.getDrawable(this, R.drawable.ic_search)
            bottom_navigation.background = ContextCompat.getDrawable(this, R.color.colorPrimary)
            val colors = intArrayOf(
                ContextCompat.getColor(this, R.color.light_purple),
                ContextCompat.getColor(this, R.color.white)
            )

            val states = arrayOf(
                intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_enabled, android.R.attr.state_checked)
            )
            bottom_navigation.itemTextColor = ColorStateList(states, colors)
            bottom_navigation.itemIconTintList = ColorStateList(states, colors)
        } else {
            loadFragment(ClientJobsFragment())

            bottom_navigation.menu.getItem(1).isVisible = false
            bottom_navigation.menu.getItem(0).icon = ContextCompat.getDrawable(this, R.drawable.ic_jobs_selector)
            bottom_navigation.background = ContextCompat.getDrawable(this, R.color.white)

            val colors = intArrayOf(
                ContextCompat.getColor(this, R.color.view_color),
                ContextCompat.getColor(this, R.color.span_text_color)
            )

            val states = arrayOf(
                intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_enabled, android.R.attr.state_checked)
            )
            bottom_navigation.itemTextColor = ColorStateList(states, colors)
            bottom_navigation.itemIconTintList = ColorStateList(states, colors)


        }

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
                if (isPilotAccount()) {
                    showFragment(PilotFindJobsFragment())
                } else {
                    showFragment(ClientJobsFragment())
                }
                return true
            }
            R.id.im_my_jobs -> {
                showFragment(PilotMyJobsFragment())
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