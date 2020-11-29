package com.example.recipe


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_list_tips.view.*
import kotlinx.android.synthetic.main.fragment_list_tips.view.img_item_photo
import kotlinx.android.synthetic.main.fragment_list_tips.view.title_tips

class ListTipsAdapter (private val listSubject: ArrayList<Subject>): RecyclerView.Adapter<ListTipsAdapter.ListViewHolder>(){
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.fragment_list_tips, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listSubject.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listSubject[position])
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(subject: Subject) {
            with(itemView) {
                itemView.setOnClickListener {
                    val intentToDetail = Intent(itemView.context, Detail_Activity::class.java)
                    intentToDetail.putExtra(Detail_Activity.SUBJECT,subject)
                    itemView.context.startActivity(intentToDetail)
                }

                img_item_photo.setImageResource(subject.photo)
                title_tips.text = subject.title
                short_desc.text = subject.short_description
            }
        }
    }
}