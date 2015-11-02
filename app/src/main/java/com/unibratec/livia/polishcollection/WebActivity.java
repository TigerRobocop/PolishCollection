package com.unibratec.livia.polishcollection;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.unibratec.livia.polishcollection.Model.Polish;

public class WebActivity extends AppCompatActivity implements OnPolishClick
        , SearchView.OnQueryTextListener
        , MenuItemCompat.OnActionExpandListener {

    private static final String WEB_FRAGMENT = "webfragment";
    private FragmentManager mFragmentManager;
    private WebFragment mWebFragment;
    private LocalFragment mLocalFragment;
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

        //  String tag = makeFragmentName(R.id.viewpager_main, 0);
        //  mWebFragment = (WebFragment)getSupportFragmentManager().findFragmentByTag(tag);
    }


    private static String makeFragmentName(int viewPagerId, int index) {
        return "android:switcher:" + viewPagerId + ":" + index;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search_by_brand_hint));
        MenuItemCompat.setOnActionExpandListener(searchItem, this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        mWebFragment.clearSearch();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mWebFragment.find(newText);
        return false;
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

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            // save the appropriate reference depending on position
            switch (position) {
                case 0:
                    mWebFragment = (WebFragment) createdFragment;
                    break;
                case 1:
                    mLocalFragment = (LocalFragment) createdFragment;
                    break;
            }
            return createdFragment;
        }


    }

}
