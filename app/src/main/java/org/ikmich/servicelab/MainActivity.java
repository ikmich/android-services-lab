package org.ikmich.servicelab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.ikmich.servicelab.databinding.ActivityMainBinding;
import org.ikmich.servicelab.services.MainService;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding bnd;
    Intent mainServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnd = DataBindingUtil.setContentView(this, R.layout.activity_main);

        new StartedServiceHelper().init();

        BoundServiceWithBinderHelper boundServiceWithBinderHelper =
                new BoundServiceWithBinderHelper(this);
        boundServiceWithBinderHelper.init();

        BoundServiceWithMessengerHelper boundServiceWithMessengerHelper =
                new BoundServiceWithMessengerHelper(this);
        boundServiceWithMessengerHelper.init();
    }

    class StartedServiceHelper {

        void init() {
            bnd.btnStartMainService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startMainService();
                }
            });

            bnd.btnStopMainService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mainServiceIntent != null) {
                        stopService(mainServiceIntent);
                    }
                }
            });
        }

        private void startMainService() {
            mainServiceIntent = new Intent(MainActivity.this, MainService.class);
            startService(mainServiceIntent);
        }
    }

}
