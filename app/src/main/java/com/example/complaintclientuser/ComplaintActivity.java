package com.example.complaintclientuser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ComplaintActivity extends AppCompatActivity {

    private String name, topic, body, created_at,instance, category;
    private Integer id;
    private double percent;
    private boolean negative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        name = getIntent().getExtras().getString("name");
        topic = getIntent().getExtras().getString("topic");
        body = getIntent().getExtras().getString("body");
        created_at = getIntent().getExtras().getString("created_at");
        instance = getIntent().getExtras().getString("instance");
        category = getIntent().getExtras().getString("category");
        id = getIntent().getExtras().getInt("id");
        negative = getIntent().getExtras().getBoolean("negative");
        percent = getIntent().getExtras().getDouble("percent");

        TextView tv_name = findViewById(R.id.details_name);
        TextView tv_body = findViewById(R.id.details_body);
        TextView tv_created_at = findViewById(R.id.details_createdAt);
        TextView tv_instance = findViewById(R.id.details_instance);
        TextView tv_category = findViewById(R.id.text_edit_category);
        TextView tv_topic = findViewById(R.id.details_topic);

        tv_name.setText(name);
        tv_body.setText(body);
        tv_created_at.setText(created_at);
        tv_instance.setText(instance);
        tv_topic.setText(topic);
        tv_category.setText(category);

        ImageButton edit = findViewById(R.id.img_btn_toEdit);
        ImageButton comment = findViewById(R.id.img_btn_toComment);

//        comment.setVisibility(View.GONE);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), EditComplaintActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("topic", topic);
                intent.putExtra("body", body);
                intent.putExtra("created_at", created_at);
                intent.putExtra("instance", instance);
                intent.putExtra("category", category);
                intent.putExtra("id", id);
                intent.putExtra("negative", negative);
                intent.putExtra("percent", percent);

                startActivity(intent);
            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CreateCommentActivity.class);
                intent.putExtra("topic", topic);
                intent.putExtra("body", body);
                startActivity(intent);
            }
        });

    }
}
