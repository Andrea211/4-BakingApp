package com.example.bakingapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.utils.ConstantsUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

    private final Context mContext;
    private final List<Ingredient> mIngredientList;

    public IngredientsAdapter(Context context, List<Ingredient> ingredientList) {
        this.mContext = context;
        this.mIngredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_ingredient_card, parent, false);

        return new IngredientsViewHolder(view);
    }

    // Bind data to create a simple ingredient item
    @Override
    public void onBindViewHolder(@NonNull final IngredientsViewHolder holder, int position) {
        Ingredient ingredient = mIngredientList.get(position);

        // name of the ingredient
        holder.ingredientName.setText(ingredient.getIngredient().toLowerCase());

        // quantity of the ingredient
        if (ingredient.getQuantity() % 1 == 0) {
            int quantity = (int) Math.round(ingredient.getQuantity());
            holder.unitNumber.setText(String.valueOf(quantity));
        } else {
            holder.unitNumber.setText(String.valueOf(ingredient.getQuantity()));
        }

        String measure = ingredient.getMeasure();
        int unitNo = 0;

        for (int i = 0; i < ConstantsUtil.units.length; i++) {
            if (measure.equals(ConstantsUtil.units[i])) {
                unitNo = i;
                break;
            }
        }
        int unitIcon = ConstantsUtil.unitIcons[unitNo];
        Log.d("UNIT_NO: ", String.valueOf(unitIcon));
        String unitLongName = ConstantsUtil.unitName[unitNo];

        // attach a proper image of the measure
        holder.unitIcon.setImageResource(unitIcon);
        holder.ingredientUnitLongName.setText(unitLongName);

        // create onClickListener to check (or uncheck) the checkbox
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.ingredientCheckbox.isChecked()) {
                    holder.ingredientCheckbox.setChecked(true);
                } else {
                    holder.ingredientCheckbox.setChecked(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIngredientList.size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.iv_unit_icon)
        ImageView unitIcon;

        @Nullable
        @BindView(R.id.tv_ingredient_name)
        TextView ingredientName;

        @Nullable
        @BindView(R.id.tv_unit_number)
        TextView unitNumber;

        @Nullable
        @BindView(R.id.tv_unit_long_name)
        TextView ingredientUnitLongName;

        @Nullable
        @BindView(R.id.ingredient_checkbox)
        CheckBox ingredientCheckbox;

        private IngredientsViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}