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
                finish();
            }
        });
    }
}


/**
public class ListActivity extends AppCompatActivity{

    private ArrayList<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ListView lv = (ListView) findViewById(R.id.list)
    }

    private class MyListAdapter extends ArrayAdapter<string> {
        private int layout;
        public MyListAdapter(Context context, int resource, List<string> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                //viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.)
                viewHolder.button = (Button) convertView.findViewById(R.id.show);
                viewHolder.button.setOnclickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getContent(),)

                    }
                });
                convertView.setTag(viewHolder);
            }
            else{
                mainViewholder = (ViewHolder) convertView.getTag();
                //mainViewholder.title.setText(getItem(position));
            }
            return convertView;

        }
    }

    public class ViewHolder {
        //ImageView thumbnail;
        //TextView title;
        Button button;

    }
}
**/