package com.grewon.dronedin.mapscreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.extraadapter.ChipEquipmentsAdapter
import com.grewon.dronedin.extraadapter.ChipSkillsAdapter
import com.grewon.dronedin.filter.contract.FilterContract
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.message.ChatActivity
import com.grewon.dronedin.pilotfindjobs.FindJobsDetailsActivity
import com.grewon.dronedin.pilotmyjobs.contract.PilotFindJobsDetailContract
import com.grewon.dronedin.pilotprofile.contract.PilotProfileContract
import com.grewon.dronedin.review.ReviewActivity
import com.grewon.dronedin.review.adapter.ReviewsAdapter
import com.grewon.dronedin.server.*
import com.grewon.dronedin.server.params.FilterParams
import com.grewon.dronedin.utils.IconUtils
import com.grewon.dronedin.utils.MapUtils
import com.grewon.dronedin.utils.TimeUtils
import kotlinx.android.synthetic.main.activity_pilot_profile.*
import kotlinx.android.synthetic.main.jobs_map_bottom_dialog.view.*
import kotlinx.android.synthetic.main.jobs_map_bottom_dialog.view.txt_category_name
import kotlinx.android.synthetic.main.pilot_map_bottom_dialog.view.*
import retrofit2.Retrofit
import java.util.ArrayList
import javax.inject.Inject


