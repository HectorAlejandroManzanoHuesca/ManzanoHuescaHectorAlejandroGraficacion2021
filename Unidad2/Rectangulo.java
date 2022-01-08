/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unidad2;

import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author Manzano
 */
public class Rectangulo extends Figura {
    Point punto1;
    Point punto2;
    Point punto3;
    Point punto4;
    Color color;
    
    public Rectangulo(Point _p1, Point _p2, Color c) {
        this.punto1 = new Point(_p1.x, _p1.y);
        this.punto2 = new Point(_p2.x, _p2.y);
        this.punto3 = new Point(_p2.x, _p1.y);
        this.punto4 = new Point(_p1.x, _p2.y);
        this.color = c;
    }
    
    public Rectangulo(Point _p1, Point _p2, Point _p3, Point _p4, Color _c) {
        this.punto1 = new Point(_p1.x, _p1.y);
        this.punto2 = new Point(_p2.x, _p2.y);
        this.punto3 = new Point(_p3.x, _p3.y);
        this.punto4 = new Point(_p4.x, _p4.y);
        this.color = _c;
    }
}
