package Model;


public class Player implements NPC{

    private int score;
    private int Ox, platformOx1, platformOx2, shootingOx;
    private int Oy, groundOy, platformOy, shootingOy;
    private int speed;
    private int width, shootingWidth = 15;
    private int height, shootingHeight = 15;
    private int life;
    private int shootingCounter = 100;
    public boolean  onPlatform;
    private boolean jumpThread, shooting, ifFalling, speedBoost, shootThread, isRunning;
    private Panels dynamicPanel;
    private String playerImageName = "Data/Images/playerImageRight.png";
    private String shootingImageName = "Data/Images/shootingImage.png";
    public enum Direction{
        RIGHT,
        LEFT,
        NONE
    }public Direction direction, shootingDirection = Direction.RIGHT, whileShootingDirection = Direction.RIGHT;

    public Player(){}
    public Player(int Ox, int Oy, int lifeLevel, int groundOy, int width, int height, int speed) {
        this.Ox = Ox; this.width = width;
        this.Oy = Oy; this.height = height;
        this.groundOy = groundOy;
        this.speed = speed;
        direction = Direction.NONE;
        life = lifeLevel;
        onPlatform = false;
        jumpThread = false;
        ifFalling = false;
    }

    public int getOx(){return Ox;}
    public int getOy(){return Oy;}
    public int getShootingOx(){return shootingOx;}
    public int getShootingOy(){return shootingOy;}
    public int getSpeed(){return speed;}
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public int getShootingWidth(){return shootingWidth;}
    public int getShootingHeight(){return shootingHeight;}
    public int getLife(){return life;}
    public int getScore(){return score;}
    public boolean isJumping(){return jumpThread;}
    public boolean isFalling(){return ifFalling;}
    public boolean isOnPatform(){return onPlatform;}
    public boolean isShooting(){return shooting;}
    public void makeHurt(){life--; speed = 15; speedBoost = false; stopShooting();}
    public void increaseScore(int points){score += points;}
    public void stopJumping(){jumpThread = false;}
    public void stopFalling(){ifFalling = false;}
    public void stopShooting(){shooting = false;}
    public void setOy(int Oy){this.Oy = Oy;}
    public void setOx(int Ox){this.Ox = Ox;}
    public void setToPosition(int Ox, int Oy){this.Ox = Ox; this.Oy = Oy;}
    public void clearDirection(){direction = Direction.NONE;}
    public void setOnPlatform(int NPCOx1, int NPCOx2, int NPCOy1){onPlatform = true; platformOx1 = NPCOx1; platformOx2 = NPCOx2; platformOy = NPCOy1;}
    public void setNotOnPlatform(){onPlatform = false; platformOx1 = 0; platformOx2 = 0; platformOy = 0; dynamicPanel = null;}
    public void setDynamicPanel(Panels panel){ this.dynamicPanel = panel;}
    public void setDynamicPanelNotHere(){if(dynamicPanel != null){dynamicPanel.setPlayerNotHere();}}
    public void setSpeed(int speed){this.speed = speed;}
    public void setLife(int life){this.life = life;}
    public void setSpeedBoost(){speedBoost = true; speed += 5;}
    public void setNotSpeedBoost(){speedBoost = false; speed = 15;}
    //public Image getImage(){return playerImage;}
    public String getImageName(){return  playerImageName;}
    public String getShootingImageName(){return shootingImageName;}
    //public void setRunning(){isRunning = true;}

