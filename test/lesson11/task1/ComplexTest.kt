package lesson11.task1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag

internal class ComplexTest {

    private fun assertApproxEquals(expected: Complex, actual: Complex, eps: Double) {
        assertEquals(expected.re, actual.re, eps)
        assertEquals(expected.im, actual.im, eps)
    }

    @Test
    @Tag("2")
    fun plus() {
        assertApproxEquals(Complex("2.5-2i"), Complex("-1.17+2i") + Complex("3.67-4i"), 1e-10)
        assertApproxEquals(Complex("-3+2.9i"), Complex("2.9i") + Complex("-3"), 1e-10)
        assertApproxEquals(Complex("0.0+3i"), Complex("-1i") + Complex("4i"), 1e-10)
        assertThrows(IllegalStateException::class.java) { Complex("67+i") + Complex("i") }
    }

    @Test
    @Tag("2")
    operator fun unaryMinus() {
        assertApproxEquals(Complex(-2.0, 1.0), -Complex(2.0, -1.0), 1e-10)
    }

    @Test
    @Tag("2")
    fun minus() {
        assertApproxEquals(Complex("1.89+6.22i"), Complex("-1.23+2.22i") - Complex("-3.12-4i"), 1e-10)
    }

    @Test
    @Tag("4")
    fun times() {
        assertApproxEquals(Complex("24.94+2.9i"), Complex("2.22-4.1i") * Complex("2+5i"), 1e-10)
    }

    @Test
    @Tag("4")
    fun div() {
        assertApproxEquals(Complex("2+5i"), Complex("24.94+2.9i") / Complex("2.22-4.1i"), 1e-10)
    }

    @Test
    @Tag("2")
    fun equals() {
        assertTrue(Complex(1.0, 2.0) == Complex("1+2i"))
        assertTrue(Complex(1.0, 1.0) != Complex(1.0))
        assertFalse(Complex(1.0, -2.0) == Complex("1+2i"))
        assertFalse(Complex(1.23, 2.2) != Complex("1.23+2.2i"))
    }
}