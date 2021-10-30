package com.arinoyu.util;

import com.arinoyu.DesAlgorithmApplication;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;

public class Utils {

    // 获取存储文件的路径
    public static String getFilesPath() {
        ApplicationHome home = new ApplicationHome(DesAlgorithmApplication.class);
        File jarFile = home.getSource();
        return jarFile.getParentFile().toString() + "/files";
    }
}
