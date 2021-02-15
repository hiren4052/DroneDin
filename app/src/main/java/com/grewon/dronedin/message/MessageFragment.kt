package com.grewon.dronedin.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.grewon.dronedin.R
import com.grewon.dronedin.message.dapter.MessagesAdapter
import com.grewon.dronedin.server.MessagesDataBean
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.layout_square_toolbar.*


class MessageFragment : Fragment(), MessagesAdapter.OnItemClickListeners {

    private var messageAdapter: MessagesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        txt_toolbar_title.text = getString(R.string.messages)
        setMessageAdapter()
    }

    private fun setMessageAdapter() {
        message_data_recycle.layoutManager = LinearLayoutManager(context)
        messageAdapter = MessagesAdapter(requireContext(), this)
        message_data_recycle.adapter = messageAdapter
    }

    override fun onItemClick(jobsDataBean: MessagesDataBean.Result?) {

    }

    override fun onDeleteItem(jobsDataBean: MessagesDataBean.Result?, adapterPosition: Int) {
    }

}