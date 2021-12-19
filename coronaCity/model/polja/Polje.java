package coronaCity.model.polja;

import coronaCity.model.stanovnici.Stanovnik;
import coronaCity.model.transport.AmbulantnoVozilo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;

public abstract class Polje extends JPanel implements Serializable {
    
    private int vrsta;
    private int kolona;
    private List<Stanovnik> stanovniciNaPolju=Collections.synchronizedList(new ArrayList<>());
    
    private boolean voziloNaPolju=false;
    private AmbulantnoVozilo vozilo;
   
    public Polje(int vrsta, int kolona) {
        this.vrsta = vrsta;
        this.kolona = kolona;
    }

    public int getVrsta() {
        return vrsta;
    }

    public void setVrsta(int vrsta) {
        this.vrsta = vrsta;
    }

    public int getKolona() {
        return kolona;
    }
    
    public void setKolona(int kolona) {
        this.kolona = kolona;
    }

    public List<Stanovnik> getStanovniciNaPolju() {
        return stanovniciNaPolju;
    }

    public void setStanovniciNaPolju(List<Stanovnik> stanovniciNaPolju) {
        this.stanovniciNaPolju = stanovniciNaPolju;
    }
   
    public synchronized void dodajStanovnikaNaPolje(Stanovnik s){
        stanovniciNaPolju.add(s);
    }
    
    public synchronized void ukloniStanovnikaSaPolja(Stanovnik s){
        stanovniciNaPolju.remove(s);
    }
    
    public synchronized Stanovnik vratiJednogStanovnika(){
        if(!this.stanovniciNaPolju.isEmpty()){
            return stanovniciNaPolju.get(0);
        }return null;
    }
   /* public void ispisiStanovnikeSaPolja(){
        for(Stanovnik s: stanovniciNaPolju){
            System.out.println(s+ " na polju: "+ this);
        }
    }*/

    @Override
    public String toString(){
        return "["+vrsta+","+kolona+"]";
    }
    
    public synchronized int rastojanjeIzmedjuPolja(Polje drugoPolje){
        int dx=Math.abs(this.kolona-drugoPolje.getKolona());
        int dy=Math.abs(this.vrsta-drugoPolje.getVrsta());
        return dx<dy?dy:dx;
    }
    
    public boolean isZauzeto(){
        return !stanovniciNaPolju.isEmpty();
    }
    

    //dio koda da kada stanovnik predje preko vozila da ga mozemo ponovo iscrtati
    //ako je bilo na polju
    public AmbulantnoVozilo getVozilo() {
        return vozilo;
    }
    
    public boolean imaVozilo(){
        return vozilo!=null;
    }

    public void setVozilo(AmbulantnoVozilo vozilo) {
        if(vozilo==null){
            this.vozilo=null;
            voziloNaPolju=false;
            return;
        }
        this.vozilo = vozilo;
        voziloNaPolju=true;
    }
    
}
