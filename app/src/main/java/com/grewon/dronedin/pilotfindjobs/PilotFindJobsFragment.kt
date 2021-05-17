package com.grewon.dronedin.pilotfindjobs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.addprofile.AddProfileActivity
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseFragment
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.filter.FilterActivity
import com.grewon.dronedin.helper.AspectImageView
import com.grewon.dronedin.mapscreen.JobsMapScreenActivity
import com.grewon.dronedin.pilotfindjobs.adapter.PilotFindJobsAdapter
import com.grewon.dronedin.pilotfindjobs.contract.PilotJobsContract
import com.grewon.dronedin.savedjobs.SavedJobsActivity
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.MainScreenData
import com.grewon.dronedin.server.PilotJobsDataBean
import com.grewon.dronedin.server.params.FilterParams
import com.grewon.dronedin.utils.ValidationUtils
import com.malinskiy.superrecyclerview.OnMoreListener
import kotlinx.android.synthetic.main.fragment_pilot_find_jobs.*
import retrofit2.Retrofit
import javax.inject.Inject


class PilotFindJobsFragment : BaseFragment(), View.OnClickListener,
    PilotFindJobsAdapter.OnItemClickListeners, PilotJobsContract.View,
    SwipeRefreshLayout.OnRefreshListener, OnMoreListener {


    @Inject
    lateinit var pilotJobsPresenter: PilotJobsContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit


    private var findJobsAdapter: PilotFindJobsAdapter? = null
    private var filterParams: FilterParams? = null
    private var pageCount: Int = 1
    private var adapterPosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pilot_find_jobs, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        setClicks()

    }

    private fun initView() {
        title_user_name.text =
            getString(R.string.hello, preferenceUtils.getLoginCredentials()?.data?.userName)

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        pilotJobsPresenter.attachView(this)
        pilotJobsPresenter.attachApiInterface(retrofit)

        find_job_data_recycle.setLayoutManager(
            LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
            )
        )

        find_job_data_recycle.setRefreshListener(this)
        find_job_data_recycle.setRefreshingColorResources(
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark,
            R.color.colorPrimaryDark
        )
        find_job_data_recycle.setupMoreListener(this, 1)

        if (preferenceUtils.getProfileData() != null && preferenceUtils.getProfileData()?.data != null
        ) {
            onSNACK(preferenceUtils.getProfileData()?.data!!)
        }


    }

    override fun onResume() {
        super.onResume()
        apiCall(pageCount)
    }

    private fun apiCall(pageCount: Int) {
        filterParams = FilterParams()
        filterParams?.page = pageCount.toString()
        pilotJobsPresenter.getPilotJobs(filterParams!!)

    }


    private fun initJobsAdapter() {
        findJobsAdapter = PilotFindJobsAdapter(requireContext(), this)
        find_job_data_recycle.adapter = findJobsAdapter
    }

    private fun setClicks() {
        im_search.setOnClickListener(this)
        image_map.setOnClickListener(this)
        image_save.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_search -> {
                startActivity(Intent(context, FilterActivity::class.java))
            }
            R.id.image_save -> {
                startActivity(Intent(context, SavedJobsActivity::class.java))
            }
            R.id.image_map -> {
                filterParams = FilterParams()
                startActivity(
                    Intent(context, JobsMapScreenActivity::class.java).putExtra(
                        AppConstant.BEAN,
                        filterParams
                    )
                )
            }

        }
    }

    override fun onItemClick(jobsDataBean: PilotJobsDataBean.Data?) {
        startActivity(
            Intent(context, FindJobsDetailsActivity::class.java).putExtra(
                AppConstant.ID,
                jobsDataBean?.jobId
            )
        )
    }

    override fun onJobSaved(jobsDataBean: PilotJobsDataBean.Data?, adapterPosition: Int) {
        this.adapterPosition = adapterPosition
        pilotJobsPresenter.saveJobs(jobsDataBean?.jobId.toString())
    }

    override fun onApiException(error: Int) {
        if (pageCount == 1) {
            setEmptyView(getString(error), R.drawable.ic_connectivity)
        }

    }

    override fun onJobsDataGetSuccessful(response: PilotJobsDataBean) {
        if (context != null && isVisible) {
            if (response.data != null && response.data.size > 0) {
                if (pageCount == 1) {
                    initJobsAdapter()
                }
                findJobsAdapter?.addItemsList(response.data)
            } else {
                if (pageCount == 1) {
                    setEmptyView(response.msg.toString(), R.drawable.ic_no_data)
                }
                stopMore()
            }
        }
    }

    override fun onJobsDataGetFailed(loginParams: CommonMessageBean) {
        if (context != null && isVisible) {
            if (loginParams.msg != null) {
                setEmptyView(loginParams.msg, R.drawable.ic_no_data)
            }
        }

    }

    override fun onJobsSaveSuccessful(response: CommonMessageBean) {
        if (context != null && isVisible) {
            if (response.msg != null) {
                DroneDinApp.getAppInstance().showToast(response.msg)
                if (findJobsAdapter != null)
                    findJobsAdapter?.notifySavedItem(adapterPosition)
            }
        }
    }

    override fun onJobsSaveFailed(loginParams: CommonMessageBean) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))

    }

    override fun onRefresh() {
        find_job_data_recycle.setupMoreListener(this, 1)
        pageCount = 1
        apiCall(pageCount)
        find_job_data_recycle.setRefreshing(false)
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
            initJobsAdapter()
            if (emptyDrawable != null) {
                val errorImage: AspectImageView =
                    find_job_data_recycle.emptyView.findViewById(R.id.txt_no_data_image)
                errorImage.setImageResource(emptyDrawable)
            }
            val error: TextView =
                find_job_data_recycle.emptyView.findViewById(R.id.txt_no_data_title)
            error.text = errorMessage
        }

    }

    private fun stopMore() {
        find_job_data_recycle.hideMoreProgress()
        find_job_data_recycle.setupMoreListener(null, 0)
    }

    fun onSNACK(data: MainScreenData.Data) {
        if (context != null && isVisible) {

            var snackText =
                "Please upload identity document and use premium membership free for one month!"
            var snackColor = R.color.colorPrimary

            if (data.proofFrontSide != null && !ValidationUtils.isEmptyFiled(data.proofFrontSide.toString()) && data.proofBackSide != null && !ValidationUtils.isEmptyFiled(
                    data.proofBackSide.toString()
                )
            ) {
                if (data.userWarningText != null && !ValidationUtils.isEmptyFiled(data.userWarningText.toString())) {
                    snackText = data.userWarningText
                    snackColor = R.color.red
                }
            }

            val snackbar = Snackbar.make(
                coordinate_layout,
                snackText,
                Snackbar.LENGTH_INDEFINITE
            ).setAction(R.string.upload) {
                startActivity(
                    Intent(requireContext(), AddProfileActivity::class.java).putExtra(
                        AppConstant.TAG,
                        true
                    )
                )
            }

            snackbar.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            val snackbarView = snackbar.view
            snackbarView.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    snackColor
                )
            )
            val textView =
                snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            textView.textSize = 14f
            textView.maxLines = 6

            if (ValidationUtils.isEmptyFiled(data.proofFrontSide.toString()) && ValidationUtils.isEmptyFiled(
                    data.proofBackSide.toString()
                )) {
                snackbar.show()
            } else {
                if (data.documentVerified == AppConstant.NO_STATUS) {
                    if (data.userWarningText != null && !ValidationUtils.isEmptyFiled(data.userWarningText.toString())) {
                        snackbar.show()
                    }
                }

            }
        }
    }

}