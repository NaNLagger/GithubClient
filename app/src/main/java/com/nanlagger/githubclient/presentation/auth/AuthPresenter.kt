package com.nanlagger.githubclient.presentation.auth

import com.arellomobile.mvp.MvpPresenter
import com.nanlagger.githubclient.Screens
import com.nanlagger.githubclient.tools.addTo
import com.nanlagger.githubclient.domain.entity.AuthResult
import com.nanlagger.githubclient.domain.repository.AuthRepository
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

class AuthPresenter @Inject constructor(
    private val authRepository: AuthRepository,
    private val router: Router
) : MvpPresenter<AuthView>() {

    private val compositeDisposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        authRepository.authResult
            .filter { it is AuthResult.AccountResult }
            .map { it as AuthResult.AccountResult }
            .flatMap { if (it.error != null) Observable.error(it.error) else Observable.just(true)}
            .subscribe({
                router.newRootScreen(Screens.RepositoriesScreen)
            }, { error ->
                Timber.e(error)
            })
            .addTo(compositeDisposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    fun login() {
        authRepository.login()
    }
}