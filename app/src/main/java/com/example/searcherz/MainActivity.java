package com.example.searcherz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R2.id.button_camera)
    Button buttonCamera;
    @BindView(R2.id.button_gallery)
    Button buttonGallery;
    @BindView(R2.id.editText_detectedText)
    EditText editTextDetectedText;
    @BindView(R2.id.button_google)
    Button buttonGoogle;
    @BindView(R2.id.button_youtube)
    Button buttonYoutube;
    @BindView(R2.id.button_shopping)
    Button buttonShopping;
    @BindView(R2.id.button_newImage)
    Button buttonNewImage;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
            FirebaseVisionImage firebaseVisionImage=FirebaseVisionImage.fromBitmap(imageBitmap);
            FirebaseVisionTextDetector firebaseVisionTextDetector= FirebaseVision.getInstance().getVisionTextDetector();
            firebaseVisionTextDetector.detectInImage(firebaseVisionImage).addOnCompleteListener(new OnCompleteListener<FirebaseVisionText>() {
                @Override
                public void onComplete(@NonNull Task<FirebaseVisionText> task) {
                    if (task.isSuccessful()){

                        editTextDetectedText.setText("");
                        displayTextFromImage(task.getResult());
                    }else {
                        Toast.makeText(MainActivity.this, "Error "+ task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void displayTextFromImage(FirebaseVisionText firebaseVisionText){

        List<FirebaseVisionText.Block> blocks=firebaseVisionText.getBlocks();
        if (blocks.size()==0){
            Toast.makeText(this, "No text Detected", Toast.LENGTH_SHORT).show();
        }else {
//            Log.i("BLOCK",blocks.get(0).getText().toString()+" "+blocks.get(1).getText());
            editTextDetectedText.setVisibility(View.VISIBLE);
            String result="";
            for (FirebaseVisionText.Block block:blocks){
                result +=block.getText().toString()+System.lineSeparator();
//                editTextDetectedText.setText(new StringBuilder().append(editTextDetectedText.getText()).append("\n").append(block.getText()).toString());
//                editTextDetectedText.setText(block.getText());
            }
            editTextDetectedText.setText(result);
        }

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R2.id.button_camera, R2.id.button_gallery, R2.id.button_google, R2.id.button_youtube, R2.id.button_shopping, R2.id.button_newImage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_camera:
                dispatchTakePictureIntent();


                break;
            case R.id.button_gallery:
                break;
            case R.id.button_google:
                break;
            case R.id.button_youtube:
                break;
            case R.id.button_shopping:
                break;
            case R.id.button_newImage:
                break;
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

}