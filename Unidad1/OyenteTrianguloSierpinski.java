/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unidad1;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author  Manzano
 */
public class OyenteTrianguloSierpinski extends MouseAdapter{
    private final TrianguloSierpinski triangulo;
    private final PanelTrianguloSierpinski panel;
    
    public OyenteTrianguloSierpinski(
            TrianguloSierpinski triangulo,
            PanelTrianguloSierpinski panel){
        this.triangulo = triangulo;
        this.panel = panel;
    }
    
    @Override
    public void mouseClicked(MouseEvent e){
       triangulo.setNivel(triangulo.getNivel()+1);
       panel.repaint();
    }
}
