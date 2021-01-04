package ziz.org.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

@SuppressWarnings("ALL")
public class AdminAddNewProductActivity extends AppCompatActivity {


    private String categoryName;
    private String productName;
    private String productDescription;
    private String productPrice;
    private String saveCurrentDate;
    private String saveCurrentTime;
    private String productRandomKey;
    private String downloadImageUri;

    private ImageView inputProductImage;
    private EditText inputProductName, inputProductDescription, inputProductPrice;
    private Button addNewProductButton;

    private StorageReference productStorageReference;
    private DatabaseReference productDatabaseReference;
    private ProgressDialog progressBar;

    private static final int GALLERYPICK = 1;
    private Uri imageURI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        progressBar = new ProgressDialog(this);
        categoryName = getIntent().getExtras().get("category").toString();

        productStorageReference = FirebaseStorage.getInstance().getReference().child("Product Images");
        productDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Products");

        addNewProductButton = (Button) findViewById(R.id.add_new_product);
        inputProductImage = (ImageView) findViewById(R.id.select_product_image);
        inputProductName = (EditText) findViewById(R.id.product_name);
        inputProductDescription = (EditText) findViewById(R.id.product_description);
        inputProductPrice = (EditText) findViewById(R.id.product_price);

        /* acceder au gallery */
        inputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        /* sauvegarde des donnees dans la base de donnees*/
        addNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });
    }
    /**
     * Ouvrir le galerry d'image
     */
    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERYPICK);
    }

    /**
     * Methode appele par starActivityForResult "openGalery()"
     * Recupere l'URI et charge l'image automatiquement
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERYPICK && resultCode==RESULT_OK && data!=null){
            imageURI = data.getData();
            //Log.d("Le chemin de l'image: ", String.valueOf(imageURI));
            inputProductImage.setImageURI(imageURI);
        }
    }


    private void validateProductData() {
        productName = inputProductName.getText().toString();
        productDescription = inputProductDescription.getText().toString();
        productPrice = inputProductPrice.getText().toString();
        if (imageURI == null){
            Toast.makeText(this, "Product image is mandatory ....", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(productDescription)){
            Toast.makeText(this, "Please write product description ...", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(productName)){
            Toast.makeText(this, "Please write product name ...", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(productPrice)){
            Toast.makeText(this, "Please write product price ...", Toast.LENGTH_SHORT).show();
        }else {
            this.storeProductInformation();
        }
    }

    /**
     *
     */
    private void storeProductInformation() {
        progressBar.setTitle("Add New Product");
        progressBar.setMessage("Dear Admin, Please wait we are adding the new product.");
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();

        Calendar calendar = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = (saveCurrentDate + saveCurrentTime).replace('.', ' ');
        //Log.d("productRandomKey: ", productRandomKey);

        final StorageReference filePath = productStorageReference.child(imageURI.getLastPathSegment() + productRandomKey+".jpg");
        final UploadTask uploadTask = filePath.putFile(imageURI);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Errors: "+message, Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "Product Image uploaded successfully .......", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUri = filePath.getDownloadUrl().toString();
                        //Log.d("downloadImage2 :", downloadImageUri);
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadImageUri = task.getResult().toString();
                            //Log.d("downloadImage1 :", downloadImageUri);
                            Toast.makeText(AdminAddNewProductActivity.this, "get the product image Uri successfully ...", Toast.LENGTH_SHORT).show();
                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    /***
     * Sauvegarde les informations d'un produit a la base de donnees
     */
    private void saveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", productDescription);
        productMap.put("image", downloadImageUri);
        productMap.put("price", productPrice);
        productMap.put("pname", productName);
        productMap.put("category", categoryName);

        productDatabaseReference.child(productRandomKey).updateChildren(productMap)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                        startActivity(intent);
                        progressBar.dismiss();
                        Toast.makeText(AdminAddNewProductActivity.this, "Product added successfully .....", Toast.LENGTH_SHORT).show();
                    }else {
                        progressBar.dismiss();
                        String message = task.getException().toString();
                        Toast.makeText(AdminAddNewProductActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}