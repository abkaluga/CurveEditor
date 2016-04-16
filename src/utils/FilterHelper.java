package utils;

import java.io.File;

/**
 * Created by Albert on 16.04.2016.
 */
class FilterHelper {

    static private FilterHelper instance = new FilterHelper();


    private FilterHelper() {

    }

    static FilterHelper getInstance() {
        return instance;
    }


    /*
 * Get the extension of a file.
 */
    String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
