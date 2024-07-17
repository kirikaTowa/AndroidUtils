package com.kakusummer.sample.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kakusummer.androidutils.R
import com.kakusummer.androidutils.databinding.ItemWheelCenterBinding


class WheelCenterAdapter : ListAdapter<String, WheelCenterAdapter.CustomViewHolder>(object : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemWheelCenterBinding = DataBindingUtil.inflate(inflater, R.layout.item_wheel_center, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }

    inner class CustomViewHolder(private val binding: ItemWheelCenterBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, position: Int) {
            binding.tvNumber.text = item
            binding.executePendingBindings()
        }
    }
}