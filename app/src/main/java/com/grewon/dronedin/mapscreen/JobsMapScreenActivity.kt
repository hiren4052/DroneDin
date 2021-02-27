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
import com.grewon.dronedin.extraadapter.ChipEquipmentsAdapter
import com.grewon.dronedin.extraadapter.ChipSkillsAdapter
import com.grewon.dronedin.review.ReviewActivity
import com.grewon.dronedin.review.adapter.ReviewsAdapter
import com.grewon.dronedin.utils.IconUtils
import com.grewon.dronedin.utils.ListUtils
import kotlinx.android.synthetic.main.jobs_map_bottom_dialog.view.*
import kotlinx.android.synthetic.main.jobs_map_bottom_dialog.view.txt_category_name
import kotlinx.android.synthetic.main.pilot_map_bottom_dialog.view.*


class JobsMapScreenActivity : BaseActivity(), OnMapReadyCallback,
    View.OnClickListener, GoogleMap.OnMarkerClickListener {


    private var latitude: Double? = 0.0 // Latitude
    private var longitude: Double? = 0.0// Longitude
    private var mMap: GoogleMap? = null
    private var gpsTracker: GPSTracker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobs_map_screen)

        setClickListeners()


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment



        mapFragment.getMapAsync(this)


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

        if (isPilotAccount()) {
            for (i in 0..5) {
                mMap?.addMarker(
                    MarkerOptions().icon(
                        IconUtils.bitmapDescriptorFromVector(this, R.drawable.ic_jobs_map)
                    ).position(LatLng(latitude!! + i, longitude!! + i))
                )
            }
        } else {
            for (i in 0..5) {
                mMap?.addMarker(
                    MarkerOptions().icon(
                        IconUtils.bitmapDescriptorFromVector(this, R.drawable.ic_pilot_map)
                    ).position(LatLng(latitude!! + i, longitude!! + i))
                )
            }
        }

        mMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    latitude!!, longitude!!
                ), 15f
            )
        )
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {


        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        if (isPilotAccount()) {
            openJobsDetailDialog()
        } else {
            openPilotDetailDialog()
        }
        return true
    }


    @SuppressLint("InflateParams")
    private fun openJobsDetailDialog() {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.jobs_map_bottom_dialog, null)
        val dialog = BottomSheetDialog(
            this, R.style.CustomBottomSheetDialogTheme
        )
        dialog.setContentView(view)

        var chip_skills = view.chip_skills
        var chip_equipments = view.chip_equipments
        var txt_contact = view.txt_contact

        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        chip_skills.layoutManager = layoutManager
        val skillsAdapter = ChipSkillsAdapter(this, R.color.colorPrimary_3d, R.color.gray_d4)
        chip_skills.adapter = skillsAdapter
        skillsAdapter.addItemsList(ListUtils.getSkillsBean())

        val elayoutManager = FlexboxLayoutManager(this)
        elayoutManager.flexDirection = FlexDirection.ROW
        elayoutManager.justifyContent = JustifyContent.FLEX_START
        chip_equipments.layoutManager = elayoutManager
        val equipmentsAdapter = ChipEquipmentsAdapter(this, R.color.colorPrimary_5d, R.color.white)
        chip_equipments.adapter = equipmentsAdapter
        equipmentsAdapter.addItemsList(ListUtils.getEquipmentsBean())

        dialog.show()

    }


    @SuppressLint("InflateParams")
    private fun openPilotDetailDialog() {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.pilot_map_bottom_dialog, null)
        val dialog = BottomSheetDialog(
            this, R.style.CustomBottomSheetDialogTheme
        )
        dialog.setContentView(view)

        var textUserName = view.txt_user_name
        var textCategoryName = view.txt_category_name
        var textPrice = view.txt_price
        var textRatings = view.txt_ratings
        var textViewAll = view.txt_view_all
        var reviewRecycle = view.review_recycle
        var textInvite = view.txt_invite

        reviewRecycle.layoutManager = LinearLayoutManager(this)
        var reviewAdapter = ReviewsAdapter(this)
        reviewRecycle.adapter = reviewAdapter

        textViewAll.setOnClickListener {
            startActivity(Intent(this, ReviewActivity::class.java))
        }



        dialog.show()

    }


}
