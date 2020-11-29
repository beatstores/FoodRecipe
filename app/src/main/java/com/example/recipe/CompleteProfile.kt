package com.example.recipe

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.View
import android.widget.*
import com.example.recipe.model.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_complete_profile.*
import java.util.*

class CompleteProfile : AppCompatActivity() {

    var txtGender : String? = null
    var txtCountry : String? = null
    var selectedPhotoUrl: Uri? = null
    lateinit var mDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)

        //untuk Option Select -Spinner

        val country = arrayOf("Indonesia", "USA", "Singapore")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            country
            )

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                txtCountry = "${parent?.getItemAtPosition(position)}"
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }

        }


        //load image

        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
        val user = FirebaseAuth.getInstance().currentUser

        val uid = user!!.uid

        mDatabase.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {
                //Change image database

                Picasso.get()
                    .load( p.child("imageUrl").value.toString())
                    .transform(RoundedCornersTransformation(300,0))
                    .fit()
                    .placeholder(R.drawable.ic_photo_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .centerCrop()
                    .into(imgCompleteProfile)
            }
        })

        //save
        imgCompleteProfile.setOnClickListener {
            openImage()
        }

        //gender
        radio_gender?.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.female -> {
                    male.isChecked = false
                    txtGender = "Female"
                }
                R.id.male -> {
                    female.isChecked = false
                    txtGender = "Male"
                }
            }
        }

        //submit next
        img_next.setOnClickListener {
            mDatabase = FirebaseDatabase.getInstance().getReference("Users")

            if(txtGender.isNullOrEmpty()){
                Toast.makeText(this, "Please Choose Gender", Toast.LENGTH_LONG).show()
            }else {
                mDatabase.child(uid).child("gender").setValue(txtGender)
                mDatabase.child(uid).child("country").setValue(txtCountry)

                startActivity(Intent(this, Layout::class.java))
                finish()
            }

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUrl = data.data

            progressBar.visibility = View.VISIBLE
            img_next.isClickable = false
            uploadImage()

        }
    }

    private fun openImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }



    private fun uploadImage(){
        if(selectedPhotoUrl == null) return

        val filename = UUID.randomUUID().toString()
        val ref =FirebaseStorage.getInstance().getReference("uploads/$filename")

        ref.putFile(selectedPhotoUrl!!).addOnSuccessListener {

            ref.downloadUrl.addOnSuccessListener {
                saveToUser("$it")
            }

        }
    }

    private fun saveToUser(location : String) {
        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
        val user = FirebaseAuth.getInstance().currentUser

        val uid = user!!.uid

        mDatabase.child(uid).child("imageUrl").setValue(location)

        Toast.makeText(this, "Image Uploaded", Toast.LENGTH_LONG).show()

        mDatabase.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {
                //Change image database

                Picasso.get()
                    .load( p.child("imageUrl").value.toString())
                    .transform(RoundedCornersTransformation(200,0))
                    .fit()
                    .placeholder(R.drawable.ic_photo_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .centerCrop()
                    .into(imgCompleteProfile)
            }
        })

        progressBar.visibility = View.INVISIBLE
        img_next.isClickable = true

    }

    private var doubleBackToExit = false
    override fun onBackPressed() {
        super.onBackPressed()

        if(doubleBackToExit){
            super.onBackPressed()
            System.exit(0)
        } else {
            this.doubleBackToExit = true
            Toast.makeText(this,"Click back again to exit",Toast.LENGTH_SHORT).show()
            Handler().postDelayed(Runnable{doubleBackToExit = false}, 2000)
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)


    }

}
