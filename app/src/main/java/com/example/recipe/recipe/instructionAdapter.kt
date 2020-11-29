package com.example.recipe.recipe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe.R
import kotlinx.android.synthetic.main.instruction_row.view.*

class instructionAdapter(private val instructs: Array<String>): RecyclerView.Adapter<instructionHolder>(){

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): instructionHolder {
        context = parent.context
        return instructionHolder(LayoutInflater.from(context).inflate(R.layout.instruction_row, parent, false))
    }

    override fun getItemCount(): Int = instructs.size - 1

    override fun onBindViewHolder(holder: instructionHolder, position: Int) {
        val instruct = instructs[position]

        val insSum = holder.itemView.ins_sum
        val insTxt = holder.itemView.ins_txt

        insSum.text = (position + 1).toString() + ". "
        insTxt.text = instruct
    }

}

class instructionHolder(itemView: View): RecyclerView.ViewHolder(itemView)