package model.viewModel;

import model.ICurve;
import model.IPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Albert on 26.03.2016.
 */
public class MainWindowModel {
    private final SpinnerNumberModel weighModel = new SpinnerNumberModel(1.0, 0, Double.POSITIVE_INFINITY, 0.5f);
    private final SpinnerNumberModel xModel = new SpinnerNumberModel(0, 0, 1024, 1);
    private final SpinnerNumberModel yModel = new SpinnerNumberModel(0, 0, 1024, 1);
    private final DefaultComboBoxModel<ICurve> curveModel = new DefaultComboBoxModel<>();
    private final DefaultComboBoxModel<IPoint> pointModel = new DefaultComboBoxModel<>();
    private final Set<IPoint> selectedPoints = new ConcurrentSkipListSet<>();
    private final JToggleButton.ToggleButtonModel editModeButton = new JToggleButton.ToggleButtonModel();
    private final JToggleButton.ToggleButtonModel convexHullModel = new JToggleButton.ToggleButtonModel();
    private final AtomicBoolean dirty = new AtomicBoolean(true);
    private final Semaphore lockSpinners = new Semaphore(1, true);
    private final DefaultComboBoxModel<ICurve.CurveType> curveTypeModel = new DefaultComboBoxModel<>(ICurve.CurveType.values());
    private Color colorModel = Color.BLACK;
    private BufferedImage background;



    public SpinnerNumberModel getWeighModel() {
        return weighModel;
    }

    public Color getColor() {
        return colorModel;
    }

    public void setColorModel(Color colorModel) {
        this.colorModel = colorModel;
    }

    public DefaultComboBoxModel<ICurve> getCurveModel() {
        return curveModel;
    }


    public DefaultComboBoxModel<IPoint> getPointModel() {
        return pointModel;
    }

    public BufferedImage getBackground() {
        return background;
    }

    public void setBackground(BufferedImage background) {
        this.background = background;
        dirty.compareAndSet(false, true);
    }

    public DefaultButtonModel getEditModeButton() {
        return editModeButton;
    }

    public Set<IPoint> getSelectedPoints() {
        return selectedPoints;
    }

    public SpinnerNumberModel getxModel() {
        return xModel;
    }

    public SpinnerNumberModel getyModel() {
        return yModel;
    }

    public AtomicBoolean isDirty() {
        return dirty;
    }

    public Semaphore getLockSpinners() {
        return lockSpinners;
    }

    public DefaultComboBoxModel<ICurve.CurveType> getCurveTypeModel() {
        return curveTypeModel;
    }

    public DefaultButtonModel getConvexHullModel() {
        return convexHullModel;
    }
}
