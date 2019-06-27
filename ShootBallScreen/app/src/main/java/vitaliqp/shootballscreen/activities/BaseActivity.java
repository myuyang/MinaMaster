package vitaliqp.shootballscreen.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qp
 */
public class BaseActivity extends AppCompatActivity {

    private long lastClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    protected boolean isFastClick() {

        long now = System.currentTimeMillis();
        boolean flag = true;
        if (now - lastClick >= 1000) {
            flag = false;
        }
        lastClick = now;
        return flag;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    protected static class ActivityCollector {

        private static List<Activity> activities = new ArrayList<>();

        private static void addActivity(Activity activity) {
            activities.add(activity);
        }

        private static void removeActivity(Activity activity) {

            activities.remove(activity);
        }

        public static void finishAll() {
            for (Activity activity : activities) {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
        }
    }
}
