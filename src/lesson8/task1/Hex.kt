@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import java.lang.Math.*

/**
 * Точка (гекс) на шестиугольной сетке.
 * Координаты заданы как в примере (первая цифра - y, вторая цифра - x)
 *
 *       60  61  62  63  64  65
 *     50  51  52  53  54  55  56
 *   40  41  42  43  44  45  46  47
 * 30  31  32  33  34  35  36  37  38
 *   21  22  23  24  25  26  27  28
 *     12  13  14  15  16  17  18
 *       03  04  05  06  07  08
 *
 * В примерах к задачам используются те же обозначения точек,
 * к примеру, 16 соответствует HexPoint(x = 6, y = 1), а 41 -- HexPoint(x = 1, y = 4).
 *
 * В задачах, работающих с шестиугольниками на сетке, считать, что они имеют
 * _плоскую_ ориентацию:
 *  __
 * /  \
 * \__/
 *
 * со сторонами, параллельными координатным осям сетки.
 *
 * Более подробно про шестиугольные системы координат можно почитать по следующей ссылке:
 *   https://www.redblobgames.com/grids/hexagons/
 */
data class HexPoint(val x: Int, val y: Int) {
    /**
     * Средняя (3 балла)
     *
     * Найти целочисленное расстояние между двумя гексами сетки.
     * Расстояние вычисляется как число единичных отрезков в пути между двумя гексами.
     * Например, путь межу гексами 16 и 41 (см. выше) может проходить через 25, 34, 43 и 42 и имеет длину 5.
     */
    fun distance(other: HexPoint): Int = when {
        other.y > y && other.x <= x && other.y + other.x >= y + x ||
                other.y < y && other.x >= x && other.y + other.x <= y + x ->
            abs(y - other.y)
        other.y >= y && other.x > x || other.y <= y && other.x < x ->
            abs(y - other.y) + abs(x - other.x)
        else ->
            abs(y - other.y) + abs(other.y + other.x - y - x)
    }

    override fun toString(): String = "$y.$x"
}

/**
 * Правильный шестиугольник на гексагональной сетке.
 * Как окружность на плоскости, задаётся центральным гексом и радиусом.
 * Например, шестиугольник с центром в 33 и радиусом 1 состоит из гексов 42, 43, 34, 24, 23, 32.
 */
data class Hexagon(val center: HexPoint, val radius: Int) {

    /**
     * Средняя (3 балла)
     *
     * Рассчитать расстояние между двумя шестиугольниками.
     * Оно равно расстоянию между ближайшими точками этих шестиугольников,
     * или 0, если шестиугольники имеют общую точку.
     *
     * Например, расстояние между шестиугольником A с центром в 31 и радиусом 1
     * и другим шестиугольником B с центром в 26 и радиуоом 2 равно 2
     * (расстояние между точками 32 и 24)
     */
    fun distance(other: Hexagon): Int =
        if (center.distance(other.center) <= radius + other.radius) 0
        else center.distance(other.center) - radius - other.radius

    /**
     * Тривиальная (1 балл)
     *
     * Вернуть true, если заданная точка находится внутри или на границе шестиугольника
     */
    fun contains(point: HexPoint): Boolean = distance(Hexagon(point, 0)) == 0

    /**
     * Возвращает точки, располагающиеся на границе шестиугольника
     */
    fun border(): Set<HexPoint> {
        val set = mutableSetOf<HexPoint>()
        var point = center.move(Direction.DOWN_LEFT, radius)
        var d = Direction.RIGHT
        repeat(6) {
            repeat(radius) {
                point = point.move(d, 1)
                set.add(point)
            }
            d = d.next()
        }
        return set.toSet()
    }

    /**
     * Проверяет точки на принадлежность к границе шестиугольника
     */
    fun isBorder(p: HexPoint): Boolean =
        p.y == center.y - radius && p.x in center.x..center.x + radius ||
                p.x == center.x + radius && p.y in center.y - radius..center.y ||
                p.x + p.y == center.x + center.y + radius && p.y in center.y..center.y + radius ||
                p.y == center.y + radius && p.x in center.x - radius..center.x ||
                p.x == center.x - radius && p.y in center.y..center.y + radius ||
                p.x + p.y == center.x + center.y - radius && p.y in center.y - radius..center.y
}

/**
 * Прямолинейный отрезок между двумя гексами
 */
