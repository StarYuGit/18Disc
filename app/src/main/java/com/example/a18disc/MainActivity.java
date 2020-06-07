package com.example.a18disc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

import android.app.Activity;
import android.app.Application;
import android.media.Image;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //宣告顯示文字用的變數
    TextView player1_money,player2_money,
             player1_result,player2_result,
             message_text,total_bet,
             win_or_lose;
    //顯示按鈕用的變數
    Button plus_1,plus_10,plus_100,plus_1000;
    Button multi_2,division_2,bet_min,bet_max,bet_start;
    //顯示圖片用的變數
    ImageView disc1_1,disc1_2,disc1_3,disc1_4,
              disc2_1,disc2_2,disc2_3,disc2_4;

    int bet_counter = 0, //起始下注總額
        bet = 0; //單注起始金額

    String operator, //宣告運算變數
            temp = "", //暫存用
            gambler = "player1"; //設定起始玩家

    player player1 = new player(); //將玩家實體化
    player player2 = new player(); //將玩家實體化

    Timer timer; //Timer
    TimerTask task; //TimerTask

    Random ran = new Random(); //變數專用

    public class player {
        private int money = 100000;
        private int[] disc_4 = new int[4];
        private int score;
        int t_out = 10;
        public void play_disc(final TextView View) {
            if (timer == null) {
                timer = new Timer();
            }
            if (task == null) {
                task = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                temp = "";
                                t_out--;
                                for (int i=0;i<4;i++){
                                    disc_4[i] = ran.nextInt(6)+1;
                                }
                                for (int i:disc_4){
                                    temp = temp + i;
                                }
                                View.setText(temp);
                                if (t_out == 0) {
                                    temp = "";
                                    t_out = 10;
                                    bet_start.setEnabled(true);
                                    timer.cancel();
                                    timer = null;
                                    task.cancel();
                                    task = null;
                                    for (int i:disc_4){
                                        temp = temp + i;
                                    }
                                    View.setText(temp);
                                    score = chk_disc(disc_4);
                                    View.setText(String.valueOf(score));
                                    change_gambler(score, gambler);
                                    win_or_lose.setText(gambler);
                                }
                            }
                        });
                    }
                };
                timer.schedule(task, 50, 50);
            }
        }
        public int[] get_disc4(){
            return this.disc_4;
        }
        public void init_score(TextView View){
            this.score = 0;
            View.setText("");
        }
        public int get_score(){
            return this.score;
        }
        public void set_money(int m){
            this.money = m;
        }
        public int get_money(){
            return this.money;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player1_money = (TextView) findViewById(R.id.player1_money);
        player2_money = (TextView) findViewById(R.id.player2_money);
        player1_result = (TextView) findViewById(R.id.player1_result);
        player2_result = (TextView) findViewById(R.id.player2_result);
        message_text = (TextView) findViewById(R.id.message_text);
        total_bet = (TextView) findViewById(R.id.total_bet);
        plus_1 = (Button) findViewById(R.id.plus_1);
        plus_10 = (Button) findViewById(R.id.plus_10);
        plus_100 = (Button) findViewById(R.id.plus_100);
        plus_1000 = (Button) findViewById(R.id.plus_1000);
        multi_2 = (Button) findViewById(R.id.multi_2);
        division_2 = (Button) findViewById(R.id.division_2);
        bet_min = (Button) findViewById(R.id.bet_min);
        bet_max = (Button) findViewById(R.id.bet_max);
        bet_start = (Button) findViewById(R.id.bet_start);
        disc1_1 = (ImageView) findViewById(R.id.disc1_1);
        disc1_2 = (ImageView) findViewById(R.id.disc1_2);
        disc1_3 = (ImageView) findViewById(R.id.disc1_3);
        disc1_4 = (ImageView) findViewById(R.id.disc1_4);
        disc2_1 = (ImageView) findViewById(R.id.disc2_1);
        disc2_2 = (ImageView) findViewById(R.id.disc2_2);
        disc2_3 = (ImageView) findViewById(R.id.disc2_3);
        disc2_4 = (ImageView) findViewById(R.id.disc2_4);

        total_bet.setText(String.valueOf(bet_counter));
        win_or_lose = (TextView) findViewById(R.id.win_or_lose);

        player1_money.setText(String.valueOf(player1.get_money()));
        player2_money.setText(String.valueOf(player2.get_money()));
        player1_result.setText("");
        player2_result.setText("");

        plus_1.setOnClickListener(this);
        plus_10.setOnClickListener(this);
        plus_100.setOnClickListener(this);
        plus_1000.setOnClickListener(this);
        multi_2.setOnClickListener(this);
        division_2.setOnClickListener(this);
        bet_min.setOnClickListener(this);
        bet_max.setOnClickListener(this);
        bet_start.setOnClickListener(this);
    }

    public int Betting(String op,int p_money,int b_counter,int b){
        switch (op) {
            case "plus":
                b_counter = b_counter + b;
                break;
            case "multi":
                b_counter = b_counter * 2;
                break;
            case "division":
                b_counter = b_counter / 2;
                break;
            case "min":
                b_counter = 1;
                break;
            case "max":
                b_counter = p_money;
                break;
        }
        if (p_money < b_counter)
            b_counter = p_money;
        bet_counter = b_counter;
        return bet_counter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.plus_1:
                operator = "plus";
                bet = 1;
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), bet_counter, bet)));
                break;
            case R.id.plus_10:
                operator = "plus";
                bet = 10;
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), bet_counter, bet)));
                break;
            case R.id.plus_100:
                operator = "plus";
                bet = 100;
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), bet_counter, bet)));
                break;
            case R.id.plus_1000:
                operator = "plus";
                bet = 1000;
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), bet_counter, bet)));
                break;
            case R.id.multi_2:
                operator = "multi";
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), bet_counter, bet)));
                break;
            case R.id.division_2:
                operator = "division";
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), bet_counter, bet)));
                break;
            case R.id.bet_min:
                operator = "min";
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), bet_counter, bet)));
                break;
            case R.id.bet_max:
                operator = "max";
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), bet_counter, bet)));
                break;
            case R.id.bet_start:
                player1.init_score(player1_result);
                player2.init_score(player1_result);
                if (gambler.equals("player1")) {
                    while (true) {
                        bet_start.setEnabled(false);
                        player1.play_disc(player1_result);
                        if(player1.get_score() != 0)
                            break;
                    }
                }
                if(gambler.equals("player2")) {
                    while (true) {
                        bet_start.setEnabled(false);
                        player2.play_disc(player2_result);
                        if(player2.get_score() != 0)
                            break;
                    }
                }
                if (player1.get_score() != 0 && player2.get_score() != 0)
                    wolresult(player1.get_score(), player2.get_score());
                break;
        }
    }
    public int chk_disc(int[] r) {
        int result = -1;
        Arrays.sort(r);
        if (r[0] == r[1] && r[1] == r[2] && r[2] == r[3]) {
            result = 100;
        } else if (r[0] == r[1] && r[1] == r[2] || r[1] == r[2] && r[2] == r[3]){
            result = 0;
        } else if (r[0] == r[1] && r[2] == r[3]){
            result = r[2] + r[3];

        } else if (r[0] == r[1]) {
            result = r[2] + r[3];
        } else if (r[0] == r[2]) {
            result = r[1] + r[3];
        } else if (r[0] == r[3]) {
            result = r[1] + r[2];

        } else if (r[1] == r[2]) {
            result = r[0] + r[3];
        } else if (r[1] == r[3]) {
            result = r[0] + r[2];

        } else if (r[2] == r[3]) {
            result = r[0] + r[1];
        } else {
            result = 0;
        }
        return result;
    }
    public void change_gambler(int s,String g){
        if (s != 0){
            if(g.equals("player1"))
                gambler = "player2";
            if(g.equals("player2"))
                gambler = "player1";

        }
    }

    public void result_message(int result) {
        message_text.setText("");
        if (result == 100) {
            message_text.setText("十八啦!!1");
        } else if (result == 0) {
            message_text.setText("無點數!\n重骰一次");
        } else {
            message_text.setText(result+"點");
        }
    }

    public void wolresult (int s1, int s2) {
        if (s1 > s2){
            message_text.setText("");
            message_text.setText("你贏了");
        } else if (s1 < s2){
            message_text.setText("");
            message_text.setText("輸了");
        } else {
            message_text.setText("");
            message_text.setText("平手，重來一次");
        }

    }
    /*private void runthread(){
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(5000);
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }*/
}
