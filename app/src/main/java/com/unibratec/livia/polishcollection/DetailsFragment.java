package com.unibratec.livia.polishcollection;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.unibratec.livia.polishcollection.Data.DAO;
import com.unibratec.livia.polishcollection.Model.Polish;

/**
 * Created by Livia on 31/10/2015.
 */
public class DetailsFragment extends Fragment {


    public static final String TAG_DETAILS = "tag_details";
    public static final String EXTRA_POLISH = "polish";
    Polish mPolish;

    TextView mTxtName;
    TextView mTxtBrand;
    TextView mTxtColor;

    MenuItem mMentuItem;
    DAO mDao;

    public static DetailsFragment newInstance(Polish p) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_POLISH, p);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPolish = (Polish) getArguments().getSerializable(EXTRA_POLISH);
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDao = new DAO(getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.detail_favorite, menu);

        mMentuItem = menu.findItem(R.id.action_add_favorite);
        UpdateFaveButton(mDao.isFavorite(mPolish));
    }

    void UpdateFaveButton(boolean isFavorite) {
        mMentuItem.setIcon(isFavorite ?
                android.R.drawable.ic_menu_delete :
                android.R.drawable.ic_menu_save);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_favorite) {

            boolean isFavorite = mDao.isFavorite(mPolish);
            if (isFavorite) {
                mDao.Delete(mPolish);
            } else {
                mDao.Insert(mPolish);
            }

            UpdateFaveButton(!isFavorite);
            Bus bus = ((PolishApp) getActivity().getApplication()).getBus();
            bus.post(mPolish);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_details, container, false);

        mTxtName = (TextView) layout.findViewById(R.id.textView_name);
        mTxtBrand = (TextView) layout.findViewById(R.id.textView_brand);
        mTxtColor = (TextView) layout.findViewById(R.id.textView_color);

        if (mPolish != null) {
            mTxtName.setText(mPolish.name);
            mTxtBrand.setText(mPolish.brand);
            mTxtColor.setText(mPolish.color);
        }
        return layout;
    }
}
