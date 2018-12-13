package kosy.shipyo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.json.JSONObject;
import java.util.ArrayList;

//TODO a jó helyre kerüljön minden ivCells imageview
//TODO automatizálni valahogy őket
//TODO ne ütközzön bele más tárgyba + ne repüljön ki a pályából a hajó
//TODO drawable fgv-t meghívni
//TODO kitalálni, hogy tároljam az elemeket a DBben.....

public class game extends Activity {

    final static int maxN = 6;
    private ImageView[][] ivCells = new ImageView[maxN][maxN];
    private ImageView[][] bgCells = new ImageView[maxN][maxN];
    public Context context;
    private Drawable[] drawCell = new Drawable[9];
    //private boolean once = true;
    float x = 0,y = 0,dx = 0 ,dy = 0, distance = 0, AMtemp = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //ne legyen cím az activityen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //activityben full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //az activity viewjét beállítani "game_layout" XML szerint
        setContentView(R.layout.game_layout);
        //beállítani a Contextre magát az activityt
        context = this;

        //meghívjuk a függvényeket
        loadResources();
        designTabla();
        makeBackGround();
        makeJsonObject();
        //setShips();
        //getShipsCordinates(5,3);

    }


    private void makeJsonObject(){
        JSONObject kishajo = new JSONObject();
        JSONObject JSkettesA1 = new JSONObject();
        JSONObject JSkettesA2 = new JSONObject();
        JSONObject JSkettesB1 = new JSONObject();
        JSONObject JSkettesB2 = new JSONObject();
        JSONObject JSwood1 = new JSONObject();
        JSONObject JSwood2 = new JSONObject();

        //public Ship(int id, int i, int j, int shipmerete, Ship secondShipID, int positionOfShip, int rajzID)
        try {
            kishajo.put("id","102");    kishajo.put("i",2);     kishajo.put("j",1);     kishajo.put("shipmerete",1);    kishajo.put("rajzID",3); kishajo.put("direction",1);
            JSkettesA1.put("id","103"); JSkettesA1.put("i",3); JSkettesA1.put("j",3);   JSkettesA1.put("shipmerete",2); JSkettesA1.put("secondShipId",104); JSkettesA1.put("positionOfShip",1); JSkettesA1.put("rajzID",4); JSkettesA1.put("direction",1);
            JSkettesA2.put("id","104"); JSkettesA2.put("i",3); JSkettesA2.put("j",4);   JSkettesA2.put("shipmerete",2); JSkettesA2.put("secondShipId",103); JSkettesA2.put("positionOfShip",2); JSkettesA2.put("rajzID",2); JSkettesA2.put("direction",1);
            JSkettesB1.put("id","105"); JSkettesB1.put("i",1); JSkettesB1.put("j",2);   JSkettesB1.put("shipmerete",2); JSkettesB1.put("secondShipId",105); JSkettesB1.put("positionOfShip",1); JSkettesB1.put("rajzID",5); JSkettesB1.put("direction",0);
            JSkettesB2.put("id","106"); JSkettesB2.put("i",2); JSkettesB2.put("j",2);   JSkettesB2.put("shipmerete",2); JSkettesB2.put("secondShipId",106); JSkettesB2.put("positionOfShip",2); JSkettesB2.put("rajzID",6); JSkettesB2.put("direction",0);
            JSwood1.put("id","100");    JSwood1.put("i",4);     JSwood1.put("j",3);     JSwood1.put("shipmerete",2);    JSwood1.put("secondShipId",101); JSwood1.put("positionOfShip",1); JSwood1.put("rajzID",7);          JSwood1.put("direction",0);
            JSwood2.put("id","101");    JSwood2.put("i",5);     JSwood2.put("j",3);     JSwood2.put("shipmerete",2);    JSwood2.put("secondShipId",100); JSwood2.put("positionOfShip",2); JSwood2.put("rajzID",8);          JSwood2.put("direction",0);
        } catch (Exception ex){
            System.out.println(ex);
        }

        //System.out.println(kishajo.length());
        //JSkettesA1.put("secondShipId",104); JSkettesA1.put("positionOfShip",1);
        ArrayList<Ship> ships = new ArrayList<>();
        try {

            Ship csonak =   new Ship(kishajo.getInt("id"),      kishajo.getInt("i"),    kishajo.getInt("j"),    kishajo.getInt("shipmerete"),   kishajo.getInt("rajzID"), kishajo.getInt("direction"));
            ships.add(csonak);
            Ship kettesA1 = new Ship(JSkettesA1.getInt("id"),   JSkettesA1.getInt("i"), JSkettesA1.getInt("j"), JSkettesA1.getInt("shipmerete"),JSkettesA1.getInt("secondShipId"),  JSkettesA1.getInt("positionOfShip"),JSkettesA1.getInt("rajzID"), JSkettesA1.getInt("direction"));
            ships.add(kettesA1);
            Ship kettesA2 = new Ship(JSkettesA2.getInt("id"),   JSkettesA2.getInt("i"), JSkettesA2.getInt("j"), JSkettesA2.getInt("shipmerete"),JSkettesA2.getInt("secondShipId"),  JSkettesA2.getInt("positionOfShip"),JSkettesA2.getInt("rajzID"), JSkettesA2.getInt("direction"));
            ships.add(kettesA2);
            Ship kettesB1 = new Ship(JSkettesB1.getInt("id"),   JSkettesB1.getInt("i"), JSkettesB1.getInt("j"), JSkettesB1.getInt("shipmerete"),JSkettesB1.getInt("secondShipId"),  JSkettesB1.getInt("positionOfShip"),JSkettesB1.getInt("rajzID"), JSkettesB1.getInt("direction"));
            ships.add(kettesB1);
            Ship kettesB2 = new Ship(JSkettesB2.getInt("id"),   JSkettesB2.getInt("i"), JSkettesB2.getInt("j"), JSkettesB2.getInt("shipmerete"),JSkettesB2.getInt("secondShipId"),  JSkettesB2.getInt("positionOfShip"),JSkettesB2.getInt("rajzID"), JSkettesB2.getInt("direction"));
            ships.add(kettesB2);
            Ship wood1 =    new Ship(JSwood1.getInt("id"),      JSwood1.getInt("i"),    JSwood1.getInt("j"),    JSwood1.getInt("shipmerete"),   JSwood1.getInt("secondShipId"),     JSwood1.getInt("positionOfShip"),JSwood1.getInt("rajzID"), JSwood1.getInt("direction"));
            ships.add(wood1);
            Ship wood2 =    new Ship(JSwood2.getInt("id"),      JSwood2.getInt("i"),    JSwood2.getInt("j"),    JSwood2.getInt("shipmerete"),   JSwood2.getInt("secondShipId"),     JSwood2.getInt("positionOfShip"),JSwood2.getInt("rajzID"), JSwood2.getInt("direction"));
            ships.add(wood2);

        } catch (Exception ex){
            System.out.println("masodik try : " + ex);
        }
    }

    final class Cells{

        private ImageView cells;
        private int id,i,j;
        private float x,y;
        public Cells(int id, ImageView cells, int i, int j, float x, float y) {
            this.id = id;
            this.cells = cells;
            this.i = i;
            this.j = j;
            this.x = x; System.out.println("x : " + x);
            this.y = y + (j*120); System.out.println("y : " + y);
        }

        public int getId() {
            return id;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

    }

    private void listenOnDrag(final View elsohajo, final Ship ship){


        elsohajo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = MotionEventCompat.getActionMasked(event);
                //event.getAction() & MotionEvent.ACTION_MASK

                x = 0;
                y = 0;
                dx = 0;
                dy = 0;
                distance = 0;
                switch(action) {
                    case (MotionEvent.ACTION_DOWN) :

                        x = event.getX();
                        y = event.getY();
                        dx = x - v.getX();
                        dy = y - v.getY();
                        break;

                    case (MotionEvent.ACTION_MOVE) :

                        // ship jobb/bal
                        if (ship.getDirection() == 1) {
                            //hány részből áll a ship
                            if (ship.getShipmerete() == 1) {

                                ObjectAnimator animX = ObjectAnimator.ofFloat(v, "x", event.getX() - dx);
                                ObjectAnimator animY = ObjectAnimator.ofFloat(v, "y", v.getY());
                                AnimatorSet animSetXY = new AnimatorSet();
                                animSetXY.playTogether(animX, animY);
                                animSetXY.setDuration(300);
                                animSetXY.start();

                            } else if (ship.getShipmerete() == 2){
                                //a paraméternek megadott ship ha 1, akkor az eleje, ha 2 a közepe/vége, ha 3 a vége
                                AMtemp = ship.getPositionOfShip();
                                if (AMtemp == 1) {
                                    ObjectAnimator animX = ObjectAnimator.ofFloat(v, "x", event.getX() - dx);
                                    ObjectAnimator animY = ObjectAnimator.ofFloat(v, "y", v.getY());
                                    AnimatorSet animSetXY = new AnimatorSet();
                                    animSetXY.playTogether(animX, animY);
                                    animSetXY.setDuration(300);
                                    animSetXY.start();
                                    //TODO KILL ME
                                    //ObjectAnimator animXB = ObjectAnimator.ofFloat(ivCells[ship.getI()][ship.getJ()+1], "x", event.getX() - (event.getX() - ivCells[ship.getI()][ship.getJ()+1].getX()) + ScreenWidth()/maxN);
                                    //ObjectAnimator animYB = ObjectAnimator.ofFloat(ivCells[ship.getI()][ship.getJ()+1], "y", ivCells[ship.getI()][ship.getJ()+1].getY());
                                    ObjectAnimator animXB = ObjectAnimator.ofFloat(ivCells[ship.getI()][ship.getJ()+1], "x", event.getX() - dx+120);
                                    ObjectAnimator animYB = ObjectAnimator.ofFloat(ivCells[ship.getI()][ship.getJ()+1], "y", v.getY());
                                    AnimatorSet animSetXYB = new AnimatorSet();
                                    animSetXYB.playTogether(animXB, animYB);
                                    animSetXYB.setDuration(300);
                                    animSetXYB.start();

                                } else if (AMtemp == 2){

                                    ObjectAnimator animX = ObjectAnimator.ofFloat(v, "x", event.getX() - dx);
                                    ObjectAnimator animY = ObjectAnimator.ofFloat(v, "y", v.getY());
                                    AnimatorSet animSetXY = new AnimatorSet();
                                    animSetXY.playTogether(animX, animY);
                                    animSetXY.setDuration(300);
                                    animSetXY.start();

                                    ObjectAnimator animXB = ObjectAnimator.ofFloat(ivCells[ship.getI()][ship.getJ()-1], "x", event.getX() - dx-120);
                                    ObjectAnimator animYB = ObjectAnimator.ofFloat(ivCells[ship.getI()][ship.getJ()-1], "y", v.getY());
                                    AnimatorSet animSetXYB = new AnimatorSet();
                                    animSetXYB.playTogether(animXB, animYB);
                                    animSetXYB.setDuration(300);
                                    animSetXYB.start();

                                }

                            } else {

                                if (ship.getShipmerete() == 1) {

                                    ObjectAnimator animX = ObjectAnimator.ofFloat(v, "x", event.getX() - dx);
                                    ObjectAnimator animY = ObjectAnimator.ofFloat(v, "y", v.getY());
                                    AnimatorSet animSetXY = new AnimatorSet();
                                    animSetXY.playTogether(animX, animY);
                                    animSetXY.setDuration(300);
                                    animSetXY.start();

                                } else if (ship.getShipmerete() == 2) {
                                    //a paraméternek megadott ship ha 1, akkor az eleje, ha 2 a közepe/vége, ha 3 a vége
                                    AMtemp = ship.getPositionOfShip();
                                    if (AMtemp == 1) {
                                        ObjectAnimator animX = ObjectAnimator.ofFloat(v, "x", v.getX());
                                        ObjectAnimator animY = ObjectAnimator.ofFloat(v, "y", event.getY() - dy);
                                        AnimatorSet animSetXY = new AnimatorSet();
                                        animSetXY.playTogether(animX, animY);
                                        animSetXY.setDuration(300);
                                        animSetXY.start();
                                        //TODO KILL ME
                                        ObjectAnimator animXB = ObjectAnimator.ofFloat(ivCells[ship.getI()+1][ship.getJ()], "x", v.getX());
                                        ObjectAnimator animYB = ObjectAnimator.ofFloat(ivCells[ship.getI()+1][ship.getJ()], "y", event.getY() - dy + 120);
                                        AnimatorSet animSetXYB = new AnimatorSet();
                                        animSetXYB.playTogether(animXB, animYB);
                                        animSetXYB.setDuration(300);
                                        animSetXYB.start();

                                    } else if (AMtemp == 2) {

                                        ObjectAnimator animX = ObjectAnimator.ofFloat(v, "x", v.getX());
                                        ObjectAnimator animY = ObjectAnimator.ofFloat(v, "y", event.getY() - dy);
                                        AnimatorSet animSetXY = new AnimatorSet();
                                        animSetXY.playTogether(animX, animY);
                                        animSetXY.setDuration(300);
                                        animSetXY.start();

                                        ObjectAnimator animXB = ObjectAnimator.ofFloat(ivCells[ship.getI()-1][ship.getJ()], "x", v.getX());
                                        ObjectAnimator animYB = ObjectAnimator.ofFloat(ivCells[ship.getI()-1][ship.getJ()], "y", event.getY() - dy + 120);
                                        AnimatorSet animSetXYB = new AnimatorSet();
                                        animSetXYB.playTogether(animXB, animYB);
                                        animSetXYB.setDuration(300);
                                        animSetXYB.start();

                                    }
                                }

                            }

                        } else {

                            ObjectAnimator animX = ObjectAnimator.ofFloat(ivCells[ship.getI()+1][ship.getJ()], "x", v.getX());
                            ObjectAnimator animY = ObjectAnimator.ofFloat(ivCells[ship.getI()+1][ship.getJ()], "y", event.getY() - dx);
                            AnimatorSet animSetXY = new AnimatorSet();
                            animSetXY.playTogether(animX, animY);
                            animSetXY.setDuration(300);
                            animSetXY.start();

                            ObjectAnimator animXb = ObjectAnimator.ofFloat(v, "x", v.getX());
                            ObjectAnimator animYb = ObjectAnimator.ofFloat(v, "y", event.getY() - dx);
                            AnimatorSet animSetXYb = new AnimatorSet();
                            animSetXY.playTogether(animXb, animYb);
                            animSetXY.setDuration(300);
                            animSetXY.start();


                            if (v.getY() <= -50){

                                ivCells[3][3].setImageDrawable(drawCell[ship.getRajzID()]);
                                ivCells[4][3].setImageDrawable(drawCell[ship.getRajzID()+1]);
                                action = MotionEvent.ACTION_UP;
                                break;
                            }

                        }

                        break;
                    case (MotionEvent.ACTION_UP) :
                        System.out.println("enough is enough");
                        break;

                    case (MotionEvent.ACTION_CANCEL) :

                        System.out.println("Action cancel");
                        break;

                    case (MotionEvent.ACTION_OUTSIDE) :

                        System.out.println("Action outside");
                        break;

                }
                return true;

            }
        });
    }

    final private class Ship{

        private int id = 0;
        private int i = 0,j =0;
        private float x = 0;
        private float y = 0;
        private int shipmerete = 0;
        private int secondShipID = 0;
        private int thirdShipID = 0;
        private int positionOfShip = 0;
        private int rajzID = 0;
        private int direction = 0;

        //hajomerete 1,2,3
        // a positionOfShip pedig, 1 = bal/eleje, 2 közepe, 3 jobb/vége,
        // int i & j a tömbbeli elhelyezkedésük
        // rajzID = drawable rajzból

        //egyes hajó konstruktora
        public Ship(int id, int i, int j, int shipmerete, int rajzID, int direction) {

            this.id = id;
            this.i = i;
            this.j = j;
            this.shipmerete = shipmerete;
            this.rajzID = rajzID;
            this.direction = direction;

            this.x = (ScreenWidth()/maxN)*j;
            this.y = (ScreenWidth()/maxN)*i + 175;
            ivCells[this.i][this.j].setImageDrawable(drawCell[this.rajzID]);
            listenOnDrag(ivCells[this.i][this.j],this);
        }

        //kettes hajó konstruktora
        public Ship(int id, int i, int j, int shipmerete, int secondShipID, int positionOfShip, int rajzID, int direction) {

            this.id = id;
            this.i = i;
            this.j = j;
            this.shipmerete = shipmerete;
            this.secondShipID = secondShipID;
            this.positionOfShip = positionOfShip;
            this.rajzID = rajzID;
            this.direction = direction;

            this.x = (ScreenWidth()/maxN)*j;
            this.y = (ScreenWidth()/maxN)*i + 175;
            ivCells[this.i][this.j].setImageDrawable(drawCell[this.rajzID]);
            listenOnDrag(ivCells[this.i][this.j],this);
        }

        //hármas hajó konstruktora
        public Ship(int id, int i, int j, int shipmerete, int secondShipID, int thirdShipID, int positionOfShip, int rajzID, int direction) {

            this.id = id;
            this.shipmerete = shipmerete;
            this.positionOfShip = positionOfShip;
            this.secondShipID = secondShipID;
            this.thirdShipID = thirdShipID;
            this.positionOfShip = positionOfShip;
            this.rajzID = rajzID;
            this.direction = direction;

            this.x = (ScreenWidth()/maxN)*j;
            this.y = (ScreenWidth()/maxN)*i + 175;
            ivCells[this.i][this.j].setImageDrawable(drawCell[this.rajzID]);
            listenOnDrag(ivCells[this.i][this.j],this);
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public int getI() {
            return i;
        }

        public int getJ() {
            return j;
        }

        public int getId() { return id; }

        public int getShipmerete() { return shipmerete; }

        public int getSecondShipID() { return secondShipID; }

        public int getThirdShipID() { return thirdShipID; }

        public int getPositionOfShip() { return positionOfShip; }

        public int getRajzID() { return rajzID; }

        public int getDirection() { return direction; }

        @Override
        public String toString() {
            return "Ship{" +
                    "id=" + id +
                    ", i=" + i +
                    ", j=" + j +
                    ", shipmerete=" + shipmerete +
                    ", rajzID=" + rajzID +
                    '}';
        }
    }

    //beállítjuk a cellákra a rajzolást (a hajókat)
    private void setShips() {

        //hajó
        ivCells[3][3].setImageDrawable(drawCell[4]);
        ivCells[3][4].setImageDrawable(drawCell[2]);

        //hajó
        ivCells[1][2].setImageDrawable(drawCell[5]);
        ivCells[2][2].setImageDrawable(drawCell[6]);

        //wood
        ivCells[4][3].setImageDrawable(drawCell[7]);
        ivCells[5][3].setImageDrawable(drawCell[8]);

        //done
        ivCells[2][1].setImageDrawable(drawCell[3]);

    }

    //beállítjuk a rajzokat, hogy mi legyen kirajzolva a setShipsben
    private void loadResources() {
        drawCell[0] = null;
        drawCell[1] = context.getResources().getDrawable(R.drawable.cell_bg);

        drawCell[2] = context.getResources().getDrawable(R.drawable.kettes_horiz_1st);
        drawCell[3] = context.getResources().getDrawable(R.drawable.one_size_ship);
        drawCell[4] = context.getResources().getDrawable(R.drawable.kettes_horiz_2nd);

        drawCell[5] = context.getResources().getDrawable(R.drawable.kettes_vert_1st);
        drawCell[6] = context.getResources().getDrawable(R.drawable.kettes_vert_2nd);

        drawCell[7] = context.getResources().getDrawable(R.drawable.wood_1st);
        drawCell[8] = context.getResources().getDrawable(R.drawable.wood_2nd);

    }

    //....itt történik a csoda....
    @SuppressLint("NewApi")
    private void designTabla() {

        for (int i = 0 ; i < maxN ; i++){
            for (int j = 0; j < maxN; j++){
                ivCells[i][j] = new ImageView(context);
                ivCells[i][j].setBackground(drawCell[0]);
                //ivCells[i][j].bringToFront();
            }
        }
        /* működik !!! */
        //setShips();

        //makeShipsDragable();

        //a kép szélességét elosztjuk ahányszorosra akarjuk felosztani, mi esetünkben 6x6 pályát akarunk, így 6 fele, azaz 120pixel lesz egy cella

        int sizeofCells = Math.round(ScreenWidth() / maxN);
        //létrehozunk egy Sort lényegében, ami 120x6 hosszúságú és 120pixel magasságú
        LinearLayout.LayoutParams lpRow = new LinearLayout.LayoutParams(sizeofCells * maxN, sizeofCells);
        //4pixelt leszedünk a cellákból, 'margó' gyanánt
        LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(sizeofCells - 4, sizeofCells - 4);

        //BoardGame változóba kimentjük az XMLból a LinearLayout objektumot
        LinearLayout BoardGame = (LinearLayout) findViewById(R.id.BoardGame);

        //sor
        for (int i = 0; i < maxN; i++) {
            //létrehozzuk a linRowot és átadjuk neki az összes 'resourcet' amiből eddig áll az activity
            LinearLayout linRow = new LinearLayout(context);
            //oszlop
            for (int j = 0; j < maxN; j++) {
                //minden cella elemén átmegyünk és létrehozzuk őket kép 'tárolóként'
                //ivCells[i][j] = new ImageView(context);
                //beállítjuk minden cella elemére a hátteret, a háttér képe a drawCell utolsó elemében van tárolva
                //ivCells[i][j].setBackground(drawCell[1]);
                //how to check if ship or not?
                //megnézzük hajóe, ha igen, nem állítunk margót neki, hogy a 2 kép ne álljon külön egymást
                boolean temp = false;
                //ivCells[i][j].getDrawable() != null
                if (temp) {
                    //lpCellből már levan vágva 4,4 pixel, de a hajóból nem akarjuk, hogy lelegyen vágva, így új változó
                    LinearLayout.LayoutParams lpCellforShips = new LinearLayout.LayoutParams(sizeofCells, sizeofCells);
                    linRow.addView(ivCells[i][j], lpCellforShips);
                } else {
                    //ha nem hajó, akkor a margó mehet, hogy ne legyen egybe egy csírkét...
                    lpCell.setMargins(2, 2, 2, 2);
                    linRow.addView(ivCells[i][j], lpCell);
                }
            }
            //soronként hozzáadjuk a BoardGamehez
            BoardGame.addView(linRow, lpRow);

        }
    }

    @SuppressLint("NewApi")
    private void makeBackGround(){

        //a kép szélességét elosztjuk ahányszorosra akarjuk felosztani, mi esetünkben 6x6 pályát akarunk, így 6 fele, azaz 120pixel lesz egy cella
        int sizeofCells = Math.round(ScreenWidth() / maxN);
        //létrehozunk egy Sort lényegében, ami 120x6 hosszúságú és 120pixel magasságú
        LinearLayout.LayoutParams lpRow = new LinearLayout.LayoutParams(sizeofCells * maxN, sizeofCells);
        //4pixelt leszedünk a cellákból, 'margó' gyanánt
        LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(sizeofCells - 4, sizeofCells - 4);

        //BoardGame változóba kimentjük az XMLból a LinearLayout objektumot
        LinearLayout BackGround = (LinearLayout) findViewById(R.id.BackGround);
        //sor
        for (int i = 0; i < maxN; i++) {
            //létrehozzuk a linRowot és átadjuk neki az összes 'resourcet' amiből eddig áll az activity
            LinearLayout linRow = new LinearLayout(context);
            //oszlop
            for (int j = 0; j < maxN; j++) {
                bgCells[i][j] = new ImageView(context);
                bgCells[i][j].setBackground(drawCell[1]);
                lpCell.setMargins(2, 2, 2, 2);
                linRow.addView(bgCells[i][j], lpCell);
            }
            //soronként hozzáadjuk a BoardGamehez
            linRow.setOnDragListener(new MyDragListener());
            BackGround.addView(linRow, lpRow);
        }
    }

    class MyDragListener implements View.OnDragListener {
        //Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
        //Drawable normalShape = getResources().getDrawable(R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //System.out.println(temp.getY());
                    break;

                case DragEvent.ACTION_DRAG_LOCATION:
                    break;

                case DragEvent.ACTION_DRAG_ENTERED:

                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    //v.setBackgroundDrawable(normalShape);
                    break;

                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup

                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //v.setBackgroundDrawable(normalShape);

                default:
                    break;
            }
            return true;
        }
    }

    private int ScreenWidth() {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        System.out.println(context.getResources().getDisplayMetrics().density);
        return dm.widthPixels;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);


    }

}

