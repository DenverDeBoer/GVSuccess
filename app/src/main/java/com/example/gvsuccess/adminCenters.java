package com.example.gvsuccess;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;

//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class adminCenters extends AppCompatActivity {

    private RecyclerView centerList;
    private Adapter adapter;
    private ArrayList<SuccessCenter> scList;
    private Context context;
    private DataAccess da;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_centers);

        //GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(this);
        centerList = findViewById(R.id.centerList);
        scList = new ArrayList<>();
        context = this;
        da = new DataAccess();

        Task<QuerySnapshot> centers = da.getCenters();
        centers.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            public void onSuccess(QuerySnapshot snapshot){
                for(QueryDocumentSnapshot doc : snapshot) {
                    if (doc.exists()) {
                        SuccessCenter c = doc.toObject(SuccessCenter.class);
                        c.setKey(doc.getId());
                        scList.add(c);
                    }
                }
                createCardView();
            }
        });
    }

    public void createCardView() {
        centerList.setLayoutManager(new LinearLayoutManager(context));
        adapter = new Adapter(context, scList);
        centerList.setAdapter(adapter);
        setupCardViewListeners();
    }

    private void setupCardViewListeners() {
        final Intent intent = new Intent(this, adminStudents.class);

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            public void onItemClick(int position) {
                SuccessCenter successCenter = scList.get(position);

                try {
                    email = getIntent().getExtras().getString("email");
                    da.incrementAvailable(email, successCenter.getKey());
                }catch(Exception e) {
                    Log.e("TUTOR INFORMATION", e.toString());
                }

                intent.putExtra("successCenter", successCenter);
                intent.putExtra("email", email);

                startActivity(intent);
            }
        });
    }
}
