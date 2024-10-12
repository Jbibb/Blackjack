package Blackjack;

import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.Map;

public class StrategyTableModel extends AbstractTableModel {

    public static enum Action {
        Hit, Stand, Double
    }

    private int[] dealerScores = new int[]{
            2, 3, 4, 5, 6, 7, 8, 9, 10, 11
    };

    private int[] playerScoresWithoutAce = new int[]{
            4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20
    };
    private int[] playerScoresWithAce = new int[]{
            -1, -2, -3, -4, -5, -6, -7, -8, -9
    };

    private String[] rowHeaders = new String[]{
            "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "A A", "A 2", "A 3", "A 4", "A 5", "A 6", "A 7", "A 8", "A 9"
    };

    private Map<Integer, Action[]> map = new HashMap<>();
    public StrategyTableModel(){
        for(int score : playerScoresWithoutAce) {
            Action[] actions = new Action[dealerScores.length];
            for(int i = 0; i < actions.length; i++)
                actions[i] = Action.Hit;
            map.put(score, actions);
        }

        for(int score : playerScoresWithAce) {
            Action[] actions = new Action[dealerScores.length];
            for(int i = 0; i < actions.length; i++)
                actions[i] = Action.Hit;
            map.put(score, actions);
        }

        fillWithOptimalStrategy();

    }

    @Override
    public int getRowCount() {
        return map.size();
    }

    @Override
    public int getColumnCount() {
        return map.get(4).length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex < playerScoresWithoutAce.length)
            return map.get(playerScoresWithoutAce[rowIndex])[columnIndex];
        else
            return map.get(playerScoresWithAce[rowIndex - playerScoresWithoutAce.length])[columnIndex];
    }

    public Action getAction(int playerScore, int dealerUpcardValue) {
        return map.get(playerScore)[dealerUpcardValue - 2];
    }

    private void fillWithOptimalStrategy(){
        int playerScoreWithoutAce;
        int dealerScore;

        //double scenarios
        playerScoreWithoutAce = 9;
        for(dealerScore = 3; dealerScore < 7; dealerScore++)
            map.get(playerScoreWithoutAce)[dealerScore - 2] = Action.Double;

        playerScoreWithoutAce = 10;
        for(dealerScore = 2; dealerScore < 10; dealerScore++)
            map.get(playerScoreWithoutAce)[dealerScore - 2] = Action.Double;

        playerScoreWithoutAce = 11;
        for(dealerScore = 2; dealerScore < 11; dealerScore++)
            map.get(playerScoreWithoutAce)[dealerScore - 2] = Action.Double;

        //stand scenarios
        playerScoreWithoutAce = 12;
        for(dealerScore = 4; dealerScore < 7; dealerScore++)
            map.get(playerScoreWithoutAce)[dealerScore - 2] = Action.Stand;

        for(playerScoreWithoutAce = 13; playerScoreWithoutAce < 17; playerScoreWithoutAce++)
            for(dealerScore = 2; dealerScore < 7; dealerScore++)
                map.get(playerScoreWithoutAce)[dealerScore - 2] = Action.Stand;

        for(playerScoreWithoutAce = 17; playerScoreWithoutAce <= 20; playerScoreWithoutAce++)
            for(dealerScore = 2; dealerScore < 12; dealerScore++)
                map.get(playerScoreWithoutAce)[dealerScore - 2] = Action.Stand;

        //ace scenarios
        int playerScoreWithAce;
        for(playerScoreWithAce = -2; playerScoreWithAce > -4; playerScoreWithAce--)
            for(dealerScore = 5; dealerScore < 7; dealerScore++)
                map.get(playerScoreWithAce)[dealerScore - 2] = Action.Double;

        for(playerScoreWithAce = -4; playerScoreWithAce > -6; playerScoreWithAce--)
            for(dealerScore = 4; dealerScore < 7; dealerScore++)
                map.get(playerScoreWithAce)[dealerScore - 2] = Action.Double;

        for(playerScoreWithAce = -6; playerScoreWithAce > -8; playerScoreWithAce--)
            for(dealerScore = 3; dealerScore < 7; dealerScore++)
                map.get(playerScoreWithAce)[dealerScore - 2] = Action.Double;

        playerScoreWithAce = -7;
        map.get(playerScoreWithAce)[2 - 2] = Action.Stand;
        map.get(playerScoreWithAce)[7 - 2] = Action.Stand;
        map.get(playerScoreWithAce)[8 - 2] = Action.Stand;

        for(playerScoreWithAce = -8; playerScoreWithAce >= -9; playerScoreWithAce--)
            for(dealerScore = 2; dealerScore <= 11; dealerScore++)
                map.get(playerScoreWithAce)[dealerScore - 2] = Action.Stand;



    }

    @Override
    public String getColumnName(int column) {
        if(column == 9)
            return "A";
        return String.valueOf(dealerScores[column]);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(rowIndex < playerScoresWithoutAce.length)
            map.get(playerScoresWithoutAce[rowIndex])[columnIndex] = (Action) aValue;
        else
            map.get(playerScoresWithAce[rowIndex - playerScoresWithoutAce.length])[columnIndex] = (Action) aValue;
    }



    public String getRowName(int row) {
        return rowHeaders[row];
    }


}
