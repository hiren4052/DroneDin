package com.grewon.dronedin.message

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.message.adapter.ChatAdapter
import com.grewon.dronedin.message.contract.ChatContract
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.SentMessageParams
import com.grewon.dronedin.utils.ValidationUtils
import kotlinx.android.synthetic.main.activity_chat.*
import retrofit2.Retrofit
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        initAdapter()
        initView()
        setClicks()
        setRecycleListeners()

    }

    private fun setRecycleListeners() {
        chat_recycle.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val llm = recyclerView.layoutManager as LinearLayoutManager?
                if (llm != null && !mIsLastItem && !isLoading && chatAdapter!!.itemCount >= 10 && llm.findFirstVisibleItemPosition() == 0) {
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
                   // chatPresenter.getNewMessage(chatAdapter?.getTopId().toString(), chatRoomId)
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
        chat_recycle.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        chatAdapter = ChatAdapter(this, this)
        chat_recycle.adapter = chatAdapter
    }


    private fun setClicks() {
        im_camera.setOnClickListener(this)
        im_back.setOnClickListener(this)
        im_attachments.setOnClickListener(this)
        fab_send_message.setOnClickListener(this)
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

            }
            R.id.im_attachments -> {

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
        chatPresenter.getOldMessage(chatAdapter?.getLastBottomId().toString(), chatRoomId)
    }

    private fun setView(recieverDetail: ChatRoomBean.Data.RecieverDetail?) {
        Glide.with(this)
            .load(recieverDetail?.profileImage!!)
            .apply(RequestOptions().placeholder(R.drawable.ic_user_place_holder))
            .into(img_user)
        txt_user_name.text = recieverDetail.userName
        if (recieverDetail.isUserOnline == AppConstant.YES_STATUS) {
            txt_online_status.visibility = View.VISIBLE
        } else {
            txt_online_status.visibility = View.GONE
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
        }
    }

    private fun setSingleItemToAdapter(chatDataBean: ChatDataBean.Data) {
        chatAdapter?.addMessageItem(chatDataBean)
    }

    override fun onMessageSendFailed(loginParams: CommonMessageBean) {
        DroneDinApp.getAppInstance().showToast(loginParams.msg.toString())
    }

    override fun onOldMessageGetSuccessfully(response: ChatDataBean) {
        if (response.data != null && response.data.size > 0) {
            isOldMessageServiceCalled = true
            chatAdapter?.addOldMessageItemsList(response.data)
        }

    }

    override fun onOldMessageGetFailed(loginParams: CommonMessageBean) {
        isOldMessageServiceCalled = true
    }

    override fun onNewMessageGetSuccessfully(response: ChatDataBean) {
        if (response.data != null && response.data.size > 0) {
            mIsLastItem = false
            chatAdapter?.addOffsetMessageItemsList(response.data)
        }

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
}