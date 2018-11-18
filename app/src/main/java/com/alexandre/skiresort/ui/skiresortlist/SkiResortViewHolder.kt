package com.alexandre.skiresort.ui.skiresortlist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.alexandre.skiresort.R
import com.alexandre.skiresort.domain.model.SkiResort

class SkiResortViewHolder(view: View)  : RecyclerView.ViewHolder(view) {
    private val skiResortName: TextView = view.findViewById(R.id.skiResortName)
    private val skiResortCountry: TextView = view.findViewById(R.id.skiResortCountry)
    private val skiResortMountain: TextView = view.findViewById(R.id.skiResortMountain)
    private val skiResortSlopesKm: TextView = view.findViewById(R.id.skiResortSlopesKm)
    private val skiResortLifts: TextView = view.findViewById(R.id.skiResortLifts)
    private val skiResortSlopes: TextView = view.findViewById(R.id.skiResortSlopes)
    private val favoriteIV: ImageView = view.findViewById(R.id.favoriteIV)

    fun bind(skiResort: SkiResort?, toggleFav: (View?, SkiResort) -> Unit) {
        if (skiResort != null) {
            showSkiResortData(skiResort, toggleFav)
        }
    }

    private fun showSkiResortData(skiResort: SkiResort, toggleFav: (View?, SkiResort) -> Unit) {
        skiResort.apply {
            skiResortName.text = name
            skiResortCountry.text = country
            skiResortMountain.text = mountainRange
            skiResortSlopesKm.text = slopeKm.toString()
            skiResortLifts.text = lifts.toString()
            skiResortSlopes.text = slopes.toString()
            if(isFav) {
                favoriteIV.setImageResource(R.drawable.ic_star_black_24dp)
            }
            else {
                favoriteIV.setImageResource(R.drawable.ic_star_border_black_24dp)
            }
            favoriteIV.setOnClickListener{
                toggleFav(it, skiResort)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): SkiResortViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.ski_resort_item, parent, false)
            return SkiResortViewHolder(view)
        }
    }
}