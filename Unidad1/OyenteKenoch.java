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
public class OyenteKenoch extends MouseAdapter{
    private final Koch koch;
    private final PanelKoch panel;
    
    public OyenteKenoch(Koch koch,PanelKoch panel){
        this.koch = koch;
        this.panel = panel;
    }
    
    @Override
    public void mouseClicked(MouseEvent e){
        koch.setNivel(koch.getNivel()+1);
        panel.repaint();
    }
}
