package com.example.complaintclientuser;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.complaintclientuser.adapters.ComplaintAdapter;
import com.example.complaintclientuser.models.Complaint;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    protected static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        new GetComplaint().execute();

        FloatingActionButton button = findViewById(R.id.button_createComplaint);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CreateComplaintActivity.class);
                startActivity(intent);
            }
        });

    }


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class GetComplaint extends AsyncTask<Void, Void, List<Complaint>> {
        @Override
        protected List<Complaint> doInBackground(Void... voids) {
            final String url = getString(R.string.base_uri) + "/complaint/all";

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<?> requestEntity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            try {
                ResponseEntity<List<Complaint>> responseEntity = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        requestEntity,
                        new ParameterizedTypeReference<List<Complaint>>() {
                        }
                );

                return responseEntity.getBody();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return null;

        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(HomeActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(List<Complaint> complaints) {
            progressDialog.dismiss();
            displayComplaints(complaints);
        }
    }

    private void displayComplaints(List<Complaint> complaints) {
        RecyclerView recyclerView = findViewById(R.id.recycleView_complaints);

        ComplaintAdapter complaintAdapter = new ComplaintAdapter(complaints);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(complaintAdapter);

    }
}
