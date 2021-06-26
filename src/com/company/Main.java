package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    //Real Name on Server
    private static final String realName = "Saif";
    private static String nickname;
    private static String username;
    private static String channel;
    private static PrintWriter out;
    private static String message = "";

    public static void main(String[] args) throws IOException {

        //Creates a socket which takes in host address and port number
        Socket socket = new Socket("selsey.nsqdc.city.ac.uk", 6667);

        out = new PrintWriter(socket.getOutputStream(), true);
        //Takes in message on server and inputs into stream to be read
        Scanner in = new Scanner(socket.getInputStream());

        //Nickname on server
        nickname = "SafeBot";
        write("NICK", nickname);
        //username on server
        username = "CleverBot ";
        write("USER", username + "8 * :" + realName);
        //Channel to connect to
        channel = "#help";
        write("JOIN", channel);



        while (in.hasNext()) {
            String serverMessage = in.nextLine();
            System.out.println("<<<< " + serverMessage);

            //Reponds to ping
            if (serverMessage.startsWith("PING")) { // Server PING
                String pingContents = serverMessage.split(" ", 2)[1];
                write("PONG", pingContents);
            }
            // Must exist before each command
            if (serverMessage.contains("safeBot")) {
                String content = serverMessage.split(" ", 5)[4];
                if (content.equals("help")) { // Help
                    for(int i = 0; i < 10; i++) {
                        message = listCommands(i);
                        write("PRIVMSG", channel + " :" + message);
                    }
                }
                if (content.equals("marco")) { // Marco
                    message = "polo!";
                    write("PRIVMSG", channel + " :" + message);
                }
                if (content.equals("date")) { // Date
                    message = findDate();
                    write("PRIVMSG", channel + " :" + message);
                }
                if (content.equals("time")) { // Time
                    message = findTime();
                    write("PRIVMSG", channel + " :" + message);
                }
                if (content.equals("tosscoin")) { // Coin Toss
                    message = tossCoin();
                    write("PRIVMSG", channel + " :" + message);
                }
                if (content.equals("rolldice")) { // Dice Roll
                    message = rollDice();
                    write("PRIVMSG", channel + " :" + message);
                }
                if (content.equals("joke")) { // Joke
                    int randomInt = (int)(10.0 * Math.random());
                    message = getJoke(randomInt);
                    write("PRIVMSG", channel + " :" + message);
                    //Leaves a 2 second delay to give the answer to joke
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    message = getJokeAnswer(randomInt);
                                    write("PRIVMSG", channel + " :" + message);
                                }
                            },
                            2000
                    );
                }
                if (content.equals("art")) { // Art
                    for(int i = 0; i < 28; i++) {
                        message = drawArt(i);
                        write("PRIVMSG", channel + " :" + message);
                    }
                }
                if (content.equals("facts")) { // Facts
                    int randomInt = (int)(14.0 * Math.random());
                    message = getFacts(randomInt);
                    write("PRIVMSG", channel + " :" + message);
                }
                if (content.equals("hothitsuk")) { //Top 10 Hits UK
                    for(int i = 0; i < 10; i++) {
                        message = getSongs(i);
                        write("PRIVMSG", channel + " :" + message);
                    }
                }
            }

        }

        in.close();
        out.close();
        socket.close();

        System.out.println("Done!");
    }

    //Method that writes commands to server
    private static void write(String command, String message) {
        String fullMessage = command + " " + message;
        System.out.println(">>> " + fullMessage);
        out.print(fullMessage + "\r\n");
        out.flush();
    }

    //Finds today's date
    private static String findDate() {
        LocalDateTime myDate = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy");
        return myDate.format(myFormatObj);
    }

    //Find today's time
    private static String findTime() {
        LocalDateTime myDate = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
        return myDate.format(myFormatObj);
    }

    //Tosses a coin.
    private static String tossCoin() {
        if (Math.random() < 0.5) {
            return "Heads";
        } else {
            return "Tails";
        }
    }

    private static int[] rollDice(int sides, int dice) {
        int[] numbers = new int[dice];
        Random rand = new Random();

        for (int i = 0; i < dice; i++) {
            numbers[i] = rand.nextInt(sides);
        }

        return numbers;
    }

    //Rolls a dice
    private static String rollDice() {
        int sides = 6;
        int dice = 1;

        int[] numbers = rollDice(sides, dice);

        for (int n : numbers) {
            return ("Rolls: " + n + " ");
        }
        return null;
    }

    //Help function list all commands
    private static String listCommands(int n) {
        ArrayList<String> commands = new ArrayList<>();
        commands.add("date: Today's date");
        commands.add("tosscoin: Flips a coin");
        commands.add("rolldice: Rolls a dice");
        commands.add("marco: responds");
        commands.add("Time: Today's time");
        commands.add("Art: amazing art");
        commands.add("facts: Interesting facts");
        commands.add("hothitsuk: Hot Hits UK");
        commands.add("joke: Fun Jokes");
        return commands.get(n);
    }

    //Gets a joke
    private static String getJoke(int n) {

        ArrayList<String> jokeList = new ArrayList<>();
        jokeList.add("What do you call a dinosaur that is sleeping?");
        jokeList.add("What is fast, loud and crunchy?");
        jokeList.add("Why did the teddy bear say no to dessert?");
        jokeList.add("What has ears but cannot hear?");
        jokeList.add("What did the left eye say to the right eye?");
        jokeList.add("What do you get when you cross a vampire and a snowman?");
        jokeList.add("What did one plate say to the other plate?");
        jokeList.add("Why did the student eat his homework?");
        jokeList.add("When you look for something, why is it always in the last place you look?");
        jokeList.add("What is brown, hairy and wears sunglasses?");
        return jokeList.get(n);
    }

    //Chooses a fact
    private static String getFacts(int n) {

        ArrayList<String> factsList = new ArrayList<>();
        factsList.add("The hashtag symbol is technically called an octothorpe.");
        factsList.add("The 100 folds in a chef's hat represent 100 ways to cook an egg.");
        factsList.add("The longest wedding veil was longer than 63 football fields.");
        factsList.add("Some cats are allergic to people.");
        factsList.add("Apple Pie isn't actually American at all.");
        factsList.add("The unicorn is the national animal of Scotland.");
        factsList.add("The largest known living organism is an aspen grove.");
        factsList.add("M&M stands for Mars and Murrie.");
        factsList.add("Neil Armstrong didn't say \"That's one small step for man.\"");
        factsList.add("You can hear a blue whale's heartbeat from more than 2 miles away.");
        factsList.add("The odds of getting a royal flush are exactly 1 in 649,740.");
        factsList.add("If you drive south from Detroit, you'll hit Canada.");
        factsList.add("A baby puffin is called a \"puffling.\"");
        factsList.add("Four times more people speak English as a second language than as a native one.");
        return factsList.get(n);
    }

    //Gets an answer to a joke
    private static String getJokeAnswer(int n) {
        ArrayList<String> jokeList = new ArrayList<>();
        jokeList.add("A dino-snore!");
        jokeList.add("A rocket chip!");
        jokeList.add("Because she was stuffed.");
        jokeList.add("A cornfield.");
        jokeList.add("Between us, something smells!");
        jokeList.add("Frost bite!");
        jokeList.add("Dinner is on me!");
        jokeList.add("Because the teacher told him it was a piece of cake!");
        jokeList.add("Because when you find it, you stop looking.");
        jokeList.add("A coconut on vacation.");
        return jokeList.get(n);
    }

    //Draws an ASCII art
    private static String drawArt(int n) {
        ArrayList<String> art = new ArrayList<>();
        art.add("░░░░░░░▄▄████▄▄▄░░░░░░▄▄██████▄▄");
        art.add("░░░░░██▓▓▓▓▓▓▒▓▓██░░▓█▓▓▓▓▒░▒▒▓▓██");
        art.add("░░░░░██▓▓▓▓▓▓▒▓▓██░░▓█▓▓▓▓▒░▒▒▓▓██");
        art.add("░░██▓▓▓▓▓▓▓▓▓▓▓▒▒▓▓█▓▓▓▓▓▓▓▓▓▓▒░░▓▓█");
        art.add("░▒█▓▓▓▓▓▓▓▓▓▓▓▓▓▓██▓██▓▓▓▓▓▓▓▓▓▒░░▓▓█");
        art.add("░█▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▒░▒▓█");
        art.add("░█▓▓▓▓▓▓▓▓▓▓▓▓████░████▓▓▓▓▓▓▓▓▓▒░░▓██");
        art.add("█▓▓▓▓▓▓▓▓▓▓▓▓██░░░░░░░██▓▓▓▓▓▓▓▓▓░░▓▓█");
        art.add("█▓▓▓▓▓▓▓▓▓▓▓▓▓░░█░░░█░░▓▓▓▓▓▓▓▓▓▓▒░▓▓█▒");
        art.add("█▓▓▓▓▓▓▓▓▓▓▓▓▓░█▒█░█▒█░▓▓▓▓▓▓▓▓▓▓▒▒▓▓█▒");
        art.add("█▓▓▓▓▓▓▓▓▓▓▓▓▓░░░░░░░░░▓▓▓▓▓▓▓▓▓▓▒▒▓██▒");
        art.add("█▓▓▓▓▓▓▓▓▓▓▓▓▓▓░▒██░░░▓▓▓▓▓▓▓▓▓▓▓▒▓▓█▓▒");
        art.add("░█▓▓▓▓▓▓▓▓▓▓▓▓▓▓████▓▓▓▓▓▓▓▓▓▓▓▓▓▒▓▓█▓▒");
        art.add("░█▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓█▓▒");
        art.add("░░█▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓█▓▒");
        art.add("░░█▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▓▓▓▓▓▓▓▓▓▓█▓▒");
        art.add("░░░█▓▓▓▓▓▓▓▓▓██▓▓▓▓█████▓▓▓▓▓▓▓▓▓▓█▓▒");
        art.add("░░░░█▓▓▓▓▓▓▓▓██████████▓▓▓▓▓▓▓▓▓▓██▓▒");
        art.add("░░░░▓█▓▓▓▓▓▓▓▓██▒▒▓▒▒█▓▓▓▓▓▓▓▓▓▓██▓▒");
        art.add("░░░░░░█▓▓▓▓▓▓▓▓██▒▒▒█▓▓▓▓▓▓▓▓▓▓██▓▒");
        art.add("░░░░░░░██▓▓▓▓▓▓▓▓███▓▓▓▓▓▓▓▓▓▓██▓▒");
        art.add("░░░░░░░░██▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓██▓▒");
        art.add("░░░░░░░░░░█▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓█▓▒");
        art.add("░░░░░░░░░░░██▓▓▓▓▓▓▓▓▓▓▓▓██▓▒");
        art.add("░░░░░░░░░░░░░██▓▓▓▓▓▓▓▓▓█▓▒");
        art.add("░░░░░░░░░░░░░░░██▓▓▓▓▓██▓▒");
        art.add("░░░░░░░░░░░░░░░░░█▓▓██▓▒");
        art.add("░░░░░░░░░░░░░░░░░░░█▓▒");
        return art.get(n);
    }

    //Finds Top 10 UK POP hits
    private static String getSongs(int n) {
        ArrayList<String> songlist = new ArrayList<>();
        songlist.add("Break My Heart - Dua Lipa");
        songlist.add("Lonely - Joel Corry");
        songlist.add("Boyfriend - Mabel");
        songlist.add("Flowers(feat. Jaykae) - Nathan Dawe");
        songlist.add("Say So");
        songlist.add("Roses(Imanbek Remix) - SAINTt JHN, Imanbek");
        songlist.add("Physical - Dua Lipa");
        songlist.add("Blinding Lights - The Weeknd");
        songlist.add("Death Bed - Powfu, beabadoobee");
        songlist.add("Toosie Slide - Drake");
        return songlist.get(n);
    }
}
