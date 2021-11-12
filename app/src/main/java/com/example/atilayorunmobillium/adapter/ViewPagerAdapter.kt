package com.example.atilayorunmobillium.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.atilayorunmobillium.R
import com.example.atilayorunmobillium.Util.Util.loadImage
import com.example.atilayorunmobillium.model.Results
import kotlin.Boolean as Boolean1

class ViewPagerAdapter(internal var context: Context, internal var itemList: List<Results>) :
    PagerAdapter() {

    private var mLayoutInflater: LayoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var pos = 0

    override fun getCount(): Int {
        return 5
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val holder = ViewHolder()
        val itemView = mLayoutInflater.inflate(R.layout.layout_item_view_pager, container, false)
        holder.itemImage = itemView.findViewById(R.id.iv_slider) as ImageView
        holder.tvTitle = itemView.findViewById(R.id.tv_title)
        holder.tvDescription = itemView.findViewById(R.id.tv_description)
        if (pos > this.itemList.size - 1)
            pos = 0

        holder.sliderItem = this.itemList[pos]
        holder.itemImage.loadImage("https://www.themoviedb.org/t/p/w220_and_h330_face/${itemList[pos].poster_path}")

        (container as ViewPager).addView(itemView)

        holder.itemImage.scaleType = ImageView.ScaleType.FIT_XY
        holder.tvTitle.text = holder.sliderItem.title
        holder.tvDescription.text = holder.sliderItem.overview
        pos++
        return itemView
    }

    class ViewHolder {
        lateinit var sliderItem: Results
        lateinit var itemImage: ImageView
        lateinit var tvTitle: TextView
        lateinit var tvDescription: TextView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean1 {
        return arg0 === arg1 as View
    }

    override fun destroyItem(arg0: View, arg1: Int, arg2: Any) {
        (arg0 as ViewPager).removeView(arg2 as View)
    }
}