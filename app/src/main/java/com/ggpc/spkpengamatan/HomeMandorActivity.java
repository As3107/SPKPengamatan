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

public class HomeMandorActivity extends AppCompatActivity {

    TextView tvName, tvDataNull, tvTotalActivity, tvTotalOnProcess, tvTotalWaitting, tvTotalApproved, tvTotalRevisiKasie, tvTotalRevisiObserver;
    RecyclerView rvListSPK;
    ImageView ivLogout;
    MaterialCardView cvSpkRelease, cvOnProcess, cvWaitingApproved, cvApproved, cvRevisiKasie, cvRevisiTk;

    String nama_mandor, index_mandor, index_kasi, date;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_mandor_new);

        Objects.requireNonNull(getSupportActionBar()).hide();

        ivLogout = findViewById(R.id.iv_logout);
        tvName = findViewById(R.id.tv_name);
        tvDataNull = findViewById(R.id.tv_data_null);
        rvListSPK = findViewById(R.id.rv_list_data);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        cvSpkRelease = findViewById(R.id.cv_spk_release);
        cvOnProcess = findViewById(R.id.cv_on_process);
        cvWaitingApproved = findViewById(R.id.cv_waiting_approved);
        cvApproved = findViewById(R.id.cv_approved);
        cvRevisiKasie = findViewById(R.id.cv_revisi_kasie);
        cvRevisiTk = findViewById(R.id.cv_revisi_tk);
        tvTotalActivity = findViewById(R.id.tv_total_activity);
        tvTotalOnProcess = findViewById(R.id.tv_total_on_process);
        tvTotalWaitting = findViewById(R.id.tv_total_waiting);
        tvTotalApproved = findViewById(R.id.tv_total_approved);
        tvTotalRevisiKasie = findViewById(R.id.tv_total_revisi_kasie);
        tvTotalRevisiObserver = findViewById(R.id.tv_total_revisi_tk);

        nama_mandor = SharedPrefManager.getInstance(this).getDataMandor().getNama();
        index_mandor = SharedPrefManager.getInstance(this).getDataMandor().getMandorIndex();
        index_kasi = SharedPrefManager.getInstance(this).getDataMandor().getKasieIndex();
        tvName.setText(nama_mandor);

        swipeRefreshLayout.setProgressViewEndTarget(false,0);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            onResume();
            new Handler().postDelayed(() ->
                    swipeRefreshLayout.setRefreshing(false), 2000);
        });

        ivLogout.setOnClickListener(v -> confirmLogout());

        cvSpkRelease.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListSPKActivity.class);
            intent.putExtra(ListSPKActivity.data_user, "Mandor_Operator");
            intent.putExtra(ListSPKActivity.data_index, index_mandor);
            intent.putExtra(ListSPKActivity.data_status, "RELEASE");
            startActivity(intent);
        });

        cvOnProcess.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListSPKActivity.class);
            intent.putExtra(ListSPKActivity.data_user, "Mandor_Operator");
            intent.putExtra(ListSPKActivity.data_index, index_mandor);
            intent.putExtra(ListSPKActivity.data_status, "ON_PROCESS");
            startActivity(intent);
        });

        cvWaitingApproved.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListSPKActivity.class);
            intent.putExtra(ListSPKActivity.data_user, "Mandor_Operator");
            intent.putExtra(ListSPKActivity.data_index, index_mandor);
            intent.putExtra(ListSPKActivity.data_status, "ON_MANDOR");
            startActivity(intent);
        });

        cvApproved.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListSPKActivity.class);
            intent.putExtra(ListSPKActivity.data_user, "Mandor_Operator");
            intent.putExtra(ListSPKActivity.data_index, index_mandor);
            intent.putExtra(ListSPKActivity.data_status, "ON_KASIE");
            startActivity(intent);
        });

        cvRevisiKasie.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListSPKActivity.class);
            intent.putExtra(ListSPKActivity.data_user, "Mandor_Operator");
            intent.putExtra(ListSPKActivity.data_index, index_mandor);
            intent.putExtra(ListSPKActivity.data_status, "REV_MANDOR");
            startActivity(intent);
        });

        cvRevisiTk.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListSPKActivity.class);
            intent.putExtra(ListSPKActivity.data_user, "Mandor_Operator");
            intent.putExtra(ListSPKActivity.data_index, index_mandor);
            intent.putExtra(ListSPKActivity.data_status, "REV_PENGAMAT");
            startActivity(intent);
        });

    }

    private void setDate(){
        Calendar calendar = Calendar.getInstance();
        date = calendar.get(Calendar.YEAR) +"-"+ (calendar.get(Calendar.MONTH)+1) +"-"+ calendar.get(Calendar.DAY_OF_MONTH);
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
                params.put("column_name", "Mandor_Operator");
                params.put("index", index_mandor);
                params.put("SPK_Status", "RELEASE");
                params.put("SPK_Status2", "RELEASE");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getTotalOnProcess(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_TOTAL_ACTIVITY_SRV,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))){
                            tvTotalOnProcess.setText(jsonObject.getString("data"));
                        } else {
                            tvTotalOnProcess.setText("0");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("column_name", "Mandor_Operator");
                params.put("index", index_mandor);
                params.put("SPK_Status", "NEW");
                params.put("SPK_Status2", "ON_PROCESS");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getTotalWaitingApproved(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_TOTAL_ACTIVITY_SRV,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))){
                            tvTotalWaitting.setText(jsonObject.getString("data"));
                        } else {
                            tvTotalWaitting.setText("0");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("column_name", "Mandor_Operator");
                params.put("index", index_mandor);
                params.put("SPK_Status", "ON_MANDOR");
                params.put("SPK_Status2", "ON_MANDOR");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getTotalApproved(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_TOTAL_ACTIVITY_SRV,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))){
                            tvTotalApproved.setText(jsonObject.getString("data"));
                        } else {
                            tvTotalApproved.setText("0");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("column_name", "Mandor_Operator");
                params.put("index", index_mandor);
                params.put("SPK_Status", "ON_KASIE");
                params.put("SPK_Status2", "ON_KASIE");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getTotalRevisiKasie(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_TOTAL_ACTIVITY_SRV,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))){
                            tvTotalRevisiKasie.setText(jsonObject.getString("data"));
                        } else {
                            tvTotalRevisiKasie.setText("0");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("column_name", "Mandor_Operator");
                params.put("index", index_mandor);
                params.put("SPK_Status", "REV_MANDOR");
                params.put("SPK_Status2", "REV_MANDOR");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getTotalRevisiTK(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_TOTAL_ACTIVITY_SRV,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))){
                            tvTotalRevisiObserver.setText(jsonObject.getString("data"));
                        } else {
                            tvTotalRevisiObserver.setText("0");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("column_name", "Mandor_Operator");
                params.put("index", index_mandor);
                params.put("SPK_Status", "REV_PENGAMAT");
                params.put("SPK_Status2", "REV_PENGAMAT");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void confirmLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeMandorActivity.this);
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

    @Override
    protected void onResume() {
        super.onResume();
        getTotalActivity();
        getTotalOnProcess();
        getTotalWaitingApproved();
        getTotalApproved();
        getTotalRevisiKasie();
        getTotalRevisiTK();
    }

}