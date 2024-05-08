package com.ecl.pokedex

import android.app.Activity
import android.content.Intent
import com.google.android.material.navigation.NavigationView

class NavDrawer(navView: NavigationView, private val activity: Activity) {

    init {
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_item_natDex -> {
                    if (activity.localClassName != MainActivity::class.simpleName) {
                        activity.startActivity(Intent(activity, MainActivity::class.java))
                        activity.finish()
                        true
                    }
                    else false
                }
                R.id.nav_item_genDex -> {
                    if (activity.localClassName != GenerationsActivity::class.simpleName) {
                        activity.startActivity(Intent(activity, GenerationsActivity::class.java))
                        activity.finish()
                        /*Theme.id = R.style.Theme_Pokedex_Red
                        activity.recreate()*/
                        true
                    }
                    else false
                }
                else -> false
            }
            // Close the drawer after handling the click
            //drawerLayout.closeDrawers()
            //true
        }
    }
}