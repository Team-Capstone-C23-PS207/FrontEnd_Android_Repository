package com.example.scumapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scumapps.databinding.ItemRecommendBinding
import com.example.scumapps.service.RekomendasiItem

class RecommendAdapter(private var listRecommend: ArrayList<RekomendasiItem>) : RecyclerView.Adapter<RecommendAdapter.ViewHolder>() {

    private lateinit var binding: ItemRecommendBinding

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecommendBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.recTitle.text = listRecommend[position].title
        viewHolder.binding.recLink.text = listRecommend[position].link
    }

    override fun getItemCount() = listRecommend.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRecommendBinding.bind(view)
    }

}