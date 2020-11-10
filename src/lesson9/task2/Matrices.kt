@file:Suppress("UNUSED_PARAMETER")

package lesson9.task2

import lesson9.task1.*
import java.lang.IllegalArgumentException

// Все задачи в этом файле требуют наличия реализации интерфейса "Матрица" в Matrix.kt

/**
 * Пример
 *
 * Транспонировать заданную матрицу matrix.
 * При транспонировании строки матрицы становятся столбцами и наоборот:
 *
 * 1 2 3      1 4 6 3
 * 4 5 6  ==> 2 5 5 2
 * 6 5 4      3 6 4 1
 * 3 2 1
 */
fun <E> transpose(matrix: Matrix<E>): Matrix<E> {
    if (matrix.width < 1 || matrix.height < 1) return matrix
    val result = createMatrix(height = matrix.width, width = matrix.height, e = matrix[0, 0])
    for (i in 0 until matrix.width) {
        for (j in 0 until matrix.height) {
            result[i, j] = matrix[j, i]
        }
    }
    return result
}

/**
 * Пример
 *
 * Сложить две заданные матрицы друг с другом.
 * Складывать можно только матрицы совпадающего размера -- в противном случае бросить IllegalArgumentException.
 * При сложении попарно складываются соответствующие элементы матриц
 */
operator fun Matrix<Int>.plus(other: Matrix<Int>): Matrix<Int> {
    require(!(width != other.width || height != other.height))
    if (width < 1 || height < 1) return this
    val result = createMatrix(height, width, this[0, 0])
    for (i in 0 until height) {
        for (j in 0 until width) {
            result[i, j] = this[i, j] + other[i, j]
        }
    }
    return result
}

/**
 * Сложная (5 баллов)
 *
 * Заполнить матрицу заданной высоты height и ширины width
 * натуральными числами от 1 до m*n по спирали,
 * начинающейся в левом верхнем углу и закрученной по часовой стрелке.
 *
 * Пример для height = 3, width = 4:
 *  1  2  3  4
 * 10 11 12  5
 *  9  8  7  6
 */
fun generateSpiral(height: Int, width: Int): Matrix<Int> {
    val matrix = createMatrix(height, width, 0)
    var c = 1
    var startH = 0
    var lastH = height - 1
    var startW = 0
    var lastW = width - 1
    while (true) {
        if (startH > lastH || startW > lastW) break
        for (i in startW..lastW)
            matrix[startH, i] = c++
        startH++
        if (startH > lastH || startW > lastW) break
        for (j in startH..lastH)
            matrix[j, lastW] = c++
        lastW--
        if (startH > lastH || startW > lastW) break
        for (i in lastW downTo startW)
            matrix[lastH, i] = c++
        lastH--
        if (startH > lastH || startW > lastW) break
        for (j in lastH downTo startH)
            matrix[j, startW] = c++
        startW++
    }
    return matrix
}

/**
 * Сложная (5 баллов)
 *
 * Заполнить матрицу заданной высоты height и ширины width следующим образом.
 * Элементам, находящимся на периферии (по периметру матрицы), присвоить значение 1;
 * периметру оставшейся подматрицы – значение 2 и так далее до заполнения всей матрицы.
 *
 * Пример для height = 5, width = 6:
 *  1  1  1  1  1  1
 *  1  2  2  2  2  1
 *  1  2  3  3  2  1
 *  1  2  2  2  2  1
 *  1  1  1  1  1  1
 */
fun generateRectangles(height: Int, width: Int): Matrix<Int> {
    val matrix = createMatrix(height, width, 0)
    var c = 1
    var startH = 0
    var lastH = height - 1
    var startW = 0
    var lastW = width - 1
    while (true) {
        if (startH > lastH || startW > lastW) break
        for (i in startW..lastW)
            matrix[startH, i] = c
        startH++
        if (startH > lastH || startW > lastW) break
        for (j in startH..lastH)
            matrix[j, lastW] = c
        lastW--
        if (startH > lastH || startW > lastW) break
        for (i in lastW downTo startW)
            matrix[lastH, i] = c
        lastH--
        if (startH > lastH || startW > lastW) break
        for (j in lastH downTo startH)
            matrix[j, startW] = c
        startW++
        c++
    }
    return matrix
}

