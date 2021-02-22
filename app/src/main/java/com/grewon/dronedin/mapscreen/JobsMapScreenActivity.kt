package com.grewon.dronedin.mapscreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.helper.LogX
import com.grewon.dronedin.server.LocationBean
import com.grewon.dronedin.utils.MapUtils
import kotlinx.android.synthetic.main.activity_map_screen.*


class JobsMapScreenActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener,
    View.OnClickListener {


    private lateinit var mMap: GoogleMap
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
        im_back.setOnClickListener(this)
        btn_pickup_address.setOnClickListener(this)

    }

    override fun onMapReady(mMap: GoogleMap?) {
        this.mMap = mMap!!

        mMap.setOnCameraIdleListener(this)


    }


    override fun onCameraIdle() {


    }


    override fun onClick(p0: View?) {

        when (p0?.id) {
            R.id.im_back -> {
                finish()
            }

            /*R.id.fab_current_location -> {
                setCurrentLocation()
            }*/
            R.id.btn_pickup_address -> {

            }
        }
    }



}
