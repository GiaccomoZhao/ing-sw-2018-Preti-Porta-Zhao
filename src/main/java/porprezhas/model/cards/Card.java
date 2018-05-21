package porprezhas.model.cards;

import porprezhas.model.dices.Board;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.DiceBag;
import porprezhas.model.dices.DraftPool;
import porprezhas.model.track.RoundTrack;

import java.io.Serializable;

public abstract class
Card implements Serializable {


    public final Effect effect;


    public enum Effect{

        PUC1("Colori diversi - Riga",1,"Righe senza colori ripetuti"),
        PUC2("Colori diversi - Colonna", 2, "Colonne senza colori ripetuti"),
        PUC3("Sfumeture diverse - Riga", 3, "Righe senza sfumature ripetute"),
        PUC4("Sfumature diverse - Colonna", 4, "Colonne senza sfumature ripetute"),
        PUC5("Sfumature Chiare", 5, "Set di 1 & 2 ovunque"),
        PUC6("Sfumature Medie", 6, "Set di 3 & 4 ovunque"),
        PUC7("Sfumature Scure", 7, "Set di 5 &6 ovunque"),
        PUC8("Sfumature Diverse", 8, "Set di dadi di ogni valore ovunque"),
        PUC9("Diagonali Colorate", 9, "Numero di dadi dello stesso colore diagonalmente adiacenti"),
        PUC10("Varietà di Colore", 10, "Set di dadi di ogni colore ovunque"),
        PRC1("Sfumature Rosse - Privata", 11, "Somma dei valori su tutti i dadi rossi"),
        PRC2("Sfumature Gialle - Privata", 12, "Somma dei valori su tutti i dadi gialli"),
        PRC13("Sfumature Verdi - Privata", 13, "Somma dei valori su tutti i dadi verdi"),
        PRC14("Sfumature Blu - Privata", 14, "Somma dei valori su tutti i dadi blu"),
        PRC15("Sfumature Viola - Privata", 15, "Somma dei valori su tutti i dadi viola"),
        TC1("Pinza Sgrossatrice", 16, "Dopo aver scelto un dado,\naumenta o dominuisci il valore\ndel dado scelto di 1\nNon puoi cambiare\nun 6 in 1 o un 1 in 6 "),
        TC2("Pinza per Eglomise", 17, "Muovi un qualsiasi dado nella tua\nvetrata ignorando le restrizioni\ndi colore\nDevi rispettare tutte le altre\nrestrizioni di piazzamento"),
        TC3("Alesatore per lamina di rame", 18, "Muovi un qualsiasi dado nella tua\nvetrata ignorando le restrizioni\ndi valore\nDevi rispettare tutte le altre\nrestrizioni di piazzamento"),
        TC4("Lathekin", 19, "Muovi esattamente due dadi,\nrispettando tutte le restrizioni di\npiazzamento"),
        TC5("Taglierina circolare", 20, "Dopo aver scelto un dado,\nscambia quel dado con un dado\nsul Tracciato dei Round"),
        TC6("Pennello per Pasta Salda", 21, "Dopo aver scelto un dado, tira\nnuovamente quel dado\nSe non puoi piazzarlo, riponilo\nnella Riserva"),
        TC7("Martelletto", 22, "Tira nuovamente\ntutti i dadi della Riserva\nQuesta carta può essera usata\nsolo durante il tuo secondo turno,\nprima di scegliere il secondo dado"),
        TC8("Tenaglia a Rotelle", 23, "Dopo il tuo primo turno scegli\nimmediatamente un altro dado\nSalta il tuo secondo turno in\nquesto round"),
        TC9("Riga in Sughero", 24, "Dopo aver scelto un dado,\npiazzalo in una casella che non\nsia adiacente a un altro dado\nDevi rispettare tutte le restrizioni\ndi piazzamento"),
        TC10("Tampone Diamantato", 25, "Dopo aver scelto un dado, giralo\nsulla faccia opposta\n6 diventa 1, 5 diventa 2, 4\ndiventa 3 ecc."),
        TC11("Diluente per Pasta Salda", 26, "Dopo aver scelto un dado, riponilo nel\nSacchetto, poi pescane uno dal Sacchetto\nScegli il valore del nuovo dado e\npiazzalo, rispettando tutte le restrizioni di\npiazzamento"),
        TC12("Taglierina Manuale", 27, "Muovi fino a due dadi dello\nstesso colore di un solo dado sul\nTracciato dei Round\nDevi rispettare tutte le restrizioni\ndi piazzamento");


        protected  String name;
        protected  int ID;
        protected String description;

        Effect(String name, int ID, String description){
            this.name = name;
            this.ID = ID;
            this.description = description;
        }

    }



    public Card(Effect effect){
        this.effect=effect;
    }

    public String getName() { return effect.name; }

    public String getDescription() {
        return effect.description;
    }

    public int getEffectID(){
        return effect.ID;
    }

    public abstract int apply(Board board);

    public abstract void use(Board board, DraftPool draftPool, int xStart1, int yStart1, int xDestination1, int yDestination1, int xStart2, int yStart2, int xDestination2, int yDestination2, Dice dice1, int  number, boolean operation, RoundTrack roundTrack, DiceBag diceBag);
}
