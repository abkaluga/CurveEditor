package view;

import model.IPoint;
import model.beziere.BeziereCurve;
import utils.ParametrizationHelper;

import javax.swing.*;
import java.awt.geom.Ellipse2D;
import java.util.List;

public class SplitDialog extends JDialog {
    private static final double dil = 4.0;
    private final CustomCanvas canvas;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSlider splitPlace;
    private List<IPoint> points;
    private double splitValue;

    public SplitDialog(BeziereCurve selectedCurve, CustomCanvas canvas) {
        this.canvas = canvas;
        setContentPane(contentPane);
        setModal(true);
        setModalityType(ModalityType.APPLICATION_MODAL);
        points = selectedCurve.getInterpolatedPoints();
        splitPlace.setMinimum(0);
        splitPlace.setMaximum(points.size() - 1);
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> dispose());
        splitPlace.addChangeListener(e -> {
            canvas.repaint();
            IPoint p = points.get(splitPlace.getValue());
            canvas.splitPoint = new Ellipse2D.Double(p.getX() - (dil / 2), p.getY() - (dil / 2), dil, dil);
        });
        this.setVisible(true);
    }

    private void onOK() {
        splitValue = ParametrizationHelper.getInstance().translate(splitPlace.getValue(), points.size());
        canvas.splitPoint = null;
        canvas.repaint();
        dispose();
    }

    public double getSplitValue() {
        return splitValue;
    }
}
