package com.ggpc.spkpengamatan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ggpc.spkpengamatan.Adapter.ChopperAdapter;
import com.ggpc.spkpengamatan.Adapter.RidgerSingleAdapter;
import com.ggpc.spkpengamatan.Model.Chopper;
import com.ggpc.spkpengamatan.Model.Notes;
import com.ggpc.spkpengamatan.Model.RidgerSingle;
import com.ggpc.spkpengamatan.Model.SPK_SRV;
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
import java.util.Map;
import java.util.Objects;

public class ResultRidgerSRActivity extends AppCompatActivity {

    TextView tvNoSpk, tvDate, tvItem, tvLocation, tvLuas, tvStatus;
    TextView tvTotalSPL, tvTotalSTDR, tvTotalSTD, tvTotalMIN, tvTotalMAX, tvTotalRata, tvTotalPencapaian, tvTotalKualitas,
            tvTotalSPL2, tvTotalSTDR2, tvTotalSTD2, tvTotalMIN2, tvTotalMAX2, tvTotalRata2, tvTotalPencapaian2, tvDataNull, tvNotes;
    RecyclerView rvListRidgerS;
    EditText etNotes;
    MaterialButton btDisAgree, btAgree;
    MaterialCardView cvGetNotes, cvSetNotes;
    ProgressDialog progressDialog;

    private final ArrayList<RidgerSingle> list = new ArrayList<>();
    private final ArrayList<String> jarakPoros = new ArrayList<>();
    private final ArrayList<String> kedalamanKuku = new ArrayList<>();
    String status, get_index, catatan, set_index, URL;
    double totalSamplePoros = 0, totalSampleKuku = 0;
    int countSTDRPoros = 0, countSTDRKuku = 0;

    double minValuePoros = 0, maxValuePoros = 0;
    double minValueKuku = 0, maxValueKuku = 0;
    double meanPoros = 0, meanKuku = 0;
    double pencapaianPoros = 0, pencapaianKuku = 0;

    public static final String data_spk = "data_spk";
    public static final String data_user = "data_user";
    //private SPK_REAL spk_real;
    private SPK_SRV spk_srv;
    private Notes notes;
    private RidgerSingle ridgerSingle;
    public String dt_user, dataNotes, messageBuilder;
    RequestOptions requestOptions = new RequestOptions();
    DecimalFormat df = new DecimalFormat("##.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_ridger_s_r);

        //Setting Toolbar
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.ridger_single);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        //TextView Header  SPK
        tvNoSpk = findViewById(R.id.tv_no_spk);
        tvDate = findViewById(R.id.tv_date);
        tvItem = findViewById(R.id.tv_item);
        tvStatus = findViewById(R.id.tv_status);
        tvLuas = findViewById(R.id.tv_area);
        tvLocation = findViewById(R.id.tv_location);

        //EditText Input
        etNotes = findViewById(R.id.et_notes);
        tvNotes = findViewById(R.id.tv_notes);
        cvGetNotes = findViewById(R.id.cv_get_note);
        cvSetNotes = findViewById(R.id.cv_notes);

        //ReportPoros
        rvListRidgerS = findViewById(R.id.rv_list_ridger_single);
        tvTotalSPL = findViewById(R.id.tv_total_spl);
        tvTotalSTDR = findViewById(R.id.tv_total_stdr);
        tvTotalSTD = findViewById(R.id.tv_total_std);
        tvTotalMIN = findViewById(R.id.tv_total_min);
        tvTotalMAX = findViewById(R.id.tv_total_max);
        tvTotalRata = findViewById(R.id.tv_total_rata);
        tvTotalPencapaian = findViewById(R.id.tv_total_pencapaian);
        tvTotalKualitas = findViewById(R.id.tv_total_pkualitas);
        //ReportKuku
        tvTotalSPL2 = findViewById(R.id.tv_total_spl2);
        tvTotalSTDR2 = findViewById(R.id.tv_total_stdr2);
        tvTotalSTD2 = findViewById(R.id.tv_total_std2);
        tvTotalMIN2 = findViewById(R.id.tv_total_min2);
        tvTotalMAX2 = findViewById(R.id.tv_total_max2);
        tvTotalRata2 = findViewById(R.id.tv_total_rata2);
        tvTotalPencapaian2 = findViewById(R.id.tv_total_pencapaian2);
        tvDataNull = findViewById(R.id.tv_data_null);

        btDisAgree = findViewById(R.id.bt_disagree);
        btAgree = findViewById(R.id.bt_agree);

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

