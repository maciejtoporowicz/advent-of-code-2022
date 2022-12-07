import java.util.*
import java.util.stream.Stream

fun main() {
    val lines = { ResourceReader.readLines("day07.txt") }

    println(Day07().runPart01(lines()))
    println(Day07().runPart02(lines()))
}

class Day07 {
    fun runPart01(lines: Stream<String>): Int {
        val root = parseFileSystem(lines)

        var sumOfSizesOfDirectoriesUpTo10000InSize = 0

        root.walkThroughDirectoriesRecursively { currentDir ->
            val sizeOfCurrentDir = currentDir.size()
            if (sizeOfCurrentDir <= 100000) {
                sumOfSizesOfDirectoriesUpTo10000InSize += sizeOfCurrentDir
            }
        }

        return sumOfSizesOfDirectoriesUpTo10000InSize
    }

    fun runPart02(lines: Stream<String>): Int {
        val root = parseFileSystem(lines)

        val directoriesWhichWouldFreeUpEnoughSpace = TreeMap<Int, Item.Directory>()

        val totalSpace = 70000000
        val currentlyUsedSpace = root.size()
        val spaceRequired = 30000000

        val minimumDirectorySizeWhichFreesEnoughSpace = spaceRequired - (totalSpace - currentlyUsedSpace)

        root.walkThroughDirectoriesRecursively { currentDir ->
            val sizeOfCurrentDir = currentDir.size()
            if (sizeOfCurrentDir >= minimumDirectorySizeWhichFreesEnoughSpace) {
                directoriesWhichWouldFreeUpEnoughSpace[sizeOfCurrentDir] = currentDir
            }
        }

        return directoriesWhichWouldFreeUpEnoughSpace.firstKey()
    }

    private fun parseFileSystem(lines: Stream<String>): Item.Directory {
        var currentDir: Item.Directory? = null

        lines.forEach { line ->
            if (isItCommand(line)) {
                val command = line.substring(2)
                if (command.startsWith("cd")) {
                    val dirName = command.substring(3)
                    if (dirName == "..") {
                        currentDir = currentDir!!.parent
                    } else {
                        currentDir = if (currentDir == null) {
                            Item.Directory(name = dirName)
                        } else {
                            currentDir!!
                                .content
                                .filterIsInstance<Item.Directory>()
                                .find { it.name == dirName }!!
                        }
                    }
                }
            } else {
                val fileListingRegex = Regex("^(\\d+) (.*)")
                if (line.matches(fileListingRegex)) {
                    val (fileSize, fileName) = fileListingRegex
                        .find(line)!!
                        .groupValues
                        .let { Integer.parseInt(it[1]) to it[2] }

                    currentDir!!.addFile(fileName, fileSize)
                } else {
                    val dirName = line.replace("dir ", "")
                    currentDir!!.addDir(dirName)
                }
            }
        }

        while (currentDir!!.name != "/") {
            currentDir = currentDir!!.parent
        }

        return currentDir!!
    }

    private fun isItCommand(line: String) = line.startsWith("$")

    sealed class Item {
        data class File(val name: String, val size: Int) : Item()

        class Directory(
            val name: String,
            val content: MutableList<Item> = mutableListOf(),
            val parent: Directory? = null
        ) : Item() {
            fun addDir(name: String): Directory {
                val directory = Directory(name, mutableListOf(), this)
                this.content.add(directory)
                return directory
            }

            fun addFile(name: String, size: Int): File {
                val file = File(name, size)
                this.content.add(file)
                return file
            }

            fun size(): Int {
                return this.content.sumOf {
                    when (it) {
                        is File -> it.size
                        is Directory -> it.size()
                    }
                }
            }

            fun walkThroughDirectoriesRecursively(visit: (item: Directory) -> Unit) {
                visit(this)
                content
                    .filterIsInstance<Directory>()
                    .forEach { item -> item.walkThroughDirectoriesRecursively(visit) }
            }
        }
    }
}