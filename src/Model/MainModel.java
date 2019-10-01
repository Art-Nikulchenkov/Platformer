package Model;

import java.io.*;

public class MainModel{

    private int countOfDrawable, countOfEmenies, countOfPanels, countOfItems, GROUND = 700, levelNumber;
    private NPC[] enemyArray = new NPC[50];
    private Player player = new Player(20, GROUND, 3, GROUND, 50, 50, 15);
    private String groundImageName, mainImageName;
    private int firstPlayerOx, firstPlayerOy;
    public static int levelCount;
    private static boolean nextLevel = false, gameOver = false;


    public static void goToNextLevel(){nextLevel = true;}
    public static boolean gameEnding(){return gameOver;}

    public boolean isNextLevel(){return  nextLevel;}
    //public Image getMainImage(){return mainImage;}
    //public Image getGroundImage(){return groundImage;}
    public String getGroundImageName(){return groundImageName;}
    public String getMainImageName(){return mainImageName;}
    public Player getPlayer(){return player;}
    public int getGroundOy(){return GROUND;}
    //public JFrame getFrame(){return frame;}
    public int getCountOfDrawable(){return countOfDrawable;}
    public int getCountOfItems(){return countOfItems;}
    public int getCountOfEnemies(){return countOfEmenies;}
    public int getPlayersScore(){return player.getScore();}
    public int getLevelCount(){return levelCount;}
    public NPC[] getNPCArray(){return enemyArray;}

    public void keyReleased(int keyKode){
        if(keyKode != 32)
            player.clearDirection();
    }

    public void keyTyped(int keyKode){
        player.move(keyKode);
    }

    public void keyPressed(int keyKode){
        player.move(keyKode);
    }

