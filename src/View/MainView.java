package View;


import Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainView extends JPanel implements ActionListener {

    private Timer timer = new Timer(1, this);
    private static int score;
    private  JFrame frame;
    private MainModel main;
    private Image mainImage, groundImage, playerImage, playerShootingImage, lifeImage = new ImageIcon("Data/Images/lifeImage.png").getImage();
    private int countOfDrawable, countOfEnemies, countOfItems, GROUND;
    private NPC[] npcArray;
    private Player player;


    public MainView(MainModel main, JFrame frame){
        timer.start();
        this.frame = frame;
        this.main = main;
        player = main.getPlayer();
        playerImage = new ImageIcon(player.getImageName()).getImage();
        playerShootingImage = new ImageIcon(player.getShootingImageName()).getImage();
        refreshMainModel(main);

        this.frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                //System.out.println("Typed: " + e.getKeyCode());
                main.keyTyped(e.getKeyCode());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                //System.out.println("Pressed " + e.getKeyCode());
                main.keyPressed(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                //System.out.println("Released: " + e.getKeyCode());
                main.keyReleased(e.getKeyCode());
            }
        });
    }

    public void refreshMainModel(MainModel main){
        this.main = main;
        //mainImage = main.getMainImage();
        mainImage = new ImageIcon(main.getMainImageName()).getImage();
        //groundImage = main.getGroundImage();
        groundImage = new ImageIcon(main.getGroundImageName()).getImage();

        Player player = main.getPlayer();
        GROUND = main.getGroundOy();
        countOfDrawable = main.getCountOfDrawable();
        countOfItems = main.getCountOfItems();
        countOfEnemies = main.getCountOfEnemies();
        npcArray = main.getNPCArray();
    }

    public void paint(Graphics g){

        //FRONT
        g.drawImage(mainImage, 0, 0, frame.getWidth(), frame.getHeight(), null);
        g.drawImage(groundImage, 0, GROUND + player.getHeight(), frame.getWidth(), frame.getHeight() - GROUND, null);
        g.drawString("Total level score: " + ((Integer)(player.getScore())).toString(), 50, 30);
        Image NPCImage;
        //LIFE
        for(int i = 0; i < player.getLife(); i++){
            g.drawImage(lifeImage, 0 + 50*i, 50, 50, 50, null);
        }
        //NPC
        for(int i = 0; i < countOfDrawable; i++){

            //Enemies, Panels
            if(i < countOfDrawable - countOfItems) {
                //npcArray[i].paint(g);
                NPCImage = new ImageIcon(((NPC)npcArray[i]).getImageName()).getImage();
                // enemy\panel
                g.drawImage(NPCImage, ((NPC)npcArray[i]).getOx(), ((NPC)npcArray[i]).getOy(),
                        ((NPC)npcArray[i]).getWidth(), ((NPC)npcArray[i]).getHeight(), null);

                //for shooting enemy
                if(i < countOfEnemies){
                    if(((Enemy)npcArray[i]).canShooting()){
                        Image enemyShootingImage = new ImageIcon(((Enemy)npcArray[i]).getShootingImageName()).getImage();

                        g.drawImage(enemyShootingImage, ((Enemy)npcArray[i]).getShootingOx(), ((Enemy)npcArray[i]).getShootingOy(),
                                ((Enemy)npcArray[i]).getShootingWidth(), ((Enemy)npcArray[i]).getShootingHeight(), null);
                    }
                }
            }

                //Items
            else {
                if (((Items) npcArray[i]).isDestroyed())
                    continue;
                else {
                    NPCImage = new ImageIcon(((NPC)npcArray[i]).getImageName()).getImage();

                    g.drawImage(NPCImage, ((NPC)npcArray[i]).getOx(), ((NPC)npcArray[i]).getOy(), ((NPC)npcArray[i]).getWidth(),
                            ((NPC)npcArray[i]).getHeight(), null);
                }
            }

        }
        playerImage = new ImageIcon(player.getImageName()).getImage();
        g.drawImage(playerImage, player.getOx(), player.getOy(), player.getWidth(), player.getHeight(), null);
        if(player.isShooting())
            g.drawImage(playerShootingImage, player.getShootingOx(), player.getShootingOy(),
                    player.getShootingWidth(), player.getShootingHeight(), null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println("REPAINT");
        repaint();
    }
}
