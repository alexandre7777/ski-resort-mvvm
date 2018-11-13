package com.alexandre.skiresort.domain.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class SkiResort(val skiResortId: Int,
                     val name: String = "",
                     val country: String = "",
                     val mountainRange: String = "",
                     val slopeKm: Int = 0,
                     val lifts: Int = 0,
                     val slopes: Int = 0)