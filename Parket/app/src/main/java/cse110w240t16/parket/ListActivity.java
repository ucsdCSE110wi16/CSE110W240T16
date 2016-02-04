package cse110w240t16.parket;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by XiaoyuePu on 2/4/16.
 */
public class ListActivity extends Activity{
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        button = (Button) findViewById(R.id.choose);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), DetailActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }
}