//is it needed????
    /*private ArrayList<Ship> Ships(){
        ArrayList<Ship> ships = new ArrayList<>();
        for (int i = 0; i < maxN; i++){
            for (int j = 0; j < maxN; j++){
                if (ivCells[i][j].getDrawable() != null){
                    //ships.add(new Ship(i,j));
                }
            }
        }
        return ships;
    }*/

/*
                            ObjectAnimator animXb = ObjectAnimator.ofFloat(ivCells[3][4], "x", (event.getX() - dx)+120);
                            ObjectAnimator animYb = ObjectAnimator.ofFloat(ivCells[3][4], "y", ivCells[3][4].getY());
                            AnimatorSet animSetXYb = new AnimatorSet();
                            animSetXYb.playTogether(animXb, animYb);
                            animSetXYb.setDuration(300);
                            animSetXYb.start();*/


                            /*
                            distance = ivCells[3][3].getX() - ivCells[3][2].getX();
                            if (ivCells[3][4].getDrawable() != null) {
                                if (ivCells[3][3].getX() < ivCells[3][2].getX()) {
                                    ivCells[3][4].setImageDrawable(null);
                                    ivCells[3][3].setImageDrawable(drawCell[2]);
                                    ivCells[3][2].setImageDrawable(drawCell[4]);
                                    animSetXY.cancel();
                                    animSetXYb.cancel();
                                    break;
                                }
                            }
                            if (ivCells[3][2].getDrawable() != null && ivCells[3][3].getDrawable() != null){
                                System.out.println("Siker" + distance + " ...... " + v.getX());
                            } else {
                                System.out.println("not yet" + distance + " ...... " + v.getX() + " , " + ivCells[3][2].getX());
                            }
                            */



                        /*ObjectAnimator animX = ObjectAnimator.ofFloat(v, "x", event.getX() - dx);
                        ObjectAnimator animY = ObjectAnimator.ofFloat(v, "y", event.getY() - dy);
                        AnimatorSet animSetXY = new AnimatorSet();
                        animSetXY.playTogether(animX, animY);
                        animSetXY.setDuration(300);
                        animSetXY.start();*/

