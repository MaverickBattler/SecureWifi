package com.practice.securewifi.check.password_generation

class PasswordGeneratorFromSsid: WordGenerator {

    /**
     * This function returns [passwordsAmount] or less generated passwords
     */
    fun generatePasswordsFromSsid(
        ssid: String,
        passwordsAmount: Int
    ): List<String> {
        if (passwordsAmount == 0) return emptyList()
        val generatedPasswords: MutableList<String> = mutableListOf()

        val ssidWithoutSpaces = ssid.replace(" ", "")
        generatedPasswords += generateFromWordAndPopularNumbers(ssidWithoutSpaces)
        if (generatedPasswords.size >= passwordsAmount) {
            return generatedPasswords.subList(0, passwordsAmount)
        } else {
            val amountOfPasswordsToGenerate = passwordsAmount - generatedPasswords.size
            generatedPasswords += generateAllEightCharactersVariations(
                ssidWithoutSpaces,
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