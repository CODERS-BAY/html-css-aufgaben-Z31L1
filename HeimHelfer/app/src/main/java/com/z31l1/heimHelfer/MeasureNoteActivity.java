package com.z31l1.heimHelfer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class MeasureNoteActivity extends AppCompatActivity {

    Spinner spinner;
    EditText inTitle, inDesc;
    TextView inTimestamp;
    Button btnDone, btnDelete, photoButton;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    Bitmap photo, bitMapConvert;
    Bitmap template = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    DataBank dataBank;
    Measure updateMeasure;
    byte[] imageInByte;
    boolean isNewMeasure = false;
    private ImageView imageView;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private final String[] filterTag = { "Vormittag", "Mittag", "Nachmittag", "Abend", "Archiv" };
    public ArrayList<String> spinnerList = new ArrayList<>(Arrays.asList(filterTag));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinner = findViewById(R.id.spinner);
        inTitle = findViewById(R.id.inTitle);
        inDesc = findViewById(R.id.inDescription);
        inTimestamp = findViewById(R.id.inTimeStamp);
        btnDone = findViewById(R.id.btnDone);
        btnDelete = findViewById(R.id.btnDelete);
        imageView = findViewById(R.id.imageView);
        photoButton = findViewById(R.id.cam);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        dataBank = Room.databaseBuilder(getApplicationContext(), DataBank.class, DataBank.MEASURE_DB).build();

        photoButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

    int MeasureId = getIntent().getIntExtra("id", -100);
        if (MeasureId == -100)
            isNewMeasure = true;
        if (!isNewMeasure) {
            fetchMeasureById(MeasureId);
            btnDelete.setVisibility(View.VISIBLE);
        }
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeStamp = dateFormat.format(new Date());
                if(photo == null) {
                    photo = template;
                }
                bitToByte();
                if (isNewMeasure) {
                    Measure measure = new Measure();
                    measure.name = inTitle.getText().toString();
                    measure.timeStamp = "Erstellt: " + timeStamp;
                    measure.description = inDesc.getText().toString();
                    measure.filterTag = spinner.getSelectedItem().toString();
                    Log.d("PHOTO", photo.toString() + " new");
                    measure.img = imageInByte;
                    insertRow(measure);
                }
                else {
                    updateMeasure.name = inTitle.getText().toString();
                    updateMeasure.description = inDesc.getText().toString();updateMeasure.filterTag = spinner.getSelectedItem().toString();
                    updateMeasure.img = imageInByte;
                    updateRow(updateMeasure);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRow(updateMeasure);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Kamera zugriff erlaubt", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else {
                Toast.makeText(this, "Kamera zugriff verweigert", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchMeasureById(final int measureId) {
        new AsyncTask<Integer, Void, Measure>() {
            @Override
            protected Measure doInBackground(Integer... params) {
                return dataBank.dbDao().fetchMeasureListById(params[0]);
            }

            @Override
            protected void onPostExecute(Measure measure) {
                super.onPostExecute(measure);
                inTitle.setText(measure.name);
                inDesc.setText(measure.description);
                spinner.setSelection(spinnerList.indexOf(measure.filterTag));
                imageInByte = measure.img;
                byteToBit();
                if(photo.getWidth() > 20) {
                    imageView.setImageBitmap(photo);
                }
                updateMeasure = measure;
            }
        }.execute(measureId);
    }

    @SuppressLint("StaticFieldLeak")
    private void insertRow(Measure measure) {
        new AsyncTask<Measure, Void, Long>() {
            @Override
            protected Long doInBackground(Measure... params) {
                return dataBank.dbDao().insertMeasure(params[0]);
            }

            @Override
            protected void onPostExecute(Long id) {
                super.onPostExecute(id);

                Intent intent = getIntent();
                intent.putExtra("isNew", true).putExtra("id", id);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(measure);
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteRow(Measure measure) {
        new AsyncTask<Measure, Void, Integer>() {
            @Override
            protected Integer doInBackground(Measure... params) {
                return dataBank.dbDao().deleteMeasure(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra("isDeleted", true).putExtra("number", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(measure);
    }


    @SuppressLint("StaticFieldLeak")
    private void updateRow(Measure measure) {
        new AsyncTask<Measure, Void, Integer>() {
            @Override
            protected Integer doInBackground(Measure... params) {
                return dataBank.dbDao().updateMeasure(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra("isNew", false).putExtra("number", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(measure);
    }

    public void bitToByte(){
        if(photo != null) {
            bitMapConvert = photo;
            ByteArrayOutputStream streamer = new ByteArrayOutputStream();
            bitMapConvert.compress(Bitmap.CompressFormat.JPEG, 100, streamer);
            imageInByte = streamer.toByteArray();
        }
    }
    public void byteToBit(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        if(imageInByte!= null){ photo = BitmapFactory.decodeByteArray(imageInByte, 0, imageInByte.length, options);}
    }
}
