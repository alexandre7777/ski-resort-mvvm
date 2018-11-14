package com.alexandre.skiresort.domain.model

data class SkiResort(val skiResortId: Int,
                     val name: String = "",
                     val country: String = "",
                     val mountainRange: String = "",
                     val slopeKm: Int = 0,
                     val lifts: Int = 0,
                     val slopes: Int = 0)