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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView player1_money,player2_money,
             player1_result,player2_result,
             message_text,total_bet;

    Button plus_1,plus_10,plus_100,plus_1000;
    Button multi_2,division_2,bet_min,bet_max,bet_start;

    ImageView disc1_1,disc1_2,disc1_3,disc1_4,
              disc2_1,disc2_2,disc2_3,disc2_4;

    int p1_total_money = 1000000,
        bet_counter = 0,
        bet = 0,
        t_out = 10;

    String operator,
            temp = "";

    Timer timer = new Timer();
    Random ran = new Random();
    public class player {
        int money = 1;
        void play_disc(){
            final TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        int[] disc4 = new int[4];
                        @Override
                        public void run() {
                            t_out--;
                            for (int i=0; i<4;i++){
                                disc4[i] = ran.nextInt(6)+1;
                            }
                            for (int i=0; i <4;i++){
                                temp = temp + String.valueOf(disc4[i]);
                            }
                            player1_result.setText(temp);
                            if (t_out == 0){
                                player1_result.setText("");
                                timer.cancel();
                            }
                        }
                    });
                }
            };
            timer.schedule(task, 100, 100);
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
        player player1 = new player();
        player player2 = new player();
        player1_money.setText(String.valueOf(player1.money));
        player2_money.setText(String.valueOf(player2.money));
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

    public int chk_disc(int[] disc_result) {
        int result = -1;
        Arrays.sort(disc_result);
        if (disc_result[0] == disc_result[1] && disc_result[1] == disc_result[2] && disc_result[2] == disc_result[3]) {
            result = 100;
        } else if (disc_result[0] == disc_result[1] && disc_result[1] == disc_result[2] || disc_result[1] == disc_result[2] && disc_result[2] == disc_result[3]){
            result = 0;
        } else if (disc_result[0] != disc_result[1] && disc_result[0] != disc_result[2] && disc_result[0] != disc_result[3]) {
            result = 0;
        } else if (disc_result[0] == disc_result[1] && disc_result[2] == disc_result[3]) {
            result = 90;
        } else if (disc_result[0] == disc_result[1]){
            result = disc_result[3] + disc_result[4];
        } else if (disc_result[1] == disc_result[2]){
            result = disc_result[0] + disc_result[3];
        } else if (disc_result[2] == disc_result[3]){
            result = disc_result[0] + disc_result[1];
        }
        return result;
    }

    public void result_message(int result) {
        if (result == 100) {
            message_text.setText("十八啦!!");
        } else if (result == 90) {
            message_text.setText("十八啦!!");
        } else if (result == 0) {
            message_text.setText("無點數!重骰一次");
        }
    }

    public int Betting(String op,int p_money,int b_counter,int b){
        if (op == "plus"){
            b_counter = b_counter + b;
        } else if (op == "multi"){
            b_counter = b_counter * 2;
        } else if (op == "division") {
            b_counter = b_counter / 2;
        } else if (op == "min"){
            b_counter = 1;
        } else if (op == "max"){
            b_counter = p_money;
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
                total_bet.setText(String.valueOf(Betting(operator, p1_total_money, bet_counter, bet)));
                break;
            case R.id.plus_10:
                operator = "plus";
                bet = 10;
                total_bet.setText(String.valueOf(Betting(operator, p1_total_money, bet_counter, bet)));
                break;
            case R.id.plus_100:
                operator = "plus";
                bet = 100;
                total_bet.setText(String.valueOf(Betting(operator, p1_total_money, bet_counter, bet)));
                break;
            case R.id.plus_1000:
                operator = "plus";
                bet = 1000;
                total_bet.setText(String.valueOf(Betting(operator, p1_total_money, bet_counter, bet)));
                break;
            case R.id.multi_2:
                operator = "multi";
                total_bet.setText(String.valueOf(Betting(operator, p1_total_money, bet_counter, bet)));
                break;
            case R.id.division_2:
                operator = "division";
                total_bet.setText(String.valueOf(Betting(operator, p1_total_money, bet_counter, bet)));
                break;
            case R.id.bet_min:
                operator = "min";
                total_bet.setText(String.valueOf(Betting(operator, p1_total_money, bet_counter, bet)));
                break;
            case R.id.bet_max:
                operator = "max";
                total_bet.setText(String.valueOf(Betting(operator, p1_total_money, bet_counter, bet)));
                break;
            case R.id.bet_start:
                break;
        }
    }
}
