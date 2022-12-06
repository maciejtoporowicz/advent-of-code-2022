import java.io.Reader

fun main() {
    val lines = { ResourceReader.reader("day06.txt") }

    println(Day06().runPart01(lines))
    println(Day06().runPart02(lines))
}

class Day06 {
    fun runPart01(readerProvider: () -> Reader): Int {
        return findPositionOfDistinctCharacters(4, readerProvider)
    }

    fun runPart02(readerProvider: () -> Reader): Int {
        return findPositionOfDistinctCharacters(14, readerProvider)
    }

    private fun findPositionOfDistinctCharacters(numOfDistinctCharacters: Int, readerProvider: () -> Reader): Int {
        return readerProvider().use { reader ->
            var character: Int = reader.read()
            val lastReadCharactersCharacters = mutableListOf<Int>()
            var characterPosition = 1

            while (character != -1) {
                if (lastReadCharactersCharacters.size == numOfDistinctCharacters) {
                    lastReadCharactersCharacters.removeFirst()
                }

                lastReadCharactersCharacters.add(character)

                if (lastReadCharactersCharacters.toSet().size == numOfDistinctCharacters) {
                    return@use characterPosition
                }

                character = reader.read()
                characterPosition++
            }

            throw RuntimeException("Not found")
        }
    }
}
