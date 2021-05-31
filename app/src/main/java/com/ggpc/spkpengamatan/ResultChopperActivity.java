package com.ggpc.spkpengamatan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ggpc.spkpengamatan.Adapter.ChopperAdapter;
import com.ggpc.spkpengamatan.Adapter.ChopperAdapter2;
import com.ggpc.spkpengamatan.Model.Chopper;
import com.ggpc.spkpengamatan.Model.Notes;
import com.ggpc.spkpengamatan.Model.SPK_REAL;
import com.ggpc.spkpengamatan.Model.SPK_SRV;
import com.ggpc.spkpengamatan.Model.TK;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResultChopperActivity extends AppCompatActivity {

    TextView tvTotalBt, tvTotalTh, tvTotalAr, tvDataNull, tvNotes, tvTotalSample, tvTitleNotes, tvTotalAchievement;
    TextView tvNoSPK, tvDate, tvItem, tvLocation, tvStatus;
    EditText etNotes;
    RecyclerView rvDataChopper;
    MaterialButton btDisAgree, btAgree;
    MaterialCardView cvGetNotes, cvSetNotes;
    ProgressDialog progressDialog;

    private final ArrayList<Chopper> list = new ArrayList<>();
    String no_spk, status, get_index, catatan, set_index, URL;
    double totalBT, totalTH, totalAR, totalAchievement;
    int totalSample = 0;

    public static final String data_spk = "data_spk";
    public static final String data_user = "data_user";
    //private SPK_REAL spk_real;
    private SPK_SRV spk_srv;
    private Notes notes;
    private Chopper chopper;
    public String dt_user, dataNotes, messageBuilder;
    RequestOptions requestOptions = new RequestOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_chopper);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.result_chopper);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        tvLocation = findViewById(R.id.tv_location);
        tvDataNull = findViewById(R.id.tv_data_null);
        tvTotalBt = findViewById(R.id.tv_total_bt);
        tvTotalTh = findViewById(R.id.tv_total_th);
        tvTotalAr = findViewById(R.id.tv_total_ar);
        tvNotes = findViewById(R.id.tv_notes);
        tvTotalSample = findViewById(R.id.tv_total_sample);
        tvTitleNotes = findViewById(R.id.tv_title_note);
        tvNoSPK = findViewById(R.id.tv_no_spk);
        tvDate = findViewById(R.id.tv_date);
        tvItem = findViewById(R.id.tv_item);
        tvStatus = findViewById(R.id.tv_status);
        tvTotalAchievement = findViewById(R.id.tv_total_achievement);
        cvGetNotes = findViewById(R.id.cv_get_note);
        cvSetNotes = findViewById(R.id.cv_notes);
        etNotes = findViewById(R.id.et_notes);
        rvDataChopper = findViewById(R.id.rv_list_data);
        btDisAgree = findViewById(R.id.bt_disagree);
        btAgree = findViewById(R.id.bt_agree);

        //spk_real = getIntent().getParcelableExtra(data_spk);
        spk_srv = getIntent().getParcelableExtra(data_spk);
        dt_user = getIntent().getStringExtra(data_user);

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String inputDateStr = spk_srv.getTanggal_SPK();
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);

        assert spk_srv != null;
        tvNoSPK.setText(spk_srv.getSPK_ID());
        tvLocation.setText(spk_srv.getLokasi());
        tvDate.setText(outputDateStr);
        tvItem.setText(spk_srv.getItem());
        tvStatus.setText(spk_srv.getSPK_Status());

        switch (dt_user) {
            case "Kasie":
                set_index = spk_srv.getKasie();
                break;
            case "Mandor_Operator":
                set_index = spk_srv.getMandor_Operator();
                break;
            case "Pengamat":
                //set_index = spk_real.getPengamat();
                btAgree.setVisibility(View.GONE);
                btDisAgree.setVisibility(View.GONE);
                cvSetNotes.setVisibility(View.GONE);
                set_index = spk_srv.getMandor_Operator();
                break;
            default:
                set_index = null;
                break;
        }

        //getProfileTK();
        getDataChopper();
        getNotes();

        btDisAgree.setOnClickListener(v -> {
            catatan = etNotes.getText().toString();
            if (dt_user.equals("Kasie")){
                status = "REV_MANDOR";
                messageBuilder = getString(R.string.message_disagree_kasie);
                get_index = spk_srv.getMandor_Operator();
                URL = EndPoints.SEND_NOTIF_MANDOR;
            } else if (dt_user.equals("Mandor_Operator")) {
                messageBuilder = getString(R.string.message_disagree_mandor);
                status = "REV_PENGAMAT";
                get_index = spk_srv.getPengamat();
                URL = EndPoints.SEND_NOTIF_TK;
            }
            confirmDisAgree();

        });

        btAgree.setOnClickListener(v -> {
            catatan = etNotes.getText().toString();
            if (dt_user.equals("Kasie")){
                status = "CLOSE";
                messageBuilder = getString(R.string.message_agree_kasie);
                get_index = "CLOSE";
            } else if (dt_user.equals("Mandor_Operator")) {
                messageBuilder = getString(R.string.message_agree_mandor);
                status = "ON_KASIE";
                get_index = spk_srv.getKasie();
                URL = EndPoints.SEND_NOTIF_KASIE;
            }
            confirmAgree();
        });

    }

    private void confirmDisAgree(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ResultChopperActivity.this);
        builder.setTitle("Konfirmasi Data");
        builder.setMessage(messageBuilder);
        builder.setPositiveButton("Ya", (dialog, which) -> {
                if (!(catatan.matches(""))){
                    sendNotes();
                    updateStatus();
                    sendNotifDisAgree();
                } else {
                    updateStatus();
                    sendNotifDisAgree();
                }

        });
        builder.setNegativeButton("Tidak", (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void confirmAgree(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ResultChopperActivity.this);
        builder.setTitle("Konfirmasi Data");
        builder.setMessage(messageBuilder);
        builder.setPositiveButton("Ya", (dialog, which) -> {
            if (!(catatan.matches(""))){
                sendNotes();
                if (spk_srv.getSPK_Status().equals("ON_KASIE")){
                    sendDataSAP();
                }
                updateStatus();
                sendNotifAgree();
            } else {
                if (spk_srv.getSPK_Status().equals("ON_KASIE")){
                    sendDataSAP();
                }
                updateStatus();
                sendNotifAgree();
            }
        });
        builder.setNegativeButton("Tidak", (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void sendDataSAP(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.UPLOAD_CHOPPER_SRV,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("response : ", response);
                        if (obj.getString("data").equalsIgnoreCase("succes")){
                            Toast.makeText(this, "Data Berhasil Terkirim", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                DecimalFormat dcf = new DecimalFormat("##");
                params.put("SPKID_KodeSPK", spk_srv.getSPK_ID());
                params.put("ITEMNO_ItemSPK", spk_srv.getItem());
                params.put("AKCODE_KodeAktifitas", spk_srv.getKode_Aktivitas());
                params.put("PGMTDATE_TanggalPengamatan", spk_srv.getTanggal_SPK());
                params.put("TKPGMT_KodeIndukTenagaKerja", spk_srv.getPengamat());
                params.put("TKMD_KodeIndukTenagaKerja", spk_srv.getMandor_Operator());
                params.put("LOKASI_KodeLokasi", spk_srv.getLokasi());
                params.put("TOTSAMPLE_TotalSample", String.valueOf(totalSample));
                params.put("BGLCACAH_BonggolTercacah", String.valueOf(totalBT));
                params.put("TNMHANCUR_TanamanHancur", String.valueOf(totalTH));
                params.put("APLRAPAT_AplikasiRapat", String.valueOf(totalAR));
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @SuppressLint("SetTextI18n")
    private void getDataChopper(){
        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_CHOPPER_URL,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length()>0){
                            for (int i = 0; i<jsonArray.length(); i++){
                                JSONObject data =jsonArray.getJSONObject(i);

                                chopper = new Chopper(
                                        data.getInt("ID"),
                                        data.getString("SPK_ID"),
                                        data.getString("Item_No"),
                                        data.getString("Tanggal_SPK"),
                                        data.getString("KIT"),
                                        data.getString("Pengamat"),
                                        data.getString("Lokasi"),
                                        data.getString("Plot"),
                                        data.getString("BT"),
                                        data.getString("TH"),
                                        data.getString("AR")
                                );
                                list.add(chopper);
                                totalBT += Float.parseFloat(chopper.getBt())/10;
                                totalTH += Float.parseFloat(chopper.getTh())/10;
                                totalAR += Float.parseFloat(chopper.getAr())/10;
                                totalSample = list.size()*10;

                            }
                            totalAchievement = (totalTH/totalSample*40) + (totalBT/totalSample*40) + (totalAR/totalSample*20);
                            DecimalFormat df = new DecimalFormat("##.##");
                            tvTotalBt.setText(String.valueOf(df.format(totalBT)));
                            tvTotalTh.setText(String.valueOf(df.format(totalTH)));
                            tvTotalAr.setText(String.valueOf(df.format(totalAR)));
                            tvTotalSample.setText(String.valueOf(totalSample));
                            if (totalAchievement<85){
                                tvTotalAchievement.setTextColor(ContextCompat.getColor(this, R.color.red));
                            }
                            tvTotalAchievement.setText(df.format(totalAchievement)+" %");

                            tvDataNull.setVisibility(View.GONE);
                            rvDataChopper.setVisibility(View.VISIBLE);
                            rvDataChopper.setLayoutManager(new LinearLayoutManager(ResultChopperActivity.this));
                            ChopperAdapter adapter = new ChopperAdapter(ResultChopperActivity.this, list);
                            rvDataChopper.setAdapter(adapter);
                            adapter.onItemClickCallback(data -> {
                                viewImage(String.valueOf(data.getId()));
                            });

                        } else {
                            tvDataNull.setVisibility(View.VISIBLE);
                            rvDataChopper.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("KIT", spk_srv.getPengamat());
                params.put("SPK_ID", spk_srv.getSPK_ID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ResultChopperActivity.this);
        requestQueue.add(stringRequest);
    }

    private void viewImage(String id){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.item_view_image, null);

        ImageView ivObservation = dialogView.findViewById(R.id.iv_observation);

        String url = "https://ggp-pis.com/spk_pengamatan/images/";

        Glide.with(this)
                .load(url + id+".jpg")
                .apply(requestOptions)
                .into(ivObservation);

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void updateStatus() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.UPDATE_STATUS_SPK_SRV,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("message").equalsIgnoreCase(String.valueOf(true))) {
                            //Toast.makeText(this, "Data Berhasil Dikirim", Toast.LENGTH_SHORT).show();
                            /*if (dt_user.equals("Kasie") && spk_real.getSPK_Status().equals("ON_KASIE")){
                                sendDataSAP();
                            }*/
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
                params.put("Item_No", spk_srv.getItem());
                params.put("SPK_ID", spk_srv.getSPK_ID());
                Calendar calendar = Calendar.getInstance();
                String date = calendar.get(Calendar.YEAR) +"-"+ (calendar.get(Calendar.MONTH)+1) +"-"+ calendar.get(Calendar.DAY_OF_MONTH);
                params.put("Tanggal_Realisasi", date);
                params.put("SPK_Status", status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void sendNotes(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SET_NOTES_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))) {
                            Toast.makeText(this, "Catatan Berhasil Dikirim", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Catatan Gagal Dikirim", Toast.LENGTH_SHORT).show();
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
                params.put("id", spk_srv.getItem());
                params.put("indeks", get_index);
                params.put("no_spk", spk_srv.getSPK_ID());
                params.put("catatan", catatan);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ResultChopperActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getNotes(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_NOTES_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        StringBuilder set_catatan = new StringBuilder();
                        if (jsonArray.length()>0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                notes = new Notes(
                                        data.getString("id"),
                                        data.getString("indeks"),
                                        data.getString("no_spk"),
                                        data.getString("catatan")
                                );
                                set_catatan.append(notes.getCatatan());
                                set_catatan.append("\n");
                            }

                            tvNotes.setText(notes.getCatatan());

                            /*if (dataNotes.equals("kasie_disagree")){
                                tvTitleNotes.setText(R.string.notes_kasie);
                                tvNotes.setText(notes.getCatatan());
                            } else if (dataNotes.equals("mandor")){
                                tvTitleNotes.setText(R.string.notes_mandor);
                                tvNotes.setText(set_catatan.toString());
                            } else {
                                tvTitleNotes.setText(R.string.notes_tk);
                                tvNotes.setText(set_catatan.toString());
                            }*/

                        } else {
                            tvNotes.setText(R.string.data_null);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("id", spk_srv.getItem());
                params.put("indeks", set_index);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ResultChopperActivity.this);
        requestQueue.add(stringRequest);
    }

    private void sendNotifAgree(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    Toast.makeText(ResultChopperActivity.this, "Notifikasi Terkirim", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", "Laporan Pengamatan");
                params.put("message", spk_srv.getDeskripsi());
                params.put("index", get_index);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(this));
        requestQueue.add(stringRequest);
    }

    private void sendNotifDisAgree(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    Toast.makeText(ResultChopperActivity.this, "Notifikasi Terkirim", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", "Revisi Pengamatan");
                params.put("message", spk_srv.getDeskripsi());
                params.put("index", get_index);
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