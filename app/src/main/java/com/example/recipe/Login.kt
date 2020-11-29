package com.example.recipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()
    lateinit var mDatabase : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        buttonLogin.setOnClickListener {
            loginUser()
        }

        goToRegister.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun loginUser(){

        val sharedPreference:SharedPreference=SharedPreference(this)

        var email = emailLogin.text.toString()
        var password = passwordLogin.text.toString()

        if(email.isEmpty()){
           emailLogin.error = "Please input your email"
            emailLogin.requestFocus()
            return
        }

        if(password.isEmpty()){
            passwordLogin.error = "Please input password"
            passwordLogin.requestFocus()
            return
        }



        if(!email.isEmpty() && !password.isEmpty()){
            this.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->

                if(task.isSuccessful){
                    sharedPreference.save("email",email)
                    sharedPreference.save("pwd",password)
                    cekActivity()
                }else{
                    Toast.makeText(this,"Error Logging in : (",Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            Toast.makeText(this,"Please fill up the Credentials : | ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cekActivity() {
        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
        val user = FirebaseAuth.getInstance().currentUser
        var cek: String? = null

        val uid = user!!.uid

        mDatabase.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {
                cek = p.child("gender").value.toString()

                if (cek == "null") {
                    startActivity(Intent(this@Login, CompleteProfile::class.java))
                    finish()
                }else{
                    startActivity(Intent(this@Login, Layout::class.java))
                    finish()
                }
            }
        })
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

    }

