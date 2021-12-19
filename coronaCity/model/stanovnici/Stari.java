package coronaCity.model.stanovnici;

import coronaCity.model.grad.CoronaCity;
import coronaCity.model.polja.KontrolniPunkt;
import coronaCity.model.polja.Polje;
import coronaCity.model.polja.Put;
import coronaCity.utils.PersonUtils;
import java.awt.Color;
import java.awt.Graphics;

public class Stari extends Stanovnik{
    
    private int xmax;
    private int xmin;
    private int ymax;
    private int ymin;
    
    public Stari(){
        super();
        setBrojGodina(PersonUtils.randomGodineStari());   
        setGodRodjenja(PersonUtils.randomGodiste(this.getBrojGodina()));
    }
    
    public void postaviGraniceZaKretanje(){
        int gore,dolje,lijevo,desno;
        int maxVrsta=(CoronaCity.getVelicina())-1;
        int maxKolona=(CoronaCity.getVelicina())-1;
        
        gore =this.getMojaKuca().getVrsta()-3;
        dolje=this.getMojaKuca().getVrsta()+3;
        if(gore<0){
            ymin=0;
            ymax=dolje-gore;
        }else if(dolje>maxVrsta){
            ymax=maxVrsta;
            ymin=gore-(dolje-maxVrsta);
        }else{
            ymax=dolje;
            ymin=gore;
        }
 
        lijevo =this.getMojaKuca().getKolona()-3;
        desno=this.getMojaKuca().getKolona()+3;
        if(lijevo<0){
            xmin=0;
            xmax=desno-lijevo;
        }else if(desno>maxKolona){
            xmax=maxVrsta;
            xmin=lijevo-(desno-maxKolona);
        }else{
            xmax=desno;
            xmin=lijevo;
        }
    }
    
   /* @Override
    public synchronized void iscrtajStanovnika(){
        Graphics g=this.getPolje().getGraphics();
        g.setColor(Color.BLACK);
        g.fillOval(0, 0,this.getPolje().getWidth() ,this.getPolje().getHeight());
    }*/

    @Override 
    public synchronized boolean provjeriRastojanjeOdOsoba(Polje novoPolje) {
        for(Stanovnik s:CoronaCity.getInstance().getStanovnici()){
            if(s!=this){
                Polje p=s.getPolje();
                int r=novoPolje.rastojanjeIzmedjuPolja(p);
                if(r<2 && (p instanceof Put || p instanceof KontrolniPunkt)){
                    return false;
                }  
            }
        }
        return true;
    }
    
    @Override 
    public synchronized boolean provjeriRastojanjeOdKuce(Polje novoPolje) {
        if(novoPolje.getVrsta()<ymin || novoPolje.getVrsta()>ymax 
                || novoPolje.getKolona()<xmin || novoPolje.getKolona()>xmax){
            return false;
        }
        return true;
    }
    
    
}
