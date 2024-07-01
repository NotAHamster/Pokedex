package com.ecl.pokedex

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ExpandableListView
import com.ecl.pokedex.adapters.ELV_NavAdapter
import com.ecl.pokedex.data.GenItemData
import com.ecl.pokedex.data.VerItemData
import com.ecl.pokedex.databinding.NavLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NavDrawer(private val activity: Activity, binding: NavLayoutBinding) {
    private val expandableListView: ExpandableListView
    private lateinit var expandableListAdapter: ELV_NavAdapter

    var onCurrentActivityClicked: ((id: Int) -> Boolean)? = null

    init {
        expandableListView = binding.expandableListView
        CoroutineScope(Dispatchers.IO).launch {
            expandableListAdapter = ELV_NavAdapter(activity, getListData())
            CoroutineScope(Dispatchers.Main).launch {
                expandableListView.setAdapter(expandableListAdapter)

                expandableListView.setOnChildClickListener(OnChildClickListener())

                expandableListView.setOnGroupClickListener(OnGroupClickListener())
            }
        }
    }

    class GroupBase(
        override val header: String
    ): ELV_NavAdapter.Group()
    class GroupGens(
        override val header: String,
        override val children: List<GenItemData>
    ): ELV_NavAdapter.Group()
    class GroupVers(
        override val header: String,
        override val children: List<VerItemData>
    ): ELV_NavAdapter.Group()

    private fun getListData(): List<ELV_NavAdapter.Group> {
        val genData = getNavGensData()
        val verData = getVersionsData()

        return listOf(
            GroupBase("National Dex"),
            GroupGens("Generation Dex", genData),
            GroupVers("Versions", verData),
            GroupBase("Settings")
        )
    }

    private fun getVersionsData(): List<VerItemData> {
        val data = Globals.network.getVersions()
        return List(data.size) {
            data[it].let {
                VerItemData(it.name, it.id)
            }
        }
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
                    val genID = (group as GroupGens).children[childPosition].id
                    if (activity.localClassName != GenerationsActivity::class.simpleName) {
                        val intent = Intent(activity, GenerationsActivity::class.java)
                        intent.putExtra("default-gen", genID)
                        activity.startActivity(intent)
                        activity.finish()
                        true
                    }
                    else onCurrentActivityClicked?.invoke(genID)?: false
                }
                "Versions" -> {
                    val versID = (group as GroupVers).children[childPosition].id
                    if (activity.localClassName != "place-holder, VersionActivity::class.simpleName") {
                        TODO("Start version activity")
                        val intent = Intent(activity, GenerationsActivity::class.java)
                        intent.putExtra("default-gen", versID)
                        activity.startActivity(intent)
                        activity.finish()
                        true
                    }
                    else onCurrentActivityClicked?.invoke(versID)?: false

                }
                else -> false
            }
        }
    }

    inner class OnGroupClickListener : ExpandableListView.OnGroupClickListener {
        override fun onGroupClick(
            parent: ExpandableListView?,
            v: View?,
            groupPosition: Int,
            id: Long
        ): Boolean {
            val group = expandableListAdapter.groupData[groupPosition]
            if (group.children.isEmpty()) {
                return when (group.header) {
                    "National Dex" -> {
                        if (activity.localClassName != MainActivity::class.simpleName) {
                            activity.startActivity(
                                Intent(
                                    activity,
                                    MainActivity::class.java
                                )
                            )
                            activity.finish()
                            true
                        } else {
                            activity.recreate()
                            true
                        }
                    }
                    "Settings" -> {
                        if (activity.localClassName != SettingsActivity::class.simpleName) {
                            activity.startActivityForResult(
                                Intent(
                                    activity,
                                    SettingsActivity::class.java
                                ),
                                1
                            )
                            true
                        } else {
                            activity.recreate()
                            true
                        }
                    }

                    else -> false
                }
            }
            return true
        }

    }
}