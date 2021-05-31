package com.ggpc.spkpengamatan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ggpc.spkpengamatan.EndPoints;
import com.ggpc.spkpengamatan.Model.SPK_REAL;
import com.ggpc.spkpengamatan.Model.TK;
import com.ggpc.spkpengamatan.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ListViewHolder> {

    private final Context context;
    private List<SPK_REAL> list;
    private ArrayList<String> arrayTK = new ArrayList<>();
    private static TK tk;
    private SPK_REAL spk_real;
    private String[] item = {"Bakso", "Ayam Goreng", "Mie Rebus", "Nasi Padang", "Ikan Bakar", "Seblak", "Gorengan", "Mie Ayam"};
    private String[] items;
    ArrayList<String> name_tk = new ArrayList<String>();
    private String[] tk_nama;
    private String[] tk_kit;
    private static String index_mandor, nama_tk, kit_tk;

    public ActivityAdapter(Context context, List<SPK_REAL> list, String[] tk_nama, ArrayList<String> arrayTK) {
        this.context = context;
        this.list = list;
        this.tk_nama = tk_nama;
        this.arrayTK = arrayTK;
        //getTK();
        //getProfileTK();
    }

    public ActivityAdapter(Context context, List<SPK_REAL> list) {
        this.context = context;
        this.list = list;
    }

    public ActivityAdapter(Context context, String[] tk_nama, String[] tk_kit){
        this.context = context;
        this.tk_nama = tk_nama;
        this.tk_kit = tk_kit;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        spk_real = list.get(position);
        index_mandor = spk_real.getMandor_Operator();
        kit_tk = spk_real.getPengamat();
        //getProfileTK(kit_tk);

        holder.tvMandor.setText(spk_real.getMandor_Operator());
        holder.tvActivity.setText(spk_real.getDeskripsi());
        holder.tvLocation.setText(spk_real.getLokasi());

        /*ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.item_spinner,tk_nama);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Memasukan Adapter pada Spinner
        holder.spinner.setAdapter(adapter);
        //holder.spinner.setSelection(0, false);
        holder.spinner.setBackgroundColor(R.drawable.bg_edit_text_white);

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context,arrayTK.get(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        if (spk_real.getPengamat().equals("0")){
            holder.tvObserver.setText(R.string.observer_null);
        } else {
            holder.tvObserver.setText(spk_real.getNama_Pengamat());
            //Toast.makeText(context, "Hasil : " + getTk().getNama(), Toast.LENGTH_SHORT).show();
        }
        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(list.get(holder.getAdapterPosition())));

    }

    private void getProfileTK(String kit_tk){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_PROFILE_TK_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("message").equalsIgnoreCase(String.valueOf(true))) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            tk = new TK(
                                    data.getString("kit"),
                                    data.getString("nama"),
                                    data.getString("mandor"));
                            //Toast.makeText(context, tk.getNama(), Toast.LENGTH_SHORT).show();

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("kit", kit_tk);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    /*private void sendObserver(String tk_kit) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SEND_OBSERVER,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("message").equalsIgnoreCase(String.valueOf(true))) {
                            Toast.makeText(context, "Data Berhasil Dikirim", Toast.LENGTH_SHORT).show();
                            sendNotifTK(tk_kit);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },

                error -> Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID", spk_real.getID());
                params.put("SPK_ID", spk_real.getSPK_ID());
                params.put("Pengamat", tk_kit);
                params.put("SPK_Status", "NEW");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }*/

    /*private void sendNotifTK(String kit_tk) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SEND_NOTIF_TK,
                response -> {
                    Toast.makeText(context, "Notifikasi Terkirim", Toast.LENGTH_SHORT).show();
                    //finish();
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

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }*/

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        TextView tvMandor, tvActivity, tvLocation, tvObserver;
        Spinner spinner;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMandor = itemView.findViewById(R.id.tv_mandor);
            tvActivity = itemView.findViewById(R.id.tv_activity);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvObserver = itemView.findViewById(R.id.tv_observer);
            spinner = itemView.findViewById(R.id.sp_list_tk);
        }
    }

    private OnItemClickCallback onItemClickCallback;

    public void onItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(SPK_REAL spk);
    }

}
