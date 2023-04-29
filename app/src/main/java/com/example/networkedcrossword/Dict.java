package com.example.networkedcrossword;

import java.io.Serializable;
import java.util.ArrayList;

public class Dict implements Serializable {
    private ArrayList<ArrayList<String>> boards;
    ArrayList<String> file = null;
    private int seed;
    public Dict(int seed){
        System.out.println(seed);
        this.boards = new ArrayList<>(10);
        this.seed = seed;
    }

    public ArrayList<ArrayList<String>> build() {
        for (int i = 0; i < 10; i++) {
            boards.add(new ArrayList<>());
        }
        String is = "";
        switch (seed) {
            case 1:
                is = "1:D:Chia:Seed in some puddings\n" +
                        "11:A:Assures:Guarantees\n" +
                        "18:A:Ada:Lovelace (Early computer programmer)\n" +
                        "14:D:Edu:.Follower at school\n" +
                        "15:D:Wax:Vinyl informally\n" +
                        "12:A:LIL:Money Way rapper\n" +
                        "21:A:FETE:Fancy do\n" +
                        "17:D:EYE:its a real looker\n" +
                        "16:D:MAT:spread out for prayer\n" +
                        "20:A:TUX:Fancy outfit";
                break;
            case 2:
                is = "1:A:LAVASH:Flour water salt bread baked in a tandoor\n" +
                        "10:A:SIG:john hancock for short\n" +
                        "13:D:TEE:shirt named for its shape\n" +
                        "8:D:TEST:Vet maybe\n" +
                        "4:D:ATF:DOJ divison\n" +
                        "11:A:MGS:Booker Ts backers\n" +
                        "15:A:TRES:3 in spanish\n" +
                        "14:A:SHEENY:Lustrous\n" +
                        "6:D:HANGRY:Lashing out due to hunger\n" +
                        "1:D:LAPSES:Minor slips";
                break;
            case 3:
                is = "1:D:PUCK:Hockey disc\n" +
                        "1:A:PITTED:Removed from middle of an olive\n" +
                        "7:A:UNHOLY:2023 hit song by sam smith and kim petras\n" +
                        "8:A:CHEWIE:Han solos furry co pilot casually\n" +
                        "9:A:KARATS:Gold measurements\n" +
                        "10:A:LORE:body of old stories\n" +
                        "11:A:ENDS:Termini\n" +
                        "6:D:DYES:changing hair colors\n" +
                        "2:D:INHALE:Breathe in\n" +
                        "5:D:ELITES:Rymths with cleets";
                break;
        }
        String[] splitArray = is.split("\n");
        file = new ArrayList<String>();
        for(int i = 0; i < splitArray.length; i++){
            String[] parts = splitArray[i].split(":");
            for(int j = 0; j < parts.length; j++) {
                this.boards.get(i).add(parts[j]);
            }
        }

        return boards;
    }

    public ArrayList<ArrayList<String>> getBoards() {
        return this.boards;
    }
}