package sipsimplecs;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.sun.media.rtp.RTPSessionMgr;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.*;
import javax.media.control.TrackControl;
import javax.media.format.AudioFormat;
import javax.media.format.UnsupportedFormatException;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.rtp.InvalidSessionAddressException;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.ReceiveStreamListener;
import javax.media.rtp.SendStream;
import javax.media.rtp.SessionAddress;
import javax.media.rtp.event.NewReceiveStreamEvent;
import javax.media.rtp.event.ReceiveStreamEvent;

/**
 *
 * @author B. slimane
 */
public class TP_RTP extends javax.swing.JFrame implements ReceiveStreamListener {


    String remoteIP; // Sink IP
    int remotePort, localPort; // Sink port and src port
    int fmt = 5; //to store the media format : default is 5 = ULAW_RTP
    String log = "";


    private RTPSessionMgr myVoiceSessionManager=null; //session Manager
    private Processor myProcessor=null;  //processor
    private SendStream ss=null;     //stream to send
    private ReceiveStream rs=null;   // received Stream
    private Player player=null;     //Player
    private AudioFormat af=null;    // audio format
    private DataSource oDS=null;    //Datasource de sortie

    private String soundMicInput = "javasound://0"; //Location of the media source : microphone




    /**
     * fonction pour le log dans le TextArea
     * @param txt : text à écrire
     */
    private void logprog(String txt){
        log = log + txt;
        logTA.setText(log);
    }

    //à l'ouverture de l'application
    private void onOpen(java.awt.event.WindowEvent evt) {
        try {
            String myIP = InetAddress.getLocalHost().getHostAddress();
            localIpTF.setText(myIP);

        } catch (UnknownHostException ex) {
            Logger.getLogger(TP_RTP.class.getName()).log(Level.SEVERE, null, ex);
            logprog(ex.toString());
        }
        logprog("Default Audio Format : ULAW RTP" +"\n");
    }


    //TODO : getParam

    private void getParam(){

        //TODO: obtenir remote IP, local port, remote port et format audio à partir des élements de l'interface graphique;
        //...
        // hint 1 : Integer.parseInt
        // hint 2 : ComboBox.getSelectedIndex + switch case + fmt = 5 ULAW; fmt = 4 G723 ; fmt = 3 GSM
        //afficher les choix avec logprog

        try {
            // Get remote IP from the IP text field
            remoteIP = sinkIPTF.getText();
            System.out.println(remoteIP);

            // Parse local and remote ports from the respective text fields
            localPort = Integer.parseInt(srcRTPportTF.getText());
            System.out.println(localPort);
            remotePort = Integer.parseInt(sinkPortTF.getText());
            System.out.println(remotePort);

            // Get the selected audio format from the ComboBox
            int formatIndex = comboFmt.getSelectedIndex();

            // Assign audio format based on ComboBox selection using a switch case
            switch (formatIndex) {
                case 0:
                    fmt = 5; // ULAW format code
                    logprog("Audio Format: ULAW selected (fmt=5)\n");
                    break;
                case 1:
                    fmt = 3; // GSM format code
                    logprog("Audio Format: GSM selected (fmt=3)\n");
                    break;
                case 2:
                    fmt = 4; // G723 format code
                    logprog("Audio Format: G723 selected (fmt=4)\n");
                    break;
                default:
                    logprog("Unknown format selected.\n");
                    break;
            }
        } catch (NumberFormatException e) {
            logprog("Error: Invalid port number entered.\n");
        }
    }


    /**
     * Creates new form TP_RTP
     */
    public TP_RTP() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        logTA = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        localIpTF = new javax.swing.JTextField();
        sinkIPTF = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        sinkPortTF = new javax.swing.JTextField();
        srcRTPportTF = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        comboFmt = new javax.swing.JComboBox<>();
        Button_start = new javax.swing.JButton();
        Button_stop = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jScrollPane1.setEnabled(false);

        logTA.setColumns(20);
        logTA.setRows(5);
        jScrollPane1.setViewportView(logTA);

        jLabel1.setText("Local IP");

        localIpTF.setEditable(false);
        localIpTF.setBackground(new java.awt.Color(255, 255, 255));
        localIpTF.setName(""); // NOI18N
        localIpTF.setOpaque(false);

        jLabel3.setText("Remote IP");

        jLabel4.setText("Remote RTP port");

        sinkPortTF.setText("15000");

        srcRTPportTF.setText("16000");

        jLabel2.setText("RTP port");
        jLabel2.setToolTipText("");

        jLabel5.setText("Audio Format");

