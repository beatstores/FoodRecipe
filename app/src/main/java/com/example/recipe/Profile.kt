package com.example.recipe


import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.recipe.isiHome.isiHome

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_profile.*
import java.net.URL


class Profile : Fragment() {

    lateinit var mDatabase : DatabaseReference
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)




    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
        val user = FirebaseAuth.getInstance().currentUser
        val sharedPreference:SharedPreference=SharedPreference(requireContext())
        val uid = user!!.uid
        val img = view!!.findViewById(R.id.imgProfile) as ImageView


        mDatabase.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {
                //Load Name
                txtName?.text =  p.child("name").value.toString()

                //Load Image
                Picasso.get()
                    .load( p.child("imageUrl").value.toString())
                    .transform(RoundedCornersTransformation(200,0))
                    .fit()
                    .placeholder(R.drawable.ic_photo_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .centerCrop()
                    .into(img)

                //Load Gender
                txtGender?.text = p.child("gender").value.toString()

                //Load Country
                txtCountry?.text = p.child("country").value.toString()

            }
        })


        //logout

        signOut.setOnClickListener {
            sharedPreference.clearSharedPreference()

            mAuth.signOut()
            val intent = Intent(activity, Login::class.java)
            startActivity(intent)
            activity!!.finish()
        }

        //get email
        val email = user.email
        txtEmail.text = email
        title.text = "Profile"

        //edit
        val mFragmentManager = fragmentManager as FragmentManager
        btnEdit.setOnClickListener {
            mFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container,  EditProfile(), EditProfile::class.java.simpleName)
                .addToBackStack(EditProfile::class.java.simpleName)
                .commit()
        }


        //change password
//        txtChange.setOnClickListener {
//            mAuth.sendPasswordResetEmail(email.toString()).addOnCompleteListener({listener->
//                if(listener.isSuccessful){
//                    Log.d(TAG,"Email Sent")
//                    Toast.makeText(activity, "Check your email to change your password ",Toast.LENGTH_SHORT).show()
//                }else{
//                    Toast.makeText(activity,"Error sending email ",Toast.LENGTH_SHORT).show()
//                }
//            })
//        }



    }
}

