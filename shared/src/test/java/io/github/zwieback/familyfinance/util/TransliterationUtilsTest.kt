package io.github.zwieback.familyfinance.util

import org.junit.Assert.assertEquals
import org.junit.Test

class TransliterationUtilsTest {

    @Test
    fun `nullable string should return empty string`() {
        assertEquals("", TransliterationUtils.transliterate(null))
    }

    @Test
    fun `non nullable string should return transliterated string`() {
        val sourceText = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя" +
                "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
        val expectedText = "abvgdeezhziiklmnoprstufkhtschshshchieyeiuia" +
                "ABVGDEEZHZIIKLMNOPRSTUFKHTSCHSHSHCHIEYEIUIA"
        assertEquals(expectedText, TransliterationUtils.transliterate(sourceText))
    }
}
