package com.javix.wordflipster

import org.junit.Assert.assertEquals
import org.junit.Test

class EncodeWordTests {

    @Test
    fun `Test empty input returns -1`() {
        val mapping = getMapping()
        val emptyMessage = encodeWord("", mapping)
        assertEquals(listOf(-1), emptyMessage)
    }

    @Test
    fun `Test all letters mapped`() {
        val message = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val mapping = getMapping()
        val encoded = encodeWord(message, mapping)
        assertEquals(26, encoded.size)
    }


    @Test
    fun `Test duplicate letters in the mapping`() {
        val mapping = mapOf(1 to 'A', 2 to 'B', 3 to 'A')
        val message = "AA"
        val encoded = encodeWord(message, mapping)
        assertEquals(listOf(1, 1), encoded)
    }

    @Test
    fun `Test case sensitivity`() {
        val message = "hello"
        val mapping = getMapping()
        val encoded = encodeWord(message, mapping)
        assertEquals(message.uppercase().length, encoded.size)
    }

    @Test
    fun `Test special characters in mapping`() {
        val mapping = getMapping()
        val message = "@#$"
        val encoded = encodeWord(message, mapping)
        assertEquals(listOf(-1, -1, -1), encoded)
    }
    @Test
    fun `Test space characters in mapping`() {
        val mapping = getMapping()
        val message = " "
        val encoded = encodeWord(message, mapping)
        assertEquals(listOf(-1), encoded)
    }

    @Test
    fun `Test wrap-around mapping`() {
        val mapping = mapOf(1 to 'Z', 2 to 'A', 3 to 'B') // Wraps around
        val message = "ZAB"
        val encoded = encodeWord(message, mapping)
        assertEquals(listOf(1, 2, 3), encoded)
    }

    @Test
    fun `Test repeated letters`() {
        val message = "AAAA"
        val mapping = getMapping()
        val encoded = encodeWord(message, mapping)
        assertEquals(message.length, encoded.size)
    }

    @Test
    fun `Test performance with long input`() {
        val message = "A".repeat(1000)
        val mapping = getMapping()
        val encoded = encodeWord(message, mapping)
        assertEquals(1000, encoded.size)
    }

    @Test
    fun `Test non-randomized mapping`() {
        val mapping = (1..26).associateWith { ('A' + it - 1).toChar() } // A = 1, B = 2, ...
        val message = "ABC"
        val encoded = encodeWord(message, mapping)
        assertEquals(listOf(1, 2, 3), encoded)
    }
}
