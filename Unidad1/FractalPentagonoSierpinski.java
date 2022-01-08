/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unidad1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Path2D;
import static java.lang.Math.*;
import java.util.Random;
import javax.swing.*;

/**
 *
 * @author  Manzano
 */
public class FractalPentagonoSierpinski extends JPanel {

    final double degrees072 = toRadians(72);

    final double scaleFactor = 1 / (2 + cos(degrees072) * 2);

    final int margin = 20;
    int limit = 0;
    Random r = new Random();

    public FractalPentagonoSierpinski() {
        setPreferredSize(new Dimension(640, 640));
        setBackground(Color.white);

        new Timer(3000, (ActionEvent e) -> {
            limit++;
            if (limit >= 5) {
                limit = 0;
            }
            repaint();
        }).start();
    }

    void drawPentagon(Graphics2D g, double x, double y, double side, int depth) {
        double angle = 3 * degrees072;

        if (depth == 0) {

            Path2D p = new Path2D.Double();
            p.moveTo(x, y);

            for (int i = 0; i < 5; i++) {
                x = x + cos(angle) * side;
                y = y - sin(angle) * side;
                p.lineTo(x, y);
                angle += degrees072;
            }

            g.setColor(RandomHue.next());
            g.fill(p);

        } else {

            side *= scaleFactor;

            double distance = side + side * cos(degrees072) * 2;

            for (int i = 0; i < 5; i++) {
                x = x + cos(angle) * distance;
                y = y - sin(angle) * distance;
                drawPentagon(g, x, y, side, depth - 1);
                angle += degrees072;
            }
        }
    }

    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        double radius = w / 2 - 2 * margin;
        double side = radius * sin(PI / 5) * 2;

        drawPentagon(g, w / 2, 3 * margin, side, limit);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setTitle("Sierpinski Pentagon");
            f.setResizable(true);
            f.add(new FractalPentagonoSierpinski(), BorderLayout.CENTER);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}

class RandomHue {

    final static double goldenRatioConjugate = (sqrt(5) - 1) / 2;
    private static double hue = Math.random();

    static Color next() {
        hue = (hue + goldenRatioConjugate) % 1;
        return Color.getHSBColor((float) hue, 1, 1);
    }
}
