package com.alexandre.skiresort.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.alexandre.skiresort.db.SkiResortDao
import com.alexandre.skiresort.domain.model.toDbModel
import com.alexandre.skiresort.domain.model.toViewModelFromDb
import com.alexandre.skiresort.service.SkiResortListService
import com.alexandre.skiresort.service.requestSkiResort
import java.util.concurrent.Executor

class SkiResortRepo(private val skiResortListService: SkiResortListService, private val skiResortDao: SkiResortDao, private val ioExecutor: Executor) {

    fun getAllSkiResorts(): LiveData<List<com.alexandre.skiresort.domain.model.SkiResort>> {
        requestSkiResort(skiResortListService, {
            skiResorts ->
            ioExecutor.execute {
                skiResortDao.insertAll(toDbModel(skiResorts))
            }
        }, { error ->

        })
        return Transformations.map(skiResortDao.getAllSkiResorts()) { skiResortList ->
            toViewModelFromDb(skiResortList)
        }
    }
}
