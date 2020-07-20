package io.github.zwieback.familyfinance.extension

import org.junit.Assert.assertEquals
import org.junit.Test

class TransliterationExtensionsTest {

    @Test
    fun `nullable string should return empty string`() {
        assertEquals("", null.transliterate())
    }

    @Test
    fun `non nullable string should return transliterated string`() {
        val sourceText = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя" +
                "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
        val expectedText = "abvgdeezhziiklmnoprstufkhtschshshchieyeiuia" +
                "ABVGDEEZHZIIKLMNOPRSTUFKHTSCHSHSHCHIEYEIUIA"
        assertEquals(expectedText, sourceText.transliterate())
    }
}
