package com.enclave.recipeproject.app.detail

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.enclave.recipeproject.R
import com.enclave.recipeproject.app.detail.viewmodel.DetailViewModel
import com.enclave.recipeproject.app.detail.viewmodel.DetailViewModel.Companion.ADD_NEW_RECIPE
import com.enclave.recipeproject.app.detail.viewmodel.DetailViewModel.Companion.DELETE_RECIPE
import com.enclave.recipeproject.app.detail.viewmodel.DetailViewModel.Companion.UPDATE_RECIPE
import com.enclave.recipeproject.base.BaseBindingActivity
import com.enclave.recipeproject.databinding.ActivityDetailBinding
import com.enclave.recipeproject.model.Recipe
import com.enclave.recipeproject.model.RecipeType
import java.io.FileNotFoundException
import kotlin.reflect.KClass


class DetailActivity : BaseBindingActivity<ActivityDetailBinding, DetailViewModel>() {

    companion object {
        const val RESULT_LOAD_IMG = 1997
        const val READ_STORAGE_PERMISSION_REQUEST_CODE = 2807
        const val INTENT_DETAIL = "detail"

        @JvmStatic
        fun prepareIntent(context: Context, recipe: Recipe): Intent =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(INTENT_DETAIL, recipe)
            }

        @JvmStatic
        fun prepareIntent(context: Context): Intent = Intent(context, DetailActivity::class.java)
    }

    private val listener = View.OnFocusChangeListener { v, hasFocus ->
        if (!hasFocus) {
            hideKeyboard(v)
        }
    }

    override val layoutId: Int = R.layout.activity_detail

    override val viewModelClass: KClass<DetailViewModel> = DetailViewModel::class

    val recipe: Recipe? by lazy {
        intent.getParcelableExtra(INTENT_DETAIL) as Recipe?
    }

    override fun init(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            if (recipe == null) {
                viewModel.setIsDetailView(false)
                viewModel.setIsUpdating(true)
            } else {
                viewModel.setIsDetailView(true)
                if (viewModel.firstRecipe == null)
                    viewModel.firstRecipe = recipe
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setUpChooseImage()
        setUpEditText()
        //default
        Glide.with(this)
            .load(R.drawable.image_holder)
            .into(binding.imvRecipe)

        viewModel.listRecipeTypeLiveData.observe(this, Observer {
            setUpRecipeTypes(it)
        })
        viewModel.finishActivityLiveData.observe(this, Observer {
            if (it) {
                this.finish()
            }
        })
        viewModel.isEmptyLiveData.observe(this, Observer {
            if (it) {
                Toast.makeText(this, resources.getString(R.string.check_fields), Toast.LENGTH_SHORT)
                    .show()
            }
        })
        viewModel.isSuccessfullyLiveData.observe(this, Observer {
            when (it) {
                ADD_NEW_RECIPE -> {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.add_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                UPDATE_RECIPE -> {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.update_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                DELETE_RECIPE -> {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.delete_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun setUpEditText() {
        binding.edtName.onFocusChangeListener = listener
        binding.edtIngredients.onFocusChangeListener = listener
        binding.edtSteps.onFocusChangeListener = listener
    }

    private fun setUpChooseImage() {
        binding.btnChooseImg.setOnClickListener {
            if (!checkPermissionForReadExtertalStorage())
                requestPermissionForReadExternalStorage()
            else {
                goToImageGallery()
            }
        }
    }

    private fun checkPermissionForReadExtertalStorage(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result = this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    private fun requestPermissionForReadExternalStorage() {
        try {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE_PERMISSION_REQUEST_CODE
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                goToImageGallery()
            }
        }
    }

    private fun setUpRecipeTypes(list: List<RecipeType>) {
        val recipeTypes = mutableListOf<String>()
        recipeTypes.addAll(list.map {
            it.title
        })
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, recipeTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerType.adapter = adapter
        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //nothing
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setTypeId(list[position].id)
            }
        }

        viewModel.isUpdatingLiveData.observe(this, Observer {
            binding.spinnerType.isEnabled = it
        })

        viewModel.typeIdLiveData.observe(this, Observer { typeId ->
            list.find {
                it.id == typeId
            }?.let {
                binding.spinnerType.setSelection(list.indexOf(it))
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (recipe != null)
            menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.menu_edit -> {
                if (viewModel.isUpdatingLiveData.value != true)
                    Toast.makeText(this, resources.getString(R.string.edit_now), Toast.LENGTH_SHORT)
                        .show()
                viewModel.setIsUpdating(true)
                true
            }
            R.id.menu_delete -> {
                AlertDialog.Builder(this).apply {
                    setMessage(R.string.confirm_delete)
                    setPositiveButton(R.string.delete) { dialog, _ ->
                        viewModel.deleteRecipe()
                        dialog.dismiss()
                    }
                    setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    setCancelable(false)
                    show()
                }
                true
            }
            android.R.id.home -> {
                this.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onActivityResult(reqCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(reqCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            try {
                val imageUri = data!!.data
                getRealPathFromURI(imageUri!!).let {
                    viewModel.setImg(it)
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, resources.getText(R.string.pick_image_alert), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun getRealPathFromURI(contentURI: Uri): String {
        var result = ""
        val cursor = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            contentURI.path?.let {
                result = it
            }
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    private fun goToImageGallery() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(
            photoPickerIntent,
            RESULT_LOAD_IMG
        )
    }
}