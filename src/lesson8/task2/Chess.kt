@file:Suppress("UNUSED_PARAMETER")

package lesson8.task2

import kotlin.math.abs
import java.util.*
import kotlin.math.max


/**
 * Клетка шахматной доски. Шахматная доска квадратная и имеет 8 х 8 клеток.
 * Поэтому, обе координаты клетки (горизонталь row, вертикаль column) могут находиться в пределах от 1 до 8.
 * Горизонтали нумеруются снизу вверх, вертикали слева направо.
 */
data class Square(val column: Int, val row: Int) {
    /**
     * Пример
     *
     * Возвращает true, если клетка находится в пределах доски
     */
    fun inside(): Boolean = column in 1..8 && row in 1..8

    /**
     * Простая (2 балла)
     *
     * Возвращает строковую нотацию для клетки.
     * В нотации, колонки обозначаются латинскими буквами от a до h, а ряды -- цифрами от 1 до 8.
     * Для клетки не в пределах доски вернуть пустую строку
     */
    fun notation(): String =
        if (!inside()) ""
        else (column + 96).toChar() + row.toString()

}


/**
 * Простая (2 балла)
 *
 * Создаёт клетку по строковой нотации.
 * В нотации, колонки обозначаются латинскими буквами от a до h, а ряды -- цифрами от 1 до 8.
 * Если нотация некорректна, бросить IllegalArgumentException
 */
fun square(notation: String): Square =
    if (!notation.matches(Regex("""^[a-h][1-8]$"""))) throw IllegalArgumentException()
    else Square(notation[0].toInt() - 96, notation[1].toInt() - 48)


/**
 * Простая (2 балла)
 *
 * Определить число ходов, за которое шахматная ладья пройдёт из клетки start в клетку end.
 * Шахматная ладья может за один ход переместиться на любую другую клетку
 * по вертикали или горизонтали.
 * Ниже точками выделены возможные ходы ладьи, а крестиками -- невозможные:
 *
 * xx.xxххх
 * xх.хxххх
 * ..Л.....
 * xх.хxххх
 * xx.xxххх
 * xx.xxххх
 * xx.xxххх
 * xx.xxххх
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: rookMoveNumber(Square(3, 1), Square(6, 3)) = 2
 * Ладья может пройти через клетку (3, 3) или через клетку (6, 1) к клетке (6, 3).
 */
fun rookMoveNumber(start: Square, end: Square): Int = when {
    !start.inside() || !end.inside() -> throw IllegalArgumentException("Square is not inside.")
    start == end -> 0
    start.column == end.column || start.row == end.row -> 1
    else -> 2
}

/**
 * Средняя (3 балла)
 *
 * Вернуть список из клеток, по которым шахматная ладья может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов ладьи см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры: rookTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          (здесь возможен ещё один вариант)
 *          rookTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(3, 3), Square(6, 3))
 *          (здесь возможен единственный вариант)
 *          rookTrajectory(Square(3, 5), Square(8, 5)) = listOf(Square(3, 5), Square(8, 5))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun rookTrajectory(start: Square, end: Square): List<Square> = when (rookMoveNumber(start, end)) {
    0 -> listOf(start)
    1 -> listOf(start, end)
    else -> listOf(start, Square(start.column, end.row), end)
}

/**
 * Простая (2 балла)
 *
 * Определить число ходов, за которое шахматный слон пройдёт из клетки start в клетку end.
 * Шахматный слон может за один ход переместиться на любую другую клетку по диагонали.
 * Ниже точками выделены возможные ходы слона, а крестиками -- невозможные:
 *
 * .xxx.ххх
 * x.x.xххх
 * xxСxxxxx
 * x.x.xххх
 * .xxx.ххх
 * xxxxx.хх
 * xxxxxх.х
 * xxxxxхх.
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если клетка end недостижима для слона, вернуть -1.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Примеры: bishopMoveNumber(Square(3, 1), Square(6, 3)) = -1; bishopMoveNumber(Square(3, 1), Square(3, 7)) = 2.
 * Слон может пройти через клетку (6, 4) к клетке (3, 7).
 */
fun bishopMoveNumber(start: Square, end: Square): Int = when {
    !start.inside() || !end.inside() -> throw IllegalArgumentException()
    start == end -> 0
    (start.column % 2 == end.column % 2) xor (start.row % 2 == end.row % 2) -> -1
    abs(start.column - end.column) == abs(start.row - end.row) -> 1
    else -> 2
}

/**
 * Сложная (5 баллов)
 *
 * Вернуть список из клеток, по которым шахматный слон может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов слона см. предыдущую задачу.
 *
 * Если клетка end недостижима для слона, вернуть пустой список.
 *
 * Если клетка достижима:
 * - список всегда включает в себя клетку start
 * - клетка end включается, если она не совпадает со start.
 * - между ними должны находиться промежуточные клетки, по порядку от start до end.
 *
 * Примеры: bishopTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          bishopTrajectory(Square(3, 1), Square(3, 7)) = listOf(Square(3, 1), Square(6, 4), Square(3, 7))
 *          bishopTrajectory(Square(1, 3), Square(6, 8)) = listOf(Square(1, 3), Square(6, 8))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun bishopTrajectory(start: Square, end: Square): List<Square> = when (bishopMoveNumber(start, end)) {
    -1 -> listOf()
    0 -> listOf(start)
    1 -> listOf(start, end)
    else -> {
        val column = start.column + (end.column + end.row - start.column - start.row) / 2
        val row = start.row + (end.column + end.row - start.column - start.row) / 2
        listOf(
            start,
            if (column <= 8 && row <= 8) Square(column, row)
            else Square(
                end.column - abs(end.column + end.row - start.column - start.row) / 2,
                end.row - abs(end.column + end.row - start.column - start.row) / 2
            ),
            end
        )
    }
}

/**
 * Средняя (3 балла)
 *
 * Определить число ходов, за которое шахматный король пройдёт из клетки start в клетку end.
 * Шахматный король одним ходом может переместиться из клетки, в которой стоит,
 * на любую соседнюю по вертикали, горизонтали или диагонали.
 * Ниже точками выделены возможные ходы короля, а крестиками -- невозможные:
 *
 * xxxxx
 * x...x
 * x.K.x
 * x...x
 * xxxxx
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: kingMoveNumber(Square(3, 1), Square(6, 3)) = 3.
 * Король может последовательно пройти через клетки (4, 2) и (5, 2) к клетке (6, 3).
 */
