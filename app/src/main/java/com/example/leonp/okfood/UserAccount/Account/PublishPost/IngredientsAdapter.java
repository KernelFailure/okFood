package com.example.leonp.okfood.UserAccount.Account.PublishPost;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.leonp.okfood.R;
import com.example.leonp.okfood.UserAccount.Account.Models.Ingredient;

import java.util.List;

public class IngredientsAdapter extends ArrayAdapter<Ingredient> {

    // vars
    private LayoutInflater mInflator;
    private int mLayoutResource;
    private Context mContext;

    public IngredientsAdapter(@NonNull Context context, int resource, @NonNull List<Ingredient> objects) {
        super(context, resource, objects);
        mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResource = resource;
        mContext = context;
    }

    static class ViewHolder{

        TextView tvIngredientName;
        TextView tvQuantity;
        TextView tvUnit;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflator.inflate(mLayoutResource, parent, false);
            holder = new ViewHolder();

            holder.tvIngredientName = (TextView) convertView.findViewById(R.id.tvIngredientName);
            holder.tvQuantity = (TextView) convertView.findViewById(R.id.tvQuantity);
            holder.tvUnit = (TextView) convertView.findViewById(R.id.tvUnit);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // set actual values
        String ingredientName = getItem(position).getIngredientName();
        String quantity = getItem(position).getQuantityOfUnit();
        String unit = getItem(position).getTypeOfUnit();

        holder.tvIngredientName.setText(ingredientName);
        holder.tvQuantity.setText(quantity);
        holder.tvUnit.setText(unit);

        return convertView;
    }
}
