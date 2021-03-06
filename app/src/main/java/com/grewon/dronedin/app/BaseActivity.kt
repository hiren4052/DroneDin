package com.grewon.dronedin.app


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.grewon.dronedin.R
import com.grewon.dronedin.dialogs.LoadingDialog
import com.grewon.dronedin.dialogs.SingleAlertViewDialog
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.splash.SplashActivity
import com.grewon.dronedin.utils.PreferenceUtils

import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject


open class BaseActivity : AppCompatActivity(), BaseContract.View {

    @Inject
    lateinit var preferenceUtils: PreferenceUtils

    private var dialog: LoadingDialog? = null

    private var alertViewDialog: SingleAlertViewDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DroneDinApp.getAppInstance().getAppComponent().inject(this)
    }

    override fun onResume() {
        super.onResume()

        LocalBroadcastManager.getInstance(this).registerReceiver(
            sessionReceiver,
            IntentFilter(AppConstant.SESSION_BROADCAST)
        )
    }

    private val sessionReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {


                if (intent.getStringExtra(AppConstant.DATA_TYPE) != null) {
                    if (intent.getStringExtra(AppConstant.DATA_TYPE) == "yes") {
                        if(preferenceUtils.getLoginCredentials()!=null)
                        displaySessionDialog()
                    }
                }
            }
        }
    }

    private fun displaySessionDialog() {

        if (alertViewDialog == null) {
            alertViewDialog = SingleAlertViewDialog(this, R.style.DialogThme)
            alertViewDialog?.setTitle(
                "Oh no, your session expired!\n" +
                        "Please login again."
            )
            alertViewDialog?.setPositiveBtnTxt(getString(R.string.ok))
            alertViewDialog?.setOkListener(View.OnClickListener {
                preferenceUtils.flushData()
                alertViewDialog?.dismiss()
                startActivity(Intent(this, SplashActivity::class.java))
                finishAffinity()
            })
            if (!this.isFinishing)
                alertViewDialog?.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(sessionReceiver)
    }


    override fun showProgress() {
        if (dialog == null) {
            initDialog()
        } else {
            if (dialog != null && dialog?.isShowing == true) {
                dialog?.dismiss()
                initDialog()
            } else {
                initDialog()
            }
        }
    }

    private fun initDialog() {
        if (!isFinishing) {
            dialog = LoadingDialog(this)
            dialog?.show()
        }
    }

    override fun hideProgress() {
        if (!isFinishing && dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }

    override fun attachBaseContext(newBase: Context) {

        super.attachBaseContext(
            ViewPumpContextWrapper.wrap(
                newBase
            )

        )
    }

    fun isPilotAccount(): Boolean {
        LogX.E("-------" + preferenceUtils.getLoginCredentials()?.data?.userType.toString())
        return preferenceUtils.getLoginCredentials()?.data?.userType == AppConstant.USER_PILOT
    }


}