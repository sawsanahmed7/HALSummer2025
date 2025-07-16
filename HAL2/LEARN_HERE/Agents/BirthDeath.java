package LEARN_HERE.Agents;

import HAL.GridsAndAgents.AgentSQ2Dunstackable;
import HAL.Gui.GridWindow;
import HAL.GridsAndAgents.AgentGrid2D;
import HAL.Rand;
import HAL.Util;

import static HAL.Util.*;

/**
 * Created by Rafael on 9/5/2017.
 */

class Cell1 extends AgentSQ2Dunstackable<CAR_T> {

    int color;
    boolean mutated = false;
    int mutationLimit = 100;
    //double randomValue
    public void Step() {
        if (mutated) {
            if (G.rn.Double() < (G.DEATH_PROB/1000)) {
                Dispose();
                return;
            }
            if (G.rn.Double() < G.BIRTH_PROB) {
                int nOptions = G.MapEmptyHood(G.mooreHood, Xsq(), Ysq());
                if (nOptions > 0) {
                    G.NewAgentSQ(G.mooreHood[G.rn.Int(nOptions)]).color = this.color;
                }
                return;
            }
        } else {
            if (G.rn.Double() < G.DEATH_PROB) {
                Dispose();
                return;
            }
            if (G.rn.Double() < G.BIRTH_PROB) {
                int nOptions = G.MapEmptyHood(G.mooreHood, Xsq(), Ysq());
                if (nOptions > 0) {

                   Cell3 c = G.NewAgentSQ(G.mooreHood[G.rn.Int(nOptions)]);

                }

            }

        }
    }
}

public class BirthDeath extends AgentGrid2D<Cell3> {
    int BLACK=RGB(0,0,0);
    double DEATH_PROB;
    double BIRTH_PROB;
    Rand rn=new Rand();
    int[]mooreHood=MooreHood(false);
    int color;
    public BirthDeath(int x, int y,int color) {
        super(x, y, Cell3.class);
        this.color=color;
    }
    public void Setup(double rad){
        int[]coords= CircleHood(true,rad);
        int nCoords= MapHood(coords,xDim/2,yDim/2);
        for (int i = 0; i < nCoords ; i++) {
            NewAgentSQ(coords[i]).color=color;
        }
    }
    public void setDeathRate(double deathRateInput) {
        this.DEATH_PROB=deathRateInput;
    }
    public void setBirthRate(double birthRateInput) {
        this.BIRTH_PROB=birthRateInput;
    }
    public void Step() {
        for (Cell3 c : this) {
            c.Step();
        }
        CleanAgents();
        ShuffleAgents(rn);
    }
    public void Draw(GridWindow vis){
        for (int i = 0; i < vis.length; i++) {
            Cell3 c = GetAgent(i);
            vis.SetPix(i, c == null ? BLACK : c.color);
        }
    }

    public static void main(String[] args) {
        CAR_T t=new CAR_T(100,100, Util.CYAN);
        GridWindow win=new GridWindow(100,100,10);
        t.Setup(10);

        t.setBirthRate(0.2);
        t.setDeathRate(0.02);
        System.out.print("initial birth rate: ");
        System.out.println(t.BIRTH_PROB);
        System.out.print("initial death rate: ");
        System.out.println(t.DEATH_PROB);

        for (int i = 0; i < 50; i++) {
            win.TickPause(200);
            t.Step();
            t.Draw(win);
        }

        t.setBirthRate(0.1);
        t.setDeathRate(0.15);
        System.out.print("new birth rate: ");
        System.out.println(t.BIRTH_PROB);
        System.out.print("new death rate: ");
        System.out.println(t.DEATH_PROB);

        for (int i = 0; i < 100000; i++) {
            win.TickPause(200);
            t.Step();
            t.Draw(win);
        }
    }
}
