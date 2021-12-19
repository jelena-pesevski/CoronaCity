package coronaCity.utils;

import coronaCity.appFrames.MainFrame;
import coronaCity.model.grad.CoronaCity;
import coronaCity.model.polja.Ambulanta;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileWatcher extends Thread{
    
    private static final String IME_FAJLA="zarazeni.txt";
    private Path path;
    private boolean radi=false;
    
    public FileWatcher(Path path){
        this.path=path;
    }
    
    public void pokreniFileWatcher(){
        radi=true;
        this.start();
    }
    
    public void zaustaviFileWatcher(){
        radi=false;
    }
    
    @Override
    public void run(){
        try (WatchService watcher = FileSystems.getDefault().newWatchService()){
            path.register(watcher, ENTRY_MODIFY);
            
            while(radi){
                WatchKey key;
                
                key = watcher.take();
                
                for(WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    
                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();
                    
                    if(fileName.toString().trim().equals(IME_FAJLA)
                            && kind.equals(ENTRY_MODIFY)){
                        //salje glavnoj formi znak da azurira brojeve zarazenih
                        //i oporavljenih
                        MainFrame.getInstance().updateBrojZarazenihIOporavljenih();
                    }
                }
                boolean valid = key.reset();
                if (!valid) { break; }
            }
        } catch (IOException ex) {
            Logger.getLogger(FileWatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch(InterruptedException ex){
            Logger.getLogger(FileWatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
