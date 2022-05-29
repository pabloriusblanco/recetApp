package com.example.recetapp.Adapters;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recetapp.Listeners.RecipeDetailsClickListener;
import com.example.recetapp.models.Recipe;
import com.example.recetapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RandomRecipeAdapter extends RecyclerView.Adapter<RandomRecipeViewHolder>{

    Context context;
    List<Recipe> recipesList;
    RecipeDetailsClickListener listenerDetails;


    //Constructor
    public RandomRecipeAdapter(Context context, List<Recipe> recipesList, RecipeDetailsClickListener listenerDetails) {
        this.context = context;
        this.recipesList = recipesList;
        this.listenerDetails = listenerDetails;
    }

    @NonNull
    @Override
    public RandomRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RandomRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_random_recipe, parent,false));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RandomRecipeViewHolder holder, int position) {
        holder.card_titleText.setText(recipesList.get(position).title);
        holder.card_titleText.setSelected(true);
        holder.card_summary.setText(Html.fromHtml(recipesList.get(position).summary, Html.FROM_HTML_MODE_COMPACT));
        holder.recipe_likesText.setText(recipesList.get(position).aggregateLikes + " likes");
        holder.recipe_servingsText.setText(recipesList.get(position).servings + " porciones");
        holder.recipe_timeText.setText(recipesList.get(position).readyInMinutes + " minutos");
        Picasso.get().load(recipesList.get(position).image).into(holder.image_plate);

        //Listener que pasa el id de la receta clickeada. Definido el id como string desde la interfaz listener
        holder.random_list_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerDetails.onRecipeClick(String.valueOf(recipesList.get(holder.getAdapterPosition()).id));
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }
}

class RandomRecipeViewHolder extends RecyclerView.ViewHolder {
    CardView random_list_container;
    TextView card_titleText, card_summary, recipe_likesText, recipe_servingsText, recipe_timeText;
    ImageView image_plate;


    public RandomRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        random_list_container = itemView.findViewById(R.id.random_list_container);
        card_titleText = itemView.findViewById(R.id.card_titleText);
        card_summary = itemView.findViewById(R.id.card_summary);
        recipe_likesText = itemView.findViewById(R.id.recipe_likesText);
        recipe_servingsText = itemView.findViewById(R.id.recipe_servingsText);
        recipe_timeText = itemView.findViewById(R.id.recipe_timeText);
        image_plate = itemView.findViewById(R.id.image_plate);
    }
}


