package controler;

import model.*;
import model.Point;
import model.beziere.BeziereCurve;
import model.beziere.BeziereHornerCurve;
import model.beziere.BezieredeCastelCurve;
import model.beziere.RationalBeziereHornerCurve;
import model.viewModel.MainWindowModel;
import utils.NameGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Albert on 20.03.2016.
 */
public class MainWindowController {

    private final MainWindowModel model;

    public MainWindowController(MainWindowModel model) {
        this.model = model;
    }


    public void addPointToCurve() {
        if (model.getCurveModel().getSize() > 0) {
            ICurve curve = (ICurve) model.getCurveModel().getSelectedItem();
            IPoint point = new Point();
            point.setWeigh(model.getWeighModel().getNumber().floatValue());
            point.setX(model.getxModel().getNumber().intValue());
            point.setY(model.getyModel().getNumber().intValue());

            List<IPoint> points = new ArrayList<>(curve.getPoints());
            points.add(point);
            model.getPointModel().addElement(point);
            model.getPointModel().setSelectedItem(point);
            curve.setPoints(points);
            CurveUpdater.update((ICurve) model.getCurveModel().getSelectedItem(), model.isDirty());
            model.isDirty().compareAndSet(false, true);
        }

    }

    public void handleCurveChange() {
        ICurve curve = (ICurve) model.getCurveModel().getSelectedItem();
        model.getPointModel().removeAllElements();
        if (curve != null) {
            curve.getPoints().stream().forEach(model.getPointModel()::addElement);
        }
        model.getSelectedPoints().parallelStream().forEach(p -> p.setColor(Color.BLACK));
        model.getSelectedPoints().clear();
        model.isDirty().compareAndSet(false, true);
    }

    public void handleRemovePointFromCurve() {
        ICurve curve = (ICurve) model.getCurveModel().getSelectedItem();
        IPoint point = (IPoint) model.getPointModel().getSelectedItem();

        List<IPoint> points = new ArrayList<>(curve.getPoints());
        points.remove(point);
        model.getPointModel().removeElement(point);
        curve.setPoints(points);
        CurveUpdater.update((ICurve) model.getCurveModel().getSelectedItem(), model.isDirty());
        model.isDirty().compareAndSet(false, true);

    }

    public void handleAddCurve() {
        Optional<String> name = Optional.empty();
        Optional<List<IPoint>> points = Optional.empty();
        Color color = (Color) model.getColorModel().getSelectedItem();
        ICurve.CurveType type = (ICurve.CurveType) model.getCurveTypeModel().getSelectedItem();
        createCurve(name, points, color, type);
        handleCurveChange();
    }

    private void createCurve(Optional<String> name, Optional<List<IPoint>> points, Color color, ICurve.CurveType type) {
        ICurve curve = null;


        switch (type) {
            case NewtonInterpolated:
                curve = new NewtonInterpolated();
                break;
            case Chain:
                curve = new PolygonalChain();
                break;
            case CubicSpineInterpolated:
                curve = new CubicSpineInterpolated();
                break;
            case BeziereHorner:
                curve = new BeziereHornerCurve();
                break;
            case RationalBeziereHorner:
                curve = new RationalBeziereHornerCurve();
                break;
            case BezieredeCastel:
                curve = new BezieredeCastelCurve();
                break;
            case RationalBezieredeCastel:
                curve = new RationalBeziereHornerCurve();
                break;
            case BeziereInterpolated:
                curve = new BeziereInterpolated();
        }
        curve.setColor(color);
        if (name.isPresent()) {
            curve.setName(name.get());
        } else {
            NameGenerator.generateName(curve);
        }
        if (points.isPresent()) {
            curve.setPoints(points.get());
            CurveUpdater.update(curve, model.isDirty());
        } else {
            curve.setPoints(Collections.emptyList());
        }
        model.getCurveModel().addElement(curve);
        model.getCurveModel().setSelectedItem(curve);
    }

    public void handleTransformCurve() {
        ICurve selected = (ICurve) model.getCurveModel().getSelectedItem();
        Optional<String> name = Optional.of(selected.getName());
        Optional<List<IPoint>> points = Optional.of(new ArrayList<>(selected.getPoints()));
        Color color = selected.getColor();
        ICurve.CurveType type = (ICurve.CurveType) model.getCurveTypeModel().getSelectedItem();
        createCurve(name, points, color, type);
        model.getCurveModel().removeElement(selected);
        handleCurveChange();
    }

    public void handleRemoveCurve() {
        ICurve curve = (ICurve) model.getCurveModel().getSelectedItem();
        model.getCurveModel().removeElement(curve);
        handleCurveChange();
    }

