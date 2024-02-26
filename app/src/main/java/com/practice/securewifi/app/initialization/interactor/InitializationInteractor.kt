package com.practice.securewifi.app.initialization.interactor

class InitializationInteractor(
    private val loadPasswordsListsFromAssetsInteractor: LoadPasswordsListsFromAssetsInteractor
) {

    suspend fun initializeApp() {
        loadPasswordsListsFromAssetsInteractor.loadPasswordsListsFromAssets()
    }
}