package com.grewon.dronedin.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grewon.dronedin.R
import com.grewon.dronedin.dialogs.LoadingDialog
import com.grewon.dronedin.utils.PreferenceUtils
import javax.inject.Inject


open class BaseFragment : Fragment(), BaseContract.View {

    @Inject
    lateinit var preferenceUtils: PreferenceUtils

    lateinit var dialog: LoadingDialog

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        DroneDinApp.getAppInstance().getAppComponent().inject(this)
    }

    fun isPilotAccount(): Boolean {
        return true
    }

    override fun showProgress() {
        dialog = LoadingDialog(requireContext())
        dialog.show()
    }

    override fun hideProgress() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }
}