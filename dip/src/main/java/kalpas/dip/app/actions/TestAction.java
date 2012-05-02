package kalpas.dip.app.actions;

import kalpas.dip.app.Action;
import kalpas.dip.app.NeuralNetworkBanch;


public class TestAction implements Action
{

    public boolean execute(String... args)
    {
        return NeuralNetworkBanch.test(args);
    }
    
}
