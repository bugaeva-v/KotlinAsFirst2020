@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import java.lang.Integer.max

/**
 * Класс "комплексное число".
 *
 * Общая сложность задания -- лёгкая, общая ценность в баллах -- 8.
 * Объект класса -- комплексное число вида x+yi.
 * Про принципы работы с комплексными числами см. статью Википедии "Комплексное число".
 *
 * Аргументы конструктора -- вещественная и мнимая часть числа.
 */
class Complex(var re: Double, var im: Double) {

    /**
     * Конструктор из вещественного числа
     */
    constructor(x: Double) : this(x, 0.0)

    /**
     * Сложение.
     */
    operator fun plus(other: Complex): Complex = Complex(re + other.re, im + other.im)

    /**
     * Смена знака (у обеих частей числа)
     */
    operator fun unaryMinus(): Complex = Complex(-re, -im)

    /**
     * Вычитание
     */
    operator fun minus(other: Complex): Complex = Complex(re - other.re, im - other.im)

    /**
     * Умножение
     */
    operator fun times(other: Complex): Complex = Complex(re * other.re - im * other.im, re * other.im + im * other.re)

    /**
     * Деление
     */
    operator fun div(other: Complex): Complex = Complex(
        (re * other.re + im * other.im) / (other.re * other.re + other.im * other.im),
        (im * other.re - re * other.im) / (other.re * other.re + other.im * other.im)
    )

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean = other is Complex && re == other.re && im == other.im

    /**
     * Преобразование в строку
     */
    override fun toString(): String = "$re${im}i"

    override fun hashCode(): Int {
        var result = re.hashCode()
        result = 31 * result + im.hashCode()
        return result
    }
}

/**
 * Имитация конструктора из строки вида x+yi
 */
fun Complex(s: String): Complex {
    var re = 0.0
    var im = 0.0
    when {
        s.matches(Regex("""^-?\d+(\.\d+)*$""")) ->
            re = s.toDouble()
        s.matches(Regex("""^-?\d+(\.\d+)*i$""")) ->
            im = s.substring(0 until s.lastIndex).toDouble()
        s.matches(Regex("""^-?\d+(\.\d+)*[+-]\d+(\.\d+)*i$""")) -> {
            val i = max(s.indexOf('+', 1), s.indexOf('-', 1))
            var str = s.substring(0 until i)
            re = str.toDouble()
            str = s.substring(i until s.lastIndex)
            im = str.toDouble()
        }
        else -> throw IllegalStateException("illegal notation")
    }
    return Complex(re, im)
}
