package com.alexandre.skiresort.data

import android.arch.lifecycle.*
import android.util.Log
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

        println("plop")

        val result = MediatorLiveData<List<com.alexandre.skiresort.domain.model.SkiResort>>()

        val liveDataService = requestSkiResort(skiResortListService, {skiResorts ->
            ioExecutor.execute {
                skiResortDao.insertAll(prepareInsertWithFavStatus(toDbModel(skiResorts)))
            }
        }, {error ->

        })

        val liveDataDb = skiResortDao.getAllSkiResorts()

        result.addSource(liveDataService) { value ->
            println("result.addSource(liveDataService)")
            value?.let {
                println("result.addSource(liveDataService) value != null")
                liveDataDb.value?.let { it2 ->
                    println("liveDataDb.value != null")
                    result.value = toViewModel(it, it2)
                } ?: run {
                    println("liveDataDb.value == null")
                    result.value = toViewModel(it)
                }
            }

                    //combineLatestData(liveData1, liveData2)
        }
        result.addSource(liveDataDb) { value ->
            println("result.addSource(liveDataDb)")
            value?.let {
                println("result.addSource(liveDataDb) value != null")
                liveDataService.value?.let { it2 ->
                    println("liveDataService.value != null")
                    result.value = toViewModel(it2, it)
                }?: run {
                    println("liveDataService.value == null")
                    result.value = toViewModelFromDb(it)
                }
            }
        }
        println("plop end")
        return result
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
