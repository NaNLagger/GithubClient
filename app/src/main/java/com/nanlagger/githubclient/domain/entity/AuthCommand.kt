package com.nanlagger.githubclient.domain.entity

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

sealed class AuthCommand {

    object LoginCommand : AuthCommand()
    object LogoutCommand : AuthCommand()
    object GetUserCommand : AuthCommand()
}

sealed class AuthResult {

    class AccountResult(val account: GoogleSignInAccount?, val error: Throwable? = null) : AuthResult()
    object LogoutResult : AuthResult()
}