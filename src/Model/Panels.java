package Model;



import static Model.Player.Direction.*;

public class Panels implements NPC{
    private int Ox1, width, Oy1, height, speed, fieldOx1, fieldOx2;
    private boolean canMove, playerHere;
    String imageName;
    private Player.Direction direction = NONE;

    public int getOx() {return Ox1;}
    public int getWidth() {return width;}
    public int getOy() {return Oy1;}
    public int getHeight() {return height;}
    public int getSpeed(){return speed;}
    public boolean canMove(){return canMove;}
    public boolean isPlayerHere(){return playerHere;}
    public void setPlayerHere(){playerHere = true;}
    public void setPlayerNotHere(){playerHere = false;}
    public Player.Direction getDirection(){return direction;}
    public String getImageName(){return imageName;}

    public Panels(){}
    public Panels(int Ox1, int Oy1, int width, int height, boolean canMove, int speed, int fieldOx1, int fieldOx2, String imageName){
        this.Ox1 = Ox1; this.Oy1 = Oy1;
        this.width = width; this.height = height;
        this.canMove = canMove;
        this.imageName = imageName;
        if(canMove) {
            direction = LEFT;
            this.fieldOx1 = fieldOx1;
            this.fieldOx2 = fieldOx2;
            this.speed = speed;
        }
    }

    public void move(int keyKode){
        if(!canMove)
            return;

        switch(direction){
            case LEFT:
                Ox1 -= speed;
                break;
            case RIGHT:
                Ox1 += speed;
                break;
        }
        if(Ox1 <= fieldOx1) {
            Ox1 = fieldOx1;
            direction = RIGHT;
        }
        if(Ox1 >= fieldOx2) {
            Ox1 = fieldOx2;
            direction = Player.Direction.LEFT;
        }
    }
}
