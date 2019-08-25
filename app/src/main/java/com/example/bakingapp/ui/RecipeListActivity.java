package com.example.bakingapp.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.adapters.RecipeListAdapter;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.rest.RecipeClient;
import com.example.bakingapp.rest.RecipeService;
import com.example.bakingapp.utils.DialogUtil;
import com.example.bakingapp.utils.NetworkUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends AppCompatActivity {

    private static final String RECIPE_JSON_STATE = "recipe_json_state";
    private static final String RECIPE_ARRAY_LIST_STATE = "recipe_arraylist_state";
    private final String TAG = RecipeListActivity.class.getSimpleName();
    @BindView(R.id.rv_recipes)
    RecyclerView mRecyclerViewRecipes;
    private RecipeService mRecipeService;
    private RecipeListAdapter recipeListAdapter;
    private String mJsonResult;
    private ArrayList<Recipe> mRecipeArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            mJsonResult = savedInstanceState.getString(RECIPE_JSON_STATE);
            mRecipeArrayList = savedInstanceState.getParcelableArrayList(RECIPE_ARRAY_LIST_STATE);
            recipeListAdapter = new RecipeListAdapter(RecipeListActivity.this, mRecipeArrayList, mJsonResult);

            RecyclerView.LayoutManager mLayoutManager;
            mLayoutManager = new LinearLayoutManager(RecipeListActivity.this);

            mRecyclerViewRecipes.setLayoutManager(mLayoutManager);
            mRecyclerViewRecipes.setAdapter(recipeListAdapter);

        } else {
            if (NetworkUtil.isConnected(this)) {
                mRecipeService = new RecipeClient().mRecipeService;
                new FetchRecipesAsync().execute();
            } else {
                DialogUtil.showDialogWithButtons(this,
                        R.drawable.yellow_cake,
                        getResources().getString(R.string.no_internet_connection));
            }
        }
    }

    // Fetch recipes
    private void fetchRecipes() {
        Call<ArrayList<Recipe>> call = mRecipeService.getRecipes();

        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {

                mRecipeArrayList = response.body();

                mJsonResult = new Gson().toJson(response.body());

                recipeListAdapter = new RecipeListAdapter(RecipeListActivity.this, mRecipeArrayList, mJsonResult);
                RecyclerView.LayoutManager mLayoutManager;
                mLayoutManager = new LinearLayoutManager(RecipeListActivity.this);

                mRecyclerViewRecipes.setLayoutManager(mLayoutManager);
                mRecyclerViewRecipes.setAdapter(recipeListAdapter);

            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RECIPE_JSON_STATE, mJsonResult);
        outState.putParcelableArrayList(RECIPE_ARRAY_LIST_STATE, mRecipeArrayList);
    }

    private class FetchRecipesAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            fetchRecipes();
            return null;
        }
    }
}
