package sigmobile.sigapp.signet;

/**
 * Created by amaddah on 29/04/16.
 */

public class Paquet {
    /** Construit un paquet avec la fonction prepare()
     * Construction du message :
     * typeDuMessage suivi de la taille de la trame complete
     * Puis du séparateur ; et enfin le message réel (le payload)
     * Exemple : 110;coucou
     */

    private int type = 0;
    private String payload = "";

    private int tailleTrame = 0;


    final String sep = ";";
    final int tailleType = 1;
    final int octetTaille = 4;
    final int tailleEnTete = tailleType + octetTaille;


    public int getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

    public int getTailleTrame() {
        return tailleTrame;
    }

    public String getSep() {
        return sep;
    }

    public int getTailleType() {
        return tailleType;
    }

    public int getOctetTaille() {
        return octetTaille;
    }

    public int getTailleEnTete() {
        return tailleEnTete;
    }

    public Paquet(int type, String payload) {
        this.type = type;
        this.payload = payload;
        this.tailleTrame = this.tailleEnTete + this.getSep().length() + this.getPayload().length();
    }

    public String stringFill(int n, int taille){
        return String.format("%0"+String.valueOf(taille)+"d", n);
    }

    public String prepare(){
        return String.valueOf(type) + stringFill(tailleTrame,octetTaille) + sep + this.payload;
    }
}
