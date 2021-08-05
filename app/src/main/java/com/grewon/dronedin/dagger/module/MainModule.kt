package com.grewon.dronedin.dagger.module

import com.grewon.dronedin.main.contract.MainContract
import com.grewon.dronedin.main.presenter.MainPresenter
import com.grewon.dronedin.settings.contract.SettingsContract
import com.grewon.dronedin.settings.presenter.SettingsPresenter

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule {

    @Singleton
    @Provides
    fun provideMainPresenter(): MainContract.Presenter {
        return MainPresenter()
    }


    @Singleton
    @Provides
    fun provideSettingsPresenter(): SettingsContract.Presenter {
        return SettingsPresenter()
    }
}