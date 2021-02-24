package com.grewon.dronedin.mapscreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
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
import com.grewon.dronedin.utils.IconUtils
import com.grewon.dronedin.utils.ListUtils
import com.plumillonforge.android.chipview.Chip
import kotlinx.android.synthetic.main.jobs_map_bottom_dialog.view.*


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


        for (i in 0..5) {
            mMap?.addMarker(
                MarkerOptions().icon(
                    IconUtils.bitmapDescriptorFromVector(this, R.drawable.ic_jobs_map)
                ).position(LatLng(latitude!! + i, longitude!! + i))
            )
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
        openJobsDetailDialog()
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

        val skillChipList: List<Chip> = ListUtils.getSkillsBean()
        chip_skills.chipList = skillChipList

        val equipmentsChipList: List<Chip> = ListUtils.getEquipmentsBean()
        chip_equipments.chipList = equipmentsChipList

        dialog.show()

    }


}
