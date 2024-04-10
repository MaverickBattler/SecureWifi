package com.practice.securewifi.custom_list.di

import com.practice.securewifi.custom_list.custom_list_edit.interactor.DynamicPasswordsInfoInteractor
import com.practice.securewifi.custom_list.custom_list_edit.interactor.FixedPasswordsListInteractor
import com.practice.securewifi.custom_list.custom_list_edit.interactor.GetDynamicPasswordsInfoInteractor
import com.practice.securewifi.custom_list.custom_list_edit.interactor.GetFixedPasswordsForListInteractor
import com.practice.securewifi.custom_list.custom_list_edit.repository.DynamicPasswordsInfoRepository
import com.practice.securewifi.custom_list.interactor.CustomPasswordListsInteractor
import com.practice.securewifi.custom_list.mapper.CustomPasswordListsMapper
import com.practice.securewifi.custom_list.custom_list_edit.repository.FixedPasswordsListRepository
import com.practice.securewifi.custom_list.custom_list_edit.viewmodel.CustomPasswordListViewModel
import com.practice.securewifi.custom_list.custom_list_edit.viewmodel.DynamicPasswordsListViewModel
import com.practice.securewifi.custom_list.custom_list_edit.viewmodel.FixedPasswordsListViewModel
import com.practice.securewifi.custom_list.viewmodel.CustomPasswordListsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val customListModule = module {

    factory {
        CustomPasswordListsInteractor(
            passwordListsRepository = get(),
            passwordListFixedPasswordsRepository = get(),
            customPasswordListsMapper = get()
        )
    }

    factory {
        FixedPasswordsListInteractor(
            fixedPasswordsListRepository = get()
        )
    }

    viewModel {
        CustomPasswordListsViewModel(
            customPasswordListsInteractor = get(),
        )
    }

    viewModel { params ->
        CustomPasswordListViewModel(
            listName = params.get(),
            fixedPasswordsListInteractor = get(),
            dynamicPasswordsInfoInteractor = get(),
            customPasswordListsInteractor = get()
        )
    }

    single {
        FixedPasswordsListRepository()
    }
    single {
        DynamicPasswordsInfoRepository()
    }

    factory {
        CustomPasswordListsMapper(
            application = androidApplication()
        )
    }

    viewModel { params ->
        DynamicPasswordsListViewModel(
            listName = params.get(),
            dynamicPasswordsInfoInteractor = get(),
            getDynamicPasswordsInfoInteractor = get()
        )
    }

    viewModel { params ->
        FixedPasswordsListViewModel(
            listName = params.get(),
            fixedPasswordsListInteractor = get(),
            getFixedPasswordsForListInteractor = get()
        )
    }

    factory {
        GetDynamicPasswordsInfoInteractor(
            passwordListDynamicPasswordsInfoRepository = get(),
            passwordListsRepository = get()
        )
    }

    factory {
        GetFixedPasswordsForListInteractor(
            passwordListFixedPasswordsRepository = get()
        )
    }

    factory {
        DynamicPasswordsInfoInteractor(
            dynamicPasswordsInfoRepository = get()
        )
    }
}