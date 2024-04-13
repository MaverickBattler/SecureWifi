package com.practice.securewifi.check.password_generation

import com.practice.securewifi.data.password_lists.entity.PlaceName

class PasswordGeneratorFromPlaceName: WordGenerator {

    /**
     * This function returns [passwordsAmount] or less generated passwords
     */
    fun generatePasswordsFromPlaceName(
        placeName: PlaceName,
        passwordsAmount: Int
    ): List<String> {
        if (passwordsAmount == 0) return emptyList()
        val generatedPasswords: MutableList<String> = mutableListOf()

        generatedPasswords += generateFromWordAndPopularNumbers(placeName.placeName)
        if (generatedPasswords.size >= passwordsAmount) {
            return generatedPasswords.subList(0, passwordsAmount)
        } else {
            val amountOfPasswordsToGenerate = passwordsAmount - generatedPasswords.size
            generatedPasswords += generateAllEightCharactersVariations(
                placeName.placeName,
                amountOfPasswordsToGenerate
            )
        }
        val distinctPasswords = generatedPasswords.distinct()
        return if (distinctPasswords.size >= passwordsAmount) {
            distinctPasswords.subList(0, passwordsAmount)
        } else {
            distinctPasswords
        }
    }
}