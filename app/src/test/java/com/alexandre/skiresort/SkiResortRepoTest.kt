package com.alexandre.skiresort

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.*
import com.alexandre.skiresort.data.SkiResortRepo
import com.alexandre.skiresort.db.SkiResortDao
import com.alexandre.skiresort.db.model.SkiResort
import com.alexandre.skiresort.service.SkiResortListService
import io.mockk.*
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.concurrent.Executors
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


class SkiResortRepoTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun repo_empty() {
        val skiResortDao = mockkClass(SkiResortDao::class)

        val retrofit = Retrofit.Builder()
                .baseUrl("https://firebasestorage.googleapis.com/")
                .build()

        val behavior = NetworkBehavior.create()
        val mockRetrofit = MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior)
                .build()

        behavior.setDelay(0, TimeUnit.MILLISECONDS)

        val delegate = mockRetrofit.create(SkiResortListService::class.java)

        val skiResortListService = spyk(MockSkiResortListService(delegate))

        val liveDataDb = MutableLiveData<List<SkiResort>>()
        liveDataDb.postValue(listOf(SkiResort(1, "Val d'Isère", "France", "Alps", 300, 83, 96, true)))

        val liveDataService = MutableLiveData<List<com.alexandre.skiresort.service.model.SkiResort>>()
        liveDataService.postValue(listOf(com.alexandre.skiresort.service.model.SkiResort(1, "Val d'Isère", "France", "Alps", 300, 83, 96, "sunny")))

        every {
            skiResortDao.getAllSkiResorts()
        } returns liveDataDb

        every {
            skiResortDao.isFav(1)
        } returns true

        every {
            skiResortDao.insertAll(listOf(SkiResort(1, "Val d'Isère", "France", "Alps", 300, 83, 96, true)))
        } returns Unit

        val expetedRes = listOf(com.alexandre.skiresort.domain.model.SkiResort(1, "Val d'Isère", "France", "Alps", 300, 83, 96, true, R.drawable.ic_wb_sunny))

        SkiResortRepo(skiResortListService, skiResortDao, Executors.newSingleThreadExecutor()).getAllSkiResorts().observeForever {
            println("CALL")
            assertEquals(expetedRes, it)
        }
    }
}