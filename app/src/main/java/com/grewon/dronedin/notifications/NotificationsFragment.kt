package com.grewon.dronedin.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.grewon.dronedin.R
import com.grewon.dronedin.notifications.adapter.NotificationsAdapter
import com.grewon.dronedin.server.NotificationDataBean
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.layout_square_toolbar.*


class NotificationsFragment : Fragment(), NotificationsAdapter.OnItemClickListeners {


    private var notificationsAdapter: NotificationsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        txt_toolbar_title.text = getString(R.string.notifications)
        setMessageAdapter()
    }

    private fun setMessageAdapter() {
        message_data_recycle.layoutManager = LinearLayoutManager(context)
        notificationsAdapter = NotificationsAdapter(requireContext(), this)
        message_data_recycle.adapter = notificationsAdapter
    }

    override fun onItemClick(jobsDataBean: NotificationDataBean.Result?) {

    }

    override fun onDeleteItem(jobsDataBean: NotificationDataBean.Result?, adapterPosition: Int) {
    }

}