package com.grewon.dronedin.onlineoffline

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.onlineoffline.contract.OnlineStatusContract
import com.grewon.dronedin.server.CommonMessageBean
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Created by Jeff Klima on 6/28/18.
 */
class OnlineOfflineService : Service(), OnlineStatusContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var onlineStatusPresenter: OnlineStatusContract.Presenter

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.extras != null) {
            DroneDinApp.getAppInstance().getAppComponent().inject(this)
            onlineStatusPresenter.attachView(this)
            onlineStatusPresenter.attachApiInterface(retrofit)

            if (intent.getStringExtra(AppConstant.TAG) != null) {
                onlineStatusPresenter.changeOnlineStatus(
                    intent.getStringExtra(AppConstant.TAG).toString()
                )
            }
        }
        return START_NOT_STICKY
    }

    override fun onApiException(error: Int) {
        stopSelf()
    }

    override fun onUserOnlineSuccessful(response: CommonMessageBean) {
        stopSelf()
    }

    override fun onUserOnlineFailed(loginParams: CommonMessageBean) {
        stopSelf()
    }

}