package com.grewon.dronedin.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.clientjobs.ClientJobsFragment
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.main.contract.MainContract
import com.grewon.dronedin.message.MessageFragment
import com.grewon.dronedin.notifications.NotificationsFragment
import com.grewon.dronedin.pilotfindjobs.PilotFindJobsFragment
import com.grewon.dronedin.pilotmyjobs.PilotMyJobsFragment
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.MainScreenData
import com.grewon.dronedin.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import q.rorbin.badgeview.Badge
import q.rorbin.badgeview.QBadgeView
import retrofit2.Retrofit
import javax.inject.Inject

class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    MainContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var mainScreenPresenter: MainContract.Presenter

    private var notificationBadgeView: Badge? = null
    private var messageBadgeView: Badge? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        setClicks()
    }

    private fun mainAPICall() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // Get new FCM registration token

            val token = task.result
            mainScreenPresenter.getMainScreenData(token)

        })
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            notificationReceiver,
            IntentFilter(AppConstant.NOTIFICATION_BROADCAST)
        )
        mainAPICall()
    }


    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver)
    }


    private val notificationReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null && intent.extras != null) {
                mainAPICall()
            }
        }
    }


    private fun initView() {

        notificationBadgeView = QBadgeView(this)
        messageBadgeView = QBadgeView(this)


        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        mainScreenPresenter.attachView(this)
        mainScreenPresenter.attachApiInterface(retrofit)



        LogX.E(DroneDinApp.getAppInstance().getDeviceInformation())
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
            bottom_navigation.menu.getItem(0).icon =
                ContextCompat.getDrawable(this, R.drawable.ic_jobs_selector)
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

    override fun onApiException(error: Int) {

    }

    override fun onMainScreenDataGetSuccessful(response: MainScreenData) {
        preferenceUtils.saveProfileData(response)
        if (response.data != null) {
            val fragment = supportFragmentManager.findFragmentById(R.id.main_contain)

            if(fragment is PilotFindJobsFragment){
                fragment.onSNACK(response.data)
            }


            if (response.data.totalUnreadNotification != "0") {
                addBadgeAt(
                    3,
                    response.data.totalUnreadNotification?.toInt()!!
                )
            } else {
                removeBadgeAt(3)
            }


            if (response.data.totalUnreadMsg != "0") {
                addBadgeAt(2, response.data.totalUnreadMsg?.toInt()!!)
            } else {
                removeBadgeAt(2)
            }
        }
    }

    override fun onMainScreenDataGetFailed(loginParams: CommonMessageBean) {

    }

    private fun addBadgeAt(position: Int, number: Int) {
        // add badge
        if (isPilotAccount()) {
            if (position == 2 && messageBadgeView != null) {
                messageBadgeView?.setBadgeNumber(number)
                    ?.setGravityOffset(15f, 0.2f, true)
                    ?.bindTarget(bottom_navigation.getBottomNavigationItemView(position))?.isExactMode =
                    false

            }
            if (position == 3 && notificationBadgeView != null) {
                notificationBadgeView?.setBadgeNumber(number)
                    ?.setGravityOffset(15f, 0.2f, true)
                    ?.bindTarget(bottom_navigation.getBottomNavigationItemView(position))?.isExactMode =
                    false

            }
        } else {
            if (position == 2 && messageBadgeView != null) {
                messageBadgeView?.setBadgeNumber(number)
                    ?.setGravityOffset(15f, 0.2f, true)
                    ?.bindTarget(bottom_navigation.getBottomNavigationItemView(position))?.isExactMode =
                    false

            }
            if (position == 3 && notificationBadgeView != null) {
                notificationBadgeView?.setBadgeNumber(number)
                    ?.setGravityOffset(25f, 1f, true)
                    ?.bindTarget(bottom_navigation.getBottomNavigationItemView(position))?.isExactMode =
                    false

            }
        }
    }

    private fun removeBadgeAt(position: Int) {

        if (isPilotAccount()) {
            if (position == 2 && messageBadgeView != null) {
                // remove badge
                messageBadgeView
                    ?.setBadgeNumber(0)
                    ?.setGravityOffset(15f, 0.2f, true)
                    ?.bindTarget(bottom_navigation.getBottomNavigationItemView(position))
                    ?.hide(true)
            }

            if (position == 3 && notificationBadgeView != null) {
                // remove badge
                notificationBadgeView
                    ?.setBadgeNumber(0)
                    ?.setGravityOffset(15f, 1f, true)
                    ?.bindTarget(bottom_navigation.getBottomNavigationItemView(position))
                    ?.hide(true)
            }
        } else {
            if (position == 2 && messageBadgeView != null) {
                // remove badge
                messageBadgeView
                    ?.setBadgeNumber(0)
                    ?.setGravityOffset(15f, 0.2f, true)
                    ?.bindTarget(bottom_navigation.getBottomNavigationItemView(position))
                    ?.hide(true)
            }

            if (position == 3 && notificationBadgeView != null) {
                // remove badge
                notificationBadgeView
                    ?.setBadgeNumber(0)
                    ?.setGravityOffset(25f, 0.2f, true)
                    ?.bindTarget(bottom_navigation.getBottomNavigationItemView(position))
                    ?.hide(true)
            }
        }

    }



}