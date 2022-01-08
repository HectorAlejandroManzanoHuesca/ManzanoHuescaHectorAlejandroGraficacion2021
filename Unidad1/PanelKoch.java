/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unidad1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author  Manzano
 */
public class PanelKoch extends JPanel{
     private final Koch koch;

    public PanelKoch(Koch koch) {
        this.koch = koch;
        setBackground(Color.CYAN);
    }

    public void addEventos(OyenteKenoch oyente) {
        this.addMouseListener(oyente);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Nivel:" + koch.getNivel(), 650, 50);
        koch.setY(450);
        koch.setX(150);
        koch.setAngulo(0);
        koch.setLargo(500);
        g.setColor(Color.black);
        koch.dibujarTriangulo(g);
    }
}
