@file:Suppress("UNUSED_PARAMETER")

package lesson12.task1

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
    private var start: Point? = null

    /**
     * Количество пар в таблице
     */
    val size: Int
        get() {
            var current = start
            var result = 0
            while (current != null) {
                current = current.next
                result++
            }
            return result
        }

    private class Point(val x: Double, var y: Double, var next: Point? = null)

    /**
     * Добавить новую пару.
     * Вернуть true, если пары с заданным x ещё нет,
     * или false, если она уже есть (в этом случае перезаписать значение y)
     */
    fun add(x: Double, y: Double): Boolean {
        if (start == null) start = Point(x, y)
        else {
            var current = start
            var prev: Point? = null
            while (current?.next != null && current.x < x) {
                prev = current
                current = current.next
            }
            when {
                current!!.next == null -> {
                    if (current.x == x) {
                        current.y = y
                        return false
                    } else if (current.x > x) {
                        if (prev == null)
                            start = Point(x, y, current)
                        else
                            prev.next = Point(x, y, current)
                    } else
                        current.next = Point(x, y)
                }
                current.x == x -> {
                    current.y = y
                    return false
                }
                else -> {
                    if (prev == null)
                        start = Point(x, y, current)
                    else
                        prev.next = Point(x, y, current)
                }
            }
        }
        return true
    }

    /**
     * Удалить пару с заданным значением x.
     * Вернуть true, если пара была удалена.
     */
    fun remove(x: Double): Boolean {
        if (start == null) return false
        else if (start!!.x == x) {
            start = start?.next
            return true
        }
        var current = start
        var prev = start
        while (current != null && current.x < x) {
            prev = current
            current = current.next
        }
        if (current == null || current.x > x) return false
        prev!!.next = current.next
        return true
    }

    /**
     * Вернуть коллекцию из всех пар в таблице
     */
    fun getPairs(): Collection<Pair<Double, Double>> {
        val result = mutableListOf<Pair<Double, Double>>()
        var current = start
        while (current != null) {
            result.add(Pair(current.x, current.y))
            current = current.next
        }
        return result
    }

    /**
     * Вернуть пару, ближайшую к заданному x.
     * Если существует две ближайшие пары, вернуть пару с меньшим значением x.
     * Если таблица пуста, бросить IllegalStateException.
     */
    fun findPair(x: Double): Pair<Double, Double> {
        if (start == null) throw IllegalStateException()
        var current = start!!
        while (current.next != null && (current.next)!!.x < x)
            current = current.next!!
        return Pair(current.x, current.y)
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
        if (start == null) throw  IllegalStateException()
        var current = start!!
        var prev: Point? = null
        while (current.next != null && current.x < x) {
            prev = current
            current = current.next!!
        }
        return when {
            current.x == x -> current.y
            size == 1 -> current.y
            current.x < x && current.next == null ->
                prev!!.y + (x - prev.x) * (current.y - prev.y) / (current.x - prev.x)
            current.x == start!!.x ->
                start!!.y + (x - start!!.x) * (start!!.next!!.y - start!!.y) / (start!!.next!!.x - start!!.x)
            else ->
                current.y + (x - current.x) * (current.y - prev!!.y) / (current.x - prev.x)
        }
    }

    /**
     * Таблицы равны, если в них одинаковое количество пар,
     * и любая пара из второй таблицы входит также и в первую
     */
    override fun equals(other: Any?): Boolean {
        if (other is TableFunction && size == other.size) {
            var currentThis = start
            var currentOther = other.start
            while (currentThis != null) {
                if (currentThis.x != currentOther!!.x || currentThis.y != currentOther.y)
                    return false
                currentThis = currentThis.next
                currentOther = currentOther.next
            }
            return true
        } else return false
    }

    override fun hashCode(): Int = start?.hashCode() ?: 0
}