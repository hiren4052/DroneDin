package com.grewon.dronedin.postjob

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.grewon.dronedin.R
import com.grewon.dronedin.filter.adapter.FilterEquipmentsAdapter
import com.grewon.dronedin.filter.adapter.FilterSkillsAdapter
import com.grewon.dronedin.utils.ListUtils
import kotlinx.android.synthetic.main.fragment_select_fragment.*


class SelectFragmentFragment : Fragment(), FilterSkillsAdapter.OnFilterSkillsItemSelected,
    FilterEquipmentsAdapter.OnFilterEquipmentsItemSelected, View.OnClickListener {

    private var filterEquipmentsAdapter: FilterEquipmentsAdapter? = null
    private var filterSkillsAdapter: FilterSkillsAdapter? = null


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

        setCategoryAdapter()
        setSkillsAdapter()
        setEquipmentsAdapter()
    }


    private fun setCategoryAdapter() {

        val categoryAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_dropdown_item,
            ListUtils.getCategoryStrings(ListUtils.getCategoryBean())
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

                    txt_category.text = ListUtils.getCategoryBean()[position].userProfileName
                }

            }
    }


    private fun setSkillsAdapter() {
        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        skills_recycle.layoutManager = layoutManager
        filterSkillsAdapter = FilterSkillsAdapter(requireContext(), this)
        skills_recycle.adapter = filterSkillsAdapter
        filterSkillsAdapter?.addItemsList(ListUtils.getSkillsBean())
    }

    private fun setEquipmentsAdapter() {
        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        equipments_recycle.layoutManager = layoutManager
        filterEquipmentsAdapter = FilterEquipmentsAdapter(requireContext(), this)
        equipments_recycle.adapter = filterEquipmentsAdapter
        filterEquipmentsAdapter?.addItemsList(ListUtils.getEquipmentsBean())
    }

    override fun onFilterSkillsItemSelected() {

    }

    override fun onFilterEquipmentsItemSelected() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_next -> {
                (activity as PostJobActivity).showFragment(AddJobDetailsFragment())
            }
            R.id.txt_category -> {
                category_spinner.performClick()
            }
        }
    }

}