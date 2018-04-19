package PorPreZha.Model;


public class PublicObjectiveCard extends ObjectiveCard{

    public enum PUC {

        PUC1("Colori diversi - Riga",1,"Righe senza colori ripetuti",6),
        PUC2("Colori diversi - Colonna", 2, "Colonne senza colori ripetuti", 5),
        PUC3("Sfumeture diverse - Riga", 3, "Righe senza sfumature ripetute", 5),
        PUC4("Sfumature diverse - Colonna", 4, "Colonne senza sfumature ripetute", 4),
        PUC5("Sfumature Chiare", 5, "Set di 1 & 2 ovunque", 2),
        PUC6("Sfumature Medie", 6, "Set di 3 & 4 ovunque", 2),
        PUC7("Sfumature Scure", 7, "Set di 5 &6 ovunque", 2),
        PUC8("Sfumature Diverse", 8, "Set di dadi di ogni valore ovunque", 5),
        PUC9("Diagonali Colorate", 9, "Numero di dadi dello stesso colore diagonalmente adiacenti", 0),
        PUC10("Varietà di Colore", 10, "Set di dadi di ogni colore ovunque", 4);
        // Il punteggio della carta PUC9 viene posto pari a 0 in quanto non ha valore costante, ma dipende dai dadi nello schema del dato giocatore, il punteggio verrò quindi calcolato successivamente

        private final int ID;
        private final String description;
        private final int numScore;
        private final String name;


        PUC(String name, int ID, String description, int numScore) {

            this.name = name;
            this.ID = ID;
            this.description = description;
            this.numScore = numScore;

        }



    }


    public PublicObjectiveCard( PUC puc) {
        super(board);
        this.puc = puc;
    }

    public PUC puc;





}
