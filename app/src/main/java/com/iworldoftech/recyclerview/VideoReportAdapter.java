package com.iworldoftech.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.LinkedList;

public class VideoReportAdapter extends
        RecyclerView.Adapter<VideoReportAdapter.VideoViewHolder> {

    private final LinkedList<String> videoList;
    private LayoutInflater mInflater;
    private Context context;


    public VideoReportAdapter(Context context,
                           LinkedList<String> wordList) {
        mInflater = LayoutInflater.from(context);
        this.videoList = wordList;
    }

    @NonNull
    @Override
    public VideoReportAdapter.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.wordlist_item,
                parent, false);
        return new VideoViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoReportAdapter.VideoViewHolder holder, int position) {
        String mCurrent = videoList.get(position);
        holder.wordItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView wordItemView;
        final VideoReportAdapter mAdapter;

        public VideoViewHolder(View itemView, VideoReportAdapter adapter) {
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
            String element = videoList.get(mPosition);
            // Change the word in the mWordList.
            //mWordList.set(mPosition, "Clicked! " + element);

            // Notify the adapter that the data has changed so it can
            // update the RecyclerView to display the data.
            //mAdapter.notifyDataSetChanged();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            // Create a storage reference from our app
            StorageReference storageRef = storage.getReference();

            // Create a reference with an initial file path and name
            StorageReference pathReference = storageRef.child(element);

            // FragmentManager manager = contegetSupportFragmentManager();

            AppCompatActivity activity = (AppCompatActivity) wordItemView.getContext();

            //new PicturePopUp().show(activity.getSupportFragmentManager(), PicturePopUp.TAG);

            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.video_viewer, null);
            final TextView edit = (TextView) view.findViewById(R.id.viewTextContent);
            final VideoView vd = (VideoView) view.findViewById(R.id.vdView);





            pathReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                @Override
                public void onSuccess(StorageMetadata storageMetadata) {
                    // Metadata now contains the metadata for 'images/forest.jpg'
                    edit.setText(storageMetadata.getCustomMetadata("videoDescription"));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(wordItemView.getContext());
            builder.setMessage("Video Report");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setView(view);
            builder.setPositiveButton("OK", (dialog, which) -> {} );
            builder.setCancelable(false);

            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri downloadUrl) {

                    vd.setVideoURI(downloadUrl);
                    MediaController controls = new MediaController(builder.getContext());
                    controls.setAnchorView(vd);
                    vd.setMediaController(controls);
                    vd.seekTo(1);
                    vd.start();
                    controls.show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });


            builder.create().show();

        }
    }

}