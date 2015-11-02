package com.unibratec.livia.polishcollection.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unibratec.livia.polishcollection.Model.Polish;
import com.unibratec.livia.polishcollection.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Livia on 31/10/2015.
 */
public class DAO {

    private DBHelper helper;

    public DAO(Context ctx) {
        helper = new DBHelper(ctx);
    }

    public void Insert(Polish p) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues val = new ContentValues();

        val.put(DBHelper.COL_ID, p.id);
        val.put(DBHelper.COL_BRAND, p.brand);
        val.put(DBHelper.COL_COLOR, p.color);
        val.put(DBHelper.COL_NAME, p.name);
        val.put(DBHelper.COL_IMGURL, p.imageUrl);

        long id = db.insert(DBHelper.TBL_POLISH, null, val);
        if (id == -1) {
            throw new RuntimeException("Erro ao inserir");
        }

        db.close();
    }

    public void Delete(Polish p) {

        SQLiteDatabase db = helper.getWritableDatabase();

        db.delete(DBHelper.TBL_POLISH, DBHelper.COL_ID + " = ? "
                , new String[]{String.valueOf(p.id)}
        );
        db.close();
    }

    public List<Polish> getAll() {
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + DBHelper.TBL_POLISH, null);

        List<Polish> retorno = new ArrayList<Polish>();
        int index_dbId = cursor.getColumnIndex(DBHelper.COL_dbID);
        int index_id = cursor.getColumnIndex(DBHelper.COL_ID);
        int index_brand = cursor.getColumnIndex(DBHelper.COL_BRAND);
        // int index_finish = cursor.getColumnIndex(DBHelper.COL_FINISH);
        int index_color = cursor.getColumnIndex(DBHelper.COL_COLOR);
        int index_name = cursor.getColumnIndex(DBHelper.COL_NAME);
        int index_imageUrl = cursor.getColumnIndex(DBHelper.COL_IMGURL);


        while (cursor.moveToNext()) {
            long dbId = cursor.getLong(index_dbId);
            int id = cursor.getInt(index_id);
            String brand = cursor.getString(index_brand);
            String color = cursor.getString(index_color);
            String name = cursor.getString(index_name);
            String imgUrl = cursor.getString(index_imageUrl);

            Polish p = new Polish(dbId, id, brand, color, name, imgUrl);

            retorno.add(p);
        }

        cursor.close();

        db.close();

        return retorno;
    }

    public boolean isFavorite(Polish p) {
        boolean retorno = false;

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + DBHelper.COL_dbID +
                        " from " + DBHelper.TBL_POLISH +
                        " where " + DBHelper.COL_ID + " = ? ",
                new String[]{String.valueOf(p.id)});

        retorno = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return retorno;
    }
}
