package com.example.log_book_java;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    HouseService _service;
    List<FormModel> houses;
    ArrayAdapter<FormModel> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MaterialToolbar appbar = findViewById(R.id.topAppBar);
        appbar.setTitle("RenTaiz");

        ListView listView = (ListView)findViewById(R.id.listView);
        //
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        _service = retrofit.create(HouseService.class);


        Call<List<FormModel>> repos = _service.listHouse("houses");
        repos.enqueue(new Callback<List<FormModel>>() {
            @Override
            public void onResponse(Call<List<FormModel>> call, Response<List<FormModel>> response) {
                if(response.isSuccessful()){
                    houses = response.body();
                    arrayAdapter
                            = new ArrayAdapter<FormModel>(MainActivity.this, android.R.layout.simple_list_item_1 , houses);

                    listView.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<FormModel>> call, Throwable t) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + houses.get(position).get_propertyType());
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Call<Void> call = _service.deleteHouse(houses.get(position).getId());
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (!response.isSuccessful()){
                                    return;
                                }
                                Toast.makeText(MainActivity.this, "Deleted Successfully : " + houses.get(position).getId(), Toast.LENGTH_LONG).show();
                                houses.remove(positionToRemove);
                                arrayAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });

                    }});
                adb.show();
            }
        });
    }
    public void nextScreen(View view) {
        Intent i = new Intent(MainActivity.this, FormActivity.class);
        startActivity(i);
    }
}