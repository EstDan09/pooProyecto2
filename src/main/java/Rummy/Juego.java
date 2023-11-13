/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Rummy;

import Clientes.Cliente;
import Servidor.Partida;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author andy2
 */
public class Juego extends javax.swing.JFrame implements ActionListener {

    ArrayList<Integer> jugadores = new ArrayList<>();
    private JButton[][] mesa;
    private JButton[][] fichasJugador;
    private int rows = 15;
    private int columns = 20;
    private ArrayList<Ficha> fichas = new ArrayList<>();
    private ArrayList<String> fichasInfo = new ArrayList<>();
    private ArrayList<ArrayList<Ficha>> listaMazos = new ArrayList<>();
    String[][] mesaInfo;
    Cliente cliente;
    Partida partida;

    Color verdeOscuro = new Color(0, 128, 0);
    boolean turno = true;
    boolean ActionListenerHabilitado = true;
    String textoAColocar;
    Color colorAColocar;
    String codigoAColocar;
    int puntosDelPrimerTurno = 0;
    boolean primerTurnoValido = false;
    private ArrayList<Jugada> jugadas = new ArrayList<>();
    Ficha fichaActual = null;
    FichaComodin comodin = null;
    ArrayList<Ficha> mazoJugador;
    Jugada nuevaJugada = null;
    //Variables para devolver el valor si el espacio esta ocupado
    int x = -1;
    int y = -1;

    public void actualizarMesaServer(String[][] mesaActualizada) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (mesaActualizada[i][j].contains("VV")) {
                    this.mesa[i][j].setBackground(verdeOscuro);
                    this.mesa[i][j].setText("");
                } else if (mesaActualizada[i][j].contains("R")) {
                    this.mesa[i][j].setForeground(Color.red);
                    this.mesa[i][j].setBackground(Color.gray);
                } else if (mesaActualizada[i][j].contains("N")) {
                    this.mesa[i][j].setForeground(Color.black);
                    this.mesa[i][j].setBackground(Color.gray);
                } else if (mesaActualizada[i][j].contains("Z")) {
                    this.mesa[i][j].setForeground(Color.blue);
                    this.mesa[i][j].setBackground(Color.gray);
                } else if (mesaActualizada[i][j].contains("A")) {
                    this.mesa[i][j].setForeground(Color.yellow);
                    this.mesa[i][j].setBackground(Color.gray);
                }

