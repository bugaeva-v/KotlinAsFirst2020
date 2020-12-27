@file:Suppress("UNUSED_PARAMETER")

package lesson12.task1

import kotlin.math.abs

/**
 * Класс "табличная функция".
 *
 * Общая сложность задания -- средняя, общая ценность в баллах -- 16.
 * Объект класса хранит таблицу значений функции (y) от одного аргумента (x).
 * В таблицу можно добавлять и удалять пары (x, y),
 * найти в ней ближайшую пару (x, y) по заданному x,
 * найти (интерполяцией или экстраполяцией) значение y по заданному x.
 *
 * Класс должен иметь конструктор по умолчанию (без параметров).
 */
class TableFunction {
    /**
     * Количество пар в таблице
     */
    val size: Int
        get() = array.size

    private val array = mutableListOf<Pair<Double, Double>>()

    /**
     * Добавить новую пару.
     * Вернуть true, если пары с заданным x ещё нет,
     * или false, если она уже есть (в этом случае перезаписать значение y)
     */
    fun add(x: Double, y: Double): Boolean {
        val t = array.find { it.first == x }
        return if (t == null) {
            array.add(Pair(x, y))
            true
        } else {
            array.remove(t)
            array.add(Pair(x, y))
            false
        }
    }

    /**
     * Удалить пару с заданным значением x.
     * Вернуть true, если пара была удалена.
     */
    fun remove(x: Double): Boolean {
        val t = array.find { it.first == x }
        return if (t == null) false
        else {
            array.remove(t)
            true
        }
    }

    /**
     * Вернуть коллекцию из всех пар в таблице
     */
    fun getPairs(): Collection<Pair<Double, Double>> = array

    /**
     * Вернуть пару, ближайшую к заданному x.
     * Если существует две ближайшие пары, вернуть пару с меньшим значением x.
     * Если таблица пуста, бросить IllegalStateException.
     */
    fun findPair(x: Double): Pair<Double, Double> {
        if (array.isEmpty()) throw IllegalStateException()
        var min = Pair(Double.POSITIVE_INFINITY, 0.0)
        for (i in array) {
            if (abs(x - i.first) == abs(x - min.first) && min.first > i.first)
                min = i
            else if (abs(x - i.first) < abs(x - min.first))
                min = i
        }
        return min
    }

    /**
     * Вернуть значение y по заданному x.
     * Если в таблице есть пара с заданным x, взять значение y из неё.
     * Если в таблице есть всего одна пара, взять значение y из неё.
     * Если таблица пуста, бросить IllegalStateException.
     * Если существуют две пары, такие, что x1 < x < x2, использовать интерполяцию.
     * Если их нет, но существуют две пары, такие, что x1 < x2 < x или x > x2 > x1, использовать экстраполяцию.
     */
    fun getValue(x: Double): Double {
        if (size == 0) throw  IllegalStateException()
        if (size == 1) return array.first().second
        var diffLeft = Double.POSITIVE_INFINITY
        var diffRight = Double.POSITIVE_INFINITY
        var left = Pair(Double.NEGATIVE_INFINITY, 0.0)
        var right = Pair(Double.POSITIVE_INFINITY, 0.0)
        for (i in array) {
            if (x == i.first)
                return i.second
            if (i.first < x && x - i.first < diffLeft) {
                diffLeft = x - i.first
                left = i
            } else if (i.first > x && i.first - x < diffRight) {
                diffRight = i.first - x
                right = i
            }
        }
        if (diffLeft != Double.POSITIVE_INFINITY && diffRight != Double.POSITIVE_INFINITY)
            return left.second + (right.second - left.second) * (x - left.first) / (right.first - left.first)
        else if (diffLeft == Double.POSITIVE_INFINITY) {
            var diff = Double.POSITIVE_INFINITY
            var right2 = Pair(0.0, 0.0)
            for (i in array)
                if (i.first > right.first && i.first - right.first < diff) {
                    diff = i.first - right.first
                    right2 = i
                }
            return right.second + (right2.second - right.second) * (x - right.first) / (right2.first - right.first)
        } else {
            var diff = Double.POSITIVE_INFINITY
            var left2 = Pair(0.0, 0.0)
            for (i in array)
                if (i.first < left.first && left.first - i.first < diff) {
                    diff = left.first - i.first
                    left2 = i
                }
            return left2.second + (left.second - left2.second) * (x - left2.first) / (left.first - left2.first)
        }
    }

    /**
     * Таблицы равны, если в них одинаковое количество пар,
     * и любая пара из второй таблицы входит также и в первую
     */
    override fun equals(other: Any?): Boolean =
        other is TableFunction && size == other.size && array.all { it in other.array }

    override fun hashCode(): Int = array.toSet().hashCode()

}