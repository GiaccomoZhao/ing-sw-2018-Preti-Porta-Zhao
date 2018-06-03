package porprezhas.model.cards;

import javafx.fxml.FXML;
import porprezhas.model.dices.Board;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.DiceBag;
import porprezhas.model.dices.DraftPool;
import porprezhas.model.track.RoundTrack;

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
        TC1(   1,  "Pinza Sgrossatrice",            "Dopo aver scelto un dado,\naumenta o dominuisci il valore\ndel dado scelto di 1\nNon puoi cambiare\nun 6 in 1 o un 1 in 6 "),
        TC2(   2,  "Pennello per Eglomise",         "Muovi un qualsiasi dado nella tua\nvetrata ignorando le restrizioni\ndi colore\nDevi rispettare tutte le altre\nrestrizioni di piazzamento"),
        TC3(   3,  "Alesatore per lamina di rame",  "Muovi un qualsiasi dado nella tua\nvetrata ignorando le restrizioni\ndi valore\nDevi rispettare tutte le altre\nrestrizioni di piazzamento"),
        TC4(   4,  "Lathekin",                      "Muovi esattamente due dadi,\nrispettando tutte le restrizioni di\npiazzamento"),
        TC5(   5,  "Taglierina circolare",          "Dopo aver scelto un dado,\nscambia quel dado con un dado\nsul Tracciato dei Round"),
        TC6(   6,  "Pennello per Pasta Salda",      "Dopo aver scelto un dado, tira\nnuovamente quel dado\nSe non puoi piazzarlo, riponilo\nnella Riserva"),
        TC7(   7,  "Martelletto",                   "Tira nuovamente\ntutti i dadi della Riserva\nQuesta carta può essera usata\nsolo durante il tuo secondo turno,\nprima di scegliere il secondo dado"),
        TC8(   8,  "Tenaglia a Rotelle",            "Dopo il tuo primo turno scegli\nimmediatamente un altro dado\nSalta il tuo secondo turno in\nquesto round"),
        TC9(   9,  "Riga in Sughero",               "Dopo aver scelto un dado,\npiazzalo in una casella che non\nsia adiacente a un altro dado\nDevi rispettare tutte le restrizioni\ndi piazzamento"),
        TC10( 10,  "Tampone Diamantato",            "Dopo aver scelto un dado, giralo\nsulla faccia opposta\n6 diventa 1, 5 diventa 2, 4\ndiventa 3 ecc."),
        TC11( 11,  "Diluente per Pasta Salda",      "Dopo aver scelto un dado, riponilo nel\nSacchetto, poi pescane uno dal Sacchetto\nScegli il valore del nuovo dado e\npiazzalo, rispettando tutte le restrizioni di\npiazzamento"),
        TC12( 12,  "Taglierina Manuale",            "Muovi fino a due dadi dello\nstesso colore di un solo dado sul\nTracciato dei Round\nDevi rispettare tutte le restrizioni\ndi piazzamento");


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
        return effect.name;
    }


    public abstract int apply(Board board);
/*
    public abstract void use(Board board, DraftPool draftPool, int xStart1, int yStart1, int xDestination1, int yDestination1, int xStart2, int yStart2, int xDestination2, int yDestination2, Dice dice1, int  number, boolean operation, RoundTrack roundTrack, DiceBag diceBag);
    */
}
