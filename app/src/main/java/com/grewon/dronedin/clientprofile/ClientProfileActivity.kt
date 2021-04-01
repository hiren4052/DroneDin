package com.grewon.dronedin.clientprofile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.grewon.dronedin.R
import com.grewon.dronedin.addprofile.AddProfileActivity
import com.grewon.dronedin.app.AppConstant
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.DroneDinApp
import com.grewon.dronedin.clientprofile.contract.ClientProfileContract
import com.grewon.dronedin.message.ChatActivity
import com.grewon.dronedin.review.adapter.WhiteScreenReviewsAdapter
import com.grewon.dronedin.server.ClientProfileBean
import com.grewon.dronedin.server.CommonMessageBean
import com.grewon.dronedin.utils.ValidationUtils
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_client_profile.*
import kotlinx.android.synthetic.main.activity_client_profile.blur_img_user
import kotlinx.android.synthetic.main.activity_client_profile.im_back
import kotlinx.android.synthetic.main.activity_client_profile.im_message
import kotlinx.android.synthetic.main.activity_client_profile.img_user
import kotlinx.android.synthetic.main.activity_client_profile.layout_no_data
import kotlinx.android.synthetic.main.activity_client_profile.layout_progress
import kotlinx.android.synthetic.main.activity_client_profile.main_layout
import kotlinx.android.synthetic.main.activity_client_profile.no_review_layout
import kotlinx.android.synthetic.main.activity_client_profile.review_recycle
import kotlinx.android.synthetic.main.activity_client_profile.txt_active_jobs
import kotlinx.android.synthetic.main.activity_client_profile.txt_location
import kotlinx.android.synthetic.main.activity_client_profile.txt_no_review
import kotlinx.android.synthetic.main.activity_client_profile.txt_ratings
import kotlinx.android.synthetic.main.activity_client_profile.txt_user_name
import kotlinx.android.synthetic.main.activity_pilot_profile.*

import kotlinx.android.synthetic.main.layout_no_data.*
import retrofit2.Retrofit
import javax.inject.Inject

class ClientProfileActivity : BaseActivity(), View.OnClickListener, ClientProfileContract.View {


    private var reviewsAdapter: WhiteScreenReviewsAdapter? = null

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var clientProfilePresenter: ClientProfileContract.Presenter

    private var profileId: String = ""

    private var isEdit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_profile)
        setClicks()
        initView()
    }

    private fun setClicks() {
        im_back.setOnClickListener(this)
        im_message.setOnClickListener(this)
    }

    private fun initView() {

        profileId = intent.getStringExtra(AppConstant.ID).toString()
        isEdit = intent.getBooleanExtra(AppConstant.TAG, false)

        DroneDinApp.getAppInstance().getAppComponent().inject(this)
        clientProfilePresenter.attachView(this)
        clientProfilePresenter.attachApiInterface(retrofit)

        clientProfilePresenter.getClientProfile(profileId)

        if (isEdit) {
            im_message.setImageResource(R.drawable.ic_edit)
        }

    }

    private fun setReviewAdapter(review: List<String?>) {
        no_review_layout.visibility = View.GONE
        review_recycle.visibility = View.VISIBLE
        review_recycle.layoutManager = LinearLayoutManager(this)
        reviewsAdapter = WhiteScreenReviewsAdapter(this)
        review_recycle.adapter = reviewsAdapter
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
                    startActivity(Intent(this, ChatActivity::class.java).putExtra(AppConstant.ID,profileId))
                }
            }
        }
    }

    override fun onClientProfileDetailSuccessfully(response: ClientProfileBean) {
        setView(response)
    }

    private fun setView(response: ClientProfileBean) {
        main_layout.visibility = View.VISIBLE

        Glide.with(this)
            .load(response.profileImage!!)
            .apply(RequestOptions().placeholder(R.drawable.ic_user_place_holder))
            .into(img_user)

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
        txt_active_jobs.text = response.totalActiveJob
        txt_total_jobs.text = response.totalJob
        if (response.totalSpending != null) {
            txt_total_spending.text = getString(R.string.price_string, response.totalSpending)
        } else {
            txt_total_spending.text = getString(R.string.price_string, "0")
        }

        if (response.review != null && response.review.isNotEmpty()) {
            setReviewAdapter(response.review)
        } else {
            no_review_layout.visibility = View.VISIBLE
            review_recycle.visibility = View.GONE
            if (isEdit) {
                txt_no_review.text = getString(R.string.no_review_client_message)
            } else {
                txt_no_review.text = getString(R.string.no_review_message)
            }
        }
    }

    override fun onClientProfileDetailFailed(loginParams: CommonMessageBean) {
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

}