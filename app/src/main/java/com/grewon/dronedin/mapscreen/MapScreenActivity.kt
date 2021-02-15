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


class MapScreenActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener,
    View.OnClickListener {

    private var latitude: Double? = null // Latitude
    private var longitude: Double? = null // Longitude
    private var locationAddress: String? = null
    private lateinit var mMap: GoogleMap
    private var gpsTracker: GPSTracker? = null
    private var isSelected = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_screen)

        setClickListeners()


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        val locationBean = intent.getParcelableExtra(AppConstant.LOCATION_BEAN) as LocationBean?

        latitude = locationBean?.latitude
        longitude = locationBean?.longitude
        locationAddress = locationBean?.address


        mapFragment.getMapAsync(this)



        et_search_location.setAdapter(
            PlaceAPIAdapter(
                this,
                android.R.layout.simple_list_item_1,
                this
            )
        )


        et_search_location.setOnItemClickListener { parent, view, position, id ->


            locationAddress = parent.getItemAtPosition(position) as String

            if (locationAddress != null) {
                val latlng = MapUtils.getLocationFormAddress(this, locationAddress!!)
                isSelected = true
                if (latlng != null) {

                    latitude = latlng.latitude
                    longitude = latlng.longitude
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                latitude!!,
                                longitude!!
                            ), 15f
                        )
                    )

                }
            }

        }


    }

    private fun setClickListeners() {
        im_back.setOnClickListener(this)
        btn_pickup_address.setOnClickListener(this)

    }

    override fun onMapReady(mMap: GoogleMap?) {
        this.mMap = mMap!!

        mMap.setOnCameraIdleListener(this)

        if (latitude != 0.0 && longitude != 0.0) {
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(latitude!!, longitude!!),
                    15f
                )
            )
        } else {
            setCurrentLocation()
        }
    }


    override fun onCameraIdle() {

        if (!isSelected) {
            val latLng = mMap.cameraPosition.target
            latitude = latLng.latitude
            longitude = latLng.longitude
            locationAddress =
                MapUtils.getCompleteAddressString(this, latLng.latitude, latLng.longitude)
            et_search_location.setText(locationAddress.toString().trim(), false)
        }
        isSelected = false
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
                LogX.E(MapUtils.getCountryName(this, latitude!!, longitude!!))
                passBackIntent()
//                if (MapUtils.getCountryName(this, latitude!!, longitude!!) == "Australia") {
//                    passBackIntent()
//                } else {
//                    Toast.makeText(
//                        this,
//                        getString(R.string.please_select_autralia_address),
//                        Toast.LENGTH_SHORT
//                    ).show();
//                }
            }
        }
    }

    private fun passBackIntent() {
        val intent = Intent()
        intent.putExtra(AppConstant.LATITUDE, latitude)
        intent.putExtra(AppConstant.LONGITUDE, longitude)
        intent.putExtra(AppConstant.ADDRESS, locationAddress?.trim())
        setResult(Activity.RESULT_OK, intent)
        finish()

    }

    private fun setCurrentLocation() {
        gpsTracker = GPSTracker(this, this)
        latitude = gpsTracker!!.latitude
        longitude = gpsTracker!!.longitude
        if (latitude != 0.0 && longitude != 0.0)
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(latitude!!, longitude!!),
                    15f
                )
            )
    }
}
