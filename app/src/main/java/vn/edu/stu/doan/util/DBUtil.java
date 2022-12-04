package vn.edu.stu.doan.util;

import android.app.Activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DBUtil {
    public static void copyDBFileFromAssets(Activity context, String dbName, String dbPathSuffix) {
        File dbFile = context.getDatabasePath(dbName);
        if (!dbFile.exists()) {
            File dbDir = new File(context.getApplicationInfo().dataDir + dbPathSuffix);
            if (!dbDir.exists()) {
                dbDir.mkdir();
            }
            try {
                InputStream is = context.getAssets().open(dbName);
                String outputFilePath = context.getApplicationInfo().dataDir + dbPathSuffix + dbName;
                OutputStream os = new FileOutputStream(outputFilePath);
                byte[] buffer = new byte[1024];
                int length=0;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