        //Output SPK HEADER
        tvNoSpk.setText(spk_srv.getSPK_ID());
        tvDate.setText(outputDateStr);
        tvItem.setText(spk_srv.getItem());
        tvStatus.setText(spk_srv.getSPK_Status());
        tvLocation.setText(spk_srv.getLokasi());

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
        getDataRidgerS();
        getNotes();

        btDisAgree.setOnClickListener(v -> {
            catatan = etNotes.getText().toString();
            if (dt_user.equals("Kasie")) {
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
            if (dt_user.equals("Kasie")) {
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

    private void confirmDisAgree() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Data");
        builder.setMessage(messageBuilder);
        builder.setPositiveButton("Ya", (dialog, which) -> {
            if (!(catatan.matches(""))) {
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

    private void confirmAgree() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Data");
        builder.setMessage(messageBuilder);
        builder.setPositiveButton("Ya", (dialog, which) -> {
            if (!(catatan.matches(""))) {
                sendNotes();
                if (spk_srv.getSPK_Status().equals("ON_KASIE")) {
                    sendDataSAP();
                }
                updateStatus();
                sendNotifAgree();
            } else {
                if (spk_srv.getSPK_Status().equals("ON_KASIE")) {
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

    private void sendDataSAP() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.UPLOAD_RIDGERS_SRV,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("response : ", response);
                        if (obj.getString("data").equalsIgnoreCase("succes")) {
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
                params.put("SPKID_KodeSPK", spk_srv.getSPK_ID());
                params.put("ITEMNO_ItemSPK", spk_srv.getItem());
                params.put("AKCODE_KodeAktifitas", spk_srv.getKode_Aktivitas());
                params.put("PGMTDATE_TanggalPengamatan", spk_srv.getTanggal_SPK());
                params.put("TKPGMT_KodeIndukTenagaKerja", spk_srv.getPengamat());
                params.put("TKMD_KodeIndukTenagaKerja", spk_srv.getMandor_Operator());
                params.put("LOKASI_KodeLokasi", spk_srv.getLokasi());
                params.put("TOTSAMPLE_TotalSample", String.valueOf(jarakPoros.size()));
                params.put("MASUKSTD_MasukStandard", String.valueOf(countSTDRPoros));
                params.put("ZMIN_Minimum", String.valueOf(minValuePoros));
                params.put("ZMAX_Maximum", String.valueOf(maxValuePoros));
                params.put("ZAVG_RataRata", df.format(meanPoros));
                params.put("TOTSAMPLE2_TotalSample", String.valueOf(kedalamanKuku.size()));
                params.put("MASUKSTD2_MasukStandard", String.valueOf(countSTDRKuku));
                params.put("ZMIN2_Minimum", String.valueOf(minValuePoros));
                params.put("ZMAX2_Maximum", String.valueOf(maxValuePoros));
                params.put("ZAVG2_RataRata", df.format(meanKuku));
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @SuppressLint("SetTextI18n")
    private void getDataRidgerS() {
        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.item_progres_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_RIDGERS_URL_SRV,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                ridgerSingle = new RidgerSingle(
                                        data.getString("ID"),
                                        data.getString("No_Sampel"),
                                        data.getString("SPK_ID"),
                                        data.getString("Tanggal_SPK"),
                                        data.getString("KIT"),
                                        data.getString("Pengamat"),
                                        data.getString("Lokasi"),
                                        data.getString("Jarak_Poros"),
                                        data.getString("Kedalaman_Kuku")
                                );
                                list.add(ridgerSingle);

                                totalSamplePoros += Double.parseDouble(ridgerSingle.getPoros_gulud());
                                totalSampleKuku += Double.parseDouble(ridgerSingle.getKuku_ridger());

                                //Poros Gulud
                                jarakPoros.add(ridgerSingle.getPoros_gulud());
                                if (minValuePoros == 0) {
                                    minValuePoros = Double.parseDouble(jarakPoros.get(0));
                                }
                                if (Double.parseDouble(jarakPoros.get(i)) > 48 && Double.parseDouble(jarakPoros.get(i)) < 52) {
                                    countSTDRPoros++;
                                }
                                if (Double.parseDouble(jarakPoros.get(i)) < minValuePoros) {
                                    minValuePoros = Double.parseDouble(jarakPoros.get(i));
                                }
                                if (Double.parseDouble(jarakPoros.get(i)) > maxValuePoros) {
                                    maxValuePoros = Double.parseDouble(jarakPoros.get(i));
                                }


                                //Kedalaman Kuku
                                kedalamanKuku.add(ridgerSingle.getKuku_ridger());
                                if (minValueKuku == 0) {
                                    minValueKuku = Double.parseDouble(kedalamanKuku.get(0));
                                }
                                if (Double.parseDouble(kedalamanKuku.get(i)) > 15 && Double.parseDouble(kedalamanKuku.get(i)) < 20) {
                                    countSTDRKuku++;
                                }
                                if (Double.parseDouble(kedalamanKuku.get(i)) < minValueKuku) {
                                    minValueKuku = Double.parseDouble(kedalamanKuku.get(i));
                                }
                                if (Double.parseDouble(kedalamanKuku.get(i)) > maxValueKuku) {
                                    maxValueKuku = Double.parseDouble(kedalamanKuku.get(i));
                                }

                            }

                            pencapaianPoros = (Double.parseDouble(String.valueOf(countSTDRPoros)) / jarakPoros.size())*100;
                            pencapaianKuku = (Double.parseDouble(String.valueOf(countSTDRKuku)) / kedalamanKuku.size())*100;

                            meanPoros = totalSamplePoros / jarakPoros.size();
                            meanKuku = totalSampleKuku / kedalamanKuku.size();

                            double totalPKualitas = (pencapaianPoros + pencapaianKuku) / 2;

                            //SetText Report Jarak Poros
                            tvTotalSPL.setText(String.valueOf(jarakPoros.size()));
                            tvTotalSTDR.setText(String.valueOf(countSTDRPoros));
                            tvTotalSTD.setText(String.valueOf(jarakPoros.size() - countSTDRPoros));
                            tvTotalMIN.setText(String.valueOf(minValuePoros));
                            tvTotalMAX.setText(String.valueOf(maxValuePoros));
                            tvTotalRata.setText(String.valueOf(df.format(meanPoros)));
                            tvTotalPencapaian.setText(df.format(pencapaianPoros) + "%");

                            //SetText Report Kedalaman Kuku
                            tvTotalSPL2.setText(String.valueOf(kedalamanKuku.size()));
                            tvTotalSTDR2.setText(String.valueOf(countSTDRKuku));
                            tvTotalSTD2.setText(String.valueOf(kedalamanKuku.size() - countSTDRKuku));
                            tvTotalMIN2.setText(String.valueOf(minValueKuku));
                            tvTotalMAX2.setText(String.valueOf(maxValueKuku));
                            tvTotalRata2.setText(String.valueOf(df.format(meanKuku)));
                            tvTotalPencapaian2.setText(df.format(pencapaianKuku) + "%");

                            tvTotalKualitas.setText(df.format(totalPKualitas) + "%");

                            tvDataNull.setVisibility(View.GONE);
                            rvListRidgerS.setVisibility(View.VISIBLE);
                            rvListRidgerS.setLayoutManager(new LinearLayoutManager(this));
                            rvListRidgerS.setNestedScrollingEnabled(false);
                            rvListRidgerS.setHasFixedSize(false);
                            RidgerSingleAdapter adapter = new RidgerSingleAdapter(ResultRidgerSRActivity.this, list);
                            rvListRidgerS.setAdapter(adapter);
                            adapter.onItemClickCallback(data -> {
                                viewImage(String.valueOf(data.getId()));
                            });
                        } else {
                            tvDataNull.setVisibility(View.VISIBLE);
                            rvListRidgerS.setVisibility(View.GONE);
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
                params.put("KIT", spk_srv.getPengamat());
                params.put("SPK_ID", spk_srv.getSPK_ID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void viewImage(String id) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.item_view_image, null);

        ImageView ivObservation = dialogView.findViewById(R.id.iv_observation);

        String url = "https://ggp-pis.com/spk_pengamatan/images/";

        Glide.with(this)
                .load(url + id + ".jpg")
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
                String date = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
                params.put("Tanggal_Realisasi", "date");
                params.put("SPK_Status", status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void sendNotes() {

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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getNotes() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_NOTES_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        StringBuilder set_catatan = new StringBuilder();
                        if (jsonArray.length() > 0) {
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

                        } else {
                            tvNotes.setText(R.string.data_null);
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
                params.put("indeks", set_index);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void sendNotifAgree() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    Toast.makeText(this, "Notifikasi Terkirim", Toast.LENGTH_SHORT).show();
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

    private void sendNotifDisAgree() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    Toast.makeText(this, "Notifikasi Terkirim", Toast.LENGTH_SHORT).show();
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