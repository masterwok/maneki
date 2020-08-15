package com.masterwok.shrimplesearch.features.query.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.masterwok.shrimplesearch.R
import com.masterwok.shrimplesearch.common.extensions.dpToPx
import com.masterwok.shrimplesearch.common.extensions.hideSoftKeyboard
import com.masterwok.shrimplesearch.common.utils.notNull
import com.masterwok.shrimplesearch.di.AppInjector
import com.masterwok.shrimplesearch.features.query.adapters.QueryResultsAdapter
import com.masterwok.shrimplesearch.features.query.viewmodels.QueryViewModel
import com.masterwok.xamarininterface.enums.IndexerType
import com.masterwok.xamarininterface.enums.QueryState
import com.masterwok.xamarininterface.models.Indexer
import com.masterwok.xamarininterface.models.IndexerQueryResult
import com.masterwok.xamarininterface.models.Query
import kotlinx.android.synthetic.main.fragment_query.*
import kotlinx.android.synthetic.main.include_toolbar_query.*
import javax.inject.Inject


class QueryFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: QueryViewModel by viewModels(this::requireActivity) { viewModelFactory }

    private val queryResultsAdapter = QueryResultsAdapter {
        viewModel.setSelectedIndexerQueryResult(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_query
        , container
        , false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        subscribeToViewComponents()
        subscribeToLiveData()
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = queryResultsAdapter
        }
    }

    private fun subscribeToViewComponents() {
        autoCompleteTextViewSearch.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.setQuery(Query(textView.text.toString()))
                queryResultsAdapter.configure(emptyList())
                activity?.hideSoftKeyboard()
                progressBar.isVisible = true
                linearLayoutQueryHint.isVisible = false
                setAutoCompleteDrawableRight()
                true
            } else {
                false
            }
        }

        autoCompleteTextViewSearch.setOnFocusChangeListener { _, isFocused ->
            if (isFocused) {
                setAutoCompleteDrawableRight()
            }
        }
    }

    private fun setAutoCompleteDrawableRight() = context?.notNull { context ->
        autoCompleteTextViewSearch.setCompoundDrawablesWithIntrinsicBounds(
            null
            , null
            , if (autoCompleteTextViewSearch.text.isNullOrEmpty()) {
                null
            } else {
                ContextCompat.getDrawable(context, R.drawable.ic_auto_complete_clear)
            }
            , null
        )
    }

    private fun subscribeToLiveData() {
        viewModel.liveDataIndexerQueryResults.observe(
            viewLifecycleOwner,
            this::onIndexerQueryResultsChange
        )

        viewModel.liveDataQueryCompleted.observe(viewLifecycleOwner) {
            onQueryCompleted()
        }
    }

    private fun onQueryCompleted() {
        progressBar.isVisible = false
    }

    private fun onIndexerQueryResultsChange(queryResults: List<IndexerQueryResult>) {
        if (queryResults.count() == 0) {
            queryResultsAdapter.configure(queryResults)
            return
        }

        val aggregateIndexerQueryResult = createAggregateIndexerQueryResult(queryResults)
        val sortedQueryResults = sortQueryResults(queryResults)

        queryResultsAdapter.configure(listOf(aggregateIndexerQueryResult) + sortedQueryResults)
    }

    private fun sortQueryResults(
        queryResults: List<IndexerQueryResult>
    ): List<IndexerQueryResult> = queryResults.sortedByDescending { it.magnetCount }

    private fun createAggregateIndexerQueryResult(
        results: Collection<IndexerQueryResult>
    ): IndexerQueryResult = IndexerQueryResult(
        Indexer(
            id = ""
            , type = IndexerType.Aggregate
            , displayName = getString(R.string.indexer_aggregate_display_name)
            , displayDescription = null
        )
        , results.flatMap { it.items }
        , queryState = QueryState.Success
        , failureReason = null
        , magnetCount = results.sumBy { it.magnetCount }
        , linkCount = results.sumBy { it.linkCount }
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)

        AppInjector
            .queryComponent
            .inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = QueryFragment()
    }
}

