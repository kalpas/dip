package kalpas.dip.app.actions;

import kalpas.dip.app.Action;
import kalpas.dip.app.NeuralNetworkBanch;

public class ShowPatternAction implements Action
{

    public boolean execute(String... args)
    {
        return NeuralNetworkBanch.showPattern(args);
    }

}
