package com.nanlagger.githubclient.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.google.android.material.snackbar.Snackbar
import java.util.*

private const val KEY_FRAGMENT_ID = "key.fragment.id"

abstract class BaseFragment : MvpAppCompatFragment() {

    abstract val layoutId: Int

    private var isInstanceStateSaved = false
    protected lateinit var fragmentId: String

    protected fun showSnackMessage(message: String, duration: Int = Snackbar.LENGTH_LONG) {
        view?.let {
            val snackbar = Snackbar.make(it, message, duration)
            snackbar.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentId = savedInstanceState?.getString(KEY_FRAGMENT_ID) ?: UUID.randomUUID().toString()
        onOpenScope()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            = inflater.inflate(layoutId, container, false)!!

    override fun onStart() {
        super.onStart()
        isInstanceStateSaved = false
    }

    override fun onResume() {
        super.onResume()
        isInstanceStateSaved = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        isInstanceStateSaved = true
        outState.putString(KEY_FRAGMENT_ID, fragmentId)
    }

    override fun onDestroy() {
        super.onDestroy()
        //We leave the screen and respectively all fragments will be destroyed
        if (activity?.isFinishing == true) {
            onCloseScope()
            return
        }

        // When we rotate device isRemoving() return true for fragment placed in backstack
        // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
        if (isInstanceStateSaved) {
            isInstanceStateSaved = false
            return
        }

        // See https://github.com/Arello-Mobile/Moxy/issues/24
        var anyParentIsRemoving = false
        var parent = parentFragment
        while (!anyParentIsRemoving && parent != null) {
            anyParentIsRemoving = parent.isRemoving
            parent = parent.parentFragment
        }

        if (isRemoving || anyParentIsRemoving) {
            onCloseScope()
        }
    }

    protected open fun onOpenScope() {}

    protected open fun onCloseScope() {}
}