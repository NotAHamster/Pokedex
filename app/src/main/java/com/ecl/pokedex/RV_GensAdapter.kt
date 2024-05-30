package com.ecl.pokedex

import android.content.res.ColorStateList
import android.content.res.Resources.Theme
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class RV_GensAdapter(val dataset: List<GenItemData>, val theme: Theme, val onGenClicked: ((id: Int) -> Unit)) : RecyclerView.Adapter<RV_GensAdapter.ViewHolder>() {
    //var onGenerationClicked: ((id: Int) -> Unit)? = null
    private var activePos = 0

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var id = -1
        val textView: TextView
        val cardView: MaterialCardView
        init {
            textView = view.findViewById(R.id.tv_genName)
            cardView = view.findViewById(R.id.mcv_genCard)
            cardView.setOnClickListener {
                if (id < 0) throw Exception("generation id: $id, was not set")

                //cardView.setCardForegroundColor(highlightColor)
                //onGenerationClicked?.invoke(id)
                setNewActive(adapterPosition)
                onGenClicked.invoke(id)
            }
        }
    }

    fun setNewActive(newPos: Int) {
        dataset[activePos].active = false
        notifyItemChanged(activePos)
        dataset[newPos].active = true
        notifyItemChanged(newPos)
        activePos = newPos
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tab_generation, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = dataset[position].name
        holder.id = dataset[position].id
        if (dataset[position].active) {
            //holder.cardView.setCardForegroundColor(highlightColor)
            //val light = ColorUtils.getColorHighlight(theme)
            //val base = ColorUtils.getColorPrimary(theme)
            val blend = ColorUtils.getColorPrimaryHighlight(theme)
            holder.cardView.setCardBackgroundColor(ColorStateList.valueOf(blend))
        }
        else
            holder.cardView.setCardBackgroundColor(ColorUtils.getColorPrimary(theme))
    }

    override fun getItemCount() = dataset.size
}