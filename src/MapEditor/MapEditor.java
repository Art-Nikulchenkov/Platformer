package MapEditor;
/*
panels <-5 V2
enemy V5
 */

import Model.Items;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Vector;

class Drawable {
    Image image;
    int Ox, Oy, width, height;
    Drawable(int Ox, int Oy, int width, int height, Image image){
        this.Ox = Ox; this.Oy = Oy;
        this.width = width; this.height =height;
        this.image= image;
    }
    public String toString(){
        return Ox +"\n" + Oy + "\n" + width + "\n" + height + "\n" + image.toString();
    }
}


class MapEditor extends JPanel implements ActionListener{
    private JFrame frame;
    private File newLevel;
    private Vector<Drawable> drawable;
    private JPanel contentPane;

    private Timer timer = new Timer(10, this);

    private int MOx, MOy, width, height, plOx = 140, plOy = 700, enemyCount, panelCount, itemCount;
    private Vector<String> enemies, panels, items;
    private JTextField pictureNumberText, fieldOx1Text, fieldOx2Text, widthText, heightText, exceptionText;
    private String shootingImageName = "Data/Images/shootingImage.png";
    private boolean canMove, shootingSkills;
    enum Item{
        PANEL,
        ENEMY,
        ITEM,
        PLAYER,
        NULL,
    }Item item = Item.NULL;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Map editor");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setUndecorated(false);
        frame.setSize(500, 120);
        MapEditor edit = new MapEditor(frame);
        frame.add(edit);
    }

    public MapEditor(JFrame jframe){
        this.frame = jframe;
        drawable = new Vector<Drawable>(5);

        contentPane = new JPanel();
        contentPane.setLayout(new FlowLayout());
        contentPane.setSize(800, 100);
        enemies = new Vector<String>(2);
        panels = new Vector<String>(2);
        items = new Vector<String>(2);

        //Panel Button
        JButton panelButton = new JButton("Panel");
        panelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                item = Item.PANEL;
            }
        });
        panelButton.setSize(50,20);
        contentPane.add(panelButton);

        //Enemy Button
        JButton enemyButton = new JButton("Enemy");
        enemyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                item = Item.ENEMY;
            }
        });
        enemyButton.setSize(50,20);
        contentPane.add(enemyButton);

        //Item Button
        JButton itemButton = new JButton("Item");
        itemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                item = Item.ITEM;
            }
        });
        itemButton.setSize(50,20);
        contentPane.add(itemButton);

        //Player Button
        JButton playerButton = new JButton("Player");
        playerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                item = Item.PLAYER;
            }
        });
        playerButton.setSize(50,20);
        contentPane.add(playerButton);

        //Done Button
        JButton doneButton = new JButton("Done!");
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newLevel = new File("Data/Levels/newLevel.txt");
                try {
                    if(panelCount == 0 && itemCount == 0 && enemyCount == 0) {
                        makeExceptionText(new Exception("Nothing has placed yet"));
                        return;
                    }

                    FileWriter writer = new FileWriter(newLevel);
                    /*
                    if(plOx == 0 || plOy == 0){
                        plOx = Integer.parseInt(fieldOx1Text.getText());
                        plOy = Integer.parseInt(fieldOx2Text.getText());
                    }
                    */
                    System.out.println(plOy + " " + plOx);
                    writer.write("Data/Images/mainImage0.png\n" + "Data/Images/groundImage1.png \n" + (plOy - 50) +"\n" + plOx + "\n\n\n");

                    //enemy
                    writer.write(enemyCount + "\n\n\n");
                    for(String i: enemies){
                        writer.write(i);
                    }

                    //panel
                    writer.write(panelCount + "\n\n\n");
                    for(String i: panels){
                        writer.write(i);
                    }

                    //item
                    writer.write(itemCount + "\n\n\n");
                    for(String i: items){
                        writer.write(i);
                    }

                    writer.flush();
                }
                catch(NumberFormatException ex){System.out.println(ex.getMessage());}
                catch(Exception ex){System.out.println(ex.getMessage());}
            }
        });
        doneButton.setSize(50,20);
        contentPane.add(doneButton);

        //Shooting Flag
        JCheckBox shootingBox = new JCheckBox("Shooting skills");
        shootingBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if(shootingBox.isSelected()){
                    shootingSkills = true;
                }
                else{
                    shootingSkills = false;
                }
            }
        });
        contentPane.add(shootingBox);

        //Moving Flag
        JCheckBox movingBox = new JCheckBox("Moving skills");
        movingBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if(movingBox.isSelected()){
                    canMove = true;
                }
                else{
                    canMove = false;
                }
            }
        });
        contentPane.add(movingBox);

        //Number of Picture
        pictureNumberText = new JTextField( 5);
        pictureNumberText.setToolTipText("Picture number");
        contentPane.add(pictureNumberText);

        //Width
        widthText = new JTextField( 5);
        widthText.setToolTipText("Width");
        contentPane.add(widthText);

        //Height
        heightText = new JTextField( 5);
        heightText.setToolTipText("Height");
        contentPane.add(heightText);

        //Field Ox1
        fieldOx1Text = new JTextField( 5);
        fieldOx1Text.setToolTipText("Field Ox1");
        contentPane.add(fieldOx1Text);

        //Field Ox2
        fieldOx2Text = new JTextField( 5);
        fieldOx2Text.setToolTipText("Field Ox2");
        contentPane.add(fieldOx2Text);

        //Exception text
        exceptionText = new JTextField( 15);
        exceptionText.setToolTipText("Exception text");
        contentPane.add(exceptionText);

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                MOx = e.getX();
                MOy = e.getY();
                System.out.println("Mouse: " + MOx +", " + MOy);
                if(MOy > 100 && MOy < 800)
                    place();
                repaint();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }
        });
        frame.add(contentPane);
        frame.setVisible(true);
    }

    public void makeNewEnemy(){
        try {
            enemyCount++;
            int picNum = Integer.parseInt(pictureNumberText.getText());
            if(picNum < 1 || picNum > Items.itemCount)
                throw new Exception("Wrong params: item count");
            String imageName = "Data/Images/enemyImage" + picNum + "Left.png";
            StringBuffer buffer = new StringBuffer();

            buffer.append("" + MOx + "\n");
            buffer.append("" + (MOy - 71) + "\n");
            buffer.append("" + 50 + "\n");
            buffer.append("" + 51 + "\n");
            buffer.append("" + 1 + "\n");

            if (shootingSkills) {
                buffer.append("true\n");

            }
            else {
                buffer.append("false\n");
            }
            if (canMove) {
                buffer.append("true\n");
            }
            else {
                buffer.append("false\n");
            }
            if (canMove) {
                int fOx1 = Integer.parseInt(fieldOx1Text.getText());
                int fOx2 = Integer.parseInt(fieldOx2Text.getText());
                if(fOx1 >= fOx2)
                    throw new Exception("Bad params: fieldOx1 and/or fieldOx2");

                buffer.append("" + fOx1 + "\n");
                buffer.append("" + fOx2 + "\n");
            } else {
                buffer.append("0\n0\n");
            }
            buffer.append(imageName);
            if (shootingSkills) {
                buffer.append("\n" + shootingImageName);
            }
            buffer.append("\n\n\n");

            enemies.add(buffer.toString());
            Image enemyImage = new ImageIcon(imageName).getImage();


            Drawable obj = new Drawable(MOx, MOy - 71, 50, 51, enemyImage);
            drawable.add(obj);
            System.out.println("Ok");
            cleanExceptionText();
        }
        catch(NumberFormatException e){enemyCount--;System.out.println("NumberFormatEx");makeExceptionText(e);}
        catch (Exception e){enemyCount--;System.out.println(e.getMessage());makeExceptionText(e);}

    }

    public void makeNewItem(){
        try {
            System.out.println("item");
            itemCount++;
            String imageName;
            int picNum = Integer.parseInt(pictureNumberText.getText());

            if(picNum < 1 || picNum > Items.itemCount)
                throw new Exception("Wrong params: item count");

            Image itemImage;

            switch (picNum) {
                case 1:
                    imageName = "Data/Images/doorImage.png";
                    break;
                case 2:
                    imageName = "Data/Images/keyImage.png";
                    break;
                case 3:
                    imageName = "Data/Images/lifeImage.png";
                    break;
                case 4:
                    imageName = "Data/Images/boostImage.png";
                    break;
                case 5:
                    imageName = "Data/Images/pointImage.png";
                    break;
                default:
                    imageName = "Data/Images/lifeImage.png";
                    break;
            }

            StringBuffer buffer = new StringBuffer(50);
            buffer.append("" + MOx + "\n");
            buffer.append("" + (MOy - 60) + "\n");
            buffer.append("" + 30 + "\n");
            buffer.append("" + 30 + "\n");
            buffer.append("" + 1 + "\n");
            buffer.append(imageName + "\n");
            buffer.append(picNum + "\n\n\n");
            items.add(buffer.toString());

            itemImage = new ImageIcon(imageName).getImage();
            Drawable obj = new Drawable(MOx, MOy - 60, 30, 30, itemImage);
            drawable.add(obj);
            System.out.println("Ok");
            cleanExceptionText();
        }
        catch(NumberFormatException e){itemCount--;System.out.println("NumberFormatException");makeExceptionText(e);}
        catch(Exception e){itemCount--;System.out.println(e.getMessage());makeExceptionText(e);}
    }

    public void makeNewPanel(){
        try {
            System.out.println("panel");
            panelCount++;
            try {
                width = Integer.parseInt(widthText.getText());
                height = Integer.parseInt(heightText.getText());
            }catch (Exception ex){throw ex;}
            if(width <= 0 || height <=0)
                throw new Exception("Wrong params: width/height");

            int picNum = Integer.parseInt(pictureNumberText.getText());
            if(picNum < 1 || picNum > 3)
                throw new Exception("Wrong params: picture number");

            String imageName = "Data/Images/platformImage" + picNum + ".png";

            StringBuffer buffer = new StringBuffer(60);
            buffer.append("" + (MOx - 7) + "\n");
            buffer.append("" + (MOy - height + 3) + "\n");
            buffer.append("" + width + "\n");
            buffer.append("" + height + "\n");

            if (canMove) {
                buffer.append("true" + "\n");
            }
            else {
                buffer.append("false" + "\n");
            }

            buffer.append(1 + "\n");

            if (canMove) {
                int fOx1 = Integer.parseInt(fieldOx1Text.getText());
                int fOx2 = Integer.parseInt(fieldOx2Text.getText());
                if(fOx1 >= fOx2)
                    throw new Exception("Bad params: fieldOx1 and/or fieldOx2");
                buffer.append(fOx1 + "\n");
                buffer.append(fOx2 + "\n");
            } else {
                buffer.append("0\n0\n");
            }

            buffer.append(imageName);
            buffer.append("\n\n\n");
            panels.add(buffer.toString());

            Image panelImage = new ImageIcon(imageName).getImage();
            Drawable obj = new Drawable(MOx - 7, MOy - height + 3, width, height, panelImage);
            drawable.add(obj);
            System.out.println("Ok");
            cleanExceptionText();
        }
        catch(NumberFormatException e) {panelCount--;System.out.println("NumberFormatException");makeExceptionText(e);}
        catch(Exception e) {panelCount--;System.out.println(e.getMessage());makeExceptionText(e);}
    }

    public void setPlayer(int Ox, int Oy){
        try {
            System.out.println("player set");
            if(Ox == -1 && Oy == -1) {
                System.out.println("Mouse");
                plOx = MOx;
                plOy = MOy;
            }
            else{
                plOx = Integer.parseInt(fieldOx1Text.getText());
                plOy = Integer.parseInt(fieldOx2Text.getText());
            }
            System.out.println("Ok");
        }
        catch(Exception e){}
    }

    public void place(){
        System.out.print("place: ");
        switch(item){
            case ENEMY:
                makeNewEnemy();
                break;
            case PLAYER:
                setPlayer(-1, -1);
                break;
            case ITEM:
                makeNewItem();
                break;
            case PANEL:
                makeNewPanel();
                break;
            case NULL:
                break;
            default:
                break;
        }

    }

    private void makeExceptionText(Exception ex){
        if(ex instanceof NumberFormatException){
            exceptionText.setText("Wrong number format");
            return;
        }
        exceptionText.setText(ex.getMessage());
    }
    private void cleanExceptionText(){exceptionText.setText("");}

    @Override
    public void paint(Graphics g){
        g.drawImage(new ImageIcon("Data/Images/mainImage0.png").getImage(),0, 0, frame.getWidth(), frame.getHeight(), null);
        g.drawImage(new ImageIcon("Data/Images/groundImage1.png").getImage(),0, 700 + 50, null);
        for(Drawable i: drawable){
            g.drawImage(i.image, i.Ox, i.Oy, i.width, i.height, null);
        }
        contentPane.paint(g);
        for(int j = 100; j < 1000; j += 100) {
            for (int i = 0; i < 2000; i += 100) {
                g.drawString("| " + i, i, j);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
