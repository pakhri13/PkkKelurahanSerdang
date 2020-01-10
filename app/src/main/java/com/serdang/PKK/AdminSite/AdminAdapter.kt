package com.serdang.PKK.AdminSite

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.serdang.PKK.AdminSite.Model.Event
import com.serdang.PKK.R
import kotlinx.android.synthetic.main.add_layout_admin.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class AdminAdapter(val mCtx : Context, val layoutResId : Int,  val eventList : MutableList<Event>)
    : ArrayAdapter<Event>(mCtx, layoutResId, eventList) {

    private val database = FirebaseDatabase.getInstance()

    private val myRef = database.reference

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
        val view : View = layoutInflater.inflate(layoutResId, null)
        val textNameEvent = view.findViewById<TextView>(R.id.txt_name)
        val textPlaceEvent = view.findViewById<TextView>(R.id.txt_place)
        val textDateEvent = view.findViewById<TextView>(R.id.txt_date)
        val textTimeEvent = view.findViewById<TextView>(R.id.txt_time)
        val textUniformEvent = view.findViewById<TextView>(R.id.txt_uniform)
        val edit = view.findViewById<Button>(R.id.button_edit_admin)
        val delete = view.findViewById<Button>(R.id.button_delete_admin)

        val EventList = eventList[position]

        textNameEvent.text = EventList.name
        textPlaceEvent.text = EventList.place
        textDateEvent.text = EventList.date
        textTimeEvent.text = EventList.time
        textUniformEvent.text = EventList.uniform

        delete.setOnClickListener {
            deleteEvent(EventList)
        }

        edit.setOnClickListener {
            updateEvent(EventList)
        }


        return view

    }

    private fun deleteEvent(EventList : Event) {
        myRef.child("ListEvent").child(EventList.id).removeValue()
        Toast.makeText(mCtx, "Berhasil Menghapus Acara!", Toast.LENGTH_SHORT).show()
    }

    private fun updateEvent(EventList : Event) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("Ubah Acara")
        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.update_layout_admin, null)
        val textNameUp = view.findViewById<TextView>(R.id.event_update_name)
        val textPlaceUp = view.findViewById<TextView>(R.id.event_update_place)
        val textDateUp = view.findViewById<TextView>(R.id.event_update_date)
        val textTimeUp = view.findViewById<TextView>(R.id.event_update_time)
        val textUniformUp = view.findViewById<TextView>(R.id.event_update_uniform)
        textNameUp.setText(EventList.name)
        textPlaceUp.setText(EventList.place)
        textDateUp.setText(EventList.date)
        textTimeUp.setText(EventList.time)
        textUniformUp.setText(EventList.uniform)

        textDateUp.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(mCtx, DatePickerDialog.OnDateSetListener { view, mYear, monthOfYear, dayOfMonth ->
                val simpleDateFormat = SimpleDateFormat("EEEE")
                val date = Date(mYear, monthOfYear, dayOfMonth - 1)
                val dayString = simpleDateFormat.format(date)

//                event_date.text = "$dayString, $dayOfMonth/${monthOfYear + 1}/$mYear"
                textDateUp.setText(dayString + " , " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + mYear)
            }, year, month, day)
            dpd.show()
        }

        textTimeUp.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                textTimeUp.setText(SimpleDateFormat("HH:mm").format(cal.time))
            }
            TimePickerDialog(mCtx, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
        builder.setView(view)
        builder.setPositiveButton("Ubah") {dialog, which ->

            val name = textNameUp.text.toString().trim()
            val place = textPlaceUp.text.toString().trim()
            val date = textDateUp.text.toString().trim()
            val time = textTimeUp.text.toString().trim()
            val uniform = textUniformUp.text.toString().trim()
            if (name.isEmpty()) {
                textNameUp.error = "Masukkan Nama Acara"
                return@setPositiveButton
            }
            if (place.isEmpty()) {
                textPlaceUp.error = "Masukkan Tempat Acara"
                return@setPositiveButton
            }
            if (date.isEmpty()) {
                textDateUp.error = "Masukkan Tanggal Acara"
                return@setPositiveButton
            }
            if (time.isEmpty()) {
                textTimeUp.error = "Masukkan Waktu Acara"
                return@setPositiveButton
            }
            if (uniform.isEmpty()) {
                textUniformUp.error = "Masukkan Seragam / Pakaian Acara"
                return@setPositiveButton
            }
            val updateKamar = Event(EventList.id,name,place,date,time,uniform)
            myRef.child("ListEvent").child(EventList.id).setValue(updateKamar)
            Toast.makeText(mCtx,"Acara Berhasil Diubah!", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Batal") {dialog, which ->

        }
        val alert = builder.create()
        alert.show()
    }
}