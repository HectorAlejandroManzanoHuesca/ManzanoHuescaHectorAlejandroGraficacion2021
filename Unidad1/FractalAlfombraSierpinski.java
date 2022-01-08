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
public class FractalAlfombraSierpinski {
    public static void main(String[] args) {
        JFrame f = new JFrame("Alfombra de Sierpinski");
        AlfombraSierpinski triangulo = new AlfombraSierpinski();
        PanelAlfombraSierpinski panel = new PanelAlfombraSierpinski(triangulo);
        OyenteAlfombraSierpinski oyente = new OyenteAlfombraSierpinski(triangulo, panel);
        panel.addEventos(oyente);
        f.setSize(800, 800);
        f.setLocation(100, 10);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(panel);
        f.setVisible(true);
    }
   
}
