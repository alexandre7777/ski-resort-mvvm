package com.alexandre.skiresort

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.*
import android.util.Log
import com.alexandre.skiresort.data.SkiResortRepo
import com.alexandre.skiresort.db.SkiResortDao
import com.alexandre.skiresort.db.model.SkiResort
import com.alexandre.skiresort.service.SkiResortListService
import com.alexandre.skiresort.service.requestSkiResort
import io.mockk.*
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.mock
import java.util.concurrent.Executors

class SkiResortRepoTest {

    //inline fun <reified T> lambdaMock(): T = mock(T::class.java)

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun repo_empty() {
        val skiResortDao = mockkClass(SkiResortDao::class)
        val skiResortListService = SkiResortListService.create()

        val liveDataDb = MutableLiveData<List<SkiResort>>()
        liveDataDb.postValue(listOf(SkiResort(1, "Val d'Isère", "France", "Alps", 300, 83, 96, true)))

        val liveDataService = MutableLiveData<List<com.alexandre.skiresort.service.model.SkiResort>>()
        liveDataService.postValue(listOf(com.alexandre.skiresort.service.model.SkiResort(1, "Val d'Isère", "France", "Alps", 300, 83, 96, "sunny")))

        //val result = MutableLiveData<List<com.alexandre.skiresort.domain.model.SkiResort>>()
        //result.postValue(listOf(com.alexandre.skiresort.domain.model.SkiResort(1, "Val d'Isère", "France", "Alps", 300, 83, 96, true, R.drawable.ic_wb_sunny)))

        every {
            skiResortDao.getAllSkiResorts()
        } returns liveDataDb

        //every {
        //    requestSkiResort(skiResortListService, {}, {})
        //} returns liveDataService

        val expetedRes1 = listOf(com.alexandre.skiresort.domain.model.SkiResort(1, "Val d'Isère", "France", "Alps", 300, 83, 96, true))
        val expetedRes2 = listOf(com.alexandre.skiresort.domain.model.SkiResort(2, "Val d'Isère", "France", "Alps", 300, 83, 96, true, R.drawable.ic_wb_sunny))

        var calledNb = 1
        SkiResortRepo(skiResortListService, skiResortDao, Executors.newSingleThreadExecutor()).getAllSkiResorts().observeForever {
            println("CALL")
            if(calledNb == 1) {
                assertEquals(expetedRes1, it)
            }
            else if(calledNb == 2)
            {
                assertEquals(expetedRes2, it)
            }
            calledNb++
        }

        //liveDataService.postValue(listOf(com.alexandre.skiresort.service.model.SkiResort(1, "Val d'Isère", "France", "Alps", 300, 83, 96, "sunny")))


        //val observer = lambdaMock<(String) -> Unit>()
        //val lifecycle = LifecycleRegistry(mock(LifecycleOwner::class.java))
        //lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

    }

    /*@Test
    fun test() {
        val skiResortDao = mockkClass(SkiResortDao::class)
        val skiResortRepo = spyk<SkiResortRepo>(SkiResortRepo(SkiResortListService.create(), skiResortDao, Executors.newSingleThreadExecutor()), recordPrivateCalls = true)

        val liveDataDb = MutableLiveData<List<SkiResort>>()
        liveDataDb.value = listOf(SkiResort(1, "Val d'Isère", "France", "Alps", 300, 83, 96))

        val liveDataService = MutableLiveData<List<SkiResort>>()
        liveDataService.value = listOf(SkiResort(1, "Val d'Isère", "France", "Alps", 300, 83, 96))


        every { skiResortRepo getProperty "liveDataDb" } returns liveDataDb
        every { skiResortRepo getProperty "liveDataService" } returns liveDataService

        val result = MediatorLiveData<List<com.alexandre.skiresort.domain.model.SkiResort>>()

        assertEquals(result.value, skiResortRepo.getAllSkiResorts().value)
    }*/

    @Test
    fun liveData() {
        var calledNb = 1

        val liveData = MutableLiveData<Int>()

        liveData.observeForever {

            if(calledNb == 1)
            {
                assertEquals(5, it)
            }
            else if(calledNb == 2)
            {
                assertEquals(6, it)
            }
            println(it.toString())

            calledNb++
        }


        liveData.postValue(5)

        //assertEquals(1, liveData.value)

        liveData.postValue(6)



        //assertEquals(2, liveData.value)

    }

}