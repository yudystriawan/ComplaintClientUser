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
import android.widget.Toast;

import com.example.complaintclientuser.models.request.RegisterForm;

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

import java.util.Collections;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mName, mEmail, mUsername, mPassword, mPasswordConfirm;
    protected static final String TAG = RegisterActivity.class.getSimpleName();
    private String password, passwordConfirm, name, email, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = findViewById(R.id.text_layout_name);
        mEmail = findViewById(R.id.text_layout_email);
        mUsername = findViewById(R.id.text_layout_Rusername);
        mPassword = findViewById(R.id.text_layout_Rpassword);
        mPasswordConfirm = findViewById(R.id.text_layout_passwordConfirm);
        Button button_signup = findViewById(R.id.button_register);
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = mPassword.getEditText().getText().toString();
                passwordConfirm = mPasswordConfirm.getEditText().getText().toString();
                name = mName.getEditText().getText().toString();
                email = mEmail.getEditText().getText().toString();
                username = mUsername.getEditText().getText().toString();
                if (!name.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty() && !passwordConfirm.isEmpty()) {
                    if (!password.equals(passwordConfirm)){
                        mPassword.setErrorEnabled(true);
                        mPassword.setError("password tidak sama");

                        mPasswordConfirm.setErrorEnabled(true);
                        mPasswordConfirm.setError("password tidak sama");
                    }else{
                        mPassword.setError(null);
                        mPasswordConfirm.setError(null);

                        mPassword.setErrorEnabled(false);
                        mPasswordConfirm.setErrorEnabled(false);
                        new Register().execute();
                    }
                }else{
                    Toast.makeText(getBaseContext(), "form tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private class Register extends AsyncTask<Void, Void, ResponseEntity<?>> {
        private ProgressDialog progressDialog;
        private final String url = getString(R.string.base_uri) + "/auth/signup";
        private RegisterForm form;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("Register...");
            progressDialog.show();

            form = new RegisterForm();
            form.setName(name);
            form.setEmail(email);
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
                return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
            } catch (RestClientException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        @Override
        protected void onPostExecute(ResponseEntity<?> responseEntity) {
            progressDialog.dismiss();
            showResponse(responseEntity);
        }
    }

    private void showResponse(ResponseEntity<?> responseEntity) {
        if (responseEntity.getBody() == null) {
            Toast.makeText(this, "Form tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, responseEntity.getBody().toString(), Toast.LENGTH_SHORT).show();

            if (responseEntity.getStatusCode() == HttpStatus.OK) {

                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }
        }


    }
}
