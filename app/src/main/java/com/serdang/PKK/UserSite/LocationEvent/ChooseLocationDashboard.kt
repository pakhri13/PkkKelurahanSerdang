package com.serdang.PKK.UserSite.LocationEvent

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.serdang.PKK.R

class ChooseLocationDashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_location_layout)

        supportActionBar?.title = "Lokasi Acara PKK"

        val linear_kecataman = findViewById<LinearLayout>(R.id.linear_district_kemayoran)
        val linear_kelurahan = findViewById<LinearLayout>(R.id.linear_sub_district_serdang)
        val linear_rptra = findViewById<LinearLayout>(R.id.linear_rptra_serdang)
        val linear_puskesmas = findViewById<LinearLayout>(R.id.linear_puskesmas_serdang)
        val linear_pkk = findViewById<LinearLayout>(R.id.linear_sekretariat_pkk)
        val stb = AnimationUtils.loadAnimation(this, R.anim.stb)

        linear_kecataman.startAnimation(stb)
        linear_kelurahan.startAnimation(stb)
        linear_rptra.startAnimation(stb)
        linear_puskesmas.startAnimation(stb)
        linear_pkk.startAnimation(stb)

        linear_kecataman.setOnClickListener {
            startActivity(Intent(this,KemayoranDistrictLocation::class.java))
        }

        linear_kelurahan.setOnClickListener {
            startActivity(Intent(this,SerdangDistrictLocation::class.java))
        }

        linear_rptra.setOnClickListener {
            startActivity(Intent(this,RptraLocation::class.java))
        }

        linear_puskesmas.setOnClickListener {
            startActivity(Intent(this,PuskesmasSerdangLocation::class.java))

        }

        linear_pkk.setOnClickListener {
            startActivity(Intent(this,SekretariatPkkLocation::class.java))
        }
    }
}