package com.unibratec.livia.polishcollection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.unibratec.livia.polishcollection.Model.Polish;

import java.util.List;

/**
 * Created by Livia on 31/10/2015.
 */
public class PolishAdapter  extends ArrayAdapter<Polish> {
    public PolishAdapter(Context context, List<Polish> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView ==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.polish_list_item, null);
            viewHolder = new ViewHolder();

            viewHolder.imgThumb = (ImageView)convertView.findViewById(R.id.imageView_thumb);
            viewHolder.txtBrand = (TextView)convertView.findViewById(R.id.textView_brand);
            viewHolder.txtName = (TextView)convertView.findViewById(R.id.textView_name);

            convertView.setTag(viewHolder);
        }else  {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Polish polish = getItem(position);

        if(polish != null){
            Picasso.with(getContext()).load(polish.imageUrl).into(viewHolder.imgThumb);
            viewHolder.txtBrand.setText(polish.brand);
            viewHolder.txtName.setText(polish.name);
        }

        return convertView;
    }
}

class ViewHolder {
    ImageView imgThumb;
    TextView txtBrand;
    TextView txtName;
}

