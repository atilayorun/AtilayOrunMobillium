package com.example.atilayorunmobillium.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.atilayorunmobillium.Util.Util.loadImage
import com.example.atilayorunmobillium.data.api.ApiService
import com.example.atilayorunmobillium.databinding.ItemViewPagerBinding
import com.example.atilayorunmobillium.data.model.Results
import kotlin.Boolean as Boolean1

class ViewPagerAdapter(
    internal var context: Context,
    private var itemList: List<Results>,
    listener: ViewPagerAdapterListener
) :
    PagerAdapter() {

    private var pos = 0
    private val listener = listener

    override fun getCount(): Int {
        return 5
    }

    interface ViewPagerAdapterListener {
        fun sliderItemOnClickListener(movieId: Int)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutItemViewPagerBinding: ItemViewPagerBinding = ItemViewPagerBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
        val holder = ViewHolder(layoutItemViewPagerBinding)

        if (pos > this.itemList.size - 1)
            pos = 0

        if(itemList.isNotEmpty()) {
            val item = this.itemList[pos]
            (container as ViewPager).addView(layoutItemViewPagerBinding.root)
            holder.itemImage.loadImage("${ApiService.photoUrl + itemList[pos].poster_path}")
            holder.itemImage.scaleType = ImageView.ScaleType.FIT_XY
            holder.itemImage.setOnClickListener {
                listener.sliderItemOnClickListener(item.id)
            }
            holder.tvTitle.text = item.title
            holder.tvDescription.text = item.overview
            pos++
        }
        return layoutItemViewPagerBinding.root
    }

    class ViewHolder(binding: ItemViewPagerBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemImage: ImageView = binding.ivSlider
        val tvTitle: TextView = binding.tvTitle
        val tvDescription: TextView = binding.tvDescription
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
