@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import lesson1.task1.sqr
import java.lang.IllegalArgumentException
import kotlin.math.*

// Урок 8: простые классы
// Максимальное количество баллов = 40 (без очень трудных задач = 11)

/**
 * Точка на плоскости
 */
data class Point(val x: Double, val y: Double) {
    /**
     * Пример
     *
     * Рассчитать (по известной формуле) расстояние между двумя точками
     */
    fun distance(other: Point): Double = sqrt(sqr(x - other.x) + sqr(y - other.y))

    fun insideCircle(c: Circle): Boolean = c.center.distance(this) <= c.radius
}

/**
 * Треугольник, заданный тремя точками (a, b, c, см. constructor ниже).
 * Эти три точки хранятся в множестве points, их порядок не имеет значения.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Triangle private constructor(private val points: Set<Point>) {

    private val pointList = points.toList()

    val a: Point get() = pointList[0]

    val b: Point get() = pointList[1]

    val c: Point get() = pointList[2]

    constructor(a: Point, b: Point, c: Point) : this(linkedSetOf(a, b, c))

    /**
     * Пример: полупериметр
     */
    fun halfPerimeter() = (a.distance(b) + b.distance(c) + c.distance(a)) / 2.0

    /**
     * Пример: площадь
     */
    fun area(): Double {
        val p = halfPerimeter()
        return sqrt(p * (p - a.distance(b)) * (p - b.distance(c)) * (p - c.distance(a)))
    }

    /**
     * Пример: треугольник содержит точку
     */
    fun contains(p: Point): Boolean {
        val abp = Triangle(a, b, p)
        val bcp = Triangle(b, c, p)
        val cap = Triangle(c, a, p)
        return abp.area() + bcp.area() + cap.area() <= area()
    }

    override fun equals(other: Any?) = other is Triangle && points == other.points

    override fun hashCode() = points.hashCode()

    override fun toString() = "Triangle(a = $a, b = $b, c = $c)"
}

/**
 * Окружность с заданным центром и радиусом
 */
data class Circle(val center: Point, val radius: Double) {
    /**
     * Простая (2 балла)
     *
     * Рассчитать расстояние между двумя окружностями.
     * Расстояние между непересекающимися окружностями рассчитывается как
     * расстояние между их центрами минус сумма их радиусов.
     * Расстояние между пересекающимися окружностями считать равным 0.0.
     */
    fun distance(other: Circle): Double =
        max(0.0, center.distance(other.center) - radius - other.radius)

    /**
     * Тривиальная (1 балл)
     *
     * Вернуть true, если и только если окружность содержит данную точку НА себе или ВНУТРИ себя
     */
    fun contains(p: Point): Boolean = center.distance(p) <= radius
}

/**
 * Отрезок между двумя точками
 */
data class Segment(val begin: Point, val end: Point) {
    override fun equals(other: Any?) =
        other is Segment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        begin.hashCode() + end.hashCode()

    fun medium(): Point = Point((begin.x + end.x) / 2, (begin.y + end.y) / 2)
}

/**
 * Средняя (3 балла)
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment {
    if (points.toSet().size < 2) throw IllegalArgumentException()
    var p1 = points[0]
    var p2 = points[1]
    var distance = p1.distance(p2)
    for (i in 0..points.lastIndex)
        for (j in i + 1..points.lastIndex) {
            val distanceIJ = points[i].distance(points[j])
            if (distance < distanceIJ) {
                p1 = points[i]
                p2 = points[j]
                distance = distanceIJ
            }
        }
    return Segment(p1, p2)
}

/**
 * Простая (2 балла)
 *
 * Построить окружность по её диаметру, заданному двумя точками
 * Центр её должен находиться посередине между точками, а радиус составлять половину расстояния между ними
 */
fun circleByDiameter(diameter: Segment): Circle = Circle(
    diameter.medium(),
    diameter.begin.distance(diameter.end) / 2
)

/**
 * Прямая, заданная точкой point и углом наклона angle (в радианах) по отношению к оси X.
 * Уравнение прямой: (y - point.y) * cos(angle) = (x - point.x) * sin(angle)
 * или: y * cos(angle) = x * sin(angle) + b, где b = point.y * cos(angle) - point.x * sin(angle).
 * Угол наклона обязан находиться в диапазоне от 0 (включительно) до PI (исключительно).
 */
class Line private constructor(val b: Double, val angle: Double) {
    init {
        require(angle >= 0 && angle < PI) { "Incorrect line angle: $angle" }
    }

    constructor(point: Point, angle: Double) : this(point.y * cos(angle) - point.x * sin(angle), angle)

