package com.pointwelve.revealed.ui.post


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.pointwelve.revealed.AppExecutors

import com.pointwelve.revealed.R
import com.pointwelve.revealed.databinding.PostFragmentBinding
import com.pointwelve.revealed.di.Injectable
import com.pointwelve.revealed.ui.common.RetryCallback
import com.pointwelve.revealed.binding.FragmentDataBindingComponent
import com.pointwelve.revealed.util.views.autoCleared
import kotlinx.android.synthetic.main.post_fragment.*
import timber.log.Timber
import javax.inject.Inject

class PostFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    private val postViewModel: PostViewModel by viewModels {
        viewModelFactory
    }

    var dataBindingComponent: DataBindingComponent =
        FragmentDataBindingComponent(this)
    var binding by autoCleared<PostFragmentBinding>()
    private var adapter by autoCleared<PostAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<PostFragmentBinding>(
            inflater,
            R.layout.post_fragment,
            container,
            false
        )
        dataBinding.retryCallback = object : RetryCallback {
            override fun retry() {
                postViewModel.retry()
            }
        }
        binding = dataBinding
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(R.transition.move)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = PostAdapter(dataBindingComponent, appExecutors) {
                post ->
            //TODO: Navigate to post detail
            Timber.d("Navigate to post detail with id ${post.id}")
        }
        this.adapter = adapter
        binding.posts = postViewModel.posts
        binding.postsList.adapter = adapter
        postponeEnterTransition()
        binding.postsList.doOnPreDraw {
            startPostponedEnterTransition()
        }
        initPostList(postViewModel)
        createPostButton.setOnClickListener {
            findNavController().navigate(R.id.action_postFragment_to_createPostFragment)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logOut -> {
                postViewModel.logout(this.requireContext())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initPostList(viewModel: PostViewModel) {
        viewModel.retry()

        viewModel.posts.observe(viewLifecycleOwner, Observer { listResource ->
            if (listResource?.data != null) {
                adapter.submitList(listResource.data)
            } else {
                adapter.submitList(emptyList())
            }
        })
    }
}
