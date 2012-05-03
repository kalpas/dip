package kalpas.dip.app;

import kalpas.dip.app.actions.DrawAction;
import kalpas.dip.app.actions.LoadAction;
import kalpas.dip.app.actions.NewAction;
import kalpas.dip.app.actions.SaveAction;
import kalpas.dip.app.actions.SetPropAction;
import kalpas.dip.app.actions.ShowPatternAction;
import kalpas.dip.app.actions.TestAction;
import kalpas.dip.app.actions.TrainAction;
import kalpas.dip.app.actions.ViewAction;


public enum AppCmd
{
    loadnet(new LoadAction(),1),
    savenet(new SaveAction(),1),
    train(new TrainAction(),1),
    test(new TestAction(),0),
    setproperty(new SetPropAction(),2),
    newnet(new NewAction(),0),
    showpattern(new ShowPatternAction(),2),
    drawdigit(new DrawAction(),1),
    viewnet(new ViewAction(),2);
    
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
