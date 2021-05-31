package com.ggpc.spkpengamatan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ggpc.spkpengamatan.Adapter.ActivityAdapter;
import com.ggpc.spkpengamatan.Adapter.ActivityAdapter2;
import com.ggpc.spkpengamatan.Adapter.ActivitySRVAdapter;
import com.ggpc.spkpengamatan.Adapter.ActivitySRVAdapter2;
import com.ggpc.spkpengamatan.Model.SPK_HEADER_SRV;
import com.ggpc.spkpengamatan.Model.SPK_REAL;
import com.ggpc.spkpengamatan.Model.SPK_SRV;
import com.ggpc.spkpengamatan.Model.TK;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DetailSPKActivity2 extends AppCompatActivity {

    TextView tvNoSPK, tvDate, tvDataNull;
    RecyclerView rvListActivity;
    MaterialButton btConfirm;
    //FloatingActionButton fabAddItem;
    SwipeRefreshLayout swipeRefreshLayout;

    public static final String data_spk = "data_spk";
    public static final String data_user = "data_user";
    public static final String data_index = "data_index";
    public static final String data_status = "data_status";
    //private SPK_REAL spk_real;
    private SPK_SRV spk_srv;
    private SPK_HEADER_SRV spk_header_srv;
    //private final ArrayList<SPK_REAL> list = new ArrayList<>();
    private final ArrayList<SPK_SRV> listsrv = new ArrayList<>();
    private final ArrayList<TK> arrayTK = new ArrayList<>();
    private final ArrayList<String> Pengamat = new ArrayList<>();
    String[] tk_nama;
    //String[] tk_kit;
    private final ArrayList<String> tk_kit = new ArrayList<>();
    private TK tk;
    ProgressDialog progressDialog;
    private String dt_status, dt_user, messageBuilder;
    int lastIndex = 0, index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_s_p_k2);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.detail_spk);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        tvNoSPK = findViewById(R.id.tv_no_spk);
        tvDate = findViewById(R.id.tv_date_spk);
        tvDataNull = findViewById(R.id.tv_data_null);
        btConfirm = findViewById(R.id.bt_confirm);
        //fabAddItem = findViewById(R.id.fab_add_item);
        rvListActivity = findViewById(R.id.rv_list_activity);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        //spk_real = getIntent().getParcelableExtra(data_spk);
        spk_header_srv = getIntent().getParcelableExtra(data_spk);
        dt_status = getIntent().getStringExtra(data_status);
        dt_user = getIntent().getStringExtra(data_user);

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String inputDateStr = spk_header_srv.getTanggal_SPK();
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);

        swipeRefreshLayout.setProgressViewEndTarget(false,0);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            onResume();
            new Handler(Looper.getMainLooper()).postDelayed(() ->
                    swipeRefreshLayout.setRefreshing(false), 2000);
        });

        /*fabAddItem.setOnClickListener(v -> {
            addActivity();
        });*/

        tvNoSPK.setText(spk_header_srv.getSPK_ID());
        tvDate.setText(outputDateStr);

    }

    private void addActivity(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.item_add_activity, null);

        Spinner spTypeObservation, spActivity, spObserver;
        EditText etLocation;
        MaterialButton btSave;

        spTypeObservation = view.findViewById(R.id.sp_list_observation);
        spActivity = view.findViewById(R.id.sp_list_activity);
        spObserver = view.findViewById(R.id.sp_list_observer);
        etLocation = view.findViewById(R.id.et_location);
        btSave = view.findViewById(R.id.bt_save);

        final String[] typeObs = new String[1];
        String sip;
        String[] type_observation = getResources().getStringArray(R.array.list_type_observation);
        String[] item_activity_landprep = getResources().getStringArray(R.array.list_activity);
        String[] item_activity_planting = getResources().getStringArray(R.array.list_activity_planting);
        String[] oke = {"Pilih Aktifitas"};
        ArrayList<String> listAct = new ArrayList<>();


        ArrayAdapter<String> adapterTypeObs = new ArrayAdapter<>(this, R.layout.item_spinner_activity, type_observation);
        adapterTypeObs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTypeObservation.setAdapter(adapterTypeObs);
        spTypeObservation.setSelection(0, true);

        spTypeObservation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(DetailSPKActivity2.this, "Dipilih "+type_observation[position], Toast.LENGTH_SHORT).show();
                if (type_observation[position].equals("Land Preparation")){
                    typeObs[0] = "landprep";
                    listAct.add("Chopper");
                    listAct.add("Ridger");
                    listAct.add("Jalan");
                    listAct.add("Bajak");
                    listAct.add("Fin");
                    listAct.add("Subsoil");
                    Toast.makeText(DetailSPKActivity2.this, typeObs[0].toString(), Toast.LENGTH_SHORT).show();
                } else {
                    typeObs[0] = "null";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                typeObs[0] = "null";
            }
        });

        ArrayAdapter<String> adapterActivity = new ArrayAdapter<>(this, R.layout.item_spinner_activity, item_activity_landprep);
        adapterActivity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spActivity.setAdapter(adapterActivity);
        spActivity.setSelection(0, false);

        spActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(DetailSPKActivity2.this, "Dipilih "+item_activity_landprep[position], Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        alertDialog.setView(view);
        alertDialog.show();
    }

    private void confirm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Data");
        builder.setMessage(messageBuilder);
        builder.setPositiveButton("Ya", (dialog, which) -> {
            /*Intent intent = new Intent(this, ListSPKActivity.class);
            intent.putExtra(ListSPKActivity.data_user, "Mandor_Operator");
            intent.putExtra(ListSPKActivity.data_index, SharedPrefManager.getInstance(this).getDataMandor().getMandorIndex());
            intent.putExtra(ListSPKActivity.data_status, "RELEASE");
            startActivity(intent);
            finish();*/
            if (ActivitySRVAdapter.getItemSPK().size()!=0){
                for (int i = 0; i < ActivitySRVAdapter.getItemSPK().size(); i++) {
                    //Toast.makeText(this, "Total : "+ActivitySRVAdapter.getItemSPK().get(i), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this, "Pengamat : "+ActivitySRVAdapter.getNamaTK().get(i), Toast.LENGTH_SHORT).show();
                    sendObserver(ActivitySRVAdapter.getItemSPK().get(i), spk_header_srv.getSPK_ID(),
                            ActivitySRVAdapter.getKitTK().get(i), ActivitySRVAdapter.getNamaTK().get(i));
                    lastIndex = Integer.parseInt(ActivitySRVAdapter.getItemSPK().get(ActivitySRVAdapter.getItemSPK().size()-1));
                }
            } else {
                Toast.makeText(this, "Pengamat Belum Dipilih", Toast.LENGTH_SHORT).show();
            }

        });
        builder.setNegativeButton("Tidak", (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void sendObserver(String item, String spk_id, String tk_kit, String tk_name) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SEND_OBSERVER_SRV,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("message").equalsIgnoreCase(String.valueOf(true))) {
                            Toast.makeText(this, "Data Berhasil Dikirim", Toast.LENGTH_SHORT).show();
                            sendNotifTK(tk_kit, tk_name);
                        }
                        //getSPKActivity();
                        //!Pengamat.contains("NULL")
                        if (listsrv.size()==ActivitySRVAdapter.getItemSPK().size()){
                            Intent intent = new Intent(this, ListSPKActivity.class);
                            intent.putExtra(ListSPKActivity.data_user, "Mandor_Operator");
                            intent.putExtra(ListSPKActivity.data_index, SharedPrefManager.getInstance(this).getDataMandor().getMandorIndex());
                            intent.putExtra(ListSPKActivity.data_status, "RELEASE");
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
                params.put("Item_No", item);
                params.put("SPK_ID", spk_id);
                params.put("Kasie", SharedPrefManager.getInstance(DetailSPKActivity2.this).getDataMandor().getKasieIndex());
                params.put("Pengamat", tk_kit);
                params.put("Nama_Pengamat", tk_name);
                params.put("SPK_Status", "NEW");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void sendNotifTK(String kit_tk, String name_tk) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SEND_NOTIF_TK,
                response -> {
                    //Toast.makeText(this, "Notifikasi Terkirim", Toast.LENGTH_SHORT).show();
                    //finish();
                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", "SPK Hari ini");
                params.put("message", name_tk);
                params.put("index", kit_tk);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getTK() {
        arrayTK.clear();
        tk_kit.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_LIST_TK_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            arrayTK.add(0, new TK("0", "Pilih Pengamat", "0"));
                            tk_kit.add("0");
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
                params.put("index", SharedPrefManager.getInstance(DetailSPKActivity2.this).getDataMandor().getMandorIndex());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getSPKActivity() {

        //fabAddItem.setVisibility(View.VISIBLE);
        listsrv.clear();
        Pengamat.clear();
        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_SPK_ACTIVITY_SRV,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                spk_srv = new SPK_SRV(
                                        data.getString("SPK_ID"),
                                        data.getString("Tanggal_SPK"),
                                        data.getString("Tanggal_Realisasi"),
                                        data.getString("Item_No"),
                                        data.getString("Kode_Aktifitas"),
                                        data.getString("Deskripsi"),
                                        data.getString("Lokasi"),
                                        data.getString("Kebun"),
                                        data.getString("Wilayah"),
                                        data.getString("Pengamat"),
                                        data.getString("Nama_Pengamat"),
                                        data.getString("Mandor_Operator"),
                                        data.getString("Kasie"),
                                        data.getString("Kasie_Bagian"),
                                        data.getString("SPK_Status")
                                );
                                listsrv.add(spk_srv);
                                Pengamat.add(spk_srv.getPengamat());
                            }
                            btConfirm.setVisibility(View.VISIBLE);
                            btConfirm.setOnClickListener(v -> {
                                confirm();
                            });
                            tvDataNull.setVisibility(View.GONE);
                            rvListActivity.setVisibility(View.VISIBLE);
                            rvListActivity.setLayoutManager(new LinearLayoutManager(DetailSPKActivity2.this));
                            ActivitySRVAdapter adapter = new ActivitySRVAdapter(DetailSPKActivity2.this, listsrv, tk_nama, tk_kit);
                            rvListActivity.setNestedScrollingEnabled(false);
                            rvListActivity.setHasFixedSize(false);
                            rvListActivity.setAdapter(adapter);
                            adapter.onItemClickCallback(data -> {
                                /*if (data.getPengamat().equals("0")) {
                                    Intent intent = new Intent(this, SelectTKActivity.class);
                                    intent.putExtra(SelectTKActivity.data_spk, data);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(this, "Pengamat Sudah Dipilih", Toast.LENGTH_SHORT).show();
                                }*/
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
                params.put("SPK_ID", spk_header_srv.getSPK_ID());
                //params.put("SPK_Status", dt_status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailSPKActivity2.this);
        requestQueue.add(stringRequest);
    }

    private void getSPKActivityTK() {

        listsrv.clear();
        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_SPK_ACTIVITY_TK_SRV,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                spk_srv = new SPK_SRV(
                                        data.getString("SPK_ID"),
                                        data.getString("Tanggal_SPK"),
                                        data.getString("Tanggal_Realisasi"),
                                        data.getString("Item_No"),
                                        data.getString("Kode_Aktifitas"),
                                        data.getString("Deskripsi"),
                                        data.getString("Lokasi"),
                                        data.getString("Kebun"),
                                        data.getString("Wilayah"),
                                        data.getString("Pengamat"),
                                        data.getString("Nama_Pengamat"),
                                        data.getString("Mandor_Operator"),
                                        data.getString("Kasie"),
                                        data.getString("Kasie_Bagian"),
                                        data.getString("SPK_Status")
                                );
                                listsrv.add(spk_srv);
                            }

                            btConfirm.setVisibility(View.GONE);
                            tvDataNull.setVisibility(View.GONE);
                            rvListActivity.setVisibility(View.VISIBLE);
                            rvListActivity.setLayoutManager(new LinearLayoutManager(DetailSPKActivity2.this));
                            ActivitySRVAdapter2 adapter = new ActivitySRVAdapter2(DetailSPKActivity2.this, listsrv);
                            rvListActivity.setAdapter(adapter);
                            adapter.onItemClickCallback(data -> {
                                switch (data.getSPK_Status()) {
                                    case "NEW":
                                        //updateStatusChopper(data);
                                        if (data.getKode_Aktivitas().equals("1113511311")) {
                                            updateStatusChopper(data);
                                        } else if(data.getKode_Aktivitas().equals("1113512111")){
                                            updateStatusChopper(data);
                                        } else {
                                            Toast.makeText(this, "Fitur Pengamatan Masih Dalam Pengembangan", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    case "ON_PROCESS":
                                    case "REV_PENGAMAT":
                                        if (data.getKode_Aktivitas().equals("1113511311")) {
                                            Intent intent = new Intent(this, InputChopperActivity.class);
                                            intent.putExtra(InputChopperActivity.data_spk_real, data);
                                            startActivity(intent);
                                        } else if (data.getKode_Aktivitas().equals("1113512111")){
                                            Intent intent = new Intent(this, InputRidgerSingleActivity.class);
                                            intent.putExtra(InputRidgerSingleActivity.data_spk_real, data);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(this, "Fitur Pengamatan Masih Dalam Pengembangan", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    case "ON_KASIE":
                                    case "ON_MANDOR":
                                        if (data.getKode_Aktivitas().equals("1113511311")) {
                                            Intent intent = new Intent(this, ResultChopperActivity.class);
                                            intent.putExtra(ResultChopperActivity.data_spk, data);
                                            intent.putExtra(ResultChopperActivity.data_user, "Pengamat");
                                            startActivity(intent);
                                        } else if (data.getKode_Aktivitas().equals("1113512111")) {
                                            Intent intent = new Intent(this, ResultRidgerSRActivity.class);
                                            intent.putExtra(ResultRidgerSRActivity.data_spk, data);
                                            intent.putExtra(ResultRidgerSRActivity.data_user, "Pengamat");
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
                params.put("index", SharedPrefManager.getInstance(DetailSPKActivity2.this).getDataTK().getKit());
                params.put("SPK_ID", spk_header_srv.getSPK_ID());
                params.put("SPK_Status", dt_status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailSPKActivity2.this);
        requestQueue.add(stringRequest);
    }

    private void getSPKActivityMandor() {
        btConfirm.setVisibility(View.GONE);
        listsrv.clear();
        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_SPK_ACTIVITY_TK_SRV,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                spk_srv = new SPK_SRV(
                                        data.getString("SPK_ID"),
                                        data.getString("Tanggal_SPK"),
                                        data.getString("Tanggal_Realisasi"),
                                        data.getString("Item_No"),
                                        data.getString("Kode_Aktifitas"),
                                        data.getString("Deskripsi"),
                                        data.getString("Lokasi"),
                                        data.getString("Kebun"),
                                        data.getString("Wilayah"),
                                        data.getString("Pengamat"),
                                        data.getString("Nama_Pengamat"),
                                        data.getString("Mandor_Operator"),
                                        data.getString("Kasie"),
                                        data.getString("Kasie_Bagian"),
                                        data.getString("SPK_Status")
                                );
                                listsrv.add(spk_srv);
                            }


                            tvDataNull.setVisibility(View.GONE);
                            rvListActivity.setVisibility(View.VISIBLE);
                            rvListActivity.setLayoutManager(new LinearLayoutManager(DetailSPKActivity2.this));
                            ActivitySRVAdapter2 adapter = new ActivitySRVAdapter2(DetailSPKActivity2.this, listsrv);
                            rvListActivity.setAdapter(adapter);
                            adapter.onItemClickCallback(data -> {
                                switch (data.getSPK_Status()) {
                                    case "ON_PROCESS":
                                    case "NEW":
                                        Toast.makeText(this, "Sedang Dilakukan Pengamatan", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "ON_KASIE":
                                        if (data.getKode_Aktivitas().equals("1113511311")) {
                                            Intent intent = new Intent(this, ResultChopperActivity.class);
                                            intent.putExtra(ResultChopperActivity.data_spk, data);
                                            intent.putExtra(ResultChopperActivity.data_user, "Pengamat");
                                            startActivity(intent);
                                        } else if (data.getKode_Aktivitas().equals("1113512111")) {
                                            Intent intent = new Intent(this, ResultRidgerSRActivity.class);
                                            intent.putExtra(ResultRidgerSRActivity.data_spk, data);
                                            intent.putExtra(ResultRidgerSRActivity.data_user, "Pengamat");
                                            startActivity(intent);
                                        }
                                        break;
                                    case "ON_MANDOR":
                                    case "REV_MANDOR":
                                        if (data.getKode_Aktivitas().equals("1113511311")) {
                                            Intent intent = new Intent(this, ResultChopperActivity.class);
                                            intent.putExtra(ResultChopperActivity.data_spk, data);
                                            intent.putExtra(ResultChopperActivity.data_user, "Mandor_Operator");
                                            startActivity(intent);
                                        } else if (data.getKode_Aktivitas().equals("1113512111")) {
                                            Intent intent = new Intent(this, ResultRidgerSRActivity.class);
                                            intent.putExtra(ResultRidgerSRActivity.data_spk, data);
                                            intent.putExtra(ResultRidgerSRActivity.data_user, "Mandor_Operator");
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
                params.put("index", SharedPrefManager.getInstance(DetailSPKActivity2.this).getDataMandor().getMandorIndex());
                params.put("SPK_ID", spk_header_srv.getSPK_ID());
                params.put("SPK_Status", dt_status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailSPKActivity2.this);
        requestQueue.add(stringRequest);
    }

    private void getSPKActivityKasie() {

        listsrv.clear();
        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_SPK_ACTIVITY_TK_SRV,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                spk_srv = new SPK_SRV(
                                        data.getString("SPK_ID"),
                                        data.getString("Tanggal_SPK"),
                                        data.getString("Tanggal_Realisasi"),
                                        data.getString("Item_No"),
                                        data.getString("Kode_Aktifitas"),
                                        data.getString("Deskripsi"),
                                        data.getString("Lokasi"),
                                        data.getString("Kebun"),
                                        data.getString("Wilayah"),
                                        data.getString("Pengamat"),
                                        data.getString("Nama_Pengamat"),
                                        data.getString("Mandor_Operator"),
                                        data.getString("Kasie"),
                                        data.getString("Kasie_Bagian"),
                                        data.getString("SPK_Status")
                                );
                                listsrv.add(spk_srv);
                            }

                            btConfirm.setVisibility(View.GONE);
                            tvDataNull.setVisibility(View.GONE);
                            rvListActivity.setVisibility(View.VISIBLE);
                            rvListActivity.setLayoutManager(new LinearLayoutManager(DetailSPKActivity2.this));
                            ActivitySRVAdapter2 adapter = new ActivitySRVAdapter2(DetailSPKActivity2.this, listsrv);
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
                                        } else if (data.getKode_Aktivitas().equals("1113512111")) {
                                            Intent intent = new Intent(this, ResultRidgerSRActivity.class);
                                            intent.putExtra(ResultRidgerSRActivity.data_spk, data);
                                            intent.putExtra(ResultRidgerSRActivity.data_user, "Kasie");
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
                params.put("index", SharedPrefManager.getInstance(DetailSPKActivity2.this).getDataKasie().getKasieIndex());
                params.put("SPK_ID", spk_header_srv.getSPK_ID());
                params.put("SPK_Status", dt_status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailSPKActivity2.this);
        requestQueue.add(stringRequest);
    }

    private void updateStatusChopper(SPK_SRV dt_spk_real) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.UPDATE_STATUS_SPK_SRV,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("message").equalsIgnoreCase(String.valueOf(true))) {
                            //Intent Chopper
                            if (dt_spk_real.getKode_Aktivitas().equals("1113511311")){
                                Toast.makeText(this, "Data Berhasil Dikirim", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(this, InputChopperActivity.class);
                                intent.putExtra(InputChopperActivity.data_spk_real, dt_spk_real);
                                startActivity(intent);
                            } else if (dt_spk_real.getKode_Aktivitas().equals("1113512111")){
                                Toast.makeText(this, "Data Berhasil Dikirim", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(this, InputRidgerSingleActivity.class);
                                intent.putExtra(InputRidgerSingleActivity.data_spk_real, dt_spk_real);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "Fitur Pengamatan Masih Dalam Pengembangan", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(this, "Error/Koneksi Bermasalah", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },

                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Item_No", dt_spk_real.getItem());
                params.put("SPK_ID", dt_spk_real.getSPK_ID());
                params.put("Tanggal_Realisasi", "NULL");
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

        if (dt_user.equals("Pengamat")) {
            getSPKActivityTK();
        } else if (dt_user.equals("Mandor_Operator") && !dt_status.equals("RELEASE")) {
            getSPKActivityMandor();
        } else if (dt_user.equals("Kasie")) {
            getSPKActivityKasie();
        } else {
            getTK();
            getSPKActivity();
        }
    }
}