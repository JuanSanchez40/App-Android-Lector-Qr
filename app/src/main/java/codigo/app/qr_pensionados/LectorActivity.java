package codigo.app.qr_pensionados;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class LectorActivity extends AppCompatActivity {


    private SurfaceView camara;
    private BarcodeDetector detector;
    private CameraSource cameraSource;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector);
        camara = findViewById(R.id.Scanner);

        detector = new BarcodeDetector.Builder(this)
        .setBarcodeFormats(Barcode.QR_CODE)
        .build();

        cameraSource = new CameraSource.Builder(this,detector)
                .setRequestedPreviewSize(640,300)
                .build();

        camara.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
               // permisos camara
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    return;
                }
                try {
                    cameraSource.start(camara.getHolder());

                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                  cameraSource.stop();
            }


        });

             detector.setProcessor(new Detector.Processor<Barcode>() {
                 @Override
                 public void release() {

                 }

                 @Override
                 public void receiveDetections(Detector.Detections<Barcode> detections) {
                     SparseArray<Barcode> codes = detections.getDetectedItems();
                     if (codes.size()!=0){
                         token= codes.valueAt(0).displayValue;
                         startActivity(new Intent(getApplicationContext(),PensionadosActivity.class)
                         .putExtra("id",Integer.parseInt(token)));
                         finish();
                     }
                 }
             });



    }
}
