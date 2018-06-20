package com.example.rssgrabber.application.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.rssgrabber.R
import com.example.rssgrabber.application.adapters.ViewAdapterImpl
import com.example.rssgrabber.application.ui.UiItem
import com.example.rssgrabber.retrofit.entities.FeedItem
import com.squareup.picasso.Picasso
import kotterknife.bindOptionalView

class ItemHolder(viewGroup: ViewGroup) : BaseHolder(UiItem().createAnko(viewGroup)) {
    val itemContainer: View? by bindOptionalView(R.id.item_container)
    val imageView: ImageView? by bindOptionalView(R.id.preview)
    val title: TextView? by bindOptionalView(R.id.title)
    val salary: TextView? by bindOptionalView(R.id.salary)
    val description: TextView? by bindOptionalView(R.id.description)

    override fun render(adapter: ViewAdapterImpl<*>, holder: RecyclerView.ViewHolder, position: Int) {
        val item = adapter.getItem(position) as FeedItem

        title?.text = item.title?.replace("Требуется ", "")
        description?.text = item.description
        salary?.text = item.salary

        itemContainer?.setOnClickListener {
            adapter.getCallback()?.onItemSelected(position, null)
        }

        val logoSize = item.companyLogo?.length ?: 0
        if (logoSize > 0) {
            Picasso.with(adapter.getWeakContext()).cancelRequest(imageView)
            Picasso.with(adapter.getWeakContext())
                    .load(item.companyLogo)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(imageView)
        }
    }

    override fun onViewDetachedFromWindow(adapter: ViewAdapterImpl<*>, holder: RecyclerView.ViewHolder?) {}
    override fun onViewAttachedToWindow(adapter: ViewAdapterImpl<*>, holder: RecyclerView.ViewHolder?) {}
}