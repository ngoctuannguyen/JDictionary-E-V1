package game.Olympia.Round;

import game.Olympia.Countdown.CountdownTimer;
import game.Olympia.UserInfo.Player;


public class Starting extends Player {

    private int startingScore;

    public Starting(int startingScore) {
        this.startingScore = getStartingScore();
    }

    @Override
    public int correctAnsPlus(int score) {
         score += 10;
         return score;
    }

    @Override
    public int incorrectAnsMinus(int score) {
        score -= 5;
        return score;
    }

    public static void main(String[] args) {
        Starting starting = new Starting(0);
        CountdownTimer countdownTimer = new CountdownTimer();
        System.out.println("Player " + ". Let's start");
        countdownTimer.Countdown(60);
    }
}
