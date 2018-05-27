package com.example.leonp.okfood.UserAccount.Account.PublishPost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.leonp.okfood.R;
import com.example.leonp.okfood.UserAccount.Account.Models.Ingredient;

import java.util.ArrayList;

public class AddIngredientsFragment extends Fragment {

    private static final String TAG = "AddIngredientsFragment";

    // widgets
    private ListView listViewIngredients;

    // vars
    private IngredientsAdapter mAdapter;
    private ArrayList<Ingredient> mIngredientList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addingredients, container, false);

        listViewIngredients = (ListView) view.findViewById(R.id.listViewIngredients);

        mIngredientList = new ArrayList<>();

        Ingredient tempIngredient1 = new Ingredient("Salt", "Tablespoon", "2");
        Ingredient tempIngredient2 = new Ingredient("Shrimp", "Pounds", "13");

        mIngredientList.add(tempIngredient1);
        mIngredientList.add(tempIngredient2);

        mAdapter = new IngredientsAdapter(getContext(), R.layout.layout_ingredient_item, mIngredientList);

        listViewIngredients.setAdapter(mAdapter);

        return view;
    }

}
