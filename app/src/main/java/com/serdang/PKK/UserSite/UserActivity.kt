package com.serdang.PKK.UserSite

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.serdang.PKK.R
import com.serdang.PKK.UserSite.ListEvent.ListEventUserActivity
import com.serdang.PKK.UserSite.LocationEvent.ChooseLocationDashboard
import kotlinx.android.synthetic.main.dashboard_user.*

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_user)
        supportActionBar?.title = "User PKK Dashboard"

        val linear_list = findViewById<LinearLayout>(R.id.linear_list_event)
        val linear_location = findViewById<LinearLayout>(R.id.linear_location_event)
        val linear_about = findViewById<LinearLayout>(R.id.linear_about_event)
        val linear_profile = findViewById<LinearLayout>(R.id.linear_my_profile)

        val stb = AnimationUtils.loadAnimation(this, R.anim.stb)

        linear_list.startAnimation(stb)
        linear_location.startAnimation(stb)
        linear_about.startAnimation(stb)
        linear_profile.startAnimation(stb)

        linear_list_event.setOnClickListener {
            val intent = Intent(this,ListEventUserActivity::class.java)
            startActivity(intent)
        }

        linear_location_event.setOnClickListener {
            val intent = Intent(this,ChooseLocationDashboard::class.java)
            startActivity(intent)
        }

        linear_about_event.setOnClickListener {
            val intent = Intent(this,AboutPkkActivity::class.java)
            startActivity(intent)
        }

        linear_my_profile.setOnClickListener {
            val intent = Intent(this,MyProfileActivity::class.java)
            startActivity(intent)
        }

    }
}