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
 * @author  Manzano
 */
public class TrianguloSierpinski {
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
            int[] cX = new int[3];
            int[] cY = new int[3];
            cX[0] = (int) x;
            cY[0] = (int) y;
            cX[1] = (int) (x + l);
            cY[1] = (int) y;
            cX[2] = (int) (x + l / 2);
            cY[2] = (int) (y - l * Math.sin(Math.PI / 3));
            g.setColor(Color.BLACK);
            g.fillPolygon(cX, cY, 3);
        } else {
            l = l / 2;
            n--;
            dibujar(g, l, n, x, y);
            dibujar(g, l, n, x + l, y);
            dibujar(g, l, n, x + l / 2, y - l * Math.sin(Math.PI / 3));
        }
    }
}
