package com.practice.securewifi.connect

sealed interface Command {

    object StopConnections: Command

    object PrepareForConnections: Command

    object StartConnections: Command

    object AskForAccessFineLocationPermission: Command

    data class ShowMessageToUser(val message: String): Command

}