package cse110w240t16.parket;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by XiaoyuePu on 2/4/16.
 */
public class DetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);

        TextView name = (TextView) findViewById(R.id.textName);
//        name.setText();
        TextView address = (TextView) findViewById(R.id.textAddress);
//        address.setText();
        TextView distance = (TextView) findViewById(R.id.textDistance);
//        distance.setText();
        TextView time = (TextView) findViewById(R.id.textTime);
//        time.setText();
        TextView availability = (TextView) findViewById(R.id.textAvail);
//        availability.setText();

    }


}
