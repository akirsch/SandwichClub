package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static final int EMPTY_ARRAY = 0;

    public static Sandwich parseSandwichJson(String json) {
        // create new Sandwich object to store data passed from JSON
        Sandwich sandwich = new Sandwich();

        // turn the string passed into this method into a JSON Object in order to pass its data
        try {
            JSONObject rootJSONObject = new JSONObject(json);

            // parse JSONObject to find the main name of the sandwich
            JSONObject sandwichNameObject = rootJSONObject.optJSONObject("name");
            String sandwichMainName = sandwichNameObject.getString("mainName");

            // set the main name of the sandwich if value is not null
            if (sandwichMainName != null && !sandwichMainName.equals("")){
                sandwich.setMainName(sandwichMainName);
            }

            // parse JSONObject to find the other names of the sandwich
            JSONArray sandwichOtherNamesArray = sandwichNameObject.getJSONArray("alsoKnownAs");

            ArrayList<String> alsoKnownAsList = new ArrayList<>();

            // if list isn't empty extract its values and set them to the sandwich object
            if (sandwichOtherNamesArray.length() > EMPTY_ARRAY ) {

                for (int i = 0; i < sandwichOtherNamesArray.length(); i++) {
                    // get string at current position in the JSONArray of alsoKnownAs names
                    String otherName = sandwichOtherNamesArray.getString(i);

                    // add this name to ArrayList
                    alsoKnownAsList.add(otherName);
                }
                // set this list of names to the sandwich object
                sandwich.setAlsoKnownAs(alsoKnownAsList);

                }

            // parse the JSON to find place of origin and set the value to the Sandwich object
            String placeOfOrigin = rootJSONObject.getString("placeOfOrigin");
            sandwich.setPlaceOfOrigin(placeOfOrigin);

            // do the same for the description
            String sandwichDescription = rootJSONObject.getString("description");
            sandwich.setDescription(sandwichDescription);

            // and the the url for the image
            String imageUrl = rootJSONObject.getString("image");
            sandwich.setImage(imageUrl);

            // and finally for the list of ingredients
            JSONArray ingredientsJSONArray = rootJSONObject.getJSONArray("ingredients");

            // if list isn't empty extract its values and set them to the sandwich object
            if (ingredientsJSONArray.length() > EMPTY_ARRAY ) {

                ArrayList<String> ingredientsList = new ArrayList<>();

                for (int i = 0; i < ingredientsJSONArray.length(); i++) {
                    // get string at current position in the JSONArray of ingredients
                    String ingredient = ingredientsJSONArray.getString(i);

                    // add this name to ArrayList
                    ingredientsList.add(ingredient);
                }
                // set this list of names to the sandwich object
                sandwich.setIngredients(ingredientsList);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sandwich;
    }
}
