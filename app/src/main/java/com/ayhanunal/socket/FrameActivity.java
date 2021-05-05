package com.ayhanunal.socket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FrameActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference boxState = database.getReference("state");
    DatabaseReference connPort = database.getReference("port");
    DatabaseReference connIP = database.getReference("IP");


    String info, readingState, ipAddress, portNumber;
    Button button, button2, button3, button4;

    TextView frameInfoTextView;

    ImageView img00, img01, img02, img10, img11, img12, img20, img21, img22, img30, img31, img32;
    //List<ImageView> imgList = Arrays.asList(img00, img01, img02, img10, img11, img12, img20, img21, img22, img30, img31, img32);

    ImageView[][] multiDimImg = {{},{},{},{}};

    int posX = 1;
    int posY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);

        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        frameInfoTextView = findViewById(R.id.frameInfoTextView);

        initializeImg();


        Intent intent = getIntent();
        info = intent.getStringExtra("info");
        ipAddress = intent.getStringExtra("IP");
        portNumber = intent.getStringExtra("port");

        if (info.matches("client")){
            frameInfoTextView.setText("Client");
            button.setClickable(false);
            button2.setClickable(false);
            button3.setClickable(false);
            button4.setClickable(false);

            button.setBackgroundColor(getResources().getColor(R.color.clientButton));
            button2.setBackgroundColor(getResources().getColor(R.color.clientButton));
            button3.setBackgroundColor(getResources().getColor(R.color.clientButton));
            button4.setBackgroundColor(getResources().getColor(R.color.clientButton));
        }else{
            frameInfoTextView.setText("Server");
        }

        connPort.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String availablePort = snapshot.getValue().toString();
                    if (availablePort.matches("")){
                        Toast.makeText(getApplicationContext(), "Connection Problems", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.toException().getLocalizedMessage();
            }
        });


        boxState.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    readingState = dataSnapshot.getValue().toString();

                    if(readingState.matches("1")){
                        //next
                        posX --;
                        if(posX >= 0 && posX <= 3){
                            moveToBox(posX,posY);
                            baseBoxState();
                        }else{
                            posX ++;
                            Toast.makeText(getApplicationContext(), "Siniri astiniz", Toast.LENGTH_LONG).show();
                        }

                    }else if(readingState.matches("2")){
                        //left
                        posY --;
                        if(posY >= 0 && posY <= 2){
                            moveToBox(posX,posY);
                            baseBoxState();
                        }else{
                            posY ++;
                            Toast.makeText(getApplicationContext(), "Siniri astiniz", Toast.LENGTH_LONG).show();
                        }
                    }else if(readingState.matches("3")){
                        //back
                        posX ++;
                        if(posX >= 0 && posX <= 3){
                            moveToBox(posX,posY);
                            baseBoxState();
                        }else{
                            posX --;
                            Toast.makeText(getApplicationContext(), "Siniri astiniz", Toast.LENGTH_LONG).show();
                        }
                    }else if(readingState.matches("4")){
                        //right
                        posY ++;
                        if(posY >= 0 && posY <= 2){
                            moveToBox(posX,posY);
                            baseBoxState();
                        }else{
                            posY --;
                            Toast.makeText(getApplicationContext(), "Siniri astiniz", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        System.out.println("base state ready");
                    }
                }catch (Exception e){
                    System.out.println(e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(),"Conn Error.",Toast.LENGTH_LONG).show();

            }
        });




    }

    private void baseBoxState(){
        try {
            boxState.setValue("0");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void next(View view){
        try {
            boxState.setValue("1");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void left(View view){
        try {
            boxState.setValue("2");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void back(View view){
        try {
            boxState.setValue("3");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void right(View view){
        try {
            boxState.setValue("4");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initializeImg(){
        img00 = findViewById(R.id.imageView00);
        img01 = findViewById(R.id.imageView01);
        img02 = findViewById(R.id.imageView02);
        img10 = findViewById(R.id.imageView10);
        img11 = findViewById(R.id.imageView11);
        img12 = findViewById(R.id.imageView12);
        img20 = findViewById(R.id.imageView20);
        img21 = findViewById(R.id.imageView21);
        img22 = findViewById(R.id.imageView22);
        img30 = findViewById(R.id.imageView30);
        img31 = findViewById(R.id.imageView31);
        img32 = findViewById(R.id.imageView32);
        multiDimImg = new ImageView[][]{{img00,img01,img02},{img10, img11, img12},{img20, img21, img22},{img30, img31, img32}};

    }

    private void moveToBox(int posx, int posy){

        for(int i=0; i<4; i++){
            for (int j=0; j<3; j++){
                multiDimImg[i][j].setVisibility(View.INVISIBLE);
            }
        }

        multiDimImg[posx][posy].setVisibility(View.VISIBLE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            boxState.setValue("0");
            if (info.matches("server")){
                connPort.setValue("");
                connIP.setValue("");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            boxState.setValue("0");
            if (info.matches("server")){
                connPort.setValue("");
                connIP.setValue("");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}