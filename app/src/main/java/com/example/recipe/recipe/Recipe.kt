package com.example.recipe.recipe

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.ParsedRequestListener
import com.example.recipe.Home
import com.example.recipe.Layout
import com.example.recipe.Profile
import com.example.recipe.R
import com.example.recipe.model.information.ExtendedIngredient
import com.example.recipe.model.information.Information
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_layout.*
import kotlinx.android.synthetic.main.fragment_recipe.*
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 */
class Recipe : Fragment() {

    companion object{
        var EXTRA_ID: String = ""
        var EXTRA_URL: String = ""
    }

    val mAuth = FirebaseAuth.getInstance()
    var saved: String = ""

    lateinit var mDatabase : DatabaseReference
    lateinit var mDatabaseLoved : DatabaseReference

    val Url = "https://api.spoonacular.com/recipes/"
    val api = "/information?apiKey="+ Layout.Api
    val link = Url+EXTRA_ID+api
    private val ingredientList: MutableList<ExtendedIngredient> = mutableListOf()
    private lateinit var myIngredient: ingredientAdapter
    private lateinit var myInstruction: instructionAdapter

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
//        val user = FirebaseAuth.getInstance().currentUser
//
//        val uid = user!!.uid
//
//        mDatabase.child(uid).addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//
//            override fun onDataChange(p: DataSnapshot) {
//                val saved = p.child("saved").value.toString()
//                val loved = p.child("loved").value.toString()
//                var Hsaved: String = ""
//                menu_recipe?.setOnNavigationItemSelectedListener { item ->
//                    when(item.itemId){
//                        R.id.saveNav -> {
//
//                            val lsaved = saved.split(",")
//                            val lsave = lsaved.size - 2
//                            val ans: Boolean = lsaved.contains(EXTRA_ID)
//                            if(ans){
//                                for(x in 0..lsave){
//                                    if(!lsaved[x].equals(EXTRA_ID)){
//                                        Hsaved = Hsaved + lsaved[x] + ","
//                                    }
//                                }
//                                mDatabase.child(uid).child("saved").setValue(Hsaved)
//                                Toast.makeText(context, "not Saved", Toast.LENGTH_SHORT).show()
//                            }else{
//                                Hsaved = saved + EXTRA_ID +","
//                                mDatabase.child(uid).child("saved").setValue(Hsaved)
//                                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
//                            }
//
//                            return@setOnNavigationItemSelectedListener true
//                        }
//                        R.id.loveNav -> {
//
//                            val lloved = loved.split(",")
////                            val llove = lloved.size - 2
////                            val ans: Boolean = lloved.contains(EXTRA_ID)
////                            if(ans){
////                                for(x in 0..llove){
////                                    if(!lloved[x].equals(EXTRA_ID)){
////                                        Hsaved = Hsaved + lloved[x] + ","
////                                    }
////                                }
////                                mDatabase.child(uid).child("saved").setValue(Hsaved)
////                                Toast.makeText(context, "not Saved", Toast.LENGTH_SHORT).show()
////                            }else{
////                                Hsaved = saved + EXTRA_ID +","
////                                mDatabase.child(uid).child("saved").setValue(Hsaved)
////                                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
////                            }
//
//                            lateinit var mDatabaseLoved : DatabaseReference
//                            mDatabaseLoved = FirebaseDatabase.getInstance().getReference("Loved")
//
//                            var count : Int
//                            var temp : String
//
//                            mDatabaseLoved.child(EXTRA_ID).addListenerForSingleValueEvent(object : ValueEventListener {
//                                override fun onCancelled(p0: DatabaseError) {
//
//                                }
//
//                                override fun onDataChange(p: DataSnapshot) {
//
//                                    //var ultimaVersion: Integer = counter = p.child("")
//
//                                    //Get Counter
//                                    if(p.child("counter").exists()){
//                                        temp = p.child("counter").value.toString()
//                                        count = temp.toInt() + 1
//                                    }else{
//                                        count = 1
//                                    }
//
//                                    Toast.makeText(context, "$count", Toast.LENGTH_SHORT).show()
//                                    mDatabaseLoved.child(EXTRA_ID).child("counter").setValue((count).toString())
//
//                                }
//
//
//                            })
//
//
//                            return@setOnNavigationItemSelectedListener true
//                        }
//                    }
//                    false
//                }
//
//                getData()
//
//                myIngredient = ingredientAdapter(ingredientList)
//                ingredients_recycler_view?.layoutManager = LinearLayoutManager(context)
//                ingredients_recycler_view.addItemDecoration(DividerItemDecoration(context, OrientationHelper.VERTICAL))
//                ingredients_recycler_view.adapter = myIngredient
//
//                AndroidNetworking.initialize(context)
//
//                AndroidNetworking.get(link)
//                    .build()
//                    .getAsObject(Information::class.java, object: ParsedRequestListener<Information>{
//                        override fun onResponse(response: Information) {
//                            ingredientList.addAll(response.extendedIngredients)
//                            myIngredient.notifyDataSetChanged()
//                        }
//                        override fun onError(anError: ANError?) {
//
//                        }
//
//                    })
//            }
//
//        })
//
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_recipe, container, false)

        val MRec = rootView.findViewById(R.id.menu_recipe) as BottomNavigationView
        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
        val user = FirebaseAuth.getInstance().currentUser
        mDatabaseLoved = FirebaseDatabase.getInstance().getReference("Loved")

        val uid = user!!.uid

        MRec.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.saveNav -> {
                    mDatabase.child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                        override fun onDataChange(p: DataSnapshot) {
                            var saved: String
                            if(p.child("saved").exists()){
                                saved = p.child("saved").value.toString()
                            }else{
                                saved = ""
                            }

                            var Hsaved: String = ""
                            val lsaved = saved.split(",")
                            val lsave = lsaved.size - 2
                            val ans: Boolean = lsaved.contains(EXTRA_ID)
                            if(ans){
                                for(x in 0..lsave){
                                    if(!lsaved[x].equals(EXTRA_ID)){
                                        Hsaved = Hsaved + lsaved[x] + ","
                                    }
                                }
                                mDatabase.child(uid).child("saved").setValue(Hsaved)
                                Toast.makeText(context, "not Saved", Toast.LENGTH_SHORT).show()
                            }else{
                                Hsaved = saved + EXTRA_ID +","
                                mDatabase.child(uid).child("saved").setValue(Hsaved)
                                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                            }
                        }

                    })
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.loveNav -> {

                    mDatabaseLoved.child(EXTRA_ID).addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p: DataSnapshot) {
                            var count : Int
                            var temp : String
                            if(p.child("counter").exists()){
                                temp = p.child("counter").value.toString()
                                count = temp.toInt() + 1
                            }else{
                                count = 1
                            }

                            Toast.makeText(context, "$count", Toast.LENGTH_SHORT).show()
                            mDatabaseLoved.child(EXTRA_ID).child("counter").setValue((count).toString())
                        }

                    })

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.shareNav -> {
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.type="text/plain"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, EXTRA_URL)
                    startActivity(Intent.createChooser(shareIntent ,  " Share Via "))
                }
            }
            false
        }

        getData()

        myIngredient = ingredientAdapter(ingredientList)
        val ingview = rootView.findViewById(R.id.ingredients_recycler_view) as RecyclerView
        ingview.layoutManager = LinearLayoutManager(context)
        ingview.addItemDecoration(DividerItemDecoration(context, OrientationHelper.VERTICAL))
        ingview.adapter = myIngredient

        AndroidNetworking.initialize(context)

        AndroidNetworking.get(link)
            .build()
            .getAsObject(Information::class.java, object: ParsedRequestListener<Information>{
                override fun onResponse(response: Information) {
                    ingredientList.addAll(response.extendedIngredients)
                    myIngredient.notifyDataSetChanged()
                }
                override fun onError(anError: ANError?) {

                }

            })
        return rootView
    }

    fun getData(){


        AndroidNetworking.get(link)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject) {
                    var ins = (Html.fromHtml(response["instructions"].toString()).toString())
                    recipe_name?.text = response["title"].toString()
                    vtotal?.text = response["readyInMinutes"].toString() + " Minutes"
                    vservings?.text = response["servings"].toString() + " Minutes"

                    Picasso.get()
                        .load(response["image"].toString())
                        .transform(RoundedCornersTransformation(10,0))
                        .into(recipe_img)

                    val lins = ins.split(".")
                    val inst = lins.toTypedArray()

                    myInstruction = instructionAdapter(inst)
                    instruction_recycler_view.layoutManager = LinearLayoutManager(context)
                    instruction_recycler_view.addItemDecoration(DividerItemDecoration(context, OrientationHelper.VERTICAL))
                    instruction_recycler_view.adapter = myInstruction

                }

                override fun onError(anError: ANError?) {

                }

            })
    }

}
