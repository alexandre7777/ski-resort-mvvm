package com.alexandre.skiresort

import com.alexandre.skiresort.domain.model.getDrawableForString
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SkiResortConvertorTest {

    @Test
    fun getDrawableForString_sunny() {
        assertEquals(R.drawable.ic_wb_sunny, getDrawableForString("sunny"))
    }

    @Test
    fun getDrawableForString_cloudy() {
        assertEquals(R.drawable.ic_wb_cloudy, getDrawableForString("cloudy"))
    }

    @Test
    fun getDrawableForString_snow() {
        assertEquals(R.drawable.ic_ac_unit, getDrawableForString("snow"))
    }

    @Test
    fun getDrawableForString_rain() {
        assertEquals(R.drawable.ic_grain, getDrawableForString("rain"))
    }

    @Test
    fun getDrawableForString_unknown() {
        assertEquals(null, getDrawableForString(""))
    }
}
