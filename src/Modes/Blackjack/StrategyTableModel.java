package Modes.Blackjack;

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

    private Map<Integer, Action[]> table = new HashMap<>();
    public StrategyTableModel(){
        for(int score : playerScoresWithoutAce) {
            Action[] actions = new Action[dealerScores.length];
            for(int i = 0; i < actions.length; i++)
                actions[i] = Action.Stand;
            table.put(score, actions);
        }

        for(int score : playerScoresWithAce) {
            Action[] actions = new Action[dealerScores.length];
            for(int i = 0; i < actions.length; i++)
                actions[i] = Action.Stand;
            table.put(score, actions);
        }

    }

    @Override
    public int getRowCount() {
        return table.size();
    }

    @Override
    public int getColumnCount() {
        return table.get(4).length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex < playerScoresWithoutAce.length)
            return table.get(playerScoresWithoutAce[rowIndex])[columnIndex];
        else
            return table.get(playerScoresWithAce[rowIndex - playerScoresWithoutAce.length])[columnIndex];
    }

    public Action getAction(int playerScore, int dealerUpcardValue) {
        return table.get(playerScore)[dealerUpcardValue - 2];
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
            table.get(playerScoresWithoutAce[rowIndex])[columnIndex] = (Action) aValue;
        else
            table.get(playerScoresWithAce[rowIndex - playerScoresWithoutAce.length])[columnIndex] = (Action) aValue;
    }



    public String getRowName(int row) {
        return rowHeaders[row];
    }


}
