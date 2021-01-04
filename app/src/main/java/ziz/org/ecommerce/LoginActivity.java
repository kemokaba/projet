package ziz.org.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
/* import android.widget.CheckBox; */
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;
import ziz.org.ecommerce.model.Users;
import ziz.org.ecommerce.prevalent.Prevalent;

public class LoginActivity extends AppCompatActivity {
    /**
     * le numero de telephone
     */
    private EditText inputNumber;
    /**
     * le mots de passe
     */
    private EditText inputPassword;
    /**
     * boutton connexion
     */
    private Button loginButton;
    /**
     * la barre de progression
     */
    private ProgressDialog progressBar;
    /**
     * Remember me "se rappeler de moi"
     */
    private CheckBox checkBoxRememberMe;
    /**
     *
     */
    private TextView adminLink, notAdminLink;

    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button)findViewById(R.id.login_btn);
        inputNumber = (EditText)findViewById(R.id.login_phone_number_input);
        inputPassword = (EditText)findViewById(R.id.login_password_input);
        adminLink = (TextView) findViewById(R.id.admin_panel_link);
        notAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
        checkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);

        progressBar = new ProgressDialog(this);

        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                loginButton.setText("Login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                loginButton.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });
    }

    /**
     * la fonction de connexion user | admin
     */
    private void loginUser()
    {
        String phone = inputNumber.getText().toString();
        String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(LoginActivity.this, "Please write your phone number ...", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Please write your password ....", Toast.LENGTH_SHORT).show();
        }else {
            progressBar.setTitle("Login Account");
            progressBar.setMessage("Please wait, while we are checking the credentials");
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.show();

            this.allowAccessAccount(phone, password); //allow == autorisé
        }
    }

    /**
     * verifie les identifiants de l'utilisateur
     * @param phone
     * @param password
     */
    private void allowAccessAccount(final String phone, final String password)
    {
        if (checkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }
        final DatabaseReference ROOTREF = FirebaseDatabase.getInstance().getReference();
        ROOTREF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDbName).child(phone).exists()) {
                    Users usersData = snapshot.child(parentDbName).child(phone).getValue(Users.class);
                    if (usersData.getPhone().equals(phone)){
                        if (usersData.getPassword().equals(password)){
                            if (parentDbName.equals("Admins")){
                                Toast.makeText(LoginActivity.this, "Welcome Admin you are Logged in successfullyyy ...", Toast.LENGTH_SHORT).show();
                                progressBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            }else if (parentDbName.equals("Users")){
                                Toast.makeText(LoginActivity.this, "Logged successfullyyy ...", Toast.LENGTH_SHORT).show();
                                progressBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnLineUser = usersData;
                                startActivity(intent);
                            }
                        }else {
                            Toast.makeText(LoginActivity.this, "Password incorrect ...", Toast.LENGTH_SHORT).show();
                            progressBar.dismiss();
                        }
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Account with this "+ phone +" number does not exists.", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                    //Toast.makeText(LoginActivity.this, "You need to create a new Account.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}