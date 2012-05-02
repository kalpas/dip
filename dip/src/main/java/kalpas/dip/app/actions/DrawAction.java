package kalpas.dip.app.actions;

import kalpas.dip.app.Action;
import kalpas.dip.app.NeuralNetworkBanch;

public class DrawAction implements Action
{

    public boolean execute(String... args)
    {
        return NeuralNetworkBanch.drawDigit(args);
    }

}
