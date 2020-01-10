package com.serdang.PKK.AdminSite

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.serdang.PKK.AdminSite.Model.Event
import com.serdang.PKK.R
import kotlinx.android.synthetic.main.add_layout_admin.*
import java.text.SimpleDateFormat
import java.util.*

class AdminAddActivity : AppCompatActivity() {


    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_layout_admin)
        supportActionBar?.title = "PKK Admin Tambah Acara"

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        event_date.setOnClickListener {

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, monthOfYear, dayOfMonth ->
                val simpleDateFormat = SimpleDateFormat("EEEE")
                val date = Date(mYear, monthOfYear, dayOfMonth - 1)
                val dayString = simpleDateFormat.format(date)

//                event_date.text = "$dayString, $dayOfMonth/${monthOfYear + 1}/$mYear"
                event_date.setText(dayString + " , " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + mYear)
            }, year, month, day)
            dpd.show()

        }

        event_time.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                event_time.setText(SimpleDateFormat("HH:mm").format(cal.time))
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        btn_add_event_admin.setOnClickListener {
            val name = event_name.text.toString().trim()
            val place = event_place.text.toString().trim()
            val date = event_date.text.toString().trim()
            val time = event_time.text.toString().trim()
            val uniform = event_uniform.text.toString().trim()
            val progressDialog = ProgressDialog(this)
            if (checkValid()) {
                progressDialog.isIndeterminate = true
                progressDialog.setMessage("Penambahan di Proses")
                progressDialog.show()
                val database = FirebaseDatabase.getInstance()
                val myRef = database.reference
                val idEvent = myRef.push().key.toString()
                val EventMocel = Event(idEvent,name,place,date,time,uniform)

                myRef.child("ListEvent").child(idEvent).setValue(EventMocel).addOnCompleteListener {
                    Log.e("saveDataOnFirebase", "saveDataOnFirebase success")
                    progressDialog.dismiss()
                    startActivity(Intent(this, AdminActivity::class.java))
                }.addOnFailureListener {
                    Log.e("saveDataOnFirebase", "saveDataOnFirebase fail")
                }
            }
        }

        btn_cancel_event_admin.setOnClickListener {
            val intent = Intent(this,AdminActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun checkValid(): Boolean {
        if (event_name.text.toString().trim() == "") {
            Toast.makeText(this, "Masukkan Nama Acara", Toast.LENGTH_SHORT).show()
            return false
        } else if (event_place.text.toString().trim() == "") {
            Toast.makeText(this, "Masukkan Tempat Acara", Toast.LENGTH_SHORT).show()
            return false
        } else if (event_date.text.toString().trim() == "") {
            Toast.makeText(this, "Masukkan Tanggal Acara", Toast.LENGTH_SHORT).show()
            return false
        } else if (event_time.text.toString().trim() == "") {
            Toast.makeText(this, "Masukkan Waktu Acara", Toast.LENGTH_SHORT).show()
            return false
        } else if (event_uniform.text.toString().trim() == "") {
            Toast.makeText(this, "Masukkan Seragam yang Dipakai Acara", Toast.LENGTH_SHORT).show()
            return false
        }

        return true

    }
}