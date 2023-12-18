package com.practice.securewifi.data.interactor

import com.practice.securewifi.data.repository.FixedPasswordListsRepository

class FixedPasswordListsInteractor(
    private val fixedPasswordListsRepository: FixedPasswordListsRepository
) {

    fun getFixedPasswordListsNames(): List<String> {
        return fixedPasswordListsRepository.getFixedPasswordListsNames()
    }

    fun getFixedPasswordListForShow(
        fixedPassword: FixedPasswordListsRepository.FixedPassword
    ): List<String> {
        return fixedPasswordListsRepository.getFixedPasswordListForShow(fixedPassword)
    }

    fun getFixedPasswordList(
        fixedPassword: FixedPasswordListsRepository.FixedPassword,
        wifiSsid: String?
    ): List<String> {
        return fixedPasswordListsRepository.getFixedPasswordList(fixedPassword, wifiSsid)
    }
}