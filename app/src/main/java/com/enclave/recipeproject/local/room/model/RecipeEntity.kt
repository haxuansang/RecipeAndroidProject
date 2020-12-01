package com.enclave.recipeproject.local.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_table")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "step")
    var step: String,
    @ColumnInfo(name = "type")
    var typeId: Int,
    @ColumnInfo(name = "ingredient")
    var ingredients: String,
    @ColumnInfo(name = "img")
    var img: String
)
