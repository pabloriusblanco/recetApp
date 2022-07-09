package com.example.recetapp.Listeners;

import com.example.recetapp.models.FavoriteApiRes;
import com.example.recetapp.models.Recipe;

import java.util.List;

public interface FavoriteRecipeResponseListener {
    void didFetch(List<Recipe> response, String message);
    void didError(String message);
}
