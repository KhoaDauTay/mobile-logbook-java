package com.example.log_book_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity2 extends AppCompatActivity {
    HouseService _service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        _service = retrofit.create(HouseService.class);

        Intent i = getIntent();
        FormModel formData = (FormModel) i.getSerializableExtra("data");
        TextView detail = findViewById(R.id.Detail);
        detail.setTextSize(16);
        detail.setText(Html.fromHtml("Property Name: " + "<b>" + formData.get_propertyType() + "</b>" + "<br>" +
                "Bed Room: "+ "<b>" + formData.get_bedType() + "</b>" + "<br>" +
                "Adding Date: " + "<b>" + formData.get_createdDate() + "</b>" + "<br>" +
                "Monthly Rent Price: "+ "<b>" + formData.get_rent() + "</b>" + "<br>" +
                "Furniture Type: " + "<b>" + formData.get_furnitureType() + "</b>" + "<br>" +
                "Notes: " + "<b>" + formData.get_noteText() + "</b>" + "<br>" +
                "Reporter Name: " + "<b>" + formData.get_reporter() + "</b>"));
        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(v -> {

        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData(formData);
                Snackbar.make(v, "Saved data successfully", Snackbar.LENGTH_LONG)
                        .setAction("Go Back", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }).show();
            }
        });
        Button back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity2.this, FormActivity.class);
            intent.putExtra("data", formData);
            startActivity(intent);
        });
    }
    private void submitData(FormModel model) {
        Log.d("API", "Convert data to Json");
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("property", model.get_propertyType().toLowerCase());
        jsonParams.put("bed_rooms", model.get_bedType().toLowerCase());
        jsonParams.put("create_at", null);
        jsonParams.put("rent", model.get_rent());
        jsonParams.put("furniture", model.get_furnitureType().toLowerCase());
        jsonParams.put("notes", model.get_noteText());
        jsonParams.put("name_reporter", model.get_reporter());
        jsonParams.put("reporter", null);
        jsonParams.put("name", model.get_reporter());
        jsonParams.put("address", "");
        jsonParams.put("image", null);
        JSONObject jsonBody = new JSONObject(jsonParams);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonBody.toString());

        Call<ResponseBody> response = _service.createHouse(body);
        Log.d("API", jsonBody.toString());;
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Error", t.toString());
            }
        });
    }
}