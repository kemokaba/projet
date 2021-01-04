package ziz.org.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import ziz.org.ecommerce.model.Users;
import ziz.org.ecommerce.prevalent.Prevalent;

public class MainActivity extends AppCompatActivity {

    /**
     * Boutton registre "creer un compte"
     */
    private Button joinNowButton;
    /**
     * Boutton "connexion"
     */
    private Button loginButton;

    private String parentDbName = "Users";
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton = (Button)findViewById(R.id.main_join_now_btn);
        loginButton = (Button)findViewById(R.id.main_login_btn);
        progressBar = new ProgressDialog(this);

        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey != "" && UserPasswordKey != ""){
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){
                this.allowAccess(UserPhoneKey, UserPasswordKey);
                progressBar.setTitle("Already Logged in");
                progressBar.setMessage("Please wait ...... ");
                progressBar.setCanceledOnTouchOutside(false);
                progressBar.show();
            }
        }
    }

    /***
     * verifie si l'utilisateur s'est deja connecte
     * @param userPhoneKey
     * @param userPasswordKey
     */
    private void allowAccess(final String userPhoneKey, final String userPasswordKey) {
        final DatabaseReference ROOTREF = FirebaseDatabase.getInstance().getReference();
        ROOTREF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDbName).child(userPhoneKey).exists()) {
                    Users usersData = snapshot.child(parentDbName).child(userPhoneKey).getValue(Users.class);
                    if (usersData.getPhone().equals(userPhoneKey)){
                        if (usersData.getPassword().equals(userPasswordKey)){
                            Toast.makeText(MainActivity.this, "Please wait, you are already logged in", Toast.LENGTH_SHORT).show();
                            progressBar.dismiss();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentOnLineUser = usersData;
                            startActivity(intent);
                        }else {
                            Toast.makeText(MainActivity.this, "Password incorrect ...", Toast.LENGTH_SHORT).show();
                            progressBar.dismiss();
                        }
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Account with this "+ userPhoneKey +" number does not exists.", Toast.LENGTH_SHORT).show();
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