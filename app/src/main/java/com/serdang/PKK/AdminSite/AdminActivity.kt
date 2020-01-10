package com.serdang.PKK.AdminSite

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.serdang.PKK.AdminSite.Model.Event
import com.serdang.PKK.Auth.LoginActivity
import com.serdang.PKK.R
import kotlinx.android.synthetic.main.admin_layout.*

class AdminActivity : AppCompatActivity() {

    lateinit var ref : DatabaseReference
    lateinit var list : MutableList<Event>
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_layout)
        supportActionBar?.title = "PKK Admin Dashboard"

        fab_add_admin.setOnClickListener {
            val intent = Intent(this, AdminAddActivity::class.java)
            startActivity(intent)
        }

        fab_logout_admin.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            Toast.makeText(this,"Anda Berhasil Logout",Toast.LENGTH_SHORT).show()
            finishAffinity()
        }
        ref = FirebaseDatabase.getInstance().getReference("ListEvent")
        list = mutableListOf()
        listView = findViewById(R.id.lv_admin_site)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()) {
                    list.clear()
                    for (h in p0.children){
                        val user  = h.getValue(Event::class.java)
                        list.add(user!!)
                    }
                    val adapter = AdminAdapter(this@AdminActivity, R.layout.listview_content_admin, list)
                    listView.adapter = adapter
                }
            }
        })
    }
}