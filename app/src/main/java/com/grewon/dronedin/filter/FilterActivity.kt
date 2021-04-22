package com.grewon.dronedin.filter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.grewon.dronedin.R
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.filter.adapter.FilterCategoryAdapter
import com.grewon.dronedin.filter.adapter.FilterEquipmentsAdapter
import com.grewon.dronedin.filter.adapter.FilterSkillsAdapter
import com.grewon.dronedin.mapscreen.MapScreenActivity
import com.grewon.dronedin.postjob.contract.SkillsEquipmentsContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.JobInitBean
import com.grewon.dronedin.server.LocationBean
import com.grewon.dronedin.server.params.FilterParams
import com.grewon.dronedin.utils.IconUtils
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import kotlinx.android.synthetic.main.activity_filter.*
import retrofit2.Retrofit
import javax.inject.Inject


class FilterActivity : BaseActivity(), FilterSkillsAdapter.OnFilterSkillsItemSelected,
    FilterEquipmentsAdapter.OnFilterEquipmentsItemSelected,
    FilterCategoryAdapter.OnCategoryItemSelected, View.OnClickListener,
    SkillsEquipmentsContract.View {


    @Inject
    lateinit var skillsEquipmentsPresenter: SkillsEquipmentsContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit


    private var filterCategoryAdapter: FilterCategoryAdapter? = null
    private var filterEquipmentsAdapter: FilterEquipmentsAdapter? = null
    private var filterSkillsAdapter: FilterSkillsAdapter? = null

    private var selectedCategoryId: List<Int>? = null
    private var selectedSkillsId: List<Int>? = null
    private var selectedEquipmentsId: List<Int>? = null
    private var categoryList: ArrayList<JobInitBean.Category>? = null
    private var skillsList: ArrayList<JobInitBean.Skill>? = null
    private var equipmentsList: ArrayList<JobInitBean.Equipment>? = null

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var locationAddress: String = ""

    private var filterParams: FilterParams? = null

    private var minValue: Int = 1
    private var maxValue: Int = 10000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        setClicks()
        initView()

    }

    private fun initView() {

        if (isPilotAccount()) {
            txt_title.text = getString(R.string.select_job_filters)
        } else {
            txt_title.text = getString(R.string.filters)
        }

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        skillsEquipmentsPresenter.attachView(this)
        skillsEquipmentsPresenter.attachApiInterface(retrofit)

        skillsEquipmentsPresenter.getJobCommonData()


        sb_range_selector.setProgress(1f, 100f)
        sb_range_selector.leftSeekBar.setIndicatorText("$1")
        sb_range_selector.rightSeekBar.setIndicatorText("$10000")

        sb_range_selector.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onRangeChanged(
                view: RangeSeekBar,
                leftValue: Float,
                rightValue: Float,
                isFromUser: Boolean
            ) {

                minValue = leftValue.toInt()
                maxValue = rightValue.toInt()

                view.leftSeekBar.setIndicatorText("$" + leftValue.toInt())
                view.rightSeekBar.setIndicatorText("$" + rightValue.toInt())


                txt_price_range.text = getString(
                    R.string.price_range_value,
                    leftValue.toInt().toString(),
                    rightValue.toInt().toString()
                )

            }

            override fun onStartTrackingTouch(view: RangeSeekBar, isLeft: Boolean) {}
            override fun onStopTrackingTouch(view: RangeSeekBar, isLeft: Boolean) {}
        })


    }

    private fun setClicks() {
        layout_category.setOnClickListener(this)
        layout_skill.setOnClickListener(this)
        layout_equipments.setOnClickListener(this)
        img_back.setOnClickListener(this)
        txt_apply.setOnClickListener(this)
        layout_location.setOnClickListener(this)
    }

    private fun setSkillsAdapter() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        skills_recycle.layoutManager = layoutManager
        filterSkillsAdapter = FilterSkillsAdapter(this, this)
        skills_recycle.adapter = filterSkillsAdapter
        filterSkillsAdapter?.addItemsList(skillsList!!)
        if (selectedSkillsId != null && selectedSkillsId!!.isNotEmpty()) {
            filterSkillsAdapter?.addSelectedIntItems(selectedSkillsId!!)
        }
    }

    private fun setEquipmentsAdapter() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        equipments_recycle.layoutManager = layoutManager
        filterEquipmentsAdapter = FilterEquipmentsAdapter(this, this)
        equipments_recycle.adapter = filterEquipmentsAdapter
        filterEquipmentsAdapter?.addItemsList(equipmentsList!!)
        if (selectedEquipmentsId != null && selectedEquipmentsId!!.isNotEmpty()) {
            filterEquipmentsAdapter?.addSelectedIntItems(selectedEquipmentsId!!)
        }
    }


    private fun setCategoryAdapter() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        category_recycle.layoutManager = layoutManager
        filterCategoryAdapter = FilterCategoryAdapter(this, this)
        category_recycle.adapter = filterCategoryAdapter
        filterCategoryAdapter?.addItemsList(categoryList!!)
        if (selectedCategoryId != null && selectedCategoryId!!.isNotEmpty()) {
            filterCategoryAdapter?.addSelectedItems(selectedCategoryId!!)
        }
    }

    override fun onFilterSkillsItemSelected() {

    }

    override fun onFilterEquipmentsItemSelected() {

    }

    override fun onCategoryItemSelected() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_apply -> {
                filterParams = FilterParams()
                filterParams?.location = locationAddress
                filterParams?.latitude = latitude
                filterParams?.longitude = longitude
                filterParams?.category = filterCategoryAdapter?.getSelectedItemsStrings()
                filterParams?.skill = filterSkillsAdapter?.getSelectedItemsStrings()
                filterParams?.equipment = filterEquipmentsAdapter?.getSelectedItemsStrings()
                filterParams?.price = "$minValue-$maxValue"
                startActivity(
                    Intent(
                        this,
                        FilterResultActivity::class.java
                    ).putExtra(AppConstant.BEAN, filterParams)
                )
            }
            R.id.layout_category -> {
                if (layout_category_recycle.visibility == View.GONE) {
                    IconUtils.setRightDrawableIcontoText(
                        this,
                        R.drawable.ic_up_chevron,
                        text_select_category
                    )
                    layout_category_recycle.visibility = View.VISIBLE
                } else {
                    IconUtils.setRightDrawableIcontoText(
                        this,
                        R.drawable.ic_drop_down,
                        text_select_category
                    )
                    layout_category_recycle.visibility = View.GONE
                }

            }
            R.id.layout_skill -> {
                if (layout_skills_recycle.visibility == View.GONE) {
                    IconUtils.setRightDrawableIcontoText(
                        this,
                        R.drawable.ic_up_chevron,
                        text_select_skills
                    )
                    layout_skills_recycle.visibility = View.VISIBLE
                } else {
                    IconUtils.setRightDrawableIcontoText(
                        this,
                        R.drawable.ic_drop_down,
                        text_select_skills
                    )
                    layout_skills_recycle.visibility = View.GONE
                }
            }
            R.id.layout_equipments -> {
                if (layout_equipments_recycle.visibility == View.GONE) {
                    IconUtils.setRightDrawableIcontoText(
                        this,
                        R.drawable.ic_up_chevron,
                        text_select_equipments
                    )
                    layout_equipments_recycle.visibility = View.VISIBLE
                } else {
                    IconUtils.setRightDrawableIcontoText(
                        this,
                        R.drawable.ic_drop_down,
                        text_select_equipments
                    )
                    layout_equipments_recycle.visibility = View.GONE
                }
            }
            R.id.layout_location -> {
                passIntent()
            }
        }
    }

    private fun passIntent() {
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
                val locationsBean = LocationBean(latitude, longitude, locationAddress)
                startActivityForResult(
                    Intent(
                        this,
                        MapScreenActivity::class.java
                    ).putExtra(AppConstant.LOCATION_BEAN, locationsBean),
                    AppConstant.ADD_LOCATION_REQUEST_CODE
                )
            }
        } else {
            val locationsBean = LocationBean(latitude, longitude, locationAddress)
            startActivityForResult(
                Intent(
                    this,
                    MapScreenActivity::class.java
                ).putExtra(AppConstant.LOCATION_BEAN, locationsBean),
                AppConstant.ADD_LOCATION_REQUEST_CODE
            )
        }

    }


    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }

    override fun onJobCommonDataGetSuccessful(response: JobInitBean) {
        if (response.category != null && response.category.size > 0) {
            categoryList = response.category
            setCategoryAdapter()
        }

        if (response.skill != null && response.skill.size > 0) {
            skillsList = response.skill
            setSkillsAdapter()
        }

        if (response.equipment != null && response.equipment.size > 0) {
            equipmentsList = response.equipment
            setEquipmentsAdapter()
        }
    }

    override fun onJobCommonDataGetFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            AppConstant.ADD_LOCATION_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    latitude = data.getDoubleExtra(AppConstant.LATITUDE, 37.8136)
                    longitude = data.getDoubleExtra(AppConstant.LONGITUDE, 144.9631)
                    locationAddress = data.getStringExtra(AppConstant.ADDRESS)!!
                    txt_location.visibility = View.VISIBLE
                    txt_location.text = locationAddress.trim()
                }

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == AppConstant.LOCATION_PERMISSION_REQUEST_CODE) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED

            ) {
                passIntent()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.please_allow_location_permission),
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        }


    }


}