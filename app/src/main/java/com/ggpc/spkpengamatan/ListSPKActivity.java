package com.ggpc.spkpengamatan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import com.ggpc.spkpengamatan.Adapter.SPKRealAdapter;
import com.ggpc.spkpengamatan.Adapter.SPKSRVAdapter;
import com.ggpc.spkpengamatan.Model.SPK_HEADER_SRV;
import com.ggpc.spkpengamatan.Model.SPK_REAL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ListSPKActivity extends AppCompatActivity {

    TextView tvTitleMandor, tvTitleKasie;
    TextView tvDataNull, tvMandor, tvKasie, tvDivision, tvDate;
    RecyclerView rvListSPK;
    SwipeRefreshLayout swipeRefreshLayout;

    ProgressDialog progressDialog;
    //private final List<SPK_REAL> list = new ArrayList<>();
    private final ArrayList<SPK_HEADER_SRV>list_srv = new ArrayList<>();
    String index_mandor, date, dt_user, dt_index, dt_status, dt_status2;

    public static final String data_user = "data_user";
    public static final String data_index = "data_index";
    public static final String data_status = "data_status";
    //private SPK_REAL spk_real;
    private SPK_HEADER_SRV spk_header_srv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_s_p_k);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.list_spk);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        tvDataNull = findViewById(R.id.tv_data_null);
        tvTitleMandor = findViewById(R.id.tv_title_mandor);
        tvTitleKasie = findViewById(R.id.tv_title_kasie);
        tvMandor = findViewById(R.id.tv_mandor);
        tvKasie = findViewById(R.id.tv_kasie);
        tvDivision = findViewById(R.id.tv_division);
        tvDate = findViewById(R.id.tv_date);
        rvListSPK = findViewById(R.id.rv_list_spk);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        setDate();
        dt_user = getIntent().getStringExtra(data_user);
        dt_index = getIntent().getStringExtra(data_index);
        dt_status = getIntent().getStringExtra(data_status);

        getData();

        swipeRefreshLayout.setProgressViewEndTarget(false,0);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            list_srv.clear();
            getData();
            new Handler().postDelayed(() ->
                    swipeRefreshLayout.setRefreshing(false), 2000);
        });

        tvDate.setText(date);

    }

    private void getData(){
        assert dt_user!=null;
        switch (dt_user) {
            case "Pengamat":
                tvTitleMandor.setText(R.string.pengamat);
                tvTitleKasie.setText(R.string.mandor);
                tvMandor.setText(SharedPrefManager.getInstance(this).getDataTK().getNama());
                tvKasie.setText(SharedPrefManager.getInstance(this).getDataTK().getMandor());
                dt_status2 = dt_status;
                getSPKHeader();
                break;
            case "Mandor_Operator":
                tvMandor.setText(SharedPrefManager.getInstance(this).getDataMandor().getNama());
                tvKasie.setText(SharedPrefManager.getInstance(this).getDataMandor().getKasieIndex());
                if (dt_status.equals("ON_PROCESS")) {
                    dt_status2 = "NEW";
                } else {
                    dt_status2 = dt_status;
                }
                if (dt_status.equals("RELEASE")){
                    getSPKHeaderMandor();
                } else {
                    getSPKHeader();
                }

                break;
            case "Kasie":
                tvTitleMandor.setVisibility(View.GONE);
                tvMandor.setVisibility(View.GONE);
                tvKasie.setText(SharedPrefManager.getInstance(this).getDataKasie().getKasieIndex());
                dt_status2 = dt_status;
                getSPKHeader();
                break;
        }
    }

    private void getSPKHeader() {

        list_srv.clear();
        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_SPK_HEADER_SRV,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                /*spk_real = new SPK_REAL(
                                        data.getString("ID"),
                                        data.getString("SPK_ID"),
                                        data.getString("Tanggal_SPK"),
                                        data.getString("Tanggal_Realisasi"),
                                        data.getString("Item"),
                                        data.getString("Kode_Aktivitas"),
                                        data.getString("Deskripsi"),
                                        data.getString("Lokasi"),
                                        data.getString("Kebun"),
                                        data.getString("Wilayah"),
                                        data.getString("Pengamat"),
                                        data.getString("Nama_Pengamat"),
                                        data.getString("Mandor_Operator"),
                                        data.getString("Kasie"),
                                        data.getString("Bagian"),
                                        data.getString("SPK_Status")
                                );
                                list.add(spk_real);*/

                                spk_header_srv = new SPK_HEADER_SRV(
                                        data.getString("SPK_ID"),
                                        data.getString("Tanggal_SPK"));
                                list_srv.add(spk_header_srv);
                            }
                            tvDataNull.setVisibility(View.GONE);
                            rvListSPK.setVisibility(View.VISIBLE);
                            rvListSPK.setLayoutManager(new LinearLayoutManager(ListSPKActivity.this));
                            SPKSRVAdapter adapter = new SPKSRVAdapter(ListSPKActivity.this, list_srv);
                            rvListSPK.setAdapter(adapter);
                            adapter.onItemClickCallback(data -> {
                                Intent intent = new Intent(this, DetailSPKActivity2.class);
                                intent.putExtra(DetailSPKActivity2.data_spk, data);
                                intent.putExtra(DetailSPKActivity2.data_status, dt_status);
                                intent.putExtra(DetailSPKActivity2.data_user, dt_user);
                                startActivity(intent);

                            });

                        } else {
                            tvDataNull.setVisibility(View.VISIBLE);
                            rvListSPK.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("column_name", dt_user);
                params.put("index", dt_index);
                params.put("SPK_Status", dt_status);
                params.put("SPK_Status2", dt_status2);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ListSPKActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getSPKHeaderMandor() {

        list_srv.clear();
        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_SPK_HEADER_MANDOR_SRV,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                /*spk_real = new SPK_REAL(
                                        data.getString("ID"),
                                        data.getString("SPK_ID"),
                                        data.getString("Tanggal_SPK"),
                                        data.getString("Tanggal_Realisasi"),
                                        data.getString("Item"),
                                        data.getString("Kode_Aktivitas"),
                                        data.getString("Deskripsi"),
                                        data.getString("Lokasi"),
                                        data.getString("Kebun"),
                                        data.getString("Wilayah"),
                                        data.getString("Pengamat"),
                                        data.getString("Nama_Pengamat"),
                                        data.getString("Mandor_Operator"),
                                        data.getString("Kasie"),
                                        data.getString("Bagian"),
                                        data.getString("SPK_Status")
                                );
                                list.add(spk_real);*/

                                spk_header_srv = new SPK_HEADER_SRV(
                                        data.getString("SPK_ID"),
                                        data.getString("Tanggal_SPK"));
                                list_srv.add(spk_header_srv);

                            }
                            tvDataNull.setVisibility(View.GONE);
                            rvListSPK.setVisibility(View.VISIBLE);
                            rvListSPK.setLayoutManager(new LinearLayoutManager(ListSPKActivity.this));
                            SPKSRVAdapter adapter = new SPKSRVAdapter(ListSPKActivity.this, list_srv);
                            rvListSPK.setAdapter(adapter);
                            adapter.onItemClickCallback(data -> {
                                Intent intent = new Intent(this, DetailSPKActivity2.class);
                                intent.putExtra(DetailSPKActivity2.data_spk, data);
                                intent.putExtra(DetailSPKActivity2.data_status, dt_status);
                                intent.putExtra(DetailSPKActivity2.data_user, dt_user);
                                startActivity(intent);

                            });

                        } else {
                            tvDataNull.setVisibility(View.VISIBLE);
                            rvListSPK.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("column_name", dt_user);
                params.put("index", dt_index);
                params.put("SPK_Status", "CLOSE");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ListSPKActivity.this);
        requestQueue.add(stringRequest);
    }

    private void setDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.YEAR);
        calendar.get(Calendar.MONTH);
        calendar.get(Calendar.DAY_OF_MONTH);
        //date = calendar.get(Calendar.DAY_OF_MONTH) +"-"+ (calendar.get(Calendar.MONTH)+1) +"-"+ calendar.get(Calendar.YEAR);
        date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}