class HexSegment(val begin: HexPoint, val end: HexPoint) {
    /**
     * Простая (2 балла)
     *
     * Определить "правильность" отрезка.
     * "Правильным" считается только отрезок, проходящий параллельно одной из трёх осей шестиугольника.
     * Такими являются, например, отрезок 30-34 (горизонталь), 13-63 (прямая диагональ) или 51-24 (косая диагональ).
     * А, например, 13-26 не является "правильным" отрезком.
     */
    fun isValid(): Boolean =
        begin != end && (begin.x == end.x || begin.y == end.y || begin.x + begin.y == end.x + end.y)

    /**
     * Средняя (3 балла)
     *
     * Вернуть направление отрезка (см. описание класса Direction ниже).
     * Для "правильного" отрезка выбирается одно из первых шести направлений,
     * для "неправильного" -- INCORRECT.
     */
    fun direction(): Direction = when {
        !isValid() ->
            Direction.INCORRECT
        begin.x == end.x -> {
            if (begin.y > end.y) Direction.DOWN_LEFT
            else Direction.UP_RIGHT
        }
        begin.y == end.y -> {
            if (begin.x > end.x) Direction.LEFT
            else Direction.RIGHT
        }
        else -> {
            if (begin.y > end.y) Direction.DOWN_RIGHT
            else Direction.UP_LEFT
        }
    }

    /**
     * Возвращает направления для обеих частей любого возможного пути
     */
    fun directionForAny(): Pair<Direction, Direction?> = when {
        this.isValid() -> Pair<Direction, Direction?>(this.direction(), null)
        begin.y < end.y && end.x in begin.x - end.y + begin.y + 1 until begin.x -> Pair(
            Direction.UP_LEFT,
            Direction.UP_RIGHT
        )
        begin.y > end.y && end.x in begin.x + 1 until begin.x + begin.y - end.y -> Pair(
            Direction.DOWN_LEFT,
            Direction.DOWN_RIGHT
        )
        begin.x < end.x && begin.y < end.y -> Pair(Direction.RIGHT, Direction.UP_RIGHT)
        begin.x > end.x && begin.y < end.y -> Pair(Direction.LEFT, Direction.UP_LEFT)
        begin.x > end.x && begin.y > end.y -> Pair(Direction.LEFT, Direction.DOWN_LEFT)
        else -> Pair(Direction.RIGHT, Direction.DOWN_RIGHT)
    }

    override fun equals(other: Any?) =
        other is HexSegment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        begin.hashCode() + end.hashCode()
}

/**
 * Направление отрезка на гексагональной сетке.
 * Если отрезок "правильный", то он проходит вдоль одной из трёх осей шестугольника.
 * Если нет, его направление считается INCORRECT
 */
enum class Direction {
    RIGHT,      // слева направо, например 30 -> 34
    UP_RIGHT,   // вверх-вправо, например 32 -> 62
    UP_LEFT,    // вверх-влево, например 25 -> 61
    LEFT,       // справа налево, например 34 -> 30
    DOWN_LEFT,  // вниз-влево, например 62 -> 32
    DOWN_RIGHT, // вниз-вправо, например 61 -> 25
    INCORRECT;  // отрезок имеет изгиб, например 30 -> 55 (изгиб в точке 35)

    /**
     * Простая (2 балла)
     *
     * Вернуть направление, противоположное данному.
     * Для INCORRECT вернуть INCORRECT
     */
    fun opposite(): Direction = when (this) {
        RIGHT -> LEFT
        LEFT -> RIGHT
        DOWN_RIGHT -> UP_LEFT
        DOWN_LEFT -> UP_RIGHT
        UP_LEFT -> DOWN_RIGHT
        UP_RIGHT -> DOWN_LEFT
        else -> INCORRECT
    }

    /**
     * Средняя (3 балла)
     *
     * Вернуть направление, повёрнутое относительно
     * заданного на 60 градусов против часовой стрелки.
     *
     * Например, для RIGHT это UP_RIGHT, для UP_LEFT это LEFT, для LEFT это DOWN_LEFT.
     * Для направления INCORRECT бросить исключение IllegalArgumentException.
     * При решении этой задачи попробуйте обойтись без перечисления всех семи вариантов.
     */
    fun next(): Direction =
        when (this) {
            INCORRECT -> throw IllegalArgumentException("This direction is incorrect.")
            DOWN_RIGHT -> RIGHT
            else -> values()[this.ordinal + 1]
        }

