package com.nanlagger.githubclient.domain.entity

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

sealed class AuthCommand {

    object LoginCommand : AuthCommand()
    object LogoutCommand : AuthCommand()
    object GetUserCommand : AuthCommand()
}

sealed class AuthResult(val command: AuthCommand) {

    class AccountResult(command: AuthCommand, val account: GoogleSignInAccount?, val error: Throwable? = null) : AuthResult(command)
    class LogoutResult(command: AuthCommand) : AuthResult(command)
}