package LEARN_HERE.Agents;

import HAL.GridsAndAgents.AgentSQ2Dunstackable;
import HAL.Gui.GridWindow;
import HAL.GridsAndAgents.AgentGrid2D;
import HAL.Rand;
import HAL.Util;
import java.io.*;
import java.util.Arrays;
import java.awt.Color;

import static HAL.Util.*;

/**
 * Created by Rafael on 9/5/2017.
 *
 * edited by me in 2025 :>
 */


class Cell2 extends AgentSQ2Dunstackable<CAR_T> {

    int color;
    int colorRef;
    public static String[] colorIndex = {"a", "b", "c", "d", "e"};
    boolean mutated = false;
    int mutationLimit = 100;





    //rColor = G.rn.Double();
    //gColor = G.rn.Double();
    //bColor = G.rn.Double();

    public void Step() {
        if (this.mutated) {
            if (G.rn.Double() < (G.DEATH_PROB/(0.75 + (G.rn.Double())))) {
                CAR_T.removeColor(this.colorRef);
                System.out.println("mutated [" + this.colorRef + "] cell died");
                Dispose();
                return;
            }
            if (G.rn.Double() < (G.BIRTH_PROB*(0.75 + (G.rn.Double())))) {
                int nOptions = G.MapEmptyHood(G.mooreHood, Xsq(), Ysq());
                if (nOptions > 0) {
                    Cell3 c1 = G.NewAgentSQ(G.mooreHood[G.rn.Int(nOptions)]);
                    c1.color = this.color;
                    c1.colorRef = this.colorRef;
                    c1.mutated = this.mutated;
                    CAR_T.addColor(this.colorRef);
                    System.out.println("mutated [" + this.colorRef + "] cell divided");

                }
                return;
            }
        } else {
            if (G.rn.Double() < G.DEATH_PROB) {
                CAR_T.nm -= 1;
                System.out.println("non-mutated cell died");
                Dispose();
                return;
            }
            if (G.rn.Double() < G.BIRTH_PROB) {
                int nOptions = G.MapEmptyHood(G.mooreHood, Xsq(), Ysq());
                if (nOptions > 0) {
                   if (G.rn.Int(mutationLimit) == 1) {

                       // mutate
                       this.mutated = true;
                       this.colorRef = G.rn.Int(5);
                       this.color = G.mutationColors[colorRef];

                       // replace non-mutated cell with mutated cell
                       CAR_T.addColor(colorRef);
                       CAR_T.nm -= 1;

                       // record new mutated cell
                       CAR_T.addColor(colorRef);

                       System.out.println("non-mutated cell mutated [" + this.colorRef + "] and divided");




                   } else {
                       // record non-mutated cell
                       CAR_T.nm += 1;
                       System.out.println("non-mutated cell divided");
                   }
                   // add cell
                   Cell3 c2 = G.NewAgentSQ(G.mooreHood[G.rn.Int(nOptions)]);
                   c2.color = this.color;
                   c2.colorRef = this.colorRef;
                   c2.mutated = this.mutated;


                }

            }

        }
    }
}

public class Mutation extends AgentGrid2D<Cell3> {
    public static int[] mutationColors;
    static int a = 0;
    static int b = 0;
    static int c = 0;
    static int d = 0;
    static int e = 0;
    public static void addColor(int color_input) {
        if (color_input == 0) {
            a += 1;
        } else if (color_input == 1) {
            b += 1;
        } else if (color_input == 2) {
            c += 1;
        } else if (color_input == 3) {
            d += 1;
        } else if (color_input == 4) {
            e += 1;
        }
    }

    public static void removeColor(int color_input) {
        if (color_input == 0) {
            a -= 1;
        } else if (color_input == 1) {
            b -= 1;
        } else if (color_input == 2) {
            c -= 1;
        } else if (color_input == 3) {
            d -= 1;
        } else if (color_input == 4) {
            e -= 1;
        }
    }

