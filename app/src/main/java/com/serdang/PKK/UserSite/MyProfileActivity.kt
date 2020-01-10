package com.serdang.PKK.UserSite

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.serdang.PKK.AdminSite.Model.UploadImage
import com.serdang.PKK.Auth.LoginActivity
import com.serdang.PKK.Auth.Model.RegisterModel
import com.serdang.PKK.R
import kotlinx.android.synthetic.main.my_profile_layout.*
import java.io.IOException

class MyProfileActivity : AppCompatActivity(), View.OnClickListener {

    private var upload: Uri? = null
    val fbAuth = FirebaseAuth.getInstance()
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_profile_layout)

        supportActionBar?.title = "Profile Saya"

        val akunId = fbAuth.currentUser!!.uid

        val ref = FirebaseDatabase.getInstance().getReference("/User")
            .orderByChild("uid").equalTo(akunId)

        val refpoto = FirebaseDatabase.getInstance().getReference("/FotoUser")
            .orderByChild("uid").equalTo(akunId)

        mAuth = FirebaseAuth.getInstance()

        refpoto.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                var getImg = ""
                if (p0!!.exists()) {
                    for (h in p0.children) {
                        val img = h.getValue(UploadImage::class.java)?.photo.toString()

                        getImg = img
                    }
                    Glide.with(this@MyProfileActivity).load(getImg).into(image_my_profile)
                }
            }
        })

        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                var getName = ""
                var getEmail = ""
                var getJabatan = ""

                if (p0!!.exists()) {

                    for (h in p0.children) {
                        val name = h.getValue(RegisterModel::class.java)?.name.toString()
                        val email = h.getValue(RegisterModel::class.java)?.email.toString()
                        val jabatan = h.getValue(RegisterModel::class.java)?.jabatan.toString()

                        getName = name
                        getEmail = email
                        getJabatan = jabatan
                    }

                    text_name_profile.text = getName
                    text_email_profile.text = getEmail
                    text_position_profile.text = getJabatan
                } else {
                    Toast.makeText(this@MyProfileActivity, "Data sedang di muat...",Toast
                        .LENGTH_SHORT).show()
                }
            }
        })

        btn_logout_user.setOnClickListener {
            btnSignOut()
        }
        image_my_profile.setOnClickListener(this)
        upload_foto.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when (v) {
            upload_foto ->{
                val akunId = fbAuth.currentUser!!.uid
                val storageRef = FirebaseStorage.getInstance().getReference("FotoUser")
                val databaseRef = FirebaseDatabase.getInstance().getReference("FotoUser")
                storageRef.putFile(upload!!)
                    .addOnSuccessListener {
                        storageRef.downloadUrl.addOnSuccessListener {
                            val url = it!!.toString()
                            val isi = UploadImage(akunId, url)
                            databaseRef.child(akunId).setValue(isi).addOnCompleteListener{
                                Toast.makeText(this, "Berhasil Mengupload foto", Toast.LENGTH_SHORT).show()

                            }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this,"Sentuh gambar foto untuk upload foto",
                            Toast.LENGTH_SHORT).show()
                        println("Info file : ${it.message}")
                    }

            }

            image_my_profile -> {
                val iPot = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(iPot,0)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            upload = data!!.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, upload)
                image_my_profile.setImageBitmap(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun btnSignOut() {
        mAuth.signOut()
        updateUI(null)

    }

    private fun updateUI(user : FirebaseUser?) {
        if (user == null) {
            startActivity(Intent(this,LoginActivity::class.java))
            Toast.makeText(this, "Anda Telah Logout", Toast.LENGTH_SHORT).show()
            finishAffinity()
        }
    }
}