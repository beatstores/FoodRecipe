package com.example.recipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.recipe.isiHome.isiHome
import com.example.recipe.save.SavedFragment
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_layout.*


class Layout : AppCompatActivity() {

    lateinit var mDatabase : DatabaseReference

    companion object{
        val Api = "9ded8f10fba0450b8cbab19b40c0fa4e"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)



        val SV = findViewById<SearchView>(R.id.nav_search)
        SV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                isiHome.EXTRA_QUERY = query
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, isiHome(), isiHome::class.java.simpleName)
                    .addToBackStack(isiHome::class.java.simpleName)
                    .commit()
                Toast.makeText(this@Layout, query, Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

        })

        loadFragment(Home())
        bottom_nav.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_SELECTED

        bottom_nav.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.profileNav -> {

                    loadFragment(Profile())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.homeNav ->{

                    loadFragment(Home())
                    return@setOnNavigationItemSelectedListener true

                }

                R.id.savedNav ->{

                    loadFragment(SavedFragment())
                    return@setOnNavigationItemSelectedListener true

                }
            }
            false
        }


//        editsearch = findViewById(R.id.nav_search) as SearchView
//        editsearch!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                Toast.makeText(this@Layout, query, Toast.LENGTH_SHORT).show()
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//
//        })




    }

//override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//    super.onCreate(savedInstanceState, persistentState)
//
//    val SV = findViewById<SearchView>(R.id.nav_search)
//    SV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//        override fun onQueryTextSubmit(query: String): Boolean {
////                Toast.makeText(this@Layout, query, Toast.LENGTH_SHORT).show()
//            Log.e("Test", "Test")
//            return false
//        }
//
//        override fun onQueryTextChange(newText: String): Boolean {
//            return false
//        }
//
//    })
//}

    private fun loadFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment, fragment::class.java.simpleName)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }


    private var doubleBackToExit = false
    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount>0){
            supportFragmentManager.popBackStack()
        } else {
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

}

