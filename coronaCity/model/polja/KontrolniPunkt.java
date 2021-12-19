package coronaCity.model.polja;

import coronaCity.appFrames.MainFrame;
import coronaCity.model.grad.CoronaCity;
import coronaCity.model.stanovnici.Odrasli;
import coronaCity.model.stanovnici.Stanovnik;
import coronaCity.model.stanovnici.Stari;
import coronaCity.model.transport.Alarm;
import coronaCity.model.transport.AmbulantnoVozilo;
import coronaCity.utils.Locker;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.Handler;
import java.util.logging.FileHandler;

public class KontrolniPunkt extends Polje implements Runnable{
    private transient Thread nit;
    private boolean radi;
    private int xmax, xmin, ymax, ymin;
    
    public static boolean simulacijaZaustavljena=false;
 
    @Override
    public void paint(Graphics g){
       /* super.paint(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());*/
       
        super.paint(g);
        if(this.isZauzeto()){
            Stanovnik s=this.vratiJednogStanovnika();
            if(s instanceof Odrasli){
                g.setColor(Color.MAGENTA);
                g.fillOval(0, 0,this.getWidth() ,this.getHeight());
            }else if(s instanceof Stari){
                g.setColor(Color.BLACK);
                g.fillOval(0, 0,this.getWidth() ,this.getHeight());
            }else{
                g.setColor(Color.CYAN);
                g.fillOval(this.getWidth()/8, this.getHeight()/8,this.getWidth()*3/4 ,this.getHeight()*3/4);
            }
        }else if(this.imaVozilo()){
            AmbulantnoVozilo v=this.getVozilo();
            g.drawImage(AmbulantnoVozilo.getSlika(), 0, 0, this.getWidth(), this.getHeight(), null);
        }else{
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        
    }
    
    public KontrolniPunkt(int vrsta, int kolona) {
        super(vrsta, kolona);
    }
    
    public synchronized void pokreniPunkt(){
        nit=new Thread(this);
        radi=true;
        nit.start();
    }
    
    public synchronized void zaustaviPunkt(){
        radi=false;
    }
    
    public void postaviGraniceZaSkeniranje(){
        int velicina=CoronaCity.getVelicina();
        int vrsta=this.getVrsta();
        int kolona=this.getKolona();
        
        ymin=(vrsta-1)<0?0:vrsta-1;
        ymax=(vrsta+1)>(velicina-1)?velicina-1:vrsta+1;
        
        xmin=(kolona-1)<0?0:kolona-1;
        xmax=(kolona+1)>(velicina-1)?velicina-1:kolona+1;
    }

    @Override
    public void run() {
        while(radi){

            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Logger.getLogger(KontrolniPunkt.class.getName()).log(Level.SEVERE, null, ex);
            }
            for(int i=ymin; i<ymax; i++){
                for(int j=xmin; j<xmax; j++){
                    Polje p=CoronaCity.getInstance().getPoljeMatrice(i, j);
                    if(p.isZauzeto() && (p instanceof Put || p instanceof KontrolniPunkt)){
                        
                         //sinhronizovano sa stanovnicima
                         //da ne promijene poziciju prije nego ih zaustavi
                         //ako su locirani
                        synchronized(Locker.getLock()){
                            if(!MainFrame.simulacijaZaustavljena){
                            for(Stanovnik s:p.getStanovniciNaPolju()){
                                if(s.getTrenutnaTemp()>=37 && s.isKreciSe()){
                                    s.smjestiStanovnikaUIzolaciju();
                                    CoronaCity.getInstance().dodajAlarm(new Alarm(s));
                                }  
                            }
                            }
                        }

                    }
                }
            }

        }     
    }
}
