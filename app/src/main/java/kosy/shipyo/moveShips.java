package kosy.shipyo;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import kosy.shipyo.RealTimeGame.Ship;

/**
 * Created by Ákos on 2017. 05. 26..
 */

public class moveShips extends RealTimeGame{

    private View view ;
    private Ship ship;
    private ArrayList<Ship> ships;
    private int maxN = 6;
    private ImageView[][] ivCells = new ImageView[maxN][maxN];
    private Ship secondShip, thirdShip;

    public moveShips(View view, Ship ship, ImageView[][] ivCells, ArrayList<Ship> ships) {

        this.view = view;
        this.ship = ship;
        this.ivCells = ivCells;
        this.ships = ships;
        setNextToShips();

        if (ship.getDirection() == 0) {
            if (ship.getShipmerete() == 2) {
                if (ship.getPositionOfShip() == 1) {
                    if (!hasShip(ship.getI()-1,ship.getJ())) {
                        moveTopKettes();
                    }
                }
                else if (ship.getPositionOfShip()==2) {
                    if (!hasShip(ship.getI()+1,ship.getJ())) {
                        moveDownKettes();
                    }
                }
            }
            else if (ship.getShipmerete() == 3) {
                if (ship.getPositionOfShip() == 1){
                    moveTopHarmas();
                }
                else if (ship.getPositionOfShip() == 2){
                    ;
                }
                else if (ship.getPositionOfShip() == 3){
                    moveDownHarmas();
                }
            }
        }

        else if (ship.getDirection() == 1) {
            if (ship.getShipmerete() == 2) {
                if (ship.getPositionOfShip() == 1) {
                    if (!hasShip(ship.getI(),ship.getJ()-1)) {
                        moveLeftKettes();
                    }
                }
                else if (ship.getPositionOfShip()==2) {
                    if (!hasShip(ship.getI(),ship.getJ()+1)) {
                        moveRightKettes();
                    }
                }
            }
            else if (ship.getShipmerete() == 3) {
                if (ship.getPositionOfShip() == 1){
                    if (!hasShip(ship.getI(),ship.getJ()-1)) {
                        moveLeftHarmas();
                    }
                }
                else if (ship.getPositionOfShip() == 2){
                    ;
                }
                else if (ship.getPositionOfShip() == 3){
                    if (!hasShip(ship.getI(),ship.getJ()+1)) {
                        moveRightHarmas();
                    }
                }
            }
        }
    }

    public ImageView[][] getCells(){
        return this.ivCells;
    }

    public Ship getFirstShip(){
        return this.ship;
    }

    public Ship getSecondaryShip(){
        return secondShip;
    }

    public Ship getThirdShip(){
        return thirdShip;
    }

    private boolean hasShip(int i, int j){

        for (Ship s : ships){
            if (s.getI() == i && s.getJ() == j){
                return true;
            }
        }

        return false;
    }

    private void setNextToShips(){

        Ship secondShip = null;
        Ship thirdShip = null;
        for (Ship s : ships) {

            if (s.getId() == ship.getSecondShipID() && !s.equals(ship)) {
                this.secondShip = s;
            }

            if (s.getId() == ship.getThirdShipID() && !s.equals(ship)) {
                this.thirdShip = s;
            }

        }

    }

    private void moveRightKettes(){
        if (ship.getJ() < 5 && ship.getJ() > 0) {

            ship.setJ(ship.getJ() + 1);
            ship.setRajzID(ship.getRajzID());
            secondShip.setJ(secondShip.getJ() + 1);
            secondShip.setRajzID(secondShip.getRajzID());

            ivCells[secondShip.getI()][secondShip.getJ() - 1].setImageDrawable(null);
            ivCells[secondShip.getI()][secondShip.getJ() - 1].setOnTouchListener(null);

        } else {

        }
    }

    private void moveLeftKettes(){

        if (ship.getJ() < 5 && ship.getJ() > 0) {
        ship.setJ(ship.getJ() - 1);
        ship.setRajzID(ship.getRajzID());
        secondShip.setJ(secondShip.getJ() - 1);
        secondShip.setRajzID(secondShip.getRajzID());

        ivCells[secondShip.getI()][secondShip.getJ() + 1].setImageDrawable(null);
        ivCells[secondShip.getI()][secondShip.getJ() + 1].setOnTouchListener(null);

        } else {

        }
    }

    private void moveTopKettes(){

        if (ship.getI() < 5 && ship.getI() > 0) {

            ship.setI(ship.getI() - 1);
            ship.setRajzID(ship.getRajzID());
            secondShip.setI(secondShip.getI() - 1);
            secondShip.setRajzID(secondShip.getRajzID());

            ivCells[secondShip.getI() + 1][secondShip.getJ()].setImageDrawable(null);
            ivCells[secondShip.getI() + 1][secondShip.getJ()].setOnTouchListener(null);
        } else {

        }
    }

    private void moveDownKettes(){
        if (ship.getI() < 5 && ship.getI() > 0) {

            ship.setI(ship.getI() + 1);
            ship.setRajzID(ship.getRajzID());
            secondShip.setI(secondShip.getI() + 1);
            secondShip.setRajzID(secondShip.getRajzID());

            ivCells[secondShip.getI() - 1][secondShip.getJ()].setImageDrawable(null);
            ivCells[secondShip.getI() - 1][secondShip.getJ()].setOnTouchListener(null);

        } else {

        }
    }

    private void moveRightHarmas(){
        if (ship.getJ() < 5 && ship.getJ() > 0) {
            ship.setJ(ship.getJ() + 1);
            ship.setRajzID(ship.getRajzID());
            secondShip.setJ(secondShip.getJ() + 1);
            secondShip.setRajzID(secondShip.getRajzID());
            thirdShip.setJ(thirdShip.getJ() + 1);
            thirdShip.setRajzID(thirdShip.getRajzID());

            ivCells[thirdShip.getI()][thirdShip.getJ() - 1].setImageDrawable(null);
            ivCells[thirdShip.getI()][thirdShip.getJ() - 1].setOnTouchListener(null);
        } else {

        }
    }

    private void moveLeftHarmas(){
        if (ship.getJ() < 5 && ship.getJ() > 0) {

            ship.setJ(ship.getJ() - 1);
            ship.setRajzID(ship.getRajzID());
            secondShip.setJ(secondShip.getJ() - 1);
            secondShip.setRajzID(secondShip.getRajzID());
            thirdShip.setJ(thirdShip.getJ() - 1);
            thirdShip.setRajzID(thirdShip.getRajzID());

            ivCells[thirdShip.getI()][thirdShip.getJ() + 1].setImageDrawable(null);
            ivCells[thirdShip.getI()][thirdShip.getJ() + 1].setOnTouchListener(null);
        } else {

        }
    }

    private void moveTopHarmas(){
        ;
    }

    private void moveDownHarmas(){
        ;
    }

}
