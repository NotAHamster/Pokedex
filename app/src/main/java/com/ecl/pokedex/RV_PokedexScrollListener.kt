package com.ecl.pokedex

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecl.pokedex.adapters.RV_PokedexAdapter
import kotlin.math.abs

class RV_PokedexScrollListener(private val rvPokedexAdapter: RV_PokedexAdapter) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

        if (abs(dy) < 5) {
            // Load data for items in the visible range
            requestData(
                firstVisibleItemPosition,
                lastVisibleItemPosition
            )
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            // Load data for items in the visible range
            requestData(
                firstVisibleItemPosition,
                lastVisibleItemPosition
            )
        }
    }



    private fun requestData(fvip: Int, lvip: Int) {
        if (fvip < 0 || lvip < 0)
            return
        else
            rvPokedexAdapter.requestNewData(fvip, lvip)
    }
}