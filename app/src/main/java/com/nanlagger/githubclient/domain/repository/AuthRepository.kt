package com.nanlagger.githubclient.domain.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.jakewharton.rxrelay2.PublishRelay
import com.nanlagger.githubclient.data.database.dao.RepositoryDao
import com.nanlagger.githubclient.domain.entity.AuthCommand
import com.nanlagger.githubclient.domain.entity.AuthResult
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single

class AuthRepository(
        private val repositoryDao: RepositoryDao,
        private val uiScheduler: Scheduler,
        private val ioScheduler: Scheduler
) {

    private val authRelay = PublishRelay.create<AuthCommand>()
    private val resultAuthRelay = PublishRelay.create<AuthResult>()

    val authCommand: Observable<AuthCommand> = authRelay.hide()
    val authResult: Observable<AuthResult> = resultAuthRelay.hide()

    fun login(): Single<GoogleSignInAccount> {
        authRelay.accept(AuthCommand.LoginCommand)
        return resultAuthRelay.ofType(AuthResult.AccountResult::class.java)
                .filter { it.command == AuthCommand.LoginCommand }
                .firstOrError()
                .map {
                    if (it.account == null || it.error != null)
                        throw it.error ?: Exception("Login failed")
                    else
                        it.account
                }
    }

    fun logout(): Single<Unit> {
        authRelay.accept(AuthCommand.LogoutCommand)
        return resultAuthRelay.ofType(AuthResult.LogoutResult::class.java)
                .firstOrError()
                .observeOn(ioScheduler)
                .map { repositoryDao.clearRepositories() }
                .observeOn(uiScheduler)
    }

    fun currentAccount() {
        authRelay.accept(AuthCommand.GetUserCommand)
    }

    fun sendAccountResult(command: AuthCommand, account: GoogleSignInAccount?) {
        resultAuthRelay.accept(AuthResult.AccountResult(command, account))
    }

    fun sendError(command: AuthCommand, throwable: Throwable) {
        resultAuthRelay.accept(AuthResult.AccountResult(command,null, throwable))
    }

    fun sendLogoutResult() {
        resultAuthRelay.accept(AuthResult.LogoutResult(AuthCommand.LogoutCommand))
    }
}