    public static int nm = 0;
    public static String[][] data = new String[600][7];
    public static void WriteData(int t_input, int a_input, int b_input, int c_input, int d_input, int e_input, int nm_input) {
        data[t_input] = new String[] {
                Integer.toString(t_input),
                Integer.toString(a_input),
                Integer.toString(b_input),
                Integer.toString(c_input),
                Integer.toString(d_input),
                Integer.toString(e_input),
                Integer.toString(nm_input),

        };
    }
    int BLACK=RGB(0,0,0);
    double DEATH_PROB;
    double BIRTH_PROB;
    Rand rn=new Rand();
    int[]mooreHood=MooreHood(false);
    int color;
    public Mutation(int x, int y,int color) {
        super(x, y, Cell3.class);
        this.color=color;
    }
    public void Setup(double rad){
        mutationColors = new int[] {
                RGB(rn.Double(),rn.Double(),rn.Double()),
                RGB(rn.Double(),rn.Double(),rn.Double()),
                RGB(rn.Double(),rn.Double(),rn.Double()),
                RGB(rn.Double(),rn.Double(),rn.Double()),
                RGB(rn.Double(),rn.Double(),rn.Double()),
        };


        int[]coords= CircleHood(true,rad);
        int nCoords= MapHood(coords,xDim/2,yDim/2);
        for (int i = 0; i < nCoords ; i++) {
            NewAgentSQ(coords[i]).color=color;
            nm += 1;
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
        CAR_T m=new CAR_T(100,100, Util.CYAN);
        GridWindow win=new GridWindow(100,100,10);
        m.Setup(10);



        m.setBirthRate(0.2);
        m.setDeathRate(0.02);
        System.out.print("initial birth rate: ");
        System.out.println(m.BIRTH_PROB);
        System.out.print("initial death rate: ");
        System.out.println(m.DEATH_PROB);
        CAR_T.WriteData(0, a,b,c,d,e, nm);
        for (int t = 0; t < 50; t++) {
            win.TickPause(100);
            m.Step();
            m.Draw(win);

            CAR_T.WriteData(t, a,b,c,d,e, nm);
        }

        m.setBirthRate(0.15);
        m.setDeathRate(0.05);
        System.out.print("new birth rate: ");
        System.out.println(m.BIRTH_PROB);
        System.out.print("new death rate: ");
        System.out.println(m.DEATH_PROB);

        for (int t = 50; t < 600; t++) {
            win.TickPause(50);
            m.Step();
            m.Draw(win);

            CAR_T.WriteData(t, a,b,c,d,e, nm);
        }

        for (int i = 0; i < 600; i++) {
            System.out.println(Arrays.toString(data[i]));
        }

        try {
            FileWriter writer = new FileWriter("mutation_data.csv");
            writer.write("t" + "," + "a" + "," + "b" + "," + "c" + "," + "d" + "," + "e" + "," + "nm" + "\n");
            for (int i = 0; i < data.length; i += 10) {
                writer.write(data[i][0] + "," + data[i][1] + "," + data[i][2] + "," + data[i][3] + "," + data[i][4] + "," + data[i][5] + "," + data[i][6] + "\n");
            }


            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);

        }

        try {
            FileWriter writer = new FileWriter("mutation_data_colors.csv");
            writer.write( "index,R,G,B\n");
            Color color2;
            for (int i = 0; i < CAR_T.mutationColors.length; i++) {
                color2 = new Color(CAR_T.mutationColors[i]);
                writer.write( i + "," + color2.getRed() + "," + color2.getGreen() + "," + color2.getBlue() + "\n");

            }


            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);

        }



        System.out.println("\ncolors: " +
                "\n a: " + new Color(CAR_T.mutationColors[0]) +
                "\n b: " + new Color(CAR_T.mutationColors[1]) +
                "\n c: " + new Color(CAR_T.mutationColors[2]) +
                "\n d: " + new Color(CAR_T.mutationColors[3]) +
                "\n e: " + new Color(CAR_T.mutationColors[4])
                );
    }
}
