package com.swe401.safeready.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.swe401.safeready.R
import com.swe401.safeready.data.Badge
import com.swe401.safeready.ui.Motion

class BadgeAdapter : RecyclerView.Adapter<BadgeAdapter.ViewHolder>() {

    private var badges: List<Badge> = emptyList()
    private val animated = mutableSetOf<String>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val content: View = view.findViewById(R.id.badgeContent)
        val emoji: TextView = view.findViewById(R.id.badgeEmoji)
        val title: TextView = view.findViewById(R.id.badgeTitle)
        val description: TextView = view.findViewById(R.id.badgeDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_badge, parent, false)
        Motion.springPress(view)
        view.setOnClickListener { Motion.hapticTick(it) }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val badge = badges[position]
        holder.emoji.text = if (badge.unlocked) badge.emoji else "🔒"
        holder.title.text = badge.title
        holder.description.text = badge.description
        holder.content.alpha = if (badge.unlocked) 1f else 0.45f
        // Pop each badge in only the first time it is shown, not on every rebind/onResume
        if (badge.unlocked && animated.add(badge.id)) {
            Motion.popIn(holder.emoji, 120L + position * 40L)
        } else {
            holder.emoji.animate().cancel()
            holder.emoji.scaleX = 1f
            holder.emoji.scaleY = 1f
            holder.emoji.alpha = 1f
        }
        holder.itemView.contentDescription =
            badge.title + if (badge.unlocked) ", unlocked" else ", locked. " + badge.description
    }

    override fun getItemCount(): Int = badges.size

    @SuppressLint("NotifyDataSetChanged")
    fun submit(newBadges: List<Badge>) {
        if (newBadges == badges) return // nothing changed since the last onResume
        badges = newBadges
        notifyDataSetChanged()
    }
}
