package com.practice.securewifi.custom_list.di

import com.practice.securewifi.data.interactor.CustomPasswordListInteractor
import com.practice.securewifi.data.interactor.CustomPasswordListsInteractor
import com.practice.securewifi.data.interactor.FixedPasswordListsInteractor
import com.practice.securewifi.custom_list.repository.CustomListRepository
import com.practice.securewifi.custom_list.viewmodel.CustomPasswordListViewModel
import com.practice.securewifi.custom_list.viewmodel.CustomPasswordListsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val customListModule = module {

    factory {
        CustomPasswordListsInteractor(passwordListsRepository = get())
    }

    factory {
        FixedPasswordListsInteractor(fixedPasswordListsRepository = get())
    }

    factory {
        CustomPasswordListInteractor(
            passwordListsRepository = get(),
            customListRepository = get()
        )
    }

    viewModel {
        CustomPasswordListsViewModel(
            customPasswordListsInteractor = get(),
            fixedPasswordListsInteractor = get()
        )
    }

    viewModel { params ->
        CustomPasswordListViewModel(
            listName = params.get(),
            customPasswordListInteractor = get(),
            customPasswordListsInteractor = get(),
            fixedPasswordListsInteractor = get(),
            application = androidApplication()
        )
    }

    factory {
        CustomListRepository()
    }
}