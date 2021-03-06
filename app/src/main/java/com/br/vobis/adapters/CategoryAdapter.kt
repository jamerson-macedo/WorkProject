package com.br.vobis.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.br.vobis.R
import com.br.vobis.model.ExpandableCategory
import com.br.vobis.model.SubCategory
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class CategoryAdapter(categories: List<ExpandableCategory>) : ExpandableRecyclerViewAdapter<CategoryViewHolder, SubCategoryView>(categories) {

    lateinit var context: Context

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        context = parent.context

        val view = LayoutInflater.from(context).inflate(R.layout.list_item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): SubCategoryView {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_subcategory, parent, false)
        return SubCategoryView(view)
    }

    override fun onBindGroupViewHolder(holder: CategoryViewHolder, flatPosition: Int, group: ExpandableGroup<*>) {
        val categoryName = group.title

        holder.bind(categoryName)
    }

    override fun onBindChildViewHolder(holder: SubCategoryView, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) {
        val subcategory = group.items[childIndex] as SubCategory

        holder.bind(subcategory)
        holder.itemView.setOnClickListener {
            val intent = Intent()

            intent.putExtra("category", group.title)
            intent.putExtra("subcategory", subcategory.name)

            (context as Activity).let {
                it.setResult(AppCompatActivity.RESULT_OK, intent)
                it.finish()
            }
        }
    }
}

class CategoryViewHolder(itemView: View) : GroupViewHolder(itemView) {
    private val title: TextView = itemView.findViewById(R.id.txt_category)

    fun bind(category: String) {
        title.text = category
    }
}


class SubCategoryView(itemView: View) : ChildViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.txt_subcategory)

    fun bind(item: SubCategory) {
        title.text = item.name
    }
}