/**
 * Сложная (5 баллов)
 *
 * Заполнить матрицу заданной высоты height и ширины width диагональной змейкой:
 * в левый верхний угол 1, во вторую от угла диагональ 2 и 3 сверху вниз, в третью 4-6 сверху вниз и так далее.
 *
 * Пример для height = 5, width = 4:
 *  1  2  4  7
 *  3  5  8 11
 *  6  9 12 15
 * 10 13 16 18
 * 14 17 19 20
 */
fun generateSnake(height: Int, width: Int): Matrix<Int> = TODO()

/**
 * Средняя (3 балла)
 *
 * Содержимое квадратной матрицы matrix (с произвольным содержимым) повернуть на 90 градусов по часовой стрелке.
 * Если height != width, бросить IllegalArgumentException.
 *
 * Пример:    Станет:
 * 1 2 3      7 4 1
 * 4 5 6      8 5 2
 * 7 8 9      9 6 3
 */
fun <E> rotate(matrix: Matrix<E>): Matrix<E> {
    if (matrix.height != matrix.width) throw IllegalArgumentException()
    val result = createMatrix(matrix.height, matrix.width, matrix[0, 0])
    for (i in 0 until matrix.height)
        for (j in 0 until matrix.width)
            result[j, matrix.width - i - 1] = matrix[i, j]
    return result
}

/**
 * Сложная (5 баллов)
 *
 * Проверить, является ли квадратная целочисленная матрица matrix латинским квадратом.
 * Латинским квадратом называется матрица размером n x n,
 * каждая строка и каждый столбец которой содержат все числа от 1 до n.
 * Если height != width, вернуть false.
 *
 * Пример латинского квадрата 3х3:
 * 2 3 1
 * 1 2 3
 * 3 1 2
 */
fun isLatinSquare(matrix: Matrix<Int>): Boolean {
    var list = MutableList(matrix.width) { true }
    for (i in 0 until matrix.height) {
        for (j in 0 until matrix.width) {
            if (matrix[i, j] !in 1..matrix.width || !list[matrix[i, j] - 1])
                return false
            else list[matrix[i, j] - 1] = false
        }
        list = MutableList(matrix.width) { true }
    }
    for (i in 0 until matrix.height) {
        for (j in 0 until matrix.width) {
            if (matrix[j, i] !in 1..matrix.width || !list[matrix[j, i] - 1])
                return false
            else list[matrix[j, i] - 1] = false
        }
        list = MutableList(matrix.width) { true }
    }
    return true
}

/**
 * Средняя (3 балла)
 *
 * В матрице matrix каждый элемент заменить суммой непосредственно примыкающих к нему
 * элементов по вертикали, горизонтали и диагоналям.
 *
 * Пример для матрицы 4 x 3: (11=2+4+5, 19=1+3+4+5+6, ...)
 * 1 2 3       11 19 13
 * 4 5 6  ===> 19 31 19
 * 6 5 4       19 31 19
 * 3 2 1       13 19 11
 *
 * Поскольку в матрице 1 х 1 примыкающие элементы отсутствуют,
 * для неё следует вернуть как результат нулевую матрицу:
 *
 * 42 ===> 0
 */
fun sumNeighbours(matrix: Matrix<Int>): Matrix<Int> = TODO()

/**
 * Средняя (4 балла)
 *
 * Целочисленная матрица matrix состоит из "дырок" (на их месте стоит 0) и "кирпичей" (на их месте стоит 1).
 * Найти в этой матрице все ряды и колонки, целиком состоящие из "дырок".
 * Результат вернуть в виде Holes(rows = список дырчатых рядов, columns = список дырчатых колонок).
 * Ряды и колонки нумеруются с нуля. Любой из спискоов rows / columns может оказаться пустым.
 *
 * Пример для матрицы 5 х 4:
 * 1 0 1 0
 * 0 0 1 0
 * 1 0 0 0 ==> результат: Holes(rows = listOf(4), columns = listOf(1, 3)): 4-й ряд, 1-я и 3-я колонки
 * 0 0 1 0
 * 0 0 0 0
 */
fun findHoles(matrix: Matrix<Int>): Holes = TODO()

/**
 * Класс для описания местонахождения "дырок" в матрице
 */
data class Holes(val rows: List<Int>, val columns: List<Int>)

