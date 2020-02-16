package com.pointwelve.revealed.ui.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.pointwelve.revealed.AppExecutors
import com.pointwelve.revealed.R
import com.pointwelve.revealed.databinding.PostItemBinding
import com.pointwelve.revealed.graphql.fragment.PostDetail
import com.pointwelve.revealed.ui.common.DataBoundListAdapter

class PostAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val callback: ((PostDetail) -> Unit)?
) : DataBoundListAdapter<PostDetail, PostItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<PostDetail>() {
        override fun areItemsTheSame(oldItem: PostDetail, newItem: PostDetail): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PostDetail, newItem: PostDetail): Boolean {
            return oldItem.id == newItem.id
        }
    }
) {

    override fun createBinding(parent: ViewGroup): PostItemBinding {
        val binding = DataBindingUtil
            .inflate<PostItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.post_item,
                parent,
                false,
                dataBindingComponent
            )

        binding.root.setOnClickListener {
            binding.postDetail?.let {
                callback?.invoke(it)
            }
        }

        return binding
    }

    override fun bind(binding: PostItemBinding, item: PostDetail) {
        binding.postDetail = item
    }
}
