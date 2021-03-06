package com.aptopayments.mobile.data.user

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import java.util.*

class BirthdateDataPointTest {

    val sut = BirthdateDataPoint(birthdate = LocalDate.of(2020, 4, 14))

    @Test
    fun `when toStringRepresentation then correct format is applied`() {
        assertEquals("April 14, 2020", sut.toStringRepresentation(Locale.US))
    }

    @Test
    fun `when type is requested then Birtdate is returned`() {
        assertEquals(DataPoint.Type.BIRTHDATE, sut.getType())
    }
}