/**
 * Средняя (3 балла)
 *
 * В целочисленной матрице matrix каждый элемент заменить суммой элементов подматрицы,
 * расположенной в левом верхнем углу матрицы matrix и ограниченной справа-снизу данным элементом.
 *
 * Пример для матрицы 3 х 3:
 *
 * 1  2  3      1  3  6
 * 4  5  6  =>  5 12 21
 * 7  8  9     12 27 45
 *
 * К примеру, центральный элемент 12 = 1 + 2 + 4 + 5, элемент в левом нижнем углу 12 = 1 + 4 + 7 и так далее.
 */
fun sumSubMatrix(matrix: Matrix<Int>): Matrix<Int> = TODO()

/**
 * Простая (2 балла)
 *
 * Инвертировать заданную матрицу.
 * При инвертировании знак каждого элемента матрицы следует заменить на обратный
 */
operator fun Matrix<Int>.unaryMinus(): Matrix<Int> {
    val result = createMatrix(this.height, this.width, 0)
    for (i in 0 until this.height)
        for (j in 0 until this.width)
            result[i, j] = -this[i, j]
    return result
}

/**
 * Средняя (4 балла)
 *
 * Перемножить две заданные матрицы друг с другом.
 * Матрицы можно умножать, только если ширина первой матрицы совпадает с высотой второй матрицы.
 * В противном случае бросить IllegalArgumentException.
 * Подробно про порядок умножения см. статью Википедии "Умножение матриц".
 */
operator fun Matrix<Int>.times(other: Matrix<Int>): Matrix<Int> {
    TODO()
}

/**
 * Сложная (7 баллов)
 *
 * Даны мозаичные изображения замочной скважины и ключа. Пройдет ли ключ в скважину?
 * То есть даны две матрицы key и lock, key.height <= lock.height, key.width <= lock.width, состоящие из нулей и единиц.
 *
 * Проверить, можно ли наложить матрицу key на матрицу lock (без поворота, разрешается только сдвиг) так,
 * чтобы каждой единице в матрице lock (штырь) соответствовал ноль в матрице key (прорезь),
 * а каждому нулю в матрице lock (дырка) соответствовала, наоборот, единица в матрице key (штырь).
 * Ключ при сдвиге не может выходить за пределы замка.
 *
 * Пример: ключ подойдёт, если его сдвинуть на 1 по ширине
 * lock    key
 * 1 0 1   1 0
 * 0 1 0   0 1
 * 1 1 1
 *
 * Вернуть тройку (Triple) -- (да/нет, требуемый сдвиг по высоте, требуемый сдвиг по ширине).
 * Если наложение невозможно, то первый элемент тройки "нет" и сдвиги могут быть любыми.
 */
fun canOpenLock(key: Matrix<Int>, lock: Matrix<Int>): Triple<Boolean, Int, Int> {
    for (i in 0 until lock.height)
        for (j in 0 until lock.width)
            if (lock[i, j] != key[0, 0])
                loop@ for (ki in 0 until key.height)
                    for (kj in 0 until key.width) {
                        if (lock[i + ki, j + kj] == key[ki, kj]) break@loop
                        if (ki == key.height - 1 && kj == key.width - 1)
                            return Triple(true, i, j)
                    }
    return Triple(false, 0, 0)
}


/**
 * Сложная (8 баллов)
 *
 * В матрице matrix размером 4х4 дана исходная позиция для игры в 15, например
 *  5  7  9  1
 *  2 12 14 15
 *  3  4  6  8
 * 10 11 13  0
 *
 * Здесь 0 обозначает пустую клетку, а 1-15 – фишки с соответствующими номерами.
 * Напомним, что "игра в 15" имеет квадратное поле 4х4, по которому двигается 15 фишек,
 * одна клетка всегда остаётся пустой. Цель игры -- упорядочить фишки на игровом поле.
 *
 * В списке moves задана последовательность ходов, например [8, 6, 13, 11, 10, 3].
 * Ход задаётся номером фишки, которая передвигается на пустое место (то есть, меняется местами с нулём).
 * Фишка должна примыкать к пустому месту по горизонтали или вертикали, иначе ход не будет возможным.
 * Все номера должны быть в пределах от 1 до 15.
 * Определить финальную позицию после выполнения всех ходов и вернуть её.
 * Если какой-либо ход является невозможным или список содержит неверные номера,
 * бросить IllegalStateException.
 *
 * В данном случае должно получиться
 * 5  7  9  1
 * 2 12 14 15
 * 0  4 13  6
 * 3 10 11  8
 */
