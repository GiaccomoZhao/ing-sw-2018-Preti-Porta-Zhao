package porprezhas.model.cards;

import porprezhas.model.dices.Board;

import java.io.Serializable;

public abstract class Card implements Serializable {


    public final Effect effect;

    public enum Effect{

        PUC1(  1,  "Colori diversi - Riga",         "Righe senza colori ripetuti"),
        PUC2(  2,  "Colori diversi - Colonna",      "Colonne senza colori ripetuti"),
        PUC3(  3,  "Sfumeture diverse - Riga",      "Righe senza sfumature ripetute"),
        PUC4(  4,  "Sfumature diverse - Colonna",   "Colonne senza sfumature ripetute"),
        PUC5(  5,  "Sfumature Chiare",              "Set di 1 & 2 ovunque"),
        PUC6(  6,  "Sfumature Medie",               "Set di 3 & 4 ovunque"),
        PUC7(  7,  "Sfumature Scure",               "Set di 5 &6 ovunque"),
        PUC8(  8,  "Sfumature Diverse",             "Set di dadi di ogni valore ovunque"),
        PUC9(  9,  "Diagonali Colorate",            "Numero di dadi dello stesso colore diagonalmente adiacenti"),
        PUC10(10,  "Varietà di Colore",             "Set di dadi di ogni colore ovunque"),
        PRC1(  1,  "Sfumature Rosse - Privata",     "Somma dei valori su tutti i dadi rossi"),
        PRC2(  2,  "Sfumature Gialle - Privata",    "Somma dei valori su tutti i dadi gialli"),
        PRC3(  3,  "Sfumature Verdi - Privata",     "Somma dei valori su tutti i dadi verdi"),
        PRC4(  4,  "Sfumature Blu - Privata",       "Somma dei valori su tutti i dadi blu"),
        PRC5(  5,  "Sfumature Viola - Privata",     "Somma dei valori su tutti i dadi viola"),
        TC1(   1,  "Pinza Sgrossatrice",            "Dopo aver scelto un dado, aumenta o dominuisci il valore del dado scelto di 1 Non puoi cambiare un 6 in 1 o un 1 in 6 "),
        TC2(   2,  "Pennello per Eglomise",         "Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di colore Devi rispettare tutte le altre restrizioni di piazzamento"),
        TC3(   3,  "Alesatore per lamina di rame",  "Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di valore Devi rispettare tutte le altre restrizioni di piazzamento"),
        TC4(   4,  "Lathekin",                      "Muovi esattamente due dadi, rispettando tutte le restrizioni di piazzamento"),
        TC5(   5,  "Taglierina circolare",          "Dopo aver scelto un dado, scambia quel dado con un dado sul Tracciato dei Round"),
        TC6(   6,  "Pennello per Pasta Salda",      "Dopo aver scelto un dado, tira nuovamente quel dado Se non puoi piazzarlo, riponilo nella Riserva"),
        TC7(   7,  "Martelletto",                   "Tira nuovamente tutti i dadi della Riserva Questa carta può essera usata solo durante il tuo secondo turno, prima di scegliere il secondo dado"),
        TC8(   8,  "Tenaglia a Rotelle",            "Dopo il tuo primo turno scegli immediatamente un altro dado Salta il tuo secondo turno in questo round"),
        TC9(   9,  "Riga in Sughero",               "Dopo aver scelto un dado, piazzalo in una casella che non sia adiacente a un altro dado Devi rispettare tutte le restrizioni di piazzamento"),
        TC10( 10,  "Tampone Diamantato",            "Dopo aver scelto un dado, giralo sulla faccia opposta 6 diventa 1, 5 diventa 2, 4 diventa 3 ecc."),
        TC11( 11,  "Diluente per Pasta Salda",      "Dopo aver scelto un dado, riponilo nel Sacchetto, poi pescane uno dal Sacchetto Scegli il valore del nuovo dado e piazzalo, rispettando tutte le restrizioni di piazzamento"),
        TC12( 12,  "Taglierina Manuale",            "Muovi fino a due dadi dello stesso colore di un solo dado sul Tracciato dei Round Devi rispettare tutte le restrizioni di piazzamento");


        public final int ID;
        public final String name;
        public final String description;

        public static final int PUBLIC_OBJECTIVE_CARD_NUMBER = 10;
        public static final int PRIVATE_OBJECTIVE_CARD_NUMBER = 5;
        public static final int TOOL_CARD_NUMBER = 12;

        Effect(int ID, String name, String description){
            this.name = name;
            this.ID = ID;
            this.description = description;
        }
    }



    public Card(Effect effect){
        this.effect=effect;
    }

    @Override
    public String toString() {
        return effect.name.concat(":  " + effect.description);
    }


}
