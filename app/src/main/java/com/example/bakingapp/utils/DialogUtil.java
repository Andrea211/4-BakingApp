package com.example.bakingapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.bakingapp.R;

public class DialogUtil {

    private static AlertDialog dialogWithButtons;

    public static void showDialogWithButtons(final Context context, int resId, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.no_connection_dialog, null);

        builder.setView(customView);

        CardView cardView = customView.findViewById(R.id.cv_dialog_card);
        ImageView dialogClose = customView.findViewById(R.id.iv_dialog_close);
        TextView dialogText = customView.findViewById(R.id.tv_dialog_text);
        Button dialogButtonRetry = customView.findViewById(R.id.btn_retry);
        Button dialogButtonExit = customView.findViewById(R.id.btn_exit);

        dialogText.setText(message);
        cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));

        dialogWithButtons = builder.create();

        dialogButtonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogWithButtons.dismiss();
                ((Activity) context).finish();
            }
        });

        dialogButtonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogWithButtons.dismiss();
                Intent intent = ((Activity) context).getIntent();
                ((Activity) context).finish();
                context.startActivity(intent);
            }
        });

        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogWithButtons.dismiss();
            }
        });

        dialogWithButtons.show();
    }
}
