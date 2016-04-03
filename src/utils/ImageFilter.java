package utils;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Arrays;

/**
 * Created by Albert on 20.03.2016.
 */
public class ImageFilter extends FileFilter {

    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()){
            return  true;
        }
        String[] accepted = {"bmp","jpeg", "jpg","gif","tiff","tif","png"};
        String ext = getExtension(f);
        return Arrays.asList(accepted).contains(ext);
    }

    @Override
    public String getDescription() {
        return "Image filter";
    }
}
