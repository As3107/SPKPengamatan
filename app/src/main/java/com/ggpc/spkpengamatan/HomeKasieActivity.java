package com.ggpc.spkpengamatan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ggpc.spkpengamatan.Adapter.SPKAdapter;
import com.ggpc.spkpengamatan.Model.SPK;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeKasieActivity extends AppCompatActivity {

    TextView tvName, tvTotalActivity, tvTotalWaiting, tvTotalRevisi, tvTotalFinish;
    ImageView ivLogout;
    MaterialCardView cvSpkRelease, cvWaitingApproved, cvRevisiMandor, cvFinish, cvHistory;

    String nama_kasie, date, index_kasie;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_kasie);

        Objects.requireNonNull(getSupportActionBar()).hide();

        ivLogout = findViewById(R.id.iv_logout);
        tvName = findViewById(R.id.tv_name);
        cvSpkRelease = findViewById(R.id.cv_spk_release);
        cvWaitingApproved = findViewById(R.id.cv_waiting_approved);
        cvRevisiMandor = findViewById(R.id.cv_revisi_mandor);
        cvFinish = findViewById(R.id.cv_finish);
        cvHistory = findViewById(R.id.cv_history);
        tvTotalActivity = findViewById(R.id.tv_total_activity);
        tvTotalRevisi = findViewById(R.id.tv_total_revisi_mandor);
        tvTotalWaiting = findViewById(R.id.tv_total_waiting);
        tvTotalFinish = findViewById(R.id.tv_total_finish);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        nama_kasie = SharedPrefManager.getInstance(this).getDataKasie().getNama();
        index_kasie = SharedPrefManager.getInstance(this).getDataKasie().getKasieIndex();
        tvName.setText(nama_kasie);

        swipeRefreshLayout.setProgressViewEndTarget(false,0);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getTotalActivity();
            getTotalWaiting();
            getTotalRevMandor();
            getTotalFinish();
            new Handler().postDelayed(() ->
                    swipeRefreshLayout.setRefreshing(false), 2000);
        });

        ivLogout.setOnClickListener(v -> confirmLogout());

        cvSpkRelease.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListSPKActivity.class);
            intent.putExtra(ListSPKActivity.data_user, "Kasie");
            intent.putExtra(ListSPKActivity.data_index, index_kasie);
            intent.putExtra(ListSPKActivity.data_status, "RELEASED");
            startActivity(intent);
        });

        cvWaitingApproved.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListSPKActivity.class);
            intent.putExtra(ListSPKActivity.data_user, "Kasie");
            intent.putExtra(ListSPKActivity.data_index, index_kasie);
            intent.putExtra(ListSPKActivity.data_status, "ON_KASIE");
            startActivity(intent);
        });

        cvRevisiMandor.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListSPKActivity.class);
            intent.putExtra(ListSPKActivity.data_user, "Kasie");
            intent.putExtra(ListSPKActivity.data_index, index_kasie);
            intent.putExtra(ListSPKActivity.data_status, "REV_MANDOR");
            startActivity(intent);
        });

        cvFinish.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListSPKActivity.class);
            intent.putExtra(ListSPKActivity.data_user, "Kasie");
            intent.putExtra(ListSPKActivity.data_index, index_kasie);
            intent.putExtra(ListSPKActivity.data_status, "CLOSE");
            startActivity(intent);
        });

        cvHistory.setOnClickListener(v -> {
            Toast.makeText(this, "Fitur Masih Dalam Pengembangan", Toast.LENGTH_SHORT).show();
        });

    }

    private void confirmLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeKasieActivity.this);
        builder.setTitle(R.string.logout);
        builder.setMessage("Apakah Anda Yakin Ingin Keluar Dari Aplikasi");

        builder.setPositiveButton("Ya", (dialog, which) -> {
            SharedPrefManager.getInstance(this).logout();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        builder.setNegativeButton("Tidak", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getTotalActivity(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_TOTAL_ACTIVITY_SRV,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))){
                            tvTotalActivity.setText(jsonObject.getString("data"));
                        } else {
                            tvTotalActivity.setText("0");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("column_name", "Kasie");
                params.put("index", index_kasie);
                params.put("SPK_Status", "NEW");
                params.put("SPK_Status2", "RELEASE");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getTotalWaiting(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_TOTAL_ACTIVITY_SRV,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))){
                            tvTotalWaiting.setText(jsonObject.getString("data"));
                        } else {
                            tvTotalWaiting.setText("0");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("column_name", "Kasie");
                params.put("index", index_kasie);
                params.put("SPK_Status", "ON_KASIE");
                params.put("SPK_Status2", "ON_KASIE");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getTotalRevMandor(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_TOTAL_ACTIVITY_SRV,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))){
                            tvTotalRevisi.setText(jsonObject.getString("data"));
                        } else {
                            tvTotalRevisi.setText("0");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("column_name", "Kasie");
                params.put("index", index_kasie);
                params.put("SPK_Status", "REV_MANDOR");
                params.put("SPK_Status2", "REV_MANDOR");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getTotalFinish(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_TOTAL_ACTIVITY_SRV,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))){
                            tvTotalFinish.setText(jsonObject.getString("data"));
                        } else {
                            tvTotalFinish.setText("0");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("column_name", "Kasie");
                params.put("index", index_kasie);
                params.put("SPK_Status", "CLOSE");
                params.put("SPK_Status2", "CLOSE");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setDate(){
        Calendar calendar = Calendar.getInstance();
        date = calendar.get(Calendar.YEAR) +"-"+ (calendar.get(Calendar.MONTH)+1) +"-"+ calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void onResume() {
        setDate();
        getTotalActivity();
        getTotalWaiting();
        getTotalRevMandor();
        getTotalFinish();
        super.onResume();
    }
}