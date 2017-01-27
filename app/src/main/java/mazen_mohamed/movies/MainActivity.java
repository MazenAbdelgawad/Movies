package mazen_mohamed.movies;


import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        // The detail container view will be present only in the
        // large-screen layouts
        // If this view is present, then the
        // activity should be in two-pane mode.
        if (findViewById(R.id.detail_container) != null)
        {
            mTwoPane = true;

        }


        ListItemsFragment listitemsfragment=new ListItemsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.item_container, listitemsfragment).commit();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.Grid:
            {
                Toast.makeText(getApplicationContext(), "Grid", Toast.LENGTH_LONG).show();
            }
            break;
            case R.id.OneCard:
            {
                Toast.makeText(getApplicationContext(), "OneCard", Toast.LENGTH_LONG).show();
            }

            case R.id.Refresh:
            {
                Toast.makeText(getApplicationContext(), "Refresh", Toast.LENGTH_LONG).show();
            }
            break;
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
