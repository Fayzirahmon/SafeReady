package com.swe401.safeready.adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.swe401.safeready.R
import com.swe401.safeready.data.DisasterCategory
import com.swe401.safeready.data.ProgressStore

class ProgressAdapter(
    private val categories: List<DisasterCategory>,
    private val store: ProgressStore
) : RecyclerView.Adapter<ProgressAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val emoji: TextView = view.findViewById(R.id.progressEmoji)
        val name: TextView = view.findViewById(R.id.progressName)
        val level: TextView = view.findViewById(R.id.progressLevel)
        val bar: LinearProgressIndicator = view.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_progress, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        val color = ContextCompat.getColor(holder.itemView.context, category.colorRes)
        val best = store.bestScore(category.id)

        holder.emoji.text = category.emoji
        holder.emoji.backgroundTintList = ColorStateList.valueOf(color).withAlpha(50)
        holder.name.text = category.name
        holder.level.text = if (best >= 0) "$best% · ${ProgressStore.levelFor(best)}" else ProgressStore.levelFor(best)
        holder.bar.setIndicatorColor(color)
        holder.bar.trackColor = ColorStateList.valueOf(color).withAlpha(40).defaultColor
        holder.bar.setProgressCompat(best.coerceAtLeast(0), false)
    }

    override fun getItemCount(): Int = categories.size

    @SuppressLint("NotifyDataSetChanged")
    fun refresh() = notifyDataSetChanged()
}
