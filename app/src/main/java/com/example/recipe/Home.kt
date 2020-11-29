package com.example.recipe


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.recipe.isiHome.isiHome
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_home.*
import java.net.URL


class Home : Fragment() {

    companion object{
        var cekbtn: Boolean = false
    }

    val url_home = arrayOf(
        "https://www.elizabethrider.com/wp-content/uploads/2018/12/Elegant-Healthy-Smoked-Salmon-Appetizer-Elizabeth-Rider-Blog.jpg",
        "https://chowhound1.cbsistatic.com/thumbnail/800/0/chowhound1.cbsistatic.com/blog-media/2006/08/EasySpinachLasagna.jpg",
        "https://img.taste.com.au/4sJKskna/w506-h253-cfill/taste/2016/11/top-50-desserts-117787-1.jpeg",
        "https://i0.wp.com/images-prod.healthline.com/hlcmsresource/images/AN_images/health-benefits-of-eggs-1296x728-feature.jpg?w=1155&h=1528",
        "https://cdn.shopify.com/s/files/1/0150/0232/products/Pearl_Valley_Swiss_Slices_36762caf-0757-45d2-91f0-424bcacc9892_grande.jpg?v=1534871055",
        "https://dairyfarmersofcanada.ca/sites/default/files/image_file_browser/article/2019/getty-854296650.jpg",
        "https://natashaskitchen.com/wp-content/uploads/2020/01/Whole-Chicken.jpg",
        "https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fimg1.cookinglight.timeinc.net%2Fsites%2Fdefault%2Ffiles%2F1502826406%2F1708w-getty-fruit-closeup-CarstenSchanter-EyeEm.jpg",
        "https://www.newtimes.co.rw/sites/default/files/styles/mystyle/public/main/articles/2020/03/01/x9-fruits-veggies-web-impact.jpg.pagespeed.ic.SZm3K2UMnb.jpg",
        "https://static01.nyt.com/images/2017/09/25/dining/bonebrothchickenstock/bonebrothchickenstock-articleLarge.jpg")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mFragmentManager = fragmentManager as FragmentManager

        txtAppetizer.setOnClickListener {isiHome.EXTRA_IMAGE=url_home[0];isiHome.EXTRA_QUERY = "Appetizer";move(mFragmentManager)}
        txtMain.setOnClickListener      {isiHome.EXTRA_IMAGE=url_home[1];isiHome.EXTRA_QUERY = "Main%20Course";move(mFragmentManager)}
        txtDessert.setOnClickListener   {isiHome.EXTRA_IMAGE=url_home[2];isiHome.EXTRA_QUERY = "Dessert";move(mFragmentManager)}
        txtEgg.setOnClickListener       {isiHome.EXTRA_IMAGE=url_home[3];isiHome.EXTRA_QUERY = "Egg";move(mFragmentManager)}
        txtCheese.setOnClickListener    {isiHome.EXTRA_IMAGE=url_home[4];isiHome.EXTRA_QUERY = "Cheese";move(mFragmentManager)}
        txtMilk.setOnClickListener      {isiHome.EXTRA_IMAGE=url_home[5];isiHome.EXTRA_QUERY = "Milk";move(mFragmentManager)}
        txtChicken.setOnClickListener   {isiHome.EXTRA_IMAGE=url_home[6];isiHome.EXTRA_QUERY = "Chicken";move(mFragmentManager)}
        txtFruit.setOnClickListener     {isiHome.EXTRA_IMAGE=url_home[7];isiHome.EXTRA_QUERY = "Fruit";move(mFragmentManager)}
        txtVege.setOnClickListener      {isiHome.EXTRA_IMAGE=url_home[8];isiHome.EXTRA_QUERY = "Vegetables";move(mFragmentManager)}

        imgTips.setOnClickListener {
            val mListFragment = ListTips()
            val fragment = mFragmentManager.findFragmentByTag(ListTips::class.java.simpleName)

            if (fragment !is ListTips) {
                mFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, mListFragment, ListTips::class.java.simpleName)
                    .addToBackStack(ListTips::class.java.simpleName)
                    .commit()
            }
        }
    }

    private fun move(mFragmentManager: FragmentManager){
        mFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, isiHome(), isiHome::class.java.simpleName)
            .addToBackStack(isiHome::class.java.simpleName)
            .commit()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loadImage(url_home[0], imgAppetizer)
        loadImage(url_home[1], imgMain)
        loadImage(url_home[2], imgDessert)
        loadImage(url_home[3], imgEgg)
        loadImage(url_home[4], imgCheese)
        loadImage(url_home[5], imgMilk)
        loadImage(url_home[6], imgChicken)
        loadImage(url_home[7], imgFruit)
        loadImage(url_home[8], imgVege)
        loadImage(url_home[9], imgTips)

    }

    fun loadImage(url : String , img : ImageView){
        Picasso.get()
            .load(url)
            .transform(RoundedCornersTransformation(5,0))
            .fit()
            .centerCrop()
            .into(img)
    }


}

