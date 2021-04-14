package com.grewon.dronedin.earnings.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface EarningContract {

    interface View : BaseContract.View {


        fun onApiException(error: Int)

        fun onMonthlyDataGetSuccessful(response: MonthlyEarningBean)

        fun onMonthlyDataGetFailed(loginParams: CommonMessageBean)

        fun onWeeklyDataGetSuccessful(response: WeeklyDataBean)

        fun onnWeeklyDataGetFailed(loginParams: CommonMessageBean)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun getMonthlyEarning()

        fun getWeeklyEarning()

    }

}