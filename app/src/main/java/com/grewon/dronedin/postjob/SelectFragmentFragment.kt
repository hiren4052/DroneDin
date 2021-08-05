package com.grewon.dronedin.postjob

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseFragment
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.filter.adapter.FilterEquipmentsAdapter
import com.grewon.dronedin.filter.adapter.FilterSkillsAdapter
import com.grewon.dronedin.postjob.contract.SkillsEquipmentsContract
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.JobInitBean
import com.grewon.dronedin.utils.ListUtils
import com.grewon.dronedin.utils.ValidationUtils
import kotlinx.android.synthetic.main.fragment_select_fragment.*
import retrofit2.Retrofit
import javax.inject.Inject


class SelectFragmentFragment : BaseFragment(), FilterSkillsAdapter.OnFilterSkillsItemSelected,
    FilterEquipmentsAdapter.OnFilterEquipmentsItemSelected, View.OnClickListener,
    SkillsEquipmentsContract.View {

    @Inject
    lateinit var skillsEquipmentsPresenter: SkillsEquipmentsContract.Presenter

    @Inject
    lateinit var retrofit: Retrofit

    private var filterEquipmentsAdapter: FilterEquipmentsAdapter? = null
    private var filterSkillsAdapter: FilterSkillsAdapter? = null
    private var selectedCategoryId: String = ""
    private var categoryList: ArrayList<JobInitBean.Category>? = null
    private var skillsList: ArrayList<JobInitBean.Skill>? = null
    private var equipmentsList: ArrayList<JobInitBean.Equipment>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        setClicks()
    }

    private fun setClicks() {
        txt_next.setOnClickListener(this)
        txt_category.setOnClickListener(this)
    }

    private fun initView() {

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        skillsEquipmentsPresenter.attachView(this)
        skillsEquipmentsPresenter.attachApiInterface(retrofit)


        if ((activity as PostJobActivity).createJobsParams?.categoryList != null && (activity as PostJobActivity).createJobsParams?.skillList != null && (activity as PostJobActivity).createJobsParams?.equipmentsList != null) {
            categoryList = (activity as PostJobActivity).createJobsParams?.categoryList
            skillsList = (activity as PostJobActivity).createJobsParams?.skillList
            equipmentsList = (activity as PostJobActivity).createJobsParams?.equipmentsList
            selectedCategoryId =
                (activity as PostJobActivity).createJobsParams?.selectedCategoryId.toString()

            setCategoryAdapter()
            setSkillsAdapter()
            setEquipmentsAdapter()

        } else {
            selectedCategoryId =
                (activity as PostJobActivity).createJobsParams?.selectedCategoryId.toString()
            skillsEquipmentsPresenter.getJobCommonData()

        }


    }


    private fun setSkillsAdapter() {
        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        skills_recycle.layoutManager = layoutManager
        filterSkillsAdapter = FilterSkillsAdapter(requireContext(), this)
        skills_recycle.adapter = filterSkillsAdapter
        filterSkillsAdapter?.addItemsList(skillsList!!)
        if ((activity as PostJobActivity).createJobsParams?.selectedSkillsIdList != null && (activity as PostJobActivity).createJobsParams?.selectedSkillsIdList!!.isNotEmpty()) {
            filterSkillsAdapter?.addSelectedItems((activity as PostJobActivity).createJobsParams?.selectedSkillsIdList!!)
        }
    }

    private fun setEquipmentsAdapter() {
        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        equipments_recycle.layoutManager = layoutManager
        filterEquipmentsAdapter = FilterEquipmentsAdapter(requireContext(), this)
        equipments_recycle.adapter = filterEquipmentsAdapter
        filterEquipmentsAdapter?.addItemsList(equipmentsList!!)
        if ((activity as PostJobActivity).createJobsParams?.selectedEquipmentsIdList != null && (activity as PostJobActivity).createJobsParams?.selectedEquipmentsIdList!!.isNotEmpty()) {
            filterEquipmentsAdapter?.addSelectedItems((activity as PostJobActivity).createJobsParams?.selectedEquipmentsIdList!!)
        }
    }

    private fun setCategoryAdapter() {

        val categoryAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
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

        if (!ValidationUtils.isEmptyFiled(selectedCategoryId)) {
            category_spinner.setSelection(setCategorySpinnerSelected())
        }

    }


    private fun setCategorySpinnerSelected(): Int {
        for ((position, item) in categoryList?.withIndex()!!) {
            if (item.categoryId == selectedCategoryId) {
                return position
            }
        }
        return 0
    }


    override fun onFilterSkillsItemSelected() {

    }

    override fun onFilterEquipmentsItemSelected() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_next -> {
                if (filterSkillsAdapter != null && filterSkillsAdapter?.getSelectedItems() != null && filterSkillsAdapter?.getSelectedItems()!!
                        .isEmpty()
                ) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_select_at_least_one_skill))
                } else if (filterEquipmentsAdapter != null && filterEquipmentsAdapter?.getSelectedItems() != null && filterEquipmentsAdapter?.getSelectedItems()!!
                        .isEmpty()
                ) {
                    DroneDinApp.getAppInstance()
                        .showToast(getString(R.string.please_select_at_least_one_equipment))
                } else {
                    (activity as PostJobActivity).createJobsParams?.skillList =
                        filterSkillsAdapter?.itemList
                    (activity as PostJobActivity).createJobsParams?.selectedSkillsIdList =
                        filterSkillsAdapter?.getSelectedIdsItems() as ArrayList<String>
                    (activity as PostJobActivity).createJobsParams?.selectedEquipmentsIdList =
                        filterEquipmentsAdapter?.getSelectedIdsItems() as ArrayList<String>
                    (activity as PostJobActivity).createJobsParams?.equipmentsList =
                        filterEquipmentsAdapter?.itemList
                    (activity as PostJobActivity).createJobsParams?.selectedCategoryId =
                        selectedCategoryId
                    (activity as PostJobActivity).showFragment(AddJobDetailsFragment())
                }

            }
            R.id.txt_category -> {
                category_spinner.performClick()
            }
        }
    }

    override fun onApiException(error: Int) {
        DroneDinApp.getAppInstance().showToast(getString(error))
    }

    override fun onJobCommonDataGetSuccessful(response: JobInitBean) {

        if (context != null && isVisible) {
            if (response.category != null && response.category.size > 0) {
                categoryList = response.category
                (activity as PostJobActivity).createJobsParams?.categoryList = categoryList
                setCategoryAdapter()
            }

            if (response.skill != null && response.skill.size > 0) {
                skillsList = response.skill
                (activity as PostJobActivity).createJobsParams?.skillList = skillsList
                setSkillsAdapter()
            }

            if (response.equipment != null && response.equipment.size > 0) {
                equipmentsList = response.equipment
                (activity as PostJobActivity).createJobsParams?.equipmentsList = equipmentsList
                setEquipmentsAdapter()
            }
        }

    }

    override fun onJobCommonDataGetFailed(loginParams: CommonMessageBean) {
        if (loginParams.msg != null) {
            DroneDinApp.getAppInstance().showToast(loginParams.msg)
        }
    }

}