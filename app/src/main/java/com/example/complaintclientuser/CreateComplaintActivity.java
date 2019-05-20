package com.example.complaintclientuser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.complaintclientuser.app.CustomApplication;
import com.example.complaintclientuser.models.request.ComplaintForm;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class CreateComplaintActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private String  username, password;

    protected static final String TAG = CreateComplaintActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_complaint);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Buat Pengaduan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        username = ((CustomApplication)this.getApplication()).getUsername();
        password = ((CustomApplication)this.getApplication()).getPassword();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_complaint, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send:
                new SendDataComplaint().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private class SendDataComplaint extends AsyncTask<Void, Void, String> {

        private ComplaintForm form;

        private final String url = getString(R.string.base_uri) + "/complaint/create";

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CreateComplaintActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            TextInputLayout topic = findViewById(R.id.input_layout_create_complaint_topic);
            String dtopic = topic.getEditText().getText().toString();

            Spinner category = findViewById(R.id.spinner_create_complaint_category);
            String dcategory = category.getSelectedItem().toString();

            EditText body = findViewById(R.id.editText_create_complaint_body);
            String dbody = body.getText().toString();

            form = new ComplaintForm(dtopic, dbody, dcategory);
        }

        @Override
        protected String doInBackground(Void... voids) {

            HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<?> entity = new HttpEntity<>(form, requestHeaders);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());


            try {

                ResponseEntity<String> responseEntity = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        String.class
                );

                return responseEntity.getBody();

            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return e.getLocalizedMessage();
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return e.getLocalizedMessage();
            } catch (RestClientException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return e.getLocalizedMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            showMessage(s);
        }
    }

    private void showMessage(String s) {
        if (s.equals("400")) {
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            intent.putExtra("toClass", CreateComplaintActivity.class);
            startActivity(intent);
        } else{
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getBaseContext(), HomeActivity.class);
            startActivity(intent);
        }

    }
}
