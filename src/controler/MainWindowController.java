package controler;

import model.*;
import model.Point;
import model.viewModel.MainWindowModel;
import utils.NameGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static model.ICurve.CurveType.Beziere;
import static model.ICurve.CurveType.Chain;
import static model.ICurve.CurveType.RationalBeziere;

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

            List<IPoint> points = curve.getPoints();
            points.add(point);
            model.getPointModel().addElement(point);
            model.getPointModel().setSelectedItem(point);
            // handlePointChange();
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

        List<IPoint> points = curve.getPoints();
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
            case Beziere:
                curve = new BeziereCurve();
                break;
            case RationalBeziere:
                curve = new RationalBeziereCurve();
                break;
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

}
