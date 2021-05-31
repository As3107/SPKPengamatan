package com.ggpc.spkpengamatan;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ggpc.spkpengamatan.Model.TK;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HomeTkActivity extends AppCompatActivity {

    TextView tvName, tvDataNull, tvTotalActivity, tvTotalOnProcess, tvTotalWaiting, tvTotalApproved, tvTotalRevisi;
    ImageView ivLogout;
    SwipeRefreshLayout swipeRefreshLayout;
    MaterialCardView cvSpkRelease, cvOnProcess, cvWaitingApproved, cvApproved, cvRevisi;

    String index_tk, nama, date;
    private TK tk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tk);

        Objects.requireNonNull(getSupportActionBar()).hide();

        tvDataNull = findViewById(R.id.tv_data_null);
        ivLogout = findViewById(R.id.iv_logout);
        tvName = findViewById(R.id.tv_name);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        cvSpkRelease = findViewById(R.id.cv_spk_release);
        cvOnProcess = findViewById(R.id.cv_on_process);
        cvWaitingApproved = findViewById(R.id.cv_waiting_approved);
        cvApproved = findViewById(R.id.cv_approved);
        cvRevisi = findViewById(R.id.cv_revisi);
        tvTotalActivity = findViewById(R.id.tv_total_activity);
        tvTotalOnProcess = findViewById(R.id.tv_total_on_process);
        tvTotalWaiting = findViewById(R.id.tv_total_waiting);
        tvTotalApproved = findViewById(R.id.tv_total_approved);
        tvTotalRevisi = findViewById(R.id.tv_total_revisi);

        index_tk = SharedPrefManager.getInstance(this).getDataTK().getKit();
        nama = SharedPrefManager.getInstance(this).getDataTK().getNama();

        tvName.setText(nama);

        setDate();

        swipeRefreshLayout.setProgressViewEndTarget(false, 0);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() ->
                    swipeRefreshLayout.setRefreshing(false), 2000);
        });

        ivLogout.setOnClickListener(v -> confirmLogout());

        cvSpkRelease.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListSPKActivity.class);
            intent.putExtra(ListSPKActivity.data_user, "Pengamat");
            intent.putExtra(ListSPKActivity.data_index, index_tk);
            intent.putExtra(ListSPKActivity.data_status, "NEW");
            startActivity(intent);
        });

        cvOnProcess.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListSPKActivity.class);
            intent.putExtra(ListSPKActivity.data_user, "Pengamat");
            intent.putExtra(ListSPKActivity.data_index, index_tk);
            intent.putExtra(ListSPKActivity.data_status, "ON_PROCESS");
            startActivity(intent);
        });

        cvWaitingApproved.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListSPKActivity.class);
            intent.putExtra(ListSPKActivity.data_user, "Pengamat");
            intent.putExtra(ListSPKActivity.data_index, index_tk);
            intent.putExtra(ListSPKActivity.data_status, "ON_MANDOR");
            startActivity(intent);
        });

        cvApproved.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListSPKActivity.class);
            intent.putExtra(ListSPKActivity.data_user, "Pengamat");
            intent.putExtra(ListSPKActivity.data_index, index_tk);
            intent.putExtra(ListSPKActivity.data_status, "ON_KASIE");
            startActivity(intent);
        });

        cvRevisi.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListSPKActivity.class);
            intent.putExtra(ListSPKActivity.data_user, "Pengamat");
            intent.putExtra(ListSPKActivity.data_index, index_tk);
            intent.putExtra(ListSPKActivity.data_status, "REV_PENGAMAT");
            startActivity(intent);
        });


    }

    private void setDate() {
        Calendar calendar = Calendar.getInstance();
        date = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void getTotalActivity() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_TOTAL_ACTIVITY_SRV,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))) {
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
                params.put("column_name", "Pengamat");
                params.put("index", index_tk);
                params.put("SPK_Status", "NEW");
                params.put("SPK_Status2", "NEW");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getTotalOnProcess() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_TOTAL_ACTIVITY_SRV,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))) {
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
                params.put("column_name", "Pengamat");
                params.put("index", index_tk);
                params.put("SPK_Status", "ON_PROCESS");
                params.put("SPK_Status2", "ON_PROCESS");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getTotalWaitingApproved() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_TOTAL_ACTIVITY_SRV,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))) {
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
                params.put("column_name", "Pengamat");
                params.put("index", index_tk);
                params.put("SPK_Status", "ON_MANDOR");
                params.put("SPK_Status2", "ON_MANDOR");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getTotalApproved() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_TOTAL_ACTIVITY_SRV,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))) {
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
                params.put("column_name", "Pengamat");
                params.put("index", index_tk);
                params.put("SPK_Status", "ON_KASIE");
                params.put("SPK_Status2", "ON_KASIE");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getTotalRevisiTK() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_TOTAL_ACTIVITY_SRV,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))) {
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
                params.put("column_name", "Pengamat");
                params.put("index", index_tk);
                params.put("SPK_Status", "REV_PENGAMAT");
                params.put("SPK_Status2", "REV_PENGAMAT");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void confirmLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeTkActivity.this);
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
        getTotalRevisiTK();

    }
}