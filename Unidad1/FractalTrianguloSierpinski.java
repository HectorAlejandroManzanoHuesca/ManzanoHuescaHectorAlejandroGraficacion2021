/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unidad1;

import javax.swing.JFrame;

/**
 *
 * @author  Manzano
 */
public class FractalTrianguloSierpinski {
    
    
    public static void main(String[] args) {
        JFrame f = new JFrame("Triángulo de Sierpinski");
        TrianguloSierpinski triangulo = new TrianguloSierpinski();
        PanelTrianguloSierpinski panel = new PanelTrianguloSierpinski(triangulo);
        OyenteTrianguloSierpinski oyente = new OyenteTrianguloSierpinski(triangulo, panel);
        panel.addEventos(oyente);
        f.setSize(800, 600);
        f.setLocation(100, 100);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(panel);
        f.setVisible(true);
    }
}
