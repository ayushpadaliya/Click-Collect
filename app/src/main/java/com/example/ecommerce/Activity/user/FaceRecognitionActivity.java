package com.example.ecommerce.Activity.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommerce.MainActivity;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;

import java.util.List;

public class FaceRecognitionActivity extends AppCompatActivity {
    Button camera;
    FaceDetector detector;
    TextView textView;
    private final static int REQUEST_IMAGE_CAPTURE = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_face_recognition);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        camera=findViewById(R.id.camera);
        // Define the ActivityResultLauncher
        textView=findViewById(R.id.smile);
        ActivityResultLauncher<Void> imageCaptureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(),
                result -> {
                    if (result != null) {
                        // Handle the result here
                        Bitmap bitmap=result;

                        InputImage image = InputImage.fromBitmap(bitmap, 0);
                        detectFaces(image);
                    } else {
                        // Handle case where the user canceled the capture
                        Toast.makeText(FaceRecognitionActivity.this, "Image capture canceled", Toast.LENGTH_SHORT).show();
                    }
                });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCaptureLauncher.launch(null);
            }
        });


    }
    private void detectFaces(InputImage image)
    {
        FaceDetectorOptions options=new FaceDetectorOptions.Builder()
                .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.15f)
            .enableTracking()
            .build();
         detector =  FaceDetection.getClient(options);
        detector.process(image).addOnSuccessListener(faces -> {
            for (Face face : faces) {
                if (face!=null)
                {
                    Toast.makeText(this, "it is face", Toast.LENGTH_SHORT).show();
                }
                if (face.getSmilingProbability() != null) {
                    float smileProb = face.getSmilingProbability()*100;
                    textView.setText("smile_percentage :"+String.valueOf(smileProb));
                }
                if (face.getRightEyeOpenProbability() != null) {
                    float rightEyeOpenProb = face.getRightEyeOpenProbability();
                }
                if (face.getTrackingId() != null) {
                    int id = face.getTrackingId();
                }

            }
        });

    }


//    public int getRotationDegrees(Bitmap bitmap) {
//        int rotationDegrees = 0;
//        try {
//            // Create an ExifInterface instance from the image
//            ExifInterface exifInterface = new ExifInterface(String.valueOf(bitmap));
//
//            // Extract rotation information
//            int orientation = exifInterface.getAttributeInt(
//                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//            // Determine rotation degrees based on orientation
//            switch (orientation) {
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    rotationDegrees = 90;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    rotationDegrees = 180;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    rotationDegrees = 270;
//                    break;
//                default:
//                    rotationDegrees = 0;
//                    break;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return rotationDegrees;
//    }


}