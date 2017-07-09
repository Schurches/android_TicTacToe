package com.example.steven.triqui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class TriquiView extends Activity {


    private int[] cuadros;
    private int[] jugadas;
    private boolean isVs;
    private boolean nightmareMode;
    private boolean gameStarted;
    private int playerMove;

    private int anchuraCasillas;
    private int alturaCasillas;
    private final int grosorMarcos = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triqui_view);
        cuadros = new int[9];
        cuadros[0] = R.id.c1;
        cuadros[1] = R.id.c2;
        cuadros[2] = R.id.c3;
        cuadros[3] = R.id.c4;
        cuadros[4] = R.id.c5;
        cuadros[5] = R.id.c6;
        cuadros[6] = R.id.c7;
        cuadros[7] = R.id.c8;
        cuadros[8] = R.id.c9;
    }

    public void iniciar(View view){
        gameStarted = true;
        int mode = view.getId();
        switch(mode){
            case R.id.easyMode:
                this.isVs = false;
                this.playerMove = 1;
                this.nightmareMode = false;
                break;
            case R.id.hardMode:
                this.isVs = false;
                this.playerMove = 1;
                this.nightmareMode = true;
                break;
            case R.id.versusMode:
                this.isVs = true;
                this.playerMove = 1;
                break;
        }
        habilitarBotones(false);
        jugadas = new int[9];
        for (int cuadro: cuadros){
            ImageView casilla = (ImageView) findViewById(cuadro);
            anchuraCasillas = alturaCasillas = casilla.getWidth();
            Bitmap fondo = Bitmap.createBitmap(alturaCasillas, alturaCasillas, Bitmap.Config.ARGB_8888);
            Canvas C = new Canvas(fondo);
            Paint P = new Paint();
            P.setColor(Color.BLACK);
            C.drawPaint(P);
            P.setColor(Color.WHITE);
            C.drawRect(grosorMarcos,grosorMarcos,anchuraCasillas-grosorMarcos,alturaCasillas-grosorMarcos,P);
            casilla.setImageBitmap(fondo);
        }
    }

    public int obtenerJugada(View cuadro){
        for (int i = 0; i < 9; i++){
            if(cuadros[i]==cuadro.getId()){
                return i;
            }
        }
        return -1;
    }

    public void habilitarBotones(boolean estado){
        findViewById(R.id.easyMode).setEnabled(estado);
        findViewById(R.id.hardMode).setEnabled(estado);
        findViewById(R.id.versusMode).setEnabled(estado);
    }

    public void mostrarGanador(){
        switch(Juego.empate(jugadas)){
            case 0: //empate
                Toast.makeText(this,R.string.empate, Toast.LENGTH_LONG).show();
                habilitarBotones(true);
                break;
            case 1: //Gana jugador 1
                Toast.makeText(this, R.string.win1, Toast.LENGTH_LONG).show();
                habilitarBotones(true);
                break;
            case 2: //Gana jugador 2
                Toast.makeText(this, R.string.win2, Toast.LENGTH_LONG).show();
                habilitarBotones(true);
                break;
        }
    }

    public void juegaAI(){
        int proxMovimiento = Juego.mejorMovimiento(jugadas, nightmareMode);
        jugadas[proxMovimiento] = 2;
        ImageView casilla = (ImageView) findViewById(cuadros[proxMovimiento]);
        if(nightmareMode){
            pintarMovimiento(casilla, false, Color.RED);
        }else{
            pintarMovimiento(casilla, false, Color.BLUE);
        }
        playerMove = 1;
    }

    public void pintarMovimiento(ImageView casilla, boolean jugador1, int colorJugada){
        Paint pincel = new Paint();
        Bitmap dibujo = Bitmap.createBitmap(alturaCasillas, alturaCasillas, Bitmap.Config.ARGB_8888);
        Canvas lienzo = new Canvas(dibujo);
        pincel.setColor(Color.BLACK);
        lienzo.drawPaint(pincel);
        pincel.setColor(Color.WHITE);
        lienzo.drawRect(grosorMarcos,grosorMarcos,anchuraCasillas-grosorMarcos,alturaCasillas-grosorMarcos,pincel);
        if(jugador1){
            pincel.setStrokeWidth(grosorMarcos);
            pincel.setColor(colorJugada);
            lienzo.drawLine(0, 0, anchuraCasillas, alturaCasillas, pincel);
            lienzo.drawLine(anchuraCasillas, 0, 0, alturaCasillas, pincel);
        }else{
            pincel.setColor(colorJugada);
            lienzo.drawCircle(anchuraCasillas/2, alturaCasillas/2, anchuraCasillas/2, pincel);
            pincel.setColor(Color.WHITE);
            lienzo.drawCircle(anchuraCasillas/2, alturaCasillas/2, anchuraCasillas/2-grosorMarcos, pincel);
        }
        casilla.setImageBitmap(dibujo);
    }

    public void elegir(View casilla) {
        if(gameStarted){
            ImageView casillaSeleccionada = (ImageView) findViewById(casilla.getId());
            if (jugadas[obtenerJugada(casilla)] == 0 && Juego.empate(jugadas) == -1) {
                if (isVs) {
                    switch (playerMove) {
                        case 1:
                            pintarMovimiento(casillaSeleccionada, true, Color.RED);
                            jugadas[obtenerJugada(casilla)] = 1;
                            playerMove = 2;
                            break;
                        case 2:
                            pintarMovimiento(casillaSeleccionada, false, Color.GREEN);
                            jugadas[obtenerJugada(casilla)] = 2;
                            playerMove = 1;
                            break;
                    }
                }else{
                    pintarMovimiento(casillaSeleccionada, true, Color.BLACK);
                    jugadas[obtenerJugada(casilla)] = 1;
                    playerMove = 2;
                    if(Juego.empate(jugadas) == -1){
                        juegaAI();
                    }
                }
                mostrarGanador();
            }
        }
    }

}

