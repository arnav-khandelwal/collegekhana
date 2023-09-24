package com.example.collegekhana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CollegeRegistration extends AppCompatActivity {

    TextInputLayout Collegename,Email,pwd,cpass;
    Button Signup;
    FirebaseAuth Fauth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    String collegename,emailid,password,confpassword;
    String role="College"


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_registration);

        Collegename = (TextInputLayout)findViewById(R.id.collegename);
        Email = (TextInputLayout)findViewById(R.id.Email);
        pwd = (TextInputLayout)findViewById(R.id.Pwd);
        cpass = (TextInputLayout)findViewById(R.id.Cpass);

        Signup = (Button)findViewById(R.id.Signup);


    databaseReference = firebaseDatabase.getInstance().getReference("College");
    Fauth = FirebaseAuth.getInstance();

        Signup.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            collegename = Collegename.getEditText().getText().toString().trim();
            emailid = Email.getEditText().getText().toString().trim();
            password = pwd.getEditText().getText().toString().trim();
            confpassword = cpass.getEditText().getText().toString().trim();

            if (isValid()){
                final ProgressDialog mDialog = new ProgressDialog(CollegeRegistration.this);
                mDialog.setCancelable(false);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setMessage("Registration in progress please wait......");
                mDialog.show();

                Fauth.createUserWithEmailAndPassword(emailid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference("User").child(useridd);
                            final HashMap<String , String> hashMap = new HashMap<>();
                            hashMap.put("Role",role);
                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    HashMap<String , String> hashMap1 = new HashMap<>();
                                    hashMap1.put("College Name",collegename);
                                    hashMap1.put("EmailId",emailid);
                                    hashMap1.put("Password",password);
                                    hashMap1.put("Confirm Password",confpassword);

                                    firebaseDatabase.getInstance().getReference("College")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    mDialog.dismiss();

                                                    Fauth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if(task.isSuccessful()){
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(CollegeRegistration.this);
                                                                builder.setMessage("You Have Registered! Make Sure To Verify Your Email");
                                                                builder.setCancelable(false);
                                                                builder.setPositiveButton("Ok", new DialogInterface().OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                        dialog.dismiss();

                                                                    }
                                                                });
                                                                AlertDialog Alert = builder.create();
                                                                Alert.show();
                                                            }else{
                                                                mDialog.dismiss();
                                                                ReusableCodeForAll.ShowAlert(ChefRegistration.this,"Error",task.getException().getMessage());
                                                            }
                                                        }
                                                    });

                                                }
                                            });

                                }
                            });
                        }
                    }
                });
            }
//
        }
    });

}

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public boolean isValid(){
        Email.setErrorEnabled(false);
        Email.setError("");
        Collegename.setErrorEnabled(false);
        Collegename.setError("");
        pwd.setErrorEnabled(false);
        pwd.setError("");
        cpass.setErrorEnabled(false);
        cpass.setError("");

        boolean isValid=false,isValidname=false,isValidemail=false,isValidpassword=false,isValidconfpassword=false;
        if(TextUtils.isEmpty(collegename)){
            Collegename.setErrorEnabled(true);
            Collegename.setError("Enter College Name");
        }else{
            isValidname = true;
        }
        if(TextUtils.isEmpty(emailid)){
            Email.setErrorEnabled(true);
            Email.setError("Email Is Required");
        }else{
            if(emailid.matches(emailpattern)){
                isValidemail = true;
            }else{
                Email.setErrorEnabled(true);
                Email.setError("Enter a Valid Email Id");
            }
        }
        if(TextUtils.isEmpty(password)){
            pwd.setErrorEnabled(true);
            pwd.setError("Enter Password");
        }else{
            if(password.length()<8){
                pwd.setErrorEnabled(true);
                pwd.setError("Password is Weak");
            }else{
                isValidpassword = true;
            }
        }
        if(TextUtils.isEmpty(confpassword)){
            cpass.setErrorEnabled(true);
            cpass.setError("Enter Password Again");
        }else{
            if(!password.equals(confpassword)){
                cpass.setErrorEnabled(true);
                cpass.setError("Password Dosen't Match");
            }else{
                isValidconfpassword = true;
            }
        }

        isValid = (isValidconfpassword && isValidpassword && isValidemail && isValidname) ? true : false;
        return isValid;

    }
}
      }
}