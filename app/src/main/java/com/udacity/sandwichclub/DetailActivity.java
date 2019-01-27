package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private static final String SELECTED_SANDWICH = "selectedSandwich";

    @BindView(R.id.image_iv)
    ImageView ingredientsIv;
    @BindView(R.id.main_name_tv)
    TextView mainNameTv;
    @BindView(R.id.also_known_tv)
    TextView alsoKnownTv;
    @BindView(R.id.place_of_origin_tv)
    TextView placeOfOriginTv;
    @BindView(R.id.description_tv)
    TextView descriptionTv;
    @BindView(R.id.ingredients_tv)
    TextView ingredientsTv;

    Sandwich sandwich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Sandwich savedSandwich = getSandwichFromSavedState(savedInstanceState);
        if (savedSandwich != null) {
            sandwich = savedSandwich;
            populateUI(sandwich);
            return;
        }

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        setTitle(sandwich.getMainName());
        Picasso.with(this)
            .load(sandwich.getImage())
            .error(R.drawable.ic_loader)
            .placeholder(R.drawable.ic_loader)
            .into(ingredientsIv);
        mainNameTv.setText(sandwich.getMainName());
        alsoKnownTv.setText(getCombinedStringFromList(sandwich.getAlsoKnownAs(), "\n"));
        placeOfOriginTv.setText(sandwich.getPlaceOfOrigin());
        descriptionTv.setText(sandwich.getDescription());
        ingredientsTv.setText(getCombinedStringFromList(sandwich.getIngredients(), "\n"));

    }

    private String getCombinedStringFromList(List<String> items, String separator) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : items) {
            stringBuilder.append(item);
            stringBuilder.append(separator);
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (sandwich != null) {
            outState.putParcelable(SELECTED_SANDWICH, Parcels.wrap(sandwich));
        }
    }

    private Sandwich getSandwichFromSavedState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_SANDWICH)) {
            return Parcels.unwrap(savedInstanceState.getParcelable(SELECTED_SANDWICH));
        }
        return null;
    }
}
