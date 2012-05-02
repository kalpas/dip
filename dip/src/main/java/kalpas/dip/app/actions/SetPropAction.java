package kalpas.dip.app.actions;

import kalpas.dip.app.Action;
import kalpas.dip.app.NeuralNetworkBanch;

public class SetPropAction implements Action
{

    public boolean execute(String... args)
    {
        return NeuralNetworkBanch.setProperty(args);
    }

}
