package com.example.android.songdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.songdetail.content.SongUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

     private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        // Set the toolbar as the app bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        /* *** Get recyclerview widget and attach the adapter to it *** */
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.song_list);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(SongUtils.SONG_ITEMS));

        if (findViewById(R.id.song_detail_container) != null) {
            mTwoPane = true;
        }
    }

    /* *** Adapter and ViewHolder *** */
    class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<SongUtils.Song> mValues;

        SimpleItemRecyclerViewAdapter(List<SongUtils.Song> items) {
            mValues = items;            // an array of songs 
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_content, parent, false);
            return new ViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            
            /* **** stuff data into the viewholder's widgets *** */
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(String.valueOf(position + 1));
            holder.mContentView.setText(mValues.get(position).song_title);
            
            /* **** Setup event handlers when user does things to the widgets in the viewholder *** */
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        
                        int selectedSong = holder.getAdapterPosition();
                       
                        SongDetailFragment fragment = SongDetailFragment.newInstance(selectedSong);
                        getSupportFragmentManager().beginTransaction().replace(R.id.song_detail_container, fragment).addToBackStack(null).commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, SongDetailActivity.class);
                        intent.putExtra(SongUtils.SONG_ID_KEY, holder.getAdapterPosition());
                        context.startActivity(intent);
                    }
                }
            });
        }
        
        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final View mView;
            final TextView mIdView;
            final TextView mContentView;
            SongUtils.Song mItem;

            ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}
