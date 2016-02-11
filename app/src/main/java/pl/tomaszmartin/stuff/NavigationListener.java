package pl.tomaszmartin.stuff;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;

import pl.tomaszmartin.stuff.ui.MainActivity;
import pl.tomaszmartin.stuff.ui.SettingsActivity;

/**
 * Created by tomaszmartin on 27.07.2015.
 */

public class NavigationListener implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = NavigationListener.class.getSimpleName();
    private Activity activity;
    private DrawerLayout drawer;

    public NavigationListener(Activity activity, DrawerLayout drawer) {
        this.activity = activity;
        this.drawer = drawer;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.drawer_home:
                startActivity(MainActivity.class);
                return true;
            case R.id.drawer_settings:
                startActivity(SettingsActivity.class);
                return true;
        }
        return false;
    }

    private void startActivity(Class aClass) {
        drawer.closeDrawer(Gravity.LEFT);
        Intent intent = new Intent(activity, aClass);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}