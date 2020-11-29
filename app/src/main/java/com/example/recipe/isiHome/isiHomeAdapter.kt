package com.example.recipe.isiHome

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe.R
import com.example.recipe.model.food.Results
import com.example.recipe.recipe.Recipe
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.food_row.view.*


class isiHomeAdapter (private val dataList: MutableList<Results>, private val ft: FragmentTransaction): RecyclerView.Adapter<isiHomeHolder>(){

    private val url_photo: String = "https://spoonacular.com/recipeImages/"
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): isiHomeHolder {
        context = parent.context
        return isiHomeHolder(LayoutInflater.from(context).inflate(R.layout.food_row, parent, false))
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: isiHomeHolder, position: Int) {
        val data = dataList[position]

        val foodName = holder.itemView.food_name
        val foodAvatar = holder.itemView.food_avatar
        var urlPhoto: String = url_photo+"${data.image}"
        foodName.text = "${data.title}"

        Picasso.get()
            .load(urlPhoto)
            .into(foodAvatar)

        holder.itemView.setOnClickListener{

            Recipe.EXTRA_ID = "${data.id}"
            Recipe.EXTRA_URL = "${data.sourceUrl}"
            
            ft.replace(R.id.fragment_container, Recipe(), Recipe::class.java.simpleName).addToBackStack(Recipe::class.java.simpleName).commit()
        }


        //set counter
        val loveCount = holder.itemView.loveCount

        lateinit var mDatabaseLoved : DatabaseReference
        mDatabaseLoved = FirebaseDatabase.getInstance().getReference("Loved")

        mDatabaseLoved.child("${data.id}").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {

                if(p.child("counter").exists()) {
                    loveCount.text = "loved : " + p.child("counter").value.toString()
                }else{
                    loveCount.text = "loved : 0"
                }

            }


        })
    }

}

class isiHomeHolder(itemView: View): RecyclerView.ViewHolder(itemView)