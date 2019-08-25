package com.example.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.models.Step;
import com.example.bakingapp.ui.CookingActivity;
import com.example.bakingapp.ui.IngredientsActivity;
import com.example.bakingapp.utils.ConstantsUtil;
import com.example.bakingapp.widget.WidgetService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private final Context mContext;
    private final ArrayList<Recipe> mRecipeList;
    private final String mJsonResult;
    private String recipeJson;

    public RecipeListAdapter(Context context, ArrayList<Recipe> recipeList, String jsonResult) {
        this.mContext = context;
        this.mRecipeList = recipeList;
        this.mJsonResult = jsonResult;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_recipe_card, parent, false);

        return new RecipeViewHolder(view);
    }

    // Populate each recipe in a gridlayout with data
    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, int position) {

        // get proper name
        holder.recipeName.setText(mRecipeList.get(position).getName());

        // get proper image
        switch (position) {
            case 0:
                holder.recipeIcon.setImageResource(R.drawable.nutella_pie);
                break;
            case 1:
                holder.recipeIcon.setImageResource(R.drawable.brownie);
                break;
            case 2:
                holder.recipeIcon.setImageResource(R.drawable.yellow_cake);
                break;
            case 3:
                holder.recipeIcon.setImageResource(R.drawable.cheesecake);
                break;
        }

        // get number of servings
        holder.numberOfServings.setText((mRecipeList.get(position).getServings()).toString() + " servings");

        // When "INGREDIENTS"  button is clicked, get recipe details as parcelable and send to Details activity
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe recipe = mRecipeList.get(holder.getAdapterPosition());
                recipeJson = jsonToString(mJsonResult, holder.getAdapterPosition());

                Intent intent = new Intent(mContext, IngredientsActivity.class);

                ArrayList<Recipe> recipeArrayList = new ArrayList<>();
                recipeArrayList.add(recipe);

                intent.putParcelableArrayListExtra(ConstantsUtil.RECIPE_INTENT_EXTRA, recipeArrayList);
                intent.putExtra(ConstantsUtil.JSON_RESULT_EXTRA, recipeJson);
                mContext.startActivity(intent);

                SharedPreferences.Editor editor = mContext.getSharedPreferences(ConstantsUtil.SHARED_PREF, MODE_PRIVATE).edit();
                editor.putString(ConstantsUtil.JSON_RESULT_EXTRA, recipeJson);
                editor.apply();

                WidgetService.startActionOpenRecipe(mContext);
            }
        });

        // When "STEP BY STEP" button is clicked
        holder.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Step> mStepArrayList;

                Recipe recipe = mRecipeList.get(holder.getAdapterPosition());
                mStepArrayList = (ArrayList<Step>) recipe.getSteps();

                Intent intent = new Intent(mContext, CookingActivity.class);
                intent.putParcelableArrayListExtra(ConstantsUtil.STEP_INTENT_EXTRA, mStepArrayList);
                intent.putExtra(ConstantsUtil.JSON_RESULT_EXTRA, mJsonResult);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    // Get selected Recipe as Json String
    private String jsonToString(String jsonResult, int position) {
        JsonElement jsonElement = new JsonParser().parse(jsonResult);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        JsonElement recipeElement = jsonArray.get(position);
        return recipeElement.toString();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.tv_recipe_name)
        TextView recipeName;

        @Nullable
        @BindView(R.id.iv_recipe_icon)
        ImageView recipeIcon;

        @Nullable
        @BindView(R.id.number_of_servings)
        TextView numberOfServings;

        @BindView(R.id.button)
        Button button;

        @BindView(R.id.button2)
        Button button2;

        private RecipeViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}