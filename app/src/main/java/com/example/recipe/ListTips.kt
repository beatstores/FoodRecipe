package com.example.recipe

import android.content.res.TypedArray
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_tips_trick.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListTips.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListTips : Fragment() {
    private val list = ArrayList<Subject>()
    private lateinit var image: TypedArray
    private lateinit var titleName: Array<String>
    private lateinit var short_desc: Array<String>
    private lateinit var content: Array<String>
    private lateinit var myAdapter: ListTipsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tips_trick, container, false)

        list.addAll(getListSubject())

        myAdapter = ListTipsAdapter(list)
        val mRecyclerView : RecyclerView? = view.findViewById(R.id.rv_tipstrick)
        mRecyclerView?.layoutManager = LinearLayoutManager(activity)
        mRecyclerView?.adapter = myAdapter
        return view

    }

    fun getListSubject(): ArrayList<Subject> {
        titleName = resources.getStringArray(R.array.title)
        short_desc = resources.getStringArray(R.array.short_description)
        image = resources.obtainTypedArray(R.array.image_list)
        content = resources.getStringArray(R.array.content)

        val listSubject = ArrayList<Subject>()
        for (position in titleName.indices){
            val subj = Subject(
                titleName[position],
                short_desc[position],
                image.getResourceId(position, -1),
                content[position]
            )
            listSubject.add(subj)
        }
        return listSubject
    }
}
