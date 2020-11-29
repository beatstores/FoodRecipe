package com.example.recipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import com.example.recipe.model.user.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern

class Register : AppCompatActivity() {

    lateinit var mDatabase : DatabaseReference
    val mAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mDatabase = FirebaseDatabase.getInstance().getReference("Users")



        buttonRegis.setOnClickListener(){
            registerUser()
        }

        goToLogin.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()

        }
    }

    private fun registerUser(){
        val name = nameRegis.text.toString()
        val email = emailRegis.text.toString()
        val password = passwordRegis.text.toString()
        val retype = repasswordRegis.text.toString()

        if(name.isEmpty()){
            nameRegis.error = "Please enter name"
            nameRegis.requestFocus()
            return

        }


        if(!Patterns.EMAIL_ADDRESS.matcher(emailRegis.text.toString()).matches()){
            emailRegis.error = "Please enter valid email"
            emailRegis.requestFocus()
            return

        }

        if(password.isEmpty()){
            passwordRegis.error = "Please enter password"
            passwordRegis.requestFocus()
            return

        }

        if(retype.isEmpty() || password != retype){
            repasswordRegis.error = "Incorrect re-type password"
            repasswordRegis.requestFocus()
            return

        }

        if(!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !retype.isEmpty()){
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                OnCompleteListener { task ->
                    if(task.isSuccessful){
                        val user = mAuth.currentUser
                        val uid = user!!.uid
                        val url = "https://image.shutterstock.com/image-vector/chef-cooking-hat-vector-logo-260nw-1264315489.jpg"
                        val userData = User(name, url, null, null)

                        mDatabase.child(uid).setValue(userData)
                        startActivity(Intent(this, Login::class.java))
                        Toast.makeText(this, "Successfully Registered : )",Toast.LENGTH_LONG).show()
                        finish()
                    }else{
                        Toast.makeText(this, "Error registering, " + task.exception?.message , Toast.LENGTH_LONG).show()
                    }
                })
        }else{
            Toast.makeText(this,"Please fill up the Credentials : |",Toast.LENGTH_LONG).show()
        }



    }



}
