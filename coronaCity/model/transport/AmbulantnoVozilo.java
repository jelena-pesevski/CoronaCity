package coronaCity.model.transport;

import coronaCity.model.grad.CoronaCity;
import coronaCity.model.polja.Ambulanta;
import coronaCity.model.polja.Polje;
import coronaCity.model.stanovnici.Stanovnik;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.imageio.ImageIO;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AmbulantnoVozilo implements Serializable{
    
    private boolean slobodnoVozilo;
    private Polje polje;
    
    private static BufferedImage slika;
    private static final String IMG_PATH="./slicice/vozilo.png";

    public static BufferedImage getSlika() {
        return slika;
    }
    
    static{
        try{
            slika=ImageIO.read(new File(IMG_PATH));
        }catch(IOException ex){
            Logger.getLogger(AmbulantnoVozilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void iscrtajVozilo(){
        Graphics g=polje.getGraphics();
        g.drawImage(slika, 0, 0, polje.getWidth(), polje.getHeight(), null);
    }
    
    //sva vozila su na pocetku slobodna
    public AmbulantnoVozilo(){
        slobodnoVozilo=true;
    }

    public boolean isSlobodnoVozilo() {
        return slobodnoVozilo;
    }

    public void setSlobodnoVozilo(boolean slobodnoVozilo) {
        this.slobodnoVozilo = slobodnoVozilo;
    }

    public Polje getPolje() {
        return polje;
    }

    public void setPolje(Polje polje) {
        this.polje = polje;
    }
    
    //ako se vozilo ne koristi prvi put
    public void obrisiVoziloSaPrethodnogPolja(){
        if(polje!=null){
            polje.setVozilo(null);
            polje.repaint();
        }
    }
    
    public void posaljiVozilo(Stanovnik s, Ambulanta a){
        //obrisi vozilo sa prethodnog polja i iscrtaj ga na novoj lokaciji
        obrisiVoziloSaPrethodnogPolja();
        polje=s.getPolje();
        polje.setVozilo(this);
        polje.repaint();
       // iscrtajVozilo();
        
        //prevezi stanovnika u ambulantu, a vozilo ostaje 
        //na istoj lokaciji gdje je pokupilo stanovnika
        s.getPolje().ukloniStanovnikaSaPolja(s);
        a.smjestiStanovnika(s);
        CoronaCity.getInstance().pisiNaFrejm(s.getIme()+s.getIdOsobe()+" je zarazen i u bolnici. id kuce:"+s.getIdentifikatorKuce());
       // System.out.println(s.getIme()+s.getIdOsobe()+" je zarazen i u bolnici. id kuce:"+s.getIdentifikatorKuce());
    }
   
      
}
