package com.example.recetapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recetapp.R;
import com.example.recetapp.models.ExtendedIngredient;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewHolder>{

    //Crear contexto
    Context context;

    // Lista de ingredientes que sale del modelo
    List<ExtendedIngredient> listIngredients;

    //Constructor
    public IngredientsAdapter(Context context, List<ExtendedIngredient> listIngredients) {
        this.context = context;
        this.listIngredients = listIngredients;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IngredientsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_ingredients,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        //Popula el ingrediente
        holder.details_ingredientText.setText(listIngredients.get(position).original);
        holder.details_ingredientText.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return listIngredients.size();
    }
}

class IngredientsViewHolder extends RecyclerView.ViewHolder {

    // Views que se van a utilizar
    TextView details_ingredientText;

    public IngredientsViewHolder(@NonNull View itemView) {
        super(itemView);

        // Inicializacion de las vistas
        details_ingredientText = itemView.findViewById(R.id.details_ingredientText);
    }
}
