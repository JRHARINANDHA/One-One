package com.reality.escape.one_one;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.reality.escape.one_one.Models.MessageModel;

public class ChatActivity extends AppCompatActivity {
    private FloatingActionButton send;
    private EditText input;
    private ListView listOfMessages;
    private FirebaseListAdapter<MessageModel> adapter;
    private String title,sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
        showMessage();
        //showReceivedMessage();


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance()
                        .getReference(title)
                        .child(sender)
                        .push()
                        .setValue(new MessageModel(input.getText().toString(), FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getDisplayName(),"received")
                        );

                FirebaseDatabase.getInstance()
                        .getReference(sender)
                        .child(title)
                        .push()
                        .setValue(new MessageModel(input.getText().toString(), FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getDisplayName(),"sent")
                        );


                input.setText("");
            }
        });

    }
    private void showMessage() {
        adapter = new FirebaseListAdapter<MessageModel>(this, MessageModel.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference(title).child(sender)) {
            @Override
            protected void populateView(View v, MessageModel model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                if(model.getMessageType().equalsIgnoreCase("sent")){
                    messageText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                    messageUser.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                    messageTime.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                }
                else{
                    messageText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    messageUser.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    messageTime.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                }

                // Format the date before showing it
                messageTime.setText(DateFormat.format("HH:mm",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    private void init() {
        listOfMessages = (ListView) findViewById(R.id.list_of_messages);
        input = (EditText) findViewById(R.id.input);
        send = (FloatingActionButton) findViewById(R.id.send);

        Intent i = getIntent();
        title = i.getExtras().getString("title");
        sender = i.getExtras().getString("sender");

       // toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
    }
}
