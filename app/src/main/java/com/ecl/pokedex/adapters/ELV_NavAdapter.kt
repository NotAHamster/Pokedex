package com.ecl.pokedex.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.ecl.pokedex.data.GenItemData
import com.ecl.pokedex.R

class ELV_NavAdapter(
    private val context: Context,
    val groupData: List<Group>
) : BaseExpandableListAdapter() {

    abstract class Group {
        abstract val header: String
        open val children: List<Child> = listOf()
    }
    abstract class Child {
        abstract val name: String
    }
    //data class Group(val header: String, val children: List<GenItemData>)


    override fun getGroupCount(): Int {
        return groupData.count()
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return groupData[groupPosition].children.count()
    }

    override fun getGroup(groupPosition: Int): Any {
        return groupData[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {


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

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var view = convertView

        if (view == null || view.tag != "parent") {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.nav_list_parent, parent, false)
            view.tag = "parent"
        }

        view!!.findViewById<TextView>(R.id.tv_nav_parent).text = groupData[groupPosition].header

        return view
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var view = convertView
        val childData = groupData[groupPosition].children[childPosition]

        if (view == null || view.tag != "child") {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.nav_list_child, parent, false)
            view.tag = "child"
        }

        view!!.findViewById<TextView>(R.id.tv_nav_child).text = childData.name

        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}