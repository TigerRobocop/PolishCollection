package com.unibratec.livia.polishcollection;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.unibratec.livia.polishcollection.Model.Polish;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_POLISH = "polish";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Polish user = (Polish)intent.getSerializableExtra(EXTRA_POLISH);

        DetailsFragment detailsFragment = DetailsFragment.newInstance(user);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.details, detailsFragment, DetailsFragment.TAG_DETAILS);
        ft.commit();
    }
}
