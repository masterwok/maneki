package com.masterwok.shrimplesearch.features.query.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.di.AppInjector
import com.masterwok.shrimplesearch.features.query.adapters.QueryFragmentStateAdapter
import com.masterwok.shrimplesearch.features.query.viewmodels.QueryViewModel
import kotlinx.android.synthetic.main.fragment_query_container.*
import javax.inject.Inject


class QueryContainerFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: QueryViewModel by viewModels(this::requireActivity) { viewModelFactory }

    private lateinit var queryFragmentStatePagerAdapter: QueryFragmentStateAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        AppInjector
            .searchComponent
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_query_container
        , container
        , false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
    }

    private fun initViewPager() {
        queryFragmentStatePagerAdapter = QueryFragmentStateAdapter(childFragmentManager, lifecycle)

        viewPager.adapter = queryFragmentStatePagerAdapter
    }

    companion object {

        @JvmStatic
        fun newInstance() = QueryContainerFragment()

    }
}
