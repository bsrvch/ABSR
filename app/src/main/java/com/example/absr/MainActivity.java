package com.example.absr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ImageView IVPreviewImage;
    public final static int PICK_PHOTO_CODE = 1046;
    private Bitmap selectedLRBitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //Button tpbtn = (Button)findViewById(R.id.button_takephoto);
        IVPreviewImage = findViewById(R.id.imageView2);
        //setLRImageViewListener(tpbtn);
        Button st = (Button)findViewById(R.id.button_start);
        st.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, UpscaleActivity.class);
                        startActivity(intent);
                    }
                }
        );
        st.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double[][][] inp = new double[100][100][1];
                        for(int i =0 ;i<100;i++){
                            for(int j=0;j<100;j++){
                                inp[i][j][0] = getPixelData(selectedLRBitmap,j,i)[0];
                            }
                        }


                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri photoUri = data.getData();
        try {
            Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            int WIDTH_MN = 100;//(int) (Math.ceil(bmp.getWidth()/LR_IMAGE_HEIGHT));
            int HEIGHT_MN = 100;//(int) (Math.ceil(bmp.getHeight()/LR_IMAGE_HEIGHT));

            selectedLRBitmap = Bitmap.createScaledBitmap(bmp, WIDTH_MN*3, HEIGHT_MN*3, true);

            final ImageView nativelyScaledImageView = findViewById(R.id.imageView2);
            nativelyScaledImageView.setImageBitmap(selectedLRBitmap);
            final LinearLayout resultLayout = findViewById(R.id.ll1);
            resultLayout.setVisibility(View.VISIBLE);
        } catch (Exception ex){
            Log.e("Error", "Exception : ",ex);
        }
    }
    private void setLRImageViewListener(Button iv) {
        iv.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


                        if (intent.resolveActivity(getPackageManager()) != null) {
                            // Bring up gallery to select a photo
                            startActivityForResult(intent, PICK_PHOTO_CODE);
                        }

                    }
                });
    }
    public static int getIntFromColor(float Red, float Green, float Blue){
        int R = Math.round(Red);
        int G = Math.round(Green);
        int B = Math.round(Blue);

        R = (R << 16) & 0x00FF0000;
        G = (G << 8) & 0x0000FF00;
        B = B & 0x000000FF;

        return 0xFF000000 | R | G | B;
    }
    public static int[] getPixelData(Bitmap img, int x, int y) {
        int argb = img.getPixel(x, y);

        int rgb[] = new int[] {
                (argb >> 16) & 0xff, //red
                (argb >>  8) & 0xff, //green
                (argb      ) & 0xff  //blue
        };
        return rgb;
    }
}

