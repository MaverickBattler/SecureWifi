package com.practice.securewifi.check.password_generation

import com.practice.securewifi.data.password_lists.entity.PersonInfo

class PasswordGeneratorFromPersonInfo : WordGenerator {

    /**
     * This function returns [passwordsAmount] or less generated passwords
     */
    fun generatePasswordsFromPersonInfo(
        personInfo: PersonInfo,
        passwordsAmount: Int
    ): List<String> {
        if (passwordsAmount == 0) return emptyList()
        val generatedPasswords: MutableList<String> = mutableListOf()
        if (personInfo.day != null && personInfo.month != null && personInfo.year != null) {
            if (personInfo.year.toString().length == 4) {
                val firstPart = if (personInfo.day >= 10) {
                    personInfo.day.toString()
                } else {
                    "0" + personInfo.day.toString()
                }
                val secondPart = if (personInfo.month >= 10) {
                    personInfo.month.toString()
                } else {
                    "0" + personInfo.month.toString()
                }
                val thirdPart = personInfo.year
                val date = firstPart + secondPart + thirdPart
                generatedPasswords.add(date)
                if (personInfo.name != null) {
                    generatedPasswords.add(personInfo.name + date)
                    generatedPasswords.add(personInfo.name.replaceFirstChar { it.uppercase() } + date)
                    generatedPasswords.add(date + personInfo.name)
                }
                if (personInfo.name != null && personInfo.secondName != null && personInfo.fatherOrMiddleName != null) {
                    val nameWithInitialsAsSuffix =
                        personInfo.secondName + personInfo.name.first() + personInfo.fatherOrMiddleName.first()
                    val nameWithInitialsAsPrefix = personInfo.name.first()
                        .toString() + personInfo.fatherOrMiddleName.first() + personInfo.secondName
                    generatedPasswords.add(nameWithInitialsAsSuffix + personInfo.year)
                    generatedPasswords.add(nameWithInitialsAsPrefix + personInfo.year)
                }
            }

        }

        if (personInfo.name != null && personInfo.secondName != null && personInfo.fatherOrMiddleName != null) {
            val nameWithInitialsAsSuffix =
                personInfo.secondName + personInfo.name.first() + personInfo.fatherOrMiddleName.first()
            val nameWithInitialsAsPrefix = personInfo.name.first()
                .toString() + personInfo.fatherOrMiddleName.first() + personInfo.secondName
            if (nameWithInitialsAsSuffix.length >= 8) {
                generatedPasswords.add(nameWithInitialsAsSuffix)
                generatedPasswords.add(nameWithInitialsAsPrefix)
            }
        }

        if (personInfo.name != null) {
            generatedPasswords += generateFromWordAndPopularNumbers(personInfo.name)
            generatedPasswords += generateFromWordAndPopularNumbers(personInfo.name.replaceFirstChar { it.uppercase() })
        }
        /*if (personInfo.secondName != null) {
            generatedPasswords += generateFromWordAndPopularNumbers(personInfo.secondName)
        }*/
        if (generatedPasswords.size >= passwordsAmount) {
            return generatedPasswords.subList(0, passwordsAmount)
        } else {
            val amountOfPasswordsToGenerate = passwordsAmount - generatedPasswords.size
            if (personInfo.name != null) {
                generatedPasswords += generateAllEightCharactersVariations(
                    personInfo.name,
                    amountOfPasswordsToGenerate
                )
            }
        }
        val distinctPasswords = generatedPasswords.distinct()
        return if (distinctPasswords.size >= passwordsAmount) {
            distinctPasswords.subList(0, passwordsAmount)
        } else {
            distinctPasswords
        }
    }
}