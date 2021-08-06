package com.grewon.dronedin.dagger.module


import com.grewon.dronedin.changepassword.contract.ChangePasswordContract
import com.grewon.dronedin.changepassword.presenter.ChangePasswordPresenter
import com.grewon.dronedin.customersupport.contract.CustomerSupportContract
import com.grewon.dronedin.customersupport.presenter.CustomerSupportPresenter
import com.grewon.dronedin.forgotpassword.contract.ForgotPasswordContract
import com.grewon.dronedin.forgotpassword.presenter.ForgotPasswordPresenter
import com.grewon.dronedin.signin.contract.SignInContract
import com.grewon.dronedin.signin.presenter.SignInPresenter
import com.grewon.dronedin.signup.contract.SignUpContract
import com.grewon.dronedin.signup.presenter.SignUpPresenter
import com.grewon.dronedin.verification.contract.VerificationContract
import com.grewon.dronedin.verification.presenter.VerificationPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SignInModule {

    @Singleton
    @Provides
    fun provideSignInPresenter(): SignInContract.Presenter {
        return SignInPresenter()
    }

    @Singleton
    @Provides
    fun provideSignUpPresenter(): SignUpContract.Presenter {
        return SignUpPresenter()
    }

    @Singleton
    @Provides
    fun provideForgotPasswordPresenter(): ForgotPasswordContract.Presenter {
        return ForgotPasswordPresenter()
    }

    @Singleton
    @Provides
    fun provideChangePasswordPresenter(): ChangePasswordContract.Presenter {
        return ChangePasswordPresenter()
    }


    @Singleton
    @Provides
    fun provideVerificationPresenter(): VerificationContract.Presenter {
        return VerificationPresenter()
    }

    @Singleton
    @Provides
    fun provideCustomerSupportPresenter(): CustomerSupportContract.Presenter {
        return CustomerSupportPresenter()
    }

}