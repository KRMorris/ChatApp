package com.example.morris.chatapp1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private static final int SIGN_IN_REQUEST_CODE=1;
    private FirebaseListAdapter<ChatMessage> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            //Start sign in
            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder()
                            .build(),SIGN_IN_REQUEST_CODE
            );
        }
        else {
            //Display welcome toast if user already logged in
            Toast.makeText(this,"Welcome "+FirebaseAuth.getInstance()
                    .getCurrentUser().getDisplayName(),Toast.LENGTH_LONG).show();

            //Load chat room
            displayChatMessages();

        }
        //Click action
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                SendMessage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == SIGN_IN_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Toast.makeText(this,"Sign in successful.",Toast.LENGTH_LONG).show();
                displayChatMessages();
            }
            else{
                Toast.makeText(this,"Error signing in. Please try again.",Toast.LENGTH_LONG).show();

                //close app
                finish();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.menu_sign_out){
            AuthUI.getInstance().signOut(this).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this, "You have signed out.",Toast.LENGTH_LONG).show();

                            finish();
                        }
                    });
        }
        return true;
    }

    //Send messages to other users through firebase
    public void SendMessage(){
        EditText input =(EditText)findViewById(R.id.input);

        //Push new message to firebase
        FirebaseDatabase.getInstance().getReference().push().setValue(
                new ChatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
        );
        //Clear input field
        input.setText("");
    }
    //Display all messages
    private void displayChatMessages(){
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,
                R.layout.message,FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                //references of message.xml
                TextView messageText =(TextView) v.findViewById(R.id.message_text);
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                TextView messageTime =(TextView) v.findViewById(R.id.message_time);

                //set text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                //Format date and time
                android.text.format.DateFormat df = new android.text.format.DateFormat();
                messageTime.setText(df.format("dd-MM-yyyy (HH:mm:ss)",model.getMessageTime()));

            }
        };
        listOfMessages.setAdapter(adapter);
    }

}
