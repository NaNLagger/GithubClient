package com.nanlagger.githubclient.presentation.main

import com.arellomobile.mvp.MvpPresenter
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.nanlagger.githubclient.Screens
import com.nanlagger.githubclient.domain.repository.AuthRepository
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val router: Router
) : MvpPresenter<MainView>() {

    fun checkAccount(account: GoogleSignInAccount?) {
        if(account == null)
            router.newRootScreen(Screens.AuthScreen)
        else
            router.newRootScreen(Screens.RepositoriesScreen)
    }
}