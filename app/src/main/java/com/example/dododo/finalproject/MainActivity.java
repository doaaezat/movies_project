package com.example.dododo.finalproject;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity
        implements MainActivityFragment.Callback {
    boolean twopane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (findViewById(R.id.container) != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new DetailsFragment());
            twopane = true;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //  String str = task.execute("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=f5a335aed31b96b3b82ccc2101bebc07").get();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {

        super.onStart();
        // The activity is about to become visible.
    }

    @Override
    protected void onResume() {

        super.onResume();
        // The activity has become visible (it is now "resumed").
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
    }

    @Override
    protected void onStop() {

        super.onStop();
        // The activity is no longer visible (it is now "stopped")
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        // The activity is about to be destroyed.
    }


    @Override
    public void onSelectedItem(gson.ResultsEntity movie) {
        if (!twopane) {
            Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
            intent.putExtra("movieId", movie);
            startActivity(intent);
        } else {
            Bundle arguments = new Bundle();
            arguments.putParcelable("movieId",movie);
            DetailsFragment detail = new DetailsFragment();
            detail.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.container,detail).commit();

        }
    }
}
