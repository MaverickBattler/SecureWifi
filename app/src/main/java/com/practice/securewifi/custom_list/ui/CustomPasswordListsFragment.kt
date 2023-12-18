package com.practice.securewifi.custom_list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.practice.securewifi.R
import com.practice.securewifi.custom_list.adapter.CustomPasswordListsAdapter
import com.practice.securewifi.custom_list.viewmodel.CustomPasswordListsViewModel
import com.practice.securewifi.databinding.FragmentPasswordListsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CustomPasswordListsFragment : Fragment() {

    private var _binding: FragmentPasswordListsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<CustomPasswordListsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val onDeleteClickListener: (String) -> Unit = { listName ->
            viewModel.onDeletePasswordList(listName)
        }
        val onItemClickListener: (String) -> Unit = { listName ->
            val editable = viewModel.isChosenPasswordListEditable(listName)
            val action = CustomPasswordListsFragmentDirections.actionCustomListsToEditCustomList(
                listName,
                editable
            )
            findNavController().navigate(action)
        }
        val adapter = CustomPasswordListsAdapter(onDeleteClickListener, onItemClickListener)
        binding.recyclerviewCustomLists.adapter = adapter
        binding.recyclerviewCustomLists.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.addListFab.setOnClickListener {
            findNavController().navigate(R.id.action_customLists_to_edit_customList)
        }
        viewModel.allPasswordLists.observe(viewLifecycleOwner) { allPasswordLists ->
            adapter.submitList(allPasswordLists)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}