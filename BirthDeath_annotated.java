
// Import libraries
import HAL.GridsAndAgents.AgentSQ2Dunstackable;
import HAL.Gui.GridWindow;
import HAL.GridsAndAgents.AgentGrid2D;
import HAL.Rand;
import HAL.Util;


import static HAL.Util.*;

/**
 * Created by Rafael on 9/5/2017.
 */

// Create Cell class as an extension of AgentSQ2Dunstackable<BirthDeath>
class Cell extends AgentSQ2Dunstackable<BirthDeath> {
    // Create color variable
    int color;

    // Define Step function
    public void Step() {
        if (G.rn.Double() < G.DEATH_PROB) {
            Dispose();
            return;
        }
        if (G.rn.Double() < G.BIRTH_PROB) {
            int nOptions = G.MapEmptyHood(G.mooreHood, Xsq(), Ysq());
            if(nOptions>0) {
                G.NewAgentSQ(G.mooreHood[G.rn.Int(nOptions)]).color=color;
            }
        }
    }
}

// Create BirthDeath class as an extension of AgentGrid2D<Cell>
public class BirthDeath extends AgentGrid2D<Cell> {

    // Define variables
    int BLACK=RGB(0,0,0);
    double DEATH_PROB=0.01;
    double BIRTH_PROB=0.2;
                                                   
    Rand rn=new Rand();
    int[]mooreHood=MooreHood(false);

    // Define color variable
    int color;

    // Define BirthDeath function with x, y, and color parameters
    public BirthDeath(int x, int y,int color) {
        super(x, y, Cell.class);
        this.color=color;
    }


    // Define Setup function
    public void Setup(double rad){
        int[]coords= CircleHood(true,rad);
        int nCoords= MapHood(coords,xDim/2,yDim/2);
        for (int i = 0; i < nCoords ; i++) {
            NewAgentSQ(coords[i]).color=color;
        }
    }

    // Define Step function
    public void Step() { 
        for (Cell c : this) {
            c.Step();
        }
        CleanAgents();
        ShuffleAgents(rn);
    }

    // Define Draw function 
    public void Draw(GridWindow vis){
    
            // Repeat for vis.length
            for (int i = 0; i < vis.length; i++) {
            Cell c = GetAgent(i);
            vis.SetPix(i, c == null ? BLACK : c.color);
        }
    }
        
    // Define main function
    public static void main(String[] args) {
        BirthDeath t=new BirthDeath(100,100, Util.RED);
        GridWindow win=new GridWindow(100,100,10);
        t.Setup(10);
     
        // Repeat 100000 times
        for (int i = 0; i < 100000; i++) {
            win.TickPause(10);
            t.Step();
            t.Draw(win);
        }
    }
}
    
    
    

