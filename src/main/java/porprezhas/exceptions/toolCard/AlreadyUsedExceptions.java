package porprezhas.exceptions.toolCard;

import porprezhas.exceptions.GenericInvalidActionException;

public class AlreadyUsedExceptions extends GenericInvalidActionException
{
    public AlreadyUsedExceptions(){super("You have already used a tool card in this turn");}

}
