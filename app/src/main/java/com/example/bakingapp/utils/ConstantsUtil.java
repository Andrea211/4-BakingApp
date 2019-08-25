package com.example.bakingapp.utils;

import com.example.bakingapp.R;

public class ConstantsUtil {

    public static final String JSON_RESULT_EXTRA = "json_result_extra";
    public static final String WIDGET_EXTRA = "widget_extra";
    public static final String RECIPE_INTENT_EXTRA = "recipe_intent_extra";
    public static final String STEP_SINGLE = "step_single";
    public static final String STEP_INTENT_EXTRA = "step_intent_extra";
    public static final String SHARED_PREF = "shared_pref";

    public static final String[] units = {"CUP", "TBLSP", "TSP", "G", "K", "OZ", "UNIT"};
    public static final String[] unitName = {"Cup", "Tablespoon", "Teaspoon", "Gram", "Kilogram", "Ounce", "Unit"};
    public static final int[] unitIcons = {
            R.drawable.measure_cup,
            R.drawable.measure_tablespoon,
            R.drawable.measure_teaspoon,
            R.drawable.measure_g,
            R.drawable.measure_kg,
            R.drawable.measure_oz,
            R.drawable.measure_unit
    };
    public static final int[] recipeIcons = {
            R.drawable.nutella_pie,
            R.drawable.brownie,
            R.drawable.yellow_cake,
            R.drawable.cheesecake
    };
}
