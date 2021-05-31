package com.ggpc.spkpengamatan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ggpc.spkpengamatan.Model.Kasie;
import com.ggpc.spkpengamatan.Model.Mandor;
import com.ggpc.spkpengamatan.Model.TK;
import com.ggpc.spkpengamatan.Model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText etUsername, etPassword;
    MaterialButton btLogin;

    private String kit, password, newToken;
    private User user;
    private Mandor mandor;
    private TK tk;
    private Kasie kasie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).hide();
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btLogin = findViewById(R.id.bt_login);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> newToken = instanceIdResult.getToken());

        btLogin.setOnClickListener(v -> {
            kit = etUsername.getText().toString().trim();
            password = etPassword.getText().toString().trim();

            if (kit.matches("")){
                Toast.makeText(this, "Username Harus Diisi", Toast.LENGTH_SHORT).show();
            } else if (password.matches("")){
                Toast.makeText(this, "Password Harus Diisi", Toast.LENGTH_SHORT).show();
            } else {
                getUser();
            }
        });

    }

    // Method getUser digunakan untuk mengambil data user account berdasarkan levelnya masing-masing
    private void getUser() {

        ProgressDialog progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.LOGIN_URL,
                response -> {
                progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))) {
                            JSONObject objUser = jsonObject.getJSONObject("data");

                            user = new User(objUser.getString("kit"),
                                    objUser.getString("password"),
                                    objUser.getString("level"));

                            SharedPrefManager.getInstance(LoginActivity.this).setUserLogin(user);

                            if (SharedPrefManager.getInstance(this).login()) {
                                switch (user.getLevel()) {
                                    case "tk": {
                                        getProfileTK();
                                        break;
                                    }
                                    case "mandor": {
                                        getProfileMandor();
                                        break;
                                    }
                                    case "kasie": {
                                        getProfileKasie();
                                        break;
                                    }
                                }
                            }

                        } else {
                            Toast.makeText(this, "Username atau Password Salah", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Log.d("TAG", "error: " + error);
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kit", kit);
                params.put("password", password);
                return params;
            }

            /*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "text/html; charset=UTF-8");
                return headers;
            }*/
        };

        requestQueue.add(stringRequest);
    }

    // Ketika berhasil mendapatkan user maka
    private void getProfileTK(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_PROFILE_TK_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))) {
                            JSONObject data = jsonObject.getJSONObject("data");

                            tk = new TK(
                                    data.getString("kit"),
                                    data.getString("nama"),
                                    data.getString("mandor")
                            );
                            updateTokenTK();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("kit", kit);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }

    private void updateTokenTK(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.UPDATE_TOKEN_TK,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if(obj.getString("message").equalsIgnoreCase(String.valueOf(true))){
                            SharedPrefManager.getInstance(LoginActivity.this).setDataTK(tk);
                            SharedPrefManager.getInstance(LoginActivity.this).login();
                            Intent intent = new Intent(LoginActivity.this, HomeTkActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },

                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kit", tk.getKit());
                params.put("token", newToken);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getProfileMandor(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_PROFILE_MANDOR_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))) {
                            JSONObject data = jsonObject.getJSONObject("data");

                            mandor = new Mandor(
                                    data.getString("index"),
                                    data.getString("nama"),
                                    data.getString("kasie")
                            );
                            updateTokenMandor();
                            /*SharedPrefManager.getInstance(LoginActivity.this).setDataMandor(mandor);
                            SharedPrefManager.getInstance(LoginActivity.this).login();
                            Intent intent = new Intent(LoginActivity.this, HomeMandorActivity.class);
                            startActivity(intent);
                            finish();*/
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("index", kit);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }

    private void updateTokenMandor(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.UPDATE_TOKEN_MANDOR,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if(obj.getString("message").equalsIgnoreCase(String.valueOf(true))){
                            SharedPrefManager.getInstance(LoginActivity.this).setDataMandor(mandor);
                            SharedPrefManager.getInstance(LoginActivity.this).login();
                            Intent intent = new Intent(LoginActivity.this, HomeMandorActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },

                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("MandorIndex", mandor.getMandorIndex());
                params.put("token", newToken);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getProfileKasie(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_PROFILE_KASIE_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))) {
                            JSONObject data = jsonObject.getJSONObject("data");

                            kasie = new Kasie(
                                    data.getString("id"),
                                    data.getString("nama"),
                                    data.getString("index")
                            );
                            updateTokenKasie();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("index", kit);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }

    private void updateTokenKasie(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.UPDATE_TOKEN_KASIE,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if(obj.getString("message").equalsIgnoreCase(String.valueOf(true))){
                            SharedPrefManager.getInstance(LoginActivity.this).setDataKasie(kasie);
                            SharedPrefManager.getInstance(LoginActivity.this).login();
                            Intent intent = new Intent(LoginActivity.this, HomeKasieActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },

                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("KasieIndex", user.getkit());
                params.put("token", newToken);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }

}