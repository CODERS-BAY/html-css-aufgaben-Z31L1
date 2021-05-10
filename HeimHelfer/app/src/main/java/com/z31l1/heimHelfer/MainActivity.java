package com.z31l1.heimHelfer;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ClickListener, AdapterView.OnItemSelectedListener {

    DataBank dataBank;
    RecyclerView recyclerView;
    Spinner spinner;
    RecyclerViewAdapter recyclerViewAdapter;
    FloatingActionButton floatingActionButton;
    public static final int NEW_MEASURE_REQUEST_CODE = 200;
    public static final int UPDATE_MEASURE_REQUEST_CODE = 300;
    private final String[] filterTag = {"Alle", "Vormittag", "Mittag", "Nachmittag", "Abend", "Archiv"};
    ArrayList<String> spinnerList = new ArrayList<>(Arrays.asList(filterTag));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews(spinnerList);
        Log.d("List",spinnerList.toString() + "1");
        dataBank = Room.databaseBuilder(getApplicationContext(), DataBank.class, DataBank.MEASURE_DB).fallbackToDestructiveMigration().build();
        checkIfAppLaunchedFirstTime();
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, MeasureNoteActivity.class), NEW_MEASURE_REQUEST_CODE);
            }
        });
        loadAllNames();
    }

    private void initViews(ArrayList spinnerArrayList) {
        floatingActionButton = findViewById(R.id.fab);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void launchIntent(int id) {
        startActivityForResult(new Intent(MainActivity.this, MeasureNoteActivity.class).putExtra("id", id), UPDATE_MEASURE_REQUEST_CODE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            loadAllMeasure();
        }
        else if(position <=5 && position > 0){
            String string = parent.getItemAtPosition(position).toString();
            loadFilteredMeasures(string);
        }
        else{
            String string = parent.getItemAtPosition(position).toString();
            loadFilteredMeasuresByName(string);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @SuppressLint("StaticFieldLeak")
    private void loadFilteredMeasures(String filterTag) {
        new AsyncTask<String, Void, List<Measure>>() {

            @Override
            protected List<Measure> doInBackground(String... params) {
                return dataBank.dbDao().fetchMeasureListByFilterTag(params[0]);
            }

            @Override
            protected void onPostExecute(List<Measure> measureList) {
                recyclerViewAdapter.updateMeasureList(measureList);
            }
        }.execute(filterTag);
    }

    @SuppressLint("StaticFieldLeak")
    private void loadFilteredMeasuresByName(String name) {
        new AsyncTask<String, Void, List<Measure>>() {

            @Override
            protected List<Measure> doInBackground(String... params) {
                return dataBank.dbDao().fetchMeasureListByName(params[0]);
            }

            @Override
            protected void onPostExecute(List<Measure> measureList) {
                recyclerViewAdapter.updateMeasureList(measureList);
            }
        }.execute(name);
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchMeasureByIdAndInsert(int id) {
        new AsyncTask<Integer, Void, Measure>() {

            @Override
            protected Measure doInBackground(Integer... params) {
                return dataBank.dbDao().fetchMeasureListById(params[0]);
            }

            @Override
            protected void onPostExecute(Measure measureList) {
                recyclerViewAdapter.addRow(measureList);
            }
        }.execute(id);
    }

    @SuppressLint("StaticFieldLeak")
    private void loadAllMeasure() {
        new AsyncTask<String, Void, List<Measure>>() {
            @Override
            protected List<Measure> doInBackground(String... params) {
                return dataBank.dbDao().fetchAllMeasures();
            }

            @Override
            protected void onPostExecute(List<Measure> measureList) {
                recyclerViewAdapter.updateMeasureList(measureList);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadAllNames() {
        new AsyncTask<String, Void, String[]>() {
            @Override
            protected String[] doInBackground(String... params) {
                return dataBank.dbDao().fetchMeasureName();
            }

            @Override
            protected void onPostExecute(String[] nameArray) {
                spinnerList = new ArrayList<>(Arrays.asList(filterTag));
                LinkedHashSet<String> uniqueNameArray = new LinkedHashSet<String>(Arrays.asList(nameArray));
                String[] uniqueStrings = uniqueNameArray.toArray(new String[ uniqueNameArray.size() ]);
                spinnerList.addAll(Arrays.asList(uniqueStrings));
                initViews(spinnerList);
                Log.d("List",spinnerList.toString() + "3");
            }
        }.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadAllNames();
            spinner.setSelection(0);
            if (requestCode == NEW_MEASURE_REQUEST_CODE) {
                long id = data.getLongExtra("id", -1);
                Toast.makeText(getApplicationContext(), "Maßnahme hinzugefügt", Toast.LENGTH_SHORT).show();
                fetchMeasureByIdAndInsert((int) id);
            }
            else if (requestCode == UPDATE_MEASURE_REQUEST_CODE) {
                boolean isDeleted = data.getBooleanExtra("isDeleted", false);
                int number = data.getIntExtra("number", -1);
                if (isDeleted) {
                    Toast.makeText(getApplicationContext(), number + " Maßnahme gelöscht", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), number + " Maßnahme aktualisiert", Toast.LENGTH_SHORT).show();
                }
                loadAllMeasure();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Keine eingriffe durch Benutzer", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void insertList(List<Measure> measureList) {
        new AsyncTask<List<Measure>, Void, Void>() {

            @Override
            protected Void doInBackground(List<Measure>... params) {
                dataBank.dbDao().insertMeasureList(params[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void voids) {
                super.onPostExecute(voids);
            }
        }.execute(measureList);
    }

    private void checkIfAppLaunchedFirstTime() {
        final String PREFS_NAME = "SharedPrefs";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("firstTime", true)) {
            settings.edit().putBoolean("firstTime", false).apply();
        }
    }
}