    /**
     * Простая (2 балла)
     *
     * Вернуть true, если данное направление совпадает с other или противоположно ему.
     * INCORRECT не параллельно никакому направлению, в том числе другому INCORRECT.
     */
    fun isParallel(other: Direction): Boolean = this != INCORRECT && (this == other || this == other.opposite())
}

/**
 * Средняя (3 балла)
 *
 * Сдвинуть точку в направлении direction на расстояние distance.
 * Бросить IllegalArgumentException(), если задано направление INCORRECT.
 * Для расстояния 0 и направления не INCORRECT вернуть ту же точку.
 * Для отрицательного расстояния сдвинуть точку в противоположном направлении на -distance.
 *
 * Примеры:
 * 30, direction = RIGHT, distance = 3 --> 33
 * 35, direction = UP_LEFT, distance = 2 --> 53
 * 45, direction = DOWN_LEFT, distance = 4 --> 05
 */
fun HexPoint.move(direction: Direction, distance: Int): HexPoint {
    if (direction == Direction.INCORRECT) throw IllegalArgumentException("This direction is incorrect.")
    if (distance == 0) return this
    return when (if (distance < 0) direction.opposite() else direction) {
        Direction.RIGHT -> HexPoint(x + abs(distance), y)
        Direction.LEFT -> HexPoint(x - abs(distance), y)
        Direction.DOWN_RIGHT -> HexPoint(x + abs(distance), y - abs(distance))
        Direction.DOWN_LEFT -> HexPoint(x, y - abs(distance))
        Direction.UP_LEFT -> HexPoint(x - abs(distance), y + abs(distance))
        Direction.UP_RIGHT -> HexPoint(x, y + abs(distance))
        else -> HexPoint(x, y)
    }
}


/**
 * Сложная (5 баллов)
 *
 * Найти кратчайший путь между двумя заданными гексами, представленный в виде списка всех гексов,
 * которые входят в этот путь.
 * Начальный и конечный гекс также входят в данный список.
 * Если кратчайших путей существует несколько, вернуть любой из них.
 *
 * Пример (для координатной сетки из примера в начале файла):
 *   pathBetweenHexes(HexPoint(y = 2, x = 2), HexPoint(y = 5, x = 3)) ->
 *     listOf(
 *       HexPoint(y = 2, x = 2),
 *       HexPoint(y = 2, x = 3),
 *       HexPoint(y = 3, x = 3),
 *       HexPoint(y = 4, x = 3),
 *       HexPoint(y = 5, x = 3)
 *     )
 */
fun pathBetweenHexes(from: HexPoint, to: HexPoint): List<HexPoint> {
    val list = mutableListOf<HexPoint>()
    var point = from
    val direction = HexSegment(from, to).directionForAny()
    while (point.y != to.y && point.x + point.y != to.x + to.y && point.x != to.x) {
        list.add(point)
        point = point.move(direction.first, 1)
    }
    while (point != to) {
        list.add(point)
        point = point.move(direction.second ?: direction.first, 1)
    }
    list.add(to)
    return list
}

/**
 * Очень сложная (20 баллов)
 *
 * Дано три точки (гекса). Построить правильный шестиугольник, проходящий через них
 * (все три точки должны лежать НА ГРАНИЦЕ, а не ВНУТРИ, шестиугольника).
 * Все стороны шестиугольника должны являться "правильными" отрезками.
 * Вернуть null, если такой шестиугольник построить невозможно.
 * Если шестиугольников существует более одного, выбрать имеющий минимальный радиус.
 *
 * Пример: через точки 13, 32 и 44 проходит правильный шестиугольник с центром в 24 и радиусом 2.
 * Для точек 13, 32 и 45 такого шестиугольника не существует.
 * Для точек 32, 33 и 35 следует вернуть шестиугольник радиусом 3 (с центром в 62 или 05).
 *
 * Если все три точки совпадают, вернуть шестиугольник нулевого радиуса с центром в данной точке.
 */
