package com.pnvpnvpnv.graphtesttask.screens.graph

import android.content.ContentValues
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pnvpnvpnv.graphtesttask.R
import com.pnvpnvpnv.graphtesttask.databinding.FragmentGraphBinding
import com.pnvpnvpnv.graphtesttask.deps.Constants
import com.pnvpnvpnv.graphtesttask.deps.DependencyContainer
import com.pnvpnvpnv.graphtesttask.view.table.PointsTableAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GraphScreenFragment : Fragment() {
    private var _binding: FragmentGraphBinding? = null
    private var _viewModel: GraphScreenViewModel? = null

    private val viewModel get() = _viewModel!!
    private val pointsTableAdapter = PointsTableAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGraphBinding.inflate(layoutInflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // some sort of AssistedFactory from Hilt here
        val argsCount = arguments?.getInt(Constants.POINTS_COUNT_KEY, 10) ?: 10
        val vmFactory = DependencyContainer.getGraphScreenViewModelFactory(pointsCount = argsCount)
        _viewModel = ViewModelProvider(this, vmFactory)[GraphScreenViewModel::class.java]

        withBinding {
            pointsTable.apply {
                adapter = pointsTableAdapter
                layoutManager = LinearLayoutManager(context)
            }
            saveToFile.setOnClickListener { saveGraphToMediaStore() }
        }

        collectMVIFlows()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _viewModel = null
    }

    private fun collectMVIFlows() {
        lifecycleScope.launch {
            launch {
                viewModel.effect.collectLatest {
                    observeEffect(it)
                }
            }

            launch {
                viewModel.state.collectLatest {
                    applyState(it)
                }
            }
        }
    }

    private fun observeEffect(graphScreenEffect: GraphScreenEffect) = when (graphScreenEffect) {
        GraphScreenEffect.ShowError -> showMessage(getString(R.string.network_error))
        GraphScreenEffect.NetworkError -> showMessageWithRetry(
            getString(R.string.network_error),
            viewModel::loadPoints
        )

        is GraphScreenEffect.ShowLoading -> showLoading(graphScreenEffect.show)
    }

    private fun applyState(graphScreenState: GraphScreenState) = withBinding {
        pointsTableAdapter.submitList(graphScreenState.pointsForTable)
        graphView.setPoints(graphScreenState.pointsForGraph)
    }

    private fun showMessage(message: String) = withBinding {
        Snackbar.make(this.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showMessageWithRetry(message: String, retry: () -> Unit) = withBinding {
        Snackbar.make(this.root, message, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.once_again)) { retry() }
            .show()
    }

    private fun showLoading(show: Boolean) = withBinding {
        spinner.isVisible = show
    }

    private fun withBinding(block: FragmentGraphBinding.() -> Unit) {
        val bindingNonNull = _binding ?: error("Binding is null")
        bindingNonNull.also(block)
    }

    // should be useCase but that's a test task you know
    private fun saveGraphToMediaStore() = withBinding {
        val resolver = requireActivity().contentResolver
        val imageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val newImageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "graph_${System.currentTimeMillis()}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/GraphImages")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val imageUri = resolver.insert(imageCollection, newImageDetails)

        imageUri?.let { uri ->
            resolver.openOutputStream(uri).use { outputStream ->
                graphView.saveToStream(outputStream!!)
            }

            newImageDetails.clear()
            newImageDetails.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, newImageDetails, null, null)

            showMessage(message = getString(R.string.graph_saved))
        } ?: run {
            showMessage(message = getString(R.string.graph_saving_error))
        }
    }
}