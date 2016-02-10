package cse110w240t16.parket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by XiaoyuePu on 2/4/16.
 */
public class DetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);


    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
        finish();
    }
}

