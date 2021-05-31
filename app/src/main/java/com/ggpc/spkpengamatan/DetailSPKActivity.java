package com.ggpc.spkpengamatan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ggpc.spkpengamatan.Adapter.ActivityAdapter;
import com.ggpc.spkpengamatan.Adapter.SPKRealAdapter;
import com.ggpc.spkpengamatan.Model.SPK_REAL;
import com.ggpc.spkpengamatan.Model.TK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DetailSPKActivity extends AppCompatActivity {

    TextView tvNoSPK, tvDate, tvDataNull;
    RecyclerView rvListActivity;

    public static final String data_spk = "data_spk";
    public static final String data_user = "data_user";
    public static final String data_index = "data_index";
    public static final String data_status = "data_status";
    private SPK_REAL spk_real;
    private final ArrayList<SPK_REAL> list = new ArrayList<>();
    private final ArrayList<TK> arrayTK = new ArrayList<>();
    String[] tk_nama;
    //String[] tk_kit;
    private final ArrayList<String> tk_kit = new ArrayList<>();
    private TK tk;
    ProgressDialog progressDialog;
    private String dt_status, dt_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_s_p_k);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.detail_spk);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        tvNoSPK = findViewById(R.id.tv_no_spk);
        tvDate = findViewById(R.id.tv_date_spk);
        tvDataNull = findViewById(R.id.tv_data_null);
        rvListActivity = findViewById(R.id.rv_list_activity);

        spk_real = getIntent().getParcelableExtra(data_spk);
        dt_status = getIntent().getStringExtra(data_status);
        dt_user = getIntent().getStringExtra(data_user);

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String inputDateStr = spk_real.getTanggal_SPK();
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);

        tvNoSPK.setText(spk_real.getSPK_ID());
        tvDate.setText(outputDateStr);

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
                                arrayTK.add(tk);
                                tk_kit.add(tk.getKit());

                            }
                            tk_nama = new String[arrayTK.size()];
                            //tk_kit = arrayTK.get(i)
                            //tk_kit = new String[arrayTK.size()];
                            for(int i=0 ; i< arrayTK.size();i++){
                                tk_nama[i] = arrayTK.get(i).getNama();
                                //tk_kit[i] = arrayTK.get(i).getKit();
                            }

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
                params.put("index", "000122");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getSPKActivity() {

        list.clear();
        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_SPK_ACTIVITY,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                spk_real = new SPK_REAL(
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
                                list.add(spk_real);
                            }
//                            String[] items = {"Bakso000", "Ayam Goreng", "Mie Rebus", "Nasi Padang", "Ikan Bakar", "Seblak", "Gorengan", "Mie Ayam"};
                            tvDataNull.setVisibility(View.GONE);
                            rvListActivity.setVisibility(View.VISIBLE);
                            rvListActivity.setLayoutManager(new LinearLayoutManager(DetailSPKActivity.this));
                            ActivityAdapter adapter = new ActivityAdapter(DetailSPKActivity.this, list);
                            rvListActivity.setAdapter(adapter);
                            adapter.onItemClickCallback(data -> {
                                if (data.getPengamat().equals("0")) {
                                    Intent intent = new Intent(this, SelectTKActivity.class);
                                    intent.putExtra(SelectTKActivity.data_spk, data);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(this, "Pengamat Sudah Dipilih", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            tvDataNull.setVisibility(View.VISIBLE);
                            rvListActivity.setVisibility(View.GONE);
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
                params.put("SPK_ID", spk_real.getSPK_ID());
                //params.put("SPK_Status", dt_status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailSPKActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getSPKActivityTK() {

        list.clear();
        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_SPK_ACTIVITY_TK,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                spk_real = new SPK_REAL(
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
                                list.add(spk_real);
                            }

                            tvDataNull.setVisibility(View.GONE);
                            rvListActivity.setVisibility(View.VISIBLE);
                            rvListActivity.setLayoutManager(new LinearLayoutManager(DetailSPKActivity.this));
                            ActivityAdapter adapter = new ActivityAdapter(DetailSPKActivity.this, list);
                            rvListActivity.setAdapter(adapter);
                            adapter.onItemClickCallback(data -> {
                                switch (data.getSPK_Status()) {
                                    case "NEW":
                                        if (data.getKode_Aktivitas().equals("1113511311")) {
                                            updateStatusChopper(data);
                                        }
                                        break;
                                    case "ON_PROCESS":
                                    case "REV_PENGAMAT":
                                        if (data.getKode_Aktivitas().equals("1113511311")) {
                                            Intent intent = new Intent(this, InputChopperActivity.class);
                                            intent.putExtra(InputChopperActivity.data_spk_real, data);
                                            startActivity(intent);
                                        }
                                        break;
                                    case "ON_KASIE":
                                    case "ON_MANDOR":
                                        if (data.getKode_Aktivitas().equals("1113511311")) {
                                            Intent intent = new Intent(this, ResultChopperActivity.class);
                                            intent.putExtra(ResultChopperActivity.data_spk, data);
                                            intent.putExtra(ResultChopperActivity.data_user, "Pengamat");
                                            startActivity(intent);
                                        }
                                        break;
                                    default:
                                        Toast.makeText(this, "Pengamat Sudah Dipilih", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            });

                        } else {
                            tvDataNull.setVisibility(View.VISIBLE);
                            rvListActivity.setVisibility(View.GONE);
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
                params.put("index", spk_real.getPengamat());
                params.put("SPK_ID", spk_real.getSPK_ID());
                params.put("SPK_Status", dt_status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailSPKActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getSPKActivityMandor() {

        list.clear();
        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_SPK_ACTIVITY_TK,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                spk_real = new SPK_REAL(
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
                                list.add(spk_real);
                            }

                            tvDataNull.setVisibility(View.GONE);
                            rvListActivity.setVisibility(View.VISIBLE);
                            rvListActivity.setLayoutManager(new LinearLayoutManager(DetailSPKActivity.this));
                            ActivityAdapter adapter = new ActivityAdapter(DetailSPKActivity.this, list);
                            rvListActivity.setAdapter(adapter);
                            adapter.onItemClickCallback(data -> {
                                switch (data.getSPK_Status()) {
                                    case "ON_PROCESS":
                                    case "NEW":
                                        Toast.makeText(this, "Sedang Dilakukan Pengamatan", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "ON_KASIE":
                                    case "ON_MANDOR":
                                    case "REV_MANDOR":
                                        if (data.getKode_Aktivitas().equals("1113511311")) {
                                            Intent intent = new Intent(this, ResultChopperActivity.class);
                                            intent.putExtra(ResultChopperActivity.data_spk, data);
                                            intent.putExtra(ResultChopperActivity.data_user, "Mandor_Operator");
                                            startActivity(intent);
                                        }
                                        break;
                                    case "REV_PENGAMAT":
                                        Toast.makeText(this, "Sedang Dilakukan Perbaikan Data", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(this, "Pengamat Sudah Dipilih", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            });

                        } else {
                            /*dt_status = "NEW";
                            getSPKActivityMandor();*/
                            tvDataNull.setVisibility(View.VISIBLE);
                            rvListActivity.setVisibility(View.GONE);
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
                params.put("index", spk_real.getMandor_Operator());
                params.put("SPK_ID", spk_real.getSPK_ID());
                params.put("SPK_Status", dt_status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailSPKActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getSPKActivityKasie() {

        list.clear();
        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_SPK_ACTIVITY_TK,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                spk_real = new SPK_REAL(
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
                                list.add(spk_real);
                            }
                            tvDataNull.setVisibility(View.GONE);
                            rvListActivity.setVisibility(View.VISIBLE);
                            rvListActivity.setLayoutManager(new LinearLayoutManager(DetailSPKActivity.this));
                            ActivityAdapter adapter = new ActivityAdapter(DetailSPKActivity.this, list);
                            rvListActivity.setAdapter(adapter);
                            adapter.onItemClickCallback(data -> {
                                switch (data.getSPK_Status()) {
                                    case "RELEASE":
                                        Toast.makeText(this, "Pengamat Belum Ditentukan Oleh Mandor", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "NEW":
                                        Toast.makeText(this, "Pengamat Sedang Melakukan Pengamatan", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "ON_KASIE":
                                        if (data.getKode_Aktivitas().equals("1113511311")) {
                                            Intent intent = new Intent(this, ResultChopperActivity.class);
                                            intent.putExtra(ResultChopperActivity.data_spk, data);
                                            intent.putExtra(ResultChopperActivity.data_user, "Kasie");
                                            startActivity(intent);
                                        }
                                        break;
                                    case "REV_MANDOR":
                                        Toast.makeText(this, "Sedang Dilakukan Perbaikan Data", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "CLOSE":
                                        Intent intent = new Intent(this, ResultChopperActivity.class);
                                        intent.putExtra(ResultChopperActivity.data_spk, data);
                                        intent.putExtra(ResultChopperActivity.data_user, "Pengamat");
                                        startActivity(intent);
                                        break;
                                    default:
                                        Toast.makeText(this, "Pengamat Sudah Dipilih", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            });

                        } else {
                            tvDataNull.setVisibility(View.VISIBLE);
                            rvListActivity.setVisibility(View.GONE);
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
                params.put("index", spk_real.getKasie());
                params.put("SPK_ID", spk_real.getSPK_ID());
                params.put("SPK_Status", dt_status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailSPKActivity.this);
        requestQueue.add(stringRequest);
    }

    private void updateStatusChopper(SPK_REAL dt_spk_real) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.UPDATE_STATUS_SPK,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("message").equalsIgnoreCase(String.valueOf(true))) {
                            Toast.makeText(this, "Data Berhasil Dikirim", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, InputChopperActivity.class);
                            intent.putExtra(InputChopperActivity.data_spk_real, dt_spk_real);
                            startActivity(intent);
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
                params.put("SPK_Status", "ON_PROCESS");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getTK();

        if (dt_user.equals("Pengamat")) {
            getSPKActivityTK();
        } else if (dt_user.equals("Mandor_Operator") && !dt_status.equals("RELEASE")) {
            getSPKActivityMandor();
        } else if (dt_user.equals("Kasie")) {
            getSPKActivityKasie();
        } else {
            getSPKActivity();
        }
    }
}