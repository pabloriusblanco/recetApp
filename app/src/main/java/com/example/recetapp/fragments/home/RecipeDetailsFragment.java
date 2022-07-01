package com.example.recetapp.fragments.home;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recetapp.Adapters.IngredientsAdapter;
import com.example.recetapp.Adapters.SimilarRecipeAdapter;
import com.example.recetapp.Listeners.LoadingDialog;
import com.example.recetapp.Listeners.RecipeDetailsClickListener;
import com.example.recetapp.Listeners.RecipeDetailsResponseListener;
import com.example.recetapp.Listeners.SimilarRecipeListener;
import com.example.recetapp.R;
//import com.example.recetapp.RecipeDetailsActivity;
import com.example.recetapp.RequestManager;
import com.example.recetapp.models.RecipeDetailsRes;
import com.example.recetapp.models.SimilarRecipeResponse;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeDetailsFragment extends Fragment {

    //Id de la receta, utilizado en la llamada a la api
    int recipeID;

    // Views que se van a utilizar
    TextView detail_titleCard, detail_summary, detail_timeText, detail_likesText, detail_servingsText;
    ImageView detail_imagePlate, like_button;
    RecyclerView recycler_details_ingredientsColumns, recycler_details_similarRecipes;

    //Objeto manager de las llamadas a API
    RequestManager manager;

    //Dialog
    LoadingDialog dialog;

    //Favorites
    Boolean favorite = false;

    //Adaptador para la lista de ingredientes
    IngredientsAdapter ingredientsAdapter;

    //Adaptador para la lista de recetas similares
    SimilarRecipeAdapter similarRecipeAdapter;

    public RecipeDetailsFragment() {}

    public static RecipeDetailsFragment newInstance() {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        // Instancia de dialogo de progreso
        dialog = new LoadingDialog(getContext());
        dialog.ShowDialog("Cargando detalles...");

        // Instanciar los elementos
        detail_titleCard = v.findViewById(R.id.detail_titleCard);
        detail_imagePlate = v.findViewById(R.id.detail_imagePlate);
        like_button = v.findViewById(R.id.like_button);
        detail_summary = v.findViewById(R.id.detail_summaryText);
        detail_timeText = v.findViewById(R.id.detail_timeText);
        detail_likesText = v.findViewById(R.id.detail_likesText);
        detail_servingsText = v.findViewById(R.id.detail_servingsText);
        recycler_details_ingredientsColumns = v.findViewById(R.id.details_ingredientsColumns);
        recycler_details_similarRecipes = v.findViewById(R.id.recycler_similarRecipes);

        // Obtener el id a partir de los argumentos que se le paso al fragment a partir del click.
        recipeID = Integer.parseInt(getArguments().getString("id_recipe"));

        // Revisar si la receta fue likeada para cambiar la estrella
        if (isFavorite(recipeID)){
            like_button.setImageResource(R.drawable.ic_starlike_filled);
            favorite = true;
        }

        // Intanciar el manager y setear el listener de los detalles y recetas similares
        manager = new RequestManager(getContext());
        manager.getRecipeDetails(recipeDetailsResponseListener,recipeID);
        manager.getSimilarRecipes(similarRecipeListener, recipeID);

        // Listener para guardar favoritos
        like_button.setOnClickListener(view -> {
            toogleLikeRecipe(recipeID, favorite);


        });

        return v;
    }

    private final RecipeDetailsResponseListener recipeDetailsResponseListener = new RecipeDetailsResponseListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void didFetch(RecipeDetailsRes response, String message) {
            // Si se obtuvo la respuesta, popular las vistas.
            detail_titleCard.setText(response.title);
            Picasso.get().load(response.image).into(detail_imagePlate);
            detail_summary.setText(Html.fromHtml(response.summary, Html.FROM_HTML_MODE_COMPACT));
            detail_timeText.setText(response.readyInMinutes + " minutos");
            detail_likesText.setText(response.aggregateLikes + " likes");
            detail_servingsText.setText(response.servings + " porciones");


            // Inicializar la lista, definir el layout y propiedades
            recycler_details_ingredientsColumns.setHasFixedSize(true);
            recycler_details_ingredientsColumns.setLayoutManager(new GridLayoutManager(getContext(),1));
            recycler_details_ingredientsColumns.setNestedScrollingEnabled(false);

            // Inicializar el adaptador de la lista
            ingredientsAdapter = new IngredientsAdapter(getContext(), response.extendedIngredients);
            recycler_details_ingredientsColumns.setAdapter(ingredientsAdapter);
            dialog.HideDialog();
        }

        @Override
        public void didError(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    };

    private final SimilarRecipeListener similarRecipeListener = new SimilarRecipeListener() {
        @Override
        public void didFetch(List<SimilarRecipeResponse> response, String message) {
            // Inicializar la lista, definir el layout y propiedades
            recycler_details_similarRecipes.setHasFixedSize(true);
            recycler_details_similarRecipes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

            // Inicializar el adaptador de la lista
            similarRecipeAdapter = new SimilarRecipeAdapter(getContext(), response, recipeDetailsClickListener);
            recycler_details_similarRecipes.setAdapter(similarRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    };

    private final RecipeDetailsClickListener recipeDetailsClickListener = new RecipeDetailsClickListener() {
        @Override
        public void onRecipeClick(String id) {
            Fragment fragment = RecipeDetailsFragment.newInstance();
            Bundle arguments = new Bundle();
            arguments.putString("id_recipe", id);
            fragment.setArguments(arguments);
            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit)
                    .addToBackStack(null)
                    .replace(R.id.home_content, fragment)
                    .commit();
        }
    };

    private boolean isFavorite(int id){
        SharedPreferences pref = getContext().getSharedPreferences("favorites", Context.MODE_PRIVATE);
        Set<String> favoritesList = pref.getStringSet("favorites",null);
        if (favoritesList == null) return false;
        else{
            return favoritesList.contains(String.valueOf(id));
        }
    }

    private void toogleLikeRecipe(int id, Boolean isFavorite){
        // Obtener el objeto de preferencias
        SharedPreferences pref = getContext().getSharedPreferences("favorites", Context.MODE_PRIVATE);

        // Crear un editor
        SharedPreferences.Editor editor = pref.edit();

        // Obtener la lista de strings de preferencias
        Set<String> favoritesList = new HashSet<String>(pref.getStringSet("favorites",null));


        if (!isFavorite){
            // Lo agrega a favoritos
            favoritesList.add(String.valueOf(id));
            like_button.setImageResource(R.drawable.ic_starlike_filled);
            favorite = true;
            animateLike();
            Toast.makeText(getContext(), "Se quitó en favoritos", Toast.LENGTH_LONG).show();
        }
        else{
            // Si ya es favorito, lo remueve
            favoritesList.remove(String.valueOf(id));
            like_button.setImageResource(R.drawable.ic_starlike_outline);
            favorite = false;
            animateLike();
            Toast.makeText(getContext(), "Se guardó en favoritos", Toast.LENGTH_LONG).show();
        }

        // Put de la nueva lista
        editor.putStringSet("favorites", favoritesList);
        editor.apply();
    }

    private void animateLike(){
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(like_button,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setRepeatCount(1);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.start();
    }
}