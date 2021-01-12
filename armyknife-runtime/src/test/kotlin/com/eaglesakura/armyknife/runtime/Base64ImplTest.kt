package com.eaglesakura.armyknife.runtime

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class Base64ImplTest {

    @Test
    fun encodeAndDecode() {
        // test data
        Base64Impl.byteArrayToString(byteArrayOf(0x01, 0x02)).also { base64 ->
            assertNotEquals("", base64)
            Base64Impl.stringToByteArray(base64).also { bytes ->
                assertEquals(2, bytes.size)
                assertEquals(0x01.toByte(), bytes[0])
                assertEquals(0x02.toByte(), bytes[1])
            }
        }

        // zero len
        Base64Impl.byteArrayToString(byteArrayOf()).also { base64 ->
            Base64Impl.stringToByteArray(base64).also { bytes ->
                assertEquals(0, bytes.size)
            }
        }
    }
}
