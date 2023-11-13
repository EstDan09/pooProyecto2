/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import Juego.DatosMesa;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Esteban
 */
public class threadServer extends Thread {

    Socket cliente = null;//referencia a socket de comunicacion de cliente
    DataInputStream entrada = null;//Para leer comunicacion
    DataOutputStream salida = null;//Para enviar comunicacion
    String nameUser; //Para el nombre del usuario de esta conexion
    Server servidor;// referencia al servidor
    int numeroDeJugador; //ID de jugador en el server global
    int inGameID = 0;
    String currentPartida = "";
    String statusPlayer = "";
    ArrayList<threadServer> jugadoresEnLinea;
    Partida partida = null;

    public threadServer(Socket clienteOwner, Server servidor, int num) {
        this.cliente = clienteOwner;
        this.servidor = servidor;
        this.numeroDeJugador = num;
        this.jugadoresEnLinea = new ArrayList<>();
        this.nameUser = "por mientras";

    }

    public String getStatusPlayer() {
        return statusPlayer;
    }

    public void setStatusPlayer(String statusPlayer) {
        this.statusPlayer = statusPlayer;
    }

    
    public String getNameUser() {
        return this.nameUser;
    }

    public void setNameUser(String name) {
        this.nameUser = name;
    }
    
    public void refresherToJugadoresEnLinea (ArrayList<threadServer> jugadoresActualizados) {
        this.jugadoresEnLinea = jugadoresActualizados;
        System.out.println("Jugador Agregado");
    }
    
    public void setInGameID(int id) {
        this.inGameID = id;
    }
    
    public void setCurrentPartida(String partida) {
        this.currentPartida = partida;
    }
    
    public String getCurrentPartida() {
        return this.currentPartida;
    }

    public void run() {
        try {
            // inicializa las comunicaciones
            this.entrada = new DataInputStream(cliente.getInputStream());//comunic
            this.salida = new DataOutputStream(cliente.getOutputStream());//comunic
            this.setNameUser(entrada.readUTF());
            System.out.println("Hola soy una nueva conexión de nombre " + getNameUser());

            sendUser();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        int caso = 0;
        while (true) {
            try {
                //Siempre espera leer un int que será la instruccion por hacer
                caso = this.entrada.readInt();
                switch (caso) {
                    case 1:
                        break;
                    case 2:
                        System.out.println("step1");
                        //Recibo lo que un jugador me manda
                        String mensajeRecibido = this.entrada.readUTF();
                        
                        //Mando a todos el mensaje
                        for (threadServer jugador: jugadoresEnLinea) {
                            jugador.salida.writeInt(2);
                            jugador.salida.writeUTF(mensajeRecibido);
                        }
                        break;
                    case 3:
                        System.out.println("ok");
                        int row = this.entrada.readInt();
                        int col = this.entrada.readInt();
                        String[][] recibido = new String[row][col];
                        for (int i = 0; i < row; i++) {
                            for (int j = 0; j < col; j++) {
                                recibido[i][j] = this.entrada.readUTF();
                                System.out.println(recibido[i][j]);
                            }
                        }
                        for (threadServer jugador : jugadoresEnLinea) {
                            jugador.salida.writeInt(3);
                            jugador.salida.writeInt(row);
                            jugador.salida.writeInt(col);
                            for (String[] rows : recibido) {
                                for (String code : rows) {
                                    jugador.salida.writeUTF(code);
                                }
                            }
                        }
                        break;
                    case 4: //host crea
                        System.out.println(jugadoresEnLinea.size());
                        String playerCreating = this.entrada.readUTF();
                        System.out.println("Voy a crear una partida para los jugadores presente");
                        for (threadServer jugador : jugadoresEnLinea) {
                            if( jugador.getNameUser().equals(playerCreating)) {
                                this.partida = new Partida(jugador);             
                            }
                        }
                        break;
                    case 5: //jugador se une
                        
                        String userToJoin = this.entrada.readUTF();
                        String jugadorWillingToJoin = this.entrada.readUTF();
                        
                        for (threadServer jugador : jugadoresEnLinea) {
                            if (jugador.getNameUser().equals(userToJoin)) {
                                jugador.salida.writeInt(5);
                                jugador.salida.writeUTF(jugadorWillingToJoin);
                            }
                        }
                        break;
                    case 6:
                        String jugadorAboutToJoin = this.entrada.readUTF();
                        System.out.println("OYE " + getNameUser() + " " + jugadorAboutToJoin + " se va a meter");
                        
                        for (threadServer jugador : jugadoresEnLinea) {
                            if (jugador.getNameUser().equals(jugadorAboutToJoin)) {
                                partida.addJugador(jugador);
                            }
                        }
                        break;
                    case 7:
                        int rowC = this.entrada.readInt();
                        int colC = this.entrada.readInt();
                        String[][] recibidoC = new String[rowC][colC];
                        for (int i = 0; i < rowC; i++) {
                            for (int j = 0; j < colC; j++) {
                                recibidoC[i][j] = this.entrada.readUTF();
                                System.out.println(recibidoC[i][j]);
                            }
                        }
                        if (partida != null) {
                            for (threadServer jugador : partida.getJugadoresEnPartida()) {
                                jugador.salida.writeInt(3);
                                jugador.salida.writeInt(rowC);
                                jugador.salida.writeInt(colC);
                                for (String[] rows : recibidoC) {
                                    for (String code : rows) {
                                        jugador.salida.writeUTF(code);
                                    }
                                }
                            }
                        }
                        break;
                }
            } catch (IOException e) {
                System.out.println("El cliente termino la conexion");
                break;
            }
        }
            
    }
    
    public void sendUser() throws JsonProcessingException {
        
        for (threadServer jugador: this.jugadoresEnLinea) {
            ArrayList<String> listaParaEnviar = new ArrayList <>();
            for (threadServer jugadorU: this.jugadoresEnLinea) {
                listaParaEnviar.add(jugadorU.getNameUser() + " " + jugadorU.getStatusPlayer());
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(listaParaEnviar);
            
            try {
                jugador.salida.writeInt(1);
                jugador.salida.writeUTF(json);	
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void createGame() throws IOException {
        this.salida.writeInt(6);
    }

}
