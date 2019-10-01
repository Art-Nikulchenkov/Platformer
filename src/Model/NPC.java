package Model;

public interface NPC {
    public int getOx();
    public int getOy();
    public int getWidth();
    public int getHeight();
    public int getSpeed();
    public void move(int keyKode);
    public String getImageName();
}
