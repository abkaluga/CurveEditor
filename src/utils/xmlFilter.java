package utils;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Arrays;

/**
 * Created by Albert on 16.04.2016.
 */

public class XmlFilter extends FileFilter {


    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String[] accepted = {"xml"};
        String ext = FilterHelper.getInstance().getExtension(f);
        return Arrays.asList(accepted).contains(ext);
    }

    @Override
    public String getDescription() {
        return "Xml filter";
    }
}
