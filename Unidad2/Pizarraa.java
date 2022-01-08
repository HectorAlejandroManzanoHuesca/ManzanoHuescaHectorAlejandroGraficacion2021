/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Unidad2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Manzano
 */
public class Pizarraa extends javax.swing.JFrame {

    static final int LINEA = 0;
    static final int TRIANGULO = 1;
    static final int CUADRADO = 2;
    static final int CIRCULO = 3;
    
    static final int ANCHO = 640;
    static final int ALTO = 480;

    Raster raster;

    Point p1, p2, p3, p4;
    boolean bP1 = false, bP2 = false, bP3 = false, bP4 = false;
    int figura = LINEA;

    JList listFiguras;
    DefaultListModel listModel;
    ArrayList<Figura> aFiguras; //contenedor de figuras 
    
    
    Matrix matrix;

    ButtonGroup bg;

    Color color;
    
    JColorChooser colorChooser;
    
    
    
    /**
     * Creates new form Pizarraa
     */
    public Pizarraa() {
        initComponents();
        
        matrix = new Matrix();

        p1 = new Point();
        p2 = new Point();
        p3 = new Point();
        p4 = new Point();

        bP1 = false;
        bP2 = false;
        bP3 = false;
        bP4 = false;
        
         raster = new Raster(ANCHO, ALTO);

        panelRaster = new MyPanel(raster);
        /*te falto agregar el panel del raster al jframe*/
        panelRaster.setFocusable(true);
         this.panelRaster.setBackground(Color.WHITE);
        this.add(panelRaster, BorderLayout.CENTER);
        
        listFiguras = new JList();
        listFiguras.setModel(new DefaultListModel());
        listModel = (DefaultListModel) listFiguras.getModel();
        scrollFiguras.setViewportView(listFiguras);
        scrollFiguras.setPreferredSize(new Dimension(50, 300));

        panelFiguras.add(scrollFiguras);
        
        color = Color.black;
        
        bg = new ButtonGroup();

        rbLinea.setSelected(true);
        bg.add(rbLinea);
        bg.add(rbTriang);
        bg.add(rbCirculo);
        bg.add(rbCuadrado);

        
        aFiguras = new ArrayList<Figura>();
        
        this.panelRaster.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                jPanel1MouseClicked(evt);
                System.out.println("click");
            }
        });
        
        
        this.btnTrasladar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Figura figura = aFiguras.get(listFiguras.getSelectedIndex());
                
                String tipoTransf = ((JToggleButton) e.getSource()).getName();
                Point[] puntos = getPuntosFigura(figura);
                ArrayList<double[]> list_pprima = aplicarTransformacion(tipoTransf, puntos);
                dibujarFigura(list_pprima, figura);
            }
        });

        this.btnEscalar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Figura figura = aFiguras.get(listFiguras.getSelectedIndex());
                String tipoTransf = ((JToggleButton) e.getSource()).getName();
                Point[] puntos = getPuntosFigura(figura);
                ArrayList<double[]> list_pprima = aplicarTransformacion(tipoTransf, puntos);
                dibujarFigura(list_pprima, figura);
            }
        });

        this.btnRotar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Figura figura = aFiguras.get(listFiguras.getSelectedIndex());
                String tipoTransf = ((JToggleButton) e.getSource()).getName();
                Point[] puntos = getPuntosFigura(figura);
                ArrayList<double[]> list_pprima = aplicarTransformacion(tipoTransf, puntos);
                dibujarFigura(list_pprima, figura);
            }
        });

        this.btnColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = JColorChooser.showDialog(null, "Seleccione un color", color);
                btnColor.setBackground(color);
            }
        });

        this.btnGuardarRast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarImagen();
            }
        });

        this.btnGuardarVect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarVectores();
            }
        });

        this.btnAbrirVect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVectores();
            }
        });

        this.btnBorrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrarFiguras();
            }
        });

        this.setVisible(true);
        this.pack();
    }

    public void dibujarFigura(ArrayList<double[]> listpprima, Figura figura) {
        String linea;
        Point p1, p2, p3, p4;
        if (figura instanceof Linea) {
            double[] pp1 = listpprima.get(0);
            double[] pp2 = listpprima.get(1);
            p1 = new Point((int) pp1[0], (int) pp1[1]);
            p2 = new Point((int) pp2[0], (int) pp2[1]);
            this.dibujarLinea(p1, p2, color);

            Linea l = new Linea(p1, p2, color);

            linea = String.format("L,%.0f,%.0f,%.0f,%.0f,%x\n", l.punto1.getX(), l.punto1.getY(),
                    l.punto2.getX(), l.punto2.getY(),
                    l.color.getRGB());

            aFiguras.add(l);
            listModel.addElement(linea);
        }

        if (figura instanceof Triangulo) {
            double[] pp1 = listpprima.get(0);
            double[] pp2 = listpprima.get(1);
            double[] pp3 = listpprima.get(2);
            p1 = new Point((int) pp1[0], (int) pp1[1]);
            p2 = new Point((int) pp2[0], (int) pp2[1]);
            p3 = new Point((int) pp3[0], (int) pp3[1]);
            this.dibujarTriangulo(p1, p2, p3, color);

            Triangulo l = new Triangulo(p1, p2, p3, color);

            linea = String.format("T,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%x\n", l.p1.getX(), l.p1.getY(),
                    l.p2.getX(), l.p2.getY(), l.p3.getX(), l.p3.getY(),
                    color.getRGB());

            aFiguras.add(l);
            listModel.addElement(linea);
        }

        if (figura instanceof Circulo) {
            double[] pp1 = listpprima.get(0);
            double[] pp2 = listpprima.get(1);
            p1 = new Point((int) pp1[0], (int) pp1[1]);
            p2 = new Point((int) pp2[0], (int) pp2[1]);
            this.dibujarCirculoRelleno(p1, p2);

            Circulo l = new Circulo(p1, p2, color);

            linea = String.format("C,%.0f,%.0f,%.0f,%.0f,%x\n", l.punto1.getX(), l.punto1.getY(),
                    l.punto2.getX(), l.punto2.getY(),
                    l.color.getRGB());

            aFiguras.add(l);
            listModel.addElement(linea);
        }

        if (figura instanceof Rectangulo) {
            double[] pp1 = listpprima.get(0);
            double[] pp2 = listpprima.get(1);
            double[] pp3 = listpprima.get(2);
            double[] pp4 = listpprima.get(3);
            p1 = new Point((int) pp1[0], (int) pp1[1]);
            p2 = new Point((int) pp2[0], (int) pp2[1]);
            p3 = new Point((int) pp3[0], (int) pp3[1]);
            p4 = new Point((int) pp4[0], (int) pp4[1]);
            this.dibujarCuadrado(p1, p2, p3, p4, color);

            Rectangulo l = new Rectangulo(p1, p2, p3, p4, color);

            linea = String.format("R,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%x\n", l.punto1.getX(), l.punto1.getY(),
                    l.punto2.getX(), l.punto2.getY(), l.punto3.getX(), l.punto3.getY(), l.punto4.getX(), l.punto4.getY(),
                    l.color.getRGB());

            aFiguras.add(l);
            listModel.addElement(linea);
        }

        this.repaint();
    }

    public ArrayList<double[]> aplicarTransformacion(String tipoTrans, Point[] puntos) {

        matrix.loadIdentity();

        switch (tipoTrans) {
            case "trasladar":
                int dx = Integer.parseInt(campoTX.getText());
                int dy = Integer.parseInt(campoTY.getText());
                matrix.traslacion(dx, dy);
                break;
            case "escalar":
                int ex = Integer.parseInt(campoEX.getText());
                int ey = Integer.parseInt(campoEY.getText());
                matrix.escalarXY(ex, ey);

                //System.out.println("Entro a escalar: " + ex + ", " + ey);
                break;
            case "rotar":
                int r = Integer.parseInt(campoR.getText());
                matrix.rotacion(r);

            //System.out.println("Entro a rotar: " + r);
        }

        ArrayList<double[]> listPPrima = new ArrayList<>();
        for (int i = 0; i < puntos.length; i++) {

            double[] punto = {puntos[i].x, puntos[i].y, 1};
            double[] pprima = matrix.pprima(punto);
            listPPrima.add(pprima);
        }

        ArrayList<double[]> listAuxPPrima = new ArrayList<>();
        if (tipoTrans.equals("rotar")) {
            double[] p1 = {puntos[0].x - listPPrima.get(0)[0], puntos[0].y - listPPrima.get(0)[1], 1};
            matrix.loadIdentity();
            matrix.traslacion((int) p1[0], (int) p1[1]);
            for (int i = 0; i < listPPrima.size(); i++) {
                double[] punto = {listPPrima.get(i)[0], listPPrima.get(i)[1], 1};
                double[] pprima = matrix.pprima(punto);
                listAuxPPrima.add(pprima);
            }
            return listAuxPPrima;
        }

        return listPPrima;

    }
    
    
    public Point[] getPuntosFigura(Figura figura) {
        Point[] puntos = null;
        if (figura instanceof Linea) {
            Linea l = (Linea) figura;
            puntos = new Point[2];
            puntos[0] = l.punto1;
            puntos[1] = l.punto2;
            //System.out.println("linea");
        }

        if (figura instanceof Triangulo) {
            Triangulo l = (Triangulo) figura;
            puntos = new Point[3];
            puntos[0] = l.p1;
            puntos[1] = l.p2;
            puntos[2] = l.p3;
            //System.out.println("Triangulo");
        }

        if (figura instanceof Circulo) {
            Circulo l = (Circulo) figura;
            puntos = new Point[2];
            puntos[0] = l.punto1;
            puntos[1] = l.punto2;
            //System.out.println("circulo");
        }

        if (figura instanceof Rectangulo) {
            Rectangulo l = (Rectangulo) figura;
            puntos = new Point[4];
            puntos[0] = l.punto1;
            puntos[1] = l.punto2;
            puntos[2] = l.punto3;
            puntos[3] = l.punto4;
            //System.out.println("rectangulo");
        }
        return puntos;
    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    public void guardarImagen() {

        BufferedImage img = toBufferedImage(raster.toImage(this));
        try {
            File outputfile = new File("saved.png");
            ImageIO.write(img, "png", outputfile);
        } catch (IOException e) {

        }
    }
    
    public void abrirVectores() {
        // TODO add your handling code here:
        this.aFiguras.clear();

        // Leer el archivo de figuras, crear los objetos que corresponda
        // y agregarlos al arreglo aFiguras.
        JFileChooser chooser = new JFileChooser();

        // FileNameExtensionFilter filter = new FileNameExtensionFilter(
        //    "Documentos", "doc", "docx", "dot");
        // chooser.setFileFilter(filter);
        String nombreArchivo = "";
        int returnVal = chooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {

            nombreArchivo = chooser.getSelectedFile().getPath();

            try {
                FileReader fr = new FileReader(nombreArchivo);
                BufferedReader br = new BufferedReader(fr);
                String linea = "";

                while ((linea = br.readLine()) != null) {
                    if (linea.startsWith("L")) {
                        String[] puntos = linea.split(",");
                        Point p1 = new Point(Integer.parseInt(puntos[1]), Integer.parseInt(puntos[2]));
                        Point p2 = new Point(Integer.parseInt(puntos[3]), Integer.parseInt(puntos[4]));
                        Color color = Color.decode("#" + puntos[5].substring(2));

                        
                        this.aFiguras.add(new Linea(p1, p2, color));
                        this.listModel.addElement(linea);
                    }

                    if (linea.startsWith("T")) {
                        String[] puntos = linea.split(",");
                        Point p1 = new Point(Integer.parseInt(puntos[1]), Integer.parseInt(puntos[2]));
                        Point p2 = new Point(Integer.parseInt(puntos[3]), Integer.parseInt(puntos[4]));
                        Point p3 = new Point(Integer.parseInt(puntos[5]), Integer.parseInt(puntos[6]));
                        Color color = Color.decode("#" + puntos[7].substring(2));

                        this.aFiguras.add(new Triangulo(p1, p2, p3, color));
                        this.listModel.addElement(linea);
                    }

                    if (linea.startsWith("R")) {
                        String[] puntos = linea.split(",");
                        Point p1 = new Point(Integer.parseInt(puntos[1]), Integer.parseInt(puntos[2]));
                        Point p2 = new Point(Integer.parseInt(puntos[3]), Integer.parseInt(puntos[4]));
                        Point p3 = new Point(Integer.parseInt(puntos[5]), Integer.parseInt(puntos[6]));
                        Point p4 = new Point(Integer.parseInt(puntos[7]), Integer.parseInt(puntos[8]));
                        Color color = Color.decode("#" + puntos[9].substring(2));

                        this.aFiguras.add(new Rectangulo(p1, p2, p3, p4, color));
                        this.listModel.addElement(linea);
                    }

                    if (linea.startsWith("C")) {
                        String[] puntos = linea.split(",");
                        Point p1 = new Point(Integer.parseInt(puntos[1]), Integer.parseInt(puntos[2]));
                        Point p2 = new Point(Integer.parseInt(puntos[3]), Integer.parseInt(puntos[4]));
                        Color color = Color.decode("#" + puntos[5].substring(2));

                        this.aFiguras.add(new Circulo(p1, p2, color));
                        this.listModel.addElement(linea);
                    }
                }

                br.close();
            } catch (IOException ex) {
                Logger.getLogger(PizarraOriginal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        this.redibujar();
    }

    public void redibujar() {
        Iterator itr = aFiguras.iterator();
        int i = 0;
        while (itr.hasNext()) {
            Figura f = (Figura) itr.next();

            if (f instanceof Linea) {
                Linea l = (Linea) f;
                this.dibujarLinea(l.punto1, l.punto2, l.color);
            }

            if (f instanceof Triangulo) {
                Triangulo t = (Triangulo) f;
                this.dibujarTriangulo(t.p1, t.p2, t.p3, t.color);
            }

            if (f instanceof Rectangulo) {
                Rectangulo c = (Rectangulo) f;
                this.dibujarCuadrado(c.punto1, c.punto2, c.punto3, c.punto4, c.color);
            }

            if (f instanceof Circulo) {
                Circulo c = (Circulo) f;
                this.dibujarCirculoRelleno(c.punto1, c.punto2);
            }
        }
    }
    
    
     public void guardarVectores() {

        // Leer el archivo de figuras, crear los objetos que corresponda
        // y agregarlos al arreglo aFiguras.
        JFileChooser chooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "txt");
        chooser.setFileFilter(filter);
        String rutaGuardar = "";
        int returnVal = chooser.showSaveDialog(this);

        FileWriter fw = null;
        String linea = "";

        if (returnVal == JFileChooser.APPROVE_OPTION) {

            
            rutaGuardar = chooser.getSelectedFile().getPath();

            if (!rutaGuardar.contains(".txt")) {
                rutaGuardar = chooser.getSelectedFile().getPath() + ".txt";
            } else {
                //System.out.println("no contiene.txt");
            }
            //System.out.println(rutaGuardar);

            try {
                fw = new FileWriter(rutaGuardar);
                for (int i = 0; i < aFiguras.size(); i++) {
                    Figura f = aFiguras.get(i);

                    if (f instanceof Linea) {
                        Linea l = (Linea) f;

                        linea = String.format("L,%.0f,%.0f,%.0f,%.0f,%x\n", l.punto1.getX(), l.punto1.getY(),
                                l.punto2.getX(), l.punto2.getY(),
                                l.color.getRGB());
                    }

                    if (f instanceof Triangulo) {
                        Triangulo t = (Triangulo) f;
                        linea = String.format("T,%d,%d,%d,%d,%d,%d,%x\n", t.p1.x, t.p1.y,
                                t.p2.x, t.p2.y,
                                t.p3.x, t.p3.y,
                                t.color.getRGB());
                    }

                    if (f instanceof Circulo) {
                        Circulo l = (Circulo) f;
                        linea = String.format("C,%.0f,%.0f,%.0f,%.0f,%x\n", l.punto1.getX(), l.punto1.getY(), l.punto2.getX(), l.punto2.getY(), l.color.getRGB());

                    }

                    if (f instanceof Rectangulo) {
                        Rectangulo l = (Rectangulo) f;
                        linea = String.format("R,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%x\n", l.punto1.getX(), l.punto1.getY(),
                                l.punto2.getX(), l.punto2.getY(), l.punto3.getX(), l.punto3.getY(), l.punto4.getX(), l.punto4.getY(),
                                l.color.getRGB());
                    }

                    fw.write(linea);
                }
            } catch (IOException ex) {
                Logger.getLogger(PizarraOriginal.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fw.close();
                } catch (IOException ex) {
                    Logger.getLogger(PizarraOriginal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public void clear() {
        int s = raster.size();
        for (int i = 0; i < s; i++) {
            raster.pixel[i] ^= 0x00ffffff;
        }
        repaint();
        return;
    }

    public void lineaSimple(int x0, int y0, int x1, int y1, Color color) {
        int pix = color.getRGB();
        int dx = x1 - x0;
        int dy = y1 - y0;

        raster.setPixel(pix, x0, y0);

        if (dx != 0) {
            float m = (float) dy / (float) dx;
            float b = y0 - m * x0;

            dx = (x1 > x0) ? 1 : -1;

            while (x0 != x1) {
                x0 += dx;
                y0 = Math.round(m * x0 + b);
                raster.setPixel(pix, x0, y0);
            }
        }
    }

    public void lineaMejorada(int x0, int y0, int x1, int y1, Color color) {
        int pix = color.getRGB();
        int dx = x1 - x0;
        int dy = y1 - y0;
        raster.setPixel(pix, x0, y0);
        if (Math.abs(dx) > Math.abs(dy)) {     // inclinacion < 1
            float m = (float) dy / (float) dx; // calcular inclinacion
            float b = y0 - m * x0;
            dx = (dx < 0) ? -1 : 1;
            while (x0 != x1) {
                x0 += dx;
                raster.setPixel(pix, x0, Math.round(m * x0 + b));
            }
        } else {
            if (dy != 0) {                         // inclinacion >= 1
                float m = (float) dx / (float) dy; // Calcular inclinacion
                float b = x0 - m * y0;
                dy = (dy < 0) ? -1 : 1;
                while (y0 != y1) {
                    y0 += dy;
                    raster.setPixel(pix, Math.round(m * y0 + b), y0);
                }
            }
        }
    }

    public void lineFast(int x0, int y0, int x1, int y1, Color color) {
        int pix = color.getRGB();
        int dy = y1 - y0;
        int dx = x1 - x0;
        int stepx, stepy;
        if (dy < 0) {
            dy = -dy;
            stepy = -raster.width;
        } else {
            stepy = raster.width;
        }
        if (dx < 0) {
            dx = -dx;
            stepx = -1;
        } else {
            stepx = 1;
        }
        dy <<= 1;
        dx <<= 1;
        y0 *= raster.width;
        y1 *= raster.width;
        raster.pixel[x0 + y0] = pix;
        if (dx > dy) {
            int fraction = dy - (dx >> 1);
            while (x0 != x1) {
                if (fraction >= 0) {
                    y0 += stepy;
                    fraction -= dx;
                }
                x0 += stepx;
                fraction += dy;
                raster.pixel[x0 + y0] = pix;
            }
        } else {
            int fraction = dx - (dy >> 1);
            while (y0 != y1) {
                if (fraction >= 0) {
                    x0 += stepx;
                    fraction -= dy;
                }
                y0 += stepy;
                fraction += dx;
                raster.pixel[x0 + y0] = pix;
            }
        }
    }

    
    private void dibujarLinea(Point _p1, Point _p2, Color color) {
        long inicio = 0, fin = 0;
        //inicio = System.nanoTime();
        // lineaMejorada(_p1.x,_p1.y,_p2.x,_p2.y,color);
        //fin    = System.nanoTime();

        ////System.out.printf("Tiempo transcurrido, simple: %d\n",(fin-inicio));
        //inicio = System.nanoTime();
        lineFast(_p1.x, _p1.y, _p2.x, _p2.y, color);
        //fin    = System.nanoTime();

        ////System.out.printf("Tiempo transcurrido, fast  : %d\n",(fin-inicio));
        this.repaint();
    }

    private void dibujarTriangulo(Point p1, Point p2, Point p3, Color c) {
        // TODO add your handling code here:
        Vertex2D v1 = new Vertex2D(p1.x, p1.y, c.getRGB());
        Vertex2D v2 = new Vertex2D(p2.x, p2.y, c.getRGB());
        Vertex2D v3 = new Vertex2D(p3.x, p3.y, c.getRGB());

        Triangulo tri = new Triangulo(v1, v2, v3, c);

        tri.dibujar(raster);

        //aFiguras.add(tri);
        //listModel.addElement("Triangulo");
        this.repaint();
    }

    /*Se utilizan 4 puntos para poder aplicar las transformaciones de manera adecuada*/
    public void dibujarCuadrado(Point p1, Point p2, Color c) {
        p3 = new Point(p2.x, p1.y);
        p4 = new Point(p1.x, p2.y);
        lineFast(p1.x, p1.y, p3.x, p3.y, c);
        lineFast(p3.x, p3.y, p2.x, p2.y, c);
        lineFast(p2.x, p2.y, p4.x, p4.y, c);
        lineFast(p4.x, p4.y, p1.x, p1.y, c);
        this.repaint();
    }

    public void dibujarCuadrado(Point p1, Point p2, Point p3, Point p4, Color c) {
        lineFast(p1.x, p1.y, p3.x, p3.y, c);
        lineFast(p3.x, p3.y, p2.x, p2.y, c);
        lineFast(p2.x, p2.y, p4.x, p4.y, c);
        lineFast(p4.x, p4.y, p1.x, p1.y, c);
        this.repaint();
    }

    public void dibujarCirculoRelleno(Point p1, Point p2) {
        double dX = Math.pow((Math.abs(p2.x - p1.x)), 2);
        double dY = Math.pow((Math.abs(p2.y - p1.y)), 2);
        int d = (int) (Math.sqrt(dX + dY));
        int radius = d / 2;
        int multiplier = 1;
        int midPointX = (p1.x + p2.x) / 2;
        int midPointY = (p1.y + p2.y) / 2;

        midPointX /= multiplier;
        midPointY /= multiplier;
        radius /= multiplier;
        int x = 0;
        int y = radius;
        d = 1 - radius;

        drawSymmetricPoints(midPointX, midPointY, x, y, color);
        while (x < y) {
            if (d < 0) {
                d = d + 2 * x + 3;
                x++;
            } else {
                d = d + 2 * (x - y) + 5;
                x++;
                y--;
            }
            drawSymmetricPoints(midPointX, midPointY, x, y, color);
        }
    }

    private void drawSymmetricPoints(int mx, int my, int x, int y, Color color) {
        int c = color.getRGB();
        raster.setPixel(c, (mx + x), (my - y));
        raster.setPixel(c, (mx + x), (my + y));
        raster.setPixel(c, (mx - x), (my + y));
        raster.setPixel(c, (mx - x), (my - y));
        raster.setPixel(c, (mx + y), (my - x));
        raster.setPixel(c, (mx + y), (my + x));
        raster.setPixel(c, (mx - y), (my + x));
        raster.setPixel(c, (mx - y), (my - x));
    }

    
    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        if (rbLinea.isSelected()) {
            figura = LINEA;
        }
        if (rbTriang.isSelected()) {
            figura = TRIANGULO;
        }
        if (rbCuadrado.isSelected()) {
            figura = CUADRADO;
        }
        if (rbCirculo.isSelected()) {
            figura = CIRCULO;
        }

        if (bP1 && bP2 && !bP3) {
            p3.x = evt.getX();
            p3.y = evt.getY();
            bP3 = true;

            dibujarLinea(p3, p3, color);
        }

        if (bP1 && !bP2) {
            p2.x = evt.getX();
            p2.y = evt.getY();
            bP2 = true;

            dibujarLinea(p2, p2, color);
        }

        if (!bP1) {
            p1.x = evt.getX();
            p1.y = evt.getY();
            bP1 = true;

            dibujarLinea(p1, p1, color);
        }

        if (figura == LINEA && bP1 && bP2) {
            dibujarLinea(p1, p2, color);
            bP1 = false;
            bP2 = false;
            bP3 = false;
            bP4 = false;
            Linea l = new Linea(p1, p2, color);

            String linea = String.format("L,%.0f,%.0f,%.0f,%.0f,%x\n", l.punto1.getX(), l.punto1.getY(),
                    l.punto2.getX(), l.punto2.getY(),
                    l.color.getRGB());

            aFiguras.add(l);
            listModel.addElement(linea);
        }

        if (figura == TRIANGULO && bP1 && bP2 && bP3) {
            dibujarTriangulo(p1, p2, p3, color);
            bP1 = false;
            bP2 = false;
            bP3 = false;
            bP4 = false;
            Triangulo l = new Triangulo(p1, p2, p3, color);
            String linea = String.format("T,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%x\n", l.p1.getX(), l.p1.getY(),
                    l.p2.getX(), l.p2.getY(), l.p3.getX(), l.p3.getY(),
                    color.getRGB());

            aFiguras.add(l);
            listModel.addElement(linea);
        }

        if (figura == CUADRADO && bP1 && bP2) {
            Rectangulo l = new Rectangulo(p1, p2, color);

            String linea = String.format("R,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%x\n", l.punto1.getX(), l.punto1.getY(),
                    l.punto2.getX(), l.punto2.getY(), l.punto3.getX(), l.punto3.getY(), l.punto4.getX(), l.punto4.getY(),
                    l.color.getRGB());

            aFiguras.add(l);
            listModel.addElement(linea);
            dibujarCuadrado(p1, p2, color);
            bP1 = false;
            bP2 = false;
            bP3 = false;
            bP4 = false;
        }

        if (figura == CIRCULO && bP1 && bP2) {
            Circulo l = new Circulo(p1, p2, color);
            //System.out.println("Color: " + color + "p2: " + l.p2 + "p1 " + l.p1);

            String linea = String.format("C,%.0f,%.0f,%.0f,%.0f,%x\n", l.punto1.getX(), l.punto1.getY(), l.punto2.getX(), l.punto2.getY(), l.color.getRGB());

            aFiguras.add(l);
            listModel.addElement(linea);
            dibujarCirculoRelleno(p1, p2);
            bP1 = false;
            bP2 = false;
            bP3 = false;
            bP4 = false;
        }
    }

    public void jPanel1KeyReleased(KeyEvent ke) {

        if (ke.getKeyCode() == KeyEvent.VK_T) {
            this.figura = 1;
        }

        if (ke.getKeyCode() == KeyEvent.VK_L) {
            this.figura = 0;
        }
    }

    public void borrarFiguras() {
        this.aFiguras.clear();
        this.listModel.clear();
        //Borrando pixeles de figuras dibujadas
        this.raster.pixel = new int[raster.pixel.length];
        panelRaster.repaint();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelControles = new javax.swing.JPanel();
        btnGuardarVect = new javax.swing.JButton();
        btnAbrirVect = new javax.swing.JButton();
        btnGuardarRast = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnColor = new javax.swing.JButton();
        panelFiguras = new javax.swing.JPanel();
        rbLinea = new javax.swing.JToggleButton();
        rbTriang = new javax.swing.JToggleButton();
        rbCirculo = new javax.swing.JToggleButton();
        rbCuadrado = new javax.swing.JToggleButton();
        btnTrasladar = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        campoTX = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        campoTY = new javax.swing.JTextField();
        btnEscalar = new javax.swing.JToggleButton();
        jLabel3 = new javax.swing.JLabel();
        campoEX = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        campoEY = new javax.swing.JTextField();
        btnRotar = new javax.swing.JToggleButton();
        jLabel5 = new javax.swing.JLabel();
        campoR = new javax.swing.JTextField();
        scrollFiguras = new javax.swing.JScrollPane();
        panelRaster = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelControles.setLayout(new java.awt.GridLayout(1, 0));

        btnGuardarVect.setText("Guardar Vector");
        panelControles.add(btnGuardarVect);

        btnAbrirVect.setText("Abrir Vector");
        panelControles.add(btnAbrirVect);

        btnGuardarRast.setText("Guardar Imagen");
        panelControles.add(btnGuardarRast);

        btnBorrar.setText("Borrar");
        panelControles.add(btnBorrar);

        btnColor.setText("Color");
        panelControles.add(btnColor);

        getContentPane().add(panelControles, java.awt.BorderLayout.PAGE_START);

        panelFiguras.setLayout(new java.awt.GridLayout(18, 1));

        rbLinea.setText("Linea");
        panelFiguras.add(rbLinea);

        rbTriang.setText("Triangulo");
        panelFiguras.add(rbTriang);

        rbCirculo.setText("Circulo");
        panelFiguras.add(rbCirculo);

        rbCuadrado.setText("Rectangulo");
        panelFiguras.add(rbCuadrado);

        btnTrasladar.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        btnTrasladar.setText("Traslacion");
        btnTrasladar.setName("trasladar"); // NOI18N
        panelFiguras.add(btnTrasladar);

        jLabel1.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel1.setText("X:");
        panelFiguras.add(jLabel1);

        campoTX.setColumns(5);
        panelFiguras.add(campoTX);

        jLabel2.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel2.setText("Y:");
        panelFiguras.add(jLabel2);

        campoTY.setColumns(5);
        panelFiguras.add(campoTY);

        btnEscalar.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        btnEscalar.setText("Dilation");
        btnEscalar.setName("escalar"); // NOI18N
        panelFiguras.add(btnEscalar);

        jLabel3.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel3.setText("X: ");
        panelFiguras.add(jLabel3);
        panelFiguras.add(campoEX);

        jLabel4.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel4.setText("Y:");
        panelFiguras.add(jLabel4);
        panelFiguras.add(campoEY);

        btnRotar.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        btnRotar.setText("Rotacion");
        btnRotar.setName("rotar"); // NOI18N
        panelFiguras.add(btnRotar);

        jLabel5.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel5.setText("Angulo");
        panelFiguras.add(jLabel5);
        panelFiguras.add(campoR);
        panelFiguras.add(scrollFiguras);

        getContentPane().add(panelFiguras, java.awt.BorderLayout.LINE_START);

        panelRaster.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelRasterLayout = new javax.swing.GroupLayout(panelRaster);
        panelRaster.setLayout(panelRasterLayout);
        panelRasterLayout.setHorizontalGroup(
            panelRasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 744, Short.MAX_VALUE)
        );
        panelRasterLayout.setVerticalGroup(
            panelRasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 540, Short.MAX_VALUE)
        );

        getContentPane().add(panelRaster, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Pizarraa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pizarraa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pizarraa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pizarraa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pizarraa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbrirVect;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnColor;
    private javax.swing.JToggleButton btnEscalar;
    private javax.swing.JButton btnGuardarRast;
    private javax.swing.JButton btnGuardarVect;
    private javax.swing.JToggleButton btnRotar;
    private javax.swing.JToggleButton btnTrasladar;
    private javax.swing.JTextField campoEX;
    private javax.swing.JTextField campoEY;
    private javax.swing.JTextField campoR;
    private javax.swing.JTextField campoTX;
    private javax.swing.JTextField campoTY;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel panelControles;
    private javax.swing.JPanel panelFiguras;
    private javax.swing.JPanel panelRaster;
    private javax.swing.JToggleButton rbCirculo;
    private javax.swing.JToggleButton rbCuadrado;
    private javax.swing.JToggleButton rbLinea;
    private javax.swing.JToggleButton rbTriang;
    private javax.swing.JScrollPane scrollFiguras;
    // End of variables declaration//GEN-END:variables
}
