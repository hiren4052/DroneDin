package com.grewon.dronedin.dispute

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.dispute.adapter.DisputeChatAdapter
import com.grewon.dronedin.dispute.contract.DisputeChatContract
import com.grewon.dronedin.helper.FileValidationUtils
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.SentDisputeMessageParams
import com.grewon.dronedin.utils.TimeUtils
import com.grewon.dronedin.utils.ValidationUtils
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.activity_dispute_chat.*
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*
import retrofit2.Retrofit
import javax.inject.Inject

class DisputeChatActivity : BaseActivity(), View.OnClickListener, DisputeChatAdapter.OnItemClickListeners,
    DisputeChatContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var chatPresenter: DisputeChatContract.Presenter

    private var chatAdapter: DisputeChatAdapter? = null
    private var disputeId: String = ""
    private var newMessageHandler: Handler? = null
    private var mIsLastItem = false
    private var isLoading = true
    private var isOldMessageServiceCalled = false
    private var dateArrayList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispute_chat)

        initAdapter()
        initView()
        setClicks()
        setRecycleListeners()



    }

    override fun onResume() {
        super.onResume()
        DroneDinApp.isChatScreen = true
    }

    override fun onStop() {
        super.onStop()
        DroneDinApp.isChatScreen = false
    }

    private fun setRecycleListeners() {
        chat_recycle.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val llm = recyclerView.layoutManager as LinearLayoutManager?
                if (llm != null && !mIsLastItem && !isLoading && chatAdapter!!.itemCount >= 10 && llm.findLastVisibleItemPosition() == chatAdapter!!.itemCount - 1) {
                    getOldMessage()
                }
            }
        })
    }

    private fun startNewMessageHandler() {
        newMessageHandler = Handler()

        val runnable: Runnable = object : Runnable {
            override fun run() {
                if (isOldMessageServiceCalled) {
                    chatPresenter.getDisputeNewMessage(chatAdapter?.getTopId().toString(), disputeId)
                }
                newMessageHandler?.postDelayed(this, 3000)
            }
        }

        newMessageHandler?.postDelayed(runnable, 3000)

    }

    override fun onDestroy() {
        super.onDestroy()
        if (newMessageHandler != null) {
            newMessageHandler?.removeCallbacksAndMessages(null)
        }
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        chat_recycle.layoutManager = layoutManager
        layoutManager.stackFromEnd = true
        chatAdapter = DisputeChatAdapter(this, this)
        chat_recycle.adapter = chatAdapter
    }


    private fun setClicks() {
        im_camera.setOnClickListener(this)
        im_attachments.setOnClickListener(this)
        fab_send_message.setOnClickListener(this)
        img_back.setOnClickListener(this)
    }

    private fun initView() {

        txt_toolbar_title.text = getString(R.string.disputes)

        disputeId = intent.getStringExtra(AppConstant.ID).toString()


        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        chatPresenter.attachView(this)
        chatPresenter.attachApiInterface(retrofit)


        chatPresenter.getDisputeOldMessage(chatAdapter?.getLastBottomId().toString(), disputeId)

        edt_message.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (ValidationUtils.isEmptyFiled(s.toString())) {
                    fab_send_message.hide()
                } else {
                    fab_send_message.show()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        startNewMessageHandler()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.im_camera -> {
                openPhotosRequest()
            }
            R.id.im_attachments -> {
                openFileRequest()
            }
            R.id.fab_send_message -> {
                if (ValidationUtils.isEmptyFiled(edt_message.text.toString())) {
                    DroneDinApp.getAppInstance().showToast(getString(R.string.please_type_message))
                } else {

                    val chatParams = SentDisputeMessageParams()
                    chatParams.dispute_id = disputeId
                    chatParams.msg = edt_message.text.toString().trim()
                    chatParams.msg_type = "Text"

                    chatPresenter.sentDisputeMessage(chatParams)
                }
            }
        }
    }

    private fun openPhotosRequest() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    AppConstant.CAMERA_PERMISSION_REQUEST_CODE
                )
            } else {
                FilePickerBuilder.instance
                    .setMaxCount(1) //optional
                    .enableCameraSupport(true)
                    .setActivityTheme(R.style.AppLibAppTheme) //optional
                    .pickPhoto(this, AppConstant.PICKFILE_REQUEST_CODE)
            }
        } else {
            FilePickerBuilder.instance
                .setMaxCount(1) //optional
                .enableCameraSupport(true)
                .setActivityTheme(R.style.AppLibAppTheme) //optional
                .pickPhoto(this, AppConstant.PICKFILE_REQUEST_CODE)
        }
    }


    private fun openFileRequest() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    AppConstant.CAMERA_PERMISSION_REQUEST_CODE
                )
            } else {
                FilePickerBuilder.instance
                    .setMaxCount(1) //optional
                    .setActivityTheme(R.style.AppLibAppTheme) //optional
                    .pickFile(this, AppConstant.PICKFILE_REQUEST_CODE)
            }
        } else {
            FilePickerBuilder.instance
                .setMaxCount(1) //optional
                .setActivityTheme(R.style.AppLibAppTheme) //optional
                .pickFile(this, AppConstant.PICKFILE_REQUEST_CODE)
        }
    }

    override fun onImageItemClick(jobsDataBean: DisputeChatDataBean.Data?) {

    }

    override fun onImageDownloadClick(jobsDataBean: DisputeChatDataBean.Data?) {

    }

    override fun onApiException(error: Int) {
        finish()
    }



    private fun getOldMessage() {
        chatPresenter.getDisputeOldMessage(chatAdapter?.getLastBottomId().toString(), disputeId)
    }




    override fun onMessageSentSuccessfully(response: SentDisputeChatBean) {
        if (response.data != null) {
            edt_message.setText("")
            edt_message.clearFocus()
            val chatDataBean = DisputeChatDataBean.Data()
            chatDataBean.msg = response.data?.msg
            chatDataBean.groupChatMsgDatecreated = response.data?.groupChatMsgDatecreated
            chatDataBean.senderId = response.data?.senderId
            chatDataBean.msgType = response.data?.msgType
            chatDataBean.extension = response.data?.extension
            chatDataBean.isRead = response.data?.isRead
            chatDataBean.groupChatMsgId = response.data?.groupChatMsgId
            setSingleItemToAdapter(chatDataBean)
            scrollBottom()
        }
    }

    private fun setSingleItemToAdapter(chatDataBean: DisputeChatDataBean.Data) {
        val withoutDateList=ArrayList<DisputeChatDataBean.Data>()
        withoutDateList.add(chatDataBean)
        val withDateList = getDateWiseList(withoutDateList)
        chatAdapter?.addOffsetMessageItemsList(withDateList)
     //   chatAdapter?.addMessageItem(chatDataBean)
    }

    override fun onMessageSendFailed(loginParams: CommonMessageBean) {
        DroneDinApp.getAppInstance().showToast(loginParams.msg.toString())
    }

    override fun onOldMessageGetSuccessfully(response: DisputeChatDataBean) {
        if (response.data != null && response.data!!.size > 0) {
            val withDateList = getDateWiseList(response.data!!)
            chatAdapter?.addOldMessageItemsList(withDateList)
            if (!isOldMessageServiceCalled) {
                scrollToEnd()
            }
            isOldMessageServiceCalled = true
        }

    }

    private fun getDateWiseList(withOutDateList: ArrayList<DisputeChatDataBean.Data>): ArrayList<DisputeChatDataBean.Data> {
        val withDateList = ArrayList<DisputeChatDataBean.Data>()
        for (bean in withOutDateList) {
            if (!dateArrayList.contains(TimeUtils.getMessageHeaderDisplayDate(bean.groupChatMsgDatecreated!!))) {
                if (dateArrayList.size > 0) {
                    val messagesBean = DisputeChatDataBean.Data()
                    messagesBean.groupChatMsgDatecreated = dateArrayList[dateArrayList.size - 1]
                    messagesBean.msgType = "Date"
                    messagesBean.groupChatMsgId = bean.groupChatMsgId
                    withDateList.add(messagesBean)
                }
                dateArrayList.add(TimeUtils.getMessageHeaderDisplayDate(bean.groupChatMsgDatecreated!!))
                withDateList.add(bean)
            } else {
                withDateList.add(bean)
            }
        }
        return withDateList
    }


    private fun scrollToEnd() {
        Handler().postDelayed({
            chat_recycle.smoothScrollToPosition(0)

        }, 300)
    }


    override fun onOldMessageGetFailed(loginParams: CommonMessageBean) {
        isOldMessageServiceCalled = true
        if (chatAdapter != null && chatAdapter?.itemList != null && chatAdapter?.itemList!!.size > 0 && !mIsLastItem) {
            if (dateArrayList.size > 0) {
                val withDateList = ArrayList<DisputeChatDataBean.Data>()
                val messagesBean = DisputeChatDataBean.Data()
                messagesBean.groupChatMsgId = dateArrayList[dateArrayList.size - 1]
                messagesBean.msgType = "Date"
                messagesBean.groupChatMsgId = chatAdapter?.itemList!![0].groupChatMsgId
                withDateList.add(messagesBean)
                chatAdapter?.addOldMessageItemsList(withDateList)
            }
        }
        mIsLastItem = true
    }

    override fun onNewMessageGetSuccessfully(response: DisputeChatDataBean) {
        if (response.data != null && response.data!!.size > 0) {
            mIsLastItem = false
            val withDateList = getDateWiseList(response.data!!)
            chatAdapter?.addOffsetMessageItemsList(withDateList)

        }

    }

    private fun scrollBottom() {
        chat_recycle.smoothScrollToPosition(0)
    }


    override fun onNewMessageGetFailed(loginParams: CommonMessageBean) {
        mIsLastItem = true
    }

    override fun showTopProgress() {
        isLoading = true
        top_progress.visibility = View.VISIBLE
    }

    override fun hideTopProgress() {
        isLoading = false
        top_progress.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            AppConstant.PICKFILE_REQUEST_CODE -> {
                if (data != null) {
                    if (resultCode == RESULT_OK) {
                        val fileList =
                            data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_DOCS)
                        if (fileList != null) {

                            val filePath: String = FileValidationUtils.getPath(this, fileList[0])
                            val chatParams = SentDisputeMessageParams()
                            chatParams.dispute_id = disputeId
                            chatParams.msg = filePath
                            chatParams.msg_type = "File"

                            chatPresenter.sentDisputeMessage(chatParams)

                            LogX.E(filePath)
                        } else {
                            Toast.makeText(this, R.string.some_thing_went_wrong, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

            }


        }
    }

}