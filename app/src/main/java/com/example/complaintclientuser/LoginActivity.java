package com.example.complaintclientuser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.complaintclientuser.app.CustomApplication;
import com.example.complaintclientuser.models.request.LoginForm;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

public class LoginActivity extends AppCompatActivity {

    private TextView textView_toRegister;
    private TextInputLayout layoutUsername, layoutPassword;
    private String username, password;

    protected static final String TAG = LoginActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView_toRegister = findViewById(R.id.text_toRegister);

        textView_toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        Button login = findViewById(R.id.button_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutUsername = findViewById(R.id.text_layout_username);
                username = layoutUsername.getEditText().getText().toString();
                layoutPassword = findViewById(R.id.text_layout_password);
                password = layoutPassword.getEditText().getText().toString();

                if (username.isEmpty()) {
                    layoutUsername.setErrorEnabled(true);
                    layoutUsername.setError("username tidak boleh kosong");
                } else {
                    layoutUsername.setError(null);
                    layoutUsername.setErrorEnabled(false);
                }
                if (password.isEmpty()) {
                    layoutPassword.setErrorEnabled(true);
                    layoutPassword.setError("password tidak boleh kosong");
                } else {
                    layoutPassword.setError(null);
                    layoutPassword.setErrorEnabled(false);
                }
                if (!username.isEmpty() && !password.isEmpty()) {


                    new Login().execute();
                }
            }
        });

    }

    private class Login extends AsyncTask<Void, Void, ResponseEntity<?>> {

        private ProgressDialog progressDialog;
        private final String url = getString(R.string.base_uri) + "/auth/signin";
        private LoginForm form;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Login...");
            progressDialog.show();

            form = new LoginForm();


            form.setUsername(username);


            form.setPassword(password);

        }

        @Override
        protected ResponseEntity<?> doInBackground(Void... voids) {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<?> requestEntity = new HttpEntity<>(form, requestHeaders);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        requestEntity,
                        String.class
                );

                return responseEntity;
            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new ResponseEntity<>("username atau password salah", HttpStatus.UNAUTHORIZED);
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            } catch (RestClientException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }

            return new ResponseEntity<>("Something wrong", HttpStatus.BAD_REQUEST);

        }

        @Override
        protected void onPostExecute(ResponseEntity<?> responseEntity) {
            progressDialog.dismiss();
            checkResponse(responseEntity);
        }
    }

    private void checkResponse(ResponseEntity<?> responseEntity) {
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            ((CustomApplication) this.getApplication()).setUsername(username);
            ((CustomApplication) this.getApplication()).setPassword(password);
            Intent intent = new Intent(getBaseContext(), HomeActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, responseEntity.getBody().toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
