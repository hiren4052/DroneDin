package com.grewon.dronedin.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.message.dapter.ChatAdapter
import com.grewon.dronedin.server.ChatDataBean
import com.grewon.dronedin.utils.ValidationUtils
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.ArrayList

class ChatActivity : BaseActivity(), View.OnClickListener, ChatAdapter.OnItemClickListeners {

    private var chatAdapter: ChatAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        initView()
        setClicks()
        setAdapter()

    }

    private fun setAdapter() {
        chat_recycle.layoutManager = LinearLayoutManager(this)
        chatAdapter = ChatAdapter(this, this)
        chat_recycle.adapter = chatAdapter
        chatAdapter?.addMessageItemsList(getChatList())
    }

    private fun getChatList(): ArrayList<ChatDataBean.Result> {
        val chatList = ArrayList<ChatDataBean.Result>()
        chatList.add(ChatDataBean.Result("message", 0))
        chatList.add(ChatDataBean.Result("date", 0))
        chatList.add(ChatDataBean.Result("image", 0))
        chatList.add(ChatDataBean.Result("message", 1))
        chatList.add(ChatDataBean.Result("date", 1))
        chatList.add(ChatDataBean.Result("image", 1))
        chatList.add(ChatDataBean.Result("message", 1))
        chatList.add(ChatDataBean.Result("image", 1))
        chatList.add(ChatDataBean.Result("message", 0))
        chatList.add(ChatDataBean.Result("message", 1))
        return chatList
    }

    private fun setClicks() {
        im_camera.setOnClickListener(this)
        im_back.setOnClickListener(this)
        im_attachments.setOnClickListener(this)
        fab_send_message.setOnClickListener(this)
    }

    private fun initView() {
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

            }
        }
    }

    override fun onImageItemClick(jobsDataBean: ChatDataBean.Result?) {

    }

    override fun onImageDownloadClick(jobsDataBean: ChatDataBean.Result?) {
    }
}