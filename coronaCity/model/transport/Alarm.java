package coronaCity.model.transport;

import coronaCity.model.grad.CoronaCity;
import coronaCity.model.polja.Kuca;
import coronaCity.model.polja.Polje;
import coronaCity.model.stanovnici.Stanovnik;
import java.io.Serializable;

public class Alarm implements Serializable{
    
    private Stanovnik osoba;
    private static final String ALARM_ZNAK="!!!";
    private static final String DODATAK_PORUCI=" vracen kuci. id kuce:";
    
    public Alarm(Stanovnik s){
        osoba=s;
    }
    
    public String getAlarmTekst(){
        return ALARM_ZNAK +osoba+", id kuce:"+ osoba.getIdentifikatorKuce();
    }
    
    public Polje getLokacijuStanovnika(){
        return osoba.getPolje();
    }

    public Stanovnik getOsoba() {
        return osoba;
    }

    public void setOsoba(Stanovnik osoba) {
        this.osoba = osoba;
    }
    //vraca ukucane kucama
    public void obavijestiUkucane(){
        Kuca k=osoba.getMojaKuca();
        for(Stanovnik s: k.getListaUkucana()){
            //ako nije osoba za koju je alarm ili osoba iz kuce istovremeno locirana od strane nekog punkta
            if(s!=osoba && s.isKreciSe()){   
                s.smjestiStanovnikaUIzolaciju();
                s.getPolje().ukloniStanovnikaSaPolja(s);
                s.getPolje().repaint();        
                                
                s.setPolje(k);
                k.dodajStanovnikaNaPolje(s);
                CoronaCity.getInstance().pisiNaFrejm(s.getIme()+s.getIdOsobe()+DODATAK_PORUCI+s.getIdentifikatorKuce());
               // System.out.println(s.getIme()+s.getIdOsobe()+DODATAK_PORUCI+s.getIdentifikatorKuce());
            }   
        }   
    }

}
