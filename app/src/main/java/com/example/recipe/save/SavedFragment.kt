package com.example.recipe.save

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.*
import com.example.recipe.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_saved.*

/**
 * A simple [Fragment] subclass.
 */
class SavedFragment : Fragment() {

    companion object{
        var datas: String? = null
    }

    val mAuth = FirebaseAuth.getInstance()
    lateinit var mDatabase : DatabaseReference

    private lateinit var myAdapter: SavedAdapter
    var data = "169923,74172,74202,94640,107878"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = inflater.inflate(R.layout.fragment_saved, container, false)
        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
        val user = FirebaseAuth.getInstance().currentUser

        val uid = user!!.uid

        mDatabase.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {
                saved_progressBar?.visibility = View.INVISIBLE
                datas = p.child("saved").value.toString()
                val mFragmentManager = fragmentManager as FragmentManager
                val mFragmentTransaction = mFragmentManager?.beginTransaction()

                var dataList = datas.toString().split(",")
                var aData = dataList.toTypedArray()

                val myAdapter = SavedAdapter(aData, mFragmentTransaction)
                val srv = rootview.findViewById(R.id.saved_recycler_view) as RecyclerView
                srv.layoutManager = GridLayoutManager(context,2)
                srv.adapter = myAdapter
            }

        })
        return rootview
    }

}
