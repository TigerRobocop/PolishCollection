package com.unibratec.livia.polishcollection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.unibratec.livia.polishcollection.Data.DAO;
import com.unibratec.livia.polishcollection.Model.Polish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Livia on 31/10/2015.
 */
public class LocalFragment extends ListFragment {

    List<Polish> mListPolish;
    PolishAdapter mAdapter;
    Bus mBus;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        mListPolish = new ArrayList<Polish>();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBus = ((PolishApp)getActivity().getApplication()).getBus();
        mBus.register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBus.unregister(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mListPolish.isEmpty()) {
          // LoadList();
        }

        clearSearch();
    }

    public void clearSearch(){
        mAdapter = new PolishAdapter(getActivity(), mListPolish);
        setListAdapter(mAdapter);
    }

    public void find(String filter){
        if (filter == null || filter.trim().equals("")){
            clearSearch();
            return;
        }

        List<Polish> filterResult = new ArrayList<Polish>(mListPolish);

        for (int i = filterResult.size() -1; i >= 0; i--){
            Polish p = filterResult.get(i);
            if (!p.brand.toUpperCase().contains(filter.toUpperCase())){
                filterResult.remove(i);
            }
        }
        mAdapter = new PolishAdapter(getActivity(), filterResult);
        setListAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadList();
    }

    void LoadList() {
        DAO _dao = new DAO(getActivity());

        mListPolish.clear();
        mListPolish.addAll(_dao.getAll());
        mAdapter.notifyDataSetChanged();

        if (getResources().getBoolean(R.bool.tablet)) {
            if (mListPolish.size() > 0){
                LoadFirstItem(mListPolish.get(0));
            }
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Polish p = (Polish) l.getItemAtPosition(position);
        LoadFirstItem(p);
    }

    private void LoadFirstItem(Polish p) {
        if (getActivity() instanceof OnPolishClick) {
            ((OnPolishClick) getActivity()).polishClick(p);
        }
    }


    @Subscribe
    public void UpdateLocalList(Polish p){
        LoadList();
    }
}
