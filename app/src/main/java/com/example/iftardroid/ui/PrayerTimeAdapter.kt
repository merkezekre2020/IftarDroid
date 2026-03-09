package com.example.iftardroid.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.iftardroid.databinding.ItemPrayerTimeBinding

data class PrayerTimeItem(val name: String, val time: String)

class PrayerTimeAdapter : ListAdapter<PrayerTimeItem, PrayerTimeAdapter.PrayerTimeViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrayerTimeViewHolder {
        val binding = ItemPrayerTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PrayerTimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PrayerTimeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PrayerTimeViewHolder(private val binding: ItemPrayerTimeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PrayerTimeItem) {
            binding.tvPrayerName.text = item.name
            binding.tvPrayerTime.text = item.time
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PrayerTimeItem>() {
        override fun areItemsTheSame(oldItem: PrayerTimeItem, newItem: PrayerTimeItem): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: PrayerTimeItem, newItem: PrayerTimeItem): Boolean {
            return oldItem == newItem
        }
    }
}
