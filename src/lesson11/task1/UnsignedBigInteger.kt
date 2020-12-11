package lesson11.task1

import lesson3.task1.digitNumber

/**
 * Класс "беззнаковое большое целое число".
 *
 * Общая сложность задания -- очень сложная, общая ценность в баллах -- 32.
 * Объект класса содержит целое число без знака произвольного размера
 * и поддерживает основные операции над такими числами, а именно:
 * сложение, вычитание (при вычитании большего числа из меньшего бросается исключение),
 * умножение, деление, остаток от деления,
 * преобразование в строку/из строки, преобразование в целое/из целого,
 * сравнение на равенство и неравенство
 */
class UnsignedBigInteger : Comparable<UnsignedBigInteger> {
    var array = ByteArray(0)

    /**
     * Конструктор из строки
     */
    constructor(s: String, reverse: Boolean = false) {
        if (!s.matches(Regex("""\d+"""))) throw IllegalStateException("negative unsigned number")
        if (s.matches(Regex("""0*"""))) {
            array = byteArrayOf(0)
            return
        }
        var str = s + ""
        if (str.length > 1) {
            val mach = if (reverse) Regex("""((?<=[1-9])0+)$""").find(str)
            else Regex("""^(0+(?=[1-9]))""").find(str)
            if (mach != null) {
                str = str.removeRange(mach.range)
            }
        }
        array = ByteArray(str.length) { 0 }
        var j = str.lastIndex
        for (i in 0..str.lastIndex)
            array[j--] = str[i].toString().toByte()
        if (reverse) array = array.reversedArray()
    }

    /**
     * Конструктор из целого
     */
    constructor(i: Int) {
        if (i < 0) throw IllegalStateException("negative unsigned number")
        array = ByteArray(digitNumber(i)) { 0 }
        var int = i
        var j = 0
        while (int != 0) {
            array[j++] = (int % 10).toByte()
            int /= 10
        }
    }

    /**
     * Сложение
     */
    operator fun plus(other: UnsignedBigInteger): UnsignedBigInteger {
        var str = ""
        var term = 0
        var i = 0
        while (i <= if (array.size >= other.array.size) other.array.lastIndex else array.lastIndex) {
            str += ((array[i] + other.array[i] + term) % 10).toString()
            term = (array[i] + other.array[i] + term) / 10
            i++
        }
        if (array.lastIndex == other.array.lastIndex) {
            if (term != 0)
                str += term.toByte()
        } else if (array.lastIndex == i - 1)
            while (i < other.array.size) {
                str += ((term + other.array[i]) % 10).toString()
                term = (other.array[i] + term) / 10
                i++
            }
        else
            while (i < array.size) {
                str += ((term + array[i]) % 10).toString()
                term = (array[i] + term) / 10
                i++
            }
        return UnsignedBigInteger(str, true)
    }

    /**
     * Вычитание (бросить ArithmeticException, если this < other)
     */
    operator fun minus(other: UnsignedBigInteger): UnsignedBigInteger {
        if (other > this) throw ArithmeticException("$this - $other")
        var str = ""
        val minuend = this.array
        var i = 0
        while (i <= other.array.lastIndex) {
            if (minuend[i] < other.array[i]) {
                var j = i + 1
                while (minuend[j] == 0.toByte())
                    minuend[j++] = 9
                minuend[j]--
                str += (minuend[i] + 10 - other.array[i]).toString()
            } else
                str += (minuend[i] - other.array[i]).toString()
            i++
        }
        while (i <= minuend.lastIndex)
            str += minuend[i++].toString()
        return UnsignedBigInteger(str, true)
    }

    /**
     * Умножение
     */
    operator fun times(other: UnsignedBigInteger): UnsignedBigInteger {
        var annex = 0
        var r = UnsignedBigInteger(0)
        for (i in 0..other.array.lastIndex) {
            var str = ""
            for (j in 0..array.lastIndex) {
                str += "0".repeat(j + i) + ((other.array[i] * array[j] + annex) % 10).toString()
                r += UnsignedBigInteger(str, true)
                annex = (other.array[i] * array[j] + annex) / 10
                str = ""
            }
            if (annex != 0)
                str += "0".repeat(array.size + i) + annex.toString()
            if (str.isNotEmpty())
                r += UnsignedBigInteger(str, true)
            annex = 0
        }
        return r
    }

    /**
     * Деление
     */
    operator fun div(other: UnsignedBigInteger): UnsignedBigInteger {
        if (other == UnsignedBigInteger(0)) throw ArithmeticException("/ 0")
        if (this < other) return UnsignedBigInteger(0)
        var localDividend = UnsignedBigInteger(0)
        var localQuotient = 0
        var str = ""
        var i = this.array.lastIndex
        val ten = UnsignedBigInteger(10)
        while (localDividend < other)
            localDividend = localDividend * ten + UnsignedBigInteger(array[i--].toInt())
        while (i >= 0) {
            while (other * UnsignedBigInteger(localQuotient) < localDividend)
                localQuotient++
            str += (--localQuotient).toString()
            localDividend = (localDividend - other * UnsignedBigInteger(localQuotient)) * ten +
                    UnsignedBigInteger(array[i--].toInt())
            localQuotient = 0
        }
        while (other * UnsignedBigInteger(localQuotient) <= localDividend)
            localQuotient++
        str += (--localQuotient).toString()
        return UnsignedBigInteger(str)
    }

    /**
     * Взятие остатка
     */
    operator fun rem(other: UnsignedBigInteger): UnsignedBigInteger {
        if (other == UnsignedBigInteger(0)) throw ArithmeticException("% 0")
        if (this < other) return this
        return this - (this / other) * other
    }

    /**
     * Сравнение на равенство (по контракту Any.equals)
     */
    override fun equals(other: Any?): Boolean {
        if (other !is UnsignedBigInteger || array.size != other.array.size) return false
        for (i in 0..array.lastIndex)
            if (array[i] != other.array[i])
                return false
        return true
    }

    /**
     * Сравнение на больше/меньше (по контракту Comparable.compareTo)
     */
    override fun compareTo(other: UnsignedBigInteger): Int {
        if (array.size > other.array.size) return 1
        if (array.size < other.array.size) return -1
        for (i in array.lastIndex downTo 0) {
            if (array[i] > other.array[i]) return 1
            if (array[i] < other.array[i]) return -1
        }
        return 0
    }

    /**
     * Преобразование в строку
     */
    override fun toString(): String {
        var str = ""
        for (i in array.lastIndex downTo 0)
            str += array[i].toString()
        return str
    }

    /**
     * Преобразование в целое
     * Если число не влезает в диапазон Int, бросить ArithmeticException
     */
    fun toInt(): Int {
        if (array.size > digitNumber(Int.MAX_VALUE) ||
            array.size == digitNumber(Int.MAX_VALUE) && this > UnsignedBigInteger(Int.MAX_VALUE)
        ) throw ArithmeticException()
        var j = 1
        var res = 0
        for (i in array) {
            res += i * j
            j *= 10
        }
        return res
    }

    override fun hashCode(): Int = array.contentHashCode()
}