import java.io.BufferedReader
import kotlin.math.max

fun main() {
    val lines = { ResourceReader.reader("day08.txt") }

    println(Day08().runPart01(lines))
    println(Day08().runPart02(lines))
}

typealias Row = Int
typealias Column = Int
typealias Height = Int

class Day08 {
    /**
     * I could load all these into memory at once, but I felt like it's more interesting to try to limit the number
     * of loops.
     *
     * The idea is to go tree by tree from the top left corner. If a tree is not visible from top and/or left,
     * remember it to check it later if it's visible from the right and/or bottom. The visibility from the right and/or
     * bottom can be evaluated when a tree to the right/to the bottom is reached.
     */
    fun runPart01(readerProvider: () -> BufferedReader): Int {
        var column = 0
        var row = 0

        var numberOfVisibleTrees = 0

        val possiblyVisibleTrees = mutableMapOf<DirectionOfPotentialVisibility, MutableList<Tree>>()
        val highestTreePerRowGoingFromLeft = mutableMapOf<Row, Tree>()
        val highestTreePerColumnGoingFromTop = mutableMapOf<Column, Tree>()

        readerProvider().use { reader ->
            var rawCharacter = reader.read()

            while (rawCharacter != -1) {
                val character = Char(rawCharacter)
                if (character.isDigit()) {
                    val tree = Tree(column = column, row = row, height = character.digitToInt())

                    var isVisible = false

                    if (tree.height > (highestTreePerRowGoingFromLeft[row]?.height ?: -1)) {
                        isVisible = true
                        highestTreePerRowGoingFromLeft[row] = tree
                    }
                    if (tree.height > (highestTreePerColumnGoingFromTop[column]?.height ?: -1)) {
                        isVisible = true
                        highestTreePerColumnGoingFromTop[column] = tree
                    }

                    possiblyVisibleTrees[DirectionOfPotentialVisibility.FROM_RIGHT]
                        ?.removeAll { sameRowAndSmallerOrSameHeight(it, tree) }
                    possiblyVisibleTrees[DirectionOfPotentialVisibility.FROM_BOTTOM]
                        ?.removeAll { sameColumnAndSmallerOrSameHeight(it, tree) }

                    if (isVisible) {
                        numberOfVisibleTrees++
                    } else {
                        possiblyVisibleTrees.compute(
                            DirectionOfPotentialVisibility.FROM_RIGHT
                        ) { _, trees -> trees?.also { it.add(tree) } ?: mutableListOf(tree) }

                        possiblyVisibleTrees.compute(
                            DirectionOfPotentialVisibility.FROM_BOTTOM
                        ) { _, trees -> trees?.also { it.add(tree) } ?: mutableListOf(tree) }
                    }

                    column++
                } else {
                    column = 0
                    row++
                }

                rawCharacter = reader.read()
            }
        }

        numberOfVisibleTrees += possiblyVisibleTrees
            .flatMap { it.value }
            .toSet()
            .count()

        return numberOfVisibleTrees
    }

    /**
    * The easy way - read all into memory at once and go tree by tree and look up/down/left/right.
    **/
    fun runPart02(readerProvider: () -> BufferedReader): Int {
        var highestScenicScore = -1

        val forest = parseForest(readerProvider)

        for (row in 0 until forest.numOfRows) {
            for (column in 0 until forest.numOfColumns) {
                forest
                    .scenicScoreForTreeAt(column to row)
                    .also { if (it > highestScenicScore) highestScenicScore = it }
            }
        }
        return highestScenicScore
    }

    private fun parseForest(readerProvider: () -> BufferedReader): Forest {
        var column = 0
        var row = 0
        var numOfColumns = -1

        val trees = mutableListOf<Height>()

        readerProvider().use { reader ->
            var rawCharacter = reader.read()

            while (rawCharacter != -1) {
                val character = Char(rawCharacter)
                if (character.isDigit()) {
                    trees.add(character.digitToInt())
                    column++
                } else {
                    numOfColumns = max(numOfColumns, column)

                    column = 0
                    row++
                }

                rawCharacter = reader.read()
            }
        }

        return Forest(trees = trees, numOfColumns = numOfColumns, numOfRows = row + 1)
    }

    private fun sameRowAndSmallerOrSameHeight(treeFromSameRow: Tree, tree: Tree) =
        treeFromSameRow.column == tree.column && treeFromSameRow.height <= tree.height

    private fun sameColumnAndSmallerOrSameHeight(treeFromSameColumn: Tree, tree: Tree) =
        treeFromSameColumn.row == tree.row && treeFromSameColumn.height <= tree.height
}

enum class DirectionOfPotentialVisibility {
    FROM_RIGHT, FROM_BOTTOM
}

data class Forest(val trees: List<Height>, val numOfColumns: Int, val numOfRows: Int) {
    fun scenicScoreForTreeAt(coordinates: Pair<Column, Row>): Int {
        val treesVisibleUp =
            numOfTreesVisibleFrom(coordinates, heightOfTreeAt(coordinates)) { (col, row) -> col to row - 1 }
                .also { if (it == 0) return 0 }
        val treesVisibleDown =
            numOfTreesVisibleFrom(coordinates, heightOfTreeAt(coordinates)) { (col, row) -> col to row + 1 }
                .also { if (it == 0) return 0 }
        val treesVisibleLeft =
            numOfTreesVisibleFrom(coordinates, heightOfTreeAt(coordinates)) { (col, row) -> col - 1 to row }
                .also { if (it == 0) return 0 }
        val treesVisibleRight =
            numOfTreesVisibleFrom(coordinates, heightOfTreeAt(coordinates)) { (col, row) -> col + 1 to row }
                .also { if (it == 0) return 0 }

        return treesVisibleUp * treesVisibleDown * treesVisibleLeft * treesVisibleRight
    }

    private fun numOfTreesVisibleFrom(
        coordinates: Pair<Column, Row>, height: Int,
        coordinatesOfNextTreeInRelationTo: (Pair<Column, Row>) -> Pair<Column, Row>
    ): Int {
        var numOfTrees = 0

        var coordinatesOfCurrentlyAnalyzedTree = coordinatesOfNextTreeInRelationTo(coordinates)

        var countFinished = false

        while (!countFinished) {
            if (isOutOfForest(coordinatesOfCurrentlyAnalyzedTree)) {
                countFinished = true
            } else {
                numOfTrees++

                if (heightOfTreeAt(coordinatesOfCurrentlyAnalyzedTree) >= height) {
                    countFinished = true
                }

                coordinatesOfCurrentlyAnalyzedTree =
                    coordinatesOfNextTreeInRelationTo(coordinatesOfCurrentlyAnalyzedTree)
            }
        }

        return numOfTrees
    }

    private fun isOutOfForest(coordinates: Pair<Column, Row>): Boolean {
        return coordinates.first < 0
                || coordinates.first >= numOfColumns
                || coordinates.second < 0
                || coordinates.second >= numOfRows
    }

    private fun heightOfTreeAt(coordinates: Pair<Column, Row>): Height {
        val index = (coordinates.second * numOfColumns) + coordinates.first
        return trees[index]
    }
}

data class Tree(val column: Int, val row: Int, val height: Int)