        comboFmt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ULAW", "GSM", "G723" }));
        comboFmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboFmtActionPerformed(evt);
            }
        });

        Button_start.setText("start");
        Button_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    Button_startActionPerformed(evt);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Button_stop.setText("stop");
        Button_stop.setEnabled(false);
        Button_stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_stopActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addComponent(Button_start)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Button_stop)
                                .addGap(100, 100, 100))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(localIpTF, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(srcRTPportTF, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(sinkPortTF))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(sinkIPTF)))
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(comboFmt, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(27, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(localIpTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3)
                                        .addComponent(sinkIPTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(srcRTPportTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4)
                                        .addComponent(sinkPortTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(comboFmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(Button_start)
                                        .addComponent(Button_stop))
                                .addGap(46, 46, 46))
        );

        pack();
    }// </editor-fold>

    private void comboFmtActionPerformed(java.awt.event.ActionEvent evt) {
        int i =comboFmt.getSelectedIndex();
        switch (i){

            case 0 : {logprog("format ULAW_RTP, code format = 5" + "\n");break;}

            case 1 : {logprog("format G723_RTP, code format = 4" + "\n");break;}

            case 2 : {logprog("format GSM_RTP, code format = 3" + "\n");break;}
        }
    }

    private void Button_startActionPerformed(java.awt.event.ActionEvent evt) throws IOException {


        getParam();
        logprog("Starting...." +"\n");


        //TO DO : démarrer une session RTP
        //  ...

        try {
            demarrerSession(remoteIP,remotePort,localPort,fmt);
        }
        catch(IOException | NoDataSourceException | NoProcessorException e) {
            logprog("RTP session failed to start" + e.getMessage()+"\n");
            return;
        }
        logprog("Media Started and Ready...." +"\n");

        //TO DO : desactiver le Bouton Start et activer le Bouton Stop
        //...

        initTransmission();


        Button_start.setEnabled(false);
        Button_stop.setEnabled(true);


    }
    public void initTransmission() throws IOException {
        // This method would start sending the stream
        ss.start(); // Start sending the stream

        // Notify the receiver to confirm
        notifyReceiver();
    }

    private void notifyReceiver() throws IOException {
        // Send a message to the receiver to show a confirmation dialog
        // This could be done via a simple UDP message or another signaling mechanism
        // For example, you can use a simple socket to send a "start transmission" message
        // Show a confirmation dialog on the receiver's side
        int response = javax.swing.JOptionPane.showConfirmDialog(null, "Incoming transmission request. Do you want to accept?", "Incoming Transmission", javax.swing.JOptionPane.YES_NO_OPTION);

        if (response == javax.swing.JOptionPane.YES_OPTION) {
            logprog("Transmission accepted.\n");
            // Start playing the stream
            try {
                player = Manager.createPlayer(rs.getDataSource());
                player.start();
                logprog("Playing received stream.\n");
            } catch (IOException | NoPlayerException e) {
                e.printStackTrace();
            }
        } else {
            logprog("Transmission rejected.\n");
            // Stop the sender from transmitting
            ss.stop();
        }
    }

    private void Button_stopActionPerformed(java.awt.event.ActionEvent evt) {

        logprog("Prepare to stop...." +"\n");

        // TO DO : Arreter la session RTP
        //...

        arreterSession();

        logprog("Media Released...." +"\n");

        //TO DO : desactiver le Bouton Stop et activer le Bouton Start
        //...

        Button_stop.setEnabled(false);
        Button_start.setEnabled(true);
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        onOpen(evt);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TP_RTP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TP_RTP().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton Button_start;
    private javax.swing.JButton Button_stop;
    private javax.swing.JComboBox<String> comboFmt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField localIpTF;
    private javax.swing.JTextArea logTA;
    private javax.swing.JTextField sinkIPTF;
    private javax.swing.JTextField sinkPortTF;
    private javax.swing.JTextField srcRTPportTF;
    // End of variables declaration


    /** TO DO : compléter le code de cette fonction
     * DemarrerSession : permet de démarrer une session RTP
     * @param peerIP : IP destination
     * @param peerPort : Port Destination
     * @param recvPort : Port d'écoute local pour la réception
     * @param fmt : Le format de l'audio
     * @throws IOException
     */
    public int demarrerSession(String peerIP, int peerPort, int recvPort, int fmt) throws IOException, NoDataSourceException, NoProcessorException {


        //TO DO : Creer un MediaLocator à partir du micro
        //....

        MediaLocator mediaLocator = new MediaLocator(soundMicInput);
        //TO DO : Creer la Data source corredpondante
        //....

        DataSource dataSource = Manager.createDataSource(mediaLocator);
        //TO DO : Creer le processeur avec la data Source comme input
        //myProcessor = ... ;
        myProcessor = Manager.createProcessor(dataSource);

        //pour tenir compte du format audio on appelle la fonction :
        PrepareProcessor(fmt);

        //TO DO : obtenir le DataSource resultant (output) à partir du processor
        //oDS = ...;
        oDS = myProcessor.getDataOutput();

        //TO DO : Creer la session Manager
        //myVoiceSessionManager = ...

        myVoiceSessionManager = new RTPSessionMgr();

        //TO DO: On inscrit le session manager pour les événements  de reception de media ReceiveStreamEvents(ajouter un Listener)
        //...

        myVoiceSessionManager.addReceiveStreamListener(this);

        //Creer les adresses pour la session
        SessionAddress senderAddr = new SessionAddress();
        try {
            //init session
            myVoiceSessionManager.initSession(senderAddr, null,0.05,0.25); //0.05 fraction de la bandpassante pour RTCP  ; 0.25 fraction de cette bande passante de RTCP vers dest, le reste pour RTCP vers Source

            //preparer addr local, remote and receiving addr
            InetAddress destAddr = InetAddress.getByName(peerIP);
            SessionAddress localAddr = new SessionAddress (InetAddress.getLocalHost(),recvPort,InetAddress.getLocalHost(),recvPort+1); //RTP et RTCP
            SessionAddress remoteAddr = new SessionAddress(destAddr, peerPort, destAddr, peerPort + 1); //RTP et RTCP

            // Démarrer la session session
            myVoiceSessionManager.startSession(localAddr,localAddr ,remoteAddr,null); //localreceiver; localsender ; remoteAdr; encryption

            //creer le stream à transmettre
            ss = myVoiceSessionManager.createSendStream(oDS, 0); //0 = mixer tout les streams dans un seul
            System.out.println("Send Stream Created...");



        } catch (InvalidSessionAddressException | UnsupportedFormatException ex) {
            System.out.println(TP_RTP.class.getName() + " Exception : " + ex);
        }

        //TO DO : demarrer et capturer le stream
        //commencer la transmission du stream cad le SendStream (ss)
        //...

        //ss.start();

        //TO DO : démarrer le processor
        //...

        myProcessor.start();
        System.out.println("Transmission Ready...");

        return 1;

    }

    @Override
    public void update(ReceiveStreamEvent event) {


        System.out.println("SEND RECEIVE EVENT...");

        if (event instanceof NewReceiveStreamEvent){
            rs=event.getReceiveStream();

            //TO DO : Obtenir la Data Source à partire de ReceiveStream rs
            //...

            DataSource receivedDataSource = rs.getDataSource();
            System.out.println("stream received...");

            //TO DO : Creer un palyer et jouer le media
            //...

            requestTransmission();

        }

//throw new UnsupportedOperationException("Not supported yet.");

        //throw new UnsupportedOperationException("Not Supported Yet!");
    }

    public void requestTransmission() {
        // Show confirmation dialog
        int response = javax.swing.JOptionPane.showConfirmDialog(this, "Incoming transmission request. Do you want to accept?", "Incoming Transmission", javax.swing.JOptionPane.YES_NO_OPTION);

        if (response == javax.swing.JOptionPane.YES_OPTION) {
            logprog("Transmission accepted.\n");
            // Start playing the stream
            try {
                player = Manager.createPlayer(rs.getDataSource());
                player.start();
                logprog("Playing received stream.\n");
            } catch (IOException | NoPlayerException e) {
                e.printStackTrace();
            }
        } else {
            logprog("Transmission rejected.\n");
            // Optionally, you can stop the sender from transmitting
            // This could involve sending a message back to the sender to stop
        }
    }

    void arreterSession(){

        System.out.println("Closing Player...");

        //stopper et fermer le player
        if (player!=null){

            //TO DO : Stopper le player
            //...
            player.stop();

            player.deallocate();

            //TO DO: Fermer le player
            //...

            player.close();
            player=null;
        }

        //try {
        //stopper la transmission
        System.out.println("Stopping transmission...");

        //TO DO : Arreter le sendStream ss et enlever les slash commentaire de la clause try-catch
        //..

        if(ss!=null) {
            try{
                ss.stop();
            }catch(IOException e) {
                logprog("Error Stopping SendStream");
            }
            ss=null;
        }
        //} catch (IOException ex) {
        //System.out.println(TP_RTP.class.getName() + " Exception : " + ex);
        //}

        //stop capture and processing:
        System.out.println("Stopping capture...");

        //TO DO : Arreter le processor
        //..
        if(myProcessor!=null) {
            myProcessor.stop();
            myProcessor.deallocate();
            myProcessor.close();
            myProcessor=null;
        }


        //TO DO : Fermer le processor
        //...
    /*myProcessor.close();
    myProcessor=null;*/
        System.out.println("Closing RTP...");


        //Fermer la session RTP libérer les ports:
        if(myVoiceSessionManager!=null) {
            myVoiceSessionManager.closeSession();
            myVoiceSessionManager.dispose();
            System.out.println("RTP session closed!");
        }

    }


    //preparer and configurer le processor
    void PrepareProcessor(int fmt) {
        myProcessor.configure();
        while (myProcessor.getState()!=Processor.Configured) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                System.out.println(TP_RTP.class.getName() + " Exception : " + ex);
            }
        }
        myProcessor.setContentDescriptor(new ContentDescriptor (ContentDescriptor.RAW_RTP));

        TrackControl track[]=myProcessor.getTrackControls();
        AudioFormat af =null;
        switch (fmt) {
            case 3: af=new AudioFormat(AudioFormat.GSM_RTP,8000,4,1);break;
            case 4: af=new AudioFormat(AudioFormat.G723_RTP,8000,4,1);break;
            case 5: af=new AudioFormat(AudioFormat.ULAW_RTP);break;
            default:logprog("unknown audio format");return;
        }
        track[0].setFormat(af);
        myProcessor.realize();
        while (myProcessor.getState() != Processor.Realized) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                System.out.println(TP_RTP.class.getName() + " Exception : " + ex);
            }
        }
    }

} //class
