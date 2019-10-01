package Tests;

import Model.MainModel;
import Model.Player;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainModelTest {

    @org.junit.Test
    public void checkForBoxesTest(){
        try {
            MainModel main = new MainModel(0);
            main.makeNewEnemy(0,0,50,50,0,false,false,0,0,100, "", "");
            Player player = main.getPlayer();
            player.setToPosition(10,10);

            assertEquals(0, main.checkForBoxes());
        }catch(IOException e){assertEquals(0, 1);}
    }

    @org.junit.Test
    public void checkForBoxesTest2(){
        try {
            MainModel main = new MainModel(0);
            main.makeNewEnemy(0,0,50,50,0,false,false,0,0,100, "", "");
            Player player = main.getPlayer();
            player.setToPosition(100,100);

            assertEquals(2, main.checkForBoxes());
        }catch(IOException e){assertEquals(0, 1);}
    }

    @org.junit.Test
    public void MainModelTest1(){
        try {
            MainModel main = new MainModel(MainModel.levelCount - (MainModel.levelCount - 1));

            assertEquals(0, 0);
        }catch(IOException e){assertEquals(0, 1);}
    }

    @org.junit.Test
    public void MainModelTest2(){
        try {
            MainModel main = new MainModel(MainModel.levelCount + 1);

            assertEquals(0, 1);
        }catch(IOException e){assertEquals(0, 0);}
    }

    @org.junit.Test
    public void JumpModelTest(){
        try {
            MainModel main = new MainModel(MainModel.levelCount);
            Player player = main.getPlayer();
            player.setToPosition(110, 0);
            player.move(32);
            Thread.sleep(2000);

            //* should be 550~
            if(player.getOy() != main.getGroundOy())
                assertEquals(0, 0);
            else
                assertEquals(0, 1);
        }
        catch(IOException e){assertEquals(1, 0);}
        catch(InterruptedException e){assertEquals(0, 1);}

    }
}