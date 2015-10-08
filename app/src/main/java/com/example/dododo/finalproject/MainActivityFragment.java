package com.example.dododo.finalproject;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.dododo.finalproject.data.Contract;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {

    private mainAdapter myAdapter;
    GridView gridview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridview = (GridView) rootView.findViewById(R.id.idofgridview);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                gson.ResultsEntity gsonobj = new gson.ResultsEntity();

                gsonobj = (gson.ResultsEntity) myAdapter.getItem(position);

                ((Callback) getActivity()).onSelectedItem(gsonobj);


            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    private void updateView(String Str1) {
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = sharedPrefs.getString(getString(R.string.pref_units_key), getString(R.string.pref_most_popular));

        if (sortBy.equals(getString(R.string.pref_highest_rated))) {

            List<gson.ResultsEntity> listAdapter1 = new ArrayList<>();
            if (Str1 != null) {
                gson gsonobject1 = new gson();
                Gson parser1 = new Gson();
                gsonobject1 = parser1.fromJson(Str1, gson.class);
                listAdapter1 = gsonobject1.getResults();
                 /* for (gson.ResultsEntity object : listAdapter) {
                      int idDeleted =  getActivity().getContentResolver().delete(Contract.highrateEntry.CONTENT_URI,
                              Contract.highrateEntry.COLUMN_image+"=?", new String[]{object.getPoster_path()});
                      Log.i("DELETE","deleted "+idDeleted);
                  }*/

                for (gson.ResultsEntity object1 : listAdapter1) {
                    if (object1.getPoster_path() == null)
                        object1.setPoster_path("empty");
                    add_highrate(object1.getId(), object1.getOriginal_title(), object1.getOverview(), String.valueOf(object1.getVote_average()),
                            object1.getRelease_date(), object1.getPoster_path());
                }
            } else {
                listAdapter1 = retrieve_highrate();
            }
            myAdapter = new mainAdapter(getActivity(), listAdapter1);
            gridview.setAdapter(myAdapter);

        }

        if (sortBy.equals(getString(R.string.pref_most_popular))) {

            List<gson.ResultsEntity> listAdapter = new ArrayList<>();
            if (Str1 != null) {
                gson gsonobject = new gson();
                Gson parser = new Gson();
                gsonobject = parser.fromJson(Str1, gson.class);
                listAdapter = gsonobject.getResults();
                 /* for (gson.ResultsEntity object : listAdapter) {
                      int idDeleted =  getActivity().getContentResolver().delete(Contract.mostpopEntry.CONTENT_URI,
                              Contract.mostpopEntry.COLUMN_image+"=?", new String[]{object.getPoster_path()});
                      Log.i("DELETE","deleted "+idDeleted);
                  }*/
                for (gson.ResultsEntity object : listAdapter) {
                    add_mostpop(object.getId(), object.getOriginal_title(), object.getOverview(), String.valueOf(object.getVote_average()),
                            object.getRelease_date(), object.getPoster_path());
                }
            } else {
                listAdapter = retrieve_mostpop();
            }
            myAdapter = new mainAdapter(getActivity(), listAdapter);
            gridview.setAdapter(myAdapter);

        }

        if (sortBy.equals(getString(R.string.pref_favourait))) {
if(Str1==null) {
    List<gson.ResultsEntity> listAdapter = new ArrayList<>();
    listAdapter = retrieve_favorite();
    myAdapter = new mainAdapter(getActivity(), listAdapter);
    gridview.setAdapter(myAdapter);
}
        }
    }


    long add_highrate(int id, String title, String overview, String rate, String relase_date, String image) {
        long highrateId = 0;
        Uri uriFindByID = Contract.highrateEntry.CONTENT_URI.buildUpon()
                .appendPath(String.valueOf(id)).build();
        Cursor highrateCursor = getActivity().getContentResolver().query(
                uriFindByID,
                new String[]{Contract.highrateEntry.ColumnMovieID, Contract.highrateEntry.COLUMN_titel},
                Contract.highrateEntry.ColumnMovieID + " = ?",
                new String[]{String.valueOf(id)},
                null);


        if (highrateCursor.moveToFirst()) {
            int highrateIdIndex = highrateCursor.getColumnIndex(Contract.highrateEntry.ColumnMovieID);
            highrateId = highrateCursor.getLong(highrateIdIndex);
            Log.i("ADDhighrate", "Already exist" + highrateId);

        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.


            ContentValues highrateValues = new ContentValues();
            highrateValues.put(Contract.highrateEntry.ColumnMovieID, id);
            highrateValues.put(Contract.highrateEntry.COLUMN_titel, title);
            highrateValues.put(Contract.highrateEntry.COLUMN_overview, overview);
            highrateValues.put(Contract.highrateEntry.COLUMN_rated, rate);
            highrateValues.put(Contract.highrateEntry.COLUMN_releasdate, relase_date);
            highrateValues.put(Contract.highrateEntry.COLUMN_image, image);


            Uri insertedUri = getActivity().getContentResolver().insert(
                    Contract.highrateEntry.CONTENT_URI,
                    highrateValues
            );
            highrateId = ContentUris.parseId(insertedUri);
            Log.i("ADDMOSTPOP", "inserted" + highrateId);


        }
        highrateCursor.close();
        return highrateId;

    }


    long add_mostpop(int id, String title, String overview, String rate, String relase_date, String image) {
        long mostpopId = 0;
        Uri uriFindByID = Contract.mostpopEntry.CONTENT_URI.buildUpon()
                .appendPath(String.valueOf(id)).build();
        Cursor mostpopCursor = getActivity().getContentResolver().query(
                uriFindByID,
                new String[]{Contract.mostpopEntry.ColumnMovieID, Contract.mostpopEntry.COLUMN_titel},
                Contract.mostpopEntry.ColumnMovieID + " = ?",
                new String[]{String.valueOf(id)},
                null);


        if (mostpopCursor.moveToFirst()) {
            int mostpopIdIndex = mostpopCursor.getColumnIndex(Contract.mostpopEntry.ColumnMovieID);
            mostpopId = mostpopCursor.getLong(mostpopIdIndex);
            Log.i("ADDMOSTPOP", "Already exist" + mostpopId);

        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.


            ContentValues mostpopValues = new ContentValues();
            mostpopValues.put(Contract.mostpopEntry.ColumnMovieID, id);
            mostpopValues.put(Contract.mostpopEntry.COLUMN_titel, title);
            mostpopValues.put(Contract.mostpopEntry.COLUMN_overview, overview);
            mostpopValues.put(Contract.mostpopEntry.COLUMN_rated, rate);
            mostpopValues.put(Contract.mostpopEntry.COLUMN_releasdate, relase_date);
            mostpopValues.put(Contract.mostpopEntry.COLUMN_image, image);


            Uri insertedUri = getActivity().getContentResolver().insert(
                    Contract.mostpopEntry.CONTENT_URI,
                    mostpopValues
            );
            mostpopId = ContentUris.parseId(insertedUri);
            Log.i("ADDMOSTPOP", "inserted" + mostpopId);


        }
        mostpopCursor.close();
        return mostpopId;

    }


    List<gson.ResultsEntity> retrieve_mostpop() {

        Cursor mostpopCursor = getActivity().getContentResolver().query(
                Contract.mostpopEntry.CONTENT_URI
                , new String[]{Contract.mostpopEntry.COLUMN_titel,
                        Contract.mostpopEntry.COLUMN_image, Contract.mostpopEntry.COLUMN_overview, Contract.mostpopEntry.COLUMN_releasdate
                        , Contract.mostpopEntry.COLUMN_rated}
                , null, null, null);


        List<gson.ResultsEntity> gsonlist = new ArrayList<>();
        if (mostpopCursor.moveToFirst()) {
            int id = 0;
            while (mostpopCursor.moveToNext()) {
                gson.ResultsEntity gsonObject = new gson.ResultsEntity();
                gsonObject.setOriginal_title(mostpopCursor.getString(mostpopCursor.getColumnIndex(Contract.mostpopEntry.COLUMN_titel)));
                gsonObject.setOverview(mostpopCursor.getString(mostpopCursor.getColumnIndex(Contract.mostpopEntry.COLUMN_overview)));
                gsonObject.setPoster_path(mostpopCursor.getString(mostpopCursor.getColumnIndex(Contract.mostpopEntry.COLUMN_image)));
                gsonObject.setRelease_date(mostpopCursor.getString(mostpopCursor.getColumnIndex(Contract.mostpopEntry.COLUMN_releasdate)));
                gsonObject.setVote_average(mostpopCursor.getDouble(mostpopCursor.getColumnIndex(Contract.mostpopEntry.COLUMN_rated)));
                gsonlist.add(gsonObject);
                Log.i("RETRIEVE", gsonObject.getPoster_path());
                id++;
            }
        } else
            Log.i("RETRIEVE", "empty db");

        return gsonlist;


    }

    List<gson.ResultsEntity> retrieve_favorite() {

        Cursor favouraitCursor = getActivity().getContentResolver().query(
                Contract.favouraitEntry.CONTENT_URI
                , new String[]{Contract.favouraitEntry.COLUMN_titel,
                        Contract.favouraitEntry.COLUMN_image, Contract.favouraitEntry.COLUMN_overview, Contract.favouraitEntry.COLUMN_releasdate
                        , Contract.favouraitEntry.COLUMN_rated}
                , null, null, null);


        List<gson.ResultsEntity> gsonlist = new ArrayList<>();
        if (favouraitCursor.moveToFirst()) {
            int id = 0;
            while (favouraitCursor.moveToNext()) {
                gson.ResultsEntity gsonObject = new gson.ResultsEntity();
                gsonObject.setOriginal_title(favouraitCursor.getString(favouraitCursor.getColumnIndex(Contract.favouraitEntry.COLUMN_titel)));
                gsonObject.setOverview(favouraitCursor.getString(favouraitCursor.getColumnIndex(Contract.favouraitEntry.COLUMN_overview)));
                gsonObject.setPoster_path(favouraitCursor.getString(favouraitCursor.getColumnIndex(Contract.favouraitEntry.COLUMN_image)));
                gsonObject.setRelease_date(favouraitCursor.getString(favouraitCursor.getColumnIndex(Contract.favouraitEntry.COLUMN_releasdate)));
                gsonObject.setVote_average(favouraitCursor.getDouble(favouraitCursor.getColumnIndex(Contract.favouraitEntry.COLUMN_rated)));
                gsonlist.add(gsonObject);
                Log.i("RETRIEVE", gsonObject.getPoster_path());
                id++;
            }
        } else
            Log.i("RETRIEVE", "empty db");

        return gsonlist;


    }

    List<gson.ResultsEntity> retrieve_highrate() {

        Cursor highrateCursor = getActivity().getContentResolver().query(
                Contract.highrateEntry.CONTENT_URI
                , new String[]{Contract.highrateEntry.COLUMN_titel,
                        Contract.highrateEntry.COLUMN_image, Contract.highrateEntry.COLUMN_overview, Contract.highrateEntry.COLUMN_releasdate
                        , Contract.highrateEntry.COLUMN_rated}
                , null, null, null);


        List<gson.ResultsEntity> gsonlist = new ArrayList<>();
        if (highrateCursor.moveToFirst()) {

            while (highrateCursor.moveToNext()) {
                gson.ResultsEntity gsonObject = new gson.ResultsEntity();
                gsonObject.setOriginal_title(highrateCursor.getString(highrateCursor.getColumnIndex(Contract.highrateEntry.COLUMN_titel)));
                gsonObject.setOverview(highrateCursor.getString(highrateCursor.getColumnIndex(Contract.highrateEntry.COLUMN_overview)));
                gsonObject.setPoster_path(highrateCursor.getString(highrateCursor.getColumnIndex(Contract.highrateEntry.COLUMN_image)));
                gsonObject.setRelease_date(highrateCursor.getString(highrateCursor.getColumnIndex(Contract.highrateEntry.COLUMN_releasdate)));
                gsonObject.setVote_average(highrateCursor.getDouble(highrateCursor.getColumnIndex(Contract.highrateEntry.COLUMN_rated)));
                gsonlist.add(gsonObject);
                Log.i("RETRIEVE", gsonObject.getPoster_path());

            }
        } else
            Log.i("RETRIEVE", "empty db");

        return gsonlist;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        Task task = new Task(getActivity());
        ;
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = sharedPrefs.getString(getString(R.string.pref_units_key), getString(R.string.pref_most_popular));
        if (sortBy.equals(getString(R.string.pref_highest_rated))) {

            task.addUri("http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=04db6a4e0e321dd1bec24ff22c995709");
        }
        if (sortBy.equals(getString(R.string.pref_most_popular))) {
            task.addUri("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=04db6a4e0e321dd1bec24ff22c995709");
        }
        if (sortBy.equals(getString(R.string.pref_favourait))) {
            task.addUri("");
        }
        return task;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        updateView(data);

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        data.moveToFirst();
//        StringBuilder res = new StringBuilder();
//        while (!data.isAfterLast()) {
//            res.append("\n" + data.getString(21) + "-" + data.getString(22));
//
//            data.moveToNext();
//        }
//      //  resTextView.setText(res);
//
//    }


    public interface Callback {
        void onSelectedItem(gson.ResultsEntity movie);
    }


}
