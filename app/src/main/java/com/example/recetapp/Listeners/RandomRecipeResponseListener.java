package com.example.recetapp.Listeners;

import com.example.recetapp.models.RandomApiRes;

public interface RandomRecipeResponseListener {
    void didFetch(RandomApiRes response, String message);
    void didError(String message);
}
