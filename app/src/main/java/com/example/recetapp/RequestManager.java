package com.example.recetapp;

import android.content.Context;

import com.example.recetapp.Listeners.RandomRecipeResponseListener;
import com.example.recetapp.Listeners.RecipeDetailsResponseListener;
import com.example.recetapp.Listeners.SimilarRecipeListener;
import com.example.recetapp.models.RandomApiRes;
import com.example.recetapp.models.RecipeDetailsRes;
import com.example.recetapp.models.SimilarRecipeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RequestManager {
    Context context;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    public void getRandomRecipes(RandomRecipeResponseListener listener, List<String> tags){
        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
        Call<RandomApiRes> call = callRandomRecipes.callRandomRecipe(context.getString(R.string.api_key), "10", tags);
        call.enqueue(new Callback<RandomApiRes>() {
            @Override
            public void onResponse(Call<RandomApiRes> call, Response<RandomApiRes> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RandomApiRes> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    };

    public void getRecipeDetails(RecipeDetailsResponseListener listener, int id){
        CallRecipeDetails callRecipeDetails = retrofit.create(CallRecipeDetails.class);
        Call<RecipeDetailsRes> call = callRecipeDetails.callRecipeDetails(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<RecipeDetailsRes>() {
            @Override
            public void onResponse(Call<RecipeDetailsRes> call, Response<RecipeDetailsRes> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RecipeDetailsRes> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    };

    public void getSimilarRecipes(SimilarRecipeListener listener, int id){
        CallSimilarRecipes callSimilarRecipes = retrofit.create((CallSimilarRecipes.class));
        Call<List<SimilarRecipeResponse>> call = callSimilarRecipes.callSimilarRecipe(id, "3", context.getString(R.string.api_key));
        call.enqueue(new Callback<List<SimilarRecipeResponse>>() {
            @Override
            public void onResponse(Call<List<SimilarRecipeResponse>> call, Response<List<SimilarRecipeResponse>> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<List<SimilarRecipeResponse>> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });

    }

    //    Usa el model RandomApiRes
    private interface CallRandomRecipes{
        @GET("recipes/random")
        Call<RandomApiRes> callRandomRecipe(
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("tags") List<String> tags
        );
    }

    //    Usa el model RecipeDetailsRes
    private interface CallRecipeDetails{
        @GET("recipes/{id}/information")
        Call<RecipeDetailsRes> callRecipeDetails(
                @Path("id") int id,
                @Query("apiKey") String apiKey
        );
    }

    //  Usa el model SimilarRecipeResponse
    private interface CallSimilarRecipes{
        @GET("/recipes/{id}/similar")
        Call<List<SimilarRecipeResponse>> callSimilarRecipe(
                @Path("id") int id,
                @Query("number") String number,
                @Query("apiKey") String apiKey
        );
    }
}
