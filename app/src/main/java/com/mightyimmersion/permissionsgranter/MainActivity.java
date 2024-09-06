package com.mightyimmersion.permissionsgranter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Dev and prod package names just for testing.
        grantRequestInstallPackagesPermission("com.mightyimmersion.mightyplatform.adminapp.prod.nohome");
        grantRequestInstallPackagesPermission("com.mightyimmersion.mightyplatform.adminapp.dev.nohome");
        grantRequestInstallPackagesPermission("com.mightyimmersion.mightyplatform.adminapp.prod.pico2");
        grantRequestInstallPackagesPermission("com.mightyimmersion.mightyplatform.adminapp.dev.pico2");
    }

    private void grantRequestInstallPackagesPermission(String packageName) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Log.v("MIGHTY_LOG", "PermissionsGranter: Not granting permission. SDK version is less than Oreo.");
            return;
        }

        try {
            Process p = Runtime.getRuntime().exec("appops set " + packageName + " REQUEST_INSTALL_PACKAGES " + " allow");
            int exitCode = p.waitFor();
            if (exitCode != 0) {
                Log.v("MIGHTY_LOG", "PermissionsGranter: Failed to grant permission. Exit code: " + exitCode);
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String line;
                while ((line = errorReader.readLine()) != null) {
                    Log.v("MIGHTY_LOG", "PermissionsGranter: " + line);
                }
            }
        } catch (Exception e) {
            Log.e("MIGHTY_LOG", "PermissionsGranter: Failed to set appops permission: ", e);
        }
    }
}
