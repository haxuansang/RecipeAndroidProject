package com.enclave.recipeproject.app.main

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.enclave.recipeproject.R
import com.enclave.recipeproject.app.detail.DetailActivity
import com.enclave.recipeproject.app.main.adapters.RecipeAdapter
import com.enclave.recipeproject.app.main.viewmodel.MainViewModel
import com.enclave.recipeproject.base.BaseBindingActivity
import com.enclave.recipeproject.databinding.ActivityMainBinding
import com.enclave.recipeproject.model.Recipe
import com.enclave.recipeproject.model.RecipeType
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.reflect.KClass

class MainActivity : BaseBindingActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutId: Int = R.layout.activity_main
    override val viewModelClass: KClass<MainViewModel> = MainViewModel::class

    companion object {
        const val RECIPE_ALL = "All"
    }

    override fun init(savedInstanceState: Bundle?) {

    }

    override fun initView(savedInstanceState: Bundle?) {
        viewModel.listRecipeTypeLiveData.observe(this, Observer {
            setUpRecipeTypes(it)
        })
        setUpRecipeRV()
        viewModel.resetSpinnerLiveData.observe(this, Observer {
            if (it) {
                binding.spinnerRecipeType.setSelection(0)
            }
        })
        viewModel.goToDetailScreenLiveData.observe(this, Observer {
            goToDetail(it)
        })
    }

    private fun setUpRecipeRV() {
        val adapter = RecipeAdapter(this, viewModel.listRecipeLiveData, viewModel::onItemClicked)
        binding.rvRecipe.layoutManager = LinearLayoutManager(this)
        binding.rvRecipe.adapter = adapter
        binding.fab.setOnClickListener {
            startActivity(DetailActivity.prepareIntent(this))
        }
    }

    private fun goToDetail(recipe: Recipe) {
        startActivity(DetailActivity.prepareIntent(this, recipe))
    }

    private fun setUpRecipeTypes(list: List<RecipeType>) {
        val recipeTypes = mutableListOf<String>()
        recipeTypes.add(RECIPE_ALL)
        recipeTypes.addAll(list.map {
            it.title
        })
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, recipeTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRecipeType.adapter = adapter
        spinner_recipe_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //nothing
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item = recipeTypes[position]
                if (position == 0) {
                    viewModel.currentRecipeTypeId = null
                    viewModel.getRecipes()
                } else {
                    val recipeId = list[position - 1].id
                    viewModel.currentRecipeTypeId = recipeId
                    viewModel.getRecipes(recipeId)
                }
            }
        }
    }
}
