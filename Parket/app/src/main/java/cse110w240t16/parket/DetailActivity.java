package cse110w240t16.parket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by XiaoyuePu on 2/4/16.
 */
public class DetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);

        Button goBackBtn;
        goBackBtn = (Button) findViewById(R.id.returnBtn);
        goBackBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), ListActivity.class);
                startActivityForResult(myIntent, 0);
                //Intent result = new Intent();
                //setResult(RESULT_OK, result);
                finish();
            }
        });
    }
}

