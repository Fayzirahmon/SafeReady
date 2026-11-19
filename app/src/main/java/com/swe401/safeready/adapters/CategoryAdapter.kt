package com.swe401.safeready.adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.swe401.safeready.R
import com.swe401.safeready.data.DisasterCategory
import com.swe401.safeready.data.ProgressStore
import com.swe401.safeready.ui.Motion

class CategoryAdapter(
    private val categories: List<DisasterCategory>,
    private val store: ProgressStore,
    private val onClick: (DisasterCategory) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val emoji: TextView = view.findViewById(R.id.categoryEmoji)
        val name: TextView = view.findViewById(R.id.categoryName)
        val summary: TextView = view.findViewById(R.id.categorySummary)
        val status: TextView = view.findViewById(R.id.categoryStatus)
        val stars: TextView = view.findViewById(R.id.categoryStars)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        Motion.springPress(view, 0.97f)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        val context = holder.itemView.context
        val color = ContextCompat.getColor(context, category.colorRes)

        holder.emoji.text = category.emoji
        holder.emoji.backgroundTintList = ColorStateList.valueOf(color).withAlpha(50)
        holder.name.text = category.name
        holder.summary.text = category.summary

        val best = store.bestScore(category.id)
        val learned = store.isLessonComplete(category.id)
        holder.status.text = when {
            best >= 0 -> "Best score: $best%" + if (learned) " · Lesson learned" else ""
            learned -> "Lesson learned · Exercise not tried"
            else -> "New"
        }
        holder.status.setTextColor(color)
        holder.stars.text = starsFor(best)
        holder.itemView.contentDescription = "${category.name}. ${holder.status.text}"
        holder.itemView.setOnClickListener { onClick(category) }
    }

    override fun getItemCount(): Int = categories.size

    @SuppressLint("NotifyDataSetChanged")
    fun refresh() = notifyDataSetChanged()

    companion object {
        /** 1 to 3 stars based on the score, empty if not attempted. */
        fun starsFor(score: Int): String = when {
            score >= 80 -> "★★★"
            score >= 60 -> "★★☆"
            score >= 40 -> "★☆☆"
            score >= 0 -> "☆☆☆"
            else -> ""
        }
    }
}
