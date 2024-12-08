package sipsimplecs;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SipClientUI extends JFrame {

    private JTextField textFieldLocalSIP;
    private JTextField textFieldDestinationSIP;
    private JTextArea textAreaMsgRecu;
    private JTextArea textAreaMsgSent;
    private JButton buttonAppeler;
    //private JButton Respond;
    private JButton buttonRaccrocher;
    // Bouton "Transfer"
    private JButton buttonTransfer = new JButton("Transfer");

    private SipClient sipClient;

    public SipClientUI() {
        // Configuration de la fenêtre principale
        setTitle("SIP Client");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(null);

        // Initialisation des composants graphiques
        JLabel labelLocalSIP = new JLabel("Adresse SIP locale:");
        labelLocalSIP.setBounds(20, 20, 150, 25);
        add(labelLocalSIP);

        JLabel labeltransferSIP = new JLabel("Adresse SIP de transfert:");
        labeltransferSIP.setBounds(20, 100, 150, 25);
        add(labeltransferSIP);

        // Récupérer l'adresse IP locale et l'afficher avec un port d'écoute
        String localAddress = getLocalIPAddress();
        String localSIP = "sip:" + localAddress + ":6060"; // Port d'écoute 6060
        textFieldLocalSIP = new JTextField(localSIP);

        textFieldLocalSIP.setBounds(180, 20, 380, 25);
        textFieldLocalSIP.setEditable(false);
        add(textFieldLocalSIP);

        String transfereSIP = "sip:" + "10.72.101.134" + ":6060"; // Port d'écoute 6060
        JTextField textFieldtransfereSIP = new JTextField(transfereSIP);
        textFieldtransfereSIP.setBounds(180, 100, 380, 25);
        textFieldtransfereSIP.setEditable(true);
        add(textFieldtransfereSIP);


        JLabel labelDestinationSIP = new JLabel("Adresse SIP destination:");
        labelDestinationSIP.setBounds(20, 60, 150, 25);
        add(labelDestinationSIP);

        textFieldDestinationSIP = new JTextField();
        textFieldDestinationSIP.setBounds(180, 60, 380, 25);
        add(textFieldDestinationSIP);

        JLabel labelMsgRecu = new JLabel("Messages SIP reçus:");
        labelMsgRecu.setBounds(20, 130, 150, 25);
        add(labelMsgRecu);

        textAreaMsgRecu = new JTextArea();
        JScrollPane scrollPaneRecu = new JScrollPane(textAreaMsgRecu);
        scrollPaneRecu.setBounds(20, 150, 260, 150);
        add(scrollPaneRecu);

        JLabel labelMsgSent = new JLabel("Messages SIP envoyés:");
        labelMsgSent.setBounds(310, 130, 150, 25);
        add(labelMsgSent);

        textAreaMsgSent = new JTextArea();
        JScrollPane scrollPaneSent = new JScrollPane(textAreaMsgSent);
        scrollPaneSent.setBounds(310, 150, 260, 150);
        add(scrollPaneSent);

        // Bouton "Appeler"
        buttonAppeler = new JButton("Appeler");
        buttonAppeler.setBounds(80, 300, 120, 30);
        add(buttonAppeler);

        buttonTransfer.setBounds(210, 300, 120, 30);
        add(buttonTransfer);

        // Bouton "Raccrocher"
        buttonRaccrocher = new JButton("Raccrocher");
        buttonRaccrocher.setBounds(340, 300, 120, 30);
        add(buttonRaccrocher);

        // Initialisation de l'objet SipClient

        sipClient = new SipClient();
        sipClient.setUiComponents(textAreaMsgRecu, textAreaMsgSent);

        System.out.println("Initialisation de la pile SIP via onOpen...");
        sipClient.onOpen();

        // Ajout du gestionnaire de clic pour "Transfer"
        buttonTransfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fallbackSIP = textFieldtransfereSIP.getText().trim();; // Remplacez par l'IP et le port de l'hôte de secours
                try {
                    sipClient.transferCall(fallbackSIP); // Appel de la méthode transferCall
                } catch (Exception ex) {
                    ex.printStackTrace();
                    textAreaMsgSent.append("Erreur lors du transfert d'appel : " + ex.getMessage() + "\n");
                }
            }
        });

// Ajout du gestionnaire de clic pour "Appeler"
        buttonAppeler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String destinationSIP = textFieldDestinationSIP.getText().trim();
                if (destinationSIP.isEmpty()) {
                    textAreaMsgSent.append("Erreur: Adresse SIP de destination vide.\n");
                    return;
                }
                try {
                    sipClient.onInvite(destinationSIP); // Appel de la méthode onInvite
                } catch (Exception ex) {
                    ex.printStackTrace();
                    textAreaMsgSent.append("Erreur lors de l'appel : " + ex.getMessage() + "\n");
                }
            }
        });

        // Ajout du gestionnaire de clic pour "Raccrocher"
        buttonRaccrocher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Appeler la méthode onBye pour terminer l'appel
                sipClient.onBye(e);
                //sipClient.stopRingtone();
            }
        });


        // Rendre la fenêtre visible
        setVisible(true);
    }

    // Méthode pour obtenir l'adresse IP locale de la machine
    private String getLocalIPAddress() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "localhost"; // Retourne localhost si l'adresse IP ne peut pas être déterminée
        }
    }

    public static void main(String[] args) {
        // Lancer l'interface utilisateur
        new SipClientUI();
    }
}