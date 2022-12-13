import java.io.BufferedReader
import java.util.*

fun main() {
    val lines = { ResourceReader.reader("day12.txt") }

    println(Day12().runPart01(lines))
    println(Day12().runPart02(lines))
}

class Day12 {
    fun runPart01(readerProvider: () -> BufferedReader): Int {
        val graph = parseGraph(readerProvider)

        return dijkstra(
            graph = graph,
            start = graph.vertices().find { it.name == 'S' }!!,
            finish = graph.vertices().find { it.name == 'E' }!!
        )
    }

    fun runPart02(readerProvider: () -> BufferedReader): Int {
        val graph = parseGraph(readerProvider)

        val startingPoints = graph.vertices().filter { it.name == 'S' || it.name == 'a' }
        val finish = graph.vertices().find { it.name == 'E' }!!

        return startingPoints
            .map { startingPoint ->
                graph.reset()
                dijkstra(graph, startingPoint, finish)
            }.minOf { it }
    }


    private fun dijkstra(graph: Graph, start: Graph.Vertex, finish: Graph.Vertex): Int {
        val queue = PriorityQueue<Graph.Vertex> { o1, o2 -> o1.distance - o2.distance }

        graph.vertices().forEach { vertex ->
            vertex.distance = Int.MAX_VALUE
            vertex.predecessor = null
            vertex.visited = false
        }

        start.also {
            it.distance = 0
            queue.offer(it)
        }

        while (queue.isNotEmpty()) {
            val vertex = queue.poll()
            vertex.visited = true

            val adjacentVertices = graph
                .verticesAdjacentTo(vertex)
                .filter { !it.visited }

            adjacentVertices.forEach { adjacent ->
                val newDistance = vertex.distance + 1

                if (newDistance < adjacent.distance) {
                    adjacent.distance = newDistance
                    adjacent.predecessor = vertex
                    queue.offer(adjacent)
                }
            }
        }

        return finish.distance
    }

    private fun parseGraph(readerProvider: () -> BufferedReader): Graph {
        val graph = Graph()
        val map: MutableList<MutableList<Char>> = mutableListOf(mutableListOf())

        var row = 0
        var column = 0

        readerProvider().use { reader ->
            var rawCharacter = reader.read()

            while (rawCharacter != -1) {
                val character = Char(rawCharacter)

                if (character.isLetter()) {
                    graph.addVertex(column to row, character)
                    map[row].add(character)
                    column++
                } else { // must be new line
                    row++
                    column = 0
                    map.add(mutableListOf())
                }

                rawCharacter = reader.read()
            }
        }

        val columns = map.first().size

        (0..row).forEach { y ->
            (0 until columns).forEach { x ->
                val vertex = graph.getVertex(x to y)!!

                listOfNotNull(up(vertex, graph), down(vertex, graph), left(vertex, graph), right(vertex, graph))
                    .forEach { adjacent ->
                        val heightDifference = heightDifference(vertex, adjacent)

                        if (heightDifference <= 1) {
                            graph.addEdge(vertex, adjacent, Math.abs(heightDifference))
                        }
                    }
            }
        }

        return graph
    }

    private fun heightDifference(from: Graph.Vertex, to: Graph.Vertex): Int {
        return actualHeight(to) - actualHeight(from)
    }

    private fun actualHeight(vertex: Graph.Vertex): Int = when (val name = vertex.name) {
        'S' -> 'a'.code
        'E' -> 'z'.code
        else -> name.code
    }

    private fun up(vertex: Graph.Vertex, graph: Graph): Graph.Vertex? =
        graph.getVertex(vertex.coords.first to vertex.coords.second - 1)

    private fun down(vertex: Graph.Vertex, graph: Graph): Graph.Vertex? =
        graph.getVertex(vertex.coords.first to vertex.coords.second + 1)

    private fun left(vertex: Graph.Vertex, graph: Graph): Graph.Vertex? =
        graph.getVertex(vertex.coords.first - 1 to vertex.coords.second)

    private fun right(vertex: Graph.Vertex, graph: Graph): Graph.Vertex? =
        graph.getVertex(vertex.coords.first + 1 to vertex.coords.second)

    class Graph(
        private val vertices: MutableMap<Pair<Int, Int>, Vertex> = mutableMapOf(),
        private val edges: MutableMap<Vertex, MutableMap<Vertex, Int>> = mutableMapOf(),
    ) {
        class Vertex(
            val coords: Pair<Int, Int>,
            val name: Char,
            var distance: Int = Integer.MAX_VALUE,
            var predecessor: Vertex? = null,
            var visited: Boolean = false
        ) : Comparable<Vertex> {
            fun reset() {
                this.distance = Integer.MAX_VALUE
                this.predecessor = null
                this.visited = false
            }

            override fun compareTo(other: Vertex): Int {
                return this.distance.compareTo(other.distance)
            }

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Vertex

                if (coords != other.coords) return false

                return true
            }

            override fun hashCode(): Int {
                return coords.hashCode()
            }

            override fun toString(): String {
                return "($coords) -> $distance"
            }
        }

        fun addVertex(coords: Pair<Int, Int>, name: Char) {
            vertices[coords] = Vertex(coords = coords, name = name)
        }

        fun vertices(): List<Vertex> = vertices.values.toList()

        fun verticesAdjacentTo(vertex: Vertex): Set<Vertex> {
            return edges[vertex]?.keys ?: emptySet()
        }

        fun addEdge(from: Vertex, to: Vertex, distance: Int) {
            edges.compute(
                from
            ) { _, currentMap ->
                if (currentMap == null) {
                    mutableMapOf(to to distance)
                } else {
                    currentMap[to] = distance
                    currentMap
                }
            }
        }

        fun getVertex(coords: Pair<Int, Int>): Vertex? {
            return vertices[coords]
        }

        fun reset() {
            vertices.values.forEach { it.reset() }
        }
    }
}