fun fifteenGameMoves(matrix: Matrix<Int>, moves: List<Int>): Matrix<Int> {
    var nullPosition = numPosition(matrix, 0)
    for (i in moves) {
        if (findNeighbours(matrix, nullPosition).all { matrix[it] != i }) throw IllegalStateException()
        for (p in findNeighbours(matrix, nullPosition))
            if (matrix[p] == i) {
                matrix.swap(nullPosition, p)
                nullPosition = p
                break
            }
    }
    return matrix
}

/**
 * Возвращает соседей данного квадрата
 */
fun findNeighbours(matrix: Matrix<Int>, position: Cell): Set<Cell> {
    val set = mutableSetOf<Cell>()
    if (position.row > 0) set.add(Cell(position.row - 1, position.column))
    if (position.column > 0) set.add(Cell(position.row, position.column - 1))
    if (position.row < 3) set.add(Cell(position.row + 1, position.column))
    if (position.column < 3) set.add(Cell(position.row, position.column + 1))
    return set
}

/**
 * Очень сложная (32 балла)
 *
 * В матрице matrix размером 4х4 дана исходная позиция для игры в 15, например
 *  5  7  9  2
 *  1 12 14 15
 *  3  4  6  8
 * 10 11 13  0
 *
 * Здесь 0 обозначает пустую клетку, а 1-15 – фишки с соответствующими номерами.
 * Напомним, что "игра в 15" имеет квадратное поле 4х4, по которому двигается 15 фишек,
 * одна клетка всегда остаётся пустой.
 *
 * Цель игры -- упорядочить фишки на игровом поле, приведя позицию к одному из следующих двух состояний:
 *
 *  1  2  3  4          1  2  3  4
 *  5  6  7  8   ИЛИ    5  6  7  8
 *  9 10 11 12          9 10 11 12
 * 13 14 15  0         13 15 14  0
 *
 * Можно математически доказать, что РОВНО ОДНО из этих двух состояний достижимо из любой исходной позиции.
 *
 * Вернуть решение -- список ходов, приводящих исходную позицию к одной из двух упорядоченных.
 * Каждый ход -- это перемена мест фишки с заданным номером с пустой клеткой (0),
 * при этом заданная фишка должна по горизонтали или по вертикали примыкать к пустой клетке (но НЕ по диагонали).
 * К примеру, ход 13 в исходной позиции меняет местами 13 и 0, а ход 11 в той же позиции невозможен.
 *
 * Одно из решений исходной позиции:
 *
 * [8, 6, 14, 12, 4, 11, 13, 14, 12, 4,
 * 7, 5, 1, 3, 11, 7, 3, 11, 7, 12, 6,
 * 15, 4, 9, 2, 4, 9, 3, 5, 2, 3, 9,
 * 15, 8, 14, 13, 12, 7, 11, 5, 7, 6,
 * 9, 15, 8, 14, 13, 9, 15, 7, 6, 12,
 * 9, 13, 14, 15, 12, 11, 10, 9, 13, 14,
 * 15, 12, 11, 10, 9, 13, 14, 15]
 *
 * Перед решением этой задачи НЕОБХОДИМО решить предыдущую
 */
val map = mapOf(
    1 to Cell(0, 0),
    2 to Cell(0, 1),
    3 to Cell(0, 2),
    4 to Cell(0, 3),
    5 to Cell(1, 0),
    6 to Cell(1, 1),
    7 to Cell(1, 2),
    8 to Cell(1, 3),
    9 to Cell(2, 0),
    10 to Cell(2, 1),
    11 to Cell(2, 2),
    12 to Cell(2, 3),
    13 to Cell(3, 0)
)

