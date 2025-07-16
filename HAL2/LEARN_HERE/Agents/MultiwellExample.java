package LEARN_HERE.Agents;


import HAL.Tools.MultiWellExperiment.MultiWellExperiment;

import static HAL.Util.*;

public class MultiwellExample{
    public static void StepModel(CAR_T model, int iWell){
        model.Step();
    }
    public static int DrawModel(CAR_T model, int x, int y){
        Cell3 c = model.GetAgent(x,y);
        return c == null ? BLACK : c.color;
    }
    public static void main(String[] args){
        int x=100,y=100;
        CAR_T[] models=new CAR_T[]{new CAR_T(x,y,RED),new CAR_T(x,y,GREEN),new CAR_T(x,y,BLUE),
        new CAR_T(x,y,YELLOW),new CAR_T(x,y,CYAN),new CAR_T(x,y,MAGENTA)};
        for (CAR_T model : models) {
            model.Setup(2);
        }
        MultiWellExperiment<CAR_T> expt=new MultiWellExperiment<CAR_T>(3,2,models,x,y, 5, WHITE, MultiwellExample::StepModel,MultiwellExample::DrawModel);
        expt.Run(200,false,100);
    }
}
