package controler;

import model.IPoint;
import model.Point;

import java.util.List;

/**
 * Created by Albert on 04.06.2016.
 */
public class BeziereSplitter {
    private static BeziereSplitter ourInstance = new BeziereSplitter();

    private BeziereSplitter() {
    }

    public static BeziereSplitter getInstance() {
        return ourInstance;
    }

    public void split(List<IPoint> orginal, double splitPoint, List<IPoint> l, List<IPoint> r) {
        l.clear();
        r.clear();
        l.addAll(orginal);
        r.add(0, new Point(l.get(0)));
        for (int j = 1; j < orginal.size(); ++j) {

            for (int i = 0; i < orginal.size() - j; ++i) {
                double s = splitPoint;
                l.get(i).setWeigh((float) ((1.0 - splitPoint) * getW(l, i) + splitPoint * getW(l, i + 1)));
                s = (s * getW(l, i + 1)) / getW(l, i);
                l.get(i).setX((int) ((1 - s) * l.get(i).getX() + s * l.get(i + 1).getX()));
                l.get(i).setY((int) ((1 - s) * l.get(i).getY() + s * l.get(i + 1).getY()));
            }
            r.add(j, new Point(l.get(0)));

        }
    }

    private float getW(List<IPoint> list, int i) {
        return list.get(i).getWeigh();
    }
}
