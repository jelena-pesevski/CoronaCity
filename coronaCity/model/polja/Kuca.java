package coronaCity.model.polja;

import coronaCity.model.stanovnici.Stanovnik;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Kuca extends Polje{
    private static BufferedImage slika;
    private static final String IMG_PATH="./slicice/kuca.jpg";
    
    private static int ID=0;
    private int idKuce;
    private List<Stanovnik> listaUkucana=Collections.synchronizedList(new ArrayList<>());
    
    static{
        try{
            slika=ImageIO.read(new File(IMG_PATH));
        }catch(IOException ex){
            Logger.getLogger(Kuca.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(slika, 0, 0, this.getWidth(), this.getHeight(), null);
    }
    
    public Kuca(int vrsta, int kolona) {
        super(vrsta, kolona);
        idKuce=ID++;
    }

    public int getIdKuce() {
        return idKuce;
    }

    public void setIdKuce(int idKuce) {
        this.idKuce = idKuce;
    }

    public List<Stanovnik> getListaUkucana() {
        return listaUkucana;
    }

    public void setListaUkucana(List<Stanovnik> listaUkucana) {
        this.listaUkucana = listaUkucana;
    }

    public void dodajUListuUkucana(Stanovnik s){
        listaUkucana.add(s);
    }
    
    public int getBrojUkucana(){
        return listaUkucana.size();
    }
    
   /* public void ispisiUkucane(){
        System.out.println("lokacija kuce:"+this);
        for(int i=0; i<listaUkucana.size(); i++){
            System.out.println(listaUkucana.get(i));
        }
    }*/
    
    @Override
    public String toString(){
        return super.toString();
    }
}
