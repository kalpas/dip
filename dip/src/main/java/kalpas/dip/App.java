package kalpas.dip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import kalpas.dip.app.AppCmd;

public class App
{
    public static void main(String[] args)
    {
        final String fin = "fin";

        InputStreamReader converter = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(converter);

        String line = "";
        StringTokenizer tokens = null;
        String token = null;
        String[] arguemens;
        boolean argsAreValid = false;
        AppCmd cmd = null;

        while(!fin.equals(line))
        {
            try
            {
                line = in.readLine();
            }
            catch(IOException e)
            {
                System.err.println("smth bad: " + e.getCause());
                e.printStackTrace();
            }
            if(line != null)
            {
                tokens = new StringTokenizer(line);
                if(tokens.hasMoreTokens())
                {
                    token = tokens.nextToken();
                    if(!fin.equals(token))
                    {
                        try
                        {
                            cmd = AppCmd.valueOf(token.toLowerCase());
                        }
                        catch(IllegalArgumentException e)
                        {
                        }
                        if(cmd != null)
                        {
                            argsAreValid = true;
                            arguemens = new String[cmd.getArgNumber()];
                            for(int i = 0; i < arguemens.length; i++)
                            {
                                if(tokens.hasMoreTokens())
                                    arguemens[i] = tokens.nextToken();
                                else
                                {
                                    argsAreValid = false;
                                    break;
                                }
                            }
                            if(argsAreValid)
                            {
                                if(cmd.execute(arguemens))
                                {
                                    System.out.println(cmd.toString()+ " executed successfully");
                                }
                                else
                                {
                                    System.err.println("errors while executing "+cmd.toString());
                                }
                                cmd = null;
                            }
                            else
                            {
                                System.err.println("wrong number of params");
                            }
                        }
                        else
                        {
                            System.err.println("command doesn't exist");
                        }
                    }
                    else
                    {
                        System.out.println("Exiting. FIN");
                        return;
                    }
                }
                else
                {
                    System.out.println("please type a command");
                }
            }

        }

    }
}
