package coronaCity.model.stanovnici;

import coronaCity.model.grad.CoronaCity;
import coronaCity.model.polja.KontrolniPunkt;
import coronaCity.model.polja.Polje;
import coronaCity.model.polja.Put;
import coronaCity.utils.PersonUtils;
import java.awt.Color;
import java.awt.Graphics;

public class Dijete extends Stanovnik{
    
    public Dijete(){
        super();
        setBrojGodina(PersonUtils.randomGodineDjeca());
        setGodRodjenja(PersonUtils.randomGodiste(this.getBrojGodina()));
    }
    
  /*  @Override
     public synchronized void iscrtajStanovnika(){
        Graphics g=this.getPolje().getGraphics();
        g.setColor(Color.CYAN);
        g.fillOval(this.getPolje().getWidth()/8, this.getPolje().getHeight()/8,this.getPolje().getWidth()*3/4 ,this.getPolje().getHeight()*3/4);
    }*/
     
    @Override
    public synchronized boolean provjeriRastojanjeOdOsoba(Polje novoPolje) {
         for(Stanovnik s:CoronaCity.getInstance().getStanovnici()){
            if(s!=this){
                Polje p=s.getPolje();
                int r=novoPolje.rastojanjeIzmedjuPolja(p);
                if(r<2 && s instanceof Stari && (p instanceof Put || p instanceof KontrolniPunkt)){
                   return false;
                }  
            }
        }
        return true;
    }

    @Override  
    public synchronized boolean provjeriRastojanjeOdKuce(Polje novoPolje) {
        return true;
    }
}
