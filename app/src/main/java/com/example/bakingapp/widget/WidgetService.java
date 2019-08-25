package com.example.bakingapp.widget;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.utils.ConstantsUtil;
import com.google.gson.Gson;

import java.util.List;

public class WidgetService extends IntentService {

    private static final String ACTION_OPEN_RECIPE =
            "com.example.bakingapp.widget.yummio_widget_service";

    public WidgetService() {
        super("WidgetService");
    }

    // open proper recipe's ingredients
    public static void startActionOpenRecipe(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_OPEN_RECIPE);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "BakingApp service",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
    }

    @Override
    protected void onHandleIntent(@NonNull Intent intent) {
        final String action = intent.getAction();
        if (ACTION_OPEN_RECIPE.equals(action)) {
            handleActionOpenRecipe();
        }
    }

    private void handleActionOpenRecipe() {
        SharedPreferences sharedpreferences =
                getSharedPreferences(ConstantsUtil.SHARED_PREF, MODE_PRIVATE);

        String jsonRecipe = sharedpreferences.getString(ConstantsUtil.JSON_RESULT_EXTRA, "");

        StringBuilder stringBuilder = new StringBuilder();

        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(jsonRecipe, Recipe.class);

        int id = recipe.getId();
        int imgResId = ConstantsUtil.recipeIcons[id - 1];
        List<Ingredient> ingredientList = recipe.getIngredients();

        for (Ingredient ingredient : ingredientList) {
            String quantity = String.valueOf(ingredient.getQuantity());
            String measure = ingredient.getMeasure();
            String ingredientName = ingredient.getIngredient();
            String line = quantity + " " + measure + " " + ingredientName;
            stringBuilder.append(line + "\n");
        }
        String ingredientsString = stringBuilder.toString();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WidgetProvider.class));

        WidgetProvider.updateWidgetRecipe(this, ingredientsString, imgResId, appWidgetManager, appWidgetIds);
    }
}
