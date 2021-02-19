package com.manohar.myapplication.ui.users;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.manohar.myapplication.OnSwipeTouchListener;
import com.manohar.myapplication.R;
import com.manohar.myapplication.model.UserModel;
import com.manohar.myapplication.viewHolder.ViewHolderClass;

import java.util.ArrayList;
import java.util.Objects;

public class UsersFragment extends Fragment {

    private ArrayList<UserModel> arrayList;
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter<UserModel, ViewHolderClass> firebaseRecyclerAdapter;
    ProgressBar progressBar;
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_users, container, false);


        recyclerView = root.findViewById(R.id.usersrv);
        progressBar = root.findViewById(R.id.progress_circular);

        arrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        recyclerView.setAdapter(firebaseRecyclerAdapter);


        loadUsers();

        return root;
    }



    private void loadUsers()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        final Query query = databaseReference;

        FirebaseRecyclerOptions<UserModel> firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<UserModel>()
                        .setQuery(query, UserModel.class)
                        .setLifecycleOwner(this)
                        .build();

        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<UserModel, ViewHolderClass>(firebaseRecyclerOptions) {
                    @NonNull
                    @Override
                    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return new ViewHolderClass(LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_users, parent, false));
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderClass holder, int position, @NonNull UserModel model) {
                        holder.setDetails(requireActivity(), model.getFirstname(), model.getLastname(), model.getAge(), model.getGender(), model.getHometown(), model.getImage());
                        progressBar.setVisibility(View.GONE);

                        holder.itemView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    String refkey = getRef(position).getKey();
                                    DatabaseReference remove = FirebaseDatabase.getInstance().getReference().child("users").child(refkey);
                                    remove.removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            Toast.makeText(requireActivity(), "Removed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                catch (Exception e)
                                {

                                }


                            }
                        });

                    }

                };



        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getActivity().findViewById(R.id.nav_host_fragment);
        TabLayout tabLayout = getActivity().findViewById(R.id.tabLayout);

        NavController navController = Navigation.findNavController(requireActivity(), view.getId());

        recyclerView.setOnTouchListener(new OnSwipeTouchListener(requireActivity()){

            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeLeft() {
                navController.navigate(R.id.enroll);
                tabLayout.getTabAt(1).select();
            }
        });




    }
}