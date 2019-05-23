package com.nanlagger.githubclient

import androidx.fragment.app.Fragment
import com.nanlagger.githubclient.ui.auth.AuthFragment
import com.nanlagger.githubclient.ui.repositories.RepositoriesContainerFragment
import com.nanlagger.githubclient.ui.repositories.RepositoriesListFragment
import com.nanlagger.githubclient.ui.repository.RepositoryInfoFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    object AuthScreen : SupportAppScreen() {
        override fun getFragment(): Fragment = AuthFragment()
    }

    object RepositoriesScreen : SupportAppScreen() {
        override fun getFragment(): Fragment = RepositoriesContainerFragment()
    }

    class RepositoryInfoScreen(private val repositoryName: String) : SupportAppScreen() {
        override fun getFragment(): Fragment = RepositoryInfoFragment.newInstance(repositoryName)
    }
}