fun hexagonByThreePoints(a: HexPoint, b: HexPoint, c: HexPoint): Hexagon? {
    val set = mutableSetOf(0, 1, 2, 3, 4, 5)
    if (b.y < a.y || c.y < a.y)
        set.remove(0)
    if (b.x > a.x || c.x > a.x)
        set.remove(1)
    if (b.x + b.y > a.x + a.y || c.x + c.y > a.x + a.y)
        set.remove(2)
    if (b.y > a.y || c.y > a.y)
        set.remove(3)
    if (b.x < a.x || c.x < a.x)
        set.remove(4)
    if (b.x + b.y < a.x + a.y || c.x + c.y < a.x + a.y)
        set.remove(5)
    val max = maxOf(a.distance(b), a.distance(c), b.distance(c))
    for (r in max / 2..max)
        for (i in 0..r) {
            val hex = arrayOf(//                               3
                Hexagon(HexPoint(a.x - i, a.y + r), r),//    4/--\2
                Hexagon(HexPoint(a.x - r, a.y + r - i), r),//5\__/1
                Hexagon(HexPoint(a.x - r + i, a.y - i), r),//   0
                Hexagon(HexPoint(a.x + i, a.y - r), r),
                Hexagon(HexPoint(a.x + r, a.y - r + i), r),
                Hexagon(HexPoint(a.x + r - i, a.y + i), r)
            )
            for (j in set)
                if (hex[j].isBorder(b) && hex[j].isBorder(c))
                    return hex[j]
        }
    return null
}

/**
 * Очень сложная (20 баллов)
 *
 * Дано множество точек (гексов). Найти правильный шестиугольник минимального радиуса,
 * содержащий все эти точки (безразлично, внутри или на границе).
 * Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит один гекс, вернуть шестиугольник нулевого радиуса с центром в данной точке.
 *
 * Пример: 13, 32, 45, 18 -- шестиугольник радиусом 3 (с центром, например, в 15)
 */

fun minContainingHexagon(vararg points: HexPoint): Hexagon {
    val extreme = Array(6) { points[0] }
//    3
//  4/--\2
//  5\__/1
//    0
    for (i in points) {
        if (i.y < extreme[0].y) extreme[0] = i
        if (i.x > extreme[1].x) extreme[1] = i
        if (i.y + i.x > extreme[2].x + extreme[2].y) extreme[2] = i
        if (i.y > extreme[3].y) extreme[3] = i
        if (i.x < extreme[4].x) extreme[4] = i
        if (i.y + i.x < extreme[5].x + extreme[5].y) extreme[5] = i
    }
    var max = 0
    for (i in points)
        for (j in points)
            if (i.distance(j) > max)
                max = i.distance(j)
    var r = max / 2
    while (true) {
        for ((x, y) in extreme)
            for (i in 0..r) {
                val hex = arrayOf(//                           3
                    Hexagon(HexPoint(x - i, y + r), r),//    4/--\2
                    Hexagon(HexPoint(x - r, y + r - i), r),//5\__/1
                    Hexagon(HexPoint(x - r + i, y - i), r),//   0
                    Hexagon(HexPoint(x + i, y - r), r),
                    Hexagon(HexPoint(x + r, y - r + i), r),
                    Hexagon(HexPoint(x + r - i, y + i), r)
                )
                for (j in 0..5)
                    if (points.all { hex[j].contains(it) })
                        return hex[j]

            }
        r++
    }
}
/*if (points.isEmpty()) throw IllegalArgumentException()
    val set = points.toSet()
    var x = 0
    var y = 0
    for (i in set) {
        x += i.x
        y += i.y
    }
    val m = HexPoint(x / set.size, y / set.size)
    var minX = Double.POSITIVE_INFINITY.toInt()
    var p1: HexPoint
    var minY = Double.POSITIVE_INFINITY.toInt()
    var p2: HexPoint
    var maxX = Double.NEGATIVE_INFINITY.toInt()
    var p3: HexPoint
    var maxY = Double.NEGATIVE_INFINITY.toInt()
    var p4: HexPoint
    for ((x, y) in set) {
        if (x > maxX) {
            p1 = HexPoint(x, y)
            maxX = x
        } else if (x < minX) {
            p2 = HexPoint(x, y)
            minX = x
        }
        if (y > maxY) {
            p3 = HexPoint(x, y)
            maxY = y
        } else if (y < minY) {
            p4 = HexPoint(x, y)
            minY = y
        }
    }
    val max = max(maxX - minX, maxY - minY)
    for (r in max..max * 2) {

    }
    *//*var maxDistance = 0
    var minDistance = Double.POSITIVE_INFINITY.toInt()
    for (i in set) {
        if (i.distance(m) > maxDistance)
            maxDistance = i.distance(m)

        if (i.distance(m) < minDistance)
            minDistance = i.distance(m)
    }
    val centers = Hexagon(m, 1).border().toMutableSet()
    centers.add(m)
    for (r in maxDistance / 2..maxDistance)
        for (i in centers)
            if (set.all { Hexagon(i, r).contains(it) })
                return Hexagon(i, r)*//*
    return Hexagon(m, 0)*/


