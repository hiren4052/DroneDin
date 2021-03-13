package com.grewon.dronedin.dagger.component


import com.grewon.dronedin.dagger.module.NetworkModule
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.BaseFragment
import com.grewon.dronedin.dagger.module.AppModule
import com.grewon.dronedin.dagger.module.SignInModule
import com.grewon.dronedin.forgotpassword.ForgotPasswordActivity
import com.grewon.dronedin.main.MainActivity
import com.grewon.dronedin.notifications.NotificationsFragment
import com.grewon.dronedin.profile.ProfileFragment
import com.grewon.dronedin.signin.SignInActivity
import com.grewon.dronedin.splash.SplashActivity

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, NetworkModule::class, SignInModule::class]
)
interface AppComponent {


    fun inject(activity: BaseActivity)
    fun inject(activity: SignInActivity)
    fun inject(activity: SplashActivity)
    fun inject(activity: ForgotPasswordActivity)
    fun inject(activity: MainActivity)


    //Fragments

    fun inject(fragment: BaseFragment)
    fun inject(fragment: NotificationsFragment)
    fun inject(fragment: ProfileFragment)


}