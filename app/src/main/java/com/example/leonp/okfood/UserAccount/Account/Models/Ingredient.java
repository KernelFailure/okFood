package com.example.leonp.okfood.UserAccount.Account.Models;

public class Ingredient {

    private String ingredientName;
    private String typeOfUnit; // e.g. mL, cup, teaspoon
    private String quantityOfUnit;

    public Ingredient(String ingredientName, String typeOfUnit, String quantityOfUnit) {
        this.ingredientName = ingredientName;
        this.typeOfUnit = typeOfUnit;
        this.quantityOfUnit = quantityOfUnit;
    }

    public Ingredient() {

    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getTypeOfUnit() {
        return typeOfUnit;
    }

    public void setTypeOfUnit(String typeOfUnit) {
        this.typeOfUnit = typeOfUnit;
    }

    public String getQuantityOfUnit() {
        return quantityOfUnit;
    }

    public void setQuantityOfUnit(String quantityOfUnit) {
        this.quantityOfUnit = quantityOfUnit;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "ingredientName='" + ingredientName + '\'' +
                ", typeOfUnit='" + typeOfUnit + '\'' +
                ", quantityOfUnit='" + quantityOfUnit + '\'' +
                '}';
    }
}
