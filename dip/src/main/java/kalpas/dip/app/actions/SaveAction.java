package kalpas.dip.app.actions;

import kalpas.dip.app.Action;
import kalpas.dip.app.NeuralNetworkBanch;

public class SaveAction implements Action
{
    public boolean execute(String...args)
    {
        return NeuralNetworkBanch.save(args);
    }

}