                if (mesaActualizada[i][j].contains("C")) {
                    this.mesa[i][j].setText(":-)");
                } else {
                    if (mesaActualizada[i][j].length() == 2) {
                        String texto = mesaActualizada[i][j].substring(0, 1);
                        this.mesa[i][j].setText(texto);
                    } else {
                        String texto = mesaActualizada[i][j].substring(0, 2);
                        this.mesa[i][j].setText(texto);
                    }
                }
            }
        }
    }

    // Enviar al server la PilaDeFichas Actualizada
    public void actualizarPilaDeFichas(ArrayList<String> fichasActualizadas) {
        ArrayList<Ficha> fichasAEliminar = new ArrayList<>();
        for (String codigo : fichasActualizadas) {
            int numero = -1;
            String color = "";
            if (codigo.length() == 2) {
                if (codigo.substring(0, 1).equals("C")) {
                    numero = 0;
                } else {
                    numero = Integer.parseInt(codigo.substring(0, 1));
                    color = codigo.substring(1);
                }
            } else {
                numero = Integer.parseInt(codigo.substring(0, 2));
                color = codigo.substring(2);
            }
            for (Ficha ficha : fichas) {
                String colorLetra = "";
                if (ficha.getColor().substring(0, 2).equals("Az")) {
                    colorLetra = "Z";
                } else {
                    colorLetra = ficha.getColor().substring(0, 1);
                }
                if (ficha.getNumero() == numero && colorLetra.equals(color)) {
                    fichasAEliminar.add(ficha);
                    break;
                }
            }
        }
        fichas.removeAll(fichasAEliminar);
    }

    public void turnosServer() {
        // recibe un booleano del server si le toca su turno
    }

    public void setCliente(Cliente clienteAsignado) {
        this.cliente = clienteAsignado;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fichaActual = new Ficha(0, " ");
        comodin = new FichaComodin(":-)", "");
        mazoJugador = listaMazos.get(0);
        if (ActionListenerHabilitado && e.getSource() instanceof JButton) {
            JButton boton = (JButton) e.getSource();
            String actionCommand = boton.getActionCommand();

            if (actionCommand != null) {
                if (actionCommand.startsWith("Boton_")) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 10; j++) {
                            if (e.getSource() == fichasJugador[i][j]) {
                                textoAColocar = fichasJugador[i][j].getText();
                                colorAColocar = fichasJugador[i][j].getForeground();

                                String colorString = "";
                                int numeroFicha = 0;
                                if (textoAColocar.equals(":-)")) {
                                    codigoAColocar = "C";
                                } else {
                                    numeroFicha = Integer.parseInt(textoAColocar);
                                    fichaActual.setNumero(numeroFicha);
                                    codigoAColocar = textoAColocar;
                                }

                                if (colorAColocar.equals(Color.black)) {
                                    colorString = "Negro";
                                    codigoAColocar += "N";
                                }
                                if (colorAColocar.equals(Color.red)) {
                                    colorString = "Rojo";
                                    codigoAColocar += "R";
                                }
                                if (colorAColocar.equals(Color.blue)) {
                                    colorString = "Azul";
                                    codigoAColocar += "Z";
                                }
                                if (colorAColocar.equals(Color.yellow)) {
                                    colorString = "Amarillo";
                                    codigoAColocar += "A";
                                }
                                fichaActual.setColor(colorString);
                                comodin.setColor(colorString);
                                x = i;
                                y = j;
                                Iterator<Ficha> iteracionMazo = mazoJugador.iterator();
                                while (iteracionMazo.hasNext()) {
                                    Ficha ficha = iteracionMazo.next();
                                    int fichaNum = ficha.getNumero();
                                    String fichaString = ficha.getColor();

                                    if (fichaNum == numeroFicha && fichaString.equals(colorString)) {
                                        nuevaJugada = new Jugada(fichaActual, x, y);
                                        iteracionMazo.remove();
                                        System.out.println(mazoJugador.size());
                                    }
                                }

                                fichasJugador[i][j].setBackground(Color.black);
                                fichasJugador[i][j].setText("");

                                if (textoAColocar.equals(":-)")) {

                                } else {
                                    if (primerTurnoValido == false) {

                                        int puntosFicha = Integer.parseInt(textoAColocar);
                                        puntosDelPrimerTurno += puntosFicha;
                                        System.out.println(puntosDelPrimerTurno);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < columns; j++) {
                            if (e.getSource() == mesa[i][j]) {
                                if (mesa[i][j].getText().equals("")) {
                                    mesa[i][j].setBackground(Color.gray);
                                    mesa[i][j].setForeground(colorAColocar);
                                    mesa[i][j].setText(textoAColocar);
                                    mesaInfo[i][j] = codigoAColocar;
                                    System.out.println(codigoAColocar);
                                    if (nuevaJugada != null) {
                                        nuevaJugada.setFila(i);
                                        nuevaJugada.setColumna(j);
                                        jugadas.add(nuevaJugada);
                                        for (Jugada jugada : jugadas) {
                                            System.out.println("--------------------->");
                                            System.out.println(jugada);
                                            System.out.println("---------------------");
                                        }

                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Espacio Ocupado por otra ficha", "Error", JOptionPane.ERROR_MESSAGE);
                                    fichasJugador[x][y].setBackground(Color.gray);
                                    fichasJugador[x][y].setText(textoAColocar);
                                    fichasJugador[x][y].setForeground(colorAColocar);
                                }

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
        //jButtonPilaDeFichas.setEnabled(false);  cambiar luego
        mesa = new JButton[rows][columns];
        mesaInfo = new String[rows][columns];
        jPanelMesa.setLayout(new GridLayout(rows, columns));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                mesa[i][j] = new JButton();
                mesa[i][j].setPreferredSize(new Dimension(50, 50));
                mesa[i][j].setBackground(verdeOscuro);
                mesa[i][j].setActionCommand("Mesa_" + i + "_" + j);
                mesa[i][j].addActionListener(this);
                mesaInfo[i][j] = "VV";
                jPanelMesa.add(mesa[i][j]);
            }
        }
        for (int i = 1; i <= 13; i++) {
            for (int z = 0; z < 2; z++) {
                fichas.add(new Ficha(i, "Negro"));
                fichasInfo.add(Integer.toString(i) + "N");
            }
        }
        for (int i = 1; i <= 13; i++) {
            for (int z = 0; z < 2; z++) {
                fichas.add(new Ficha(i, "Rojo"));
                fichasInfo.add(Integer.toString(i) + "R");
            }
        }
        for (int i = 1; i <= 13; i++) {
            for (int z = 0; z < 2; z++) {
                fichas.add(new Ficha(i, "Azul"));
                fichasInfo.add(Integer.toString(i) + "Z");
            }
        }
        for (int i = 1; i <= 13; i++) {
            for (int z = 0; z < 2; z++) {
                fichas.add(new Ficha(i, "Amarillo"));
                fichasInfo.add(Integer.toString(i) + "A");
            }
        }
        fichas.add(new FichaComodin(":-)", "Negro"));
        fichasInfo.add("CN");
        fichas.add(new FichaComodin(":-)", "Rojo"));
        fichasInfo.add("CR");

        jugadores.add(1);
        for (Integer jugador : jugadores) {
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
            for (int i = 0; i <= 13; i++) {
                Random random = new Random();
                int numeroRandom = random.nextInt(fichas.size());
                Ficha fichaRandom = fichas.get(numeroRandom);
                fichas.remove(numeroRandom);
                String colorFichaRandom = fichaRandom.getColor();
                int numFichaRandom = fichaRandom.getNumero();
                String numInfo = Integer.toString(numFichaRandom);
                String colorCodigo = "";
                if (colorFichaRandom.equals("Negro")) {
                    colorCodigo = "N";
                }
                if (colorFichaRandom.equals("Rojo")) {
                    colorCodigo = "R";
                }
                if (colorFichaRandom.equals("Azul")) {
                    colorCodigo = "Z";
                }
                if (colorFichaRandom.equals("Amarillo")) {
                    colorCodigo = "A";
                }

                if (numFichaRandom == 0) {
                    fichasInfo.remove("C" + colorCodigo);
                } else {
                    fichasInfo.remove(numInfo + colorCodigo);
                }
                mazo.add(fichaRandom);
            }
            listaMazos.add(mazo);
        }

        mazoJugador = listaMazos.get(0);
        for (int j = 0; j < mazoJugador.size(); j++) {
            Ficha ficha = mazoJugador.get(j);
            JButton boton = fichasJugador[j / 10][j % 10];
            int numero = ficha.getNumero();
            String colorString = ficha.getColor();
            Color color = Color.black;
            String texto = " ";

            if (colorString.equals("Negro")) {
                color = Color.black;
            }
            if (colorString.equals("Rojo")) {
                color = Color.red;
            }
            if (colorString.equals("Azul")) {
                color = Color.blue;
            }
            if (colorString.equals("Amarillo")) {
                color = Color.yellow;
            }
            texto = String.valueOf(numero);
            if (numero == 0) {
                FichaComodin fichaComodin = (FichaComodin) ficha;
                texto = fichaComodin.getSimbolo();
            }

            boton.setForeground(color);
            boton.setText(texto);
            boton.setBackground(Color.gray);
        }

    }

    //public void
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelMesa = new javax.swing.JPanel();
        jPanelJugador = new javax.swing.JPanel();
        jButtonPilaDeFichas = new javax.swing.JButton();
        jButtonValidarJugada = new javax.swing.JButton();

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
            .addGap(0, 80, Short.MAX_VALUE)
        );

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

        jButtonPilaDeFichas.setPreferredSize(new java.awt.Dimension(50, 50));
        jButtonPilaDeFichas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPilaDeFichasActionPerformed(evt);
            }
        });

        jButtonValidarJugada.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jButtonValidarJugada.setText("Validar Jugada");
        jButtonValidarJugada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonValidarJugadaActionPerformed(evt);
            }
        });

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
                    .addComponent(jButtonValidarJugada, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelJugador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(164, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonValidarJugada, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonPilaDeFichas, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                        .addComponent(jPanelJugador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanelMesa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(395, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonPilaDeFichasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPilaDeFichasActionPerformed
        // TODO add your handling code here:
        //jButtonValidarJugada.setEnabled(false); //cambiar luego
        if (mazoJugador.isEmpty()) {
            String mensaje = "¡Has ganado!";
            String nombre = JOptionPane.showInputDialog(null, mensaje + "\nIngrese su nombre:");
        } else {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 10; j++) {
                    fichasJugador[i][j].setText("");
                    fichasJugador[i][j].setBackground(Color.black);
                }
            }
            turno = false;
            int maximoFichas = 30;
            mazoJugador = listaMazos.get(0);
            if (mazoJugador.size() >= maximoFichas) {
                System.out.println("Maximo alcanzado");
                for (int j = 0; j < mazoJugador.size(); j++) {
                    Ficha ficha = mazoJugador.get(j);
                    JButton boton = fichasJugador[j / 10][j % 10];
                    int numero = ficha.getNumero();
                    String colorString = ficha.getColor();
                    Color color = Color.black;
                    String texto = " ";

                    if (colorString.equals("Negro")) {
                        color = Color.black;
                    }
                    if (colorString.equals("Rojo")) {
                        color = Color.red;
                    }
                    if (colorString.equals("Azul")) {
                        color = Color.blue;
                    }
                    if (colorString.equals("Amarillo")) {
                        color = Color.yellow;
                    }
                    texto = String.valueOf(numero);
                    if (numero == 0) {
                        FichaComodin fichaComodin = (FichaComodin) ficha;
                        texto = fichaComodin.getSimbolo();
                    }

                    boton.setForeground(color);
                    boton.setText(texto);
                    boton.setBackground(Color.gray);
                }
            } else {
                Random random = new Random();
                int numeroRandom = random.nextInt(fichas.size());
                Ficha fichaRandom = fichas.get(numeroRandom);

                int numFichaRandom = fichaRandom.getNumero();
                String colorFichaRandom = fichaRandom.getColor();
                String numInfo = Integer.toString(numFichaRandom);
                String colorCodigo = "";
                if (colorFichaRandom.equals("Negro")) {
                    colorCodigo = "N";
                }
                if (colorFichaRandom.equals("Rojo")) {
                    colorCodigo = "R";
                }
                if (colorFichaRandom.equals("Azul")) {
                    colorCodigo = "Z";
                }
                if (colorFichaRandom.equals("Amarillo")) {
                    colorCodigo = "A";
                }

                if (numFichaRandom == 0) {
                    fichasInfo.remove("C" + colorCodigo);
                } else {
                    fichasInfo.remove(numInfo + colorCodigo);
                }

                fichas.remove(numeroRandom);

                mazoJugador.add(fichaRandom);
                for (int j = 0; j < mazoJugador.size(); j++) {
                    Ficha ficha = mazoJugador.get(j);
                    JButton boton = fichasJugador[j / 10][j % 10];
                    int numero = ficha.getNumero();
                    String colorString = ficha.getColor();
                    Color color = Color.black;
                    String texto = " ";

                    if (colorString.equals("Negro")) {
                        color = Color.black;
                    }
                    if (colorString.equals("Rojo")) {
                        color = Color.red;
                    }
                    if (colorString.equals("Azul")) {
                        color = Color.blue;
                    }
                    if (colorString.equals("Amarillo")) {
                        color = Color.yellow;
                    }
                    texto = String.valueOf(numero);
                    if (numero == 0) {
                        texto = ":-)";
                    }

                    boton.setForeground(color);
                    boton.setText(texto);
                    boton.setBackground(Color.gray);
                }
            }
            //        if(turno == false){
            //            jButtonPilaDeFichas.setEnabled(false);
            //            ActionListenerHabilitado = false;
            //        } cambiar luego
        }
    }//GEN-LAST:event_jButtonPilaDeFichasActionPerformed

    private void jButtonValidarJugadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonValidarJugadaActionPerformed
        //jButtonPilaDeFichas.setEnabled(true); cambiar luego
        if (jugadas.size() == 1) {
            Jugada jugadaColocada = jugadas.get(0);
            Ficha fichaColocada = jugadaColocada.getFicha();
            int numFichaColocada = fichaColocada.getNumero();
            String colorFichaColocada = fichaColocada.getColor();
            Color color = Color.black;
            if (colorFichaColocada.equals("Negro")) {
                color = Color.black;
            }
            if (colorFichaColocada.equals("Rojo")) {
                color = Color.red;
            }
            if (colorFichaColocada.equals("Azul")) {
                color = Color.blue;
            }
            if (colorFichaColocada.equals("Amarillo")) {
                color = Color.yellow;
            }
            int filaFichaColocada = jugadaColocada.getFila();
            int columnaFichaColocada = jugadaColocada.getColumna();
            int yFichaColocada = jugadaColocada.getFichasJugadorY();
            int xFichaColocada = jugadaColocada.getFichasJugadorX();

            //----------------------------------------------------------------//
            //Limites
            if (columnaFichaColocada + 1 > 19
                    && mesa[filaFichaColocada][columnaFichaColocada - 1].getText().equals("")) {
                System.out.println("Centro: " + mesa[filaFichaColocada][columnaFichaColocada].getText());
                JOptionPane.showMessageDialog(null, "Jugada invalida", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Error,1:1");
                if (numFichaColocada == 0) {
                    fichasJugador[xFichaColocada][yFichaColocada].setText(":-)");
                } else {
                    fichasJugador[xFichaColocada][yFichaColocada].setText(Integer.toString(numFichaColocada));
                }
                fichasJugador[xFichaColocada][yFichaColocada].setForeground(color);
                fichasJugador[xFichaColocada][yFichaColocada].setBackground(Color.gray);
                mesa[filaFichaColocada][columnaFichaColocada].setText("");
                mesa[filaFichaColocada][columnaFichaColocada].setBackground(verdeOscuro);
                mesaInfo[filaFichaColocada][columnaFichaColocada] = "VV";
                mazoJugador.add(fichaColocada);
                System.out.println(mazoJugador.size());
            } else if (columnaFichaColocada - 1 < 0
                    && mesa[filaFichaColocada][columnaFichaColocada + 1].getText().equals("")) {
                System.out.println("Centro: " + mesa[filaFichaColocada][columnaFichaColocada].getText());
                JOptionPane.showMessageDialog(null, "Jugada invalida", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Error,1:2");
                if (numFichaColocada == 0) {
                    fichasJugador[xFichaColocada][yFichaColocada].setText(":-)");
                } else {
                    fichasJugador[xFichaColocada][yFichaColocada].setText(Integer.toString(numFichaColocada));
                }
                fichasJugador[xFichaColocada][yFichaColocada].setForeground(color);
                fichasJugador[xFichaColocada][yFichaColocada].setBackground(Color.gray);
                mesa[filaFichaColocada][columnaFichaColocada].setText("");
                mesa[filaFichaColocada][columnaFichaColocada].setBackground(verdeOscuro);
                mesaInfo[filaFichaColocada][columnaFichaColocada] = "VV";
                mazoJugador.add(fichaColocada);
                System.out.println(mazoJugador.size());
            } //----------------------------------------------------------------//
            else if (columnaFichaColocada + 1 <= 19 && columnaFichaColocada - 1 >= 0
                    && mesa[filaFichaColocada][columnaFichaColocada - 1].getText().equals("")
                    && mesa[filaFichaColocada][columnaFichaColocada + 1].getText().equals("")) {
                System.out.println("izquierda: " + mesa[filaFichaColocada][columnaFichaColocada - 1].getText());
                System.out.println("Centro: " + mesa[filaFichaColocada][columnaFichaColocada].getText());
                System.out.println("Derecha: " + mesa[filaFichaColocada][columnaFichaColocada + 1].getText());
                JOptionPane.showMessageDialog(null, "Jugada invalida", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Error,1:3");
                if (numFichaColocada == 0) {
                    fichasJugador[xFichaColocada][yFichaColocada].setText(":-)");
                } else {
                    fichasJugador[xFichaColocada][yFichaColocada].setText(Integer.toString(numFichaColocada));
                }
                fichasJugador[xFichaColocada][yFichaColocada].setForeground(color);
                fichasJugador[xFichaColocada][yFichaColocada].setBackground(Color.gray);
                mesa[filaFichaColocada][columnaFichaColocada].setText("");
                mesa[filaFichaColocada][columnaFichaColocada].setBackground(verdeOscuro);
                mesaInfo[filaFichaColocada][columnaFichaColocada] = "VV";
                mazoJugador.add(fichaColocada);
                System.out.println(mazoJugador.size());
            } else if (columnaFichaColocada - 1 > 0 && mesa[filaFichaColocada][columnaFichaColocada - 1].getText().equals(":-)")
                    || (columnaFichaColocada - 1 > 0 && mesa[filaFichaColocada][columnaFichaColocada - 1].getText().equals(Integer.toString(numFichaColocada - 1))
                    && mesa[filaFichaColocada][columnaFichaColocada - 1].getForeground().equals(color))) {
                try {
                    cliente.salida.writeInt(7);
                    cliente.salida.writeInt(rows);
                    cliente.salida.writeInt(columns);

                    for (String[] row : mesaInfo) {
                        for (String code : row) {
                            cliente.salida.writeUTF(code);
                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (columnaFichaColocada + 1 < 20 && mesa[filaFichaColocada][columnaFichaColocada + 1].getText().equals(":-)")
                    || (columnaFichaColocada + 1 < 20 && mesa[filaFichaColocada][columnaFichaColocada + 1].getText().equals(Integer.toString(numFichaColocada + 1))
                    && mesa[filaFichaColocada][columnaFichaColocada + 1].getForeground().equals(color))) {
                try {
                    cliente.salida.writeInt(7);
                    cliente.salida.writeInt(rows);
                    cliente.salida.writeInt(columns);

                    for (String[] row : mesaInfo) {
                        for (String code : row) {
                            cliente.salida.writeUTF(code);
                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (mesa[filaFichaColocada][columnaFichaColocada].getText().equals(":-)")
                    || (mesa[filaFichaColocada][columnaFichaColocada - 1].getText().equals(Integer.toString(numFichaColocada))
                    || mesa[filaFichaColocada][columnaFichaColocada - 1].getText().equals(":-)"))
                    && (mesa[filaFichaColocada][columnaFichaColocada - 2].getText().equals(Integer.toString(numFichaColocada))
                    || mesa[filaFichaColocada][columnaFichaColocada - 2].getText().equals(":-)"))
                    && (mesa[filaFichaColocada][columnaFichaColocada - 3].getText().equals(Integer.toString(numFichaColocada))
                    || mesa[filaFichaColocada][columnaFichaColocada - 3].getText().equals(":-)"))
                    && (!mesa[filaFichaColocada][columnaFichaColocada - 1].getForeground().equals(color)
                    || mesa[filaFichaColocada][columnaFichaColocada - 1].getText().equals(":-)"))
                    && (!mesa[filaFichaColocada][columnaFichaColocada - 2].getForeground().equals(color)
                    || mesa[filaFichaColocada][columnaFichaColocada - 2].getText().equals(":-)"))
                    && (!mesa[filaFichaColocada][columnaFichaColocada - 3].getForeground().equals(color)
                    || mesa[filaFichaColocada][columnaFichaColocada - 3].getText().equals(":-)"))) {
                try {
                    cliente.salida.writeInt(7);
                    cliente.salida.writeInt(rows);
                    cliente.salida.writeInt(columns);

                    for (String[] row : mesaInfo) {
                        for (String code : row) {
                            cliente.salida.writeUTF(code);
                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if ((mesa[filaFichaColocada][columnaFichaColocada].getText().equals(":-)")
                    || mesa[filaFichaColocada][columnaFichaColocada + 1].getText().equals(Integer.toString(numFichaColocada))
                    || mesa[filaFichaColocada][columnaFichaColocada + 1].getText().equals(":-)"))
                    && (mesa[filaFichaColocada][columnaFichaColocada + 2].getText().equals(Integer.toString(numFichaColocada))
                    || mesa[filaFichaColocada][columnaFichaColocada + 2].getText().equals(":-)"))
                    && (mesa[filaFichaColocada][columnaFichaColocada + 3].getText().equals(Integer.toString(numFichaColocada))
                    || mesa[filaFichaColocada][columnaFichaColocada + 3].getText().equals(":-)"))
                    && (!mesa[filaFichaColocada][columnaFichaColocada + 1].getForeground().equals(color)
                    || mesa[filaFichaColocada][columnaFichaColocada + 1].getText().equals(":-)"))
                    && (!mesa[filaFichaColocada][columnaFichaColocada + 2].getForeground().equals(color)
                    || mesa[filaFichaColocada][columnaFichaColocada + 2].getText().equals(":-)"))
                    && (!mesa[filaFichaColocada][columnaFichaColocada + 3].getForeground().equals(color)
                    || mesa[filaFichaColocada][columnaFichaColocada + 3].getText().equals(":-)"))) {
                try {
                    cliente.salida.writeInt(7);
                    cliente.salida.writeInt(rows);
                    cliente.salida.writeInt(columns);

                    for (String[] row : mesaInfo) {
                        for (String code : row) {
                            cliente.salida.writeUTF(code);
                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Jugada invalida", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Error,1:4");
                if (numFichaColocada == 0) {
                    fichasJugador[xFichaColocada][yFichaColocada].setText(":-)");
                } else {
                    fichasJugador[xFichaColocada][yFichaColocada].setText(Integer.toString(numFichaColocada));
                }
                fichasJugador[xFichaColocada][yFichaColocada].setForeground(color);
                fichasJugador[xFichaColocada][yFichaColocada].setBackground(Color.gray);
                mesa[filaFichaColocada][columnaFichaColocada].setText("");
                mesa[filaFichaColocada][columnaFichaColocada].setBackground(verdeOscuro);
                mesaInfo[filaFichaColocada][columnaFichaColocada] = "VV";
                mazoJugador.add(fichaColocada);
                System.out.println(mazoJugador.size());
            }
        }
        if (jugadas.size() == 2) {
            JOptionPane.showMessageDialog(null, "Jugada invalida", "Error", JOptionPane.ERROR_MESSAGE);
            for (Jugada jugada : jugadas) {
                Ficha fichaColocada = jugada.getFicha();
                int numFichaColocada = fichaColocada.getNumero();
                String colorFichaColocada = fichaColocada.getColor();
                Color color = Color.black;
                if (colorFichaColocada.equals("Negro")) {
                    color = Color.black;
                }
                if (colorFichaColocada.equals("Rojo")) {
                    color = Color.red;
                }
                if (colorFichaColocada.equals("Azul")) {
                    color = Color.blue;
                }
                if (colorFichaColocada.equals("Amarillo")) {
                    color = Color.yellow;
                }
                int filaFichaColocada = jugada.getFila();
                int columnaFichaColocada = jugada.getColumna();
                int yFichaColocada = jugada.getFichasJugadorY();
                int xFichaColocada = jugada.getFichasJugadorX();
                if (numFichaColocada == 0) {
                    fichasJugador[xFichaColocada][yFichaColocada].setText(":-)");
                } else {
                    fichasJugador[xFichaColocada][yFichaColocada].setText(Integer.toString(numFichaColocada));
                }
                fichasJugador[xFichaColocada][yFichaColocada].setForeground(color);
                fichasJugador[xFichaColocada][yFichaColocada].setBackground(Color.gray);
                mesa[filaFichaColocada][columnaFichaColocada].setText("");
                mesa[filaFichaColocada][columnaFichaColocada].setBackground(verdeOscuro);
                mesaInfo[filaFichaColocada][columnaFichaColocada] = "VV";
                mazoJugador.add(fichaColocada);
            }
        }
        if (jugadas.size() == 3) {
            Jugada jugada1 = jugadas.get(0);
            int filaFichaColocada1 = jugada1.getFila();
            int columnaFichaColocada1 = jugada1.getColumna();
            Ficha fichaColocada1 = jugada1.getFicha();
            int numFichaColocada1 = fichaColocada1.getNumero();
            String colorFichaColocada = fichaColocada1.getColor();
            Color color1 = Color.black;
            int yFichaColocada1 = jugada1.getFichasJugadorY();
            int xFichaColocada1 = jugada1.getFichasJugadorX();
            if (colorFichaColocada.equals("Negro")) {
                color1 = Color.black;
            }
            if (colorFichaColocada.equals("Rojo")) {
                color1 = Color.red;
            }
            if (colorFichaColocada.equals("Azul")) {
                color1 = Color.blue;
            }
            if (colorFichaColocada.equals("Amarillo")) {
                color1 = Color.yellow;
            }

            Jugada jugada2 = jugadas.get(1);
            int filaFichaColocada2 = jugada2.getFila();
            int columnaFichaColocada2 = jugada2.getColumna();
            Ficha fichaColocada2 = jugada2.getFicha();
            int numFichaColocada2 = fichaColocada2.getNumero();
            String colorFichaColocada2 = fichaColocada2.getColor();
            Color color2 = Color.black;
            int yFichaColocada2 = jugada2.getFichasJugadorY();
            int xFichaColocada2 = jugada2.getFichasJugadorX();
            if (colorFichaColocada2.equals("Negro")) {
                color2 = Color.black;
            }
            if (colorFichaColocada2.equals("Rojo")) {
                color2 = Color.red;
            }
            if (colorFichaColocada2.equals("Azul")) {
                color2 = Color.blue;
            }
            if (colorFichaColocada2.equals("Amarillo")) {
                color2 = Color.yellow;
            }

            Jugada jugada3 = jugadas.get(2);
            int filaFichaColocada3 = jugada3.getFila();
            int columnaFichaColocada3 = jugada3.getColumna();
            Ficha fichaColocada3 = jugada3.getFicha();
            int numFichaColocada3 = fichaColocada3.getNumero();
            String colorFichaColocada3 = fichaColocada3.getColor();
            Color color3 = Color.black;
            int yFichaColocada3 = jugada3.getFichasJugadorY();
            int xFichaColocada3 = jugada3.getFichasJugadorX();
            if (colorFichaColocada3.equals("Negro")) {
                color3 = Color.black;
            }
            if (colorFichaColocada3.equals("Rojo")) {
                color3 = Color.red;
            }
            if (colorFichaColocada3.equals("Azul")) {
                color3 = Color.blue;
            }
            if (colorFichaColocada3.equals("Amarillo")) {
                color3 = Color.yellow;
            }

            if (filaFichaColocada1 != filaFichaColocada2 || filaFichaColocada1 != filaFichaColocada3
                    || filaFichaColocada2 != filaFichaColocada3) {
                JOptionPane.showMessageDialog(null, "Jugada invalida", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Error,3:1");
                if (numFichaColocada1 == 0) {
                    fichasJugador[xFichaColocada1][yFichaColocada1].setText(":-)");
                } else {
                    fichasJugador[xFichaColocada1][yFichaColocada1].setText(Integer.toString(numFichaColocada1));
                }
                fichasJugador[xFichaColocada1][yFichaColocada1].setForeground(color1);
                fichasJugador[xFichaColocada1][yFichaColocada1].setBackground(Color.gray);
                mesa[filaFichaColocada1][columnaFichaColocada1].setText("");
                mesa[filaFichaColocada1][columnaFichaColocada1].setBackground(verdeOscuro);
                mesaInfo[filaFichaColocada1][columnaFichaColocada1] = "VV";
                mazoJugador.add(fichaColocada1);

                if (numFichaColocada2 == 0) {
                    fichasJugador[xFichaColocada2][yFichaColocada2].setText(":-)");
                } else {
                    fichasJugador[xFichaColocada2][yFichaColocada2].setText(Integer.toString(numFichaColocada2));
                }
                fichasJugador[xFichaColocada2][yFichaColocada2].setForeground(color2);
                fichasJugador[xFichaColocada2][yFichaColocada2].setBackground(Color.gray);
                mesa[filaFichaColocada2][columnaFichaColocada2].setText("");
                mesa[filaFichaColocada2][columnaFichaColocada2].setBackground(verdeOscuro);
                mesaInfo[filaFichaColocada2][columnaFichaColocada2] = "VV";
                mazoJugador.add(fichaColocada2);

                if (numFichaColocada3 == 0) {
                    fichasJugador[xFichaColocada3][yFichaColocada3].setText(":-)");
                } else {
                    fichasJugador[xFichaColocada3][yFichaColocada3].setText(Integer.toString(numFichaColocada3));
                }
                fichasJugador[xFichaColocada3][yFichaColocada3].setForeground(color3);
                fichasJugador[xFichaColocada3][yFichaColocada3].setBackground(Color.gray);
                mesa[filaFichaColocada3][columnaFichaColocada3].setText("");
                mesa[filaFichaColocada3][columnaFichaColocada3].setBackground(verdeOscuro);
                mesaInfo[filaFichaColocada3][columnaFichaColocada3] = "VV";
                mazoJugador.add(fichaColocada3);
            } else if (abs(columnaFichaColocada1 - columnaFichaColocada2) != 1 || abs(columnaFichaColocada2 - columnaFichaColocada3) != 1) {
                JOptionPane.showMessageDialog(null, "Jugada invalida", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Error,3:2");
                if (numFichaColocada1 == 0) {
                    fichasJugador[xFichaColocada1][yFichaColocada1].setText(":-)");
                } else {
                    fichasJugador[xFichaColocada1][yFichaColocada1].setText(Integer.toString(numFichaColocada1));
                }
                fichasJugador[xFichaColocada1][yFichaColocada1].setForeground(color1);
                fichasJugador[xFichaColocada1][yFichaColocada1].setBackground(Color.gray);
                mesa[filaFichaColocada1][columnaFichaColocada1].setText("");
                mesa[filaFichaColocada1][columnaFichaColocada1].setBackground(verdeOscuro);
                mesaInfo[filaFichaColocada1][columnaFichaColocada1] = "VV";
                mazoJugador.add(fichaColocada1);

                if (numFichaColocada2 == 0) {
                    fichasJugador[xFichaColocada2][yFichaColocada2].setText(":-)");
                } else {
                    fichasJugador[xFichaColocada2][yFichaColocada2].setText(Integer.toString(numFichaColocada2));
                }
                fichasJugador[xFichaColocada2][yFichaColocada2].setForeground(color2);
                fichasJugador[xFichaColocada2][yFichaColocada2].setBackground(Color.gray);
                mesa[filaFichaColocada2][columnaFichaColocada2].setText("");
                mesa[filaFichaColocada2][columnaFichaColocada2].setBackground(verdeOscuro);
                mesaInfo[filaFichaColocada2][columnaFichaColocada2] = "VV";
                mazoJugador.add(fichaColocada2);

                if (numFichaColocada3 == 0) {
                    fichasJugador[xFichaColocada3][yFichaColocada3].setText(":-)");
                } else {
                    fichasJugador[xFichaColocada3][yFichaColocada3].setText(Integer.toString(numFichaColocada3));
                }
                fichasJugador[xFichaColocada3][yFichaColocada3].setForeground(color3);
                fichasJugador[xFichaColocada3][yFichaColocada3].setBackground(Color.gray);
                mesa[filaFichaColocada3][columnaFichaColocada3].setText("");
                mesa[filaFichaColocada3][columnaFichaColocada3].setBackground(verdeOscuro);
                mesaInfo[filaFichaColocada3][columnaFichaColocada3] = "VV";
                mazoJugador.add(fichaColocada3);
            } else if ((numFichaColocada1 + 1 == numFichaColocada2 || numFichaColocada1 == 0 || numFichaColocada2 == 0)
                    && (numFichaColocada2 + 1 == numFichaColocada3 || numFichaColocada2 == 0 || numFichaColocada3 == 0)
                    && (colorFichaColocada.equals(colorFichaColocada2) || numFichaColocada1 == 0 || numFichaColocada2 == 0)
                    && (colorFichaColocada2.equals(colorFichaColocada3) || numFichaColocada2 == 0 || numFichaColocada3 == 0)
                    && (colorFichaColocada.equals(colorFichaColocada3) || numFichaColocada1 == 0 || numFichaColocada3 == 0)) {
                try {
                    cliente.salida.writeInt(7);
                    cliente.salida.writeInt(rows);
                    cliente.salida.writeInt(columns);

                    for (String[] row : mesaInfo) {
                        for (String code : row) {
                            cliente.salida.writeUTF(code);
                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if ((numFichaColocada1 == numFichaColocada2 || numFichaColocada1 == 0 || numFichaColocada2 == 0) && (numFichaColocada2 == numFichaColocada3 || numFichaColocada2 == 0 || numFichaColocada3 == 0)
                    && (!colorFichaColocada.equals(colorFichaColocada2) || numFichaColocada1 == 0 || numFichaColocada2 == 0)
                    && (!colorFichaColocada2.equals(colorFichaColocada3) || numFichaColocada2 == 0 || numFichaColocada3 == 0)
                    && (!colorFichaColocada.equals(colorFichaColocada3) || numFichaColocada1 == 0 || numFichaColocada3 == 0)) {
                try {
                    cliente.salida.writeInt(7);
                    cliente.salida.writeInt(rows);
                    cliente.salida.writeInt(columns);

                    for (String[] row : mesaInfo) {
                        for (String code : row) {
                            cliente.salida.writeUTF(code);
                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Num iguales, colores diferentes");
            } else {
                JOptionPane.showMessageDialog(null, "Jugada invalida", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Error,3:3");
                if (numFichaColocada1 == 0) {
                    fichasJugador[xFichaColocada1][yFichaColocada1].setText(":-)");
                } else {
                    fichasJugador[xFichaColocada1][yFichaColocada1].setText(Integer.toString(numFichaColocada1));
                }
                fichasJugador[xFichaColocada1][yFichaColocada1].setForeground(color1);
                fichasJugador[xFichaColocada1][yFichaColocada1].setBackground(Color.gray);
                mesa[filaFichaColocada1][columnaFichaColocada1].setText("");
                mesa[filaFichaColocada1][columnaFichaColocada1].setBackground(verdeOscuro);
                mesaInfo[filaFichaColocada1][columnaFichaColocada1] = "VV";
                mazoJugador.add(fichaColocada1);

                if (numFichaColocada2 == 0) {
                    fichasJugador[xFichaColocada2][yFichaColocada2].setText(":-)");
                } else {
                    fichasJugador[xFichaColocada2][yFichaColocada2].setText(Integer.toString(numFichaColocada2));
                }
                fichasJugador[xFichaColocada2][yFichaColocada2].setForeground(color2);
                fichasJugador[xFichaColocada2][yFichaColocada2].setBackground(Color.gray);
                mesa[filaFichaColocada2][columnaFichaColocada2].setText("");
                mesa[filaFichaColocada2][columnaFichaColocada2].setBackground(verdeOscuro);
                mesaInfo[filaFichaColocada2][columnaFichaColocada2] = "VV";
                mazoJugador.add(fichaColocada2);

                if (numFichaColocada3 == 0) {
                    fichasJugador[xFichaColocada3][yFichaColocada3].setText(":-)");
                } else {
                    fichasJugador[xFichaColocada3][yFichaColocada3].setText(Integer.toString(numFichaColocada3));
                }
                fichasJugador[xFichaColocada3][yFichaColocada3].setForeground(color3);
                fichasJugador[xFichaColocada3][yFichaColocada3].setBackground(Color.gray);
                mesa[filaFichaColocada3][columnaFichaColocada3].setText("");
                mesa[filaFichaColocada3][columnaFichaColocada3].setBackground(verdeOscuro);
                mesaInfo[filaFichaColocada3][columnaFichaColocada3] = "VV";
                mazoJugador.add(fichaColocada3);
            }
        }

        if (jugadas.size() > 3) {
            JOptionPane.showMessageDialog(null, "Jugada invalida", "Error", JOptionPane.ERROR_MESSAGE);
            for (Jugada jugada : jugadas) {
                Ficha fichaColocada = jugada.getFicha();
                int numFichaColocada = fichaColocada.getNumero();
                String colorFichaColocada = fichaColocada.getColor();
                Color color = Color.black;
                if (colorFichaColocada.equals("Negro")) {
                    color = Color.black;
                }
                if (colorFichaColocada.equals("Rojo")) {
                    color = Color.red;
                }
                if (colorFichaColocada.equals("Azul")) {
                    color = Color.blue;
                }
                if (colorFichaColocada.equals("Amarillo")) {
                    color = Color.yellow;
                }
                int filaFichaColocada = jugada.getFila();
                int columnaFichaColocada = jugada.getColumna();
                int yFichaColocada = jugada.getFichasJugadorY();
                int xFichaColocada = jugada.getFichasJugadorX();
                if (numFichaColocada == 0) {
                    fichasJugador[xFichaColocada][yFichaColocada].setText(":-)");
                } else {
                    fichasJugador[xFichaColocada][yFichaColocada].setText(Integer.toString(numFichaColocada));
                }
                fichasJugador[xFichaColocada][yFichaColocada].setForeground(color);
                fichasJugador[xFichaColocada][yFichaColocada].setBackground(Color.gray);
                mesa[filaFichaColocada][columnaFichaColocada].setText("");
                mesa[filaFichaColocada][columnaFichaColocada].setBackground(verdeOscuro);
                mesaInfo[filaFichaColocada][columnaFichaColocada] = "VV";
                mazoJugador.add(fichaColocada);
            }
        }

        if (puntosDelPrimerTurno >= 30) {
            primerTurnoValido = true;
        } else {
            JOptionPane.showMessageDialog(null, "Jugada invalida", "Error", JOptionPane.ERROR_MESSAGE);
            for (Jugada jugada : jugadas) {
                Ficha fichaColocada = jugada.getFicha();
                int numFichaColocada = fichaColocada.getNumero();
                String colorFichaColocada = fichaColocada.getColor();
                Color color = Color.black;
                if (colorFichaColocada.equals("Negro")) {
                    color = Color.black;
                }
                if (colorFichaColocada.equals("Rojo")) {
                    color = Color.red;
                }
                if (colorFichaColocada.equals("Azul")) {
                    color = Color.blue;
                }
                if (colorFichaColocada.equals("Amarillo")) {
                    color = Color.yellow;
                }
                int filaFichaColocada = jugada.getFila();
                int columnaFichaColocada = jugada.getColumna();
                int yFichaColocada = jugada.getFichasJugadorY();
                int xFichaColocada = jugada.getFichasJugadorX();
                if (numFichaColocada == 0) {
                    fichasJugador[xFichaColocada][yFichaColocada].setText(":-)");
                } else {
                    fichasJugador[xFichaColocada][yFichaColocada].setText(Integer.toString(numFichaColocada));
                }
                fichasJugador[xFichaColocada][yFichaColocada].setForeground(color);
                fichasJugador[xFichaColocada][yFichaColocada].setBackground(Color.gray);
                mesa[filaFichaColocada][columnaFichaColocada].setText("");
                mesa[filaFichaColocada][columnaFichaColocada].setBackground(verdeOscuro);
                mesaInfo[filaFichaColocada][columnaFichaColocada] = "VV";
                mazoJugador.add(fichaColocada);
            }
            puntosDelPrimerTurno = 0;
            System.out.println("Error, puntos del primer turno deben ser mayor a 30");
        }

        System.out.println(jugadas.size());
        jugadas.clear();
        System.out.println(jugadas.size());
    }//GEN-LAST:event_jButtonValidarJugadaActionPerformed

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
    private javax.swing.JButton jButtonValidarJugada;
    private javax.swing.JPanel jPanelJugador;
    private javax.swing.JPanel jPanelMesa;
    // End of variables declaration//GEN-END:variables
}
