package com.nanlagger.githubclient.presentation.repositories

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.nanlagger.githubclient.Screens
import com.nanlagger.githubclient.domain.repository.AuthRepository
import com.nanlagger.githubclient.tools.addTo
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

class RepositoriesContainerPresenter @Inject constructor(
        private val authRepository: AuthRepository,
        private val router: Router
) : MvpPresenter<MvpView>() {

    private val compositeDisposable = CompositeDisposable()

    fun logout() {
        authRepository.logout()
                .subscribe({
                    router.newRootScreen(Screens.AuthScreen)
                }, { error ->
                    Timber.e(error)
                })
                .addTo(compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }
}