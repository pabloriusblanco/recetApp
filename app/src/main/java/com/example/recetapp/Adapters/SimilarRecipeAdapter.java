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
import com.example.recetapp.Listeners.SimilarRecipeListener;
import com.example.recetapp.R;
import com.example.recetapp.models.SimilarRecipeResponse;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SimilarRecipeAdapter extends RecyclerView.Adapter<SimilarRecipeViewHolder> {

    Context context;
    List<SimilarRecipeResponse> list;

    // Listener para obtener los detalles de las recetas similares
    RecipeDetailsClickListener listener;

    public SimilarRecipeAdapter(Context context, List<SimilarRecipeResponse> listSimilarRecipes, RecipeDetailsClickListener listener) {
        this.context = context;
        this.list = listSimilarRecipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SimilarRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SimilarRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_similar_recipe, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull SimilarRecipeViewHolder holder, int position) {
        holder.title_similarRecipes.setText(list.get(position).title);
        holder.similar_timeText.setText(list.get(position).readyInMinutes  + " minutos");
        holder.similar_servingsText.setText(list.get(position).servings + " porciones");
        // Para obtener las imagenes se puede usar una combinacion de url + id
        // https://spoonacular.com/food-api/docs#Show-Images
        Picasso.get()
                .load( "https://spoonacular.com/recipeImages/" + list.get(position).id + "-90x90." + list.get(position).imageType)
                .into(holder.similar_imagePlate);

        //Listener que pasa el id de la receta clickeada. Definido el id como string desde la interfaz listener
        holder.similar_recipe_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecipeClick(String.valueOf(list.get(holder.getAdapterPosition()).id));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class SimilarRecipeViewHolder extends RecyclerView.ViewHolder {

    LinearLayout similar_recipe_holder;
    ShapeableImageView similar_imagePlate;
    TextView title_similarRecipes, similar_timeText, similar_servingsText;

    public SimilarRecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        // Inicializar elementos
        similar_recipe_holder = itemView.findViewById(R.id.similar_recipe_holder);
        similar_imagePlate = itemView.findViewById(R.id.similar_imagePlate);
        title_similarRecipes = itemView.findViewById(R.id.title_similarRecipes);
        similar_timeText = itemView.findViewById(R.id.similar_timeText);
        similar_servingsText = itemView.findViewById(R.id.similar_servingsText);
    }
}
