package com.alexandre.skiresort

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.*
import com.alexandre.skiresort.data.SkiResortRepo
import com.alexandre.skiresort.db.SkiResortDao
import com.alexandre.skiresort.db.model.SkiResort
import com.alexandre.skiresort.service.SkiResortListService
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.concurrent.Executors
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import retrofit2.Retrofit
import retrofit2.mock.BehaviorDelegate
import java.util.concurrent.TimeUnit


class SkiResortRepoTest {

    private val skiResortDao = mockkClass(SkiResortDao::class)
    private val liveDataDb = MutableLiveData<List<SkiResort>>()
    private val skiResortListService = spyk(MockSkiResortListService(createRetrofitDelegate()))

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun prepareDao() {
        every {
            skiResortDao.getAllSkiResorts()
        } returns liveDataDb

        every {
            skiResortDao.isFav(1)
        } returns true

        every {
            skiResortDao.insertAll(createExpectedDbData())
        } returns Unit
    }

    private fun createRetrofitDelegate(): BehaviorDelegate<SkiResortListService> {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://firebasestorage.googleapis.com/")
                .build()

        val behavior = NetworkBehavior.create()
        val mockRetrofit = MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior)
                .build()

        behavior.setDelay(0, TimeUnit.MILLISECONDS)

        return mockRetrofit.create(SkiResortListService::class.java)
    }

    @Test
    fun testMediatorLiveData() {
        liveDataDb.postValue(createExpectedDbData())

        SkiResortRepo(skiResortListService, skiResortDao, Executors.newSingleThreadExecutor()).getAllSkiResorts().observeForever {
            assertEquals(createExpectedResult(), it)
        }
    }

    private fun createExpectedResult() =
            listOf(com.alexandre.skiresort.domain.model.SkiResort(
                    1,
                    "Val d'Isère",
                    "France",
                    "Alps",
                    300,
                    83,
                    96,
                    true,
                    R.drawable.ic_wb_sunny))

    private fun createExpectedDbData() =
            listOf(SkiResort(
                    1,
                    "Val d'Isère",
                    "France",
                    "Alps",
                    300,
                    83,
                    96,
                    true))
}