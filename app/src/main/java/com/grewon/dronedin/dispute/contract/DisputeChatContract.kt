package com.grewon.dronedin.dispute.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface DisputeChatContract {

    interface View : BaseContract.View {

        fun onApiException(error: Int)

        fun onMessageSentSuccessfully(response: SentDisputeChatBean)

        fun onMessageSendFailed(loginParams: CommonMessageBean)

        fun onOldMessageGetSuccessfully(response: DisputeChatDataBean)

        fun onOldMessageGetFailed(loginParams: CommonMessageBean)

        fun onNewMessageGetSuccessfully(response: DisputeChatDataBean)

        fun onNewMessageGetFailed(loginParams: CommonMessageBean)

        fun showTopProgress()

        fun hideTopProgress()

    }

    interface Presenter : BaseContract.Presenter<View> {


        fun sentDisputeMessage(sentMessageParams: SentDisputeMessageParams)

        fun getDisputeOldMessage(offsetId: String, disputeId: String)

        fun getDisputeNewMessage(offsetId: String, disputeId: String)

    }

}