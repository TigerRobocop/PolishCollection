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

    /*
    These annotations are for ButterKnife by Jake Wharton
    https://github.com/JakeWharton/butterknife
   */
    @Bind(R.id.imageview)
    ImageView uploadImage;
    @Bind(R.id.editText_upload_title)
    EditText uploadTitle;
    @Bind(R.id.editText_upload_desc)
    EditText uploadDesc;


    ProgressDialog mProgress;

    private Upload upload; // Upload object containging image and meta data
    private File chosenFile; //chosen file from intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        ButterKnife.bind(this);

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
  //      StrictMode.setThreadPolicy(policy);

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
                .placeholder(android.R.drawable.ic_menu_camera)
                .fit()
                .into(uploadImage);

    }

    @OnClick(R.id.imageview)
    public void onChooseImage() {
        uploadDesc.clearFocus();
        uploadTitle.clearFocus();
        IntentHelper.chooseFileIntent(this);
    }

    private void clearInput() {
        uploadTitle.setText("");
        uploadDesc.clearFocus();
        uploadDesc.setText("");
        uploadTitle.clearFocus();
        uploadImage.setImageResource(R.drawable.ic_plus);
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @OnClick(R.id.fab)
    public void uploadImage() {
    /*
      Create the @Upload object
     */
        if (chosenFile == null) return;

       createUpload(chosenFile);
    /*
      Start upload
     */

        mProgress = ProgressDialog.show(this, getResources().getString(R.string.waiting)
                , getResources().getString(R.string.loading), true);
        mProgress.setCancelable(false);

       new UploadService(this).Execute(upload, new UiCallback(this));
    }

    private void createUpload(File image) {
        upload = new Upload();

        upload.image = image;
        upload.title = uploadTitle.getText().toString();
        upload.description = uploadDesc.getText().toString();
    }

    private class UiCallback implements Callback<ImageResponse> {

        Context context;

        private UiCallback(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override
        public void success(ImageResponse imageResponse, Response response) {

            String imgUrl =  imageResponse.data.link;

            UploadTask task = new UploadTask(context);
            task.execute(imgUrl);

            clearInput();
        }

        @Override
        public void failure(RetrofitError error) {
            //Assume we have no connection, since error is null
            if (error == null) {
                Snackbar.make(findViewById(R.id.image_upload_layout), "No internet connection", Snackbar.LENGTH_SHORT).show();
            }
        }
    }


    private class UploadTask extends AsyncTask<String, Void, Void>{

        Context context;

        private UploadTask(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgress.dismiss();

            Intent intent = new Intent(context, WebActivity.class);
            startActivity(intent);
        }

        @Override
        protected Void doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();

            String url = params[0];

            client.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
            client.setReadTimeout(15, TimeUnit.SECONDS);    // socket timeout

            try {
                Polish p = new Polish("Teste 2", "LALALA", "OEOE", url);

                String jsonPolish = new Gson().toJson(p);
                RequestBody body = RequestBody.create(JSON, jsonPolish);

                String teste2 = body.toString();

                Request request = new Request.Builder()
                        .url("http://tigerrobocop.esy.es/index.php")
                        .post(body)
                        .build();

                com.squareup.okhttp.Response responsePost = client.newCall(request).execute();

                //Log.v("LALA",  responsePost.body().string());

            }catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
