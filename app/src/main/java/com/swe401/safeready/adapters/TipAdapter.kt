package com.swe401.safeready.adapters

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.swe401.safeready.R

/** Shows lesson tips grouped under Before / During / After headers (two view types). */
class TipAdapter(
    private val items: List<TipItem>,
    private val headerColor: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    sealed class TipItem {
        data class Header(val title: String) : TipItem()
        data class Tip(val number: Int, val text: String) : TipItem()
    }

    class HeaderHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tipHeader)
    }

    class TipHolder(view: View) : RecyclerView.ViewHolder(view) {
        val number: TextView = view.findViewById(R.id.tipNumber)
        val text: TextView = view.findViewById(R.id.tipText)
    }

    override fun getItemViewType(position: Int): Int =
        if (items[position] is TipItem.Header) TYPE_HEADER else TYPE_TIP

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            HeaderHolder(inflater.inflate(R.layout.item_tip_header, parent, false))
        } else {
            TipHolder(inflater.inflate(R.layout.item_tip, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is TipItem.Header -> {
                val h = holder as HeaderHolder
                h.title.text = item.title
                h.title.background = GradientDrawable().apply {
                    cornerRadius = 40f
                    setColor(headerColor)
                }
            }
            is TipItem.Tip -> {
                val t = holder as TipHolder
                t.number.text = item.number.toString()
                t.number.backgroundTintList = ColorStateList.valueOf(headerColor).withAlpha(50)
                t.text.text = item.text
            }
        }
    }

    override fun getItemCount(): Int = items.size

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_TIP = 1
    }
}
