package porprezhas.view.fx.gameScene.state;

import porprezhas.model.dices.Pattern;

public class PlayerInfo {    // NOTE: should i create a new class?
    public final int position;
    public final String name;
    public final int iconId;
    public final Pattern.TypePattern typePattern;

    public PlayerInfo(int position, String name, int iconId, Pattern.TypePattern typePattern) {
        this.position = position;
        this.name = name;
        this.iconId = iconId;
        this.typePattern = typePattern;
    }

    @Override
    public String toString() {
        return String.format("PlayerInfo{" +
                            "position=" + "%2d" +
                            ", name='" + "%-12s" + '\'' +
                            ", iconId=" + "%2d" +
                            ", typePattern=" + "%s" +
                            '}',
                position, name, iconId, typePattern);
    }
}
