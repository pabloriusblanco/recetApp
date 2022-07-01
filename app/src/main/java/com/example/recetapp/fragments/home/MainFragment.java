package com.example.recetapp.fragments.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.recetapp.Adapters.RandomRecipeAdapter;
import com.example.recetapp.Listeners.LoadingDialog;
import com.example.recetapp.Listeners.RandomRecipeResponseListener;
import com.example.recetapp.Listeners.RecipeDetailsClickListener;
import com.example.recetapp.R;
//import com.example.recetapp.RecipeDetailsActivity;
import com.example.recetapp.RequestManager;
import com.example.recetapp.models.RandomApiRes;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    //Dialog
    LoadingDialog dialog;

    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;
    SearchView searchView;
    List<String> tags = new ArrayList<>();

    public MainFragment() {}

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = v.findViewById(R.id.recycler_randomRecipe);
        // Los recycler View necesitan un adapter cuando la informacion es asincronica, si no tira un error.
        // Aca estoy llamando a un constructor vacio para que no tire error que despues va a ser reemplazado.
        setTempAdapteronReciclerView();

        // Instancia de dialogo de progreso
        dialog = new LoadingDialog(getContext());
        dialog.ShowDialog("Cargando recetas...");

        manager = new RequestManager(getContext());
        manager.getRandomRecipes(randomRecipeResponseListener, tags);
        return v;
    }

    public final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomApiRes response, String message) {
            dialog.HideDialog();
            randomRecipeAdapter = new RandomRecipeAdapter(getContext(), response.recipes, recipeDetailsClickListener);
            recyclerView.setAdapter(randomRecipeAdapter);
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
//            startActivity(new Intent(getContext(), RecipeDetailsActivity.class).putExtra("id", id));
        }
    };

    private void setTempAdapteronReciclerView(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        });
    }

}