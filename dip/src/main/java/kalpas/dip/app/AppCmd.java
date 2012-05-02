package kalpas.dip.app;

import kalpas.dip.app.actions.DrawAction;
import kalpas.dip.app.actions.LoadAction;
import kalpas.dip.app.actions.NewAction;
import kalpas.dip.app.actions.SaveAction;
import kalpas.dip.app.actions.SetPropAction;
import kalpas.dip.app.actions.ShowPatternAction;
import kalpas.dip.app.actions.TestAction;
import kalpas.dip.app.actions.TrainAction;


public enum AppCmd
{
    LoadNet(new LoadAction(),1),
    SaveNet(new SaveAction(),1),
    Train(new TrainAction(),1),
    Test(new TestAction(),0),
    SetProperty(new SetPropAction(),2),
    New(new NewAction(),0),
    ShowPattern(new ShowPatternAction(),2),
    DrawDigit(new DrawAction(),1);
    
    private Action action;
    
    private int args;
    
    public int getArgNumber()
    {
        return args;
    }
    
    public boolean execute(String...args)
    {
        return action.execute(args);
    }
    
    private AppCmd(Action action, int args)
    {
        this.action = action;
        this.args = args;
    }
}
