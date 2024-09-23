package com.pnvpnvpnv.graphtesttask.screens.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.pnvpnvpnv.graphtesttask.R
import com.pnvpnvpnv.graphtesttask.databinding.FragmentMainBinding
import com.pnvpnvpnv.graphtesttask.deps.Constants.POINTS_COUNT_KEY
import com.pnvpnvpnv.graphtesttask.screens.graph.GraphScreenFragment

class MainScreenFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        withBinding {
            letsGoButton.setOnClickListener {
                val value = etPoints.text.toString()
                val valueInt = value.toIntOrNull()
                when (valueInt) {
                    null -> showError(
                        view = this@withBinding.root,
                        text = getString(R.string.please_enter_number)
                    )

                    in 1..1000 -> navigateToGraph(valueInt)

                    else -> showError(
                        view = this@withBinding.root,
                        text = getString(R.string.please_enter_valid_number)
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showError(view: View, text: String) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()
    }

    private fun navigateToGraph(pointsCount: Int) {
        val args = Bundle().apply { putInt(POINTS_COUNT_KEY, pointsCount) }
        parentFragmentManager.beginTransaction()
            .replace(R.id.root_container, GraphScreenFragment::class.java, args)
            .addToBackStack(null)
            .commit()
    }

    private fun withBinding(block: FragmentMainBinding.() -> Unit) {
        val bindingNonNull = _binding ?: error("Binding is null")
        bindingNonNull.also(block)
    }
}