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
public class PanelTrianguloSierpinski extends JPanel{
    private TrianguloSierpinski triangulo;

    public PanelTrianguloSierpinski(
            TrianguloSierpinski triangulo) {
        this.triangulo = triangulo;
        setBackground(Color.CYAN);
    }

    public void addEventos(OyenteTrianguloSierpinski oyente) {
        addMouseListener(oyente);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Nivel:" + triangulo.getNivel(), 650, 50);
        triangulo.dibujar(g, 700, triangulo.getNivel(), 50, 650);
    }
}
