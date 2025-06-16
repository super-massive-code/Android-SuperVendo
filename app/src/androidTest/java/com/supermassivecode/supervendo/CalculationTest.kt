package com.supermassivecode.supervendo

import androidx.test.platform.app.InstrumentationRegistry
import com.supermassivecode.supervendo.data.analysis.JSONParser
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Test


class CalculationTest {

    @Test
    fun `test can parse file`() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val result = JSONParser.convertToDays(appContext, "earnings_sim.json")
        assertNotNull(result)
        assertTrue(result.isNotEmpty())
    }
}