fun kingMoveNumber(start: Square, end: Square): Int =
    if (!start.inside() || !end.inside()) throw IllegalArgumentException()
    else max(abs(start.row - end.row), abs(start.column - end.column))

/**
 * Сложная (5 баллов)
 *
 * Вернуть список из клеток, по которым шахматный король может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов короля см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры: kingTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          (здесь возможны другие варианты)
 *          kingTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(4, 2), Square(5, 2), Square(6, 3))
 *          (здесь возможен единственный вариант)
 *          kingTrajectory(Square(3, 5), Square(6, 2)) = listOf(Square(3, 5), Square(4, 4), Square(5, 3), Square(6, 2))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun kingTrajectory(start: Square, end: Square): List<Square> {
    var column = start.column
    var row = start.row
    val answer = mutableListOf(Square(column, row))
    var r = end.row.compareTo(start.row)
    var c = end.column.compareTo(start.column)
    while (column != end.column && row != end.row) {
        row += r
        column += c
        answer.add(Square(column, row))
    }
    if (row == end.row) r = 0
    if (column == end.column) c = 0
    while (row != end.row || column != end.column) {
        row += r
        column += c
        answer.add(Square(column, row))
    }
    return answer
}

/**
 * Сложная (6 баллов)
 *
 * Определить число ходов, за которое шахматный конь пройдёт из клетки start в клетку end.
 * Шахматный конь одним ходом вначале передвигается ровно на 2 клетки по горизонтали или вертикали,
 * а затем ещё на 1 клетку под прямым углом, образуя букву "Г".
 * Ниже точками выделены возможные ходы коня, а крестиками -- невозможные:
 *
 * .xxx.xxx
 * xxKxxxxx
 * .xxx.xxx
 * x.x.xxxx
 * xxxxxxxx
 * xxxxxxxx
 * xxxxxxxx
 * xxxxxxxx
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: knightMoveNumber(Square(3, 1), Square(6, 3)) = 3.
 * Конь может последовательно пройти через клетки (5, 2) и (4, 4) к клетке (6, 3).
 */
class Path(val current: Square = Square(0, 0), val prev: Path? = null)

class Vertex(var sqr: Square, var path: Path = Path()) {
    var neighbors: Set<Square>

    init {
        neighbors = getPossibleMovements()
    }

    private fun getPossibleMovements(): Set<Square> {
        val set = mutableSetOf<Square>()
        for (i in -2..2)
            if (i != 0) {
                val j = if (abs(i) == 2) 1 else 2
                val squarePlus = Square(sqr.column + i, sqr.row + j)
                val squareMinus = Square(sqr.column + i, sqr.row - j)
                if (squarePlus.inside())
                    set.add(squarePlus)
                if (squareMinus.inside())
                    set.add(squareMinus)
            }
        return set
    }
}

fun knightMoveNumber(start: Square, end: Square): Int? {
    if (!start.inside() || !end.inside()) throw IllegalArgumentException()
    val queue = ArrayDeque<Square>()
    queue.add(start)
    val visited = mutableMapOf(start to 0)
    while (queue.isNotEmpty()) {
        val next = queue.poll()
        val distance = visited[next]!!
        if (next == end) return distance
        for (neighbor in Vertex(next).neighbors) {
            if (neighbor in visited) continue
            visited[neighbor] = distance + 1
            queue.add(neighbor)
        }
    }
    return -1
}

/**
 * Очень сложная (10 баллов)
 *
 * Вернуть список из клеток, по которым шахматный конь может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов коня см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры:
 *
 * knightTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 * здесь возможны другие варианты)
 * knightTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(5, 2), Square(4, 4), Square(6, 3))
 * (здесь возможен единственный вариант)
 * knightTrajectory(Square(3, 5), Square(5, 6)) = listOf(Square(3, 5), Square(5, 6))
 * (здесь опять возможны другие варианты)
 * knightTrajectory(Square(7, 7), Square(8, 8)) =
 *     listOf(Square(7, 7), Square(5, 8), Square(4, 6), Square(6, 7), Square(8, 8))
 *
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */

fun knightTrajectory(start: Square, end: Square): List<Square> {
    val queue = ArrayDeque<Vertex>()
    queue.add(Vertex(start, Path(start, null)))
    val visited = mutableMapOf(start to 0)
    if (start == end)
        return listOf(start)
    while (queue.isNotEmpty()) {
        val next = queue.poll()
        val distance = visited[next.sqr]!!
        if (next.sqr == end) {
            val list = mutableListOf<Square>()
            var path: Path? = next.path
            while (path!!.prev != null) {
                list.add(path.current)
                path = path.prev
            }
            list.add(path.current)
            return list.reversed()
        }
        for (neighbor in next.neighbors) {
            if (neighbor in visited) continue
            visited[neighbor] = distance + 1
            queue.add(Vertex(neighbor, Path(neighbor, next.path)))
        }
    }
    return emptyList()
}
