package com.serdang.PKK.UserSite.ListEvent

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.serdang.PKK.AdminSite.AdminAdapter
import com.serdang.PKK.AdminSite.Model.Event
import com.serdang.PKK.R

class ListEventUserActivity : AppCompatActivity() {

    lateinit var ref : DatabaseReference
    lateinit var list : MutableList<Event>
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_event_user)
        supportActionBar?.title = "Daftar Acara PKK"

        ref = FirebaseDatabase.getInstance().getReference("ListEvent")
        list = mutableListOf()
        listView = findViewById(R.id.lv_user_site)

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
                    val adapter = ListEventUserAdapter(this@ListEventUserActivity, R.layout.listview_content_user, list)
                    listView.adapter = adapter
                }
            }
        })
    }
}