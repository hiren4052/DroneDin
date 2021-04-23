package com.grewon.dronedin.message

import android.Manifest
import android.animation.Animator
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewAnimationUtils
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
import com.grewon.dronedin.helper.FileValidationUtils
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.message.adapter.ChatAdapter
import com.grewon.dronedin.message.contract.ChatContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.SentMessageParams
import com.grewon.dronedin.utils.TimeUtils
import com.grewon.dronedin.utils.ValidationUtils
import com.jakewharton.rxbinding.widget.RxTextView
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.activity_chat.*
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ChatActivity : BaseActivity(), View.OnClickListener, ChatAdapter.OnItemClickListeners,
    ChatContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var chatPresenter: ChatContract.Presenter

    private var chatAdapter: ChatAdapter? = null
    private var chatRoomId: String = ""
    private var receiverId: String = ""
    private var newMessageHandler: Handler? = null
    private var mIsLastItem = false
    private var isLoading = true
    private var isOldMessageServiceCalled = false
    private var dateArrayList = ArrayList<String>()
    private var isSearch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

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
                    chatPresenter.getNewMessage(chatAdapter?.getTopId().toString(), chatRoomId)
                }
                newMessageHandler?.postDelayed(this, 3000)
            }
        }

        newMessageHandler?.postDelayed(runnable, 3000)

    }

    private fun cancelMessageHandler() {
        if (newMessageHandler != null) {
            newMessageHandler?.removeCallbacksAndMessages(null)
            newMessageHandler = null
        }
    }


    private fun openSearch() {
        search_open_view.visibility = View.VISIBLE
        val circularReveal = ViewAnimationUtils.createCircularReveal(
            search_open_view,
            (img_search.right + img_search.left) / 2,
            (img_search.top + img_search.bottom) / 2,
            0f, search_open_view.width.toFloat()
        )
        circularReveal.duration = 300
        circularReveal.start()
    }

    private fun closeSearch() {
        val circularConceal = ViewAnimationUtils.createCircularReveal(
            search_open_view,
            (img_search.right + img_search.left) / 2,
            (img_search.top + img_search.bottom) / 2,
            search_open_view.width.toFloat(), 0f
        )

        circularConceal.duration = 300
        circularConceal.start()
        circularConceal.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) = Unit
            override fun onAnimationCancel(animation: Animator?) = Unit
            override fun onAnimationStart(animation: Animator?) = Unit
            override fun onAnimationEnd(animation: Animator?) {
                search_open_view.visibility = View.INVISIBLE

                circularConceal.removeAllListeners()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelMessageHandler()
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        chat_recycle.layoutManager = layoutManager
        layoutManager.stackFromEnd = true
        chatAdapter = ChatAdapter(this, this)
        chat_recycle.adapter = chatAdapter
    }


    private fun setClicks() {
        im_camera.setOnClickListener(this)
        im_back.setOnClickListener(this)
        im_attachments.setOnClickListener(this)
        fab_send_message.setOnClickListener(this)
        img_search.setOnClickListener(this)
        txt_cancel.setOnClickListener(this)
        txt_apply.setOnClickListener(this)
        txt_started_date.setOnClickListener(this)
        txt_ended_date.setOnClickListener(this)
    }

    private fun initView() {

        receiverId = intent.getStringExtra(AppConstant.ID).toString()


        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        chatPresenter.attachView(this)
        chatPresenter.attachApiInterface(retrofit)

        chatPresenter.createChatRoom(CreateChatRoomParams(receiverId))


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





    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_back -> {
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

                    val chatParams = SentMessageParams()
                    chatParams.chat_room_id = chatRoomId
                    chatParams.reciever_id = receiverId
                    chatParams.msg = edt_message.text.toString().trim()
                    chatParams.msg_type = "Text"

                    chatPresenter.sentMessage(chatParams)
                }
            }

            R.id.img_search -> {
                openSearch()
            }
            R.id.txt_cancel -> {

                if (!ValidationUtils.isEmptyFiled(search_input_text.text.toString()) || !ValidationUtils.isEmptyFiled(
                        txt_started_date.text.toString()
                    )
                    || !ValidationUtils.isEmptyFiled(txt_ended_date.text.toString())
                ) {
                    search_input_text.setText("")
                    txt_started_date.text = ""
                    txt_ended_date.text = ""
                    reStartChat(true)
                }
                closeSearch()

            }
            R.id.txt_apply -> {
                reStartChat(false)
                closeSearch()

            }

            R.id.txt_started_date -> {
                TimeUtils.showDatePickerDialogForText(this, txt_started_date)
            }

            R.id.txt_ended_date -> {
                TimeUtils.showDatePickerDialogForText(this, txt_ended_date)
            }
        }
    }

    private fun reStartChat(isStartHandler: Boolean) {
        mIsLastItem = false
        isLoading = true
        isOldMessageServiceCalled = false
        dateArrayList = ArrayList()

        initAdapter()

        if (isStartHandler)
            startNewMessageHandler()
        else
            cancelMessageHandler()

        getOldMessage()

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

    override fun onImageItemClick(jobsDataBean: ChatDataBean.Data?) {

    }

    override fun onImageDownloadClick(jobsDataBean: ChatDataBean.Data?) {

    }

    override fun onApiException(error: Int) {
        finish()
    }

    override fun onCreateChatRoomSuccessful(response: ChatRoomBean) {
        if (response.data != null) {
            setView(response.data.recieverDetail)
            chatRoomId = response.data.chatroom?.chatRoomId.toString()
            startNewMessageHandler()
            getOldMessage()
        }
    }

    private fun getOldMessage() {


        chatPresenter.getOldMessage(
            chatAdapter?.getLastBottomId().toString(),
            chatRoomId,
            search_input_text.text.toString(), txt_started_date.text.toString(), txt_ended_date.text.toString()
        )


    }

    private fun setView(recieverDetail: ChatRoomBean.Data.RecieverDetail?) {
        if (!isFinishing) {
            Glide.with(this)
                .load(recieverDetail?.profileImage!!)
                .apply(RequestOptions().placeholder(R.drawable.ic_user_place_holder))
                .into(img_user)
        }
        txt_user_name.text = recieverDetail?.userName
        if (recieverDetail?.isUserOnline == AppConstant.YES_STATUS) {
            txt_online_status.visibility = View.VISIBLE
            txt_online_status.text = getString(R.string.online)
        } else {
            //txt_online_status.visibility = View.GONE
            txt_online_status.text = getString(
                R.string.last_seen, TimeUtils.convertDate(
                    recieverDetail?.lastSeen.toString(),
                    "yyyy-MM-dd HH:mm:ss",
                    "dd/MM/yyyy hh:mm a",
                    true
                )
            )
        }
    }

    override fun onCreateChatRoomFailed(loginParams: CommonMessageBean) {
        finish()
    }

    override fun onMessageSentSuccessfully(response: SentChatBean) {
        if (response.data != null) {
            edt_message.setText("")
            edt_message.clearFocus()
            val chatDataBean = ChatDataBean.Data()
            chatDataBean.msg = response.data.msg
            chatDataBean.chatMsgDatecreated = response.data.chatMsgDatecreated
            chatDataBean.recieverId = response.data.recieverId
            chatDataBean.senderId = response.data.senderId
            chatDataBean.msgType = response.data.msgType
            chatDataBean.extension = response.data.extension
            chatDataBean.isRead = response.data.isRead
            chatDataBean.chatMsgId = response.data.chatMsgId
            setSingleItemToAdapter(chatDataBean)
            scrollBottom()
        }
    }

    private fun setSingleItemToAdapter(chatDataBean: ChatDataBean.Data) {
        val withoutDateList = ArrayList<ChatDataBean.Data>()
        withoutDateList.add(chatDataBean)
        val withDateList = getNewSentDateWiseList(withoutDateList)
        chatAdapter?.addOffsetMessageItemsList(withDateList)
        // chatAdapter?.addMessageItem(chatDataBean)
    }

    override fun onMessageSendFailed(loginParams: CommonMessageBean) {
        DroneDinApp.getAppInstance().showToast(loginParams.msg.toString())
    }

    override fun onOldMessageGetSuccessfully(response: ChatDataBean) {
        if (response.data != null && response.data.size > 0) {

            val withDateList = getDateWiseList(response.data)
            chatAdapter?.addOldMessageItemsList(withDateList)
            if (!isOldMessageServiceCalled) {
                scrollToEnd()
            }
            isOldMessageServiceCalled = true
        }

    }

    private fun getNewSentDateWiseList(withOutDateList: ArrayList<ChatDataBean.Data>): ArrayList<ChatDataBean.Data> {
        val withDateList = ArrayList<ChatDataBean.Data>()
        for (bean in withOutDateList) {
            if (!dateArrayList.contains(TimeUtils.getMessageHeaderDisplayDate(bean.chatMsgDatecreated!!))) {

                dateArrayList.add(TimeUtils.getMessageHeaderDisplayDate(bean.chatMsgDatecreated!!))
                LogX.E(dateArrayList.toString())
                withDateList.add(bean)
                if (dateArrayList.size > 0) {
                    val messagesBean = ChatDataBean.Data()
                    messagesBean.chatMsgDatecreated = dateArrayList[dateArrayList.size - 1]
                    messagesBean.msgType = "Date"
                    messagesBean.chatMsgId = bean.chatMsgId
                    withDateList.add(messagesBean)
                }
            } else {
                withDateList.add(bean)
            }
        }
        return withDateList
    }


    private fun getDateWiseList(withOutDateList: ArrayList<ChatDataBean.Data>): ArrayList<ChatDataBean.Data> {
        val withDateList = ArrayList<ChatDataBean.Data>()
        for (bean in withOutDateList) {
            if (!dateArrayList.contains(TimeUtils.getMessageHeaderDisplayDate(bean.chatMsgDatecreated!!))) {
                if (dateArrayList.size > 0) {
                    val messagesBean = ChatDataBean.Data()
                    messagesBean.chatMsgDatecreated = dateArrayList[dateArrayList.size - 1]
                    messagesBean.msgType = "Date"
                    messagesBean.chatMsgId = bean.chatMsgId
                    withDateList.add(messagesBean)
                }
                dateArrayList.add(TimeUtils.getMessageHeaderDisplayDate(bean.chatMsgDatecreated!!))
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
                val withDateList = ArrayList<ChatDataBean.Data>()
                val messagesBean = ChatDataBean.Data()
                messagesBean.chatMsgDatecreated = dateArrayList[dateArrayList.size - 1]
                messagesBean.msgType = "Date"
                messagesBean.chatMsgId =
                    chatAdapter?.itemList!![0].chatMsgId
                withDateList.add(messagesBean)
                chatAdapter?.addOldMessageItemsList(withDateList)
            }
        }
        mIsLastItem = true

    }

    override fun onNewMessageGetSuccessfully(response: ChatDataBean) {
        if (response.data != null && response.data.size > 0) {
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
                            val chatParams = SentMessageParams()
                            chatParams.chat_room_id = chatRoomId
                            chatParams.reciever_id = receiverId
                            chatParams.msg = filePath
                            chatParams.msg_type = "File"

                            chatPresenter.sentMessage(chatParams)

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