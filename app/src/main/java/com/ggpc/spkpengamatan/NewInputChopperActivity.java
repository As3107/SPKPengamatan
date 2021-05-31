package com.ggpc.spkpengamatan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ggpc.spkpengamatan.Adapter.ChopperAdapter;
import com.ggpc.spkpengamatan.Adapter.ChopperAdapter2;
import com.ggpc.spkpengamatan.Model.Chopper;
import com.ggpc.spkpengamatan.Model.Notes;
import com.ggpc.spkpengamatan.Model.SPK;
import com.ggpc.spkpengamatan.Model.SPK_REAL;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

public class NewInputChopperActivity extends AppCompatActivity {

    EditText etPlot;
    TextView tvSample, tvBt, tvTh, tvAr, tvSetVibration;
    TextView tvNoSpk, tvDate, tvItem, tvLocation, tvStatus;
    MaterialCardView cvAddSample, cvSubtractSample, cvAddBt, cvSubtractBt, cvAddTh, cvSubtractTh, cvAddAr, cvSubtractAr;
    EditText etNotes;
    RoundedImageView ivUpload2;
    MaterialButton btSave, btSend;
    MaterialCardView cvGetNotes;
    TextView tvTotalBt, tvTotalTh, tvTotalAr, tvTotalMean, tvDataNull, tvPengamat, tvNotes, tvTotalSample;
    RecyclerView rvDataChopper;
    private Bitmap image2;

    private final List<Chopper> list = new ArrayList<>();
    ProgressDialog progressDialog;
    Long tsLong;
    String kit, ts, plot, bt, th, ar, pengamat, date, no_spk, id, mandor, dataNotes, totalSample;
    String  updatePlot, updateBt, updateTh, updateAr, catatan, setVibration;
    int id_chopper;
    int totalBT;
    int totalTH;
    int totalAR;
    int totalMean;
    String btReal, thReal, arReal;
    int countSample = 0, countBt = 0, countTh = 0, countAr = 0;
    int longVibrateAdd = 0, longVibrateSubtract = 0;

    public static final String DATA_SPK = "data_spk";
    public static final String data_spk_real = "data_spk_real";
    public static final String DATA_NOTES = "data_notes";

    private SPK_REAL spk_real;
    private SPK spk;
    private Notes notes;
    private SharedPreferences sharedPreferences;
    private Chopper chopper;

    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    public static final String chopper_sample = "chopper_sample";
    public static final String chopper_bt = "chopper_bt";
    public static final String chopper_th = "chopper_th";
    public static final String chopper_ar = "chopper_ar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_input_chopper);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.chopper);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        ivUpload2 = findViewById(R.id.iv_upload2);
        btSave = findViewById(R.id.bt_save);
        btSend = findViewById(R.id.bt_send);
        etNotes = findViewById(R.id.et_notes);
        tvSetVibration = findViewById(R.id.tv_vibration);
        tvLocation = findViewById(R.id.tv_location);
        tvDataNull = findViewById(R.id.tv_data_null);
        tvTotalBt = findViewById(R.id.tv_total_bt);
        tvTotalTh = findViewById(R.id.tv_total_th);
        tvTotalAr = findViewById(R.id.tv_total_ar);
        tvSample = findViewById(R.id.tv_count_sample);
        tvBt = findViewById(R.id.tv_count_bt);
        tvTh = findViewById(R.id.tv_count_th);
        tvAr = findViewById(R.id.tv_count_ar);
        tvNoSpk = findViewById(R.id.tv_no_spk);
        tvDate = findViewById(R.id.tv_date);
        tvItem = findViewById(R.id.tv_item);
        tvStatus = findViewById(R.id.tv_status);
        cvAddSample = findViewById(R.id.cv_add_sample);
        cvSubtractSample = findViewById(R.id.cv_subtract_sample);
        cvAddBt = findViewById(R.id.cv_add_bt);
        cvSubtractBt = findViewById(R.id.cv_subtract_bt);
        cvAddTh = findViewById(R.id.cv_add_th);
        cvSubtractTh = findViewById(R.id.cv_subtract_th);
        cvAddAr = findViewById(R.id.cv_add_ar);
        cvSubtractAr = findViewById(R.id.cv_subtract_ar);
        tvTotalMean = findViewById(R.id.tv_total_mean);
        tvNotes = findViewById(R.id.tv_notes);
        tvTotalSample = findViewById(R.id.tv_total_data);
        etPlot = findViewById(R.id.et_plot);
        cvGetNotes = findViewById(R.id.cv_get_note);
        rvDataChopper = findViewById(R.id.rv_list_data);

