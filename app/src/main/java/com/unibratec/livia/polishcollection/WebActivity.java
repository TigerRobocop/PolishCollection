package com.unibratec.livia.polishcollection;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.unibratec.livia.polishcollection.Model.Polish;

public class WebActivity extends AppCompatActivity implements OnPolishClick{

    private FragmentManager mFragmentManager;
    private WebFragment mWebFragment;
    TabLayout mTb;
    ViewPager mVp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        mVp = (ViewPager) findViewById(R.id.viewpager_main);
        mVp.setAdapter(new PageAdapter(getSupportFragmentManager()));

        mTb = (TabLayout) findViewById(R.id.tablayout_tabs);
        mTb.setupWithViewPager(mVp);

        mFragmentManager = getSupportFragmentManager();
      //  mWebFragment = (WebFragment)mFragmentManager.findFragmentById(R.id.list_planets_fragment);
    }

    @Override
    public void polishClick(Polish p) {

        if (isTablet()) {
            DetailsFragment fragment = DetailsFragment.newInstance(p);

            FragmentTransaction ft = mFragmentManager.beginTransaction();

            int layuot = R.id.details_fragment;
            ft.replace(layuot, fragment, DetailsFragment.TAG_DETAILS);
            ft.commit();
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.EXTRA_POLISH, p);
            startActivity(intent);
        }
    }

    private boolean isTablet() {
        return getResources().getBoolean(R.bool.tablet);
    }

    private class PageAdapter extends FragmentPagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new WebFragment();
            } else {
                return new LocalFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        public CharSequence getPageTitle(int pos) {
            if (pos == 0) {
                return "Web";
            } else {
                return "Local";
            }
        }
    }

}
