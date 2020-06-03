package com.anshul_kulhari.trackcovid19.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anshul_kulhari.trackcovid19.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private TextView tvTotalConfirmed, tvTotalDeaths, tvTotalRecovered, tvTotalNewCases,tvTotalNewDeaths;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // call view
        tvTotalConfirmed = root.findViewById(R.id.tvTotalConfirmed);
        tvTotalDeaths = root.findViewById(R.id.tvTotalDeaths);
        tvTotalRecovered = root.findViewById(R.id.tvTotalRecovered);
        tvTotalNewCases=root.findViewById(R.id.tvtotal_new_cases_today);
        tvTotalNewDeaths=root.findViewById(R.id.tvtotal_new_deaths_today);

    progressBar = root.findViewById(R.id.progress_circular_home);

        // Action Bar title
        getActivity().setTitle("Around the World");

        // call Volley
        getData();

        return root;
    }

    private String getDate(long milliSecond){
        // Mon, 23 Mar 2020 02:01:04 PM
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss aaa");

        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(milliSecond);
        return formatter.format(calendar.getTime());
    }

    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = "https://api.thevirustracker.com/free-api?global=stats";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("results"));
                    JSONObject data = jsonArray.getJSONObject(0);
                    tvTotalConfirmed.setText(data.getString("total_cases"));
                    tvTotalDeaths.setText(data.getString("total_deaths"));
                    tvTotalRecovered.setText(data.getString("total_recovered"));
                    tvTotalNewCases.setText(" +"+data.getString("total_new_cases_today"));
                    tvTotalNewDeaths.setText(" +"+data.getString("total_new_deaths_today"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.d("Error Response", error.toString());
            }
        });

        queue.add(stringRequest);
    }
}
