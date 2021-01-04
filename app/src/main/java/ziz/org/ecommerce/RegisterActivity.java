package ziz.org.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    /**
     * Creer un compte
     */
    private Button createAccountButton;
    /**
     * Champs de saisie username
     */
    private EditText inputName;
    /**
     * Champs de saisie numero de telepone
     */
    private EditText inputPhoneNumber;
    /**
     * champs de saisie mots de passe
     */
    private EditText inputPassword;
    /**
     * La barre de progression
     */
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccountButton = (Button)findViewById(R.id.register_btn);
        inputName = (EditText)findViewById(R.id.register_username_input);
        inputPhoneNumber = (EditText)findViewById(R.id.register_phone_number_input);
        inputPassword = (EditText)findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                this.crateAccount();
            }

            private void crateAccount() {
                String name = inputName.getText().toString();
                String phone = inputPhoneNumber.getText().toString();
                String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(name)){
                    Toast.makeText(RegisterActivity.this, "Please write your name ...", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(phone)){
                    Toast.makeText(RegisterActivity.this, "Please write your number ....", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Please write your password ...", Toast.LENGTH_SHORT).show();
                }else {
                    loadingBar.setTitle("Create Account");
                    loadingBar.setMessage("Please wait, while we are checking the credentials");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    validatePhoneNumber(name,phone,password);
                }
            }
        });
    }

    /**
     * charger d'enregistrer les information d'un utilisateur
     * @param name
     * @param phone
     * @param password
     */
    private void validatePhoneNumber(final String name, final String phone, final String password) {
        /**
         * Récupérez une instance de votre base de données à l'aide de getInstance () et
         * référencez l'emplacement sur lequel vous souhaitez écrire.
         */
        final DatabaseReference ROOTREF = FirebaseDatabase.getInstance().getReference();

        /**
         * Ajoutez un écouteur pour une seule modification des données à cet emplacement.
         * Cet écouteur sera déclenché une fois avec la valeur des données à l'emplacement.
         */
        ROOTREF.addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * La méthode onDataChange() de cette classe est déclenchée une fois lorsque l'écouteur est attaché
             * et à chaque fois que les données changent, y compris les enfants.
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);

                    ROOTREF.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() { /*Ajoute un écouteur qui est appelé à la fin de la tâche.*/
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Congrutulations, your account has been created. ", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error: Please try againt ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else {
                    Toast.makeText(RegisterActivity.this, "This "+name+" already exists .", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number ", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Failed to read value .", error.toException());
            }
        });
    }
}