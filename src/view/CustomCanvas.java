package view;

import model.Curve;
import model.ICurve;
import model.viewModel.MainWindowModel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Albert on 03.04.2016.
 */
class CustomCanvas extends JPanel {


    private final MainWindowModel model;

    private long lastRepaint = System.currentTimeMillis();


    CustomCanvas(MainWindowModel model) {
        this.model = model;
    }

    long getTimeBeetwenRepaints() {
        long now = System.currentTimeMillis();
        long diff = now - lastRepaint;
        lastRepaint = now;
        return diff;

    }

    @Override
    public void paint(Graphics g) {
        exportPaint(g);
        if (model.getConvexHullModel().isSelected()) {
            ICurve selected = (ICurve) model.getCurveModel().getSelectedItem();
            if (selected != null) {
                selected.drawExtras(g);
            }
        }

        for (int i = 0; i < model.getPointModel().getSize(); ++i) {
            model.getPointModel().getElementAt(i).draw(g);
        }
    }

    void exportPaint(Graphics g) {
        super.paint(g);
        if (model.getBackground() != null) {
            int x = (getWidth() - model.getBackground().getWidth()) / 2;
            int y = (getHeight() - model.getBackground().getHeight()) / 2;
            g.drawImage(model.getBackground(), x, y, this);
        }
        for (int i = 0; i < model.getCurveModel().getSize(); ++i) {
            Curve c = (Curve) model.getCurveModel().getElementAt(i);
            c.draw(g);
        }
    }

}
