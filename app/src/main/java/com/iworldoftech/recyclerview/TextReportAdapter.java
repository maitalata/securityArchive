package com.iworldoftech.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.LinkedList;

public class TextReportAdapter extends
        RecyclerView.Adapter<TextReportAdapter.TextReportViewHolder> {

    private final LinkedList<String> mWordList;
    private LayoutInflater mInflater;
    private Context context;


    public TextReportAdapter(Context context,
                           LinkedList<String> wordList) {
        mInflater = LayoutInflater.from(context);
        this.mWordList = wordList;
    }

    @NonNull
    @Override
    public TextReportAdapter.TextReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.wordlist_item,
                parent, false);
        return new TextReportViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TextReportAdapter.TextReportViewHolder holder, int position) {
        String mCurrent = mWordList.get(position);
        holder.wordItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }

    class TextReportViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView wordItemView;
        final TextReportAdapter mAdapter;

        public TextReportViewHolder(View itemView, TextReportAdapter adapter) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.word);

            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();
            // Use that to access the affected item in mWordList.
            String element = mWordList.get(mPosition);
            // Change the word in the mWordList.
            //mWordList.set(mPosition, "Clicked! " + element);

            // Notify the adapter that the data has changed so it can
            // update the RecyclerView to display the data.
            //mAdapter.notifyDataSetChanged();

            FirebaseFirestore db = FirebaseFirestore.getInstance();;



            FirebaseStorage storage = FirebaseStorage.getInstance();
            // Create a storage reference from our app
            StorageReference storageRef = storage.getReference();

            // Create a reference with an initial file path and name
            StorageReference pathReference = storageRef.child(element);

            // FragmentManager manager = contegetSupportFragmentManager();

            AppCompatActivity activity = (AppCompatActivity) wordItemView.getContext();

            //new PicturePopUp().show(activity.getSupportFragmentManager(), PicturePopUp.TAG);

            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.text_report_viewer, null);
            final TextView edit = (TextView) view.findViewById(R.id.textReportContent);



            DocumentReference docRef = db.collection("security_report").document(element);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            edit.setText(document.get("report").toString());
                            //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            //Log.d(TAG, "No such document");
                        }
                    } else {
                        // Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });



            AlertDialog.Builder builder = new AlertDialog.Builder(wordItemView.getContext());
            builder.setMessage("MMM");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setView(view);
            builder.setPositiveButton("OK", (dialog, which) -> {} );
            builder.setCancelable(false);
            builder.create().show();

        }
    }

}