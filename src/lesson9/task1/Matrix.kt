@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson9.task1

import java.lang.IndexOutOfBoundsException

// Урок 9: проектирование классов
// Максимальное количество баллов = 40 (без очень трудных задач = 15)

/**
 * Ячейка матрицы: row = ряд, column = колонка
 */
data class Cell(val row: Int, val column: Int) {
    override fun equals(other: Any?) = other is Cell && row == other.row && column == other.column
}

/**
 * Интерфейс, описывающий возможности матрицы. E = тип элемента матрицы
 */
interface Matrix<E> {
    /** Высота */
    val height: Int

    /** Ширина */
    val width: Int

    /**
     * Доступ к ячейке.
     * Методы могут бросить исключение, если ячейка не существует или пуста
     */
    operator fun get(row: Int, column: Int): E

    operator fun get(cell: Cell): E

    /**
     * Запись в ячейку.
     * Методы могут бросить исключение, если ячейка не существует
     */
    operator fun set(row: Int, column: Int, value: E)

    operator fun set(cell: Cell, value: E)

    fun swap(a: Cell, b: Cell) {
        val t = this[a]
        this[a] = this[b]
        this[b] = t
    }

    fun clone(): Matrix<E> {
        val m = MatrixImpl(height, width, this[0, 0])
        for (i in 0 until height)
            for (j in 0 until width)
                m[i, j] = this[i, j]
        return m
    }
}

/**
 * Простая (2 балла)
 *
 * Метод для создания матрицы, должен вернуть РЕАЛИЗАЦИЮ Matrix<E>.
 * height = высота, width = ширина, e = чем заполнить элементы.
 * Бросить исключение IllegalArgumentException, если height или width <= 0.
 */
fun <E> createMatrix(height: Int, width: Int, e: E): Matrix<E> = MatrixImpl(height, width, e)

/**
 * Средняя сложность (считается двумя задачами в 3 балла каждая)
 *
 * Реализация интерфейса "матрица"
 */
class MatrixImpl<E>(override val height: Int, override val width: Int, e: E) : Matrix<E> {
    private val list = mutableListOf<E>()

    init {
        for (i in 0 until height * width) {
            list.add(e)
        }
    }

    override fun get(row: Int, column: Int): E = list[row * width + column]

    override fun get(cell: Cell): E = get(cell.row, cell.column)

    override fun set(row: Int, column: Int, value: E) {
        list[row * width + column] = value
    }

    override fun set(cell: Cell, value: E) {
        set(cell.row, cell.column, value)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Matrix<*> || height != other.height && width != other.width)
            return false
        for (i in 0 until height * width)
            if (list[i] != other[i / width, i % width]) return false
        return true
    }

    override fun toString(): String {
        val str = StringBuilder()
        str.append("[")
        for (i in 0 until height) {
            if (i != 0) str.append(" ")
            str.append("[")
            for (j in 0 until width) {
                str.append(list[i * width + j])
                if (j != width - 1) str.append(" ,")
            }
            str.append("]")
            if (i != height - 1) str.append(" ,\n")
        }
        str.append("]")
        return str.toString()
    }

    override fun hashCode(): Int {
        var result = height
        result = 31 * result + width
        result = 31 * result + list.hashCode()
        return result
    }

}

