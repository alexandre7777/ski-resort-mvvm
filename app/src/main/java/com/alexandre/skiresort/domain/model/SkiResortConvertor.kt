package com.alexandre.skiresort.domain.model

fun toViewModel(skiResortList: List<com.alexandre.skiresort.service.model.SkiResort>) :
        List<SkiResort> {
    return skiResortList.map {
        SkiResort(
                it.skiResortId,
                it.name,
                it.country,
                it.mountainRange,
                it.slopeKm,
                it.lifts,
                it.slopes,
                weather = it.weather
        )
    }
}

fun toViewModel(skiResortListService: List<com.alexandre.skiresort.service.model.SkiResort>,
                skiResortListDb: List<com.alexandre.skiresort.db.model.SkiResort>) :
        List<SkiResort> {
    return skiResortListService.map {
        SkiResort(
                it.skiResortId,
                it.name,
                it.country,
                it.mountainRange,
                it.slopeKm,
                it.lifts,
                it.slopes,
                getFavFromList(skiResortListDb, it.skiResortId),
                weather = it.weather
        )
    }
}

fun getFavFromList(skiResortListDb: List<com.alexandre.skiresort.db.model.SkiResort>, id: Int): Boolean {
    val mutableIterator = skiResortListDb.iterator()

    // iterator() extension is called here
    for (skiResort in mutableIterator) {
        if(id == skiResort.skiResortId)
            return skiResort.isFav
    }
    return false
}


fun toDbModel(skiResortList: List<com.alexandre.skiresort.service.model.SkiResort>) :
        List<com.alexandre.skiresort.db.model.SkiResort> {
    return skiResortList.map {
        com.alexandre.skiresort.db.model.SkiResort(
                it.skiResortId,
                it.name,
                it.country,
                it.mountainRange,
                it.slopeKm,
                it.lifts,
                it.slopes
        )
    }
}

fun toViewModelFromDb(skiResortList: List<com.alexandre.skiresort.db.model.SkiResort>) :
        List<SkiResort> {
    return skiResortList.map {
        SkiResort(
                it.skiResortId,
                it.name,
                it.country,
                it.mountainRange,
                it.slopeKm,
                it.lifts,
                it.slopes,
                it.isFav
        )
    }
}