class JobsMapScreenActivity : BaseActivity(), OnMapReadyCallback,
    View.OnClickListener, GoogleMap.OnMarkerClickListener, FilterContract.View,
    PilotFindJobsDetailContract.View, PilotProfileContract.View {


    private var latitude: Double? = 0.0 // Latitude
    private var longitude: Double? = 0.0// Longitude
    private var mMap: GoogleMap? = null
    private var gpsTracker: GPSTracker? = null

    @Inject
    lateinit var filterPresenter: FilterContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit

    private var filterParams: FilterParams? = null

    @Inject
    lateinit var pilotFindJobsDetailsPresenter: PilotFindJobsDetailContract.Presenter

    private var jobId: String = ""
    private var profileId: String = ""

    @Inject
    lateinit var pilotProfilePresenter: PilotProfileContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobs_map_screen)

        initView()
        setClickListeners()


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment



        mapFragment.getMapAsync(this)


    }

    private fun initView() {
        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        filterPresenter.attachView(this)
        filterPresenter.attachApiInterface(retrofit)



        filterParams = intent.getParcelableExtra(AppConstant.BEAN)

    }

    override fun onResume() {
        super.onResume()
        pilotFindJobsDetailsPresenter.attachView(this)
        pilotFindJobsDetailsPresenter.attachApiInterface(retrofit)


        pilotProfilePresenter.attachView(this)
        pilotProfilePresenter.attachApiInterface(retrofit)
    }

    private fun setClickListeners() {


    }

    override fun onMapReady(mMap: GoogleMap?) {
        this.mMap = mMap!!
        mMap.setOnMarkerClickListener(this)
        locationPermission()

    }

    private fun locationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    AppConstant.LOCATION_PERMISSION_REQUEST_CODE
                )
            } else {
                setCurrentLocation()
            }
        } else {
            setCurrentLocation()
        }
    }

    private fun setCurrentLocation() {
        gpsTracker = GPSTracker(this, this)
        latitude = gpsTracker!!.latitude
        longitude = gpsTracker!!.longitude

        apiCall()



        mMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    latitude!!, longitude!!
                ), 15f
            )
        )
    }

    private fun apiCall() {
        if (isPilotAccount()) {
            filterParams?.page = "0"
            filterPresenter.getPilotJobs(filterParams!!)
        } else {

            filterParams?.page = "0"
            filterPresenter.getPilotData(filterParams!!)
        }
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {


        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        if (isPilotAccount()) {
            jobId = p0?.tag.toString()
            LogX.E(p0?.tag.toString())
            pilotFindJobsDetailsPresenter.getPilotJobDetails(jobId, "pilot_job_detail")
        } else {
            profileId = p0?.tag.toString()
            pilotProfilePresenter.getPilotProfile(profileId)
        }
        return true
    }


    @SuppressLint("InflateParams")
    private fun openJobsDetailDialog(response: PilotFindJobsDetailBean) {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.jobs_map_bottom_dialog, null)
        val dialog = BottomSheetDialog(
            this, R.style.CustomBottomSheetDialogTheme
        )
        dialog.setContentView(view)

        val chip_skills = view.chip_skills
        val chip_equipments = view.chip_equipments
        val txt_details = view.txt_details
        val txt_category_name = view.txt_category_name
        val txt_job_title = view.txt_job_title
        val txt_job_description = view.txt_job_description
        val txt_client_name = view.txt_client_name
        val txt_budget = view.txt_budget
        val txt_job_date = view.txt_job_date
        val txt_location = view.txt_job_location

        txt_location.text = response.jobAddress
        txt_job_title.text = response.jobTitle
        txt_category_name.text = response.category?.categoryName
        txt_job_description.text = response.jobDescription
        txt_client_name.text = response.userName
        txt_budget.text = getString(R.string.price_string, response.totalPrice)
        txt_job_date.text = TimeUtils.getServerToAppDate(response.date.toString())



        if (response.skill != null && response.skill.size > 0) {
            val skillsList = response.skill.map { it.skill.toString() }
            setSkillsAdapter(skillsList, chip_skills)
        }

        if (response.equipment != null && response.equipment.size > 0) {
            val skillsList = response.equipment.map { it.equipment.toString() }
            setEquipmentsData(skillsList, chip_equipments)
        }

        txt_details.setOnClickListener {
            startActivity(
                Intent(this, FindJobsDetailsActivity::class.java).putExtra(
                    AppConstant.ID,
                    jobId
                )
            )
        }

        dialog.show()

    }

    private fun setSkillsAdapter(skillsList: List<String>, chipRecycle: RecyclerView) {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        chipRecycle.layoutManager = layoutManager
        val skillsAdapter = ChipSkillsAdapter(this, R.color.colorPrimary_3d, R.color.gray_d4)
        chipRecycle.adapter = skillsAdapter
        skillsAdapter.addItemsList(skillsList as ArrayList<String>)

    }

    private fun setEquipmentsData(equipmentsList: List<String>, equipmentRecycle: RecyclerView) {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        equipmentRecycle.layoutManager = layoutManager
        val equipmentsAdapter = ChipEquipmentsAdapter(this, R.color.colorPrimary_5d, R.color.white)
        equipmentRecycle.adapter = equipmentsAdapter
        equipmentsAdapter.addItemsList(equipmentsList as ArrayList<String>)

    }


    @SuppressLint("InflateParams")
    private fun openPilotDetailDialog(response: PilotProfileBean) {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.pilot_map_bottom_dialog, null)
        val dialog = BottomSheetDialog(
            this, R.style.CustomBottomSheetDialogTheme
        )
        dialog.setContentView(view)

        val textUserName = view.txt_user_name
        val textCategoryName = view.txt_category_name
        val textPrice = view.txt_price
        val textRatings = view.txt_ratings
        val textViewAll = view.txt_view_all
        val reviewRecycle = view.review_recycle
        val textMessage = view.txt_message
        val imageUser = view.img_user
        val locationDistance = view.txt_location_distance
        val noReviewLayout = view.no_review_layout

        textPrice.text = getString(R.string.usd, response.profilePrice)

        textUserName.text = response.userName
        textCategoryName.text = response.category?.categoryName

        if (response.rate != null) {
            textRatings.text = response.rate.toString()
        } else {
            textRatings.text = "0"
        }

        locationDistance.text = getString(
            R.string.km_away, MapUtils.calculationByDistance(
                LatLng(latitude!!, longitude!!),
                LatLng(response.userLatitude?.toDouble()!!, response.userLongitude?.toDouble()!!)
            )
        )


        Glide.with(this)
            .load(response.profileImage!!)
            .apply(RequestOptions().placeholder(R.drawable.ic_user_place_holder))
            .into(imageUser)


        if (response.review != null && response.review.size > 0) {
            if (response.review.size > 5) {
                val list = ArrayList<ReviewsDataBean>()

                for ((position, item) in response.review.withIndex()) {
                    if (position <= 4) {
                        list.add(item)
                    }
                }

                textViewAll.visibility = View.VISIBLE
                reviewRecycle.layoutManager = LinearLayoutManager(this)
                val reviewAdapter = ReviewsAdapter(this)
                reviewRecycle.adapter = reviewAdapter
                reviewAdapter.addItemsList(list)

            } else {
                reviewRecycle.layoutManager = LinearLayoutManager(this)
                val reviewAdapter = ReviewsAdapter(this)
                reviewRecycle.adapter = reviewAdapter
                reviewAdapter.addItemsList(response.review)
                textViewAll.visibility = View.GONE
            }

            noReviewLayout.visibility = View.GONE
        } else {
            textViewAll.visibility = View.GONE
            noReviewLayout.visibility = View.VISIBLE
        }



        textViewAll.setOnClickListener {
            startActivity(
                Intent(this, ReviewActivity::class.java).putExtra(
                    AppConstant.BEAN,
                    response.review
                )
            )
        }

        textMessage.setOnClickListener {
            startActivity(
                Intent(this, ChatActivity::class.java).putExtra(
                    AppConstant.ID,
                    profileId
                )
            )
        }



        dialog.show()

    }

    override fun onJobsDetailSuccessfully(response: PilotFindJobsDetailBean) {
        if (response != null) {
            openJobsDetailDialog(response)
        }
    }

    override fun onJobsDetailFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

    override fun onProfileDetailSuccessfully(response: PilotProfileBean) {
        if (response != null) {
            openPilotDetailDialog(response)
        }
    }

    override fun onProfileDetailFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

    override fun showOnScreenProgress() {
        showProgress()
    }

    override fun hideOnScreenProgress() {
        hideProgress()
    }

    override fun onApiException(error: Int) {

    }

    override fun onPilotDataGetSuccessful(response: PilotDataBean) {
        if (response.data != null) {
            loadPilotMapData(response.data)
        }
    }

    private fun loadPilotMapData(data: ArrayList<PilotDataBean.Data>) {
        for (i in data) {

            val marker = mMap?.addMarker(
                MarkerOptions().icon(
                    IconUtils.bitmapDescriptorFromVector(this, R.drawable.ic_pilot_map)
                ).position(LatLng(i.userLatitude?.toDouble()!!, i.userLongitude?.toDouble()!!))
            )
            marker?.tag = i.userId
        }
    }

    override fun onPilotDataGetFailed(loginParams: CommonMessageBean) {
    }

    override fun onPilotSaveSuccessful(response: CommonMessageBean) {
    }

    override fun onPilotSaveFailed(loginParams: CommonMessageBean) {

    }

    override fun onJobsDataGetSuccessful(response: PilotJobsDataBean) {
        if (response.data != null) {
            loadJobsMapData(response.data)
        }
    }

    private fun loadJobsMapData(data: ArrayList<PilotJobsDataBean.Data>) {
        for (i in data) {
            val marker = mMap?.addMarker(
                MarkerOptions().icon(
                    IconUtils.bitmapDescriptorFromVector(this, R.drawable.ic_jobs_map)
                ).position(LatLng(i.jobLongitude?.toDouble()!!, i.jobLongitude.toDouble()))
            )
            marker?.tag = i.jobId
            LogX.E(i.jobId.toString())
        }


    }

    override fun onJobsDataGetFailed(loginParams: CommonMessageBean) {

    }

    override fun onJobsSaveSuccessful(response: CommonMessageBean) {

    }

    override fun onJobsSaveFailed(loginParams: CommonMessageBean) {

    }


}
