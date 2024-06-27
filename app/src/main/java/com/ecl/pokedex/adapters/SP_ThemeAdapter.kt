package com.ecl.pokedex.adapters

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter.IGNORE_ITEM_VIEW_TYPE
import android.widget.SpinnerAdapter
import com.ecl.pokedex.R
import com.ecl.pokedex.databinding.SettingSpinnerItemBinding

class SP_ThemeAdapter(val dataset: List<ThemeDetails>, val context: Context): SpinnerAdapter {
    var onSelect: ((id: Int) -> Unit)? = null

    override fun registerDataSetObserver(observer: DataSetObserver?) {
        //throw NotImplementedError()
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
        //throw NotImplementedError()
    }

    override fun getCount(): Int {
        return dataset.count()
    }

    override fun getItem(position: Int): Any {
        return dataset[position]
    }

    override fun getItemId(position: Int): Long {
        //return dataset[position].id
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = convertView?: inflater.inflate(R.layout.setting_spinner_item, parent, false) //View.inflate(context, R.layout.setting_spinner_item, parent)
        SettingSpinnerItemBinding.bind(view).textView.text = dataset[position].name
        onSelect?.invoke(dataset[position].themeId)
        return view
    }

    override fun getItemViewType(position: Int): Int {
        return IGNORE_ITEM_VIEW_TYPE
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun isEmpty(): Boolean {
        return dataset.isEmpty()
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent)
    }

    fun getPositionOf(themeId: Int): Int {
        return dataset.indexOfFirst {
            it.themeId == themeId
        }
    }
}

data class ThemeDetails(val name: String, val themeId: Int)