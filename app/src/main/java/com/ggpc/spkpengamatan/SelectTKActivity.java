package com.ggpc.spkpengamatan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ggpc.spkpengamatan.Adapter.SelectTKAdapter;
import com.ggpc.spkpengamatan.Model.SPK_REAL;
import com.ggpc.spkpengamatan.Model.TK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SelectTKActivity extends AppCompatActivity {

    private TK tk;
    ArrayList<TK> list_tk = new ArrayList<>();
    ArrayList kit_tk = new ArrayList();
    ArrayList name_tk = new ArrayList();
    private String index_mandor;
    RecyclerView recyclerViewTK;
    Button btTugaskan;

    private SPK_REAL spk_real;
    public static final String data_spk = "data_spk";
    private String kit;
    private SelectTKAdapter selectTKAdapter;
    public static ArrayList<String> arrayListUser = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__t_k);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Pilih Pengamat");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerViewTK = findViewById(R.id.recycleTKselect);
        btTugaskan = findViewById(R.id.bt_tugaskan);

        index_mandor = SharedPrefManager.getInstance(this).getDataMandor().getMandorIndex();
        spk_real = getIntent().getParcelableExtra(data_spk);

        getTK();

        btTugaskan.setOnClickListener(v -> {
            ArrayList<TK> obj = SelectTKAdapter.getTkselect();
            int i = 0;
            for (TK item : obj) {
                if (item.getValue() != null) {
                    kit_tk.add(item.getValue());
                    name_tk.add(item.getName());
                }
            }

            for (i = 0; i < kit_tk.size(); i++) {
                kit_tk.get(i);
                name_tk.add(i);
            }

            if (i > 1) {
                Toast.makeText(this, "Pengamat Tidak Boleh Lebih Satu", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(this, "Hasil" + nilai.get(0), Toast.LENGTH_SHORT).show();
                sendObserver(kit_tk.get(0).toString(), name_tk.get(0).toString());
            }
            kit_tk.clear();
            finish();

        });

    }

    private void getTK() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_LIST_TK_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                tk = new TK(
                                        data.getString("kit"),
                                        data.getString("nama"),
                                        data.getString("MandorIndex")
                                );
                                list_tk.add(tk);
                            }

                            recyclerViewTK.setLayoutManager(new LinearLayoutManager(SelectTKActivity.this));
                            SelectTKAdapter adapter = new SelectTKAdapter(SelectTKActivity.this, list_tk);
                            recyclerViewTK.setAdapter(adapter);
                            /*adapter.onItemClickCallback(data -> {
                                Intent intent = new Intent(this, DetailSPKActivity.class);
                                intent.putExtra(DetailSPKActivity.data_spk, data);
                                startActivity(intent);
                            });*/
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("index", spk_real.getMandor_Operator());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void sendObserver(String tk_kit, String tk_name) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SEND_OBSERVER,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("message").equalsIgnoreCase(String.valueOf(true))) {
                            Toast.makeText(this, "Data Berhasil Dikirim", Toast.LENGTH_SHORT).show();
                            sendNotifTK(tk_kit);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },

                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID", spk_real.getID());
                params.put("SPK_ID", spk_real.getSPK_ID());
                params.put("Pengamat", tk_kit);
                params.put("Nama_Pengamat", tk_name);
                params.put("SPK_Status", "NEW");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void sendNotifTK(String kit_tk) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SEND_NOTIF_TK,
                response -> {
                    Toast.makeText(SelectTKActivity.this, "Notifikasi Terkirim", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", "SPK Hari ini");
                params.put("message", spk_real.getDeskripsi());
                params.put("index", kit_tk);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(this));
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}