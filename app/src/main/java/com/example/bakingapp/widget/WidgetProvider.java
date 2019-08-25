package com.example.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.bakingapp.R;
import com.example.bakingapp.ui.IngredientsActivity;
import com.example.bakingapp.utils.ConstantsUtil;

public class WidgetProvider extends AppWidgetProvider {

    private static void updateAppWidget(Context context, String jsonRecipeIngredients, int imgResId, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_provider);

        Intent intent = new Intent(context, IngredientsActivity.class);
        intent.putExtra(ConstantsUtil.WIDGET_EXTRA, "CAME_FROM_WIDGET");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        if (jsonRecipeIngredients.equals("")) {
            jsonRecipeIngredients = "No ingredients";
        }

        views.setTextViewText(R.id.widget_ingredients, jsonRecipeIngredients);
        views.setImageViewResource(R.id.ivWidgetRecipeIcon, imgResId);

        views.setOnClickPendingIntent(R.id.widget_ingredients, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateWidgetRecipe(Context context, String jsonRecipe, int imgResId, AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, jsonRecipe, imgResId, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetService.startActionOpenRecipe(context);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