    public void handleColorChange() {
        ICurve curve = (ICurve) model.getCurveModel().getSelectedItem();
        Color color = (Color) model.getColorModel().getSelectedItem();
        curve.setColor(color);
        model.isDirty().compareAndSet(false, true);
    }

    public void handlePointChange() {
        IPoint p = (IPoint) model.getPointModel().getSelectedItem();
        if (p != null && model.getLockSpinners().tryAcquire()) {
            model.getxModel().setValue(p.getX());
            model.getyModel().setValue(p.getY());
            model.getWeighModel().setValue(p.getWeigh());
            model.getLockSpinners().release();
        }

    }

    public void handlePointSpinnerChange() {
        IPoint point = (IPoint) model.getPointModel().getSelectedItem();
        if (point != null && model.getLockSpinners().tryAcquire()) {
            point.setX(model.getxModel().getNumber().intValue());
            point.setY(model.getyModel().getNumber().intValue());
            point.setWeigh(model.getWeighModel().getNumber().floatValue());
            model.isDirty().compareAndSet(false, true);
            model.getLockSpinners().release();
            CurveUpdater.update((ICurve) model.getCurveModel().getSelectedItem(), model.isDirty());
        }

    }

    public void riseBeziereDeegre() {
        BeziereCurve c = (BeziereCurve) model.getCurveModel().getSelectedItem();
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
        handleCurveChange();
        CurveUpdater.update(c, model.isDirty());
    }

    public void lowerDeegree() {
        BeziereCurve c = (BeziereCurve) model.getCurveModel().getSelectedItem();


        List<IPoint> oldPoints = c.getPoints();
        int n = oldPoints.size() - 1;
        int m = n + 1;
        List<IPoint> newPoints = new ArrayList<>(oldPoints.subList(0, n));


        if (n % 2 == 1) {
            newPoints.set(0, oldPoints.get(0));
            int h = (n - 1) / 2;
            for (int i = 1; i >= h; ++i) {
                IPoint p = new Point();
                IPoint oP = oldPoints.get(i);
                IPoint nP = newPoints.get(i - 1);
                p.setX((m * oP.getX() - i * nP.getX()) / (m - i));
                p.setY((m * oP.getY() - i * nP.getY()) / (m - i));
                p.setX((int) ((m * oP.getWeigh() - i * nP.getWeigh()) / (m - i)));
                newPoints.set(i, p);
            }
            newPoints.set(n - 1, oldPoints.get(n - 1));
            for (int i = n - 1; i >= h + 2; i--) {
                IPoint p = new Point();
                IPoint oP = oldPoints.get(i);
                IPoint nP = newPoints.get(i);
                p.setX((m * oP.getX() - (m - i) * nP.getX()) / i);
                p.setY((m * oP.getY() - (m - i) * nP.getY()) / i);
                p.setWeigh(((m * oP.getWeigh() - (m - i) * nP.getWeigh()) / i));
                newPoints.set(i - 1, p);
            }

        } else {
            newPoints.set(0, oldPoints.get(0));
            int h = (n) / 2;
            for (int i = 1; i >= h; ++i) {
                IPoint p = new Point();
                IPoint oP = oldPoints.get(i);
                IPoint nP = newPoints.get(i - 1);
                p.setX((m * oP.getX() - i * nP.getX()) / (m - i));
                p.setY((m * oP.getY() - i * nP.getY()) / (m - i));
                p.setX((int) ((m * oP.getWeigh() - i * nP.getWeigh()) / (m - i)));
                newPoints.set(i, p);
            }
            IPoint qP = newPoints.get(h);
            newPoints.set(n - 1, oldPoints.get(n - 1));
            for (int i = n - 1; i >= h + 1; i--) {
                IPoint p = new Point();
                IPoint oP = oldPoints.get(i);
                IPoint nP = newPoints.get(i);
                p.setX((m * oP.getX() - (m - i) * nP.getX()) / i);
                p.setY((m * oP.getY() - (m - i) * nP.getY()) / i);
                p.setWeigh(((m * oP.getWeigh() - (m - i) * nP.getWeigh()) / i));
                newPoints.set(i - 1, p);
            }
            IPoint hP = newPoints.get(h);
            hP.setX((hP.getX() + qP.getX()) / 2);
            hP.setY((hP.getY() + qP.getY()) / 2);
            hP.setWeigh((hP.getWeigh() + qP.getWeigh()) / 2);
        }
        c.setPoints(newPoints);
        handleCurveChange();
        CurveUpdater.update(c, model.isDirty());
    }
}
