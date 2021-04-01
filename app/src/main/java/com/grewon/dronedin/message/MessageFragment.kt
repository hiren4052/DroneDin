package com.grewon.dronedin.message

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseFragment
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.helper.AspectImageView
import com.grewon.dronedin.message.contract.MessageContract
import com.grewon.dronedin.message.adapter.MessagesAdapter
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.MessagesDataBean
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.layout_square_toolbar.*
import retrofit2.Retrofit
import javax.inject.Inject


class MessageFragment : BaseFragment(), MessagesAdapter.OnItemClickListeners,
    SwipeRefreshLayout.OnRefreshListener, MessageContract.View {

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var messagesPresenter: MessageContract.Presenter

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
        setLayoutManager()
        initView()
    }

    private fun initView() {
        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        messagesPresenter.attachView(this)
        messagesPresenter.attachApiInterface(retrofit)

        apiCall()

    }

    private fun apiCall() {
        messagesPresenter.getMessages()
    }


    private fun setLayoutManager() {


        message_data_recycle.setLayoutManager(
            LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
            )
        )

        message_data_recycle.setRefreshListener(this)
        message_data_recycle.setRefreshingColorResources(
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark
        )
    }


    override fun onItemClick(jobsDataBean: MessagesDataBean.Data?) {
        startActivity(
            Intent(requireContext(), ChatActivity::class.java).putExtra(
                AppConstant.ID,
                jobsDataBean?.userDetails?.userId
            )
        )
    }

    override fun onDeleteItem(jobsDataBean: MessagesDataBean.Data?, adapterPosition: Int) {
    }

    override fun onRefresh() {
        apiCall()
    }

    override fun onApiException(error: Int) {
        setEmptyView(getString(error), R.drawable.ic_connectivity)
    }

    override fun onMessageGetSuccessful(response: MessagesDataBean) {
        if (context != null && isVisible) {
            if (response.data != null && response.data.size > 0) {
                initAdapter()
                messageAdapter?.addItemsList(response.data)
            } else {
                setEmptyView(response.msg.toString(), R.drawable.ic_no_data)
            }
        }
    }

    override fun onMessageGetFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            setEmptyView(loginParams.msg, R.drawable.ic_no_data)
        }
    }

    private fun setEmptyView(errorMessage: String, emptyDrawable: Int?) {
        if (context != null && isVisible) {
            initAdapter()
            if (emptyDrawable != null) {
                val errorImage: AspectImageView =
                    message_data_recycle.emptyView.findViewById(R.id.txt_no_data_image)
                errorImage.setImageResource(emptyDrawable)
            }

            val error: TextView =
                message_data_recycle.emptyView.findViewById(R.id.txt_no_data_title)
            error.text = errorMessage
        }

    }

    private fun initAdapter() {
        messageAdapter = MessagesAdapter(requireContext(), this)
        message_data_recycle.adapter = messageAdapter
    }


}