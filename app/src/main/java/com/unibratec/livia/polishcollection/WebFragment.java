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
import com.unibratec.livia.polishcollection.Model.Polish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Livia on 31/10/2015.
 */
public class WebFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {
    List<Polish> mListPolish;
    PolishAdapter mAdapter;

    SwipeRefreshLayout mSwipe;
    PolishTask mTask;

    ProgressDialog mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        mListPolish = new ArrayList<Polish>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_polish, null);
        mSwipe = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        mSwipe.setOnRefreshListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mListPolish.isEmpty()) {
            LoadList();
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

    void LoadList() {
        ConnectivityManager ccnnMrg = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = ccnnMrg.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (mTask == null || mTask.getStatus() == AsyncTask.Status.FINISHED) {
                mTask = new PolishTask();
                mTask.execute();
            }
        } else {
            Toast.makeText(getActivity(), "No connection available", Toast.LENGTH_SHORT).show();
            mSwipe.setRefreshing(false);
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

    @Override
    public void onRefresh() {
        LoadList();
    }

    private class PolishTask extends AsyncTask<Void, Void, List<Polish>> {

        @Override
        protected void onPostExecute(List<Polish> polishList) {
            super.onPostExecute(polishList);

            mListPolish.clear();
            mListPolish.addAll(polishList);
            mAdapter.notifyDataSetChanged();

            if (getResources().getBoolean(R.bool.tablet)) {
                LoadFirstItem(polishList.get(0));
            }
            mProgress.dismiss();
            mSwipe.setRefreshing(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress = ProgressDialog.show(getActivity(), getResources().getString(R.string.waiting)
                    , getResources().getString(R.string.loading), true);
            mProgress.setCancelable(false);

        }

        @Override
        protected List<Polish> doInBackground(Void... params) {
            List<Polish> result = new ArrayList<Polish>();
            try {
                OkHttpClient client = new OkHttpClient();

                client.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
                client.setReadTimeout(15, TimeUnit.SECONDS);    // socket timeout

                Request request = new Request.Builder()
                        .url("http://tigerrobocop.esy.es/")
                        .build();

                // Read the stream

                try {
                    Response response = client.newCall(request).execute();

                    String jsonString = response.body().string();

                    Gson gson = new Gson();

                    result = Arrays.asList(gson.fromJson(jsonString, Polish[].class));

                } catch (Throwable e) {  //ao invés de IOException - classe parent de exception que abrange erros + exceptions
                    //para pegar erros no json string response por exemplo
                    e.printStackTrace();
                }

                return result;
            } catch (Throwable t) {
                t.printStackTrace();
            }

            return null;
        }
    }
}