        setVibration = "on";

        tvSetVibration.setText("Mode Getaran : "+setVibration);
        tvSetVibration.setOnClickListener(v -> {
            if (setVibration.equals("on")){
                setVibration = "off";
                longVibrateAdd = 0;
                longVibrateSubtract = 0;
            } else {
                setVibration = "on";
                longVibrateAdd = 30;
                longVibrateSubtract = 100;
            }
            tvSetVibration.setText("Mode Getaran : "+setVibration);
        });
        if (setVibration.equals("on")){
            longVibrateAdd = 30;
            longVibrateSubtract = 100;
        } else {
            longVibrateAdd = 0;
            longVibrateSubtract = 0;
        }

        tsLong = System.currentTimeMillis()/1000;
        ts = tsLong.toString();
        kit = SharedPrefManager.getInstance(NewInputChopperActivity.this).getUserLogin().getkit();
        spk_real = getIntent().getParcelableExtra(data_spk_real);
        pengamat = SharedPrefManager.getInstance(this).getDataTK().getNama();
        dataNotes = getIntent().getStringExtra(DATA_NOTES);

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

        tvNoSpk.setText(spk_real.getSPK_ID());
        tvDate.setText(outputDateStr);
        tvItem.setText(spk_real.getItem());
        tvStatus.setText(spk_real.getSPK_Status());
        tvLocation.setText(spk_real.getLokasi());

        if (dataNotes!=null && dataNotes.equals("true")){
            cvGetNotes.setVisibility(View.VISIBLE);
        } else {
            cvGetNotes.setVisibility(View.GONE);
        }

        btSave.setOnClickListener(v -> validateInput());

        ivUpload2.setOnClickListener(v -> {
            selectImage(2);

        });

        getDataChopper();
        getNotes();

        btSend.setOnClickListener(v -> {
            catatan = etNotes.getText().toString();
            if (totalBT!=0){
                confirmSend();
            } else {
                Toast.makeText(this, "Data Tidak Ada", Toast.LENGTH_SHORT).show();
            }
        });

        final Vibrator vibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        tvSample.setText(String.valueOf(sharedPreferences.getInt(chopper_sample, 0)));
        tvBt.setText(String.valueOf(sharedPreferences.getInt(chopper_bt, 0)));
        tvTh.setText(String.valueOf(sharedPreferences.getInt(chopper_th, 0)));
        tvAr.setText(String.valueOf(sharedPreferences.getInt(chopper_ar, 0)));

        cvAddSample.setOnClickListener(v -> {
            countSample = sharedPreferences.getInt(chopper_sample, 0)+1;
            vibe.vibrate(longVibrateAdd);
            editor.putInt(chopper_sample, countSample);
            editor.apply();
            tvSample.setText(String.valueOf(sharedPreferences.getInt(chopper_sample, 0)));
        });
        cvSubtractSample.setOnClickListener(v -> {
            if (sharedPreferences.getInt(chopper_sample, 0)==0){
                Toast.makeText(this, "Plot Masih Kosong", Toast.LENGTH_SHORT).show();
            } else {
                countSample = sharedPreferences.getInt(chopper_sample, 0)-1;
                vibe.vibrate(longVibrateSubtract);
                editor.putInt(chopper_sample, countSample);
                editor.apply();
            }
            tvSample.setText(String.valueOf(sharedPreferences.getInt(chopper_sample, 0)));
        });

        cvAddBt.setOnClickListener(v -> {
            countBt = sharedPreferences.getInt(chopper_bt, 0)+1;
            vibe.vibrate(longVibrateAdd);
            editor.putInt(chopper_bt, countBt);
            editor.apply();
            tvBt.setText(String.valueOf(sharedPreferences.getInt(chopper_bt, 0)));
        });
        cvSubtractBt.setOnClickListener(v -> {
            if (sharedPreferences.getInt(chopper_bt, 0)==0){
                Toast.makeText(this, "Bonggol Tercacah Masih Kosong", Toast.LENGTH_SHORT).show();
            } else {
                countBt = sharedPreferences.getInt(chopper_bt, 0)-1;
                vibe.vibrate(longVibrateSubtract);
                editor.putInt(chopper_bt, countBt);
                editor.apply();
            }
            tvBt.setText(String.valueOf(sharedPreferences.getInt(chopper_bt, 0)));
        });

