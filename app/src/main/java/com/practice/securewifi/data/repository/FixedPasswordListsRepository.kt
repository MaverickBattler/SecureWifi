package com.practice.securewifi.data.repository

import android.content.Context
import com.practice.securewifi.R

class FixedPasswordListsRepository(private val applicationContext: Context) {

    fun getFixedPasswordListsNames(): List<String> {
        return listOf(
            applicationContext.getString(R.string.adaptive),
            applicationContext.getString(R.string.most_popular)
        )
    }

    fun getFixedPasswordListForShow(fixedPassword: FixedPassword): List<String> {
        val fixedPasswords = applicationContext.assets.open("passwords.txt").bufferedReader().use {
            it.readLines()
        }
        val adaptiveList = mutableListOf<String>()
        return adaptiveList + fixedPasswords
    }

    fun getFixedPasswordList(fixedPassword: FixedPassword, wifiSsid: String?): List<String> {
        val fixedPasswords = applicationContext.assets.open("passwords.txt").bufferedReader().use {
            it.readLines()
        }
        val adaptiveList = mutableListOf<String>()
        wifiSsid?.let {
            adaptiveList.add(wifiSsid)
            adaptiveList.add("${wifiSsid}123")
        }
        return adaptiveList + fixedPasswords
    }

    enum class FixedPassword {
        ADAPTIVE,
        MOST_POPULAR
    }
}