package com.udacity.sandwichclub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    TextView alsoKnownAsTv;
    TextView originTv;
    TextView descriptionTv;
    TextView ingredientsTv;
    TextView originTitleTv;
    TextView alsoKnownAsTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView sandwichImageIv = findViewById(R.id.image_iv);

        alsoKnownAsTv = findViewById(R.id.also_known_as_tv);
        originTv = findViewById(R.id.origin_tv);
        descriptionTv = findViewById(R.id.description_tv);
        ingredientsTv = findViewById(R.id.ingredients_tv);
        originTitleTv = findViewById(R.id.origin_title);
        alsoKnownAsTitleTv = findViewById(R.id.also_known_title);


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
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        // in order to use centerCrop() we must first write this code in Glide version 4 and up
        RequestOptions options = new RequestOptions();
        options.centerCrop();

        // use Glide to get image from url and put it in image view
        Glide.with(this)
                .load(sandwich.getImage())
                .apply(options)
                .into(sandwichImageIv);

        // Enable up navigation to parent Activity
        // find Toolbar view in layout
        Toolbar myToolbar = findViewById(R.id.toolbar);
        // Set the Toolbar as Action Bar
        setSupportActionBar(myToolbar);
        // enable up navigation to parent activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //set app bar title to match name of selected attraction
        getSupportActionBar().setTitle(sandwich.getMainName());
        // set status bar to be transparent
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        // if also known as array is null or is empty, hide these views for a better UI
        if (sandwich.getAlsoKnownAs() == null) {
            alsoKnownAsTitleTv.setVisibility(View.GONE);
            alsoKnownAsTv.setVisibility(View.GONE);
        }

        // create array to store other names of sandwich
        if (sandwich.getAlsoKnownAs() != null) {
            ArrayList<String> otherNamesArray = sandwich.getAlsoKnownAs();
            for (int i = 0; i < otherNamesArray.size(); i++) {
                // for last string in the list, do not put comma after the name
                if (i == (otherNamesArray.size() - 1)) {
                    alsoKnownAsTv.append(otherNamesArray.get(i));
                } else {
                    alsoKnownAsTv.append(otherNamesArray.get(i) + ", ");
                }

            }
        }
        // if origin string is null or is empty, hide these views for a better UI
        if (sandwich.getPlaceOfOrigin() == null || sandwich.getPlaceOfOrigin().equals("")) {
            originTitleTv.setVisibility(View.GONE);
            originTv.setVisibility(View.GONE);
        } else {
            //set place of origin of sandwich to the place of origin text view
            originTv.setText(sandwich.getPlaceOfOrigin());
        }


        //set description of sandwich to the description text view
        descriptionTv.setText(sandwich.getDescription());

        if (sandwich.getIngredients() != null) {
            // create array to store ingredients of sandwich
            ArrayList<String> ingredientsArray = sandwich.getIngredients();
            for (int i = 0; i < ingredientsArray.size(); i++) {
                ingredientsTv.append(ingredientsArray.get(i) + ", ");
            }
        }


    }

}

