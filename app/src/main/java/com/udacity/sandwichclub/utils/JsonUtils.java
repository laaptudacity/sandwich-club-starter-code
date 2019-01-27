package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        if (json != null && !json.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject nameObject = jsonObject.getJSONObject("name");
                String mainName = nameObject.getString("mainName");
                JSONArray alsoKnownAsJsonArray = nameObject.getJSONArray("alsoKnownAs");
                List<String> alsoKnownAsItems = extractStringsFromJsonArray(alsoKnownAsJsonArray);

                String placeOfOrigin = jsonObject.getString("placeOfOrigin");
                String description = jsonObject.getString("description");
                String image = jsonObject.getString("image");

                JSONArray ingredientsArray = jsonObject.getJSONArray("ingredients");
                List<String> ingredients = extractStringsFromJsonArray(ingredientsArray);

                return new Sandwich(mainName, alsoKnownAsItems, placeOfOrigin, description, image, ingredients);
            } catch (JSONException jsonException) {
                Timber.e("Error parsing Json due to : %s", jsonException.getMessage());
            }
        }
        return null;
    }

    private static List<String> extractStringsFromJsonArray(JSONArray jsonArray) throws JSONException {
        List<String> jsonItems = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            jsonItems.add(jsonArray.getString(i));
        }
        return jsonItems;
    }
}
