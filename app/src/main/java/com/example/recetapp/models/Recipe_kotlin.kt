package com.example.recetapp.models

import ExtendedIngredients_kotlin

data class Recipe_kotlin (

	val vegetarian : Boolean,
	val vegan : Boolean,
	val glutenFree : Boolean,
	val dairyFree : Boolean,
	val veryHealthy : Boolean,
	val cheap : Boolean,
	val veryPopular : Boolean,
	val sustainable : Boolean,
	val weightWatcherSmartPoints : Int,
	val gaps : String,
	val lowFodmap : Boolean,
	val aggregateLikes : Int,
	val spoonacularScore : Int,
	val healthScore : Int,
	val creditsText : String,
	val license : String,
	val sourceName : String,
	val pricePerServing : Double,
	val extendedIngredients : List<ExtendedIngredients_kotlin>,
	val id : Int,
	val title : String,
	val readyInMinutes : Int,
	val servings : Int,
	val sourceUrl : String,
	val image : String,
	val imageType : String,
	val summary : String,
	val cuisines : List<String>,
	val dishTypes : List<String>,
	val diets : List<String>,
	val occasions : List<String>,
	val instructions : String,
	val analyzedInstructions : List<AnalyzedInstructions_kotlin>,
	val originalId : String,
	val spoonacularSourceUrl : String
)