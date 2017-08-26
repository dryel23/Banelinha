package com.juliao.adryel.myapplication;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

public class Jogo extends AppCompatActivity {
    private static final String PREF_SNAKE = "prefs";
    MediaPlayer mp;//musica do app
    int n;//tamanho do tabuleiro
    int dificult;//nivel de dificuldade
    GridLayout grid;//tabuleiro grid para inflar
    ImageView[][] table;//tabuleiro auxiliar
    ArrayList<int []> snake = new ArrayList<>();//cobra, arrayList de vetor
    int[] orientation = new int[2];//diretção da cobra
    int[] position = new int[2];//posição
    int x;
    int y;
//    int[] fruit = new int[2];
//    int score;
    boolean running = true;//variavel axuliar para pausar ou despausar o jogo
    ImageButton botTop;//botao para cima
    ImageButton botBot;//botao para baixo
    ImageButton botLe;//botao para esquerda
    ImageButton botRi;//botao para direita
//    ImageView headSnake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        grid = (GridLayout) findViewById(R.id.idGrid);//pegando o grid pelo id
        //pegando os botoes
        botBot = (ImageButton) findViewById(R.id.idButtonBot);
        botTop = (ImageButton) findViewById(R.id.idButtonTop);
        botLe = (ImageButton) findViewById(R.id.idButtonLe);
        botRi = (ImageButton) findViewById(R.id.idButtonRi);

        Bundle recuperaDados = getIntent().getExtras();//recuperando os dados do bandle da activity inicial
        if(recuperaDados == null){//verifica se o bundle é vazio, se for inicia com um valor padrão e dificuldade padrao

            Log.i("ENTRA", "entrou no if");
            n = 30;
            dificult = 300;
            grid.setColumnCount(n);
            grid.setRowCount(n);
            table = new ImageView[n][n];
            //for para inflar um layout, ou seja, o gridLayout
            inflatGrid(n);


        }else{

            Log.i("ENTRA", "entrou no ELSE");
            n = recuperaDados.getInt("tamanho");
            dificult = recuperaDados.getInt("dificuldade");
            grid.setColumnCount(n);
            grid.setRowCount(n);
            table = new ImageView[n][n];
            inflatGrid(n);

        }
        //coloca a musica para tocar
        mp = MediaPlayer.create(this, R.raw.spi);
        mp.setLooping(true);
        mp.start();



  createSnake();

    }

    public void inflatGrid(int n){
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){

                LayoutInflater inflar = LayoutInflater.from(this);
                ImageView imageSquares = (ImageView) inflar.inflate(R.layout.square, grid, false);
                table[i][j] = imageSquares;
                grid.addView(imageSquares);


            }
        }
    }


//metodo para setar um quadradinho vermelho
    public void squareRed(ImageView v){

        v.setImageResource(R.drawable.red_square);

    }
    //metodo para setar um quadradinho preto
    public void squareBlack(ImageView v){

        v.setImageResource(R.drawable.square_black);

    }
    //metodo que checa a posição da cabeça
     private void checkposicao(int x, int y) {
         if(x >= n){
             snake.get(0)[0] = 0;
         }
         if(y >= n){
             snake.get(0)[1] = 0;
         }
         if(x == -1){
             snake.get(0)[0] = n-1;
         }
         if(y == -1){
             snake.get(0)[1] = n-1;
         }
     }
    //cabeça da cobra
    public void head(){

        x = snake.get(0)[0];
        y = snake.get(0)[1];
        x += orientation[0];
        y += orientation[1];

        snake.get(0)[0] = x;
        snake.get(0)[1] = y;

        checkposicao(x, y);

    }
    //cria a cobra
    public void createSnake(){

        squareRed(table[n /2][n /2]);
        //cria uma orientação para direita
        orientation[0] = 0;
        orientation[1] = 1;
        //variavel para definir a posição
        int position[] = new int[2];
        position[0] = n/2;
        position[1] = n/2;
        //add a posição na cobra
        snake.add(position);
        //move ela
        move(orientation);

    }
    //thread para mover a cobra
    public void move(final int[] orient){
        //precisa do handler para setar os ui
        final android.os.Handler handler = new android.os.Handler();


        //cria uma thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                //thread roda enquanto a variavel for verdadeira
                while(running){
                    try {
                        Thread.sleep(dificult);//tempo que a thread "dorme', aqui é setado a dificuldade
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //for que percorre a cobra
                        for (int i = 0; i < snake.size(); i++) {
                            int [] position = snake.get(i);

                            //limpa a posição que a cobra passou
                            squareBlack(table[position[0]][position[1]]);


                        }
                        //cabeça da cobra
                        head();
                        for (int i = 0; i < snake.size(); i++){
                            int [] position = snake.get(i);

                            squareRed(table[position[0]][position[1]]);
                        }

                    }
                });
                }
            }
        }).start();

    }



    public void clickLeft(View v){
        botRi.setEnabled(false);
        botBot.setEnabled(true);
        botTop.setEnabled(true);

        orientation[0] = 0;
        orientation[1] = -1;
        /*if(botBot.isPressed()){

        }*/
    }

    public void clickRigh(View v){
        botLe.setEnabled(false);
        botTop.setEnabled(true);
        botBot.setEnabled(true);
        orientation[0] = 0;
        orientation[1] = 1;
    }

    public void clickTop(View v){
        botBot.setEnabled(false);
        botRi.setEnabled(true);
        botLe.setEnabled(true);
        orientation[0] = -1;
        orientation[1] = 0;
    }
    public void clickBottom(View v){
        botTop.setEnabled(false);
        botLe.setEnabled(true);
        botRi.setEnabled(true);
        orientation[0] = 1;
        orientation[1] = 0;
    }
    public void stop(View v){
       running = false;
    }
    public void continued(View v){
        running = true;
        move(orientation);
    }
    public void click2(View v){
        botLe.setEnabled(true);
        botRi.setEnabled(true);
        botBot.setEnabled(false);
        botTop.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();

    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences settings = getSharedPreferences(PREF_SNAKE, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        //editor.putInt("", table[position[0]][position[1]]);
    }
}
