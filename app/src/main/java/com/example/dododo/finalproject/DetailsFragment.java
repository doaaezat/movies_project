package com.example.dododo.finalproject;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dododo.finalproject.data.Contract;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.dododo.finalproject.gson.*;


public class DetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {

    private List<vedios.ResultsEntity> trailer;

    public DetailsFragment() {
        setHasOptionsMenu(true);
    }

    private vedios videoObjects;
    ImageView imageview;
    TextView title;
    TextView overview;
    TextView rating;
    TextView release;
    ResultsEntity gsonobject;
    detailAdapter detailAdapter = new detailAdapter(getActivity());
    public int movieId;
    public ImageButton imagebutton;
    ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        View header = inflater.inflate(R.layout.header, null, false);

        Intent intent = getActivity().getIntent();
        lv = (ListView) rootView.findViewById(R.id.list);

        Bundle getIntentExtra = getActivity().getIntent().getExtras();
        if (getIntentExtra != null) {
            gsonobject = getIntentExtra.getParcelable("movieId");
        } else {
            Bundle getArgus = getArguments();
            gsonobject = getArgus.getParcelable("movieId");
        }


        title = (TextView) header.findViewById(R.id.title);
        title.setText(gsonobject.getTitle());
        overview = (TextView) header.findViewById(R.id.overview);
        overview.setText(gsonobject.getOverview());

        release = (TextView) header.findViewById(R.id.release_date);
        release.setText(gsonobject.getRelease_date());

        rating = (TextView) header.findViewById(R.id.rating);
        rating.setText(gsonobject.getVote_average() + "");

        imageview = (ImageView) header.findViewById(R.id.imageviewditals);
        String imageurl = gsonobject.getPoster_path();
        int dimens = (int) getActivity().getResources().getDimension(R.dimen.item_movie);
        Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w185" + imageurl).resize(dimens, dimens + 100).into(imageview);

        lv.addHeaderView(header);

        movieId = gsonobject.getId();
        getLoaderManager().initLoader(1, null, this);

        imagebutton = (ImageButton) header.findViewById(R.id.favorite);
        imagebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (gsonobject.getPoster_path() == null)
                    gsonobject.setPoster_path("empty");
                add_favorite(gsonobject.getId(), gsonobject.getOriginal_title(), gsonobject.getOverview(), String.valueOf(gsonobject.getVote_average()),
                        gsonobject.getRelease_date(), gsonobject.getPoster_path());

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //your code goes here .
                if (adapterView.getItemAtPosition(i) instanceof vedios.ResultsEntity) {
                    vedios.ResultsEntity video = (vedios.ResultsEntity) adapterView.getItemAtPosition(i);
                    Uri uri = Uri.parse("http://www." + video.getSite() + ".com/watch?v=" + video.getKey());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

            }
        });

        return rootView;
    }

/*    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuShare = menu.findItem(R.id.menu_item_share);
        ShareActionProvider shareAction = (ShareActionProvider)
                menuShare.getActionProvider();
        vedios.ResultsEntity resultsEntity = trailer.get(0);
        if (resultsEntity != null) {
            vedios.ResultsEntity video2 = resultsEntity;
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "http://www." + video2.getSite() + ".com/watch?v=" + video2.getKey());
            startActivity(shareIntent);
        }
    }*/


    //                if (lv.getItemAtPosition(0) instanceof vedios.ResultsEntity){
//                    vedios.ResultsEntity video2 = (vedios.ResultsEntity) lv.getItemAtPosition(i);
//                    Intent myintent = new Intent(Intent.ACTION_SEND);
//                    myintent.setType("text/plain");
//                    myintent.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
//                    myintent.putExtra(Intent.EXTRA_TEXT, "http://www." + video2.getSite() + ".com/watch?v=" + video2.getKey());
//                    startActivity(Intent.createChooser(myintent, "Share URL")); }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        Task task = new Task(getActivity());
        task.addUri("http://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=04db6a4e0e321dd1bec24ff22c995709");
        task.addUri("http://api.themoviedb.org/3/movie/" + movieId + "/reviews?api_key=04db6a4e0e321dd1bec24ff22c995709");
        return task;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (data != null) {
            if (data.contains(("&&"))) {
                String[] result = data.split("&&");
                String video = result[0];
                String reviews = result[1];
                Gson parser1 = new Gson();
                review reviewObject = parser1.fromJson(reviews, review.class);
                videoObjects = parser1.fromJson(video, vedios.class);
                trailer = videoObjects.getResults();
                com.example.dododo.finalproject.detailAdapter adapter = new detailAdapter(getActivity());
                adapter.addItem(reviewObject.getResults());
                adapter.addItem(videoObjects.getResults());
                lv.setAdapter(adapter);
                //lv.setOnItemClickListener(new AdapterView.OnItemClickListener());


            }

        }
    }
/*
    public class ListClickHandler implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
            // TODO Auto-generated method stub

//
        }
    }*/


    @Override
    public void onLoaderReset(Loader<String> loader) {

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        vedios.ResultsEntity resultsEntity = trailer.get(0);
        switch (id) {
            case R.id.menu_item_share:
                if (resultsEntity != null) {

                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    share.putExtra(Intent.EXTRA_SUBJECT, "Favorite movie");
                    share.putExtra(Intent.EXTRA_TEXT, "http://www." + resultsEntity.getSite() + ".com/watch?v=" + resultsEntity.getKey());

                    startActivity(share);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    long add_favorite(int id, String title, String overview, String rate, String relase_date, String image) {
        long favouraitId = 0;
        Uri uriFindByID = Contract.favouraitEntry.CONTENT_URI.buildUpon()
                .appendPath(String.valueOf(id)).build();
        Cursor favouraitCursor = getActivity().getContentResolver().query(
                uriFindByID,
                new String[]{Contract.favouraitEntry.ColumnMovieID, Contract.favouraitEntry.COLUMN_titel},
                Contract.favouraitEntry.ColumnMovieID + " = ?",
                new String[]{String.valueOf(id)},
                null);


        if (favouraitCursor.moveToFirst()) {
            int favouraitIdIndex = favouraitCursor.getColumnIndex(Contract.favouraitEntry.ColumnMovieID);
            favouraitId = favouraitCursor.getLong(favouraitIdIndex);
            Log.i("ADDFOURAIT", "Already exist" + favouraitId);

        } else {

            ContentValues favouraitValues = new ContentValues();
            favouraitValues.put(Contract.favouraitEntry.ColumnMovieID, id);
            favouraitValues.put(Contract.favouraitEntry.COLUMN_titel, title);
            favouraitValues.put(Contract.favouraitEntry.COLUMN_overview, overview);
            favouraitValues.put(Contract.favouraitEntry.COLUMN_rated, rate);
            favouraitValues.put(Contract.favouraitEntry.COLUMN_releasdate, relase_date);
            favouraitValues.put(Contract.favouraitEntry.COLUMN_image, image);


            Uri insertedUri = getActivity().getContentResolver().insert(
                    Contract.favouraitEntry.CONTENT_URI,
                    favouraitValues
            );
            favouraitId = ContentUris.parseId(insertedUri);
            Log.i("ADDFAVOURAIT", "inserted" + favouraitId);


        }
        favouraitCursor.close();
        return favouraitId;

    }

}
