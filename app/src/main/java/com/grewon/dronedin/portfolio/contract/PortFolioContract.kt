package com.grewon.dronedin.portfolio.contract

import com.grewon.dronedin.app.BaseContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.IdentificationBean
import com.grewon.dronedin.server.ProfileBean
import com.grewon.dronedin.server.UserData
import com.grewon.dronedin.server.params.*


interface PortFolioContract {

    interface View : BaseContract.View {

        fun onAddPortFolioSuccessful(response: CommonMessageBean)

        fun onAddPortFolioFailed(loginParams: AddPortFolioParams)

        fun onApiException(error: Int)

        fun onUpdatePortFolioSuccessful(response: CommonMessageBean)

        fun onUpdatePortFolioFailed(loginParams: AddPortFolioParams)

        fun onDeleteAttachmentSuccessfully(commonMessageBean: CommonMessageBean)

        fun onDeleteAttachmentFailed(commonMessageBean: CommonMessageBean)

        fun onDeletePortFolioSuccessfully(commonMessageBean: CommonMessageBean)

        fun onDeletePortFolioFailed(commonMessageBean: CommonMessageBean)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun addPortFolio(params: AddPortFolioParams)

        fun updatePortFolio(params: AddPortFolioParams, portFolioId: String)

        fun deleteAttachment(attachmentId: String)

        fun deletePortFolio(portFolioId: String)

    }

}