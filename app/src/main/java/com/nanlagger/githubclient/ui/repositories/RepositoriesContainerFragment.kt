package com.nanlagger.githubclient.ui.repositories

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.nanlagger.githubclient.R
import com.nanlagger.githubclient.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_reposirories_container.*

class RepositoriesContainerFragment : BaseFragment() {
    override val layoutId: Int = R.layout.fragment_reposirories_container

    private val adapter: MyIssuesPagesAdapter by lazy { MyIssuesPagesAdapter() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.apply {
            inflateMenu(R.menu.menu_repositories)
        }
        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    childFragmentManager.fragments.forEach {
                        (it as? RepositoriesListFragment)?.submitQuery(query)
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                searchView?.queryHint = "Search in ${adapter.getPageTitle(position)}"
            }
        })
    }

    private inner class MyIssuesPagesAdapter : FragmentPagerAdapter(childFragmentManager) {
        override fun getItem(position: Int) = when (position) {
            0 -> RepositoriesListFragment.newInstance(false)
            else -> RepositoriesListFragment.newInstance(true)
        }

        override fun getCount() = 2

        override fun getPageTitle(position: Int) = when (position) {
            0 -> getString(R.string.search_title)
            1 -> getString(R.string.favorite_title)
            else -> null
        }
    }
}