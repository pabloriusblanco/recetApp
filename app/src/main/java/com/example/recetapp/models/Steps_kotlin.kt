package com.example.recetapp.models

import Ingredients_kotlin

data class Steps_kotlin (
	val number : Int,
	val step : String,
	val ingredients : List<Ingredients_kotlin>,
	val equipment : List<Equipment_kotlin>
)