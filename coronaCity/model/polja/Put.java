package coronaCity.model.polja;

import coronaCity.appFrames.MainFrame;
import coronaCity.model.stanovnici.Odrasli;
import coronaCity.model.stanovnici.Stanovnik;
import coronaCity.model.stanovnici.Stari;
import coronaCity.model.transport.AmbulantnoVozilo;
import java.awt.Color;
import java.awt.Graphics;

public class Put extends Polje{

    public Put(int vrsta, int kolona) {
        super(vrsta, kolona);
    }
    
    @Override
    public void paint(Graphics g){
     
      /*  super.paint(g);
        g.setColor(Color.LIGHT_GRAY);
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
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        
    }
    
}
