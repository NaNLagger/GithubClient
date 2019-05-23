package com.nanlagger.githubclient.ui.main

import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.nanlagger.githubclient.R
import com.nanlagger.githubclient.data.GoogleSignInService
import com.nanlagger.githubclient.di.Scopes
import com.nanlagger.githubclient.domain.entity.AuthCommand
import com.nanlagger.githubclient.domain.repository.AuthRepository
import com.nanlagger.githubclient.presentation.main.MainPresenter
import com.nanlagger.githubclient.presentation.main.MainView
import io.reactivex.disposables.Disposable
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import toothpick.Scope
import toothpick.Toothpick
import javax.inject.Inject


class MainActivity : MvpAppCompatActivity(), MainView {

    @Inject
    lateinit var navigationHolder: NavigatorHolder
    @Inject
    lateinit var authRepository: AuthRepository

    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    private val navigator: Navigator = SupportAppNavigator(this, supportFragmentManager, R.id.rootContainer)
    private val googleSignInService by lazy { GoogleSignInService(this) }
    private lateinit var scope: Scope

    private var authCommandDisposable: Disposable? = null

    @ProvidePresenter
    fun providePresenter(): MainPresenter =
        scope.getInstance(MainPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        scope = Toothpick.openScope(Scopes.APP_SCOPE)
        Toothpick.inject(this, scope)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null)
            mainPresenter.checkAccount(googleSignInService.getLastAccount())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(googleSignInService.isSignInResult(requestCode) && data != null) {
            googleSignInService.handleSignInResult(data, authRepository::sendAccountResult, authRepository::sendError)
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigationHolder.removeNavigator()
    }

    override fun onStart() {
        super.onStart()
        subscribeOnAuthCommand()
    }

    override fun onStop() {
        super.onStop()
        unsubscribeOnAuthCommand()
    }

    private fun subscribeOnAuthCommand() {
        authCommandDisposable = authRepository.authCommand
            .subscribe {
                when(it) {
                    is AuthCommand.LoginCommand -> googleSignInService.signIn()
                    is AuthCommand.LogoutCommand -> googleSignInService.signOut(authRepository::sendLogoutResult)
                    is AuthCommand.GetUserCommand -> authRepository.sendAccountResult(googleSignInService.getLastAccount())
                }
            }
    }

    private fun unsubscribeOnAuthCommand() {
        authCommandDisposable?.dispose()
    }
}