fun moveFor1256(matrix: Matrix<Int>, n: Int) {
    var nullPosition = numPosition(matrix, 0)
    var position = numPosition(matrix, n)
    if (position != map[n]) {
        val x = if (nullPosition.column > position.column - 1) -1 else 1
        val y = if (nullPosition.row > position.row - 1) -1 else 1

        while (nullPosition.column != position.column - 1 && nullPosition.column != position.column + 1) {
            /*if (position == Cell(nullPosition.row, nullPosition.column + x)) {
                matrix.swap(nullPosition, position)
                val t = position
                position = nullPosition
                nullPosition = t
                break
            }*/
            matrix.swap(nullPosition, Cell(nullPosition.row, nullPosition.column + x))
        }
        if ((nullPosition.column < position.column && position.column < map[n]!!.column
                    || nullPosition.column > position.column && position.column > map[n]!!.column)
        ) {
            matrix[nullPosition] = matrix[nullPosition.row - 1, nullPosition.column]
            matrix[nullPosition.row - 1, nullPosition.column] = matrix[position.row - 1, position.column]

        }
        while (nullPosition.row != position.row) {
            if (position == Cell(nullPosition.row + y, nullPosition.column)) {
                matrix.swap(nullPosition, position)
                val t = position
                position = nullPosition
                nullPosition = t
                break
            }
            matrix.swap(nullPosition, Cell(nullPosition.row + y, nullPosition.column))
        }
        while (position.column != 0) {
            matrix[nullPosition] = matrix[position]
            matrix[position] = matrix[position.row + 1, position.column]
            matrix[position.row + 1, position.column] = matrix[position.row + 1, position.column - 1]
            matrix[position.row + 1, position.column - 1] = matrix[position.row + 1, position.column - 2]
            matrix[position.row + 1, position.column - 2] = matrix[position.row, position.column - 2]
            matrix[position.row, position.column - 2] = 0
            position = nullPosition
            nullPosition = Cell(position.row, position.column - 1)
        }
        if (nullPosition.column == position.column + 1) {
            matrix[nullPosition] = matrix[nullPosition.row - 1, nullPosition.column]
            matrix[nullPosition.row - 1, nullPosition.column] = matrix[nullPosition.row - 1, nullPosition.column - 1]
            nullPosition = Cell(position.row - 1, position.column)
        } else {
            TODO()
        }
        while (position.row != 0) {
            matrix[nullPosition] = matrix[position]
            matrix[position] = matrix[position.row, position.column + 1]
            matrix[position.row, position.column + 1] = matrix[position.row - 1, position.column + 1]
            matrix[position.row - 1, position.column + 1] = matrix[position.row - 2, position.column + 1]
            matrix[position.row - 2, position.column + 1] = matrix[position.row - 2, position.column]
            matrix[position.row - 2, position.column] = 0
            position = nullPosition
            nullPosition = Cell(position.row - 2, position.column)
        }
    }
}

fun fifteenGameSolution(matrix: Matrix<Int>): List<Int> {
    /*fun upBefore(position: Cell, expectedPosition: Cell) {
        if (position == expectedPosition) return
        if (nullPosition != position)
        *//*if (position.row < expectedPosition.row)
            if (nullPosition.row == position.row + 1)
                if (nullPosition.column)*//*
    }*/
    /*var nullPosition = numPosition(matrix, 0)
    var position = numPosition(matrix, 1)
    if (position != Cell(0, 0)) {
        val x = if (nullPosition.column > position.column - 1) -1 else 1
        val y = if (nullPosition.row > position.row - 1) -1 else 1

        while (nullPosition.column != position.column - 1) {
            if (position == Cell(nullPosition.row, nullPosition.column + x)) {
                matrix.swap(nullPosition, position)
                val t = position
                position = nullPosition
                nullPosition = t
                break
            }
            matrix.swap(nullPosition, Cell(nullPosition.row, nullPosition.column + x))
        }
        while (nullPosition.row != position.row) {
            if (position == Cell(nullPosition.row + y, nullPosition.column)) {
                matrix.swap(nullPosition, position)
                val t = position
                position = nullPosition
                nullPosition = t
                break
            }
            matrix.swap(nullPosition, Cell(nullPosition.row + y, nullPosition.column))
        }
        while (position.column != 0) {
            matrix[nullPosition] = matrix[position]
            matrix[position] = matrix[position.row + 1, position.column]
            matrix[position.row + 1, position.column] = matrix[position.row + 1, position.column - 1]
            matrix[position.row + 1, position.column - 1] = matrix[position.row + 1, position.column - 2]
            matrix[position.row + 1, position.column - 2] = matrix[position.row, position.column - 2]
            matrix[position.row, position.column - 2] = 0
            position = nullPosition
            nullPosition = Cell(position.row, position.column - 1)
        }
        if (nullPosition.column == position.column + 1) {
            matrix[nullPosition] = matrix[nullPosition.row - 1, nullPosition.column]
            matrix[nullPosition.row - 1, nullPosition.column] = matrix[nullPosition.row - 1, nullPosition.column - 1]
            nullPosition = Cell(position.row - 1, position.column)
        } else {
            TODO()
        }
        while (position.row != 0) {
            matrix[nullPosition] = matrix[position]
            matrix[position] = matrix[position.row, position.column + 1]
            matrix[position.row, position.column + 1] = matrix[position.row - 1, position.column + 1]
            matrix[position.row - 1, position.column + 1] = matrix[position.row - 2, position.column + 1]
            matrix[position.row - 2, position.column + 1] = matrix[position.row - 2, position.column]
            matrix[position.row - 2, position.column] = 0
            position = nullPosition
            nullPosition = Cell(position.row - 2, position.column)
        }


    }*/
    TODO()

}

