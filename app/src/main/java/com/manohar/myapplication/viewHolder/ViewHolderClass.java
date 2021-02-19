package com.manohar.myapplication.viewHolder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.manohar.myapplication.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderClass extends RecyclerView.ViewHolder {

    View mView;
    ImageView imageView;
    public Context getContext() {return itemView.getContext();}
    public ViewHolderClass(View itemView)
    {

        super(itemView);
        mView= itemView;

    }

    public ViewHolderClass setDetails (Context ctx, String firstname, String lastname, Long age, String gender, String hometown, String image) {


        TextView tv2= mView.findViewById(R.id.title);
        tv2.setText(firstname.substring(0,1).toUpperCase()+firstname.substring(1)+" "+lastname.substring(0,1).toUpperCase()+lastname.substring(1));
        TextView textView = mView.findViewById(R.id.genderAgeCity);
        textView.setText(gender.substring(0,1).toUpperCase()+gender.substring(1)+ " | "+ age + " | " +hometown.substring(0,1).toUpperCase()+hometown.substring(1));
        imageView = mView.findViewById(R.id.image);
        if ( image!=null && !image.isEmpty())
        {
            Picasso.get().load(image)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {


                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(ctx, "Something Happend Wrong Uploader Image", Toast.LENGTH_LONG).show();
                        }
                    });

        }

        return null;
    }



}
