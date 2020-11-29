package com.example.recipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail_.*

class Detail_Activity : AppCompatActivity() {

    companion object {
        const val SUBJECT = "subject"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_)

        val subject = intent.getParcelableExtra(SUBJECT) as Subject
        img_item_photo.setImageResource(subject.photo)
        title_tips.text = "Tips & Trick: "+subject.title
        content_subject.text = subject.content
    }
}
