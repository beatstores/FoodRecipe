package com.example.recipe.save

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.recipe.Layout
import com.example.recipe.R
import com.example.recipe.recipe.Recipe
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.saved_row.view.*
import org.json.JSONObject

class SavedAdapter (private val savedList: Array<String>, private val ft: FragmentTransaction): RecyclerView.Adapter<savedHolder>(){

    val Url = "https://api.spoonacular.com/recipes/"
    val api = "/information?apiKey="+ Layout.Api

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): savedHolder {
        context = parent.context
        return savedHolder(LayoutInflater.from(context).inflate(R.layout.saved_row, parent, false))
    }

    override fun getItemCount(): Int = savedList.size - 1

    override fun onBindViewHolder(holder: savedHolder, position: Int) {
        val savedName = holder.itemView.saved_name
        val savedAvvatar = holder.itemView.saved_avatar

        val saved = savedList[position]
        val link = Url+saved+api

        AndroidNetworking.get(link)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    savedName.text = response["title"].toString()
                    Picasso.get()
                        .load(response["image"].toString())
                        .into(savedAvvatar)
                }

                override fun onError(anError: ANError) {

                }
            })

        holder.itemView.setOnClickListener {
            Recipe.EXTRA_ID = saved

            ft.replace(R.id.fragment_container, Recipe(), Recipe::class.java.simpleName)
                .addToBackStack(Recipe::class.java.simpleName).commit()
        }

    }

}

class savedHolder(itemView: View): RecyclerView.ViewHolder(itemView)