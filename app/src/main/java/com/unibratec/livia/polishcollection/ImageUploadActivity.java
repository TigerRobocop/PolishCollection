package com.unibratec.livia.polishcollection;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.Picasso;
import com.unibratec.livia.polishcollection.ImgurAPI.Helpers.DocumentHelper;
import com.unibratec.livia.polishcollection.ImgurAPI.Helpers.IntentHelper;
import com.unibratec.livia.polishcollection.ImgurAPI.Model.ImageResponse;
import com.unibratec.livia.polishcollection.ImgurAPI.Model.Upload;
import com.unibratec.livia.polishcollection.ImgurAPI.Service.UploadService;
import com.unibratec.livia.polishcollection.Model.Polish;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ImageUploadActivity extends AppCompatActivity {

    @Bind(R.id.imageview)
    ImageView uploadImage;
    @Bind(R.id.editText_brand)
    EditText mBrand;
    @Bind(R.id.editText_color)
    EditText mColor;
    @Bind(R.id.editText_name)
    EditText mName;

    ProgressDialog mProgress;

    private Upload upload; // Upload object containing image and meta data
    private File chosenFile; //chosen file from intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri returnUri;

        if (requestCode != IntentHelper.FILE_PICK) {
            return;
        }

        if (resultCode != RESULT_OK) {
            return;
        }

        returnUri = data.getData();
        String filePath = DocumentHelper.getPath(this, returnUri);
        //Safety check to prevent null pointer exception
        if (filePath == null || filePath.isEmpty()) return;
        chosenFile = new File(filePath);

                /*
                    Picasso is a wonderful image loading tool from square inc.
                    https://github.com/square/picasso
                 */
        Picasso.with(getBaseContext())
                .load(chosenFile)
                .placeholder(R.drawable.ic_plus)
                .fit()
                .into(uploadImage);

    }

    @OnClick(R.id.imageview)
    public void onChooseImage() {
        mBrand.clearFocus();
        mColor.clearFocus();
        mName.clearFocus();
        IntentHelper.chooseFileIntent(this);
    }

    private void clearInput() {
        mBrand.setText("");
        mBrand.clearFocus();
        mColor.setText("");
        mColor.clearFocus();
        mName.setText("");
        mName.clearFocus();
        uploadImage.setImageResource(R.drawable.ic_plus);
    }

    @OnClick(R.id.fab)
    public void uploadImage() {

        if (chosenFile == null) return;
        createUpload(chosenFile);

        mProgress = ProgressDialog.show(this, getResources().getString(R.string.waiting)
                , getResources().getString(R.string.loading_imgupload), true);
        mProgress.setCancelable(false);

        new UploadService(this).Execute(upload, new UiCallback(this));
    }

    private void createUpload(File image) {
        upload = new Upload();

        upload.image = image;
        upload.title = mBrand.getText().toString();
        upload.description = mName.getText().toString();
    }

    private class UiCallback implements Callback<ImageResponse> {

        Context context;

        private UiCallback(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override
        public void success(ImageResponse imageResponse, Response response) {

            String imgUrl = imageResponse.data.link;

            UploadTask task = new UploadTask(context);

            String _brand = mBrand.getText().toString();
            String _color = mColor.getText().toString();
            String _name = mName.getText().toString();

            Polish p = new Polish(_brand, _color, _name, imgUrl);

            task.execute(p);
        }

        @Override
        public void failure(RetrofitError error) {
            //Assume we have no connection, since error is null
            if (error == null) {
                Snackbar.make(findViewById(R.id.image_upload_layout), "No internet connection", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private class UploadTask extends AsyncTask<Object, Void, Void> {

        Context context;

        private UploadTask(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgress.dismiss();
            clearInput();

            Intent intent = new Intent(context, WebActivity.class);
            startActivity(intent);
        }

        @Override
        protected Void doInBackground(Object... params) {

            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
            client.setReadTimeout(15, TimeUnit.SECONDS);    // socket timeout

            Polish pol = (Polish) params[0];

            if (pol instanceof Polish) {
                try {

                    String jsonPolish = new Gson().toJson(pol);
                    RequestBody body = RequestBody.create(JSON, jsonPolish);

                    Request request = new Request.Builder()
                            .url("http://tigerrobocop.esy.es/index.php")
                            .post(body)
                            .build();

                    com.squareup.okhttp.Response responsePost = client.newCall(request).execute();

                    //Log.v("LALA",  responsePost.body().string());

                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

}
