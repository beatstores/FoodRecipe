package com.example.recipe

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.popup_delete.view.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [EditProfile.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfile : Fragment() {

    val mAuth = FirebaseAuth.getInstance()
    var txtCountry : String? = null
    var selectedPhotoUrl: Uri? = null
    var countryData: String? = null

    lateinit var mDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //untuk load
        val img = view!!.findViewById(R.id.imgCompleteProfile) as ImageView

        val sharedPreference:SharedPreference=SharedPreference(requireContext())

        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user!!.uid

        mDatabase.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {
                //load txtname
                txtName?.hint = p.child("name").value.toString()
                tvGender?.text = p.child("gender").value.toString()
                countryData = p.child("country").value.toString()

                //Change image database

                Picasso.get()
                    .load( p.child("imageUrl").value.toString())
                    .transform(RoundedCornersTransformation(300,0))
                    .fit()
                    .placeholder(R.drawable.ic_photo_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .centerCrop()
                    .into(img)
            }
        })

        //get email

        val email = user.email
        txtEmail.text = email

        //untuk Option Select -Spinner

        val country = arrayOf("Indonesia", "USA", "Singapore")

        val adapter = ArrayAdapter(
            activity,
            android.R.layout.simple_spinner_item,
            country
        )

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        spinner.adapter = adapter

        spinner.setSelection(country.indexOf(countryData))

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

        //Change Pass
        txtChange.setOnClickListener {
            mAuth.sendPasswordResetEmail(email.toString()).addOnCompleteListener({listener->
                if(listener.isSuccessful){
                    Log.d(TAG,"Email Sent")
                    sharedPreference.clearSharedPreference()
                    Toast.makeText(activity, "Check your email to change your password ",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(activity,"Error sending email ",Toast.LENGTH_SHORT).show()
                }
            })
        }

        //save image
        imgCompleteProfile.setOnClickListener {
            openImage()
        }

        //save btn
        btnSave.setOnClickListener {
            if(!txtName.text.toString().isNullOrEmpty()){
                mDatabase.child(uid).child("name").setValue(txtName.text.toString())
            }
            mDatabase.child(uid).child("country").setValue(txtCountry)

            val mFragmentManager = fragmentManager as FragmentManager
            mFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container,  Profile(), Profile::class.java.simpleName)
                .commit()

        }

        //delete user
        btnDelete.setOnClickListener {
            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.popup_delete, null)

            val mBuilder = AlertDialog.Builder(activity)
                .setView(mDialogView)

            val mAlertDialog = mBuilder.show()

            mDialogView.btnSubmit.setOnClickListener {

                mAlertDialog.dismiss()

                val emailVerif = mDialogView.vEmail.text.toString()
                val passwordVerif = mDialogView.vPwd.text.toString()

                if(emailVerif.equals(email) && !emailVerif.isNullOrEmpty() && !passwordVerif.isNullOrEmpty()){
                    val authCredential = EmailAuthProvider.getCredential(emailVerif, passwordVerif)
                    sharedPreference.clearSharedPreference()
                    user.reauthenticate(authCredential)
                        .addOnCompleteListener(OnCompleteListener (){ task ->
                            if(task.isSuccessful){
                                mDatabase.child(uid).removeValue()
                                user.delete().addOnCompleteListener { task1 ->
                                    if(task1.isSuccessful){

                                        Toast.makeText(activity, "Delete Account Success", Toast.LENGTH_LONG).show()

                                        startActivity(Intent(activity, Login::class.java))
                                        activity!!.finish()

                                    }
                                }

                            } else {
                                Toast.makeText(activity, "Delete Failed", Toast.LENGTH_LONG).show()
                            }

                        })
                }


            }

            mDialogView.btnCancel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }

    private fun openImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }



    private fun upload(){
        if(selectedPhotoUrl == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("uploads/$filename")

        ref.putFile(selectedPhotoUrl!!).addOnSuccessListener {

            ref.downloadUrl.addOnSuccessListener {
                saveToUser("$it")
            }

        }
    }

    private fun saveToUser(location : String) {
        val img = view!!.findViewById(R.id.imgCompleteProfile) as ImageView
        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
        val user = FirebaseAuth.getInstance().currentUser

        val uid = user!!.uid

        mDatabase.child(uid).child("imageUrl").setValue(location)

        mDatabase.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {

                //Change image database

                Picasso.get()
                    .load(p.child("imageUrl").value.toString())
                    .transform(RoundedCornersTransformation(200, 0))
                    .fit()
                    .placeholder(R.drawable.ic_photo_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .centerCrop()
                    .into(img)
            }
        })

        progressBar.visibility = View.INVISIBLE
        Toast.makeText(context, "Image Uploaded", Toast.LENGTH_LONG).show()
        btnSave.isClickable = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUrl = data.data


            progressBar.visibility = View.VISIBLE
            btnSave.isClickable = false

            upload()
        }
    }

}
