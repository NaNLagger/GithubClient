package com.nanlagger.githubclient.domain.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.jakewharton.rxrelay2.PublishRelay
import com.nanlagger.githubclient.domain.entity.AuthCommand
import com.nanlagger.githubclient.domain.entity.AuthResult
import io.reactivex.Observable

class AuthRepository {

    private val authRelay = PublishRelay.create<AuthCommand>()
    private val resultAuthRelay = PublishRelay.create<AuthResult>()

    val authCommand: Observable<AuthCommand> = authRelay.hide()
    val authResult: Observable<AuthResult> = resultAuthRelay.hide()

    fun login() {
        authRelay.accept(AuthCommand.LoginCommand)
    }

    fun logout() {
        authRelay.accept(AuthCommand.LogoutCommand)
    }

    fun currentAccount() {
        authRelay.accept(AuthCommand.GetUserCommand)
    }

    fun sendAccountResult(account: GoogleSignInAccount?) {
        resultAuthRelay.accept(AuthResult.AccountResult(account))
    }

    fun sendError(throwable: Throwable) {
        resultAuthRelay.accept(AuthResult.AccountResult(null, throwable))
    }

    fun sendLogoutResult() {
        resultAuthRelay.accept(AuthResult.LogoutResult)
    }
}