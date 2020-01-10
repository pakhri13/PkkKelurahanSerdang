package com.serdang.PKK.Auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.serdang.PKK.Auth.Model.RegisterModel
import com.serdang.PKK.R
import kotlinx.android.synthetic.main.register_layout.*


class RegistrActivity : AppCompatActivity() {

    private var mAuth : FirebaseAuth? = null
    val fbAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_layout)

        mAuth = FirebaseAuth.getInstance()

        val autotextView = findViewById<AutoCompleteTextView>(R.id.edt_input_position_regis)
        val position = resources.getStringArray(R.array.Position)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, position)
        autotextView.setAdapter(adapter)


        btn_Register.setOnClickListener {
            val name = edt_input_nama_regis.text.toString().trim()
            val email = edt_input_mail_regis.text.toString().trim()
            val password = edt_input_password_regis.text.toString().trim()
            val jabatan = edt_input_position_regis.text.toString().trim()
            val progressDialog = ProgressDialog(this)
            if (checkValid()){
                progressDialog.isIndeterminate = true
                progressDialog.setMessage("Register di proses")
                progressDialog.show()
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {task: Task<AuthResult> ->
                        if (task.isSuccessful) {
                            Toast.makeText(this,"Registrasi Berhasil",Toast.LENGTH_SHORT).show()
                            Log.e("Success", "Sign up success")
                            val akunId = fbAuth.currentUser!!.uid
                            val registerModel = RegisterModel(akunId, name, email,jabatan)
                            saveDataOnFirebase(registerModel)
                            progressDialog!!.dismiss()
                        } else {
                            progressDialog!!.dismiss()
                            Toast.makeText(this,"Registrasi Gagal",Toast.LENGTH_SHORT).show()
                            Log.e("Fail", "Sign up fail")
                        }
                    }
            }
        }

    }

    private fun saveDataOnFirebase(registerModel: RegisterModel) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference
        val getId = registerModel.uid
        myRef.child("User").child(getId).setValue(registerModel).addOnSuccessListener {
            Log.e("saveDataOnFirebase", "saveDataOnFirebase success")
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }.addOnFailureListener {
            Log.e("saveDataOnFirebase", "saveDataOnFirebase fail")
        }

    }

    private fun checkValid() : Boolean {
        if (edt_input_nama_regis.text.toString().trim() == "") {
            Toast.makeText(this,"Nama Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            return false
        } else if (edt_input_mail_regis.text.toString().trim() == "") {
            Toast.makeText(this,"Email Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(edt_input_mail_regis.text.toString().trim()).matches()) {
            Toast.makeText(this, "Email Harus Valid", Toast.LENGTH_SHORT).show()
            return false
        } else if (edt_input_position_regis.text.toString().trim() == "") {
            Toast.makeText(this, "Jabatan Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            return false
        } else if (edt_input_password_regis.text.toString().trim() == "") {
            Toast.makeText(this, "Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            return false
        }else if (edt_input_conf_password_regis.text.toString().trim() == ""){
            Toast.makeText(this, "Confirm Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            return false
        }else {
            if (edt_input_conf_password_regis.text.toString().trim().equals(edt_input_password_regis.text.toString().trim())) {
                return true
            } else
                Toast.makeText(this,"Confrim Password Tidak Sama dengan Password",Toast.LENGTH_SHORT).show()
                return false
        }
    }

}