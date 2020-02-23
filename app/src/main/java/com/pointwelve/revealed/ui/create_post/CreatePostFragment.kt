package com.pointwelve.revealed.ui.create_post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
                val topics = data.data?.getAllTopics?.edges.orEmpty().map { it.name }
                val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, topics)
                topicDropdown.setAdapter(adapter)

                val tags = data.data?.getAllTags?.edges.orEmpty().map { it.name }.toTypedArray()
                val checked = tags.map { false }.toBooleanArray()

                tagInputEditText.setOnClickListener {
                    val builder = AlertDialog.Builder(requireContext())
                        .setTitle("Select Tags")
                        .setMultiChoiceItems(tags, checked) { dialog, which, isChecked ->

                        }
                        .setPositiveButton("OK") { dialog, which ->
                        }.setNegativeButton("Cancel", null)
                        .create()

                    builder.show()
                }
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
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.setOnMenuItemClickListener {
            dismiss()
            true
        }
    }

}