    public MainModel(int level) throws IOException{
        try {
            levelCount = new File("Data/Levels/").listFiles().length;
        }catch(NullPointerException ex){levelCount = 10;}
        if(level < 0 || level > levelCount)
            throw new IOException();
        nextLevel = false;
        levelNumber = level;
        if(level == 0){
            return;
        }

        try{
            levelCount = new File("Data/Levels/").listFiles().length;
            BufferedReader fileReader;
            if(level < levelCount - 1) {
                 fileReader = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream("Data/Levels/level " + levelNumber + ".txt")
                        )
                );
                System.out.println("Level: " + levelNumber);
            }
            else{
                if(level == levelCount - 1) {
                    fileReader = new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream("Data/Levels/newLevel.txt")
                            )
                    );
                    System.out.println("New level testing");
                }
                else {
                    fileReader = new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream("Data/Levels/jumpTest.txt")
                            )
                    );
                    System.out.println("JUMPTEST");
                }
            }
            mainImageName = fileReader.readLine();
            groundImageName = fileReader.readLine();
            GROUND = Integer.parseInt(fileReader.readLine());
            firstPlayerOx = Integer.parseInt(fileReader.readLine());
            firstPlayerOy = GROUND;
            GROUND = 700;
            player.setToPosition(firstPlayerOx, firstPlayerOy);

            fileReader.readLine();fileReader.readLine();
            int counter = Integer.parseInt(fileReader.readLine());
            int iconNum = 0;
            //enemies
            for(int i = 0; i < counter; i++){
                fileReader.readLine();fileReader.readLine();
                int enOx = Integer.parseInt(fileReader.readLine());
                int enOy = Integer.parseInt(fileReader.readLine());
                int enWidth = Integer.parseInt(fileReader.readLine());
                int enHeight = Integer.parseInt(fileReader.readLine());
                int enSpeed = Integer.parseInt(fileReader.readLine());
                boolean enShSkills = Boolean.parseBoolean(fileReader.readLine());
                boolean enCanMove = Boolean.parseBoolean(fileReader.readLine());
                int enFOx1 = Integer.parseInt(fileReader.readLine());
                int enFOx2 = Integer.parseInt(fileReader.readLine());
                String enemyIc = fileReader.readLine();
                if(enemyIc.indexOf('1') != -1)
                    iconNum = 1;
                if(enemyIc.indexOf('2') != -1)
                    iconNum = 2;
                if(enemyIc.indexOf('3') != -1)
                    iconNum = 3;
                String enShName;
                if(enShSkills) {
                    enShName = fileReader.readLine();
                }
                else {
                    enShName = null;
                }
                makeNewEnemy(enOx, enOy, enWidth, enHeight, enSpeed, enShSkills, enCanMove, enFOx1, enFOx2,iconNum, enemyIc, enShName);
            }

            fileReader.readLine();fileReader.readLine();
            counter = Integer.parseInt(fileReader.readLine());
            //panels
            for(int i = 0; i < counter; i++){
                fileReader.readLine();fileReader.readLine();
                int pOx = Integer.parseInt(fileReader.readLine());
                int pOy = Integer.parseInt(fileReader.readLine());
                int pWidth = Integer.parseInt(fileReader.readLine());
                int pHeight = Integer.parseInt(fileReader.readLine());
                boolean pCanMove = Boolean.parseBoolean(fileReader.readLine());
                int pSpeed = Integer.parseInt(fileReader.readLine());
                int pFOx1 = Integer.parseInt(fileReader.readLine());
                int pFOx2 = Integer.parseInt(fileReader.readLine());
                String imageName = fileReader.readLine();
                makeNewPanel(pOx, pOy, pWidth, pHeight, pCanMove, pSpeed, pFOx1, pFOx2, imageName);
            }

            fileReader.readLine();fileReader.readLine();
            counter = Integer.parseInt(fileReader.readLine());
            //Items
            for(int i = 0; i < counter; i++){
                fileReader.readLine();fileReader.readLine();
                int iOx = Integer.parseInt(fileReader.readLine());
                int iOy = Integer.parseInt(fileReader.readLine());
                int iWidth = Integer.parseInt(fileReader.readLine());
                int iHeight = Integer.parseInt(fileReader.readLine());
                int iSpeed = Integer.parseInt(fileReader.readLine());
                String imageName = fileReader.readLine();
                Items.Item item;
                switch(Integer.parseInt(fileReader.readLine())){
                    case 1:
                        item = Items.Item.DOOR;
                        break;
                    case 2:
                        item = Items.Item.KEY;
                        break;
                    case 3:
                        item = Items.Item.LIFE;
                        break;
                    case 4:
                        item = Items.Item.BOOST;
                        break;
                    case 5:
                        item = Items.Item.POINTS;
                        break;
                    default:
                        item = Items.Item.LIFE;
                }
                makeNewItem(iOx, iOy, iWidth, iHeight, iSpeed, item, imageName);
            }

        }catch(FileNotFoundException e){}
        catch (IOException e){
            throw e;}
        catch(NullPointerException e){}

        enemyStart();
    }


    synchronized public int makeNewEnemy(int Ox, int Oy, int width, int height, int speed, boolean shootingSkills, boolean canMove, int fieldOx1,
                                         int fieldOx2, int iconNum, String enemyImageName, String enemyShootingImageName){

        if(enemyImageName == null || width <= 0 || height <= 0 || fieldOx1 > fieldOx2){
            return 1;
        }
        if(shootingSkills && enemyShootingImageName == null)
            return 1;

        Enemy enemy;
        if (enemyShootingImageName != null) {
            enemy = new Enemy(Ox, Oy, width, height, speed, shootingSkills, canMove, fieldOx1, fieldOx2, enemyImageName, enemyShootingImageName);
        }
        else{
            enemy = new Enemy(Ox, Oy, width, height, speed, shootingSkills, canMove,  fieldOx1, fieldOx2, enemyImageName, enemyShootingImageName);
        }
        enemy.setImageNumber(iconNum);
        enemy.setPlayer(player);
        enemyArray[countOfDrawable] = enemy;
        setDrawable();
        countOfEmenies++;
        //System.out.println("ENEMY: " + countOfEmenies);
        return 0;
    }

    synchronized public int makeNewItem(int Ox, int Oy, int width, int height, int speed, Items.Item item, String imageName){
        if(imageName == null ||item == null || width <= 0 || height <= 0)
            return 1;
        Items items = new Items(Ox, Oy, width, height, speed, item, imageName);
        enemyArray[countOfDrawable] = items;
        setDrawable();
        countOfItems++;
        return 0;

    }

    private void setDrawable(){
        countOfDrawable++;
    }

   synchronized public int makeNewPanel(int Ox1, int Oy1, int width, int height, boolean canMove, int speed,
                                        int fieldOx1, int fieldOx2, String imageName){
        if(imageName == null || fieldOx1 > fieldOx2 || width <=0 || height <=0) {
            return 1;
        }
        Panels panel = new Panels(Ox1, Oy1, width, height, canMove, speed, fieldOx1, fieldOx2, imageName);
        enemyArray[countOfDrawable] = panel;
        setDrawable();
        countOfPanels++;
        return 0;
    }

    private void enemyStart(){
        Thread enemyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Ok:
                while(true){
                    if(nextLevel || gameOver)
                        return;
                    //System.out.println("WORKS");
                    for(int i = 0; i < countOfDrawable; i++) {

                        enemyArray[i].move(0);
                        //System.out.println(enemyArray[i].getOx());

                        //For moving panels
                        if(i >= countOfEmenies && i < countOfDrawable - countOfItems){
                            if(((Panels)enemyArray[i]).canMove() && ((Panels)enemyArray[i]).isPlayerHere()){

                                int plSpeed = ((Panels)enemyArray[i]).getSpeed();

                                switch (((Panels)enemyArray[i]).getDirection()){
                                    case RIGHT:
                                        player.setToPosition(player.getOx() + plSpeed, player.getOy());
                                        break;
                                    case LEFT:
                                        player.setToPosition(player.getOx() - plSpeed, player.getOy());
                                        break;
                                }
                            }
                        }
                        try {
                            if (checkForBoxes() == 1) {
                                break Ok;
                            }
                        }catch (ClassCastException e){}
                    }
                    try {
                        if(player.isJumping())
                            Thread.sleep(15);
                        else
                            Thread.sleep(15);
                    } catch (InterruptedException e) {
                    }
                }
            }
        });enemyThread.start();
    }

    synchronized public int checkForBoxes(){

        int playerOx1, playerOx2, playerOy1, playerOy2, NPCOx1, NPCOx2, NPCOy1, NPCOy2;

        playerOx1 = player.getOx(); playerOx2 = playerOx1 + player.getWidth();
        playerOy1 = player.getOy(); playerOy2 = playerOy1 + player.getHeight();


        for(int i = 0; i < countOfDrawable; i++){

            NPCOx1 = enemyArray[i].getOx();
            NPCOy1 = enemyArray[i].getOy();
            NPCOx2 = NPCOx1 + enemyArray[i].getWidth();
            NPCOy2 = NPCOy1 + enemyArray[i].getHeight();

            //ENEMY
            if(i < countOfEmenies){
                //up left
                if(playerOx1 > NPCOx1 && playerOx1 < NPCOx2 && playerOy1 > NPCOy1 && playerOy1 < NPCOy2){
                    player.makeHurt();
                    if(player.getLife() == 0)
                        gameOver = true;
                    returnAllItems();
                    ((Enemy)enemyArray[i]).setShootingCounter(111);
                    for(int it = countOfEmenies; it < countOfDrawable - countOfItems; it++){
                        ((Panels)enemyArray[it]).setPlayerNotHere();
                    }
                    player.setToPosition(10, GROUND);
                    return 0;
                }
                //up right
                if(playerOx2 > NPCOx1 && playerOx2 < NPCOx2 && playerOy1 > NPCOy1 && playerOy1 < NPCOy2){
                    player.makeHurt();
                    if(player.getLife() == 0)
                        gameOver = true;
                    returnAllItems();
                    player.setDynamicPanelNotHere();
                    ((Enemy)enemyArray[i]).setShootingCounter(111);
                    for(int it = countOfEmenies; it < countOfDrawable - countOfItems; it++){
                        ((Panels)enemyArray[it]).setPlayerNotHere();
                    }
                    player.setToPosition(10, GROUND);
                    return 0;
                }
                //down left
                if(playerOx1 > NPCOx1 && playerOx1 < NPCOx2 && playerOy2 > NPCOy1 && playerOy2 < NPCOy2){
                    player.makeHurt();
                    if(player.getLife() == 0)
                        gameOver = true;
                    returnAllItems();
                    player.setDynamicPanelNotHere();
                    ((Enemy)enemyArray[i]).setShootingCounter(111);
                    for(int it = countOfEmenies; it < countOfDrawable - countOfItems; it++){
                        ((Panels)enemyArray[it]).setPlayerNotHere();
                    }
                    player.setToPosition(10, GROUND);
                    return 0;
                }
                //down right
                if(playerOx2 > NPCOx1 && playerOx2 < NPCOx2 && playerOy2 > NPCOy1 && playerOy2 < NPCOy2){
                    player.makeHurt();
                    if(player.getLife() == 0)
                        gameOver = true;
                    returnAllItems();
                    player.setDynamicPanelNotHere();
                    ((Enemy)enemyArray[i]).setShootingCounter(111);
                    for(int it = countOfEmenies; it < countOfDrawable - countOfItems; it++){
                        ((Panels)enemyArray[it]).setPlayerNotHere();
                    }
                    player.setToPosition(10, GROUND);
                    return 0;
                }
                //to make shoot physical
                if(((Enemy)enemyArray[i]).canShooting()){

                    int shOx1 = ((Enemy)enemyArray[i]).getShootingOx();
                    int shOx2 = shOx1 + ((Enemy)enemyArray[i]).getShootingWidth();
                    int shOy1 = ((Enemy)enemyArray[i]).getShootingOy();
                    int shOy2 = shOy1 + ((Enemy)enemyArray[i]).getShootingHeight();

                    //up left
                    if(playerOx1 < shOx1 && shOx1 < playerOx2 && playerOy1 < shOy1 && shOy1 < playerOy2){
                        player.makeHurt();
                        if(player.getLife() == 0)
                            gameOver = true;
                        returnAllItems();
                        player.setDynamicPanelNotHere();
                        player.setToPosition(10, GROUND);
                        return 0;
                    }
                    //down left
                    if(playerOx1 < shOx1 && shOx1 < playerOx2 && playerOy1 < shOy2 && shOy2 < playerOy2){
                        player.makeHurt();
                        if(player.getLife() == 0)
                            gameOver = true;
                        returnAllItems();
                        player.setDynamicPanelNotHere();
                        player.setToPosition(10, GROUND );
                        return 0;
                    }
                    //up right
                    if(playerOx1 < shOx2 && shOx2 < playerOx2 && playerOy1 < shOy1 && shOy1 < playerOy2){
                        player.makeHurt();
                        if(player.getLife() == 0)
                            gameOver = true;
                        returnAllItems();
                        player.setDynamicPanelNotHere();
                        player.setToPosition(10, GROUND - 200);
                        return 0;
                    }
                    //down right
                    if(playerOx1 < shOx2 && shOx2 < playerOx2 && playerOy1 < shOy2 && shOy2 < playerOy2){
                        player.makeHurt();
                        if(player.getLife() == 0)
                            gameOver = true;
                        returnAllItems();
                        player.setDynamicPanelNotHere();
                        player.setToPosition(10, GROUND);
                        return 0;
                    }

                }

                //to make player's shoot physical
                if(player.isShooting()){

                    int shOx1 = player.getShootingOx();
                    int shOx2 = shOx1 + player.getShootingWidth();
                    int shOy1 = player.getShootingOy();
                    int shOy2 = shOy1 + player.getShootingHeight();

                    //up left
                    if(NPCOx1 < shOx1 && shOx1 < NPCOx2 && NPCOy1 < shOy1 && shOy1 < NPCOy2){
                        ((Enemy)enemyArray[i]).killEnemy();
                        player.stopShooting();
                        player.increaseScore(50);
                        continue;
                    }
                    //up right
                    if(NPCOx1 < shOx2 && shOx2 < NPCOx2 && NPCOy1 < shOy1 && shOy1 < NPCOy2){
                        ((Enemy)enemyArray[i]).killEnemy();
                        player.stopShooting();
                        player.increaseScore(50);
                        continue;
                    }
                    //down left
                    if(NPCOx1 < shOx1 && shOx1 < NPCOx2 && NPCOy1 < shOy2 && shOy2 < NPCOy2){
                        ((Enemy)enemyArray[i]).killEnemy();
                        player.stopShooting();
                        player.increaseScore(50);
                        continue;
                    }
                    //down right
                    if(NPCOx1 < shOx2 && shOx2 < NPCOx2 && NPCOy1 < shOy2 && shOy2 < NPCOy2){
                        ((Enemy)enemyArray[i]).killEnemy();
                        player.stopShooting();
                        player.increaseScore(50);
                        continue;
                    }
                }
                continue;
            }

            if(countOfEmenies - 1 < i && i < countOfPanels + countOfEmenies){
                //PLATFORM
                if (player.isFalling()) {
                    //down left
                    if (playerOx1 > NPCOx1 && playerOx1 < NPCOx2 && playerOy2 > NPCOy1 && playerOy2 < NPCOy2) {
                        if (player.isFalling()) {
                            player.stopJumping();
                        }
                        player.setOnPlatform(NPCOx1, NPCOx2, NPCOy1);
                        //dynamic platform
                        if (((Panels) enemyArray[i]).canMove()) {
                            ((Panels) enemyArray[i]).setPlayerHere();
                            player.setOy(((Panels) enemyArray[i]).getOy() - player.getHeight() + 1);
                            player.setDynamicPanel(((Panels) enemyArray[i]));
                        } else
                            player.setToPosition(player.getOx(), NPCOy1 - player.getHeight());
                        return 0;
                    }
                    //down right
                    if (playerOx2 > NPCOx1 && playerOx2 < NPCOx2 && playerOy2 > NPCOy1 && playerOy2 < NPCOy2) {
                        if (player.isFalling()) {
                            player.stopJumping();
                        }
                        player.setOnPlatform(NPCOx1, NPCOx2, NPCOy1);
                        //dynamic platform
                        if (((Panels) enemyArray[i]).canMove()) {
                            ((Panels) enemyArray[i]).setPlayerHere();
                            player.setOy(((Panels) enemyArray[i]).getOy() - player.getHeight() + 1);
                            player.setDynamicPanel(((Panels) enemyArray[i]));
                        } else
                            player.setToPosition(player.getOx(), NPCOy1 - player.getHeight());
                        return 0;
                    }
                    ((Panels) enemyArray[i]).setPlayerNotHere();
                }
            }

            //ITEMS
            if( countOfEmenies + countOfPanels - 1 < i && i < countOfEmenies + countOfPanels + countOfItems){
                if(((Items)enemyArray[i]).isDestroyed())
                    continue;

                //up left
                if(playerOx1 < NPCOx1 && NPCOx1 < playerOx2 && playerOy1 < NPCOy1 && NPCOy1 < playerOy2){
                    ((Items)enemyArray[i]).action(player);
                    if(((Items)enemyArray[i]).getItem() != Items.Item.DOOR)
                        ((Items)enemyArray[i]).destroy();
                    return 0;
                }
                //down left
                if(playerOx1 < NPCOx1 && NPCOx1 < playerOx2 && playerOy1 < NPCOy2 && NPCOy2 < playerOy2){
                    ((Items)enemyArray[i]).action(player);
                    if(((Items)enemyArray[i]).getItem() != Items.Item.DOOR)
                        ((Items)enemyArray[i]).destroy();
                    return 0;
                }
                //up right
                if(playerOx1 < NPCOx2 && NPCOx2 < playerOx2 && playerOy1 < NPCOy1 && NPCOy1 < playerOy2){
                    ((Items)enemyArray[i]).action(player);
                    if(((Items)enemyArray[i]).getItem() != Items.Item.DOOR)
                        ((Items)enemyArray[i]).destroy();
                    return 0;
                }
                //down right
                if(playerOx1 < NPCOx2 && NPCOx2 < playerOx2 && playerOy1 < NPCOy2 && NPCOy2 < playerOy2){
                    ((Items)enemyArray[i]).action(player);
                    if(((Items)enemyArray[i]).getItem() != Items.Item.DOOR)
                        ((Items)enemyArray[i]).destroy();
                    return 0;
                }

            }
        }
        return 2;
    }

    private void returnAllItems(){
        // enemy's reviving
        for(int i = 0; i < countOfEmenies; i++){
            if(!((Enemy)enemyArray[i]).isAlive()) {
                ((Enemy) enemyArray[i]).makeAlive();
                //setToDefaultPosition
            }
        }
        for(int i = countOfDrawable - countOfItems; i < countOfDrawable; i++){
            if( !(((Items)enemyArray[i]).getItem() == Items.Item.LIFE) )
                ((Items)enemyArray[i]).setNotDestroyed();
        }
    }
}
