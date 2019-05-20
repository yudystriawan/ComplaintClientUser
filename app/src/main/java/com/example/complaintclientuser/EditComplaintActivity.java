package com.example.complaintclientuser;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.complaintclientuser.models.Instance;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class EditComplaintActivity extends AbstractAsyncActivity {

    private Spinner spinnerInstance;
    private List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_complaint);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Edit Pengaduan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String name = getIntent().getExtras().getString("name");
        String topic = getIntent().getExtras().getString("topic");
        String body = getIntent().getExtras().getString("body");
        String instance = getIntent().getExtras().getString("instance");
        String category = getIntent().getExtras().getString("category");
        boolean negative = getIntent().getExtras().getBoolean("negative");
        double percent = getIntent().getExtras().getDouble("percent");
        Integer id = getIntent().getExtras().getInt("id");
        String createdAt = getIntent().getExtras().getString("created_at");

        TextView viewTopic = findViewById(R.id.text_edit_topic);
        TextView viewName = findViewById(R.id.text_edit_name);
        TextView viewCategory = findViewById(R.id.text_edit_category);
        TextView viewbody = findViewById(R.id.text_edit_body);
        CheckBox boxNegative = findViewById(R.id.checkBox_negative);
        ImageButton save = findViewById(R.id.img_btn_edit_save);
        TextView viewCreatedAt = findViewById(R.id.text_edit_createdAt);

        //insert data
        viewTopic.setText(topic);
        viewName.setText(name);
        viewCategory.setText(category);
        viewbody.setText(body);
        viewCreatedAt.setText(createdAt);
        boxNegative.setChecked(negative);

//
//        int acc = (int) (percent * 100);
//        viewPercent.setText("Akurasi " + acc + "%");

        new GetDataInstance().execute();
    }

    private class GetDataInstance extends AsyncTask<Void, Void, List<Instance>> {

        private final String url = getString(R.string.base_uri) + "/instance/all";

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();

            spinnerInstance = findViewById(R.id.spinner_edit_instance);
        }

        @Override
        protected List<Instance> doInBackground(Void... voids) {

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            try {
                ResponseEntity<List<Instance>> responseEntity = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        httpEntity,
                        new ParameterizedTypeReference<List<Instance>>() {
                        }
                );
                return responseEntity.getBody();
            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Instance> instances) {
            dismissProgressDialog();
            showDatas(instances);
        }
    }

    private void showDatas(List<Instance> instances) {

        data = new ArrayList<>();

        for (int i = 0; i < instances.size(); i++) {
            data.add(instances.get(i).getName());
        }

        spinnerInstance.setAdapter(
                new ArrayAdapter<>(EditComplaintActivity.this, android.R.layout.simple_spinner_dropdown_item, data)
        );

        spinnerInstance
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String instansi = data.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
    }
}