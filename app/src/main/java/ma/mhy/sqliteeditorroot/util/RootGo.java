package ma.mhy.sqliteeditorroot.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import java.io.File;

public class RootGo {
    private ProgressDialog progress_dialog;

    private Context mCtx ;
    /***
     * 其中is_root()判断是否已经具有了ROOT权限。
     * 只要/system/bin/su、/system/xbin/su这两个文件中有一个存在，
     * 就表明已经具有ROOT权限，如果两个都不存在，则不具有ROOT权限。
     * @return
     */
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

    // 获取ROOT权限.获取Android的ROOT权限其实很简单，只要在Runtime下执行命令"su"就可以了。
    public  void get_root() {

        if (is_root()) {
            Toast.makeText(mCtx, "已经具有ROOT权限!", Toast.LENGTH_LONG).show();
        } else {
            try {
                progress_dialog = ProgressDialog.show(mCtx,
                        "ROOT", "正在获取ROOT权限...", true, false);
                Runtime.getRuntime().exec("su");
            } catch (Exception e) {
                Toast.makeText(mCtx, "获取ROOT权限时出错!", Toast.LENGTH_LONG).show();
            }
        }

    }
}