    public void move(int keyKode){

        //*shooting
        if(keyKode == 88 && !shootThread && !jumpThread){
            shooting = true;

            switch (shootingDirection) {
                case RIGHT:
                    shootingOx = Ox + getWidth() + 5;
                    break;
                case LEFT:
                    shootingOx = Ox - 35;
                    break;
            }
            shootingOy = Oy + 10;
            shoot();
            return;
        }

        //*jumping
        if(keyKode == 32 && !jumpThread){

            Thread spaceThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    onPlatform = false;
                    jumpThread = true;
                    int counter = 20;

                    if(!ifFalling){
                        for (int i = 50; i > 0; i -= counter, counter -= 5) {
                            Oy -= i;
                            if(speedBoost)
                                Oy -= 1;
                            if (direction == Direction.LEFT)
                                Ox -= speed;
                            if (direction == Direction.RIGHT)
                                Ox += speed;
                            try {
                                Thread.sleep(25);
                            } catch (InterruptedException e) {
                            }
                        }
                    }

                    //*FALLING
                    ifFalling = true;
                    counter = 5;
                    for(int i = 2; i <= 350; counter += 1, i += 2) {

                        if(!isJumping()){
                            jumpThread = false;
                            ifFalling = false;
                            return;
                        }

                        //*to stop falling too fast
                        if(i >= 30) {
                            i = 30;
                            Oy += i;
                        }
                        else{
                            Oy += i;
                        }

                        if(direction == Direction.LEFT)
                            Ox -= getSpeed();
                        if(direction == Direction.RIGHT)
                            Ox += getSpeed();

                        if(isOnPatform()){
                            if(Ox + width < platformOx1 || Ox > platformOx2){
                                onPlatform = false;
                            }
                            else{
                                onPlatform = true;
                                if(Oy >= platformOy){
                                    Oy = platformOy - height;
                                    break;
                                }
                            }
                        }
                        if(Oy >= groundOy){
                            Oy = groundOy;
                            jumpThread = false;
                            ifFalling = false;
                            direction = Direction.NONE;
                            return;
                        }
                        try {
                            Thread.sleep(35);
                        }catch(InterruptedException e){}
                    }

                    jumpThread = false;
                    ifFalling = false;
                }
            });spaceThread.start();
            return;
        }

        switch(keyKode){
            case 37:
                direction = Direction.LEFT;
                playerImageName = "Data/Images/playerImageLeft.png";
                if(!shootThread)
                    shootingDirection = Direction.LEFT;
                Ox -= speed;
                if(isOnPatform()){
                    if(Ox + width < platformOx1 && dynamicPanel == null){
                        ifFalling = true;
                        Ox += speed;
                        move(32);
                    }
                    else if(dynamicPanel != null){
                        if(Ox + width < dynamicPanel.getOx()){
                            ifFalling = true;
                            dynamicPanel = null;
                            Ox += speed;
                            move(32);
                        }
                    }
                }
                break;
            case 39:
                direction = Direction.RIGHT;
                playerImageName = "Data/Images/playerImageRight.png";
                if(!shootThread)
                    shootingDirection =  Direction.RIGHT;
                Ox += speed;
                if(isOnPatform()){
                    if(Ox > platformOx2 && dynamicPanel == null){
                        ifFalling = true;
                        Ox -= speed;
                        move(32);
                    }
                    else if(dynamicPanel != null){
                        if(Ox > dynamicPanel.getOx()){
                            ifFalling = true;
                            dynamicPanel = null;
                            Ox -= speed;
                            move(32);
                        }
                    }
                }
                break;
        }

        if(shootThread && direction != Direction.NONE){
            whileShootingDirection = direction;
        }

        if(Ox <= speed)
            Ox = speed +10;
        if(Ox >= 1480)
            Ox = 1480;
    }

    public void shoot(){

        Thread shootingThread = new Thread(new Runnable() {
            @Override
            public void run(){
                shooting = true;
                shootThread = true;
                for(int i = 0; i < shootingCounter; i++){

                    if(!shooting){
                        shootThread = false;
                        shootingDirection = whileShootingDirection;
                        return;
                    }

                    switch(shootingDirection){
                        case LEFT:
                            shootingOx -= 6;
                            break;
                        case RIGHT:
                            shootingOx += 6;
                    }

                    if(shootingOx >= 1480) {
                        shooting = false;
                        shootThread = false;
                        shootingDirection = whileShootingDirection;
                        return;
                    }
                    if(shootingOx < - 70) {
                        shooting = false;
                        shootThread = false;
                        shootingDirection = whileShootingDirection;
                        return;
                    }

                    try {
                        Thread.sleep(20);
                    }catch(InterruptedException e){}
                }
                shooting = false;
                shootThread = false;
                shootingDirection = whileShootingDirection;
            }
            });
            shootingThread.start();
        }
}
