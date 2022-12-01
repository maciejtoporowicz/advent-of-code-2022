object ResourceReader {
    fun read(path: String) = this::class.java.getResourceAsStream(path)?.bufferedReader()?.lines()!!
}