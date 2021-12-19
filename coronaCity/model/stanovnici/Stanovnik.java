package coronaCity.model.stanovnici;

import coronaCity.model.grad.CoronaCity;
import coronaCity.model.polja.KontrolniPunkt;
import coronaCity.model.polja.Kuca;
import coronaCity.model.polja.Polje;
import coronaCity.model.polja.Put;
import coronaCity.model.transport.AmbulantnoVozilo;
import coronaCity.utils.Locker;
import coronaCity.utils.PersonUtils;
import coronaCity.utils.RandomUtils;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Stanovnik extends Thread implements Serializable {
    private static int ID=0;
    private int idOsobe;
    private String ime;
    private String prezime;
    private int godRodjenja;
    private int brojGodina;
    private String pol;
    private int identifikatorKuce;
    private Polje polje; 
    private double trenutnaTemp;
    
    private boolean simulacijaAktivna;
    private boolean kreciSe;
    
    private Deque<Double> posljTriTemp=new ArrayDeque<>(3);
    
    private static final double CONST=50.00; //konstanta koja se vraca ukoliko nema izmjerene 3 temp.
    
    public int getIdOsobe() {
        return idOsobe;
    }

    public void setIdOsobe(int idOsobe) {
        this.idOsobe = idOsobe;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public int getGodRodjenja() {
        return godRodjenja;
    }

    public void setGodRodjenja(int godRodjenja) {
        this.godRodjenja = godRodjenja;
    }

    public int getBrojGodina() {
        return brojGodina;
    }

    public void setBrojGodina(int brojGodina) {
        this.brojGodina = brojGodina;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public int getIdentifikatorKuce() {
        return identifikatorKuce;
    }

    public void setIdentifikatorKuce(int identifikatorKuce) {
        this.identifikatorKuce = identifikatorKuce;
    }

    public Polje getPolje() {
        return polje;
    }

    public void setPolje(Polje polje) {
        this.polje = polje;
    }

    public double getTrenutnaTemp() {
        return trenutnaTemp;
    }

    public void setTrenutnaTemp(double trenutnaTemp) {
        this.trenutnaTemp = trenutnaTemp;
    }

    Stanovnik(){
        super();
        
        pol=PersonUtils.randomPol();
        if(PersonUtils.MUSKO.equals(pol)){
            ime=PersonUtils.randomImeMusko();
        }else{
            ime=PersonUtils.randomImeZensko();
        }
        prezime=PersonUtils.randomPrezime();
        idOsobe=ID++;   
    }
    
    @Override
    public String toString(){
        return ime+idOsobe+", "+ brojGodina+", "+polje +" t:"+ String.format("%.1f", trenutnaTemp);
    }
    
    public void ispisKretanja(String smjer){
        CoronaCity.getInstance().pisiNaFrejm(this.toString()+" se pomjerio "+ smjer);
    }
    
    public synchronized void trajnoZaustaviKretanje(){
        simulacijaAktivna=false;
        if(!kreciSe){
            this.notify();
        }
    }
    
    public synchronized void pokreniStanovnikaPrviPut(){
        simulacijaAktivna=true;
        kreciSe=true;
        this.start();
    }
    
    public synchronized void pokreniStanovnikaPoslijeZaustavljanjaSimulacije(){
        simulacijaAktivna=true;
        this.start();
    }
    
    public synchronized void smjestiStanovnikaUIzolaciju(){
        kreciSe=false;
    }
    
    public synchronized void pustiStanovnikaIzIzolacije(){
        kreciSe=true;
        notify();
        CoronaCity.getInstance().pisiNaFrejm(ime+idOsobe+" se moze nastaviti kretati. id kuce"+identifikatorKuce);
      //  System.out.println(ime+idOsobe+" se moze nastaviti kretati. id kuce "+identifikatorKuce);
    }
     
    public String smjerKretanja(int smjer){
        return PersonUtils.randomSmjerKretanja(smjer);
    }
    
    public Kuca getMojaKuca(){
        return CoronaCity.getInstance().pronadjiKucuPoId(identifikatorKuce);
    }
    
    public synchronized boolean isKreciSe(){
        return kreciSe;
    }
    
    //dodaje temperaturu
    public void dodajTempURed(){
            if(posljTriTemp.size()==3){
                posljTriTemp.pollFirst();
                posljTriTemp.offerLast(trenutnaTemp);
            }else{
                posljTriTemp.offerLast(trenutnaTemp);
            }
        //    System.out.println("Temperature u redu za "+ime+idOsobe+posljTriTemp.toString());
    }
    
    //vraca aritmeticku sredinu za kontrolu bolesti
    //ako nisu izmjerene tri temperature ne moze se otpustiti
    //pa vraca vrijednost za koju se sigurno nece otpustiti( npr. 50.00)
    public double aSredinaTemp(){
            if(posljTriTemp.size()==3){
                return posljTriTemp.stream().reduce(0.0, Double::sum)/3;
            }else{
                return CONST; //bitno da vrati broj veci od 37
            }
    }
    
  //  public abstract void iscrtajStanovnika();
    //dijete i odrasla osoba, kao i dva djeteta mogu i na istom polju
    public abstract boolean provjeriRastojanjeOdOsoba(Polje novoPolje); //true ako je sigurno polje
    public abstract boolean provjeriRastojanjeOdKuce(Polje novoPolje);  //true ako je sigurno polje

    @Override
    public void run(){
        try{
            while(simulacijaAktivna){
                synchronized(this){
                    if(!kreciSe){
                        wait();
                    }
                }
                sleep(1000);
                int smjer=RandomUtils.randomInt(4);
                int vrsta=polje.getVrsta();
                int kolona=polje.getKolona();
                Polje novoPolje=null;
                synchronized(Locker.getLock()){
                    if(kreciSe && simulacijaAktivna){
                        
                        for(int i=0; i<4 ; i++){
                            
                            switch (smjer){
                            case 0: //gore
                                novoPolje=CoronaCity.getInstance().getPoljeMatrice(vrsta-1, kolona);
                                break;
                            case 1: //dolje
                                novoPolje=CoronaCity.getInstance().getPoljeMatrice(vrsta+1, kolona);
                                break;
                            case 2: // lijevo
                                novoPolje=CoronaCity.getInstance().getPoljeMatrice(vrsta, kolona-1);
                                break;
                            case 3: //desno
                                novoPolje=CoronaCity.getInstance().getPoljeMatrice(vrsta, kolona+1);
                                break;
                            }
                            
                            if(novoPolje!=null && (novoPolje instanceof Put || novoPolje instanceof KontrolniPunkt)){  
                                if(provjeriRastojanjeOdKuce(novoPolje) && provjeriRastojanjeOdOsoba(novoPolje)){
                                    polje.ukloniStanovnikaSaPolja(this);
                                 
                                    polje.repaint();     //iscrtajStaroPolje
                                    

                                    polje=novoPolje;
                                    polje.dodajStanovnikaNaPolje(this);
                                    
                                    polje.repaint(); 
                                 //   iscrtajStanovnika();
                                    ispisKretanja(smjerKretanja(smjer));
                                    break;   //ovo break je za for koje kompenzuje kretanje
                                }
                            }
                            smjer=(smjer+1)%4;
                        }     
                    }    
                }
            }     
        }catch(InterruptedException ex){
           // ex.printStackTrace();
            Logger.getLogger(Stanovnik.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

