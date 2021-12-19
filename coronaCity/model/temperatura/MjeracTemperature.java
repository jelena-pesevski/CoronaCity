package coronaCity.model.temperatura;

import coronaCity.appFrames.MainFrame;
import coronaCity.model.grad.CoronaCity;
import coronaCity.model.polja.Ambulanta;
import coronaCity.model.stanovnici.Stanovnik;
import coronaCity.utils.Locker;
import coronaCity.utils.PersonUtils;
import java.util.Iterator;
import java.util.TimerTask;

public class MjeracTemperature extends TimerTask {

    
    public MjeracTemperature(){
    }
    
    public void prekiniMjerenjaTemperatura(){
        this.cancel();
    }
    
    @Override
    public void run() {
        if(!MainFrame.simulacijaZaustavljena){
            
            for(Stanovnik s: CoronaCity.getInstance().getStanovnici()){
                s.setTrenutnaTemp(PersonUtils.randomTemperatura());
                s.dodajTempURed();
            }
            
            synchronized(Locker.getLockZaAmbulante()){   //ovo sinhronizujem sa kreiranjem ambulanti da neko slucajno ne doda ambulantu                                                            
                for(Ambulanta a:CoronaCity.getInstance().getAmbulante()){  //se desava ovo i da baci izuzetak
                  //  synchronized(a){
                        Iterator<Stanovnik> i=a.getStanovniciNaPolju().iterator();
                        while(i.hasNext()){
                            Stanovnik s=i.next();
                            if(s.aSredinaTemp()<37.00){
                                a.ukloniStanovnikaIzAmbulante(s);
                                if(!Ambulanta.imaLiUkucanaUAmbulanti(s)){
                                    s.pustiStanovnikaIzIzolacije();
                                    Ambulanta.ponovoPokreniUkucane(s);
                                }
                                i.remove();
                            }    
                        }
                  //  }
                }
            }
        }
    }
}