/*if(once == true){
        once = false;
        ArrayList<Cells> cells = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < maxN; i++) {
        for (int j = 0; j < maxN; j++) {

        cells.add(new Cells(counter, ivCells[i][j], ivCells[i][j].getX(), ivCells[i][j].getY()));
        counter++;
        }
        }
        }*/



/*
    private void makeShipsDragable(){

        for (int i = 0; i < maxN; i++){
            for (int j = 0; j < maxN; j++){
                if (ivCells[i][j].getDrawable() != null){
                    //ivCells[i][j].setOnTouchListener(this);
                    listenOnDrag(ivCells[i][j],null);
                }
            }
        }
    }*/



/*@Override
    public boolean onTouch(View v, MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);
        //event.getAction() & MotionEvent.ACTION_MASK

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :

                x = event.getX();
                y = event.getY();
                dx = x - v.getX();
                dy = y - v.getY();
                break;

            case (MotionEvent.ACTION_MOVE) :

                ObjectAnimator animX = ObjectAnimator.ofFloat(v, "x", event.getX() - dx);
                ObjectAnimator animY = ObjectAnimator.ofFloat(v, "y", event.getY() - dy);
                AnimatorSet animSetXY = new AnimatorSet();
                animSetXY.playTogether(animX, animY);
                animSetXY.setDuration(300);
                animSetXY.start();

                break;
            case (MotionEvent.ACTION_UP) :

                break;
            case (MotionEvent.ACTION_CANCEL) :
                System.out.println("Action cancel");
                break;
            case (MotionEvent.ACTION_OUTSIDE) :
                System.out.println("Action outside");
                break;

        }
        return true;
    }*/





