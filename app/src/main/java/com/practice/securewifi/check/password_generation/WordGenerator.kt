package com.practice.securewifi.check.password_generation

import java.lang.StringBuilder

interface WordGenerator {

    fun generateFromWordAndPopularNumbers(word: String): List<String> {
        val generatedPasswords: MutableList<String> = mutableListOf()
        val wordLength = word.length
        if (wordLength < 8) {
            generatedPasswords.add(word + generateWordFromRepeatingDigit('1', 8 - wordLength))
            generatedPasswords.add(word + generateWordFromRepeatingDigit('7', 8 - wordLength))
            generatedPasswords.add(word + "12345678".substring(0, 8 - wordLength - 1))
            generatedPasswords.add(word + "12345678".substring(0, 8 - wordLength - 1).reversed())
        }
        if (wordLength >= 6) {
            generatedPasswords.add(word + "69")
            generatedPasswords.add(word + "228")
        }
        val years = listOf(
            "2024", "2023", "2022", "2021", "2020", "2019",
            "2018", "2017", "2016", "2015", "2014", "2013",
            "2012", "2011", "2010", "2009", "2008", "2007",
            "2006", "2005", "2004", "2003", "2002", "2001",
            "2000"
        )
        years.forEach {
            generatedPasswords.add(word + it)
        }
        return generatedPasswords
    }

    fun generateWordFromRepeatingDigit(digit: Char, count: Int): String {
        return StringBuilder().apply {
            repeat(count) {
                append(digit)
            }
        }.toString()
    }

    /** this function does not guarantee distinct values and correct amount of passwords returned */
    fun generateAllEightCharactersVariations(word: String, amount: Int): List<String> {
        val amountToFill = 8 - word.length
        if (amountToFill < 1) return emptyList() // nothing to generate
        val generatedPasswords: MutableList<String> = mutableListOf()

        for (i in 0..9) {
            generatedPasswords.add(generateWordFromRepeatingDigit(i.toChar(), amountToFill))
            if (generatedPasswords.size == amount) {
                return generatedPasswords
            }
        }

        // cast will always succeed
        val endNumber = "99999999".substring(0, amountToFill).toInt()
        for (i in 0..endNumber) {
            val strNumber = i.toString()
            // fillPrefix is needed for situations when, for example, we want to generate
            // name12, but it will be less than 8 digits, so we need to generate name0012
            val fillPrefix = generateWordFromRepeatingDigit('0', amountToFill - strNumber.length)
            generatedPasswords.add(word + fillPrefix + strNumber)
            // if generated too much passwords (+1000 is for repeating passwords, that will be excluded later)
            if (generatedPasswords.size == amount + 1000) {
                return generatedPasswords
            }
        }

        return generatedPasswords
    }
}