package com.gunar.cumple;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.gunar.cumple.adapter.RecyclerAdapter;
import com.gunar.cumple.model.Cumple;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText nombre, fecha;
    Button boton;

    private List<Cumple> soliList = new ArrayList<>();
    private RecyclerView soliRecycler;
    private RecyclerAdapter soliAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        load();

        nombre = (EditText) findViewById(R.id.nNombre);
        fecha = (EditText) findViewById(R.id.nFecha);

        soliRecycler = (RecyclerView) findViewById(R.id.re);
        soliAdapter = new RecyclerAdapter(soliList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        soliRecycler.setLayoutManager(mLayoutManager);

        soliRecycler.setAdapter(soliAdapter);


        boton = (Button) findViewById(R.id.nBoton);


        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nuevo();
            }
        });

    }

    public void load() {
        db.collection("cumples").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                 @Override
                                                                 public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                     if (task.isSuccessful()) {
                                                                         for (QueryDocumentSnapshot document : task.getResult()) {
                                                                             Cumple cumple = document.toObject(Cumple.class);
                                                                             soliList.add(cumple);
                                                                         }
                                                                         soliAdapter.notifyDataSetChanged();
                                                                     }
                                                                 }
                                                             }

        );
    }

    public void nuevo() {
        Map<String, Object> nuevo = new HashMap<String, Object>();
        nuevo.put("nombre", nombre.getText().toString());
        nuevo.put("fecha", fecha.getText().toString());

        nombre.setText("");
        fecha.setText("");

        db.collection("cumples").add(nuevo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("hola", "DocumentSnapshot added with ID: " + documentReference.getId());
                soliList.clear();
                load();
            }
        });
    }
}
