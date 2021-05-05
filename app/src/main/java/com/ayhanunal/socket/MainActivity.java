package com.ayhanunal.socket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText ipEditText, portEditText;
    String ipAddress, portNumber;

    IpAddressValidator validator;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbIP = database.getReference("IP");
    DatabaseReference dbPort = database.getReference("port");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validator = new IpAddressValidator();

        ipEditText = findViewById(R.id.ip_edit_text);
        portEditText = findViewById(R.id.port_edit_text);

        ipEditText.setText(Utils.getIPAddress(true));



    }

    public void serverLogin(View view){

        ipAddress = ipEditText.getText().toString();
        portNumber = portEditText.getText().toString();

        if (validator.isValid(ipAddress) && !portNumber.matches("") && isNumeric(portNumber)){
            Intent intent = new Intent(MainActivity.this, FrameActivity.class);
            intent.putExtra("info", "server");
            intent.putExtra("IP", ipAddress);
            intent.putExtra("port", portNumber);
            try {
                dbIP.setValue(ipAddress);
                dbPort.setValue(portNumber);
            }catch (Exception e){
                e.printStackTrace();
            }
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_LONG).show();
        }


    }

    public void clientLogin(View view){

        ipAddress = ipEditText.getText().toString();
        portNumber = portEditText.getText().toString();

        if (validator.isValid(ipAddress) && !portNumber.matches("") && isNumeric(portNumber)){
            dbPort.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {

                        String readingPost = dataSnapshot.getValue().toString();
                        if(readingPost.matches(portNumber)){

                            Intent intent = new Intent(MainActivity.this, FrameActivity.class);
                            intent.putExtra("info", "client");
                            intent.putExtra("IP", ipAddress);
                            intent.putExtra("port", portNumber);

                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(getApplicationContext(), "Error Port !!", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    databaseError.toException().getLocalizedMessage();

                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_LONG).show();
        }

    }

    public static boolean isNumeric(String str) {
        return str != null && str.matches("[-+]?\\d*\\.?\\d+");
    }



}

class IpAddressValidator {

    private static final String zeroTo255
            = "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])";

    private static final String IP_REGEXP
            = zeroTo255 + "\\." + zeroTo255 + "\\."
            + zeroTo255 + "\\." + zeroTo255;

    private static final Pattern IP_PATTERN
            = Pattern.compile(IP_REGEXP);

    public boolean isValid(String address) {
        return IP_PATTERN.matcher(address).matches();
    }
}