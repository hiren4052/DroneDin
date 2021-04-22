package com.grewon.dronedin.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseFragment
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.clientjobs.active.ClientActiveJobsDetailsActivity
import com.grewon.dronedin.clientjobs.history.ClientJobHistoryDetailsActivity
import com.grewon.dronedin.dispute.DisputeDetailsActivity
import com.grewon.dronedin.enum.JOB_STATUS
import com.grewon.dronedin.enum.NOTIFICATION_TYPE
import com.grewon.dronedin.helper.AspectImageView
import com.grewon.dronedin.notifications.adapter.NotificationsAdapter
import com.grewon.dronedin.notifications.contract.NotificationContract
import com.grewon.dronedin.pilotactivejobs.PilotActiveJobsDetailActivity
import com.grewon.dronedin.pilotjobhistory.PilotHistoryDetailsActivity
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.NotificationDataBean
import com.malinskiy.superrecyclerview.OnMoreListener
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.layout_square_toolbar.*
import retrofit2.Retrofit
import javax.inject.Inject


class NotificationsFragment : BaseFragment(), NotificationsAdapter.OnItemClickListeners,
    SwipeRefreshLayout.OnRefreshListener, OnMoreListener, NotificationContract.View {


    @Inject
    lateinit var notificationsPresenter: NotificationContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit

    private var notificationsAdapter: NotificationsAdapter? = null

    private var pageCount: Int = 0

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
        initView()
    }

    private fun initView() {
        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        notificationsPresenter.attachView(this)
        notificationsPresenter.attachApiInterface(retrofit)


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
        message_data_recycle.setupMoreListener(this, 1)

        apiCall(pageCount)

    }

    private fun apiCall(pageCount: Int) {
        notificationsPresenter.getNotification(pageCount.toString())
    }


    private fun initMessageAdapter() {
        notificationsAdapter = NotificationsAdapter(requireContext(), this)
        message_data_recycle.adapter = notificationsAdapter
    }

    override fun onItemClick(jobsDataBean: NotificationDataBean.Data?) {

        if (jobsDataBean?.jobStatus != NOTIFICATION_TYPE.Announcement.name) {
            if (isPilotAccount()) {
                if (jobsDataBean?.notificationType == NOTIFICATION_TYPE.Offer.name) {
                    if (jobsDataBean.jobStatus == JOB_STATUS.active.name) {
                        startActivity(
                            Intent(
                                requireContext(),
                                PilotActiveJobsDetailActivity::class.java
                            ).putExtra(AppConstant.ID, jobsDataBean.offerId)
                        )
                    } else if (jobsDataBean.jobStatus == JOB_STATUS.completed.name || jobsDataBean.jobStatus == JOB_STATUS.cancelled.name) {
                        startActivity(
                            Intent(
                                requireContext(),
                                PilotHistoryDetailsActivity::class.java
                            ).putExtra(AppConstant.ID, jobsDataBean.offerId)
                        )
                    }
                } else if (jobsDataBean?.notificationType == NOTIFICATION_TYPE.Job.name) {
                    if (jobsDataBean.jobStatus == JOB_STATUS.active.name) {
                        startActivity(
                            Intent(
                                requireContext(),
                                PilotActiveJobsDetailActivity::class.java
                            ).putExtra(AppConstant.ID, jobsDataBean.offerId)
                        )
                    } else if (jobsDataBean.jobStatus == JOB_STATUS.completed.name || jobsDataBean.jobStatus == JOB_STATUS.cancelled.name) {
                        startActivity(
                            Intent(
                                requireContext(),
                                PilotHistoryDetailsActivity::class.java
                            ).putExtra(AppConstant.ID, jobsDataBean.offerId)
                        )
                    }
                } else if (jobsDataBean?.notificationType == NOTIFICATION_TYPE.Dispute.name) {
                    startActivity(
                        Intent(
                            requireContext(),
                            DisputeDetailsActivity::class.java
                        ).putExtra(AppConstant.ID, jobsDataBean.disputeId)
                    )
                }
            } else {
                if (jobsDataBean?.notificationType == NOTIFICATION_TYPE.Offer.name) {
                    if (jobsDataBean.jobStatus == JOB_STATUS.active.name) {
                        startActivity(
                            Intent(
                                requireContext(),
                                ClientActiveJobsDetailsActivity::class.java
                            ).putExtra(AppConstant.ID, jobsDataBean.offerId)
                        )
                    } else if (jobsDataBean.jobStatus == JOB_STATUS.completed.name || jobsDataBean.jobStatus == JOB_STATUS.cancelled.name) {
                        startActivity(
                            Intent(
                                requireContext(),
                                ClientJobHistoryDetailsActivity::class.java
                            ).putExtra(AppConstant.ID, jobsDataBean.offerId)
                        )
                    }
                } else if (jobsDataBean?.notificationType == NOTIFICATION_TYPE.Job.name) {
                    if (jobsDataBean.jobStatus == JOB_STATUS.active.name) {
                        startActivity(
                            Intent(
                                requireContext(),
                                ClientActiveJobsDetailsActivity::class.java
                            ).putExtra(AppConstant.ID, jobsDataBean.offerId)
                        )
                    } else if (jobsDataBean.jobStatus == JOB_STATUS.completed.name || jobsDataBean.jobStatus == JOB_STATUS.cancelled.name) {
                        startActivity(
                            Intent(
                                requireContext(),
                                ClientJobHistoryDetailsActivity::class.java
                            ).putExtra(AppConstant.ID, jobsDataBean.offerId)
                        )
                    }
                } else if (jobsDataBean?.notificationType == NOTIFICATION_TYPE.Dispute.name) {
                    startActivity(
                        Intent(
                            requireContext(),
                            DisputeDetailsActivity::class.java
                        ).putExtra(AppConstant.ID, jobsDataBean.disputeId)
                    )
                }
            }

        }


    }

    override fun onDeleteItem(jobsDataBean: NotificationDataBean.Data?, adapterPosition: Int) {

    }


    override fun onRefresh() {
        message_data_recycle.setupMoreListener(this, 1)
        pageCount = 1
        apiCall(pageCount)
        message_data_recycle.setRefreshing(false)
    }

    override fun onMoreAsked(
        overallItemsCount: Int,
        itemsBeforeMore: Int,
        maxLastVisiblePosition: Int
    ) {
        pageCount += 1
        apiCall(pageCount)
    }

    private fun setEmptyView(errorMessage: String, emptyDrawable: Int?) {
        if (context != null && isVisible) {
            initMessageAdapter()
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

    private fun stopMore() {
        message_data_recycle.hideMoreProgress()
        message_data_recycle.setupMoreListener(null, 0)
    }

    override fun onApiException(error: Int) {
        if (pageCount == 0) {
            setEmptyView(getString(error), R.drawable.ic_connectivity)
        }
    }

    override fun onNotificationGetSuccessful(response: NotificationDataBean) {
        if (context != null && isVisible) {
            if (response.data != null && response.data.size > 0) {
                if (pageCount == 0) {
                    initMessageAdapter()
                    val intent = Intent(AppConstant.NOTIFICATION_BROADCAST) //action: "msg"
                    intent.putExtra(AppConstant.TAG, "received")
                    LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
                }
                notificationsAdapter?.addItemsList(response.data)
            } else {
                if (pageCount == 0) {
                    setEmptyView(response.msg.toString(), R.drawable.ic_no_data)
                }
                stopMore()
            }
        }
    }

    override fun onNotificationGetFailed(loginParams: CommonMessageBean) {
        if (context != null && isVisible) {
            if (loginParams.msg != null) {
                setEmptyView(loginParams.msg, R.drawable.ic_no_data)
            }
        }
    }


}