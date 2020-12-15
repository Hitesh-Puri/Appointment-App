package com.example.appointmentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText username, password;

    ArrayList<DataBaseUser> users;
    ArrayList<DataBaseService> services;

    private static DatabaseReference databaseUsers = FirebaseDatabase.getInstance().getReference("users");
    private static DatabaseReference databaseServices = FirebaseDatabase.getInstance().getReference("services");

    private Person activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        Button button2= findViewById(R.id.loginBtn);
        Button button1= findViewById(R.id.signUpBtn);
//        Button button3= findViewById(R.id.forgetPwdBtn);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
//        button3.setOnClickListener(this);

        username = (EditText)findViewById(R.id.usernameField2);
        password = (EditText)findViewById(R.id.passwordField2);

        users = new ArrayList<>();
        services = new ArrayList<>();

        updateUsers();
        updateServices();
    }

    public void updateUsers(){
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();  // might need to remove
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    DataBaseUser usr = postSnapshot.getValue(DataBaseUser.class);
                    users.add(usr);
                }
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });
    }

    public void updateServices(){
        databaseServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                services.clear();  // might need to remove
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    DataBaseService service = postSnapshot.getValue(DataBaseService.class);
                    services.add(service);
                }
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                if (validateForm()){
                    if (activeUser instanceof Employee){
                        openEmployee();
                    } else if (activeUser instanceof Patient){
                        openPatient();
                    } else {
                        openAdmin();
                    }
                }
                break;
            case R.id.signUpBtn:
                openMain();
                break;
        }
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    private boolean validateForm() {
        String userName = username.getText().toString();
        String Password = password.getText().toString();

        if (userName.equals("") || Password.equals("")){
            Toast.makeText(getApplicationContext(),"Invalid Form", Toast.LENGTH_LONG).show();
            return false;
        }else {
            for (DataBaseUser usr : users){
                String otherUsername = usr.getUsername();
                String pwd = usr.getPassword();
                String name = usr.getName();
                String role = usr.getRole();
                String id = usr.getId();
                String clinicId = usr.getClinicId();
                if (otherUsername.equals(userName)){
                    if (pwd.equals(Password)){
                        Toast.makeText(getApplicationContext(),"Logged In", Toast.LENGTH_LONG).show();
                        if (role.equals("Employee")){
                            activeUser = new Employee(name, otherUsername, pwd, clinicId);
                        }else if (role.equals("Patient")){
                            activeUser = new Patient(name, otherUsername, pwd);
                        }else {
                            activeUser = new Administrator(otherUsername, pwd);
                        }
                        activeUser.setId(id);
                        return true;
                    }else {
                        Toast.makeText(getApplicationContext(),"Incorrect Password", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        Toast.makeText(getApplicationContext(),"Invalid Login",Toast.LENGTH_LONG).show();
        return false;
    }

    public void openPatient() {
        Intent intent = new Intent(this, PatientUser.class);
        intent.putExtra("user", activeUser);
        intent.putExtra("users", users);
        intent.putExtra("services", services);
        startActivity(intent);
    }

    public void openEmployee() {
        Intent intent = new Intent(this, EmployeeUser.class);
        intent.putExtra("user", activeUser);
        intent.putExtra("users", users);
        intent.putExtra("services", services);
        startActivity(intent);
    }

    public void openAdmin(){
        Intent intent = new Intent(this, Admin.class);
        intent.putExtra("user", activeUser);
        intent.putExtra("users", users);
        intent.putExtra("services", services);
        startActivity(intent);
    }

    public void openMain() {
        Intent intent = new Intent(this, SignUp.class);
        intent.putExtra("user", activeUser);
        startActivity(intent);
    }
}