        cvAddTh.setOnClickListener(v -> {
            countTh = sharedPreferences.getInt(chopper_th, 0)+1;
            vibe.vibrate(longVibrateAdd);
            editor.putInt(chopper_th, countTh);
            editor.apply();
            tvTh.setText(String.valueOf(sharedPreferences.getInt(chopper_th, 0)));
        });
        cvSubtractTh.setOnClickListener(v -> {
            if (sharedPreferences.getInt(chopper_th, 0)==0){
                Toast.makeText(this, "Tanaman Hancur Masih Kosong", Toast.LENGTH_SHORT).show();
            } else {
                countTh = sharedPreferences.getInt(chopper_th, 0)-1;
                vibe.vibrate(longVibrateSubtract);
                editor.putInt(chopper_th, countTh);
                editor.apply();
            }
            tvTh.setText(String.valueOf(sharedPreferences.getInt(chopper_th, 0)));
        });

        cvAddAr.setOnClickListener(v -> {
            countAr = sharedPreferences.getInt(chopper_ar, 0)+1;
            vibe.vibrate(longVibrateAdd);
            editor.putInt(chopper_ar, countAr);
            editor.apply();
            tvAr.setText(String.valueOf(sharedPreferences.getInt(chopper_ar, 0)));
        });
        cvSubtractAr.setOnClickListener(v -> {
            if (sharedPreferences.getInt(chopper_ar, 0)==0){
                Toast.makeText(this, "Aplikasi Rapatan Masih Kosong", Toast.LENGTH_SHORT).show();
            } else {
                countAr = sharedPreferences.getInt(chopper_ar, 0)-1;
                vibe.vibrate(longVibrateSubtract);
                editor.putInt(chopper_ar, countAr);
                editor.apply();
            }
            tvAr.setText(String.valueOf(sharedPreferences.getInt(chopper_ar, 0)));
        });

    }

    private void clearDataInput(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(chopper_sample);
        editor.remove(chopper_bt);
        editor.remove(chopper_th);
        editor.remove(chopper_ar);
        editor.apply();
        tvSample.setText(String.valueOf(sharedPreferences.getInt(chopper_sample, 0)));
        tvBt.setText(String.valueOf(sharedPreferences.getInt(chopper_bt, 0)));
        tvTh.setText(String.valueOf(sharedPreferences.getInt(chopper_th, 0)));
        tvAr.setText(String.valueOf(sharedPreferences.getInt(chopper_ar, 0)));
    }

    private void confirmSend(){
        AlertDialog.Builder builder = new AlertDialog.Builder(NewInputChopperActivity.this);
        builder.setTitle("Konfirmasi Data");
        builder.setMessage("Apakah Anda yakin semua data pengamatan sudah LENGKAP dan akan mengimkan ke MANDOR ?");
        builder.setPositiveButton("Ya", (dialog, which) -> {
            if (!(catatan.matches(""))){
                sendNotes();
                //sendDataChopper();
                updateStatus();
            } else {
                //sendDataChopper();
                updateStatus();
            }

        });
        builder.setNegativeButton("Tidak", (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void validateInput(){
        /*else if (sharedPreferences.getInt(chopper_bt, 0)>sharedPreferences.getInt(chopper_sample, 0) || sharedPreferences.getInt(chopper_th, 0)>sharedPreferences.getInt(chopper_sample, 0)
                || sharedPreferences.getInt(chopper_ar, 0)>sharedPreferences.getInt(chopper_sample, 0)){
            Toast.makeText(this, "Data Tidak Boleh Lebih dari Total Sampel", Toast.LENGTH_SHORT).show();
        }*/
        if(TextUtils.isEmpty(etPlot.getText().toString())){
            Toast.makeText(this, "Data Plot Kosong", Toast.LENGTH_LONG).show();
        } else if (sharedPreferences.getInt(chopper_sample, 0)==0){
            Toast.makeText(this, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        }else {
            saveDataChopper();
        }
    }

    private void saveDataChopper(){

        plot =etPlot.getText().toString();

        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();

        date = calendar.get(Calendar.YEAR) +"-"+ (calendar.get(Calendar.MONTH)+1) +"-"+ calendar.get(Calendar.DAY_OF_MONTH);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SEND_CHOPPER_URL,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))){
                            Toast.makeText(this, "Berhasil Terkirim", Toast.LENGTH_SHORT).show();
                            clearDataInput();
                            if (image2!=null){
                                sendImages();
                            }
                            finish();
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        } else {
                            Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("no_spk", spk_real.getSPK_ID());
                params.put("item", spk_real.getItem());
                params.put("tanggal", date);
                params.put("kit", spk_real.getPengamat());
                params.put("pengamat", SharedPrefManager.getInstance(NewInputChopperActivity.this).getDataTK().getNama());
                params.put("lokasi", spk_real.getLokasi());
                params.put("plot", plot);
                params.put("bt", String.valueOf(sharedPreferences.getInt(chopper_bt, 0)));
                params.put("th", String.valueOf(sharedPreferences.getInt(chopper_th, 0)));
                params.put("ar", String.valueOf(sharedPreferences.getInt(chopper_ar, 0)));
                params.put("total", String.valueOf(sharedPreferences.getInt(chopper_sample, 0)));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(NewInputChopperActivity.this);
        requestQueue.add(stringRequest);
    }

    private void sendImages(){
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, EndPoints.SEND_IMAGES,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(new String(response.data));

                        if(obj.getString("data").equalsIgnoreCase("succes")){

                            Toast.makeText(NewInputChopperActivity.this, "Gambar Berhasil Diupload", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(NewInputChopperActivity.this, "Gambar Gagal Diupload", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(chopper.getId()+1));
                params.put("no_spk", spk_real.getSPK_ID());
                params.put("plot", String.valueOf(sharedPreferences.getInt(chopper_sample, 0)));
                return params;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("image", new DataPart(chopper.getId()+1+".jpg", getFileDataFromDrawable(image2)));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(volleyMultipartRequest)
                .setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void updateStatus() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.UPDATE_STATUS_SPK,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("message").equalsIgnoreCase(String.valueOf(true))) {
                            Toast.makeText(this, "Data Berhasil Dikirim", Toast.LENGTH_SHORT).show();
                            sendNotifMandor();
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
                params.put("ID", spk_real.getID());
                params.put("SPK_ID", spk_real.getSPK_ID());
                params.put("SPK_Status", "ON_MANDOR");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void updateDatAChopper(){
        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();

        date = calendar.get(Calendar.YEAR) +"-"+ (calendar.get(Calendar.MONTH)+1) +"-"+ calendar.get(Calendar.DAY_OF_MONTH);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.UPDATE_CHOPPER_URL,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))){
                            Toast.makeText(this, "Data Berhasil Diubah", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        } else {
                            Toast.makeText(this, "Data Gagal Diubah", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id_chopper));
                params.put("plot", updatePlot);
                params.put("bt", updateBt);
                params.put("th", updateTh);
                params.put("ar", updateAr);
                params.put("total", totalSample);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(NewInputChopperActivity.this);
        requestQueue.add(stringRequest);
    }

    private void sendNotes(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SET_NOTES_URL,
                response -> {
                    progressDialog.dismiss();
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
                    progressDialog.dismiss();
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", spk_real.getID());
                params.put("indeks", mandor);
                params.put("no_spk", no_spk);
                params.put("catatan", catatan);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(NewInputChopperActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getNotes(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_NOTES_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length()>0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                notes = new Notes(
                                        data.getString("id"),
                                        data.getString("indeks"),
                                        data.getString("no_spk"),
                                        data.getString("catatan")
                                );
                            }
                            tvNotes.setText(notes.getCatatan());
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
                params.put("id", spk_real.getSPK_ID());
                params.put("indeks", kit);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(NewInputChopperActivity.this);
        requestQueue.add(stringRequest);
    }

    private void sendNotifMandor(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SEND_NOTIF_MANDOR,
                response -> {
                    Toast.makeText(NewInputChopperActivity.this, "Notifikasi Terkirim", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", spk_real.getPengamat());
                params.put("message", "Laporan Pengamatan "+spk_real.getDeskripsi());
                params.put("index", spk_real.getMandor_Operator());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(this));
        requestQueue.add(stringRequest);
    }

    @SuppressLint("SetTextI18n")
    private void getDataChopper(){
        list.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_CHOPPER_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length()>0){
                            for (int i = 0; i<jsonArray.length(); i++){
                                JSONObject data =jsonArray.getJSONObject(i);

                                chopper = new Chopper(
                                        data.getInt("id"),
                                        data.getString("no_spk"),
                                        data.getString("tanggal"),
                                        data.getString("kit"),
                                        data.getString("pengamat"),
                                        data.getString("lokasi"),
                                        data.getString("plot"),
                                        data.getString("bt"),
                                        data.getString("th"),
                                        data.getString("ar"),
                                        data.getString("total")
                                );
                                list.add(chopper);
                                totalBT += Integer.parseInt(chopper.getBt());
                                totalTH += Integer.parseInt(chopper.getTh());
                                totalAR += Integer.parseInt(chopper.getAr());
                                //totalMean += Integer.parseInt(chopper.getTotal());
                                //totalSample = String.valueOf(jsonArray.length());
                            }
                            //DecimalFormat df = new DecimalFormat("##.##");
                            //totalMean = (totalBT+totalTH+totalAR);
                            tvTotalBt.setText(String.valueOf(totalBT));
                            tvTotalTh.setText(String.valueOf(totalTH));
                            tvTotalAr.setText(String.valueOf(totalAR));
                            tvTotalMean.setText(String.valueOf(totalMean));
                            //tvTotalSample.setText(totalSample);

                            tvDataNull.setVisibility(View.GONE);
                            rvDataChopper.setVisibility(View.VISIBLE);
                            rvDataChopper.setLayoutManager(new LinearLayoutManager(NewInputChopperActivity.this));
                            ChopperAdapter2 adapter = new ChopperAdapter2(NewInputChopperActivity.this, list);
                            rvDataChopper.setAdapter(adapter);
                            adapter.onItemClickCallback(this::editDataChopper);
                        } else {
                            tvDataNull.setVisibility(View.VISIBLE);
                            rvDataChopper.setVisibility(View.GONE);
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
                params.put("kit", spk_real.getPengamat());
                params.put("item", spk_real.getItem());
                params.put("no_spk", spk_real.getSPK_ID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(NewInputChopperActivity.this);
        requestQueue.add(stringRequest);
    }

    private void editDataChopper(Chopper choppers){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.item_edit_chopper, null);

        EditText etPlots, etBts, etThs, etArs;
        MaterialButton btSaves;

        etPlots = dialogView.findViewById(R.id.et_plot);
        etBts = dialogView.findViewById(R.id.et_bt);
        etThs = dialogView.findViewById(R.id.et_th);
        etArs = dialogView.findViewById(R.id.et_ar);
        btSaves = dialogView.findViewById(R.id.bt_save);

        etPlots.setText(choppers.getPlot(), TextView.BufferType.EDITABLE);
        etBts.setText(choppers.getBt(), TextView.BufferType.EDITABLE);
        etThs.setText(choppers.getTh(), TextView.BufferType.EDITABLE);
        etArs.setText(choppers.getAr(), TextView.BufferType.EDITABLE);

        btSaves.setOnClickListener(v -> {
            id_chopper = choppers.getId();
            updatePlot = etPlots.getText().toString();
            updateBt = etBts.getText().toString();
            updateTh = etThs.getText().toString();
            updateAr = etArs.getText().toString();
            //totalSample = choppers.getTotal();
            updateDatAChopper();
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void selectImage(int intent) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {

            if (intent==2){
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 20);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 200);
                }else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {

                case 20:
                    if (resultCode == RESULT_OK && data!=null){
                        image2 = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                        ivUpload2.setImageBitmap(image2);
                    }
                    break;

                case 200:
                    if ( resultCode == RESULT_OK && data != null) {
                        Uri imageUri = data.getData();
                        try {
                            image2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            ivUpload2.setImageBitmap(image2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}