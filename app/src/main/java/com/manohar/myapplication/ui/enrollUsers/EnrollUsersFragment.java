package com.manohar.myapplication.ui.enrollUsers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.manohar.myapplication.OnSwipeTouchListener;
import com.manohar.myapplication.R;
import com.manohar.myapplication.model.UserModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class EnrollUsersFragment extends Fragment {

    final Calendar myCalendar = Calendar.getInstance();
    TextInputEditText firstname, lastname, dob, country, state, hometown, phno;
    RadioGroup gender;
    DatePickerDialog.OnDateSetListener date;
    MaterialButton addUser;
    MaterialTextView addphoto;
    View root;
    ImageView imageView;
    String age, imagedownloadurl;
    Uri imagePath;
    Boolean imageSelected = false;
    NavController navController;
    List<String> phonenumbers;
    ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_enroll, container, false);

        firstname = root.findViewById(R.id.firstname);
        lastname = root.findViewById(R.id.lastname);
        dob = root.findViewById(R.id.dob);
        country = root.findViewById(R.id.country);
        state = root.findViewById(R.id.state);
        hometown = root.findViewById(R.id.hometown);
        phno = root.findViewById(R.id.phonenumber);
        gender = root.findViewById(R.id.getgender);
        addUser = root.findViewById(R.id.adduser);
        addphoto = root.findViewById(R.id.addphoto);
        imageView = root.findViewById(R.id.profilephoto);
        phonenumbers = new ArrayList<String>();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Adding User");
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);


        Query query = FirebaseDatabase.getInstance().getReference().child("users");
        DatabaseReference usersx = FirebaseDatabase.getInstance().getReference().child("users");

        usersx.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot myshot : snapshot.getChildren()) {
                        String data = (String) myshot.child("phonenumber").getValue();
                        phonenumbers.add(data);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                phonenumbers.add(String.valueOf(snapshot.child("dob").getValue()));
                //Toast.makeText(requireActivity(), phonenumbers.get(0), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Your Profile Image"), 1);
            }
        });


        date  = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(requireActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });





        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int selectedId = gender.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) root.findViewById(selectedId);

                if (firstname.getText().toString().isEmpty() ||lastname.getText().toString().isEmpty() || radioButton.getText().toString().isEmpty()
                || dob.getText().toString().isEmpty() || country.getText().toString().isEmpty()|| state.getText().toString().isEmpty()
                || hometown.getText().toString().isEmpty() || phno.getText().toString().isEmpty())
                {
                    Toast.makeText(requireActivity(), "Fill all the info", Toast.LENGTH_SHORT).show();
                }
                else if (phno.getText().toString().length()>10 || phno.getText().toString().length()<10)
                {
                    Toast.makeText(requireActivity(), "Phone number must be of 10 digit", Toast.LENGTH_SHORT).show();
                }
                else if (!imageSelected)
                {
                    Toast.makeText(requireActivity(), "Please select a Display Picture", Toast.LENGTH_SHORT).show();
                }
                else if (phonenumbers.contains(phno.getText().toString()))
                {
                    Toast.makeText(requireActivity(), "This number is already registered by someone else. Use another", Toast.LENGTH_SHORT).show();
                }
                else if (imagePath.toString().length()>0)
                {


                    progressDialog.show();
                    try {
                        uploadImage(radioButton.getText().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


            }
        });


        View view = getActivity().findViewById(R.id.nav_host_fragment);
        TabLayout tabLayout = getActivity().findViewById(R.id.tabLayout);

        navController = Navigation.findNavController(requireActivity(), view.getId());

        ScrollView scrollView = root.findViewById(R.id.scrollview);
        scrollView.setOnTouchListener(new OnSwipeTouchListener(requireActivity()){

            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeRight() {
                navController.navigate(R.id.users);
                tabLayout.getTabAt(0).select();
            }
        });

    }

    private void uploadUserInfo(String radiobutton)
    {
        String fname = firstname.getText().toString();
        String lname = lastname.getText().toString();
        String dobdata= dob.getText().toString();
        String gender = radiobutton;
        String countryname = country.getText().toString();
        String statename = state.getText().toString();
        String hometownname = hometown.getText().toString();
        String phnumber = phno.getText().toString();

        UserModel userModel = new UserModel(fname,lname, dobdata, countryname,  statename,  hometownname,
                phnumber, Long.parseLong(age), gender , imagedownloadurl);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.push().setValue(userModel);

        Toast.makeText(requireActivity(), "User Added Successfully", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        navController.navigate(R.id.users);
    }

    private void uploadImage(String radiobutton) throws IOException {
            Bitmap bitmap = null;
            bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imagePath);


            boolean flag = false;
            float aspectRatio = bitmap.getWidth() /
                    (float) bitmap.getHeight();
            int width = 240;
            int height = Math.round(width / aspectRatio);

            bitmap = Bitmap.createScaledBitmap(
                    bitmap, width, height, false);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] data = baos.toByteArray();

            StorageReference uploader = FirebaseStorage.getInstance().getReference().child("users").child("profile").child(String.valueOf(Calendar.getInstance().get(Calendar.SECOND)));

            uploader.putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()) {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imagedownloadurl = uri.toString();
                                uploadUserInfo(radiobutton);

                            }
                        });
                    }
                }

            });

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        age = sdf.format(myCalendar.getTime());
        age = age.substring(6,10).trim();
        //Toast.makeText(requireActivity(), age, Toast.LENGTH_SHORT).show();
        int agecalculate = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(age) ;
        age = String.valueOf(agecalculate);
        dob.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imagePath = data.getData();
            try {
                Bitmap b = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imagePath);
                imageView.setImageBitmap(b);
                imageSelected = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}