package com.unibratec.livia.polishcollection;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.unibratec.livia.polishcollection.ImgurAPI.Helpers.DocumentHelper;
import com.unibratec.livia.polishcollection.ImgurAPI.Helpers.IntentHelper;
import com.unibratec.livia.polishcollection.ImgurAPI.Model.ImageResponse;
import com.unibratec.livia.polishcollection.ImgurAPI.Model.Upload;
import com.unibratec.livia.polishcollection.ImgurAPI.Service.UploadService;

import java.io.File;

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


    private Upload upload; // Upload object containging image and meta data
    private File chosenFile; //chosen file from intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        ButterKnife.bind(this);

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
        new UploadService(this).Execute(upload, new UiCallback());
    }

    private void createUpload(File image) {
        upload = new Upload();

        upload.image = image;
        upload.title = uploadTitle.getText().toString();
        upload.description = uploadDesc.getText().toString();
    }

    private class UiCallback implements Callback<ImageResponse> {

        @Override
        public void success(ImageResponse imageResponse, Response response) {

            String teste =  imageResponse.data.link;

            Snackbar.make(findViewById(R.id.image_upload_layout), teste, Snackbar.LENGTH_SHORT).show();
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

}
