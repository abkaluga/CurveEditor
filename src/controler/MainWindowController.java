package controler;

import model.*;
import model.Point;
import utils.NameGenerator;
import view.viewModel.MainWindowModel;

import java.awt.*;
import java.util.List;
import java.util.concurrent.Semaphore;

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
            CurveUpdater.update((ICurve) model.getCurveModel().getSelectedItem());
            model.isDirty().compareAndSet(false,true);
        }

    }

    public void handleCurveChange() {
        ICurve curve = (ICurve) model.getCurveModel().getSelectedItem();
        model.getPointModel().removeAllElements();
        model.getSelectedPoints().parallelStream().forEach(p -> p.setColor(Color.BLACK));
        model.getSelectedPoints().clear();
        if (curve != null) {
            for (IPoint p : curve.getPoints()) {
                model.getPointModel().addElement(p);
            }
        }
        model.isDirty().compareAndSet(false,true);
    }

    public void handleRemovePointFromCurve() {
        ICurve curve = (ICurve) model.getCurveModel().getSelectedItem();
        IPoint point = (IPoint) model.getPointModel().getSelectedItem();

        List<IPoint> points = curve.getPoints();
        points.remove(point);
        model.getPointModel().removeElement(point);
        curve.setPoints(points);
        CurveUpdater.update((ICurve) model.getCurveModel().getSelectedItem());
        model.isDirty().compareAndSet(false,true);

    }

    public void handleAddCurve() {
        ICurve curve;
        ICurve.CurveType type = (ICurve.CurveType) model.getCurveTypeModel().getSelectedItem();

        if (ICurve.CurveType.Newton.equals(type)){
            curve = new Interpolated();
        } else {
            curve = new PolygonalChain();
        }


        curve.setColor((Color) model.getColorModel().getSelectedItem());
        NameGenerator.generateName(curve);
        model.getCurveModel().addElement(curve);
        model.getCurveModel().setSelectedItem(curve);
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
        model.isDirty().compareAndSet(false,true);
    }

    public void handlePointChange(){
        IPoint p = (IPoint) model.getPointModel().getSelectedItem();
        if (p!=null && model.getLockSpinners().tryAcquire()){
            model.getxModel().setValue(p.getX());
            model.getyModel().setValue(p.getY());

            model.getLockSpinners().release();
        }

    }

    public void handlePointSpinnerChange() {
        IPoint point = (IPoint) model.getPointModel().getSelectedItem();
        if (point!=null && model.getLockSpinners().tryAcquire()){
            point.setX(model.getxModel().getNumber().intValue());
            point.setY(model.getyModel().getNumber().intValue());
            point.setWeigh(model.getWeighModel().getNumber().floatValue());
            model.isDirty().compareAndSet(false,true);
            model.getLockSpinners().release();
            CurveUpdater.update((ICurve) model.getCurveModel().getSelectedItem());
        }

    }
}
