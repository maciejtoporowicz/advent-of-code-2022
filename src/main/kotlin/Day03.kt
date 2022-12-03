import java.util.stream.Stream

fun main() {
    val lines = { ResourceReader.read("day03.txt") }

    println(Day03().runPart01(lines()))
    println(Day03().runPart02(lines()))
}

class Day03 {
    fun runPart01(lines: Stream<String>): Int {
        var sumOfPriorities = 0

        lines.forEach RucksackLoop@ { line ->
            val itemsInEachCompartment = line.length / 2
            val foundItems = mutableSetOf<Char>()

            line.forEachIndexed ItemLoop@ { itemIndex, item ->
                if(doesItemBelongToFirstCompartment(itemIndex, itemsInEachCompartment)) {
                    foundItems.add(item)
                    return@ItemLoop
                }

                if(foundItems.contains(item)) {
                    sumOfPriorities += priority(item)
                    return@RucksackLoop
                }
            }
        }

        return sumOfPriorities
    }

    private fun doesItemBelongToFirstCompartment(zeroBasedItemIndex: Int, numOfItemsInCompartment: Int): Boolean {
        return zeroBasedItemIndex < numOfItemsInCompartment
    }

    fun runPart02(lines: Stream<String>): Int {
        var sumOfPriorities = 0

        var commonItemsInGroup = mutableSetOf<Char>()
        var elfNumber = 1

        lines.forEach RucksackLoop@ { line ->
            val commonItemsForGroupAndLine = mutableSetOf<Char>()

            line.forEach { item ->
                if(elfNumber == 1) {
                    commonItemsForGroupAndLine.add(item)
                }

                if(commonItemsInGroup.contains(item)) {
                    commonItemsForGroupAndLine.add(item)
                }
            }

            commonItemsInGroup = commonItemsForGroupAndLine

            if(elfNumber == 3) {
                sumOfPriorities += priority(commonItemsInGroup.first())
                elfNumber = 1
            } else {
                elfNumber++
            }
        }

        return sumOfPriorities
    }

    fun priority(char: Char): Int {
        return if (char.isLowerCase()) {
            char.code - 96
        } else {
            char.code - 38
        }
    }
}