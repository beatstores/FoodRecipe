package com.example.recipe.recipe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe.R
import com.example.recipe.model.information.ExtendedIngredient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_recipe.view.*
import kotlinx.android.synthetic.main.ingredient_row.view.*

class ingredientAdapter(private val ingredientList: MutableList<ExtendedIngredient>): RecyclerView.Adapter<ingredientHolder>(){

    private val url_photo = "https://spoonacular.com/cdn/ingredients_100x100/"
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ingredientHolder {
        context = parent.context
        return ingredientHolder(LayoutInflater.from(context).inflate(R.layout.ingredient_row, parent, false))
    }

    override fun getItemCount(): Int = ingredientList.size

    override fun onBindViewHolder(holder: ingredientHolder, position: Int) {
        val ingredient = ingredientList[position]

        val ingName = holder.itemView.ing_name
        val ingSum = holder.itemView.ing_sum
        val ingImg = holder.itemView.ing_img

        var urlPhoto: String = url_photo+"${ingredient.image}"
        ingName.text = "${ingredient.name}"
        ingSum.text = "${ingredient.amount}"+" "+"${ingredient.unit}"
        Picasso.get()
            .load(urlPhoto)
            .into(ingImg)
    }

}

class ingredientHolder(itemView: View): RecyclerView.ViewHolder(itemView)
