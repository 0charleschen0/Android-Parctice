package com.google.example.scarnesdice;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

  private int userCurrentScore;
  private int userTotalScore;

  private int comCurrentScore;
  private int comTotalScore;

  private static final int COM_TARGET_SCORE = 20;
  private static final int WIN_GAME_SCORE = 100;

  private static final String YOU_WIN_MESSAGE = " You Win!!";
  private static final String COM_WIN_MESSAGE = " Computer Win!!";

  private Random random = new Random();
  private Handler handler = new Handler();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final TextView textView = findViewById(R.id.scoreBoard);
    final ImageView imageView = findViewById(R.id.dice);

    final Button roll = findViewById(R.id.roll);
    final Button reset = findViewById(R.id.reset);
    final Button hold = findViewById(R.id.hold);

    roll.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        userCurrentScore = rollDice(userCurrentScore, imageView);

        //It means user rolls 1
        if (userCurrentScore == 0) {
          computerTurn(roll, hold, textView, imageView);
        }

        textView.setText(getScoreBoardMessage());
      }
    });

    reset.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        reset(textView, imageView, roll ,hold);
      }
    });

    hold.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        updateTotalScore(false, textView);

        if (userTotalScore >= WIN_GAME_SCORE) {
          roll.setEnabled(false);
          hold.setEnabled(false);
        } else {
          computerTurn(roll, hold, textView, imageView);
        }
      }
    });
  }

  private void reset(TextView textView, ImageView imageView, Button roll, Button hold) {
    userCurrentScore = 0;
    userTotalScore = 0;

    comCurrentScore = 0;
    comTotalScore = 0;

    textView.setText(getScoreBoardMessage());
    imageView.setImageDrawable(getDiceImage(1));

    roll.setClickable(true);
    hold.setClickable(true);
  }

  private Drawable getDiceImage(int value) {
    int id;
    switch (value) {
      case 1: id = R.drawable.dice1; break;
      case 2: id = R.drawable.dice2; break;
      case 3: id = R.drawable.dice3; break;
      case 4: id = R.drawable.dice4; break;
      case 5: id = R.drawable.dice5; break;
      case 6: id = R.drawable.dice6; break;
      default: throw new Resources.NotFoundException();
    }
    return getDrawable(id);
  }

  private  String getScoreBoardMessage() {
    return getScoreBoardMessage(false);
  }

  private String getScoreBoardMessage(boolean isComputer) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getString(R.string.scoreboard_message, userTotalScore, comTotalScore))
                  .append('\n');

    if (isComputer) {
      stringBuilder.append(getString(R.string.compter_s_turn_score, comCurrentScore));
    } else {
      stringBuilder.append(getString(R.string.your_turn_score, userCurrentScore));
    }

    if (comTotalScore >= WIN_GAME_SCORE) {
      stringBuilder.append(COM_WIN_MESSAGE);
    } else if (userTotalScore >= WIN_GAME_SCORE) {
      stringBuilder.append(YOU_WIN_MESSAGE);
    }

    return stringBuilder.toString();
  }

  private void computerTurn(Button roll, Button hold, final TextView textView, final ImageView imageView) {
    roll.setEnabled(false);
    hold.setEnabled(false);

    updateComputerStatus(textView, imageView);

    if (comTotalScore < WIN_GAME_SCORE) {
      roll.setEnabled(true);
      hold.setEnabled(true);
    }
  }

  private void updateComputerStatus(final TextView textView, final ImageView imageView) {

    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        comCurrentScore = rollDice(comCurrentScore, imageView);
        textView.setText(getScoreBoardMessage(true));

        if (comCurrentScore > 0 && comCurrentScore < COM_TARGET_SCORE) {
          updateComputerStatus(textView, imageView);
        } else {
          updateTotalScore(true, textView);
          handler.removeCallbacks(this);
        }
      }
    };
    handler.postDelayed(runnable, 500);
  }

  private int rollDice(int score, ImageView imageView) {
    int diceValue = random.nextInt(6) + 1;
    Drawable diceImage = getDiceImage(diceValue);
    imageView.setImageDrawable(diceImage);

    if (diceValue == 1) {
      score = 0;
    } else {
      score += diceValue;
    }

    return score;
  }

  private void updateTotalScore(boolean isComputer, TextView textView) {
    if (isComputer) {
      comTotalScore += comCurrentScore;
      comCurrentScore = 0;
    } else {
      userTotalScore += userCurrentScore;
      userCurrentScore = 0;
    }
    textView.setText(getScoreBoardMessage(isComputer));
  }
}
