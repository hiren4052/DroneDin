package com.grewon.dronedin.addprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.grewon.dronedin.R
import com.grewon.dronedin.app.BaseActivity

class AddProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_profile)
        supportActionBar?.hide()
    }
}