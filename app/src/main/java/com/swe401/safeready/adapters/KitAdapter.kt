package com.swe401.safeready.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import com.swe401.safeready.R
import com.swe401.safeready.data.KitItem
import com.swe401.safeready.ui.Motion

class KitAdapter(
    private val items: List<KitItem>,
    private val checked: MutableSet<String>,
    private val onToggle: (KitItem, Boolean) -> Unit
) : RecyclerView.Adapter<KitAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val check: MaterialCheckBox = view.findViewById(R.id.kitCheck)
        val name: TextView = view.findViewById(R.id.kitName)
        val note: TextView = view.findViewById(R.id.kitNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kit, parent, false)
        Motion.springPress(view, 0.97f)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val isChecked = item.id in checked
        holder.check.isChecked = isChecked
        holder.name.text = item.name
        holder.note.text = item.note
        holder.itemView.contentDescription =
            item.name + if (isChecked) ", packed" else ", not packed"

        // The whole card is the touch target
        holder.itemView.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos == RecyclerView.NO_POSITION) return@setOnClickListener
            val nowChecked = item.id !in checked
            if (nowChecked) checked.add(item.id) else checked.remove(item.id)
            holder.check.isChecked = nowChecked
            holder.itemView.contentDescription = item.name + if (nowChecked) ", packed" else ", not packed"
            if (nowChecked) {
                Motion.hapticConfirm(it)
                Motion.pulse(holder.check)
            } else {
                Motion.hapticTick(it)
            }
            onToggle(item, nowChecked)
        }
    }

    override fun getItemCount(): Int = items.size
}
