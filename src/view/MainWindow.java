package view;

import controler.CanvasListener;
import controler.MainWindowController;
import model.ICurve;
import model.IPoint;
import model.beziere.BeziereCurve;
import model.viewModel.MainWindowModel;
import utils.ImageFilter;
import utils.XmlHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
/*
    TODO :
    1) Disable/enable spinners when no point
 */

/**
 * Created by Albert on 12.03.2016.
 */
public class MainWindow extends JFrame {
    private final MainWindowController controller;
    private final MainWindowModel model;
    private JPanel canvasPanel;
    private JComboBox<ICurve> curveComboBox;
    private JCheckBox editMode;
    private JButton addCurve;
    private JButton deleteCurve;
    private JButton saveButton;
    private JButton loadButton;
    private JButton loadBackgroudButton;
    private JButton clearBackgroud;
    private JComboBox<IPoint> pointComboBox;
    private JSpinner wigthSpiner;
    private JPanel mainPanel;
    private JButton saveAsJpgButton;
    private JComboBox<Color> curveColorComboBox;
    private JButton deletePointButton;
    private JSpinner xSpinner;
    private JSpinner ySpinner;
    private JComboBox curveComboboxType;
    private JCheckBox convexHull;
    private JButton transformCurve;
    private JButton lowerDeegreeButton;
    private JButton riseDeegreeButton;
    private CustomCanvas canvas;


    public MainWindow() {
        super();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        model = new MainWindowModel();
        controller = new MainWindowController(model);
        initCanvas();
        wigthSpiner.setModel(model.getWeighModel());
        curveColorComboBox.setModel(model.getColorModel());
        curveComboBox.setModel(model.getCurveModel());
        pointComboBox.setModel(model.getPointModel());
        editMode.setModel(model.getEditModeButton());
        xSpinner.setModel(model.getxModel());
        ySpinner.setModel(model.getyModel());
        curveComboboxType.setModel(model.getCurveTypeModel());
        convexHull.setModel(model.getConvexHullModel());
        setContentPane(mainPanel);
        this.setVisible(true);
        this.setMaximumSize(new Dimension(800, 800));
        this.setSize(new Dimension(800, 800));
        riseDeegreeButton.setVisible(false);
        lowerDeegreeButton.setVisible(false);

        model.getWeighModel().addChangeListener(e -> controller.handlePointSpinnerChange());

        model.getxModel().addChangeListener(e -> controller.handlePointSpinnerChange());

        model.getyModel().addChangeListener(e -> controller.handlePointSpinnerChange());

        pointComboBox.addItemListener(e -> controller.handlePointChange());

        addCurve.addActionListener(e -> controller.handleAddCurve());
        deleteCurve.addActionListener(e -> controller.handleRemoveCurve());
        curveComboBox.addItemListener(e -> {
            controller.handleCurveChange();
            Object item = model.getCurveModel().getSelectedItem();
            boolean visible = item instanceof BeziereCurve;
            riseDeegreeButton.setVisible(visible);
            lowerDeegreeButton.setVisible(visible);
        });
        deletePointButton.addActionListener(e -> controller.handleRemovePointFromCurve());
        curveColorComboBox.addItemListener(e -> controller.handleColorChange());

        transformCurve.addActionListener(e -> {
            controller.handleTransformCurve();
        });
        loadBackgroudButton.addActionListener(new ActionListener() {
            JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView());

            @Override
            public void actionPerformed(ActionEvent e) {
                fc.setMultiSelectionEnabled(false);
                fc.addChoosableFileFilter(new ImageFilter());
                int ret = fc.showOpenDialog(MainWindow.this);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    try {
                        model.setBackground(ImageIO.read(fc.getSelectedFile()));
                    } catch (IOException e1) {
                        //Intentionally left empty
                    }
                }

            }
        });
        clearBackgroud.addActionListener(e -> model.setBackground(null));
        saveAsJpgButton.addActionListener(new ActionListener() {
            JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView());

            @Override
            public void actionPerformed(ActionEvent e) {
                fc.setMultiSelectionEnabled(false);
                fc.addChoosableFileFilter(new ImageFilter());
                int ret = fc.showSaveDialog(MainWindow.this);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    int w = canvas.getWidth();
                    int h = canvas.getHeight();
                    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2 = bi.createGraphics();
                    g2.setBackground(canvas.getBackground());
                    canvas.exportPaint(g2);
                    try {
                        ImageIO.write(bi, "jpg", fc.getSelectedFile());
                    } catch (IOException e1) {
                        System.out.println("Panel write help: " + e1.getMessage());
                    }
                }
            }
        });

        convexHull.addChangeListener(e -> model.isDirty().compareAndSet(false, true));

        saveButton.addActionListener(new ActionListener() {
            JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView());

            @Override
            public void actionPerformed(ActionEvent e) {
                fc.setMultiSelectionEnabled(false);
                fc.addChoosableFileFilter(new ImageFilter());
                int ret = fc.showOpenDialog(MainWindow.this);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    XmlHelper.getInstance().marshal(fc.getSelectedFile(), model);
                }

            }
        });

        loadButton.addActionListener(new ActionListener() {
            JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView());

            @Override
            public void actionPerformed(ActionEvent e) {
                fc.setMultiSelectionEnabled(false);
                fc.addChoosableFileFilter(new ImageFilter());
                int ret = fc.showOpenDialog(MainWindow.this);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    XmlHelper.getInstance().unmarshal(fc.getSelectedFile(), model);
                }

            }
        });
        riseDeegreeButton.addActionListener(e -> controller.riseBeziereDeegre());
    }

    private void initCanvas() {
        canvasPanel.setMinimumSize(new Dimension(800, 800));
        Timer timer = new Timer(100, e -> {
            if (model.isDirty().compareAndSet(true, false)) {
                canvas.repaint();
                System.gc();
                System.out.printf("REPAINT! %d%n", canvas.getTimeBeetwenRepaints());
            }
        });
        timer.setDelay(50);
        timer.start();
        canvas = new CustomCanvas(model);
        canvas.setSize(800, 800);
        canvas.setBackground(Color.WHITE);
        canvas.setVisible(true);
        CanvasListener canvasListener = new CanvasListener(controller, model);
        canvas.addMouseListener(canvasListener);
        canvas.addKeyListener(canvasListener);
        canvas.addMouseMotionListener(canvasListener);
        canvas.setDoubleBuffered(true);
        canvasPanel.add(canvas);
    }
}
