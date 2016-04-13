package view;

import controler.CanvasListener;
import controler.MainWindowController;
import model.*;
import utils.ImageFilter;
import model.viewModel.MainWindowModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
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


    private CustomCanvas canvas;
    private final MainWindowController controller;
    private final MainWindowModel model;


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

        model.getWeighModel().addChangeListener(e -> {
            controller.handlePointSpinnerChange();
        });

        model.getxModel().addChangeListener(e -> {
            controller.handlePointSpinnerChange();
        });

        model.getyModel().addChangeListener(e -> {
            controller.handlePointSpinnerChange();
        });

        pointComboBox.addItemListener(e -> controller.handlePointChange());

        addCurve.addActionListener(e -> {
            controller.handleAddCurve();
        });
        deleteCurve.addActionListener(e -> {
            controller.handleRemoveCurve();
        });
        curveComboBox.addItemListener(e -> {
            controller.handleCurveChange();
        });
        deletePointButton.addActionListener(e -> {
            controller.handleRemovePointFromCurve();
        });
        curveColorComboBox.addItemListener(e -> {
            controller.handleColorChange();
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
        clearBackgroud.addActionListener(e -> {
            model.setBackground(null);
        });
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

        this.createBufferStrategy(2);

    }

    private void initCanvas() {
        canvasPanel.setMinimumSize(new Dimension(800, 800));
        Timer timer = new Timer(20, e -> {
            if (model.isDirty().compareAndSet(true,false)){
                canvas.repaint();
                System.out.println("REPAINT!");
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

        canvasPanel.add(canvas);
    }
}
