package kosy.shipyo;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class RealTimeGame extends Activity {



    private int maxN = 6;
    private Drawable[] drawCell = new Drawable[9];
    private ImageView[][] ivCells = new ImageView[maxN][maxN];
    private float temp = 0;
    private int lepesszam = 0;
    Context context = this;
    float x = 0,y = 0, dx = 0 ,dy = 0;
    private ArrayList<Ship> ships = new ArrayList<>();
    private ArrayList<JSONObject> tomakeJsonObjects = new ArrayList<JSONObject>();
    TextView score;
    private String username;
    private String password;

    private ArrayList<String> atadott = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rt_game);
        Bundle extras = getIntent().getExtras();
        String[] passedAccounts = new String[2];
        if(extras !=null) {
            passedAccounts = extras.getStringArray("account");
        }
        username = passedAccounts[0];
        password = passedAccounts[1];

        setIvCells();
        //ships = makeJsonObject();
        score = (TextView) findViewById(R.id.scoreID);


        for (Ship s : ships){
            //System.out.println("hajo : " + s.getId() + " : " + s.getRajzID());
        }

        for (int i = 0; i < 6; i++){
            //ivCells[i][0].setBackgroundDrawable(null);
        }

        Button temp_Button = (Button) findViewById(R.id.downloadMapBT);
        temp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireYourAsyncTask();
                setShipsFromJson();
            }
        });
    }

    public void setShipsFromJson(){
        int namecounter = 0;
        try {
            for (JSONObject withPost : tomakeJsonObjects) {
                JSONObject onebyone = withPost.getJSONObject("post");
                String ruEmpty = onebyone.get("thirdShipId").toString();
                if (ruEmpty.equals("null")){
                    String shipname = "ship" + ++namecounter;
                    Ship ship = new Ship(onebyone.getInt("id"), onebyone.getInt("i"), onebyone.getInt("j"), onebyone.getInt("shipmerete"), onebyone.getInt("secondShipId"), onebyone.getInt("positionOfShip"), onebyone.getInt("rajzID"), onebyone.getInt("direction"));
                    ships.add(ship);
                } else {
                    String shipname = "ship" + ++namecounter;
                    Ship ship = new Ship(onebyone.getInt("id"), onebyone.getInt("i"), onebyone.getInt("j"), onebyone.getInt("shipmerete"), onebyone.getInt("secondShipId"), onebyone.getInt("thirdShipId"), onebyone.getInt("positionOfShip"), onebyone.getInt("rajzID"), onebyone.getInt("direction"));
                    //ship.setListenOnDrags();
                    ships.add(ship);
                }
            }
        } catch (JSONException ex){
            System.out.println(ex);
        }
    }

    public void setList(ArrayList<JSONObject> list) {
        this.tomakeJsonObjects = list;
    }

    private void fireYourAsyncTask() {
        String url = "http://kosysite.hol.es/webservices.php";
        new jsonArrayGet(this).execute(url);
    }

    public class Ship {

        private int maxN = 6;
        private Context context;
        private int id = 0;
        private int i = 0, j = 0;
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

            //this.x = (ScreenWidth() / maxN) * j;
            //this.y = (ScreenWidth() / maxN) * i + 175;

            ivCells[this.i][this.j].setImageResource(rajzID);
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

            this.x = (ScreenWidth() / maxN) * j;
            this.y = (ScreenWidth() / maxN) * i + 175;
            ivCells[this.i][this.j].setImageResource(rajzID);
            listenOnDrag(ivCells[this.i][this.j],this);
        }

        //hármas hajó konstruktora
        public Ship(int id, int i, int j, int shipmerete, int secondShipID, int thirdShipID, int positionOfShip, int rajzID, int direction) {

            this.id = id;
            this.i = i;
            this.j = j;
            this.shipmerete = shipmerete;
            this.positionOfShip = positionOfShip;
            this.secondShipID = secondShipID;
            this.thirdShipID = thirdShipID;
            this.positionOfShip = positionOfShip;
            this.rajzID = rajzID;
            this.direction = direction;

            //this.x = (ScreenWidth() / maxN) * j;
            //this.y = (ScreenWidth() / maxN) * i + 175;
            ivCells[this.i][this.j].setImageResource(rajzID);
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

        public int getId() {
            return id;
        }

        public int getShipmerete() {
            return shipmerete;
        }

        public int getSecondShipID() {
            return secondShipID;
        }

        public int getThirdShipID() {
            return thirdShipID;
        }

        public int getPositionOfShip() {
            return positionOfShip;
        }

        public int getRajzID() {
            return rajzID;
        }

        public int getDirection() {
            return direction;
        }

        public void setI(int i) {
            this.i = i;
        }

        public void setJ(int j) {
            this.j = j;
        }

        public void setRajzID(int rajzID) {
            this.rajzID = rajzID;
            ivCells[this.i][this.j].setImageResource(this.rajzID);
        }

        public void setListenOnDrags(){
            listenOnDrag(ivCells[this.i][this.j],this);
        }

        @Override
        public String toString() {
            return "Ship{" +
                    "maxN=" + maxN +
                    ", context=" + context +
                    ", id=" + id +
                    ", i=" + i +
                    ", j=" + j +
                    ", x=" + x +
                    ", y=" + y +
                    ", shipmerete=" + shipmerete +
                    ", secondShipID=" + secondShipID +
                    ", thirdShipID=" + thirdShipID +
                    ", positionOfShip=" + positionOfShip +
                    ", rajzID=" + rajzID +
                    ", direction=" + direction +
                    '}';
        }
    }

    private ArrayList makeJsonObject(){
        JSONObject kishajo = new JSONObject();
        JSONObject JSkettesA1 = new JSONObject();
        JSONObject JSkettesA2 = new JSONObject();
        JSONObject JSkettesB1 = new JSONObject();
        JSONObject JSkettesB2 = new JSONObject();
        JSONObject JSwood1 = new JSONObject();
        JSONObject JSwood2 = new JSONObject();

        JSONObject JSharmasA1 = new JSONObject();
        JSONObject JSharmasA2 = new JSONObject();
        JSONObject JSharmasA3 = new JSONObject();

        JSONObject JSharmasB1 = new JSONObject();
        JSONObject JSharmasB2 = new JSONObject();
        JSONObject JSharmasB3 = new JSONObject();

        //public Ship(int id, int i, int j, int shipmerete, Ship secondShipID, int positionOfShip, int rajzID)
        //public Ship(int id, int i, int j, int shipmerete, int secondShipID, int positionOfShip, int rajzID, int direction)
        //public Ship(int id, int i, int j, int shipmerete, int secondShipID, int thirdShipID, int positionOfShip, int rajzID, int direction)
        try {

            //kishajo.put("id","102");    kishajo.put("i",2);     kishajo.put("j",1);     kishajo.put("shipmerete",1);    kishajo.put("rajzID",R.drawable.one_size_ship); kishajo.put("direction",0);
            JSkettesA1.put("id","103"); JSkettesA1.put("i",3); JSkettesA1.put("j",3);   JSkettesA1.put("shipmerete",2); JSkettesA1.put("secondShipId",104); JSkettesA1.put("positionOfShip",1); JSkettesA1.put("rajzID",R.drawable.gabor_kicsihajo1_fekve); JSkettesA1.put("direction",1);
            JSkettesA2.put("id","104"); JSkettesA2.put("i",3); JSkettesA2.put("j",4);   JSkettesA2.put("shipmerete",2); JSkettesA2.put("secondShipId",103); JSkettesA2.put("positionOfShip",2); JSkettesA2.put("rajzID",R.drawable.gabor_kicsihajo2_fekve); JSkettesA2.put("direction",1);
            JSkettesB1.put("id","105"); JSkettesB1.put("i",1); JSkettesB1.put("j",2);   JSkettesB1.put("shipmerete",2); JSkettesB1.put("secondShipId",106); JSkettesB1.put("positionOfShip",1); JSkettesB1.put("rajzID",R.drawable.gabor_kicsihajo1); JSkettesB1.put("direction",0);
            JSkettesB2.put("id","106"); JSkettesB2.put("i",2); JSkettesB2.put("j",2);   JSkettesB2.put("shipmerete",2); JSkettesB2.put("secondShipId",105); JSkettesB2.put("positionOfShip",2); JSkettesB2.put("rajzID",R.drawable.gabor_kicsihajo2); JSkettesB2.put("direction",0);
            JSwood1.put("id","100");    JSwood1.put("i",4);     JSwood1.put("j",3);     JSwood1.put("shipmerete",2);    JSwood1.put("secondShipId",101); JSwood1.put("positionOfShip",1);       JSwood1.put("rajzID",R.drawable.wood_1st);    JSwood1.put("direction",0);
            JSwood2.put("id","101");    JSwood2.put("i",5);     JSwood2.put("j",3);     JSwood2.put("shipmerete",2);    JSwood2.put("secondShipId",100); JSwood2.put("positionOfShip",2);       JSwood2.put("rajzID",R.drawable.wood_2nd);    JSwood2.put("direction",0);

            JSharmasA1.put("id","110"); JSharmasA1.put("i",2); JSharmasA1.put("j",3);   JSharmasA1.put("shipmerete",3); JSharmasA1.put("secondShipId",111); JSharmasA1.put("thirdShipId",112); JSharmasA1.put("positionOfShip",1); JSharmasA1.put("rajzID",R.drawable.gabor_hajo_nagy_1_fekve); JSharmasA1.put("direction",1);
            JSharmasA2.put("id","111"); JSharmasA2.put("i",2); JSharmasA2.put("j",4);   JSharmasA2.put("shipmerete",3); JSharmasA2.put("secondShipId",110); JSharmasA2.put("thirdShipId",112); JSharmasA2.put("positionOfShip",2); JSharmasA2.put("rajzID",R.drawable.gabor_hajo_nagy_2_fekve); JSharmasA2.put("direction",1);
            JSharmasA3.put("id","112"); JSharmasA3.put("i",2); JSharmasA3.put("j",5);   JSharmasA3.put("shipmerete",3); JSharmasA3.put("secondShipId",111); JSharmasA3.put("thirdShipId",110); JSharmasA3.put("positionOfShip",3); JSharmasA3.put("rajzID",R.drawable.gabor_hajo_nagy_3_fekve); JSharmasA3.put("direction",1);

            JSharmasB1.put("id","113"); JSharmasB1.put("i",1); JSharmasB1.put("j",3);   JSharmasB1.put("shipmerete",3); JSharmasB1.put("secondShipId",114); JSharmasB1.put("thirdShipId",115); JSharmasB1.put("positionOfShip",1); JSharmasB1.put("rajzID",R.drawable.gabor_hajo_nagy_1_fekve); JSharmasB1.put("direction",1);
            JSharmasB2.put("id","114"); JSharmasB2.put("i",1); JSharmasB2.put("j",4);   JSharmasB2.put("shipmerete",3); JSharmasB2.put("secondShipId",115); JSharmasB2.put("thirdShipId",115); JSharmasB2.put("positionOfShip",2); JSharmasB2.put("rajzID",R.drawable.gabor_hajo_nagy_2_fekve); JSharmasB2.put("direction",1);
            JSharmasB3.put("id","115"); JSharmasB3.put("i",1); JSharmasB3.put("j",5);   JSharmasB3.put("shipmerete",3); JSharmasB3.put("secondShipId",114); JSharmasB3.put("thirdShipId",113); JSharmasB3.put("positionOfShip",3); JSharmasB3.put("rajzID",R.drawable.gabor_hajo_nagy_3_fekve); JSharmasB3.put("direction",1);

        } catch (Exception ex){
            try {
                throw ex;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //System.out.println(kishajo.length());
        //JSkettesA1.put("secondShipId",104); JSkettesA1.put("positionOfShip",1);
        ArrayList<Ship> internal_ships = new ArrayList<>();
        try {
            //Ship csonak =   new Ship(kishajo.getInt("id"),      kishajo.getInt("i"),    kishajo.getInt("j"),    kishajo.getInt("shipmerete"),   kishajo.getInt("rajzID"), kishajo.getInt("direction"));
            //internal_ships.add(csonak);
            Ship kettesA1 = new Ship(JSkettesA1.getInt("id"),   JSkettesA1.getInt("i"), JSkettesA1.getInt("j"), JSkettesA1.getInt("shipmerete"),JSkettesA1.getInt("secondShipId"),  JSkettesA1.getInt("positionOfShip"),JSkettesA1.getInt("rajzID"), JSkettesA1.getInt("direction"));
            internal_ships.add(kettesA1);
            Ship kettesA2 = new Ship(JSkettesA2.getInt("id"),   JSkettesA2.getInt("i"), JSkettesA2.getInt("j"), JSkettesA2.getInt("shipmerete"),JSkettesA2.getInt("secondShipId"),  JSkettesA2.getInt("positionOfShip"),JSkettesA2.getInt("rajzID"), JSkettesA2.getInt("direction"));
            internal_ships.add(kettesA2);
            Ship kettesB1 = new Ship(JSkettesB1.getInt("id"),   JSkettesB1.getInt("i"), JSkettesB1.getInt("j"), JSkettesB1.getInt("shipmerete"),JSkettesB1.getInt("secondShipId"),  JSkettesB1.getInt("positionOfShip"),JSkettesB1.getInt("rajzID"), JSkettesB1.getInt("direction"));
            internal_ships.add(kettesB1);
            Ship kettesB2 = new Ship(JSkettesB2.getInt("id"),   JSkettesB2.getInt("i"), JSkettesB2.getInt("j"), JSkettesB2.getInt("shipmerete"),JSkettesB2.getInt("secondShipId"),  JSkettesB2.getInt("positionOfShip"),JSkettesB2.getInt("rajzID"), JSkettesB2.getInt("direction"));
            internal_ships.add(kettesB2);
            Ship wood1 =    new Ship(JSwood1.getInt("id"),      JSwood1.getInt("i"),    JSwood1.getInt("j"),    JSwood1.getInt("shipmerete"),   JSwood1.getInt("secondShipId"),     JSwood1.getInt("positionOfShip"),JSwood1.getInt("rajzID"), JSwood1.getInt("direction"));
            internal_ships.add(wood1);
            Ship wood2 =    new Ship(JSwood2.getInt("id"),      JSwood2.getInt("i"),    JSwood2.getInt("j"),    JSwood2.getInt("shipmerete"),   JSwood2.getInt("secondShipId"),     JSwood2.getInt("positionOfShip"),JSwood2.getInt("rajzID"), JSwood2.getInt("direction"));
            internal_ships.add(wood2);

            Ship harmasA1 = new Ship(JSharmasA1.getInt("id"),   JSharmasA1.getInt("i"), JSharmasA1.getInt("j"), JSharmasA1.getInt("shipmerete"),JSharmasA1.getInt("secondShipId"),  JSharmasA1.getInt("thirdShipId"), JSharmasA1.getInt("positionOfShip"),JSharmasA1.getInt("rajzID"), JSharmasA1.getInt("direction"));
            internal_ships.add(harmasA1);
            Ship harmasA2 = new Ship(JSharmasA2.getInt("id"),   JSharmasA2.getInt("i"), JSharmasA2.getInt("j"), JSharmasA2.getInt("shipmerete"),JSharmasA2.getInt("secondShipId"),  JSharmasA2.getInt("thirdShipId"), JSharmasA2.getInt("positionOfShip"),JSharmasA2.getInt("rajzID"), JSharmasA2.getInt("direction"));
            internal_ships.add(harmasA2);
            Ship harmasA3 = new Ship(JSharmasA3.getInt("id"),   JSharmasA3.getInt("i"), JSharmasA3.getInt("j"), JSharmasA3.getInt("shipmerete"),JSharmasA3.getInt("secondShipId"),  JSharmasA3.getInt("thirdShipId"), JSharmasA3.getInt("positionOfShip"),JSharmasA3.getInt("rajzID"), JSharmasA3.getInt("direction"));
            internal_ships.add(harmasA3);
            Ship harmasB1 = new Ship(JSharmasB1.getInt("id"),   JSharmasB1.getInt("i"), JSharmasB1.getInt("j"), JSharmasB1.getInt("shipmerete"),JSharmasB1.getInt("secondShipId"),  JSharmasB1.getInt("thirdShipId"), JSharmasB1.getInt("positionOfShip"),JSharmasB1.getInt("rajzID"), JSharmasB1.getInt("direction"));
            internal_ships.add(harmasB1);
            Ship harmasB2 = new Ship(JSharmasB2.getInt("id"),   JSharmasB2.getInt("i"), JSharmasB2.getInt("j"), JSharmasB2.getInt("shipmerete"),JSharmasB2.getInt("secondShipId"),  JSharmasB2.getInt("thirdShipId"), JSharmasB2.getInt("positionOfShip"),JSharmasB2.getInt("rajzID"), JSharmasB2.getInt("direction"));
            internal_ships.add(harmasB2);
            Ship harmasB3 = new Ship(JSharmasB3.getInt("id"),   JSharmasB3.getInt("i"), JSharmasB3.getInt("j"), JSharmasB3.getInt("shipmerete"),JSharmasB3.getInt("secondShipId"),  JSharmasB3.getInt("thirdShipId"), JSharmasB3.getInt("positionOfShip"),JSharmasB3.getInt("rajzID"), JSharmasB3.getInt("direction"));
            internal_ships.add(harmasB3);

        } catch (Exception ex){
            try {
                throw ex;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return internal_ships;
    }

    public void setIvCells() {
        int counter = 0;
        for (int i = 0; i < maxN; i++) {
            for (int j = 0; j < maxN; j++) {
                counter++;
                String ivID = "IV" + counter;
                int resourceID = this.getResources().getIdentifier(ivID, "id", getPackageName());
                ivCells[j][i] = (ImageView) findViewById(resourceID);
                ivCells[j][i].setBackgroundResource(R.drawable.cell_top_border);
            }
        }



    }

    private int ScreenWidth() {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    private boolean hasShip(int i, int j){

        for (Ship s : ships){
            if (s.getI() == i && s.getJ() == j){
                //System.out.println(s.getId());
                return true;
            }
        }

        return false;
    }

    private void listenOnDrag(final View view, final Ship ship) {
        //System.out.println(ship.toString());
        view.setOnTouchListener(new View.OnTouchListener() {
        private boolean ignore = false;
        boolean pressFlag= false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            x = 0; y = 0; dx = 0; dy = 0;
            //ignore -> ne fusson tovább a kód, ha már húzom az imaget
            if(ignore && event.getAction()!=MotionEvent.ACTION_UP) {
                return false;
            }

            Ship firstShip = ship;
            Ship secondShip = null;
            Ship thirdShip = null;
            for (Ship s : ships) {

                if (s.getId() == ship.getSecondShipID() && !s.equals(ship)) {
                    secondShip = s;
                }

                if (s.getId() == ship.getThirdShipID() && !s.equals(ship)) {
                    thirdShip = s;
                }

            }

            int action = MotionEventCompat.getActionMasked(event);
            //event.getAction() & MotionEvent.ACTION_MASK
            switch(action) {
                case (MotionEvent.ACTION_DOWN) :

                    //System.out.println(v.getId() + " ... " + ivCells[ship.getI()][ship.getJ()].getId());

                    break;

                case (MotionEvent.ACTION_MOVE) :

                    if (pressFlag == false) {
                        lepesszam++;
                        score.setText("Moves: " +lepesszam);
                        if (ship.getId() == 100 && ship.getI()==0 && ship.getJ() == 3){

                            //Toast.makeText(getApplicationContext(), "NYERTÉL!", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(RealTimeGame.this);
                            if (lepesszam < HighScore()) {
                                dlgAlert.setMessage("You have won the game, congratulation! New HighScore:" + lepesszam);
                                String Slepesszam = String.valueOf(lepesszam);
                                try {
                                    new setScoretoDB().execute(username, password,Slepesszam).get();
                                } catch (Exception ex){
                                    System.out.println(ex);
                                }
                            } else {
                                dlgAlert.setMessage("You have won the game, congratulation! No new record set: " + lepesszam );
                            }
                            //dlgAlert.setTitle("App Title");
                            dlgAlert.setPositiveButton("Click to close the map",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            changeActivity();
                                        }
                                    });
                            dlgAlert.setCancelable(true);
                            dlgAlert.create().show();

                        }
                        moveShips test = new moveShips(v,ship,ivCells, ships);
                        Ship TfirstShip = test.getFirstShip();
                        ship.setI(TfirstShip.getI());
                        ship.setJ(TfirstShip.getJ());
                        Ship TsecondShip = test.getSecondaryShip();
                        secondShip.setI(TsecondShip.getI());
                        secondShip.setJ(TsecondShip.getJ());
                        if (thirdShip != null) {
                            Ship TthirdShip = test.getThirdShip();
                            thirdShip.setI(TthirdShip.getI());
                            thirdShip.setJ(TthirdShip.getJ());
                        }

                        pressFlag = true;
                    }
                    break;

                case (MotionEvent.ACTION_UP) :
                    ignore = false;
                    pressFlag = false;
                    ship.setListenOnDrags();
                    secondShip.setListenOnDrags();
                    if (thirdShip != null) {
                        thirdShip.setListenOnDrags();
                    }
                    break;

            }
            return true;

        }
        });
    }

    private int HighScore(){

        String result = "ures";
        try {

            result = new jsonGetScore().execute(username, password).get();
            //valami a végén ott van, gondolom \n és mindig elhalt miatta.
            result = result.substring(0,result.length()-1);

        }
        catch (Exception ex){ System.out.println("Authentication was not succesful. " + ex); }

        return Integer.parseInt(result);
    }

    private void changeActivity() {

        Intent intent = new Intent(this, loginPage.class);
        startActivity(intent);

    }

    public ImageView[][] getIvCells() {
        return ivCells;
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }
}

/*
    private void listenOnDrag(final View view, final Ship ship) {

        view.setOnTouchListener(new View.OnTouchListener() {
        private boolean ignore = false;
        boolean pressFlag= false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            x = 0; y = 0; dx = 0; dy = 0;
            //ignore -> ne fusson tovább a kód, ha már húzom az imaget
            if(ignore && event.getAction()!=MotionEvent.ACTION_UP) {
                return false;
            }


            Ship secondShip = null;
            Ship thirdShip = null;
            for (Ship s : ships) {

                if (s.getId() == ship.getSecondShipID() && !s.equals(ship)) {
                    secondShip = s;
                }

                if (s.getId() == ship.getThirdShipID() && !s.equals(ship)) {
                    thirdShip = s;
                }

            }

            moveShips moveShip = new moveShips(v,ship,secondShip,thirdShip);
            int action = MotionEventCompat.getActionMasked(event);
            //event.getAction() & MotionEvent.ACTION_MASK
            switch(action) {
                case (MotionEvent.ACTION_DOWN) :

                    //System.out.println(v.getId() + " ... " + ivCells[ship.getI()][ship.getJ()].getId());

                    break;

                case (MotionEvent.ACTION_MOVE) :

                    if (pressFlag == false) {
                        //ha 1, akkor függőlegesen mozgó hajó
                        if (ship.getDirection() == 0) {

                            if (ship.getShipmerete() == 1) {

                                if (event.getY() < 50) {


                                    ship.setI(ship.getI() - 1);
                                    ship.setRajzID(ship.getRajzID());
                                    ship.setListenOnDrags();
                                    ivCells[ship.getI() + 1][ship.getJ()].setImageResource(R.drawable.cell_bg);
                                    ivCells[ship.getI() + 1][ship.getJ()].setOnTouchListener(null);

                                    ignore = true;
                                    break;

                                } else if (event.getY() > 50) {
                                    //a hajót lefelé húzom

                                    ship.setI(ship.getI() + 1);
                                    ship.setRajzID(ship.getRajzID());
                                    ship.setListenOnDrags();
                                    ivCells[ship.getI() - 1][ship.getJ()].setImageResource(R.drawable.cell_bg);
                                    ivCells[ship.getI() - 1][ship.getJ()].setOnTouchListener(null);

                                    ignore = true;
                                    break;
                                }

                            } else if (ship.getShipmerete() == 2) {

                                if (ship.positionOfShip == 1) {

                                    if (!hasShip(ship.getI()-1,ship.getJ())) {

                                        if (ship.getI() < 5 && ship.getI() > 0) {

                                            ship.setI(ship.getI() - 1);
                                            ship.setRajzID(ship.getRajzID());
                                            secondShip.setI(secondShip.getI() - 1);
                                            secondShip.setRajzID(secondShip.getRajzID());

                                            ivCells[secondShip.getI() + 1][secondShip.getJ()].setImageDrawable(null);
                                            ivCells[secondShip.getI() + 1][secondShip.getJ()].setOnTouchListener(null);

                                            ignore = true;
                                            break;
                                        } else {
                                            if (ship.getId() == 100){
                                                Toast.makeText(getApplicationContext(), "NYERTÉL!!!!!!!!!!!!!!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();
                                            }
                                            Toast.makeText(getApplicationContext(), "lemész a pályáról haver", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "foglalt haver", Toast.LENGTH_LONG).show();
                                    }
                                } else if (ship.positionOfShip==2) {

                                    if (!hasShip(ship.getI()+1,ship.getJ())) {

                                        if (ship.getI() < 5 && ship.getI() > 0) {

                                            ship.setI(ship.getI() + 1);
                                            ship.setRajzID(ship.getRajzID());
                                            secondShip.setI(secondShip.getI() + 1);
                                            secondShip.setRajzID(secondShip.getRajzID());

                                            ivCells[secondShip.getI() - 1][secondShip.getJ()].setImageDrawable(null);
                                            ivCells[secondShip.getI() - 1][secondShip.getJ()].setOnTouchListener(null);

                                            ignore = true;
                                            break;
                                        } else {
                                            Toast.makeText(getApplicationContext(), "lemész a pályáról haver", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "foglalt haver", Toast.LENGTH_LONG).show();
                                    }
                                }


                            } else if (ship.getShipmerete() == 3) {

                                ;

                            }
                            // ha 0, akkor vízszíntesen mozog
                        } else if (ship.getDirection() == 1) {

                            if (ship.getShipmerete() == 1) {

                                if (event.getX() < 50) {

                                    ship.setI(ship.getJ() - 1);
                                    ship.setRajzID(ship.getRajzID());
                                    ship.setListenOnDrags();
                                    ivCells[ship.getI()][ship.getJ()+1].setImageResource(R.drawable.cell_bg);
                                    ivCells[ship.getI()][ship.getJ()+1].setOnTouchListener(null);

                                    ignore = true;
                                    break;

                                } else if (event.getX() > 50) {
                                    //a hajót lefelé húzom

                                    ship.setI(ship.getJ() + 1);
                                    ship.setRajzID(ship.getRajzID());
                                    ship.setListenOnDrags();
                                    ivCells[ship.getI()][ship.getJ()-1].setImageResource(R.drawable.cell_bg);
                                    ivCells[ship.getI()][ship.getJ()-1].setOnTouchListener(null);

                                    ignore = true;
                                    break;
                                }
                            } else if (ship.getShipmerete() == 2) {

                                if (ship.positionOfShip == 1) {

                                    if (!hasShip(ship.getI(),ship.getJ()-1)) {

                                        if (ship.getJ() < 5 && ship.getJ() > 0) {

                                            ship.setJ(ship.getJ() - 1);
                                            ship.setRajzID(ship.getRajzID());
                                            secondShip.setJ(secondShip.getJ() - 1);
                                            secondShip.setRajzID(secondShip.getRajzID());

                                            ivCells[secondShip.getI()][secondShip.getJ() + 1].setImageDrawable(null);
                                            ivCells[secondShip.getI()][secondShip.getJ() + 1].setOnTouchListener(null);

                                            ignore = true;
                                            break;
                                        } else {
                                            Toast.makeText(getApplicationContext(), "lemész a pályáról haver", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "foglalt haver", Toast.LENGTH_LONG).show();
                                    }

                                } else if (ship.positionOfShip==2) {
                                    if (!hasShip(ship.getI(),ship.getJ()+1)) {

                                        if (ship.getJ() < 5 && ship.getJ() > 0) {

                                            ship.setJ(ship.getJ() + 1);
                                            ship.setRajzID(ship.getRajzID());
                                            secondShip.setJ(secondShip.getJ() + 1);
                                            secondShip.setRajzID(secondShip.getRajzID());

                                            ivCells[secondShip.getI()][secondShip.getJ() - 1].setImageDrawable(null);
                                            ivCells[secondShip.getI()][secondShip.getJ() - 1].setOnTouchListener(null);

                                            ignore = true;
                                            break;
                                        } else {
                                            Toast.makeText(getApplicationContext(), "lemész a pályáról haver", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "foglalt haver", Toast.LENGTH_LONG).show();
                                    }
                                }


                            } else if (ship.getShipmerete() == 3) {

                                System.out.println(ship.getPositionOfShip());

                                if (ship.positionOfShip == 1) {

                                    if (!hasShip(ship.getI(),ship.getJ()-1)) {

                                        if (ship.getJ() < 5 && ship.getJ() > 0) {

                                            System.out.println(ship.getId() + "  :  " + secondShip.getId() + "  :  " + thirdShip.getId());
                                            ship.setJ(ship.getJ() - 1);
                                            ship.setRajzID(ship.getRajzID());
                                            secondShip.setJ(secondShip.getJ() - 1);
                                            secondShip.setRajzID(secondShip.getRajzID());
                                            thirdShip.setJ(thirdShip.getJ() - 1);
                                            thirdShip.setRajzID(thirdShip.getRajzID());

                                            ivCells[thirdShip.getI()][thirdShip.getJ() + 1].setImageDrawable(null);
                                            System.out.println(ship.getJ() + " .. i .. " + thirdShip.getI());
                                            System.out.println(ship.getJ() + " .. i .. " + thirdShip.getJ());
                                            ivCells[thirdShip.getI()][thirdShip.getJ() + 1].setOnTouchListener(null);

                                            ignore = true;
                                            break;
                                        } else {
                                            Toast.makeText(getApplicationContext(), "lemész a pályáról haver", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "foglalt haver", Toast.LENGTH_LONG).show();
                                    }

                                } else if (ship.positionOfShip==3) {
                                    if (!hasShip(ship.getI(),ship.getJ()+1)) {

                                        if (ship.getJ() < 5 && ship.getJ() > 0) {

                                            System.out.println(ship.getId() + "  :  " + secondShip.getId() + "  :  " + thirdShip.getId());
                                            ship.setJ(ship.getJ() + 1);
                                            ship.setRajzID(ship.getRajzID());
                                            secondShip.setJ(secondShip.getJ() + 1);
                                            secondShip.setRajzID(secondShip.getRajzID());
                                            thirdShip.setJ(thirdShip.getJ() + 1);
                                            thirdShip.setRajzID(thirdShip.getRajzID());

                                            ivCells[thirdShip.getI()][thirdShip.getJ() - 1].setImageDrawable(null);
                                            ivCells[thirdShip.getI()][thirdShip.getJ() - 1].setOnTouchListener(null);

                                            ignore = true;
                                            break;
                                        } else {
                                            Toast.makeText(getApplicationContext(), "lemész a pályáról haver", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "foglalt haver", Toast.LENGTH_LONG).show();
                                    }
                                }

                            }

                        }
                        pressFlag = true;
                    }
                    break;

                case (MotionEvent.ACTION_UP) :
                    ignore = false;
                    pressFlag = false;
                    ship.setListenOnDrags();
                    secondShip.setListenOnDrags();
                    if (thirdShip != null) {
                        thirdShip.setListenOnDrags();
                    }
                    break;

            }
            return true;

        }
        });
    }*/





/* if (event.getY() < 50) {
                                //v.setY(v.getY() - 10);

                                //a hajót felfelé húzom
                                //a mostani hajót, amit húzok, kirajzolja a felette lévő ImageViewre, ami az ivcells-ben van tárolva
                                //ivCells[ship.getI() - 1][ship.getJ()].setImageResource(ship.getRajzID());
                                ivCells[ship.getI() - 1][ship.getJ()].setImageDrawable(ivCells[ship.getI()][ship.getJ()].getDrawable());

                                //kirajzolja ImageView utáni (2részes hajó)-t a mostani ImageView helyére
                                ivCells[ship.getI()][ship.getJ()].setImageDrawable(ivCells[ship.getI() + 1][ship.getJ()].getDrawable());

                                //a hajó 2. része helyére üres ImageViewet tesz
                                ivCells[ship.getI() + 1][ship.getJ()].setImageResource(R.drawable.cell_bg);

                                ignore = true;

                                break;
                            } else if (event.getY() > 50) {
                                //a hajót lefelé húzom

                                ivCells[ship.getI() + 1][ship.getJ()].setImageDrawable(ivCells[ship.getI()][ship.getJ()].getDrawable());
                                ivCells[ship.getI()][ship.getJ()].setImageDrawable(ivCells[ship.getI() - 1][ship.getJ()].getDrawable());
                                ivCells[ship.getI() - 1][ship.getJ()].setImageResource(R.drawable.cell_bg);
                                ignore = true;

                                break;
                            }*/




























/*ivCells[3][3].setImageResource(R.drawable.kettes_horiz_2nd);
        ivCells[3][4].setImageResource(R.drawable.kettes_horiz_1st);

        //hajó
        ivCells[1][2].setImageResource(R.drawable.kettes_vert_1st);
        ivCells[2][2].setImageResource(R.drawable.kettes_vert_2nd);

        //wood
        ivCells[4][3].setImageResource(R.drawable.wood_1st);
        ivCells[5][3].setImageResource(R.drawable.wood_2nd);

        //done
        ivCells[2][1].setImageResource(R.drawable.one_size_ship);*/

/*
    private final class MyTouchListener implements OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {

            View temp = null;;

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                for (int i = 0; i < maxN; i++) {
                    for (int j = 0; j < maxN; j++) {
                        if (view == ivCells[i][j]){
                            temp = ivCells[i][j+1];
                        }
                    }
                }

                ClipData data = ClipData.newPlainText("", "");
                DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                DragShadowBuilder shadowBuilderB = new View.DragShadowBuilder(ivCells[4][5]);
                view.startDrag(data, shadowBuilder, view, 0);
                view.startDrag(data, shadowBuilderB, ivCells[4][5], 0);

                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }
*/


/*
    class MyDragListener implements OnDragListener {
        Drawable enterShape = getResources().getDrawable(
                R.drawable.shape_droptarget);
        Drawable normalShape = getResources().getDrawable(R.drawable.shape);


        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            View view = (View) event.getLocalState();

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
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
                    //System.out.println(v.getX() + " ... " + owner.getX());
                    //System.out.println(v.getY() + " ... " + owner.getY());

                    //TODO getX == y, getY == x .... :D
                    ViewGroup owner = (ViewGroup) view.getParent();
                    LinearLayout container = (LinearLayout) v;
                    if (v.getX() == owner.getX() && v.getY() != owner.getY()) {
                        owner.removeView(view);
                        ((LinearLayout) v).removeAllViewsInLayout();
                        container.addView(view);
                    } else {
                        //System.out.println(v.getX() + " .. " + owner.getX());
                    }
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
*/

/*public void setDragListeners() {
        findViewById(R.id.LL1).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL2).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL3).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL4).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL5).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL6).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL7).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL8).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL9).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL10).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL11).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL12).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL13).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL14).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL15).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL16).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL17).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL18).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL19).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL20).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL21).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL22).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL23).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL24).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL25).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL26).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL27).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL28).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL29).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL30).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL31).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL32).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL33).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL34).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL35).setOnDragListener(new MyDragListener());
        findViewById(R.id.LL36).setOnDragListener(new MyDragListener());

        findViewById(R.id.IV1).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV2).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV3).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV4).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV5).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV6).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV7).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV8).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV9).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV10).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV11).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV12).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV13).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV14).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV15).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV16).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV17).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV18).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV19).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV20).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV21).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV22).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV23).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV24).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV25).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV26).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV27).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV28).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV29).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV30).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV31).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV32).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV33).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV34).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV35).setOnTouchListener(new MyTouchListener());
        findViewById(R.id.IV36).setOnTouchListener(new MyTouchListener());
    }*/



