package com.nanlagger.githubclient.ui.auth

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.nanlagger.githubclient.R
import com.nanlagger.githubclient.di.Scopes
import com.nanlagger.githubclient.presentation.auth.AuthPresenter
import com.nanlagger.githubclient.presentation.auth.AuthView
import com.nanlagger.githubclient.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_auth.*
import toothpick.Scope
import toothpick.Toothpick


class AuthFragment : BaseFragment(), AuthView {
    override val layoutId: Int = R.layout.fragment_auth

    @InjectPresenter
    lateinit var authPresenter: AuthPresenter

    @ProvidePresenter
    fun providePresenter(): AuthPresenter =
        scope.getInstance(AuthPresenter::class.java)

    private lateinit var scope: Scope

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sign_in_button.setOnClickListener { authPresenter.login() }
    }

    override fun onOpenScope() {
        scope = Toothpick.openScopes(Scopes.APP_SCOPE, "AuthScreenScope")
    }

    override fun onCloseScope() {
        Toothpick.closeScope("AuthScreenScope")
    }
}