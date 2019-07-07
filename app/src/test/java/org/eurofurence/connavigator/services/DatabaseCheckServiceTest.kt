package org.eurofurence.connavigator.services

import org.junit.Assert.assertEquals
import org.junit.Test

class DatabaseCheckServiceTest {

    @Test
    fun isSameMinorVersion_match() {
        val old = "1.0.0"
        val new = "1.0.1"

        val result = DatabaseCheckService.isSameMinorVersion(old, new)

        assertEquals(true, result)
    }

    @Test
    fun isSameMinorVersion_mismatch() {
        val old = "1.0.0"
        val new = "1.1.0"

        val result = DatabaseCheckService.isSameMinorVersion(old, new)

        assertEquals(false, result)

    }

    @Test
    fun isSameMinorVersion_nonsense() {
        val old = "2.0.0"
        val new = "hahathatsnothappening"

        val result = DatabaseCheckService.isSameMinorVersion(old, new)

        assertEquals(false, result)
    }
}