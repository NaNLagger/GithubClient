package com.nanlagger.githubclient.data

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


class GoogleSignInService(
    private val activity: Activity
) {

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    private val signInRequestCode = 500

    private var googleSignInClient = GoogleSignIn.getClient(activity, gso)

    fun getLastAccount() = GoogleSignIn.getLastSignedInAccount(activity)

    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, signInRequestCode)
    }

    fun signOut(onComplete: () -> Unit) {
        googleSignInClient.signOut()
            .addOnCompleteListener(activity) { onComplete() }
    }

    fun isSignInResult(requestCode: Int): Boolean {
        return requestCode == signInRequestCode
    }

    fun handleSignInResult(data: Intent, onSuccess: (GoogleSignInAccount) -> Unit, onError: (Throwable) -> Unit) {
        try {
            val completedTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = completedTask.getResult(ApiException::class.java)!!
            onSuccess(account)
        } catch (e: ApiException) {
            onError(e)
        }
    }
}