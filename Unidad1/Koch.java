/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unidad1;

import java.awt.Graphics;

/**
 *
 * @author  Manzano
 */
public class Koch {
    private double x;
  private double y;
  private double angulo;
  private double largo;
  private int nivel;

  public void dibujarKoch(Graphics g, double l, int n) {
    if (n == 0) {
      double rad = Math.toRadians(angulo);
      double x1 = x + l * Math.cos(rad);
      double y1 = y + l * Math.sin(rad);
      g.drawLine((int) x, (int) y, (int) x1, (int) y1);
      x = x1;
      y = y1;
    } else {
      l = l / 3;
      n--;
      dibujarKoch(g, l, n);
      angulo += 60;
      dibujarKoch(g, l, n);
      angulo -= 120;
      dibujarKoch(g, l, n);
      angulo += 60;
      dibujarKoch(g, l, n);
    }
  }

  public void dibujarTriangulo(Graphics g) {
    angulo = 0;
    dibujarKoch(g, largo, nivel);
    angulo -= 120;
    dibujarKoch(g, largo, nivel);
    angulo -= 120;
    dibujarKoch(g, largo, nivel);
  }

  /**
   * @return the x
   */
  public double getX() {
    return x;
  }

  /**
   * @param x the x to set
   */
  public void setX(double x) {
    this.x = x;
  }

  /**
   * @return the y
   */
  public double getY() {
    return y;
  }

  /**
   * @param y the y to set
   */
  public void setY(double y) {
    this.y = y;
  }

  /**
   * @return the angulo
   */
  public double getAngulo() {
    return angulo;
  }

  /**
   * @param angulo the angulo to set
   */
  public void setAngulo(double angulo) {
    this.angulo = angulo;
  }

  /**
   * @return the largo
   */
  public double getLargo() {
    return largo;
  }

  /**
   * @param largo the largo to set
   */
  public void setLargo(double largo) {
    this.largo = largo;
  }

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
}
