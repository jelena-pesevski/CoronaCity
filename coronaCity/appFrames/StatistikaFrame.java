package coronaCity.appFrames;

import coronaCity.utils.StatistickiPodaci;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class StatistikaFrame extends JFrame {
    private final JButton dugme=new JButton("PREUZMI U CSV FORMATU");
	private static final String PORUKA= "Uspjesno izvrseno snimanje podataka.";
	private static final String INFORMACIJA="INFORMACIJE - Ne zaboravi ekstenziju .csv";
    private JTable tabela;
     
    public StatistikaFrame(){
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBounds(100, 100, 500, 200);
        this.setTitle("Statisticki podaci");
        this.setVisible(true);
        
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.##", otherSymbols);
        
        Object data[][]={
            {"zarazeni", df.format(StatistickiPodaci.getProcenatZarazenih())+"%"},
            {"oporavljeni", df.format(StatistickiPodaci.getProcenatOporavljenih())+"%"},
            {"zarazeni-muski pol", df.format(StatistickiPodaci.getProcenatMuskihZarazenih())+"%"},
            {"zarazeni-zenski pol",df.format(StatistickiPodaci.getProcenatZenskihZarazenih())+"%"},
            {"zarazeni-odrasli",df.format(StatistickiPodaci.getProcenatZarazenihOdrasli())+"%"},
            {"zarazeni-stari", df.format(StatistickiPodaci.getProcenatZarazenihStari())+"%"},
            {"zarazeni-djeca", df.format(StatistickiPodaci.getProcenatZarazenihDjece())+"%"},
        };
        String columnNames[]={"Podaci ", " Procentualno"};
        
        tabela=new JTable(data, columnNames);
        this.add(tabela);
        
        this.add(dugme, BorderLayout.PAGE_END);
        dodajOsluskivace();
    }
    
    private void dodajOsluskivace(){
        dugme.addActionListener(e->{
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Odabir lokacije");
            int userSelection = fileChooser.showSaveDialog(this);
            if(userSelection == JFileChooser.APPROVE_OPTION){
            File fajl= fileChooser.getSelectedFile();
            FileWriter csvWriter = null;
            TableModel model = tabela.getModel();
            int row = model.getRowCount();
            int col = model.getColumnCount();
            try {
                csvWriter = new FileWriter(fajl);
                for(int i=0; i< row; i++){
                    for(int j=0; j < col; j++){
                        if(j!=col-1){
                            csvWriter.write(model.getValueAt(i, j).toString()+", ");
                        }else{
                            csvWriter.write(model.getValueAt(i, j).toString());
                        }
                    }
                    csvWriter.write("\n");
                }
                csvWriter.close();
                JOptionPane.showMessageDialog(this, PORUKA, INFORMACIJA, JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException ex) {
                 Logger.getLogger(StatistikaFrame.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    csvWriter.close();
                } catch (IOException ex) {
                    Logger.getLogger(StatistikaFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            }
        });
    }
}
