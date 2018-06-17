package porprezhas.Network.command;

public interface ActionHandler {
    Answer handle(LoginAction loginAction);

    Answer handle(JoinAction joinAction);

    Answer handle(InsertDiceGuiAction insertDiceGuiAction);



    Answer handle(ChoosePatternAction choosePatternAction);

    Answer handle(UseToolCardAction useToolCardAction);

    Answer handle(PassAction passAction);
}
