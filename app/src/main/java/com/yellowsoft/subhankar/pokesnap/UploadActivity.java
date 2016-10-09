package com.yellowsoft.subhankar.pokesnap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by subhankar on 7/19/2016.
 */
public class UploadActivity extends AppCompatActivity {

    Uri photoUri;
    private SessionManager session;
    private ProgressDialog pDialog;
    private ImageView ivPhoto;

    public final static int PICK_PHOTO_CODE = 1046;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        setContentView(R.layout.activity_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabDone);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setEnabled(false);
                uploadFile(photoUri);
            }
        });
        ivPhoto.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Bring up gallery to select a photo
                    startActivityForResult(intent, PICK_PHOTO_CODE);
                }
            }
        });


        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(this.getResources().getColor(R.color.my_statusbar_color));

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            photoUri = data.getData();
            // Do something with the photo based on Uri
            Bitmap selectedImage = null;
            try {
                selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Load the selected image into a preview
            ImageView ivPreview = (ImageView) findViewById(R.id.ivPhoto);
            ivPreview.setImageBitmap(selectedImage);
        }
    }

    private void uploadFile(Uri fileUri) {

        pDialog.setMessage("Uploading ...");
        showDialog();

        session = new SessionManager(getApplicationContext());

        String mimeType = getContentResolver().getType(fileUri);

        // create upload service client
        ApiInterface service =
                ApiClient.getClient().create(ApiInterface.class);

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, fileUri);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(mimeType), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("photos", file.getName(), requestFile);

        // add another part within the multipart request
        String userName = session.getName();
        RequestBody user =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), userName);

        // add another part within the multipart request
        String team = session.getTeam();
        RequestBody teamName =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), team);

        // add another part within the multipart request
        EditText desc = (EditText) findViewById(R.id.etDescription);
        String bodyText = desc.getText().toString();

        RequestBody postBody =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), bodyText);

        String token = session.getToken();
        RequestBody tokenField =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), token);


        // finally, execute the request
        Call<ResponseBody> call = service.upload(user, teamName, body, postBody, tokenField);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
                hideDialog();
                Intent intent = new Intent(UploadActivity.this, FeedActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

