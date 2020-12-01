package com.enclave.recipeproject.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.reactivex.disposables.CompositeDisposable
import kotlin.reflect.KClass
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.*
import javax.inject.Inject

abstract class BaseBindingActivity<TBinding : ViewDataBinding, TViewModel : ViewModel> :
    BaseActivity() {

    @Inject
    lateinit var factory: ViewModelsFactory

    lateinit var viewModel: TViewModel
    lateinit var binding: TBinding

    protected abstract val layoutId: Int
    protected abstract val viewModelClass: KClass<TViewModel>
    protected var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(viewModelClass.java)
        init(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.setVariable(BR.viewModel, viewModel)
        binding.lifecycleOwner = this
        initView(savedInstanceState)
    }

    abstract fun init(savedInstanceState: Bundle?)

    abstract fun initView(savedInstanceState: Bundle?)

    protected fun hideKeyboard(view: View) {
        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        manager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

}