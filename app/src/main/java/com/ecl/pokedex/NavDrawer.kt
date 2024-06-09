package com.ecl.pokedex

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ExpandableListView
import com.ecl.pokedex.adapters.ELV_NavAdapter
import com.ecl.pokedex.data.GenItemData
import com.ecl.pokedex.databinding.NavLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NavDrawer(private val activity: Activity, binding: NavLayoutBinding) {
    private val expandableListView: ExpandableListView
    private lateinit var expandableListAdapter: ELV_NavAdapter

    var onGenClicked: ((id: Int) -> Boolean)? = null

    init {
        expandableListView = binding.expandableListView
        CoroutineScope(Dispatchers.IO).launch {
            expandableListAdapter = ELV_NavAdapter(activity, getListData())
            CoroutineScope(Dispatchers.Main).launch {
                expandableListView.setAdapter(expandableListAdapter)

                expandableListView.setOnChildClickListener(OnChildClickListener())

                expandableListView.setOnGroupClickListener { _, _, groupPosition, _ ->
                    val group = expandableListAdapter.groupData[groupPosition]
                    if (group.children.isEmpty()) {
                        return@setOnGroupClickListener when (group.header) {
                            "National Dex" -> {
                                if (activity.localClassName != MainActivity::class.simpleName) {
                                    activity.startActivity(
                                        Intent(
                                            activity,
                                            MainActivity::class.java
                                        )
                                    )
                                    activity.finish()
                                    /*Theme.id = R.style.Theme_Pokedex_Red
                                                activity.recreate()*/
                                    true
                                } else false
                            }

                            else -> false
                        }
                    }
                    false
                }
            }
        }
    }

    private fun getListData(): List<ELV_NavAdapter.Group> {
        val genData = getNavGensData()

        return listOf(
            ELV_NavAdapter.Group("National Dex", listOf()),
            ELV_NavAdapter.Group("Generation Dex", genData),
        )
    }

    private fun getNavGensData(): List<GenItemData> {
        val data = Globals.network.getGenerations()
        return List(
            data.size,
            init = {index ->
                data[index].let {
                    GenItemData(it.name.removePrefix("generation-").uppercase(), it.id)
                }
            }
        )
    }

    inner class OnChildClickListener : ExpandableListView.OnChildClickListener {
        override fun onChildClick(
            parent: ExpandableListView?,
            v: View?,
            groupPosition: Int,
            childPosition: Int,
            id: Long
        ): Boolean {
            val group = expandableListAdapter.groupData[groupPosition]

            return when (group.header) {
                "Generation Dex" -> {
                    val genID = group.children[childPosition].id
                    if (activity.localClassName != GenerationsActivity::class.simpleName) {
                        val intent = Intent(activity, GenerationsActivity::class.java)
                        intent.putExtra("default-gen", genID)
                        activity.startActivity(intent)
                        activity.finish()
                        true
                    }
                    else onGenClicked?.invoke(genID)?: false
                }

                else -> false
            }
        }
    }
}