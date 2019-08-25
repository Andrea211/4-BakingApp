package com.example.bakingapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.adapters.IngredientsAdapter;
import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.utils.ConstantsUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsActivity extends AppCompatActivity {

    private static final String RECIPE_LIST_STATE = "recipe_details_list";
    private static final String RECIPE_JSON_STATE = "recipe_json_list";

    @BindView(R.id.rv_recipe_ingredients)
    RecyclerView mRecyclerView;

    private ArrayList<Recipe> mRecipeArrayList;
    private String mJsonResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ButterKnife.bind(this);

        IngredientsAdapter mIngredientsAdapter;
        List<Ingredient> mIngredientList;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getStringExtra(ConstantsUtil.WIDGET_EXTRA) != null) {
            SharedPreferences sharedpreferences =
                    getSharedPreferences(ConstantsUtil.SHARED_PREF, MODE_PRIVATE);
            String jsonRecipe = sharedpreferences.getString(ConstantsUtil.JSON_RESULT_EXTRA, "");
            mJsonResult = jsonRecipe;

            Gson gson = new Gson();
            Recipe recipe = gson.fromJson(jsonRecipe, Recipe.class);

            mIngredientList = recipe.getIngredients();

        } else {

            if (savedInstanceState != null) {
                mRecipeArrayList = savedInstanceState.getParcelableArrayList(RECIPE_LIST_STATE);
                mJsonResult = savedInstanceState.getString(RECIPE_JSON_STATE);
                mIngredientList = mRecipeArrayList.get(0).getIngredients();
            } else {
                // Get recipe from intent extra
                Intent recipeIntent = getIntent();
                mRecipeArrayList = recipeIntent.getParcelableArrayListExtra(ConstantsUtil.RECIPE_INTENT_EXTRA);
                mJsonResult = recipeIntent.getStringExtra(ConstantsUtil.JSON_RESULT_EXTRA);
                mIngredientList = mRecipeArrayList.get(0).getIngredients();
            }
        }

        mIngredientsAdapter = new IngredientsAdapter(this, mIngredientList);

        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mIngredientsAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_LIST_STATE, mRecipeArrayList);
        outState.putString(RECIPE_JSON_STATE, mJsonResult);
    }
}
