package Model;


public class Enemy implements NPC{
    private int Ox, Oy, speed, width, height, shootingOx, shootingOy, shootingWidth = 20, shootingHeight = 20, shootingCounter, fieldOx1, fieldOx2;
    private boolean shootingSkills, canMove, alive = true, chosenPlayerDirection;
    private Player.Direction direction = Player.Direction.NONE, playerDirection;
    private Player player;
    private String imageName, shootingImageName;
    private int imageNumber;
    public Enemy(){}
    public Enemy(int Ox, int Oy, int width, int height, int speed, boolean shootingSkills, boolean canMove, int fieldOx1,
                 int fieldOx2, String imageName, String shootingImageName){
        this.Ox = Ox; this.Oy = Oy; this.speed = speed;
        this.width = width; this.height = height;
        this.fieldOx1 = fieldOx1;
        this.fieldOx2 = fieldOx2;
        this.shootingSkills = shootingSkills;
        this.canMove = canMove;
        direction = Player.Direction.LEFT;
        this. shootingOy = Oy + height/2;

        this.imageName = imageName;
        this.shootingImageName = shootingImageName;
    }

    public int getOx(){if(alive)return Ox; else return -100;}
    public int getOy(){if(alive)return Oy; else return -100;}
    public int getShootingOx(){if(alive)return shootingOx; else return -100;}
    public int getShootingOy(){if(alive)return shootingOy; else return -100;}
    public int getShootingWidth(){if(alive)return shootingWidth; else return 0;}
    public int getShootingHeight(){if(alive)return shootingHeight; else return 0;}
    public int getSpeed(){return speed;}
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public void setShootingCounter(int newCounter){this.shootingCounter = newCounter;}
    public void setImageNumber(int imageNumber){this.imageNumber = imageNumber;}
    public void makeAlive(){alive = true;}
    public void setPlayer(Player player){this.player = player;}
    public boolean canShooting(){return shootingSkills;}
    public boolean isAlive(){return alive;}
    public String getImageName(){return imageName;}
    public String getShootingImageName(){return shootingImageName;}
    public void killEnemy(){alive = false;}


    public void move(int empty){
        if(!alive)
            return;
        if(!canMove){
            if(shootingSkills) {
                if (shootingCounter < 100) {
                    if(shootingCounter == 0){
                        shootingOx = Ox - 20;
                        shootingOy = Oy + height/2;
                    }
                    shoot();
                } else {
                    shootingCounter = 0;
                    shootingOx = Ox - width - 20;
                }
            }
            return;
        }

        switch(direction){
            case LEFT:
                Ox -= speed;
                if(shootingSkills) {
                    if (shootingCounter < 100) {
                        if(shootingCounter == 0){
                            shootingOx = Ox - 20;
                            shootingOy = Oy + height/2;
                        }
                        shoot();
                    } else {
                        shootingCounter = 0;
                        shootingOx = Ox - width - 20;
                    }
                }
                break;
            case RIGHT:
                shootingCounter = 0;
                shootingOx = - 30;
                shootingOy = - 30;
                Ox += speed;
                break;
        }
        if(Ox <= fieldOx1) {
            Ox = fieldOx1;
            direction = Player.Direction.RIGHT;
            imageName = "Data/Images/enemyImage" + imageNumber + "Right.png";
            //enemyImage = new ImageIcon("Data/Images/enemyImage" + imageNumber + "Right.png").getImage();////////
        }
        if(Ox >= fieldOx2) {
            Ox = fieldOx2;
            direction = Player.Direction.LEFT;
            chosenPlayerDirection = false;
            imageName = "Data/Images/enemyImage" + imageNumber + "Left.png";
            //enemyImage = new ImageIcon("Data/Images/enemyImage" + imageNumber + "Left.png").getImage();/////////
        }
    }

    private void shoot(){
        if(!shootingSkills)
            return;

        if(shootingCounter < 100){

            if(!chosenPlayerDirection && player.getOx() < Ox){
                playerDirection = Player.Direction.LEFT;
                chosenPlayerDirection = true;
            }else{
                if(!chosenPlayerDirection && player.getOx() > Ox){
                    playerDirection = Player.Direction.RIGHT;
                    chosenPlayerDirection = true;
                }
            }

            switch(playerDirection){
                case LEFT:
                    shootingOx -= 5;
                    break;
                case RIGHT:
                    shootingOx += 5;
                    break;
            }

            if(shootingOx <= 0 || shootingOx >=1700){
                shootingOx = Ox;
                shootingCounter = 100;
                chosenPlayerDirection = false;
                return;
            }
            shootingCounter++;
        }
        else{
            chosenPlayerDirection = false;
        }
    }
}
