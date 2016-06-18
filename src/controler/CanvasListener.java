package controler;

import model.ICurve;
import model.IPoint;
import model.viewModel.MainWindowModel;

import java.awt.*;
import java.awt.event.*;

/**
 * Created by Albert on 27.03.2016.
 */
public class CanvasListener implements MouseListener, KeyListener, MouseMotionListener {
    private final MainWindowController controller;
    private final MainWindowModel model;
    private int startX;
    private int startY;

    public CanvasListener(MainWindowController controller, MainWindowModel model) {
        this.controller = controller;
        this.model = model;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        ICurve curve = (ICurve) model.getCurveModel().getSelectedItem();
        Point mousePoint = e.getPoint();
        if (curve != null) {
            if (!model.getEditModeButton().isSelected() && model.getLockSpinners().tryAcquire()) {
                model.getxModel().setValue(e.getPoint().x);
                model.getyModel().setValue(e.getPoint().y);
                model.getLockSpinners().release();
                controller.addPointToCurve(model.getPointModel().getIndexOf(model.getPointModel().getSelectedItem()));
            } else {
                for (IPoint p : curve.getPoints()) {
                    if (Math.abs(p.getX() - mousePoint.getX()) < 8 &&
                            Math.abs(p.getY() - mousePoint.getY()) < 8) {
                        if (model.getSelectedPoints().contains(p)) {
                            model.getSelectedPoints().remove(p);
                            p.setColor(Color.BLACK);
                        } else {
                            model.getPointModel().setSelectedItem(p);
                            p.setColor(Color.GREEN);
                            model.getSelectedPoints().add(p);
                        }
                        model.isDirty().compareAndSet(false, true);
                        break;
                    }
                }
            }

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //left empty
    }

    private void moveSelectedPoints(int deltaX, int deltaY) {
        if (model.getEditModeButton().isSelected()) {
            model.getSelectedPoints().parallelStream()
                    .forEach(p -> {
                        p.setX(p.getX() + deltaX);
                        p.setY(p.getY() + deltaY);
                    });
            model.isDirty().compareAndSet(false, true);
            CurveUpdater.update((ICurve) model.getCurveModel().getSelectedItem(), model.isDirty());
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //Intentionally left empty
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //Intentionally left empty
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (model.getEditModeButton().isSelected()) {
            switch (e.getKeyChar()) {
                case KeyEvent.VK_W:
                case KeyEvent.VK_W + 32:
                    moveSelectedPoints(0, -1);
                    break;
                case KeyEvent.VK_S:
                case KeyEvent.VK_S + 32:
                    moveSelectedPoints(0, 1);
                    break;
                case KeyEvent.VK_A:
                case KeyEvent.VK_A + 32:
                    moveSelectedPoints(-1, 0);
                    break;
                case KeyEvent.VK_D:
                case KeyEvent.VK_D + 32:
                    moveSelectedPoints(1, 0);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Intentionally left empty
    }

    @Override
    public void keyReleased(KeyEvent e) {
        CurveUpdater.update((ICurve) model.getCurveModel().getSelectedItem(), model.isDirty());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int deltaX = e.getPoint().x - startX;
        int deltaY = e.getPoint().y - startY;
        moveSelectedPoints(deltaX, deltaY);

        startX = e.getX();
        startY = e.getY();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Intentionally left empty
    }
}
