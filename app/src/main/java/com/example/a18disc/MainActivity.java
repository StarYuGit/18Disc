package com.example.a18disc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Image;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //宣告顯示文字用的變數
    TextView player1_money, player2_money,
            player1_result, player2_result,
            message_text, total_bet,
            win_or_lose;
    //顯示按鈕用的變數
    Button plus_1, plus_10, plus_100, plus_1000;
    Button multi_2, division_2, bet_min, bet_max, bet_start;
    //顯示圖片用的變數
    ImageView[] disc1 = new ImageView[4],
                disc2 = new ImageView[4];

    int bet_counter = 0, //起始下注總額
            bet = 0, //下注基數
            default_money = 100000, //預設金額
            default_p1_money = default_money, //設定初始金額
            default_p2_money = default_money, //設定初始金額
            DIALOG_ID; //通知用變數

    String operator, //宣告運算變數
            gambler = "player1"; //設定起始玩家

    player player1 = new player(); //將玩家實體化
    player player2 = new player(); //將玩家實體化

    Timer timer; //Timer
    TimerTask task; //TimerTask

    Random ran = new Random(); //亂數專用
    HandlerThread ht = new HandlerThread("name");
    Handler work, worker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        ht.start();
        worker = new Handler();
        work = new Handler(ht.getLooper());
    }

    public class player {
        private int money;
        private int[] disc_4 = new int[4];
        private int score = 0;
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
                                t_out--;
                                for (int i = 0; i < 4; i++) {
                                    disc_4[i] = ran.nextInt(6) + 1;
                                }
                                if (gambler.equals("player1")){
                                    for (int i=0; i<4; i++)
                                        change_image(disc1[i], disc_4[i]);
                                }
                                if (gambler.equals("player2")){
                                    for (int i=0; i<4; i++)
                                        change_image(disc2[i], disc_4[i]);
                                }

                                if (t_out == 0) {
                                    t_out = 10;
                                    bet_start.setEnabled(true);
                                    timer.cancel();
                                    timer = null;
                                    task.cancel();
                                    task = null;
                                    score = chk_disc(disc_4);
                                    change_gambler_message(score, gambler);
                                    View.setText(String.valueOf(score + " 點"));
                                    if (player1.get_score() != 0 && player2.get_score() !=0){
                                        wolresult(player1.get_score(), player2.get_score());
                                    }
                                    if (player1.get_score() != 0) {
                                        if (gambler.equals("player2")) {
                                            if (player2.get_score() == 0) {
                                                win_or_lose.setText("換對方擲骰子");
                                                worker.post(p2);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                };
                timer.schedule(task, 50, 50);
            }
        }

        void init_score() {
            this.score = 0;
        }

        int get_score() {
            return this.score;
        }

        void set_money(int m) {
            this.money = m;
        }

        int get_money() {
            return this.money;
        }
    }

    public int Betting(String op, int p1_money,int p2_money, int b_counter, int b) {
        switch (op) {
            case "plus":
                b_counter = b_counter + b;
                if (b_counter > p2_money)
                    b_counter = p2_money;
                break;
            case "multi":
                b_counter = b_counter * 2;
                if (b_counter > p2_money)
                    b_counter = p2_money;
                break;
            case "division":
                b_counter = b_counter / 2;
                break;
            case "min":
                b_counter = 1;
                break;
            case "max":
                b_counter = p1_money;
                if(b_counter > p2_money)
                    b_counter = p2_money;
                break;
        }
        if (p1_money < b_counter)
            b_counter = p1_money;
        bet_counter = b_counter;
        return bet_counter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plus_1:
                operator = "plus";
                bet = 1;
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), player2.get_money(), bet_counter, bet)));
                break;
            case R.id.plus_10:
                operator = "plus";
                bet = 10;
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), player2.get_money(),bet_counter, bet)));
                break;
            case R.id.plus_100:
                operator = "plus";
                bet = 100;
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), player2.get_money(),bet_counter, bet)));
                break;
            case R.id.plus_1000:
                operator = "plus";
                bet = 1000;
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), player2.get_money(),bet_counter, bet)));
                break;
            case R.id.multi_2:
                operator = "multi";
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), player2.get_money(),bet_counter, bet)));
                break;
            case R.id.division_2:
                operator = "division";
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), player2.get_money(),bet_counter, bet)));
                break;
            case R.id.bet_min:
                operator = "min";
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), player2.get_money(),bet_counter, bet)));
                break;
            case R.id.bet_max:
                operator = "max";
                total_bet.setText(String.valueOf(Betting(operator, player1.get_money(), player2.get_money(),bet_counter, bet)));
                break;
            case R.id.bet_start:
                if (bet_counter == 0){
                    message_text.setText("請下注!!!");
                } else {
                    if (player1.get_score() != 0 && player2.get_score() != 0)
                        player1_result.setText("");
                    player2_result.setText("");
                    if (gambler.equals("player1")) {
                        bet_start.setEnabled(false);
                        btn_enable();
                        worker.post(p1);
                    }
                }
        }
    }

    public int chk_disc(int[] r) {
        int result = -1;
        Arrays.sort(r);
        if (r[0] == r[1] && r[1] == r[2] && r[2] == r[3]) {
            result = 100;
        } else if (r[0] == r[1] && r[1] == r[2] || r[1] == r[2] && r[2] == r[3]) {
            result = 0;
        } else if (r[0] == r[1] && r[2] == r[3]) {
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

    public void change_gambler_message(int s, String g) {
        message_text.setText("");
        if (s == 0) {
            message_text.setText("無點數!\n重骰一次");
        }  else {
            if (g.equals("player1"))
                gambler = "player2";
            if (g.equals("player2"))
                gambler = "player1";
            if (s == 100)
                message_text.setText("十八啦!!");
        }

    }

    public void wolresult(int s1, int s2) {
        if (s1 > s2) {
            message_text.setText("");
            message_text.setText("你贏了");
            player1.set_money(player1.get_money() + bet_counter);
            player2.set_money(player2.get_money() - bet_counter);
            player1_money.setText(String.valueOf(player1.get_money()));
            player2_money.setText(String.valueOf(player2.get_money()));
            bet_counter = 0;
            total_bet.setText(String.valueOf(bet_counter));
            win_or_lose.setText("換你下注");

        } else if (s1 < s2) {
            message_text.setText("");
            message_text.setText("你輸了");
            player1.set_money(player1.get_money() - bet_counter);
            player2.set_money(player2.get_money() + bet_counter);
            bet_counter = 0;
            total_bet.setText(String.valueOf(bet_counter));
            win_or_lose.setText("換你下注");

        } else {
            message_text.setText("");
            message_text.setText("平手\n重來一次");
            message_text.setGravity(1);
            total_bet.setText(String.valueOf(bet_counter));
            win_or_lose.setText("");
        }
        player1.init_score();
        player2.init_score();
        btn_disable();
        if (player1.get_money() < 1){
            player1.set_money(0);
            player1_money.setText(String.valueOf(player1.get_money()));
            player2_money.setText(String.valueOf(player2.get_money()));
            player1_money.setTextColor(Color.RED);
            win_or_lose.setText("");
            DIALOG_ID = 1;
            showDialog(DIALOG_ID);
        } else if (player2.get_money() < 1) {
            player2.set_money(0);
            player1_money.setText(String.valueOf(player1.get_money()));
            player2_money.setText(String.valueOf(player2.get_money()));
            player2_money.setTextColor(Color.RED);
            win_or_lose.setText("");
            DIALOG_ID = 2;
            showDialog(DIALOG_ID);
        }
    }

    private Runnable p1 = new Runnable() {
        @Override
        public void run() {
            try{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bet_start.setEnabled(false);
                        player1.play_disc(player1_result);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Runnable p2 = new Runnable() {
        @Override
        public void run() {
            try{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bet_start.setEnabled(false);
                        player2.play_disc(player2_result);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(ht!=null)
            ht.quit();
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        Dialog dialog = null;

        switch(id)
        {
            case 1:
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("訊息")
                    .setMessage("你破產了!!!")
                    .setNegativeButton("重新開始", new DialogInterface.OnClickListener()
                    { //設定確定按鈕
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            reset();
                            dialog.dismiss();
                        }
                    });
            dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            break;

            case 2:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("訊息") //設定標題文字
                        // .setMessage("對方破產了\n你贏了!!!") //設定內容文字
                        .setNegativeButton("重新開始", new DialogInterface.OnClickListener()
                        { //設定確定按鈕
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                reset();
                                dialog.dismiss();
                            }
                        });
                dialog = builder.create();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                break;

            default:
                break;
        }
        return dialog;
    }

    public void init(){
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

        disc1[0] = (ImageView) findViewById(R.id.disc1_1);
        disc1[1] = (ImageView) findViewById(R.id.disc1_2);
        disc1[2] = (ImageView) findViewById(R.id.disc1_3);
        disc1[3] = (ImageView) findViewById(R.id.disc1_4);
        disc2[0] = (ImageView) findViewById(R.id.disc2_1);
        disc2[1] = (ImageView) findViewById(R.id.disc2_2);
        disc2[2] = (ImageView) findViewById(R.id.disc2_3);
        disc2[3] = (ImageView) findViewById(R.id.disc2_4);

        total_bet.setText(String.valueOf(bet_counter));
        win_or_lose = (TextView) findViewById(R.id.win_or_lose);

        player1.set_money(default_p1_money);
        player2.set_money(default_p2_money);
        player1.init_score();
        player2.init_score();

        player1_money.setText(String.valueOf(player1.get_money()));
        player2_money.setText(String.valueOf(player2.get_money()));
        player1_money.setTextColor(Color.BLACK);
        player2_money.setTextColor(Color.BLACK);
        player1_result.setText("");
        player2_result.setText("");
        message_text.setText("");
        win_or_lose.setText("換你下注");

        plus_1.setOnClickListener(this);
        plus_10.setOnClickListener(this);
        plus_100.setOnClickListener(this);
        plus_1000.setOnClickListener(this);
        multi_2.setOnClickListener(this);
        division_2.setOnClickListener(this);
        bet_min.setOnClickListener(this);
        bet_max.setOnClickListener(this);
        bet_start.setOnClickListener(this);


        for(int i = 0;i<disc1.length;i++) {
            change_image(disc1[i], 6);
        }
        for(int i = 0;i<disc2.length;i++) {
            change_image(disc2[i], 6);
        }
    }

    public void btn_enable(){
        plus_1.setEnabled(false);
        plus_10.setEnabled(false);
        plus_100.setEnabled(false);
        plus_1000.setEnabled(false);
        multi_2.setEnabled(false);
        division_2.setEnabled(false);
        bet_min.setEnabled(false);
        bet_max.setEnabled(false);
    }

    public void btn_disable(){
        plus_1.setEnabled(true);
        plus_10.setEnabled(true);
        plus_100.setEnabled(true);
        plus_1000.setEnabled(true);
        multi_2.setEnabled(true);
        division_2.setEnabled(true);
        bet_min.setEnabled(true);
        bet_max.setEnabled(true);
    }

    public void reset(){
        message_text.setText("");
        init();
    }
     public void change_image(ImageView View, int i){
        switch(i){
            case 1:
                View.setImageResource(R.drawable.disc1);
                break;
            case 2:
                View.setImageResource(R.drawable.disc2);
                break;
            case 3:
                View.setImageResource(R.drawable.disc3);
                break;
            case 4:
                View.setImageResource(R.drawable.disc4);
                break;
            case 5:
                View.setImageResource(R.drawable.disc5);
                break;
            case 6:
                View.setImageResource(R.drawable.disc6);
                break;
        }
     }
     public void delaysometime(int t){
         try{
             runOnUiThread(new Runnable() {    //可以臨時交給UI做顯示
                 public void run(){
                 }
             });
             for(int i=0;i<t;i++){
                 Thread.sleep(1000);
             }
         } catch(Exception e){
             e.printStackTrace();
         }
     }
}
