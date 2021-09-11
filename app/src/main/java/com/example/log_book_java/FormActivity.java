package com.example.log_book_java;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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

public class FormActivity extends AppCompatActivity {

    EditText inputReporter, inputNote, inputRent;
    TextView createdDate;
    Button buttonAddDate, buttonSubmit;
    FormModel _formModel;
    Spinner furnitureType, propertyType, bedRoom;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        // Init State
        Intent intent = getIntent();
        _formModel = intent.getSerializableExtra("data") == null ? new FormModel() : (FormModel) intent.getSerializableExtra("data");

        // Create EditText Reporter field
        inputReporter = findViewById(R.id.inputReporter);
        inputReporter.setText(_formModel.get_reporter());
        inputReporter.addTextChangedListener(
                new TextWatcher(

                ) {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            inputReporter.setError("The reporter name must be existed");
                        } else {
                            _formModel.set_reporter(s.toString());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }
        );
        // Create EditText Note field
        inputNote = findViewById(R.id.inputNote);
        inputNote.setText(_formModel.get_noteText());
        inputNote.addTextChangedListener(
                new TextWatcher(

                ) {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() > 100) {
                            inputNote.setError("The notes maximum length can't be over 100");
                        } else {
                            _formModel.set_noteText(s.toString());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }
        );
        // Create EditText Rent field
        inputRent = findViewById(R.id.inputRent);
        inputRent.setText(String.valueOf(_formModel.get_rent()));
        inputRent.addTextChangedListener(
                new TextWatcher(

                ) {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            inputRent.setError("The rent must be existed");
                        } else if (Double.parseDouble(s.toString()) <= 0.0) {
                            inputRent.setError("The value of rent must be over $0");
                        } else if (s.toString().contains(".") && s.toString().substring(s.toString().indexOf(".")).length() > 3) {
                            inputRent.setError("The price only accept 2 digits after the period");
                        } else {
                            _formModel.set_rent(Double.parseDouble(s.toString()));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }
        );
        // Create Created Date field and Datetime picker dialog
        String formatDate = "yyyy/MM/dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate, Locale.US);
        _formModel.set_createdDate(simpleDateFormat.format(new Date()));
        createdDate = findViewById(R.id.valueDate);
        createdDate.setText(_formModel.get_createdDate());

        buttonAddDate = findViewById(R.id.buttonAddDate);
        DatePickerDialog.OnDateSetListener datePicker = (
                (view, year, month, dayOfMonth) -> {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, month);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDialog();
                }
        );
        buttonAddDate.setOnClickListener(view -> {
            DatePickerDialog dialog = new DatePickerDialog(
                    FormActivity.this, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            dialog.show();
        });

        // Create Property types drop-down
        List<String> propertyList = Arrays.asList(getResources().getStringArray(R.array.property_types));
        propertyType = (Spinner) findViewById(R.id.dropDownProperty);
        ArrayAdapter<String> propertyAdapter = new ArrayAdapter<String>(FormActivity.this, android.R.layout.simple_spinner_dropdown_item, propertyList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        propertyType.setAdapter(propertyAdapter);
        propertyType.setSelection(0);
        propertyType.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String value = propertyList.get(position);
                        _formModel.set_propertyType(value);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
        // Create Bedroom types drop-down
        List<String> bedRoomList = Arrays.asList(getResources().getStringArray(R.array.bedroom_values));
        bedRoom = (Spinner) findViewById(R.id.dropDownBedRoom);
        ArrayAdapter<String> bedAdapter = new ArrayAdapter<String>(FormActivity.this, android.R.layout.simple_spinner_dropdown_item, bedRoomList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        bedRoom.setAdapter(bedAdapter);
        bedRoom.setSelection(0);
        bedRoom.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String value = bedRoomList.get(position);
                        _formModel.set_bedType(value);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
        // Create Furniture types drop-down
        List<String> furnitureList = Arrays.asList(getResources().getStringArray(R.array.furniture_value));
        furnitureType = (Spinner) findViewById(R.id.dropDownFurniture);
        ArrayAdapter<String> furnitureAdapter = new ArrayAdapter<String>(FormActivity.this,
                android.R.layout.simple_spinner_dropdown_item, furnitureList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        furnitureType.setAdapter(furnitureAdapter);
        furnitureType.setSelection(0);
        furnitureType.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String value = furnitureList.get(position);
                        _formModel.set_furnitureType(value);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
        //Submit data
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(this::nextScreen);
    }

    private void updateDialog() {
        String formatDate = "yyyy/MM/dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate, Locale.US);

        createdDate.setText(simpleDateFormat.format(myCalendar.getTime()));
        createdDate.setError(null);
        _formModel.set_createdDate(simpleDateFormat.format(myCalendar.getTime()));
    }

    public void nextScreen(View view) {
        boolean validation = checkValidation();
        if(validation) {
            Intent i = new Intent(FormActivity.this, MainActivity2.class);
            i.putExtra("data", _formModel);
            startActivity(i);
        }
    }

    private boolean checkValidation() {
            if(propertyType.getSelectedItem().toString().equals("Choose one (required)")) {
                TextView error = (TextView) propertyType.getSelectedView();
                error.setError("Please select a valid property type before submit");
                return false;
            }
            if(bedRoom.getSelectedItem().toString().equals("Choose one (required)")) {
                TextView error = (TextView) bedRoom.getSelectedView();
                error.setError("Please select a valid property type before submit");
                return false;
            }
            if(createdDate.getText().length() == 0);
            {
                createdDate.setError("Please choose day");
            }
            if(inputRent.length() <= 0) {
                inputRent.setError("Please fill this field before Submit");
                return false;
            }
            if(Double.parseDouble(inputRent.getText().toString()) <= 0.00) {
                inputRent.setError("Price must be larger or equal 0");
                return false;
            }
            if(inputReporter.length() <= 0) {
                inputReporter.setError("Please fill this field before Submit");
                return false;
            }

            if(inputNote.length() > 100) {
                inputNote.setError("the maximum length of notes are 100");
                return false;
            }

        return true;
    }
}