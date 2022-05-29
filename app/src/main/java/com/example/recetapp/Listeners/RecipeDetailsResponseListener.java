package com.example.recetapp.Listeners;

import com.example.recetapp.models.RecipeDetailsRes;

public interface RecipeDetailsResponseListener {
    // Usa el modelo RecipeDetailsRes
    void didFetch(RecipeDetailsRes response, String message);
    void didError(String message);
}
