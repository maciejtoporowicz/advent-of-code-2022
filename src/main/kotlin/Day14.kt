import Day14.Point.Companion.p
import java.lang.Integer.max
import java.lang.Integer.min
import java.util.stream.Stream

fun main() {
    val lines = { ResourceReader.readLines("day14.txt") }

    println(Day14().runPart01(lines))
    println(Day14().runPart02(lines))
}

class Day14 {
    fun runPart01(lines: () -> Stream<String>): Int {
        val (sandPouringPoint, mapTemplate) = parseMap(lines())
        val bounds = findMapBounds(mapTemplate)

        val map = mapTemplate.toMutableMap()

        var fallingIntoAbyss = false
        var numOfRestingSandParticles = 0
        var currentSandPosition = sandPouringPoint

        while (!fallingIntoAbyss) {
            val down = map[currentSandPosition.down()]
            if (isBlocked(down)) {
                val downLeft = map[currentSandPosition.downLeft()]

                if (isBlocked(downLeft)) {
                    val downRight = map[currentSandPosition.downRight()]

                    if (isBlocked(downRight)) {
                        map[currentSandPosition] = Particle.SAND
                        numOfRestingSandParticles++
                        currentSandPosition = sandPouringPoint
                    } else {
                        currentSandPosition = currentSandPosition.downRight()
                    }
                } else {
                    currentSandPosition = currentSandPosition.downLeft()
                }
            } else {
                currentSandPosition = currentSandPosition.down()
            }

            if (!bounds.inBounds(currentSandPosition)) {
                fallingIntoAbyss = true
            }
        }

        return numOfRestingSandParticles
    }

    fun runPart02(lines: () -> Stream<String>): Int {
        val (sandPouringPoint, mapTemplate) = parseMap(lines())
        val bounds = findMapBounds(mapTemplate)

        val map = mapTemplate.toMutableMap()

        val floorY = bounds.max.y + 2

        var noMoreSandCanPour = false
        var numOfRestingSandParticles = 0
        var currentSandPosition = sandPouringPoint

        while (!noMoreSandCanPour) {
            val pointDown = currentSandPosition.down()
            val down = map[pointDown]

            if (pointDown.y == floorY) {
                map[currentSandPosition] = Particle.SAND
                numOfRestingSandParticles++
                currentSandPosition = sandPouringPoint
                continue
            }

            if (isBlocked(down)) {
                val downLeft = map[currentSandPosition.downLeft()]

                if (isBlocked(downLeft)) {
                    val downRight = map[currentSandPosition.downRight()]

                    if (isBlocked(downRight)) {
                        map[currentSandPosition] = Particle.SAND
                        numOfRestingSandParticles++

                        if (currentSandPosition == sandPouringPoint) {
                            noMoreSandCanPour = true
                        } else {
                            currentSandPosition = sandPouringPoint
                        }
                    } else {
                        currentSandPosition = currentSandPosition.downRight()
                    }
                } else {
                    currentSandPosition = currentSandPosition.downLeft()
                }
            } else {
                currentSandPosition = currentSandPosition.down()
            }
        }

        return numOfRestingSandParticles
    }

    private fun isBlocked(down: Particle?) = down == Particle.ROCK || down == Particle.SAND

    private fun parseMap(lines: Stream<String>): Pair<Point, Map<Point, Particle>> {
        val map = mutableMapOf<Point, Particle>()

        val sandPouringPoint = p(500, 0)

        map[sandPouringPoint] = Particle.START

        lines.forEach { line ->
            val rawCoords = line.split(" -> ")

            val coords = rawCoords.map { coords -> coords.split(",").let { p(it[0].toInt(), it[1].toInt()) } }

            if (coords.size == 1) {
                map[coords.first()] = Particle.ROCK
            } else {
                (0 until coords.size - 1).forEach { index ->
                    range(coords[index], coords[index + 1]).forEach { point -> map[point] = Particle.ROCK }
                }
            }
        }

        return sandPouringPoint to map.toMap()
    }

    private fun findMapBounds(map: Map<Point, Particle>): MapBounds {
        var minX = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var minY = Int.MAX_VALUE
        var maxY = Int.MIN_VALUE

        map.keys.forEach { (x, y) ->
            if (x < minX) {
                minX = x
            } else if (x > maxX) {
                maxX = x
            }

            if (y < minY) {
                minY = y
            } else if (y > maxY) {
                maxY = y
            }
        }

        return MapBounds(
            min = Point(minX, minY),
            max = Point(maxX, maxY)
        )
    }

    private fun range(from: Point, to: Point): List<Point> {
        val (fromX, fromY) = from
        val (toX, toY) = to

        return if (fromX == toX) {
            IntRange(min(fromY, toY), max(fromY, toY))
                .map { y -> Point(fromX, y) }
        } else if (fromY == toY) {
            IntRange(min(fromX, toX), max(fromX, toX))
                .map { x -> Point(x, fromY) }
        } else {
            throw RuntimeException("Unexpected range params $from -> $to")
        }
    }

    enum class Particle {
        SAND, ROCK, START
    }

    data class MapBounds(val min: Point, val max: Point) {
        fun inBounds(coords: Point): Boolean {
            if (coords.x < min.x) {
                return false
            }
            if (coords.x > max.x) {
                return false
            }
            if (coords.y < min.y) {
                return false
            }
            if (coords.y > max.y) {
                return false
            }
            return true
        }
    }

    data class Point(val x: Int, val y: Int) {
        fun down() = copy(y = y + 1)
        fun downLeft() = copy(x = x - 1, y = y + 1)
        fun downRight() = copy(x = x + 1, y = y + 1)

        companion object {
            fun p(x: Int, y: Int) = Point(x, y)
        }
    }
}