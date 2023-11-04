/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.rummy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;

/**
 *
 * @author andy2
 */
public class Juego extends javax.swing.JFrame implements ActionListener{
    ArrayList<Integer> jugadores = new ArrayList<>();
    private JButton[][] mesa;
    private JButton[][] fichasJugador;
    private int rows = 15;
    private int columns = 20;
    private ArrayList<Ficha> fichas = new ArrayList<>();
    private ArrayList<ArrayList<Ficha>> listaMazos = new ArrayList<>();
    Color verdeOscuro = new Color(0, 128, 0);
    boolean turno = true;
    String textoAColocar;
    Color colorAColocar; 
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton boton = (JButton) e.getSource();
            String actionCommand = boton.getActionCommand(); 
            if(actionCommand != null){
                if (actionCommand.startsWith("Boton_")){
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 10; j++) {
                            if(e.getSource() == fichasJugador[i][j]){
                                textoAColocar = fichasJugador[i][j].getText();
                                colorAColocar = fichasJugador[i][j].getForeground();
                            }
                        }
                    }
                }  
                else{
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < columns; j++) {
                            if(e.getSource() == mesa[i][j]){
                                mesa[i][j].setBackground(Color.gray);
                                mesa[i][j].setForeground(colorAColocar);
                                mesa[i][j].setText(textoAColocar);
                            }
                        }
                    }
                }
            }     
        }
    }
        
    /**
     * Creates new form Juego
     */
    public Juego() {
        initComponents();
        mesa = new JButton[rows][columns];
        jPanelMesa.setLayout(new GridLayout(rows, columns));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                mesa[i][j] = new JButton();
                mesa[i][j].setPreferredSize(new Dimension(50, 50));
                mesa[i][j].setBackground(verdeOscuro);
                mesa[i][j].setActionCommand("Mesa_" + i + "_" + j); 
                mesa[i][j].addActionListener(this);
                jPanelMesa.add(mesa[i][j]); 
            }
        }
        for(int i = 1; i <= 13; i++){
            for(int z = 0; z < 2; z++){
                fichas.add(new Ficha(i, "Negro"));
            }
        }
        for(int i = 1; i <= 13; i++){
            for(int z = 0; z < 2; z++){
                fichas.add(new Ficha(i, "Rojo"));
            }
        }
        for(int i = 1; i <= 13; i++){
            for(int z = 0; z < 2; z++){
                fichas.add(new Ficha(i, "Azul"));
            }
        }
        for(int i = 1; i <= 13; i++){
            for(int z = 0; z < 2; z++){
                fichas.add(new Ficha(i, "Amarillo"));
            }
        }
        fichas.add(new FichaComodin(":-)", "Negro"));
        fichas.add(new FichaComodin(":-)", "Rojo"));
        for (Ficha ficha : fichas) {
            System.out.println(ficha); 
        }
        
        jugadores.add(1);
        for(Integer jugador: jugadores){
            fichasJugador = new JButton[3][10];
            jPanelJugador.setLayout(new GridLayout(3, 10));
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 10; j++) {
                    fichasJugador[i][j] = new JButton();
                    fichasJugador[i][j].setPreferredSize(new Dimension(50, 50));
                    fichasJugador[i][j].setBackground(Color.black);
                    fichasJugador[i][j].setActionCommand("Boton_" + i + "_" + j); 
                    fichasJugador[i][j].addActionListener(this);
                    jPanelJugador.add(fichasJugador[i][j]);
                }
            }
            ArrayList<Ficha> mazo = new ArrayList<>();
            for(int i = 0; i < 13; i++){
                Random random = new Random();
                int numeroRandom = random.nextInt(fichas.size());
                Ficha fichaRandom = fichas.get(numeroRandom);
                System.out.println(fichaRandom);
                fichas.remove(numeroRandom);
                mazo.add(fichaRandom);
            }
            listaMazos.add(mazo);
        }
        
        ArrayList<Ficha> mazoJugador = listaMazos.get(0);
        for (int j = 0; j < mazoJugador.size(); j++) {
            Ficha ficha = mazoJugador.get(j);
            JButton boton = fichasJugador[j / 10][j % 10];
            int numero = ficha.getNumero();
            String colorString = ficha.getColor();
            Color color = Color.black;
            String texto = " ";

            if(colorString.equals("Negro")){
                    color = Color.black;
                }
            if(colorString.equals("Rojo")){
                    color = Color.red;
                }
            if(colorString.equals("Azul")){
                    color = Color.blue;
                }
            if(colorString.equals("Amarillo")){
                    color = Color.yellow;
                }
            texto = String.valueOf(numero);
            if(numero == 0){
                FichaComodin fichaComodin = (FichaComodin)ficha;
                texto = fichaComodin.getSimbolo();
            }

            boton.setForeground(color);
            boton.setText(texto); 
            boton.setBackground(Color.gray);
        }
        
        
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelMesa = new javax.swing.JPanel();
        jButtonPilaDeFichas = new javax.swing.JButton();
        jPanelJugador = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rummy");
        setForeground(new java.awt.Color(0, 153, 0));

        javax.swing.GroupLayout jPanelMesaLayout = new javax.swing.GroupLayout(jPanelMesa);
        jPanelMesa.setLayout(jPanelMesaLayout);
        jPanelMesaLayout.setHorizontalGroup(
            jPanelMesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1079, Short.MAX_VALUE)
        );
        jPanelMesaLayout.setVerticalGroup(
            jPanelMesaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 495, Short.MAX_VALUE)
        );

        jButtonPilaDeFichas.setPreferredSize(new java.awt.Dimension(50, 50));
        jButtonPilaDeFichas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPilaDeFichasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelJugadorLayout = new javax.swing.GroupLayout(jPanelJugador);
        jPanelJugador.setLayout(jPanelJugadorLayout);
        jPanelJugadorLayout.setHorizontalGroup(
            jPanelJugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 597, Short.MAX_VALUE)
        );
        jPanelJugadorLayout.setVerticalGroup(
            jPanelJugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 186, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelMesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonPilaDeFichas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelJugador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(164, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonPilaDeFichas, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(229, 229, 229)
                        .addComponent(jPanelJugador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanelMesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(273, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonPilaDeFichasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPilaDeFichasActionPerformed
        // TODO add your handling code here:
        turno = false;
        int maximoFichas = 30;
        ArrayList<Ficha> mazoJugador = listaMazos.get(0);
        if(mazoJugador.size() >= maximoFichas){
            System.out.println("Maximo alcanzado");
        }
        else{
            Random random = new Random();
        int numeroRandom = random.nextInt(fichas.size());
        Ficha fichaRandom = fichas.get(numeroRandom);
        System.out.println(fichaRandom);
        fichas.remove(numeroRandom);

        mazoJugador.add(fichaRandom);
        for (int j = 0; j < mazoJugador.size(); j++) {
            Ficha ficha = mazoJugador.get(j);
            JButton boton = fichasJugador[j / 10][j % 10];
            int numero = ficha.getNumero();
            String colorString = ficha.getColor();
            Color color = Color.black;
            String texto = " ";

            if(colorString.equals("Negro")){
                    color = Color.black;
                }
            if(colorString.equals("Rojo")){
                    color = Color.red;
                }
            if(colorString.equals("Azul")){
                    color = Color.blue;
                }
            if(colorString.equals("Amarillo")){
                    color = Color.yellow;
                }
            texto = String.valueOf(numero);
            if(numero == 0){
                FichaComodin fichaComodin = (FichaComodin)ficha;
                texto = fichaComodin.getSimbolo();
            }

            boton.setForeground(color);
            boton.setText(texto); 
            boton.setBackground(Color.gray);
            }
        }     
    }//GEN-LAST:event_jButtonPilaDeFichasActionPerformed
    
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
            java.util.logging.Logger.getLogger(Juego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Juego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Juego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Juego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Juego().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonPilaDeFichas;
    private javax.swing.JPanel jPanelJugador;
    private javax.swing.JPanel jPanelMesa;
    // End of variables declaration//GEN-END:variables
}
