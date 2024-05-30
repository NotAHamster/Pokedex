package com.ecl.pokedex.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.ecl.pokedex.GenItemData
import com.ecl.pokedex.R

class ELV_NavAdapter(
    private val context: Context,
    /*private val listDataHeader: List<String>,
    private val listDataChild: HashMap<String, List<String>>*/
    val groupData: List<Group>
) : BaseExpandableListAdapter() {

    data class Group(val header: String, val children: List<GenItemData>)


    override fun getGroupCount(): Int {
        //return listDataHeader.size
        return groupData.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        //return listDataChild[listDataHeader[groupPosition]]?.size ?: 0
        return groupData[groupPosition].children.size
    }

    override fun getGroup(groupPosition: Int): Any {
        //return listDataHeader[groupPosition]
        return groupData[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        //return listDataChild[listDataHeader[groupPosition]]?.get(childPosition) ?: ""
        return groupData[groupPosition].children[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    /*override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        @Suppress("NAME_SHADOWING")
        var convertView = convertView
        val headerTitle = getGroup(groupPosition) as String
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false)
        }
        val lblListHeader = convertView!!.findViewById<TextView>(android.R.id.text1)
        lblListHeader.text = headerTitle
        return convertView
    }*/

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var view = convertView

        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.nav_list_parent, parent, false)
        }

        view!!.findViewById<TextView>(R.id.tv_nav_parent).text = groupData[groupPosition].header

        return view
    }

    /*override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val childText = getChild(groupPosition, childPosition) as String
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_2, null)
        }
        val txtListChild = convertView!!.findViewById<TextView>(android.R.id.text1)
        txtListChild.text = childText
        return convertView
    }*/

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var view = convertView
        val childData = groupData[groupPosition].children[childPosition]

        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.nav_list_parent, parent, false)
        }

        view!!.findViewById<TextView>(R.id.tv_nav_parent).text = childData.name

        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}