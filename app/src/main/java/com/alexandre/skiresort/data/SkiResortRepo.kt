package com.alexandre.skiresort.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.alexandre.skiresort.db.SkiResortDao
import com.alexandre.skiresort.db.model.SkiResort
import com.alexandre.skiresort.domain.model.toDbModel
import com.alexandre.skiresort.domain.model.toViewModel
import com.alexandre.skiresort.domain.model.toViewModelFromDb
import com.alexandre.skiresort.service.SkiResortListService
import com.alexandre.skiresort.service.requestSkiResort
import java.util.concurrent.Executor

class SkiResortRepo(private val skiResortListService: SkiResortListService, private val skiResortDao: SkiResortDao, private val ioExecutor: Executor) {

    fun getAllSkiResorts(): LiveData<List<com.alexandre.skiresort.domain.model.SkiResort>> {
        return Transformations.map(requestSkiResort(skiResortListService, {

        }, {

        })) { skiResortList ->
            toViewModel(skiResortList)
        }
        //return Transformations.map(skiResortDao.getAllSkiResorts()) { skiResortList ->
        //    toViewModelFromDb(skiResortList)
        //}
    }

    fun updateSkiResortFav(skiResortId: Int, isFav: Boolean) {
        ioExecutor.execute {
            skiResortDao.updateFav(skiResortId, isFav)
        }
    }

    private fun prepareInsertWithFavStatus(skiResorts : List<SkiResort>): List<SkiResort> {
        val mutableIterator = skiResorts.iterator()

        // iterator() extension is called here
        for (skiResort in mutableIterator) {
            skiResort.isFav = skiResortDao.isFav(skiResort.skiResortId)
        }
        return skiResorts
    }
}
