package com.example.recetapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recetapp.Adapters.IngredientsAdapter;
import com.example.recetapp.Listeners.RecipeDetailsResponseListener;
import com.example.recetapp.models.RecipeDetailsRes;
import com.squareup.picasso.Picasso;

public class RecipeDetailsActivity extends AppCompatActivity {

    //Id de la receta, utilizado en la llamada a la api
    int recipeID;

    // Views que se van a utilizar
    TextView detail_titleCard, detail_summary, detail_timeText, detail_likesText, detail_servingsText;
    ImageView detail_imagePlate;
    RecyclerView recycler_details_ingredientsColumns;

    //Objeto manager de las llamadas a API
    RequestManager manager;

    //Dialog
    ProgressDialog dialog;

    //Adaptador para la lista de ingredientes
    IngredientsAdapter ingredientsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // Hace el find de los id
        findViews();

        // Obtener del intent la id de la receta e instanciarlo.
        recipeID = Integer.parseInt(getIntent().getStringExtra("id"));

        // Intanciar el manager
        manager = new RequestManager(this);
        manager.getRecipeDetails(recipeDetailsResponseListener,recipeID);

        //Intanciacion del dialog
        dialog = new ProgressDialog(this);
        dialog.setTitle("Cargando Detalles...");
        dialog.show();
    }

    private void findViews() {
        detail_titleCard = findViewById(R.id.detail_titleCard);
        detail_imagePlate = findViewById(R.id.detail_imagePlate);
        detail_summary = findViewById(R.id.detail_summaryText);
        detail_timeText = findViewById(R.id.detail_timeText);
        detail_likesText = findViewById(R.id.detail_likesText);
        detail_servingsText = findViewById(R.id.detail_servingsText);
        recycler_details_ingredientsColumns = findViewById(R.id.details_ingredientsColumns);
    }

    //Instanciacion del listener de detalles
    private final RecipeDetailsResponseListener recipeDetailsResponseListener = new RecipeDetailsResponseListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void didFetch(RecipeDetailsRes response, String message) {
            // Si se obtuvo la respuesta, popular las vistas.
            dialog.dismiss();
            detail_titleCard.setText(response.title);
            Picasso.get().load(response.image).into(detail_imagePlate);
            detail_summary.setText(Html.fromHtml(response.summary, Html.FROM_HTML_MODE_COMPACT));
            detail_timeText.setText(response.readyInMinutes + " minutos");
            detail_likesText.setText(response.aggregateLikes + " likes");
            detail_servingsText.setText(response.servings + " porciones");


            // Inicializar la lista, definir el layout y propiedades
            recycler_details_ingredientsColumns.setHasFixedSize(true);
            recycler_details_ingredientsColumns.setLayoutManager(new GridLayoutManager(RecipeDetailsActivity.this,1));
            recycler_details_ingredientsColumns.setNestedScrollingEnabled(false);

            // Inicializar el adaptador de la lista
            ingredientsAdapter = new IngredientsAdapter(RecipeDetailsActivity.this, response.extendedIngredients);
            recycler_details_ingredientsColumns.setAdapter(ingredientsAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };
}