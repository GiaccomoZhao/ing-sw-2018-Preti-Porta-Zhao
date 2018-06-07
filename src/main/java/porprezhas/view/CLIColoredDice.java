package porprezhas.view;

enum Color{
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m");
    static final String RESET = "\u001B[0m";
    private String escape;
    Color(String escape) {
        this.escape = escape;
    }
    public String escape() {
        return escape;
    }
}

public class TEst {
    // U+2680 to U+2685
    public static final String[] faces = {
            "\u2680",
            "\u2681",
            "\u2682",
            "\u2683",
            "\u2684",
            "\u2685"
    };

    public static void main(String[] args) {
        for (int i = 0; i < faces.length; i++) {
            System.out.println(Color.values()[i%Color.values().length].escape() + faces[i]);
        }

    }

}
