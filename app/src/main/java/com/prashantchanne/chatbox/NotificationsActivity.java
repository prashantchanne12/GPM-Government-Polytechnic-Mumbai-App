package com.prashantchanne.chatbox;

import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.TextView;

public class NotificationsActivity extends AppCompatActivity {

    private TextView notificationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notificationText = findViewById(R.id.messageTextView);

        String dataMessage = getIntent().getStringExtra("message");
        String dataFromMessage = getIntent().getStringExtra("from_user_id");

        notificationText.setText(" FROM : "+dataFromMessage+ "\n MESSAGE : "+dataMessage);

    }
}
