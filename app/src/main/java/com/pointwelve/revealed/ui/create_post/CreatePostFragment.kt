package com.pointwelve.revealed.ui.create_post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.children
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.pointwelve.revealed.Constants
import com.pointwelve.revealed.R
import com.pointwelve.revealed.databinding.CreatePostFragmentBinding
import com.pointwelve.revealed.di.Injectable
import com.pointwelve.revealed.util.Status
import com.pointwelve.revealed.util.views.autoCleared
import kotlinx.android.synthetic.main.create_post_fragment.*
import javax.inject.Inject

class CreatePostFragment : DialogFragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val createPostViewModel: CreatePostViewModel by viewModels {
        viewModelFactory
    }

    private val topicTitleToIdMap = mutableMapOf<String, String>()

    var binding by autoCleared<CreatePostFragmentBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        createPostViewModel.configs.observe(viewLifecycleOwner, Observer { data ->
            progressBar.isGone = data.status != Status.LOADING
            if(data.status == Status.SUCCESS) {
                // Topics
                val topics = data.data?.getAllTopics?.edges
                    .orEmpty()
                    .onEach { topicTitleToIdMap[it.name] = it.id }
                    .map { it.name }

                val topicAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, topics)
                topicDropdown.setAdapter(topicAdapter)

                // Tags
                data.data?.getAllTags?.edges.orEmpty().forEach {
                    addChipToGroup(it.name, it.id, chipGroup)
                }
            }
        })

        createPostViewModel.createPostResults.observe(viewLifecycleOwner, Observer { data ->
            progressBar.isGone = data.status != Status.LOADING
            if(data.status == Status.SUCCESS) {
                findNavController().navigateUp()
                findNavController().previousBackStackEntry?.savedStateHandle?.set(Constants.postCreatedKey, true)
            } else {
                //TODO: Show Error
            }
        })

        initMenu()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<CreatePostFragmentBinding>(
            inflater,
            R.layout.create_post_fragment,
            container,
            false
        )

        binding = dataBinding

        return dataBinding.root
    }

    private fun initMenu() {
        toolbar.inflateMenu(R.menu.menu_create_post)
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        toolbar.setOnMenuItemClickListener {
            validateAndCreatePost()
            true
        }
    }

    private fun addChipToGroup(tagName: String, tagId: String, chipGroup: ChipGroup) {
        val chip = Chip(requireContext())
        chip.tag = tagId
        chip.isCheckable = true
        chip.text = tagName
        chipGroup.addView(chip as View)
    }

    private fun validateAndCreatePost() {
        val tagIds = chipGroup.children.map { it as Chip }
            .filter { it.isChecked }.map { it.tag as String }.toList()

        val selectedTopic = topicTitleToIdMap[topicDropdown.text.toString()] ?: ""
        val subject = subjectInputEditText.text.toString()
        val content = contentInputEditText.text.toString()
        createPostViewModel.createPost(subject, content, selectedTopic, tagIds)
    }


}