//TextView teszt = (TextView) findViewById(R.id.tesztTextView);
//ivCells[5][3] ==
//teszt.setX((ScreenWidth()/maxN)*j);
//teszt.setY((ScreenWidth()/maxN)*i + 175);


/*
        ivCells[2][1].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        x = event.getX();
                        y = event.getY();
                        dx = x - ivCells[2][1].getX();
                        dy = y - ivCells[2][1].getY();
                    }
                    break;
                    case MotionEvent.ACTION_MOVE: {

                        test

                        ObjectAnimator animX = ObjectAnimator.ofFloat(ivCells[2][1], "x", event.getX() - dx);
                        ObjectAnimator animY = ObjectAnimator.ofFloat(ivCells[2][1], "y", event.getY() - dy);
                        AnimatorSet animSetXY = new AnimatorSet();
                        animSetXY.playTogether(animX, animY);
                        animSetXY.setDuration(300);
                        animSetXY.start();

                        ----
                        ivCells[2][1].setX(event.getX() - dx);
                        ivCells[2][1].setY(event.getY() - dy);
                    }
                    break;
                    case MotionEvent.ACTION_UP: {
                        //your stuff
                    }
                }
                return true;
            }
        });*/



/*
    @SuppressLint("NewApi")
    private void makeBackGround(){

        //a kép szélességét elosztjuk ahányszorosra akarjuk felosztani, mi esetünkben 6x6 pályát akarunk, így 6 fele, azaz 120pixel lesz egy cella
        int sizeofCells = Math.round(ScreenWidth() / maxN);
        //létrehozunk egy Sort lényegében, ami 120x6 hosszúságú és 120pixel magasságú
        LinearLayout.LayoutParams lpRow = new LinearLayout.LayoutParams(sizeofCells * maxN, sizeofCells);
        //4pixelt leszedünk a cellákból, 'margó' gyanánt
        LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(sizeofCells - 4, sizeofCells - 4);

        //BoardGame változóba kimentjük az XMLból a LinearLayout objektumot
        LinearLayout BoardGame = (LinearLayout) findViewById(R.id.BoardGame);
        //sor
        for (int i = 0; i < maxN; i++) {
            //létrehozzuk a linRowot és átadjuk neki az összes 'resourcet' amiből eddig áll az activity
            LinearLayout linRow = new LinearLayout(context);
            //oszlop
            for (int j = 0; j < maxN; j++) {
                ivCells[i][j] = new ImageView(context);
                ivCells[i][j].setBackground(drawCell[1]);
                lpCell.setMargins(2, 2, 2, 2);
                linRow.addView(ivCells[i][j], lpCell);
            }
            //soronként hozzáadjuk a BoardGamehez
            BoardGame.addView(linRow, lpRow);
        }

    }
*/