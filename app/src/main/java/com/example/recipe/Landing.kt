package com.example.recipe


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth


class Landing : AppCompatActivity() {



    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        val sharedPreference:SharedPreference=SharedPreference(this)

        val btn = findViewById<Button>(R.id.buttonHome)
        btn.setOnClickListener{


            //cek for auto login
            if(!sharedPreference.getValueString("email").isNullOrBlank()){

                val email = sharedPreference.getValueString("email").toString()
                val password = sharedPreference.getValueString("pwd").toString()

                this.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if(task.isSuccessful){

                        startActivity(Intent(this, Layout::class.java))
                        finish()

                    }else{
                        Toast.makeText(this,"Error Log in" ,Toast.LENGTH_SHORT).show()
                    }
                }


            }
            else {

                startActivity(Intent(this, Login::class.java))
                finish()

            }
        }


    }
}
