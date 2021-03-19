package com.grewon.dronedin.dagger.component


import com.grewon.dronedin.addprofile.AddMoreProfileActivity
import com.grewon.dronedin.addprofile.AddProfileActivity
import com.grewon.dronedin.app.BaseActivity
import com.grewon.dronedin.app.BaseFragment
import com.grewon.dronedin.changepassword.ChangePasswordActivity
import com.grewon.dronedin.dagger.module.*
import com.grewon.dronedin.filter.FilterActivity
import com.grewon.dronedin.forgotpassword.ForgotPasswordActivity
import com.grewon.dronedin.invitepilot.InvitePilotActivity
import com.grewon.dronedin.main.MainActivity
import com.grewon.dronedin.notifications.NotificationsFragment
import com.grewon.dronedin.postjob.AddJobDetailsFragment
import com.grewon.dronedin.postjob.SelectFragmentFragment
import com.grewon.dronedin.profile.ProfileFragment
import com.grewon.dronedin.signin.SignInActivity
import com.grewon.dronedin.signup.SignUpActivity
import com.grewon.dronedin.splash.SplashActivity

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, NetworkModule::class, SignInModule::class, ProfileModule::class, CommonDataModule::class, InviteModule::class]
)
interface AppComponent {

    fun inject(activity: BaseActivity)
    fun inject(activity: SignInActivity)
    fun inject(activity: SplashActivity)
    fun inject(activity: ForgotPasswordActivity)
    fun inject(activity: MainActivity)
    fun inject(activity: SignUpActivity)
    fun inject(activity: ChangePasswordActivity)
    fun inject(activity: AddProfileActivity)
    fun inject(activity: AddMoreProfileActivity)
    fun inject(activity: FilterActivity)
    fun inject(activity: InvitePilotActivity)


    //Fragments

    fun inject(fragment: BaseFragment)
    fun inject(fragment: NotificationsFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: SelectFragmentFragment)
    fun inject(fragment: AddJobDetailsFragment)


}