package com.grewon.dronedin.message.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.*


interface ChatContract {

    interface View : BaseContract.View {

        fun onApiException(error: Int)

        fun onCreateChatRoomSuccessful(response: ChatRoomBean)

        fun onCreateChatRoomFailed(loginParams: CommonMessageBean)

        fun onMessageSentSuccessfully(response: SentChatBean)

        fun onMessageSendFailed(loginParams: CommonMessageBean)

        fun onOldMessageGetSuccessfully(response: ChatDataBean)

        fun onOldMessageGetFailed(loginParams: CommonMessageBean)

        fun onNewMessageGetSuccessfully(response: ChatDataBean)

        fun onNewMessageGetFailed(loginParams: CommonMessageBean)

        fun showTopProgress()

        fun hideTopProgress()

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun createChatRoom(createChatRoomParams: CreateChatRoomParams)

        fun sentMessage(sentMessageParams: SentMessageParams)

        fun getOldMessage(offsetId: String, chatRoomId: String)

        fun getNewMessage(offsetId: String, chatRoomId: String)

    }

}