fun numPosition(matrix: Matrix<Int>, n: Int): Cell {
    for (i in 0..3)
        for (j in 0..3)
            if (matrix[i, j] == n)
                return Cell(i, j)
    return Cell(9, 9)
}
/*class Vertex(
    var state: Pair<Matrix<Int>, Int>,
    var previous: Int,
    var cost: Int,
    var heuristic: Int,
    var marked: Boolean
)

val Neighbors = MutableList<Pair<Matrix<Int>, Int>>(4) { Pair(createMatrix(1, 1, 1), 1) }
val L = mutableListOf<Vertex>()
var tailIndex = 0

fun fifteenGameSolution(matrix: Matrix<Int>): List<Int> {
    L.add(Vertex(Pair(matrix.clone(), 0), -1, 0, heuristic(matrix.clone()), false))
    var headIndex = 0
    tailIndex = 1
    var c = 0
    while (tailIndex != 0) {
        val index = getNextIndex()
        var v = L[index]
        L[index].marked = true
        println(L[index].state.first)

        if (isGoal(v.state.first)) {
            if (v.state.second == 0) return emptyList()
            val list = mutableListOf(v.state.second)
            while (v.previous != -1) {
                v = L[v.previous]
                list.add(v.state.second)
            }
            print(list)
            return list
        }
        val N = getNeighbours(v.state.first.clone())
        for (i in 0 until N) {
            L.add(
                Vertex(
                    Pair(Neighbors[i].first.clone(), Neighbors[i].second),
                    index,
                    v.cost + 1,
                    heuristic(Neighbors[i].first.clone()),
                    false
                )
            )
            tailIndex++
        }
        headIndex++
    }
    return emptyList()
}


fun isGoal(s: Matrix<Int>): Boolean {
    var r = 0
    var n = 1
    for (i in 0..2)
        for (j in 0..3) {
            if (s[i, j] != n) return false
            n++
        }
    if (s[3, 0] == 13 && (s[3, 1] == 14 && s[3, 2] == 15 || s[3, 1] == 15 && s[3, 2] == 14))
        return true
    return false
}


fun getNeighbours(s: Matrix<Int>): Int {
    val nullPosition = numPosition(s.clone(), 0)
    var index = 0
    val di = arrayOf(-1, 0, 1, 0)
    val dj = arrayOf(0, -1, 0, 1)
    for (k in 0..3) {
        val i = nullPosition.row + di[k]
        val j = nullPosition.column + dj[k]
        if (i >= 0 && j >= 0 && i < 4 && j < 4) {
            Neighbors[index] = Pair(s.clone(), s[i, j])
            Neighbors[index].first.swap(nullPosition, Cell(i, j))
            index++
        }
    }
    return index
}

fun heuristic(s: Matrix<Int>): Int {
    val rowOf = arrayOf(0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3)
    val colOf = arrayOf(0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2)
    var r = 0
    for (i in 0..3)
        for (j in 0..3) {
            if (s[i, j] != 0)
                r += abs(rowOf[s[i, j] - 1] - i) + abs(colOf[s[i, j] - 1] - j)
        }
    return r
}

fun getNextIndex(): Int {
    var index = -1
    var min = Double.POSITIVE_INFINITY.toInt()
    for (i in 0 until tailIndex) {
        if (!L[i].marked && L[i].cost + L[i].heuristic < min) {
            index = i
            min = L[i].cost + L[i].heuristic
        }
    }
    return index
}*/
















