package com.serdang.PKK.UserSite.ListEvent

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase
import com.serdang.PKK.AdminSite.Model.Event
import com.serdang.PKK.R

class ListEventUserAdapter (val mCtx : Context, val layoutResId : Int, val eventList : MutableList<Event>)
    : ArrayAdapter<Event>(mCtx, layoutResId, eventList) {

    private val database = FirebaseDatabase.getInstance()

    private val myRef = database.reference

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
        val view : View = layoutInflater.inflate(layoutResId, null)
        val textNameEvent = view.findViewById<TextView>(R.id.txt_name_user)
        val textPlaceEvent = view.findViewById<TextView>(R.id.txt_place_user)
        val textDateEvent = view.findViewById<TextView>(R.id.txt_date_user)
        val textTimeEvent = view.findViewById<TextView>(R.id.txt_time_user)
        val textUniformEvent = view.findViewById<TextView>(R.id.txt_uniform_user)

        val EventList = eventList[position]

        textNameEvent.text = EventList.name
        textPlaceEvent.text = EventList.place
        textDateEvent.text = EventList.date
        textTimeEvent.text = EventList.time
        textUniformEvent.text = EventList.uniform

        return view

    }

}