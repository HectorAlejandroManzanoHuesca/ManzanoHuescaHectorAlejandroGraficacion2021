/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unidad1;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Manzano
 */
public class AlfombraSierpinski {
    private int nivel;

    /**
     * @return the nivel
     */
    public int getNivel() {
        return nivel;
    }

    /**
     * @param nivel the nivel to set
     */
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void dibujar(Graphics g, double l,
            int n, double x, double y) {
        if (n == 0) {
            int[] cX = new int[4];
            int[] cY = new int[4];
            cX[0] = (int) x;
            cY[0] = (int) y;
            cX[1] = (int) (x + l);
            cY[1] = (int) y;
            cX[2] = (int) (x + l);
            double angulo = Math.toRadians(90);
            cY[2] = (int) (y - l * Math.sin(angulo));
            cX[3] = (int) (x);
            cY[3] = (int) (y - l * Math.sin(angulo));
            g.setColor(Color.BLACK);
            g.fillPolygon(cX, cY, 4);
        } else {
            l = l / 3;
            n--;
            //Cuadros de abajo
            dibujar(g, l, n, x, y);
            dibujar(g, l, n, x + l, y);
            dibujar(g, l, n, x + l * 2, y);
            //Cuadros del centro
            double angulo = Math.toRadians(90);
            dibujar(g, l, n, x, y - l * Math.sin(angulo));
            dibujar(g, l, n, x + l * 2, y - l * Math.sin(angulo));
            //Cuadros de arriba
            dibujar(g, l, n, x, y - l * 2 * Math.sin(angulo));
            dibujar(g, l, n, x + l, y - l * 2 * Math.sin(angulo));
            dibujar(g, l, n, x + l * 2, y - l * 2 * Math.sin(angulo));
        }
    }
}
