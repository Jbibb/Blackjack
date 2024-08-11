package Logic;

import java.util.ArrayList;
public class CardImageNamesFetcher {
    public static ArrayList<String> fetchCardImageNames(){
        ArrayList<String> res = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        String name;
        for (Values value : Values.values()){
            for (Suits suit : Suits.values()){
                name = sb.append(value.name()).append("_").append(suit.name()).append(".png").toString();
                res.add(name);
                sb.setLength(0);
            }
        }
        res.add("Back.png");
        return res;
    }
}
