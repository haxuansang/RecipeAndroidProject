package com.enclave.recipeproject.app.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.enclave.recipeproject.databinding.RecipeItemBinding
import com.enclave.recipeproject.model.Recipe

class RecipeAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val listRecipe: LiveData<List<Recipe>>,
    private val onItemClicked: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    init {
        listRecipe.observe(lifecycleOwner, Observer {
            notifyDataSetChanged()
        })

        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RecipeViewHolder(RecipeItemBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = listRecipe.value?.size ?: 0

    override fun getItemId(position: Int): Long =
        listRecipe.value?.get(position)?.id?.toLong() ?: -1

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val item = listRecipe.value?.get(position)
        item?.let { recipe ->
            holder.recipeBinding.root.setOnClickListener {
                onItemClicked(recipe)
            }
            holder.recipeBinding.recipe = recipe
        }
    }

    class RecipeViewHolder(val recipeBinding: RecipeItemBinding) :
        RecyclerView.ViewHolder(recipeBinding.root)
}