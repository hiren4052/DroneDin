package com.grewon.dronedin.pilotprofile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.grewon.dronedin.R
import com.grewon.dronedin.addprofile.AddMoreProfileActivity
import com.grewon.dronedin.addprofile.AddProfileActivity
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.extraadapter.ChipEquipmentsAdapter
import com.grewon.dronedin.extraadapter.ChipSkillsAdapter
import com.grewon.dronedin.message.ChatActivity
import com.grewon.dronedin.pilotprofile.contract.PilotProfileContract
import com.grewon.dronedin.portfolio.PortFolioActivity
import com.grewon.dronedin.portfolio.adapter.PortFolioAdapter
import com.grewon.dronedin.review.adapter.WhiteScreenReviewsAdapter
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.server.PilotProfileBean
import com.grewon.dronedin.server.ReviewsDataBean
import com.grewon.dronedin.utils.IconUtils
import com.grewon.dronedin.utils.ValidationUtils
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_pilot_profile.*

import kotlinx.android.synthetic.main.layout_no_data.*
import retrofit2.Retrofit
import javax.inject.Inject


class PilotProfileActivity : BaseActivity(), View.OnClickListener, PilotProfileContract.View,
    PortFolioAdapter.OnItemClickListeners {

    private var reviewsAdapter: WhiteScreenReviewsAdapter? = null

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var pilotProfilePresenter: PilotProfileContract.Presenter

    private var profileId: String = ""

    private var isEdit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilot_profile)
        setClicks()
        initView()
    }

    private fun setClicks() {
        im_back.setOnClickListener(this)
        im_message.setOnClickListener(this)
        im_more_edit.setOnClickListener(this)
        txt_add_portfolio.setOnClickListener(this)
        im_add_port_folio.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.im_back -> {
                finish()
            }
            R.id.im_message -> {
                if (isEdit) {
                    startActivityForResult(
                        Intent(this, AddProfileActivity::class.java).putExtra(
                            AppConstant.TAG,
                            isEdit
                        ), 12
                    )
                } else {
                    startActivity(
                        Intent(this, ChatActivity::class.java).putExtra(
                            AppConstant.ID,
                            profileId
                        )
                    )
                }
            }
            R.id.im_more_edit -> {
                if (isEdit) {
                    startActivityForResult(
                        Intent(
                            this,
                            AddMoreProfileActivity::class.java
                        ).putExtra(AppConstant.TAG, isEdit), 12
                    )
                }
            }
            R.id.im_add_port_folio, R.id.txt_add_portfolio -> {
                if (isEdit) {
                    startActivityForResult(Intent(this, PortFolioActivity::class.java), 12)
                }
            }


        }
    }


    private fun initView() {

        profileId = intent.getStringExtra(AppConstant.ID).toString()
        isEdit = intent.getBooleanExtra(AppConstant.TAG, false)

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        pilotProfilePresenter.attachView(this)
        pilotProfilePresenter.attachApiInterface(retrofit)



        pilotProfilePresenter.getPilotProfile(profileId)


        Glide.with(this).load(R.drawable.ic_user_place_holder)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(22, 3)))
            .into(blur_img_user)

        segment_group.setOnPositionChangedListener {
            setSegmentViewVisibility(it)
        }

        if (isEdit) {
            im_more_edit.visibility = View.VISIBLE
            add_portfolio_layout.visibility = View.VISIBLE
            im_message.setImageResource(R.drawable.ic_edit)
        } else {
            add_portfolio_layout.visibility = View.GONE
            im_more_edit.visibility = View.GONE
        }


    }

    private fun setSegmentViewVisibility(position: Int) {
        first_segment_layout.visibility = if (position == 0) View.VISIBLE else View.GONE
        second_segment_layout.visibility = if (position == 1) View.VISIBLE else View.GONE
        third_segment_layout.visibility = if (position == 2) View.VISIBLE else View.GONE
    }

    private fun setSkillsAdapter(skillsList: ArrayList<String>) {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        chip_skills.layoutManager = layoutManager
        val skillsAdapter = ChipSkillsAdapter(this, R.color.gray_f4, R.color.gray_71)
        chip_skills.adapter = skillsAdapter
        skillsAdapter.addItemsList(skillsList)

    }

    private fun setEquipmentsData(skillsList: ArrayList<String>) {

        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        chip_equipments.layoutManager = layoutManager
        val equipmentsAdapter = ChipEquipmentsAdapter(this, R.color.light_sky_blue, R.color.gray_71)
        chip_equipments.adapter = equipmentsAdapter
        equipmentsAdapter.addItemsList(skillsList)

    }

    private fun setReviewAdapter(review: ArrayList<ReviewsDataBean>?) {
        no_review_layout.visibility = View.GONE
        review_recycle.visibility = View.VISIBLE
        review_recycle.layoutManager = LinearLayoutManager(this)
        reviewsAdapter = WhiteScreenReviewsAdapter(this)
        review_recycle.adapter = reviewsAdapter
        reviewsAdapter?.addItemsList(review!!)
    }


    override fun onProfileDetailSuccessfully(response: PilotProfileBean) {
        setView(response)
    }

    private fun setView(response: PilotProfileBean) {

        main_layout.visibility = View.VISIBLE

        Glide.with(this)
            .load(response.profileImage!!)
            .apply(RequestOptions().placeholder(R.drawable.ic_user_place_holder))
            .into(img_user)

        IconUtils.setBadgeImage(this,badge_type,response.badge.toString())



        if (!ValidationUtils.isEmptyFiled(
                response.profileImage
            )
        ) {
            Glide.with(this)
                .load(response.profileImage!!)
                .apply(
                    RequestOptions.bitmapTransform(BlurTransformation(22, 3))
                        .placeholder(R.drawable.drone_for_blur)
                ).into(blur_img_user)
        } else {
            Glide.with(this)
                .load(R.drawable.drone_for_blur)
                .apply(
                    RequestOptions.bitmapTransform(BlurTransformation(22, 3))
                )
                .into(blur_img_user)
        }

        txt_user_name.text = response.userName

        if (response.rate != null) {
            txt_ratings.text = response.rate
        } else {
            txt_ratings.text = "0"
        }

        txt_location.text = response.userAddress
        txt_about.text = response.userBio
        txt_active_jobs.text = response.totalActiveJob
        txt_completed_jobs.text = response.totalCompleteJob
        if (response.totalEarning != null) {
            txt_total_earnings.text = getString(R.string.price_string, response.totalEarning)
        } else {
            txt_total_earnings.text = getString(R.string.price_string, "0")
        }


        if (response.skill != null && response.skill.isNotEmpty()) {
            val skillsList = response.skill.map { it.skill.toString() }
            setSkillsAdapter(skillsList as ArrayList<String>)
        }

        if (response.equipment != null && response.equipment.isNotEmpty()) {
            val skillsList = response.equipment.map { it.equipment.toString() }
            setEquipmentsData(skillsList as ArrayList<String>)
        }


        if (response.portfolio != null && response.portfolio.isNotEmpty()) {
            setPortFolioAdapter(response.portfolio)
        } else {
            port_folio_layout.visibility = View.GONE
            no_portfolio_layout.visibility = View.VISIBLE
        }

        if (response.review != null && response.review.isNotEmpty()) {
            setReviewAdapter(response.review)
        } else {
            no_review_layout.visibility = View.VISIBLE
            review_recycle.visibility = View.GONE
            if (isEdit) {
                txt_no_review.text = getString(R.string.no_review_pilot_message)
            } else {
                txt_no_review.text = getString(R.string.no_review_message)
            }
        }

    }

    private fun setPortFolioAdapter(portfolio: ArrayList<PilotProfileBean.Portfolio>) {
        port_folio_layout.visibility = View.VISIBLE
        no_portfolio_layout.visibility = View.GONE
        portfolio_recycle.layoutManager = LinearLayoutManager(this)
        val portFolioAdapter = PortFolioAdapter(this, this, isEdit)
        portfolio_recycle.adapter = portFolioAdapter
        portFolioAdapter.addItemsList(portfolio)
    }

    override fun onProfileDetailFailed(loginParams: CommonMessageBean) {
        setEmptyView(R.drawable.ic_connectivity, loginParams.msg.toString())
    }

    override fun showOnScreenProgress() {
        layout_progress.visibility = View.VISIBLE
    }

    override fun hideOnScreenProgress() {
        layout_progress.visibility = View.GONE

    }

    override fun onApiException(error: Int) {
        setEmptyView(R.drawable.ic_connectivity, getString(error))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12 && resultCode == RESULT_OK) {
            recreate()
        }
    }

    private fun setEmptyView(drawableId: Int, errorMessage: String) {
        layout_no_data.visibility = View.VISIBLE
        main_layout.visibility = View.GONE
        txt_no_data_image.setImageResource(drawableId)
        txt_no_data_title.text = errorMessage
    }

    override fun onEditItemClick(jobsDataBean: PilotProfileBean.Portfolio) {
        startActivityForResult(
            Intent(
                this,
                PortFolioActivity::class.java
            ).putExtra(AppConstant.BEAN, jobsDataBean), 12
        )
    }

}