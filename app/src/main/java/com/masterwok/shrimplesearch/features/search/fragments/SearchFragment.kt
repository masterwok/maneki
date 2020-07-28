package com.masterwok.shrimplesearch.features.search.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.di.AppInjector
import com.masterwok.shrimplesearch.features.search.viewmodels.SearchViewModel
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SearchViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        AppInjector
            .searchComponent
            .inject(this)

        viewModel.initialize()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_search
        , container
        , false
    )

    companion object {

        @JvmStatic
        fun newInstance() = SearchFragment()

    }
}
