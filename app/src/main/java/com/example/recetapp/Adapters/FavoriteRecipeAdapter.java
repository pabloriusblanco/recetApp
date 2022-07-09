package com.example.recetapp.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recetapp.Listeners.RecipeDetailsClickListener;
import com.example.recetapp.Listeners.RemoveFavoriteClickListener;
import com.example.recetapp.R;
import com.example.recetapp.models.Recipe;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteRecipeAdapter extends RecyclerView.Adapter<FavoriteRecipeViewHolder> {

    Context context;
    List<Recipe> recipesList;

    // Listener para obtener los detalles de las recetas favoritees
    RecipeDetailsClickListener listener;

    // Listener para obtener los detalles de las recetas favoritees
    RemoveFavoriteClickListener listenerRemoveButton;

    public FavoriteRecipeAdapter(Context context, List<Recipe> recipesList, RecipeDetailsClickListener listener, RemoveFavoriteClickListener listenerRemoveButton) {
        this.context = context;
        this.recipesList = recipesList;
        this.listener = listener;
        this.listenerRemoveButton = listenerRemoveButton;
    }

    @NonNull
    @Override
    public FavoriteRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.favorites_recipes, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull FavoriteRecipeViewHolder holder, int position) {
        holder.title_favoriteRecipes.setText(recipesList.get(position).title);
        holder.favorite_timeText.setText(recipesList.get(position).readyInMinutes  + " minutos");
        holder.favorite_servingsText.setText(recipesList.get(position).servings + " porciones");
        // Para obtener las imagenes se puede usar una combinacion de url + id
        // https://spoonacular.com/food-api/docs#Show-Images
        Picasso.get()
                .load( "https://spoonacular.com/recipeImages/" + recipesList.get(position).id + "-90x90." + recipesList.get(position).imageType)
                .into(holder.favorite_imagePlate);

        //Listener que pasa el id de la receta clickeada para detalles. Definido el id como string desde la interfaz listener
        holder.favorite_recipe_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecipeClick(String.valueOf(recipesList.get(holder.getAdapterPosition()).id));
            }
        });

        //Listener que pasa el id de la receta clickeada para ser removido de favoritos.
        holder.favorite_removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.favorite_recipe_holder.setVisibility(View.GONE);
                listenerRemoveButton.onButtonRemoveClick(String.valueOf(recipesList.get(holder.getAdapterPosition()).id));
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }
}

class FavoriteRecipeViewHolder extends RecyclerView.ViewHolder {

    LinearLayout favorite_recipe_holder;
    ShapeableImageView favorite_imagePlate;
    TextView title_favoriteRecipes, favorite_timeText, favorite_servingsText;
    CardView favorite_removeButton;

    public FavoriteRecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        // Inicializar elementos
        favorite_recipe_holder = itemView.findViewById(R.id.favorite_recipe_holder);
        favorite_imagePlate = itemView.findViewById(R.id.favorite_imagePlate);
        title_favoriteRecipes = itemView.findViewById(R.id.title_favoriteRecipes);
        favorite_timeText = itemView.findViewById(R.id.favorite_timeText);
        favorite_servingsText = itemView.findViewById(R.id.favorite_servingsText);
        favorite_removeButton = itemView.findViewById(R.id.favorite_removeButton);
    }
}
