package com.grewon.dronedin.addprofile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.Gson
import com.grewon.dronedin.R
import com.grewon.dronedin.addprofile.adapter.ProfileEqipmentsAdapter
import com.grewon.dronedin.addprofile.adapter.ProfileSkillsAdapter
import com.grewon.dronedin.addprofile.contract.AddBioContract
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.error.ErrorHandler
import com.grewon.dronedin.main.MainActivity
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.JobInitBean
import com.grewon.dronedin.server.ProfileBioDataBean
import com.grewon.dronedin.server.params.BioUpdateParams
import com.grewon.dronedin.utils.ListUtils
import com.grewon.dronedin.utils.ValidationUtils
import kotlinx.android.synthetic.main.activity_add_more_profile.*
import kotlinx.android.synthetic.main.activity_add_more_profile.top_image
import kotlinx.android.synthetic.main.activity_add_more_profile.txt_save
import retrofit2.Retrofit
import javax.inject.Inject


class AddMoreProfileActivity : BaseActivity(), View.OnClickListener,
    ProfileSkillsAdapter.OnFilterSkillsItemSelected,
    ProfileEqipmentsAdapter.OnFilterSkillsItemSelected, AddBioContract.View {


    @Inject
    lateinit var addBioPresenter: AddBioContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit

    private var profileEquipmentsAdapter: ProfileEqipmentsAdapter? = null
    private var profileSkillsAdapter: ProfileSkillsAdapter? = null

    private var categoryList: ArrayList<JobInitBean.Category>? = null
    private var skillsList: ArrayList<JobInitBean.Skill>? = null
    private var equipmentsList: ArrayList<JobInitBean.Equipment>? = null
    private var selectedCategoryId: String = ""
    private var selectedSkillsId: List<Int>? = null
    private var selectedEquipmentsId: List<Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_more_profile)
        initView()
        setClicks()
    }

    private fun setClicks() {
        txt_save.setOnClickListener(this)
        im_back.setOnClickListener(this)
        txt_category.setOnClickListener(this)

    }


    private fun initView() {
        DroneDinApp.getAppInstance().getAppComponent().inject(this)

        addBioPresenter.attachView(this)
        addBioPresenter.attachApiInterface(retrofit)

        addBioPresenter.getBio()

        DroneDinApp.getAppInstance().loadGifImage(R.drawable.add_profile, top_image)


    }


    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.txt_save -> {
                if (ValidationUtils.isEmptyFiled(edt_profile_price.text.toString())) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_enter_profile_price))
                } else if (ValidationUtils.isEmptyFiled(edt_bio.text.toString())) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_enter_bio))
                } else if (ValidationUtils.isEmptyFiled(selectedCategoryId)) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_select_category))
                } else if (profileSkillsAdapter != null && profileSkillsAdapter?.getSelectedItems()
                        ?.isEmpty()!!
                ) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_select_at_least_one_skill))
                } else if (profileEquipmentsAdapter != null && profileEquipmentsAdapter?.getSelectedItems()
                        ?.isEmpty()!!
                ) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_select_at_least_one_equipment))
                } else {
                    apiCall()
                }
            }
            R.id.txt_category -> {
                category_spinner.performClick()
            }
            R.id.im_back -> {
                finish()
            }
        }
    }

    private fun apiCall() {
        val bioUpdateParams = BioUpdateParams(
            categoryId = selectedCategoryId,
            equipment = profileEquipmentsAdapter?.getSelectedItems()
                ?.map { it.equipmentId!!.toInt() }!!,
            skill = profileSkillsAdapter?.getSelectedItems()?.map { it.skillId!!.toInt() }!!,
            profilePrice = edt_profile_price.text.toString(),
            userBio = edt_bio.text.toString()
        )

        addBioPresenter.updateBio(bioUpdateParams)
    }

    override fun onFilterSkillsItemSelected() {

    }

    override fun onBioGetSuccessful(response: ProfileBioDataBean) {
        if (response.data != null) {
            setView(response.data)
        }

    }

    private fun setView(data: ProfileBioDataBean.Data) {
        edt_profile_price.setText(data.profilePrice)
        edt_bio.setText(data.userBio)

        if (data.category != null) {
            selectedCategoryId = data.category.toString()
        }

        if (data.skill != null && data.skill.size > 0) {
            selectedSkillsId = data.skill.map { it.skillId?.toInt()!! }
        }

        if (data.equipment != null && data.equipment.size > 0) {
            selectedEquipmentsId = data.equipment.map { it.equipmentId?.toInt()!! }
        }

        addBioPresenter.getJobCommonData()
    }

    override fun onBioGetFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

    override fun onBioUpdateSuccessFully(loginParams: ProfileBioDataBean) {
        if (loginParams.data != null && loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onBioUpdateFailed(loginParams: BioUpdateParams) {
        ErrorHandler.handleMapError(Gson().toJson(loginParams))
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

    private fun setSkillsAdapter() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        skills_recycle.layoutManager = layoutManager
        profileSkillsAdapter = ProfileSkillsAdapter(this, this)
        skills_recycle.adapter = profileSkillsAdapter
        profileSkillsAdapter?.addItemsList(skillsList!!)
        if (selectedSkillsId != null && selectedSkillsId!!.isNotEmpty()) {
            profileSkillsAdapter?.addSelectedItems(selectedSkillsId!!)
        }
    }

    private fun setEquipmentsAdapter() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        equipments_recycle.layoutManager = layoutManager
        profileEquipmentsAdapter = ProfileEqipmentsAdapter(this, this)
        equipments_recycle.adapter = profileEquipmentsAdapter
        profileEquipmentsAdapter?.addItemsList(equipmentsList!!)
        if (selectedEquipmentsId != null && selectedEquipmentsId!!.isNotEmpty()) {
            profileEquipmentsAdapter?.addSelectedItems(selectedEquipmentsId!!)
        }
    }

    private fun setCategoryAdapter() {

        val categoryAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.simple_spinner_dropdown_item,
            ListUtils.getCategoryStrings(categoryList) as ArrayList<String>
        )
        categoryAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        category_spinner.adapter = categoryAdapter

        category_spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCategoryId = categoryList?.get(position)?.categoryId.toString()
                    txt_category.text = categoryList?.get(position)?.categoryName
                }

            }
    }


}