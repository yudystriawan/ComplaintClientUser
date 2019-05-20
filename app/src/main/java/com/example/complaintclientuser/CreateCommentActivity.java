package com.example.complaintclientuser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class CreateCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_comment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Tambah Tindak Lanjut");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView topic = findViewById(R.id.text_c_topic);
        TextView body = findViewById(R.id.text_c_body);

        topic.setText(getIntent().getExtras().getString("topic"));
        body.setText(getIntent().getExtras().getString("body"));

        ImageButton sendComment = findViewById(R.id.img_btn_send_comment);

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
