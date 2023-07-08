package com.example.android.myqrgenerator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    private Button generate, scan;
    private EditText mytext;
    private ImageView qr_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generate = findViewById(R.id.generate);
        scan = findViewById(R.id.scan);
        mytext = findViewById(R.id.text);
        qr_code = findViewById(R.id.qrcode);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mytext.getText().toString();
                if (text != null) {
                    try {
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE , 500, 500);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        qr_code.setImageBitmap(bitmap);
                        qr_code.setVisibility(View.VISIBLE);
                        mytext.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Code for the scanner.
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
//                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//                intentIntegrator.setCameraId(0);// Decides the camera.
//                intentIntegrator.setOrientationLocked(false);
//                intentIntegrator.setPrompt("scanning");
//                intentIntegrator.setBeepEnabled(true);
//                intentIntegrator.setBarcodeImageEnabled(true);
//                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null && result.getContents() != null) {

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Scan Result")
                    .setMessage(result.getContents())
                    .setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            ClipData data = ClipData.newPlainText("result", result.getContents());
                            manager.setPrimaryClip(data);
                        }
                    }).setNegativeButton("Search", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
                    String query = result.getContents();
                    String finalQuery = "";
//                    String escapedQuery = URLEncoder.encode(query, "UTF-8");
//                    Uri uri = Uri.parse("http://www.google.com/#q=" + escapedQuery);
                    if(query.matches("^(http|https|ftp)://.*$")) {
                        finalQuery = query;
                    } else {
                        finalQuery = "http://www.google.com/search?q=" + query;
                    }
                    Uri uri = Uri.parse(finalQuery);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);


                }
            }).create().show();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_scan) {

            IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            intentIntegrator.setCameraId(0);// Decides the camera.
            intentIntegrator.setOrientationLocked(false);
            intentIntegrator.setPrompt("scanning");
            intentIntegrator.setBeepEnabled(true);
            intentIntegrator.setBarcodeImageEnabled(true);
            intentIntegrator.initiateScan();

//            Intent intent1 = new Intent(this,ScanActivity.class);
//            this.startActivity(intent1);
//            finish();
            //            Toast.makeText(this, "About " + day + " " + month + " " + year, Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
