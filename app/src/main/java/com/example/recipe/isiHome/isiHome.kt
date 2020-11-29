package com.example.recipe.isiHome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.*
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.example.recipe.Layout
import com.example.recipe.R
import com.example.recipe.model.food.Food
import com.example.recipe.model.food.Results
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_isi_home.*

/**
 * A simple [Fragment] subclass.
 */
class isiHome : Fragment() {

    private val dataList: MutableList<Results> = mutableListOf()
    private lateinit var myAdapter: isiHomeAdapter

    companion object{
        var EXTRA_QUERY = "QUERY"
        var EXTRA_IMAGE = "IMAGE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_isi_home, container, false)

        val mFragmentManager = fragmentManager as FragmentManager
        val mFragmentTransaction = mFragmentManager.beginTransaction()

        myAdapter = isiHomeAdapter(dataList, mFragmentTransaction)
        val mRecyclerView : RecyclerView = rootView.findViewById(R.id.food_recycler_view)
        val mGridLayoutManager: GridLayoutManager = GridLayoutManager(context,2)
        mRecyclerView.layoutManager = mGridLayoutManager
        mRecyclerView.adapter = myAdapter

        val url = "https://api.spoonacular.com/recipes/search?query="
        var query: String = EXTRA_QUERY
        val api = "&apiKey="+Layout.Api

        AndroidNetworking.initialize(context)

        AndroidNetworking.get(url+query+api)
            .build()
            .getAsObject(Food::class.java, object: ParsedRequestListener<Food> {
                override fun onResponse(response: Food) {
                    progressBar?.visibility = View.INVISIBLE
                    dataList.addAll(response.results)
                    myAdapter.notifyDataSetChanged()
                }

                override fun onError(anError: ANError?) {
                }

            })
//        back?.visibility = View.VISIBLE
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var mTitle: String
        progressBar.visibility = View.VISIBLE
        Picasso.get()
            .load(EXTRA_IMAGE)
            .transform(RoundedCornersTransformation(10,0))
            .fit()
            .into(food_bar_img)
        if(EXTRA_QUERY == "Main%20Course"){
            mTitle = "Main Course"
        }else{
            mTitle = EXTRA_QUERY
        }
        food_bar_text.text = mTitle
    }



}
