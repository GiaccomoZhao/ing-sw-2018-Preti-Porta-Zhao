package PorPreZha.Model;

import com.sun.tools.internal.xjc.reader.xmlschema.BindPurple;

import java.util.ArrayList;
import java.util.List;



public enum class ToolCard extends Card{

    public int tokensQuantity;


    TC1("Pinza Sgrossatrice",16,"Dopo aver scelto un dado,\naumenta o dominuisci il valore\ndel dado scelto di 1\nNon puoi cambiare\nun 6 in 1 o un 1 in 6 ",0,PURPLE),TC2("Pinza per Eglomise",17,"Muovi un qualsiasi dado nella tua\nvetrata ignorando le restrizioni\ndi colore\nDevi rispettare tutte le altre\nrestrizioni di piazzamento",0,BLUE),TC3("Alesatore per lamina di rame",18,"Muovi un qualsiasi dado nella tua\nvetrata ignorando le restrizioni\ndi valore\nDevi rispettare tutte le altre\nrestrizioni di piazzamento",0,RED),TC4("Lathekin",19,"Muovi esattamente due dadi,\nrispettando tutte le restrizioni di\npiazzamento",0,YELLOW),TC5("Taglierina circolare",20,"Dopo aver scelto un dado,\nscambia quel dado con un dado\nsul Tracciato dei Round",0,GREEN),TC6("Pennello per Pasta Salda",21,"Dopo aver scelto un dado, tira\nnuovamente quel dado\nSe non puoi piazzarlo, riponilo\nnella Riserva",0,PURPLE),TC7("Martelletto",22,"Tira nuovamente\ntutti i dadi della Riserva\nQuesta carta può essera usata\nsolo durante il tuo secondo turno,\nprima di scegliere il secondo dado",0,BLUE),TC8("Tenaglia a Rotelle",23,"Dopo il tuo primo turno scegli\nimmediatamente un altro dado\nSalta il tuo secondo turno in\nquesto round",0,RED),TC9("Riga in Sughero",24,"Dopo aver scelto un dado,\npiazzalo in una casella che non\nsia adiacente a un altro dado\nDevi rispettare tutte le restrizioni\ndi piazzamento",0,YELLOW),TC10("Tampone Diamantato",25,"Dopo aver scelto un dado, giralo\nsulla faccia opposta\n6 diventa 1, 5 diventa 2, 4\ndiventa 3 ecc.",0,GREEN),TC11("Diluente per Pasta Salda",26,"Dopo aver scelto un dado, riponilo nel\nSacchetto, poi pescane uno dal Sacchetto\nScegli il valore del nuovo dado e\npiazzalo, rispettando tutte le restrizioni di\npiazzamento",0,PURPLE),TC12("Taglierina Manuale",27,"Muovi fino a due dadi dello\nstesso colore di un solo dado sul\nTracciato dei Round\nDevi rispettare tutte le restrizioni\ndi piazzamento",0,BLUE),TC13("Strip Cutter",28,"Take one die from any player. Give them\na die of matching Color or Value\nThey may place it ignoring Color or\nValue Restrictions\nMay only be used before Round 7",0,PURPLE);

    ToolCard(String name,int ID,String description,int tokensQuantity, Dice.ColorDice colorDice){

        this.name=name;

        this.ID=ID;

        this.description=description;

        this.tokensQuantity=tokensQuantity;
        
        this.colorDice=colorDice;
    }


    public void use(){};

    public void addTokens(){
        tokensQuantity=tokensQuantity+1;
    }


}
