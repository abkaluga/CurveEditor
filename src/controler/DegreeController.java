package controler;

import model.ICurve;
import model.IPoint;
import model.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Albert on 04.06.2016.
 */
public class DegreeController {
    private static final DegreeController ourInstance = new DegreeController();

    private DegreeController() {
    }

    public static DegreeController getInstance() {
        return ourInstance;
    }

    public void riseDegree(ICurve c) {
        List<IPoint> newPoints = new ArrayList<>(c.getPoints());
        double m = newPoints.size() + 1;
        newPoints.add(c.getPoints().get(c.getPoints().size() - 1));
        for (int i = c.getPoints().size(); i > 0; --i) {
            IPoint p = new Point();
            double x = newPoints.get(i).getX(), y = newPoints.get(i).getY(), w = newPoints.get(i).getWeigh();
            double x1 = newPoints.get(i - 1).getX(), y1 = newPoints.get(i - 1).getY(), w1 = newPoints.get(i - 1).getWeigh();
            p.setX((int) ((i * x1 + (m - i) * x) / m));
            p.setY((int) ((i * y1 + (m - i) * y) / m));
            p.setWeigh((float) ((i * w1 + (m - i) * w) / m));
            newPoints.set(i, p);
        }
        c.setPoints(newPoints);
    }

    public void lowerDegree(ICurve c) {
        List<IPoint> oldPoints = c.getPoints();
        int n = oldPoints.size() - 1;
        int m = n + 1;
        List<IPoint> newPoints = new ArrayList<>(oldPoints);
        Collections.fill(newPoints, null);


        if ((n & 1) == 1) {
            newPoints.set(0, oldPoints.get(0));
            int h = (n - 1) / 2;
            for (int i = 1; i <= h; ++i) {
                firstPhase(oldPoints, m, newPoints, i);
            }
            newPoints.set(n, oldPoints.get(n));
            for (int i = n; i >= h + 2; i--) {
                secondPhase(oldPoints, m, newPoints, i);
            }

        } else {
            newPoints.set(0, oldPoints.get(0));
            int h = (n) / 2;
            for (int i = 1; i <= h; ++i) {
                firstPhase(oldPoints, m, newPoints, i);
            }
            IPoint qP = newPoints.get(h);
            newPoints.set(n, oldPoints.get(n));
            for (int i = n; i >= h + 1; i--) {
                secondPhase(oldPoints, m, newPoints, i);
            }
            IPoint hP = newPoints.get(h);
            hP.setX((hP.getX() + qP.getX()) / 2);
            hP.setY((hP.getY() + qP.getY()) / 2);
            hP.setWeigh((hP.getWeigh() + qP.getWeigh()) / 2);
        }
        newPoints.remove(n);
        c.setPoints(newPoints);
    }

    private void secondPhase(List<IPoint> oldPoints, int m, List<IPoint> newPoints, int i) {
        IPoint p = new Point();
        int opX = oldPoints.get(i).getX(), opY = oldPoints.get(i).getY();
        float opW = oldPoints.get(i).getWeigh();
        int npX = newPoints.get(i).getX(), npY = newPoints.get(i).getY();
        float npW = newPoints.get(i).getWeigh();
        p.setX((m * opX - (m - i) * npX) / i);
        p.setY((m * opY - (m - i) * npY) / i);
        p.setWeigh(((m * opW - (m - i) * npW) / i));
        newPoints.set(i - 1, p);
    }

    private void firstPhase(List<IPoint> oldPoints, int m, List<IPoint> newPoints, int i) {
        IPoint p = new Point();
        int opX = oldPoints.get(i).getX(), opY = oldPoints.get(i).getY();
        float opW = oldPoints.get(i).getWeigh();
        int npX = newPoints.get(i - 1).getX(), npY = newPoints.get(i - 1).getY();
        float npW = newPoints.get(i - 1).getWeigh();
        p.setX((m * opX - i * npX) / (m - i));
        p.setY((m * opY - i * npY) / (m - i));
        p.setWeigh((int) ((m * opW - i * npW) / (m - i)));
        newPoints.set(i, p);
    }
}
