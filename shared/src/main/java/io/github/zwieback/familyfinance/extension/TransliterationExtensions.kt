package io.github.zwieback.familyfinance.extension

private val CYRILLIC_ALPHABET = listOf(
    'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й',
    'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф',
    'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я',
    'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й',
    'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф',
    'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'
)

/**
 * As standard used International Civil Aviation Organization (ICAO)
 *
 * See [Doc 9303, 15 MB](https://www.icao.int/publications/Documents/9303_p3_cons_ru.pdf)
 */
private val LATIN_ALPHABET = listOf(
    "a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "i",
    "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f",
    "kh", "ts", "ch", "sh", "shch", "ie", "y", "", "e", "iu", "ia",
    "A", "B", "V", "G", "D", "E", "E", "ZH", "Z", "I", "I",
    "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F",
    "KH", "TS", "CH", "SH", "SHCH", "IE", "Y", "", "E", "IU", "IA"
)

/**
 * Transliterate all cyrillic characters into latin analog.
 *
 * @return transliterated text
 */
fun String?.transliterate(): String {
    return this
        ?.map { char -> char.transliterate() }
        ?.joinToString(separator = "")
        .orEmpty()
}

private fun Char.transliterate(): String {
    val cyrillicIndex = CYRILLIC_ALPHABET.indexOf(this)
    return LATIN_ALPHABET.getOrElse(cyrillicIndex) { this.toString() }
}
