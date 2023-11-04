package com.example.proyecto_iot.inicioApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.ActivityRegistroBinding;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistroActivity extends AppCompatActivity {
    private ActivityRegistroBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String name;
    private String lastName;
    private String code;
    private String email;
    private String pass;
    private String type;
    Button sendButton;
    ProgressBar progressBar;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String CHANNEL_ID = "canalDelegadoGeneral";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        progressBar  = binding.progressBar;
        sendButton = binding.sendButton;
        progressBar.setVisibility(View.GONE);

        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });

        sendButton.setOnClickListener(view -> {
            setInPogressBar(true);
            if (validFields()){

                // crear usuario en firebase authentication
                mAuth.createUserWithEmailAndPassword(code+"@app.com", pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Log.d("msg-test", "usuario creado en authentication");
                                    // crear usuario en firestore
                                    crearUsuarioFirestore();
                                }
                                else{
                                    Log.d("msg-test", "error al crear usuario");
                                }
                            }
                        });
            }
        });
    }
    public void setInPogressBar(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.GONE);
            return;
        }else {
            progressBar.setVisibility(View.GONE);
            sendButton.setVisibility(View.VISIBLE);
            return;
        }
    }

    boolean validFields(){
        name = binding.editNameSign.getEditText().getText().toString();
        lastName = binding.editLastnameSign.getEditText().getText().toString();
        code = binding.editCodeSign.getEditText().getText().toString();
        email = binding.editEmailSign.getEditText().getText().toString();
        pass = binding.editPasswSign.getEditText().getText().toString(); // validar que contraseña tenga al menos 6 caracteres
        type = binding.userTypeSpinner.getEditText().getText().toString();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(code) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(type)){
            return true;
        }else {
            if(name.isEmpty()){
                binding.editNameSign.setError("campo vacio");
            }

            if (lastName.isEmpty()) {
                binding.editLastnameSign.setError("campo vacio");
            }
            if (code.isEmpty()){
                binding.editCodeSign.setError("campo vacio");

            }
            if(email.isEmpty()){
                binding.editEmailSign.setError("campo vacio");

            }
            if (pass.isEmpty()) {
                binding.editPasswSign.setError("campo vacio");

            }
            if (type.isEmpty()){
                binding.userTypeSpinner.setError("campo vacio");

            }

            setInPogressBar(false);
            Toast.makeText(this,"Complete todos los campos",Toast.LENGTH_SHORT).show();

            return false;
        }

    }

    void crearUsuarioFirestore(){
        FirebaseUser user = mAuth.getCurrentUser();
        Alumno nuevoAlumno = new Alumno(user.getUid(),name, lastName, "Alumno", code, email, "", type, "inactivo");
        db.collection("alumnos")
                .document(user.getUid())
                .set(nuevoAlumno)
                .addOnSuccessListener(unused -> {
                    Log.d("msg-test", "usuario guardado en firestore");
                    //enviar la notificacion al delegado general
                    enviarNotificacion();
                    setInPogressBar(false);

                    Intent intent = new Intent(RegistroActivity.this, ConfirmarregistroActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }
    public void enviarNotificacion(){
        //current username, message, currentUserId, otherUserToken
        FirebaseUtilDg.getCollAlumnos().whereEqualTo("codigo","20200643").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Alumno usuarioDg = task.getResult().toObjects(Alumno.class).get(0);
                try {
                    JSONObject jsonObject = new JSONObject();

                    JSONObject notificationObj = new JSONObject();
                    notificationObj.put("title","Solicitud de registro");
                    notificationObj.put("body","Un nuevo usuario solicita ser miembro de ActiviConnect");


                    //JSONObject dataObj = new JSONObject();
                    //dataObj.put("userId",currentUser.getUserId());   //mismo identificador que el putExtra del splashActivity


                    jsonObject.put("notification",notificationObj);
                    //jsonObject.put("data",dataObj);
                    jsonObject.put("to",usuarioDg.getFcmToken());


                    callApi(jsonObject);


                }catch (Exception e){

                }

            }
        });


    }
    public void callApi(JSONObject jsonObject){
        MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization","Bearer AAAAZ6GvChg:APA91bGcMRy5DGWg6JBjNDsXSzKHFqrcZ9nE-blBNJGw9BPHXYAlHW1pdzn0n0BSf-PrhRjg_PO7qUmZPWhL4mEGuoHO-Sk7u8Ui1UZU4pIKSKplfE7wxQO6c1wiY073Jm6fzkR2Kg0q")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });

    }

}