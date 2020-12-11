package lesson11.task1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import java.lang.ArithmeticException

internal class UnsignedBigIntegerTest {

    @Test
    @Tag("8")
    fun plus() {
        assertEquals(UnsignedBigInteger("0"), UnsignedBigInteger(0) + UnsignedBigInteger(0))
        assertEquals(UnsignedBigInteger("1"), UnsignedBigInteger("0") + UnsignedBigInteger(1))
        assertEquals(UnsignedBigInteger(4), UnsignedBigInteger(2) + UnsignedBigInteger(2))
        assertEquals(UnsignedBigInteger("9087654330"), UnsignedBigInteger("9087654329") + UnsignedBigInteger(1))
        assertEquals(UnsignedBigInteger("12300", true), UnsignedBigInteger(22) + UnsignedBigInteger("299"))
        assertEquals(UnsignedBigInteger("012300"), UnsignedBigInteger(12201) + UnsignedBigInteger("99"))
    }

    @Test
    @Tag("8")
    fun minus() {
        assertEquals(UnsignedBigInteger("0"), UnsignedBigInteger(0) - UnsignedBigInteger(0))
        assertEquals(UnsignedBigInteger(2), UnsignedBigInteger(4) - UnsignedBigInteger(2))
        assertEquals(UnsignedBigInteger("9087654329"), UnsignedBigInteger("9087654330") - UnsignedBigInteger(1))
        assertEquals(
            UnsignedBigInteger("1111111111111111111111"),
            UnsignedBigInteger("1111111111111111122222") - UnsignedBigInteger(11111)
        )
        assertEquals(
            UnsignedBigInteger("1111111111111111108889"),
            UnsignedBigInteger("1111111111111111111111") - UnsignedBigInteger(2222)
        )
        assertEquals(UnsignedBigInteger("9087654329"), UnsignedBigInteger("9087654330") - UnsignedBigInteger(1))
        assertThrows(ArithmeticException::class.java) {
            UnsignedBigInteger(2) - UnsignedBigInteger(4)
        }
        assertThrows(ArithmeticException::class.java) {
            UnsignedBigInteger(578) - UnsignedBigInteger(1000)
        }
    }

    @Test
    @Tag("12")
    fun times() {

        assertEquals(UnsignedBigInteger(0), UnsignedBigInteger(125) * UnsignedBigInteger(0))
        assertEquals(UnsignedBigInteger(625), UnsignedBigInteger(125) * UnsignedBigInteger(5))
        assertEquals(UnsignedBigInteger(450), UnsignedBigInteger(10) * UnsignedBigInteger(45))
        assertEquals(
            UnsignedBigInteger("184464550395904"),
            UnsignedBigInteger("4294967296") * UnsignedBigInteger("42949")
        )
    }

    @Test
    @Tag("16")
    fun div() {
        assertEquals(UnsignedBigInteger(0), UnsignedBigInteger(0) / UnsignedBigInteger(1))
        assertEquals(UnsignedBigInteger(1), UnsignedBigInteger(1) / UnsignedBigInteger(1))
        assertEquals(UnsignedBigInteger(2), UnsignedBigInteger(4) / UnsignedBigInteger(2))
        assertEquals(
            UnsignedBigInteger("4"),
            UnsignedBigInteger("98") / UnsignedBigInteger("23")
        )
        assertEquals(
            UnsignedBigInteger("14"),
            UnsignedBigInteger("128") / UnsignedBigInteger("9")
        )
        assertEquals(
            UnsignedBigInteger("4294967296"),
            UnsignedBigInteger("184464550395904") / UnsignedBigInteger("42949")
        )
        assertThrows(ArithmeticException::class.java) {
            UnsignedBigInteger(578) / UnsignedBigInteger(0)
        }
    }

    @Test
    @Tag("16")
    fun rem() {
        assertEquals(UnsignedBigInteger(5), UnsignedBigInteger(19) % UnsignedBigInteger(7))
        assertEquals(
            UnsignedBigInteger(0),
            UnsignedBigInteger("18446744073709551616") % UnsignedBigInteger("4294967296")
        )
        assertThrows(ArithmeticException::class.java) {
            UnsignedBigInteger(578) % UnsignedBigInteger(0)
        }
    }

    @Test
    @Tag("8")
    fun equals() {
        assertTrue(UnsignedBigInteger(123456789) == UnsignedBigInteger("123456789"))
        assertFalse(UnsignedBigInteger(0) != UnsignedBigInteger("0"))
        assertFalse(UnsignedBigInteger(456) == UnsignedBigInteger(457))
        assertTrue(UnsignedBigInteger(4567) != UnsignedBigInteger(4566))
    }

    @Test
    @Tag("8")
    fun compareTo() {
        assertTrue(UnsignedBigInteger(123456789) < UnsignedBigInteger("9876543210"))
        assertTrue(UnsignedBigInteger("987654321") > UnsignedBigInteger(123456789))
        assertFalse(UnsignedBigInteger(0) > UnsignedBigInteger("0"))
        assertFalse(UnsignedBigInteger("255") < UnsignedBigInteger(254))
    }

    @Test
    @Tag("8")
    fun toInt() {
        assertEquals(0, UnsignedBigInteger("0").toInt())
        assertEquals(123, UnsignedBigInteger(123).toInt())
        assertEquals(123456789, UnsignedBigInteger("123456789").toInt())
    }
}