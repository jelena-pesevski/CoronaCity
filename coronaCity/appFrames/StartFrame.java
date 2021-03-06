package coronaCity.appFrames;

import coronaCity.utils.RandomUtils;


/**
 *
 * @author Korisnik
 */
public class StartFrame extends javax.swing.JFrame {

    /**
     * Creates new form StartFrame
     */
    
    //pocetno i krajnje vrijeme trajanja simulacije
    public static long startTime = System.nanoTime();
    public static long endTime;
    
    private static final double PUNKTOVI_PROCENAT=0.20;
    private static final double KUCE_PROCENAT=0.15;
    private static final double PROCENAT_STANOVNIKA=0.20;
    private static final double VOZILA_PROCENAT=0.20;
    private static final int DEFAULT_VOZILA=3;
    private static final int DEFAULT_STANOVNIKA_JEDNE_VRSTE=8;
    private static final int DEFAULT_KUCA=8;
    private static final int DEFAULT_PUNKTOVA=7;

    public StartFrame() {
        initComponents();
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        odrasli = new javax.swing.JTextField();
        djeca = new javax.swing.JTextField();
        stari = new javax.swing.JTextField();
        kuce = new javax.swing.JTextField();
        punktovi = new javax.swing.JTextField();
        vozila = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Corona City");

        jLabel1.setText("Broj odraslih:");

        jLabel2.setText("Broj djece:");

        jLabel3.setText("Broj starih:");

        jLabel4.setText("Broj kuca:");

        jLabel5.setText("Broj kontrolnih punktova:");

        jLabel6.setText("Broj ambulantnih vozila:");

        jButton1.setText("START");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(vozila)
                    .addComponent(punktovi)
                    .addComponent(stari, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(djeca)
                    .addComponent(odrasli)
                    .addComponent(kuce))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(149, 149, 149)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(odrasli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(djeca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(stari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(kuce, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(punktovi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(vozila, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        boolean ispravno=true;
        int velicina=RandomUtils.randomInt(16)+15;
      //  int velicina=6;
        int brOdraslih=0;
        int brDjece=0;
        int brStarih=0;
        int brKuca=0;
        int brPunktova=0;
        int brVozila=0;
        try{
            brOdraslih=Integer.parseInt(odrasli.getText().trim());
            brDjece=Integer.parseInt(djeca.getText().trim());
            brStarih=Integer.parseInt(stari.getText().trim());
            brKuca=Integer.parseInt(kuce.getText().trim());
            brPunktova=Integer.parseInt(punktovi.getText().trim());
            brVozila=Integer.parseInt(vozila.getText().trim());
            dispose();
           // MainFrame.napraviJedinuInstancu(velicina, brOdraslih, brDjece, brStarih, brKuca, brPunktova, brVozila);
        }catch (NumberFormatException e) {
            ispravno = false;
        }
        if(ispravno){
            //skaliranje vrijednosti u slucaju da su prevelike
            
            int ukupnoStanovnika=brOdraslih+brDjece+brStarih;
            double procenat=ukupnoStanovnika*1.0/Math.pow(velicina, 2);
            
            if(brOdraslih+brStarih==0){
                brOdraslih=StartFrame.DEFAULT_STANOVNIKA_JEDNE_VRSTE;
                brStarih=StartFrame.DEFAULT_STANOVNIKA_JEDNE_VRSTE;
                brDjece=(int) Math.min(brDjece,(double) brDjece*StartFrame.PROCENAT_STANOVNIKA/procenat);
            }else{
                brOdraslih=(int) Math.min(brOdraslih, 1.0* brOdraslih*StartFrame.PROCENAT_STANOVNIKA/procenat);
                brStarih=(int) Math.min(brStarih, 1.0*brStarih*StartFrame.PROCENAT_STANOVNIKA/procenat);
                brDjece=(int) Math.min(brDjece, 1.0* brDjece*StartFrame.PROCENAT_STANOVNIKA/procenat);
            }
            
            if(brKuca!=0){
                brKuca=(int) Math.min(brKuca, Math.pow(velicina, 2)*1.0*StartFrame.KUCE_PROCENAT);
            }else{
                brKuca=brOdraslih+brStarih;
            }
            
            brPunktova=(int) Math.min(brPunktova, Math.pow(velicina, 2)*1.0*StartFrame.PUNKTOVI_PROCENAT);
            
            if(brVozila==0 && brPunktova!=0){
                brVozila=StartFrame.DEFAULT_VOZILA;
            }else{
                brVozila=(int) Math.min(brVozila, Math.pow(velicina, 2)*1.0*StartFrame.VOZILA_PROCENAT);
            }
 
            MainFrame.napraviJedinuInstancu(velicina, brOdraslih, brDjece, brStarih, brKuca, brPunktova, brVozila);
        }else{
            brOdraslih=StartFrame.DEFAULT_STANOVNIKA_JEDNE_VRSTE;
            brStarih=StartFrame.DEFAULT_STANOVNIKA_JEDNE_VRSTE;
            brDjece=StartFrame.DEFAULT_STANOVNIKA_JEDNE_VRSTE;
            brKuca=StartFrame.DEFAULT_KUCA;
            brPunktova=StartFrame.DEFAULT_PUNKTOVA;
            brVozila=StartFrame.DEFAULT_VOZILA;
            MainFrame.napraviJedinuInstancu(velicina, brOdraslih, brDjece, brStarih, brKuca, brPunktova, brVozila);
        }
            
    }//GEN-LAST:event_jButton1ActionPerformed


    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StartFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField djeca;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField kuce;
    private javax.swing.JTextField odrasli;
    private javax.swing.JTextField punktovi;
    private javax.swing.JTextField stari;
    private javax.swing.JTextField vozila;
    // End of variables declaration//GEN-END:variables
}
