package View;

import Model.Items;
import Model.MainModel;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Display {
    public static void main(String[] args) {

        String[] score = new String[30];
        int counter = 0, id = Integer.MAX_VALUE, lastScore = 0;
        String answer = null;

        int playersScore = 0;
        MainModel main;
        JFrame frame;
        boolean win = false;
        MainView mainV;

        try {
            BufferedReader fileReader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("Data/Score/playersScore.txt")
                    )
            );
            frame = new JFrame("Platformer");
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            JLabel introducing = new JLabel("Enter your name:");
            TextField text = new TextField();
            frame.setLayout(new GridLayout(2,1));
            frame.add(introducing);
            frame.add(text);
            frame.setUndecorated(false);
            frame.setSize(400, 200);
            frame.setVisible(true);
            try {
                Ok:
                while (true) {
                    answer = text.getText();
                    Thread.sleep(200);
                    if (answer.indexOf(' ') != -1) {

                        String name = " ";
                        while(name != null){
                            name = fileReader.readLine();
                            score[counter++] = name;
                            if(name == null) {
                                counter--;
                                break Ok;
                            }
                            if(answer.equals(name)) {
                                System.out.println("equals");
                                id = counter - 1;
                                System.out.println("ID: " + id);
                                score[counter++] = fileReader.readLine();
                                lastScore = Integer.parseInt(score[counter - 1]);
                            }
                        }

                        break;
                    }
                }
                System.out.println("Counter: " + counter);
                for(int i=0;i< counter; i++){
                    System.out.println(score[i]);
                }
                frame.dispose();
            } catch (InterruptedException e) {}
        }catch(FileNotFoundException e){}
        catch(IOException e){}

        end:
        for(int i = 1; i <= /*MainModel.levelCount*/9; i++) {

            frame = new JFrame("Platformer: Level " + i);

            try {

            main = new MainModel(i);
            mainV = new MainView(main, frame);

            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            frame.setUndecorated(false);

            frame.add(mainV);
            frame.setVisible(true);

            if( i <= 3 || i == 6 || i == 7)
                Items.openDoor();
            while(true){
                if(main.gameEnding()) {
                    //main = null;
                    frame.dispose();
                    win = false;
                    break end;
                }
                if(main.isNextLevel()) {
                    frame.dispose();
                    //main = null;
                    playersScore += main.getPlayersScore();
                    break;
                }
                try {
                    Thread.sleep(1000);
                }catch(InterruptedException e){}
            }
            }catch (IOException e){
                System.out.println("EXCEPTION");
            }

            win = true;
        }


        try{
            BufferedWriter fileWriter = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream("Data/Score/playersScore.txt")
                    )
            );
            if(id > counter && win){
                fileWriter.write(answer + "\n");
                fileWriter.write(Integer.toString(playersScore) + "\n");
            }
            if(counter == 0 && win){
                fileWriter.write(answer + "\n" + playersScore);
            }
            else {
                for (int i = 0; i <= counter; i++) {
                    if(score[i] == null)
                        break;
                    if(id != i && !win){
                        fileWriter.write(score[i++] + "\n");
                        fileWriter.write(score[i] + "\n");
                        continue;
                    }
                    if(id != i) {
                        fileWriter.write(score[i] + "\n");
                        i++;
                        fileWriter.write(score[i] + "\n");
                        continue;
                    }
                    if(id == i && lastScore < playersScore && win){
                        fileWriter.write(answer + "\n");
                        fileWriter.write(Integer.toString(playersScore) + "\n");
                        i ++;
                    }
                    else {
                        fileWriter.write(score[i] + "\n");
                        i++;
                        fileWriter.write(score[i] + "\n");
                        continue;
                    }
                }
            }
            fileWriter.flush();
        }catch(FileNotFoundException e){
            System.out.println("not found");
        }
        catch(IOException e){
            System.out.println("IO");
        }
        frame = new JFrame("Results");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(false);
        frame.setSize(900, 600);
        JLabel label;
        JPanel contentPane = new JPanel();
        contentPane.setOpaque(true);
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(new GridLayout(10, 1));

        JLabel label1;
        if(win){
            label1 = new JLabel(
                    answer + ", you Win!\nYour score: " + playersScore
                    , JLabel.CENTER);
        }
        else{
            label1 = new JLabel(
                    answer + ", you Loose\n"
                    , JLabel.CENTER);
        }

        label1.setSize(80, 30);
        //label1.setLocation(5, 5);
        contentPane.add(label1);

        JLabel label2 = new JLabel(
                "All players Score:"
                , JLabel.CENTER);
        label2.setSize(50, 30);
        contentPane.add(label2);
        label1.setSize(300, 30);
        if(win) {
            StringBuffer buff = new StringBuffer(50);
            buff.append("\n");
            try {
                BufferedReader fileReader = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream("Data/Score/playersScore.txt")
                        )
                );
                for (int i = 0; i < score.length;i++){
                    answer = fileReader.readLine();
                    if(answer == null)
                        break;
                    buff.append(answer + ":\n");
                    buff.append(fileReader.readLine());
                }

            }catch(FileNotFoundException e){}
            catch(IOException e){}

        }
        for(int i = 0; i < score.length; i++) {
            String str = score[i];
            if(str == null)
                break;
            //if(id)
            label = new JLabel(str + ": \n" + score[++i], JLabel.CENTER);
            label.setSize(20, 30);
            contentPane.add(label);
        }
        //frame.setSize(1400, 800);
        frame.setLocationByPlatform(true);
        //frame.add(label);
        frame.add(contentPane);

        frame.setVisible(true);
    }
}
