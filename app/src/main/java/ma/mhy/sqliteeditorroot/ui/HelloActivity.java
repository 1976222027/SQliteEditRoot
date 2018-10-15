package ma.mhy.sqliteeditorroot.ui;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;

import ma.mhy.sqliteeditorroot.BuildConfig;
import ma.mhy.sqliteeditorroot.R;
import ma.mhy.sqliteeditorroot.util.PackUtils;
import ma.mhy.sqliteeditorroot.util.RequstRoot;

//import androidx.annotation.Nullable;


public class HelloActivity extends AppCompatActivity {
    ProgressDialog progress_dialog;
    String tag = "mhy";
    TextView tvVer, tvShow;

//String apkRoot = "chmod 777 " + getPackageCodePath();//getPackageCodePath()来获得当前应用程序对应的 apk 文件的路径

    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     *
     * @return 应用程序是/否获取Root权限
     */
    public static boolean upgradeRootPermission(String pkgCodePath) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod 777 " + pkgCodePath;
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }

    // 判断是否具有ROOT权限
    public static boolean is_root() {

        boolean res = false;

        try {
            if ((!new File("/system/bin/su").exists()) &&
                    (!new File("/system/xbin/su").exists())) {
                res = false;
            } else {
                res = true;
            }
            ;
        } catch (Exception e) {

        }
        return res;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //当前应用的代码执行目录
        // upgradeRootPermission(getPackageCodePath());
        // boolean b = RootCommand(apkRoot);//给这个路径777权限
        // Log.v(tag,"获取Root权限:"+b);
        //检测设备是否root 并获取root权限
        get_root();

        tvVer = findViewById(R.id.tv_ver);
        tvVer.setText(String.format(getResources().getString(R.string.version_show), PackUtils.getVersionName(this), PackUtils.getVersionCode(this)));
//tvVer.setText(PackUtils.getVersionName(this)+PackUtils.getVersionCode(this));
        tvShow = findViewById(R.id.tv_show);
        tvShow.setText(String.format(getResources().getString(R.string.version_time), BuildConfig.apkBuildTime));
        findViewById(R.id.action_ok).setOnClickListener(v ->
                new AlertDialog.Builder(HelloActivity.this)
                        .setCancelable(false)
                        .setMessage(R.string.app_notice)
                        .setPositiveButton(R.string.action_know, (dialog, which) -> finish())
                        .create()
                        .show());
    }

    // 获取ROOT权限.获取Android的ROOT权限其实很简单，只要在Runtime下执行命令"su"就可以了。
    public void get_root() {

        if (is_root()) {
            Toast.makeText(this, "请授予ROOT权限!", Toast.LENGTH_LONG).show();

            //请求root权限
            String apkRoot = "chmod 777 " + getPackageCodePath();
            RequstRoot.RootCommand(apkRoot);//

        } else {
            try {
                progress_dialog = ProgressDialog.show(this,
                        "ROOT", "正在获取ROOT权限...", true, false);
                Runtime.getRuntime().exec("su");
            } catch (Exception e) {
                Toast.makeText(this, "获取ROOT权限时出错!", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     *
     * @param ：String apkRoot="chmod 777 "+getPackageCodePath(); RootCommand(apkRoot);
     * @return 应用程序是/否获取Root权限
     */
//    public boolean RootCommand(String command) {
//        Process process = null;
//        DataOutputStream os = null;
//        DataInputStream is = null;
//        try {
//            process = Runtime.getRuntime().exec("su");
//            os = new DataOutputStream(process.getOutputStream());
//            os.writeBytes(command + "\n");
//            os.writeBytes("exit\n");
//            os.flush();
//            int aa = process.waitFor();
//            Log.w(tag,"waitFor():"+aa);
//            is = new DataInputStream(process.getInputStream());
//            byte[] buffer = new byte[is.available()];
//            Log.d(tag,"大小"+buffer.length);
//            is.read(buffer);
//            String out = new String(buffer);
//            Log.e(tag,"返回:"+out);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(tag,tag + "205:\n" + e);
//            return false;
//        } finally {
//            try {
//                if (os != null) {
//                    os.close();
//                }
//                if (is != null) {
//                    is.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e(tag,tag + "217:\n" + e);
//            }
//            process.destroy();
//        }
//        Log.d(tag,tag + "222 SUCCESS");
//        return true;
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hello, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse("https://github.com/1976222027/SQLiteEditRoot"))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        return true;
    }

    @Override
    protected void onStop() {
        disableSelf();
        super.onStop();
    }

    private void disableSelf() {
        getPackageManager().setComponentEnabledSetting(
                new ComponentName(this, this.getClass()),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 0);
    }
}
