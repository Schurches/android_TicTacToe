package com.example.steven.triqui;


/**
 * Created by steven on 7/07/2017.
 */

public class Juego {

    public static int obtenerGanador(int[] casillas){
        final int nohayganador = -1;
        if(casillas[0] == casillas[1] && casillas[1] == casillas[2] && casillas[0]!=0){
            return casillas[0];
        }else if(casillas[3] == casillas[4] && casillas[4] == casillas[5] && casillas[3]!=0){
            return casillas[3];
        }else if(casillas[6] == casillas[7] && casillas[7] == casillas[8] && casillas[6]!=0){
            return casillas[6];
        }else if(casillas[0] == casillas[3] && casillas[3] == casillas[6] && casillas[0]!=0){
            return casillas[0];
        }else if(casillas[0] == casillas[4] && casillas[4] == casillas[8] && casillas[0]!=0){
            return casillas[0];
        }else if(casillas[1] == casillas[4] && casillas[4] == casillas[7] && casillas[1]!=0){
            return casillas[1];
        }else if(casillas[2] == casillas[5] && casillas[5] == casillas[8] && casillas[2]!=0){
            return casillas[2];
        }else if(casillas[2] == casillas[4] && casillas[4] == casillas[6] && casillas[2]!=0){
            return casillas[2];
        }else{
            return nohayganador;
        }
    }

    public static int empate(int[] casillas){
        final int noGanador = 0;
        final int sigueJugando = -1;
        int ganador = obtenerGanador(casillas);
        if(ganador==-1){
            for (int i = 0; i < 9; i++) {
                if(casillas[i]==0){
                    return sigueJugando;
                }
            }
            return noGanador;
        }else{
            return ganador;
        }
    }

    public static int minimax(int[] cuadro, int turnos, boolean maquina, boolean agresivo){
        int estado = empate(cuadro);
        switch(estado){
            case 0: //si hay empatee
                return 0;
            case 1: //si gana el jugador
                return -10+turnos;
            case 2: //si gana la maquina
                return 10-turnos;
        }
        if(maquina){
            int mejorMovimiento = -50000;
            for (int i = 0; i < 9; i++) {
                if(cuadro[i]==0){
                    cuadro[i] = 2;
                    mejorMovimiento = Math.max(mejorMovimiento,minimax(cuadro,turnos+1,false, agresivo));
                    cuadro[i] = 0;
                }
            }
            return mejorMovimiento-turnos;
        }else{
            int mejorMovimiento = 50000;
            for (int i = 0; i < 9; i++) {
                if(cuadro[i]==0){
                    if(agresivo){
                        cuadro[i] = 1; //Analiza el movimiento del oponente
                    }else{
                        cuadro[i] = 2; //Analiza su siguiente movimiento
                    }
                    mejorMovimiento = Math.min(mejorMovimiento,minimax(cuadro,turnos+1,true, agresivo));
                    cuadro[i] = 0;
                }
            }
            return mejorMovimiento+turnos;
        }
    }

    public static int mejorMovimiento(int[] cuadro, boolean aggresive){
        int casilla = 0;
        int maxPuntaje = -50000;
        for (int movimiento = 0; movimiento < 9; movimiento++) {
            if(cuadro[movimiento]==0){ //si esta desocupada la posicion
                cuadro[movimiento] = 2;
                int puntaje = minimax(cuadro,0,false, aggresive);
                cuadro[movimiento] = 0;
                if(puntaje > maxPuntaje){
                    casilla = movimiento;
                    maxPuntaje = puntaje;
                }
            }
        }
        return casilla;
    }

}
