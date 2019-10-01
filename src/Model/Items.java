package Model;


public class Items implements NPC{
    int Ox;
    int Oy;
    int width;
    int height;
    int speed;
    int counter = 5;
    static boolean doorCanBeOpened = false;
    boolean destroyed;
    int points = 100;
    String imageName;
    public static final int itemCount = 5;

    public enum Item{
        DOOR,
        KEY,
        BOOST,
        LIFE,
        POINTS,
    }Item item;
    enum ItemDirection{
        UP,
        DOWN,
        NONE,
    }ItemDirection itemDirection;

    public int getOx(){return Ox;}
    public int getOy(){return Oy;}
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public int getSpeed(){return speed;}
    public boolean isDestroyed(){return destroyed;}
    //public boolean isDoorCanBeOpened(){return doorCanBeOpened;}
    public void destroy(){destroyed = true;}
    public void setNotDestroyed(){destroyed = false;}
    public Item getItem(){return item;}
    public static void openDoor(){doorCanBeOpened = true;}
    public String getImageName(){return imageName;}

    public Items(int Ox, int Oy, int width, int height, int speed, Item item, String imageName){
        this.Ox = Ox; this.Oy = Oy; this.speed =speed;
        this.width = width; this.height = height;
        this.imageName = imageName;

        this.item = item;
        switch (item){
            case KEY:
                itemDirection = ItemDirection.DOWN;
                break;
            case DOOR:
                itemDirection = ItemDirection.NONE;
                break;
            case BOOST:
                itemDirection = ItemDirection.UP;
                break;
            case LIFE:
                itemDirection = ItemDirection.UP;
                break;
            case POINTS:
                itemDirection = ItemDirection.UP;
        }
    }

    public void move(int keyKode){

        switch(itemDirection){
            case NONE:
                break;
            case UP:
                Oy += speed;
                counter++;
                break;
            case DOWN:
                Oy -= speed;
                counter--;
                break;
        }

        switch(counter){
            case 10:
                itemDirection = ItemDirection.DOWN;
                break;
            case 0:
                itemDirection = ItemDirection.UP;
                break;
            default:
                return;
        }
    }

    public void action(Player player){

        switch(item){
            case BOOST:
                player.setSpeedBoost();
                break;
            case DOOR:
                if(doorCanBeOpened){
                    doorCanBeOpened = false;
                    MainModel.goToNextLevel();
                }
                break;
            case KEY:
                doorCanBeOpened = true;
                break;
            case LIFE:
                player.setLife(player.getLife() + 1);
                break;
            case POINTS:
                player.increaseScore(points);
                break;
        }
    }
}
