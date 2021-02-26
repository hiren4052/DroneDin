package com.grewon.dronedin.postjob

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity
import kotlinx.android.synthetic.main.layout_square_toolbar_with_back.*

class PostJobActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_job)
        initView()
        loadFragment(SelectFragmentFragment())
    }

    private fun initView() {
        txt_toolbar_title.text=getString(R.string.post_new_job)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_contain, fragment)
            .commit()
    }

    fun showFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_contain, fragment)
            .addToBackStack(null)
            .commit()
    }
}