    /**
     * Средняя (3 балла)
     *
     * Найти точку пересечения с другой линией.
     * Для этого необходимо составить и решить систему из двух уравнений (каждое для своей прямой)
     */
    fun crossPoint(other: Line): Point {
        val denominator = sin(other.angle) * cos(angle) - sin(angle) * cos(other.angle)
        return Point(
            (b * cos(other.angle) - other.b * cos(angle)) / denominator,
            (b * sin(other.angle) - other.b * sin(angle)) / denominator
        )
    }

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${cos(angle)} * y = ${sin(angle)} * x + $b)"
}

/**
 * Средняя (3 балла)
 *
 * Построить прямую по отрезку
 */
fun lineBySegment(s: Segment): Line = lineByPoints(s.begin, s.end)

/**
 * Средняя (3 балла)
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line = Line(
    a, (atan2(b.y - a.y, b.x - a.x) + PI) % PI
)

/**
 * Сложная (5 баллов)
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line {
    val line = lineByPoints(a, b)
    val angle = abs(line.angle + PI / 2) % PI
    return Line(Segment(a, b).medium(), angle)
}

/**
 * Средняя (3 балла)
 *
 * Задан список из n окружностей на плоскости.
 * Найти пару наименее удалённых из них; расстояние между окружностями
 * рассчитывать так, как указано в Circle.distance.
 *
 * При наличии нескольких наименее удалённых пар,
 * вернуть первую из них по порядку в списке circles.
 *
 * Если в списке менее двух окружностей, бросить IllegalArgumentException
 */
fun findNearestCirclePair(vararg circles: Circle): Pair<Circle, Circle> {
    if (circles.size < 2) throw IllegalArgumentException()
    var c1 = circles[0]
    var c2 = circles[1]
    var distance = c1.distance(c2)
    for (i in 0..circles.lastIndex)
        for (j in i + 1..circles.lastIndex) {
            val distanceIJ = circles[i].distance(circles[j])
            if (distance > distanceIJ) {
                c1 = circles[i]
                c2 = circles[j]
                distance = distanceIJ
            }
        }
    return Pair(c1, c2)
}

/**
 * Сложная (5 баллов)
 *
 * Дано три различные точки. Построить окружность, проходящую через них
 * (все три точки должны лежать НА, а не ВНУТРИ, окружности).
 * Описание алгоритмов см. в Интернете
 * (построить окружность по трём точкам, или
 * построить окружность, описанную вокруг треугольника - эквивалентная задача).
 */
fun circleByThreePoints(a: Point, b: Point, c: Point): Circle {
    val m1 = bisectorByPoints(a, b)
    val m2 = bisectorByPoints(b, c)
    val o = m1.crossPoint(m2)
    val r = o.distance(a)
    return Circle(o, r)
}

/**
 * Очень сложная (10 баллов)
 *
 * Дано множество точек на плоскости. Найти круг минимального радиуса,
 * содержащий все эти точки. Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит одну точку, вернуть круг нулевого радиуса с центром в данной точке.
 *
 * Примечание: в зависимости от ситуации, такая окружность может либо проходить через какие-либо
 * три точки данного множества, либо иметь своим диаметром отрезок,
 * соединяющий две самые удалённые точки в данном множестве.
 */

fun minContainingCircle(vararg points: Point): Circle {
    if (points.isEmpty()) throw  IllegalArgumentException()
    if (points.size == 1) return Circle(points[0], 0.0)
    val open = points.toMutableSet()
    val close = mutableSetOf<Point>()

    fun newPoint(point: Point): Point {
        open.remove(point)
        close.add(point)
        return point
    }

    val p1 = newPoint(open.random())
    val p2 = newPoint(open.random())
    var circle = circleByDiameter(Segment(p1, p2))
    while (open.isNotEmpty()) {
        val next = open.random()
        open.remove(next)
        if (!circle.contains(next))
            circle = minCircleWith1Point(next, close)
        close.add(next)
    }
    return circle
}

fun minCircleWith1Point(p: Point, s: Set<Point>): Circle {
    val open = s.toMutableSet()
    val close = mutableSetOf<Point>()
    val p1 = open.random()
    open.remove(p1)
    close.add(p1)
    var circle = circleByDiameter(Segment(p, p1))
    while (open.isNotEmpty()) {
        val next = open.random()
        open.remove(next)
        if (!circle.contains(next))
            circle = minCircleWith2Point(next, p, close)
        close.add(next)
    }
    return circle
}

fun minCircleWith2Point(p1: Point, p2: Point, s: Set<Point>): Circle {
    var circle = circleByDiameter(Segment(p1, p2))
    for (i in s) {
        if (!i.insideCircle(circle))
            circle = circleByThreePoints(i, p1, p2)
    }
    return circle
}