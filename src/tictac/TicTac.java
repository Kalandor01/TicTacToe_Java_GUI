package tictac;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTac extends JFrame implements ActionListener{
        private int lepesDb = 0;
        private boolean nyerte;
        private static int hossz = 3;
        private static int seged = hossz - 1;
        private JButton btGomb[][] = new JButton[hossz][hossz];
        private final JButton btStart = new JButton("Új játék");
        private final JLabel lbUzenet=new JLabel("1. lépés: X");
        private final JLabel lbMeret=new JLabel("Méret: ");
        private final String[] felirat={"0", "X"}; 
        private boolean dontetlen = false;
        private final JComboBox cbMeret = new JComboBox(new String[] {"3*3", "4*4", "5*5", "6*6", "7*7", "8*8", "9*9", "10*10", "11*11", "50*50"});
        private final JPanel pnJatekTer=new JPanel(new GridLayout(hossz, hossz));
        private Font betu=new Font("Comic Sans MS", Font.BOLD, 280/hossz);
        private int[] btAkt_pos = new int[2];
        private boolean robotLefut = false;

    public TicTac() {
        inicializal(hossz);
    }    

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btAkt = (JButton)e.getSource();
        String jatekos;
        
        
        if(btAkt == btStart) {
            hossz = Integer.parseInt(cbMeret.getSelectedItem().toString().split("\\*")[0]);
            seged = hossz - 1;
            meretetAllit(hossz);
        }
        
        else  if (!nyerte)
            {
                //üres gombra kattintunk
                if (btAkt.getText().equals(""))
                {
                    //get akt bt pos
                    for (int x = 0; x < hossz; x++)
                    {
                        int y;
                        for (y = 0; y < hossz && btGomb[x][y] != btAkt; y++) {}
                        if(y<hossz)
                        {
                            btAkt_pos = new int[] {x, y};
                            break;
                        }
                    }
                    //other
                    lepesDb++;
                    jatekos = felirat[(lepesDb+1)%2];
                    lbUzenet.setText((lepesDb+1) + ". lépés: " + jatekos);
                    btAkt.setText(felirat[lepesDb%2]);

                    ellenoriz_meta();
                    if (!nyerte && robotLefut)
                    {
                        gep_lep();
                        ellenoriz_meta();
                    }
                }
            }
    }

    public void ellenoriz_meta() {
        if(lepesDb >= (hossz * 2) - 1 || lepesDb > 8)
        {
            if (hossz < 6)
                ellenoriz();
            else
                ellenoriz_5();
        }
    }
        
    private void meretetAllit(int hossz) {

        pnJatekTer.removeAll(); //gombok kiürítése
        pnJatekTer.setLayout(new GridLayout(hossz, hossz));
        betu=new Font("Comic Sans MS", Font.BOLD, 280/hossz);
        btGomb = new JButton[hossz][hossz];
        gombokLetrehozasa();
        revalidate();
        ujrakezd();
    }
    private void inicializal(int hossz) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Tic Tac Toe 3.0");
        setSize(700, 750);
//        setResizable(false);
        setLocationRelativeTo(this);
        JPanel pnAlap = new JPanel();
        pnAlap.add(btStart);
        pnAlap.add(lbMeret);
        pnAlap.add(cbMeret);
        btStart.addActionListener(this);
        pnAlap.add(lbUzenet);
        add(pnAlap, BorderLayout.NORTH);
        gombokLetrehozasa();
        add(pnJatekTer);
        setVisible(true);
        ujrakezd();
    }
    
    private void gombokLetrehozasa() {
        for (int i = 0; i < hossz; i++)
            {
                for (int j = 0; j < hossz; j++)
                {
                    btGomb[i][j] = new JButton();
                    btGomb[i][j].setFont(betu);
                    btGomb[i][j].setName(i+" "+j);
                    btGomb[i][j].addActionListener(this);
                    pnJatekTer.add(btGomb[i][j]);
                }
            }
    }
    
    private void ujrakezd() {
        nyerte = false;
        lepesDb = 0;
            for (int i = 0; i < hossz; i++)
            {
                for (int j = 0; j < hossz; j++)
                {
                    btGomb[i][j].setText("");
                }
            }
        lbUzenet.setText((lepesDb+1)+". lépés: X");
        dontetlen = false;
    }

    private void ellenoriz() {
              
        //jobbra le átló
        jobbraLeAtlo();
           
        //balra le átló
        balraLeAtlo();
                    
        //soron belüli ell...
        soronBelul();
                        
        //oszlopon belüli ell.
        oszloponBelul();
            
        //döntetlen
        dontetlenE();
        
    }

    private void ellenoriz_5() {
        //jobbra le átló
        jelKeresJobbraLe();
        //balra le átló
        jelKeresBalraLe();
        //soron belüli ell...
        jelKeresSoronBelul();
        //oszlopon belüli ell.
        jelKeresOszloponBelul();
        //döntetlen
        dontetlenE();
    }

    public void gep_lep() {
        String jatekos;
        jatekos = felirat[(lepesDb+1)%2];

        int sz;
        int h;
        do {
            sz = (int)(Math.random() * hossz);
            h = (int)(Math.random() * hossz);
        } while (!btGomb[sz][h].getText().equals(""));
        //get bt akt pos
        btAkt_pos = new int[] {sz, h};

        btGomb[sz][h].setText(jatekos);

        lepesDb++;
        jatekos = felirat[(lepesDb+1)%2];
        lbUzenet.setText((lepesDb+1) + ". lépés: " + jatekos);
    }
    
    private void nyerteskiir(String nyert) {
        nyerte = true;
        JOptionPane.showMessageDialog(this, "Nyert az "+nyert+" jellel játszó játékos "+lepesDb+" lépésben!");
    }

    private void oszloponBelul() {
        int sor;
        int oszlop = 0;
        
        if (!nyerte)
            {
                while (oszlop <= seged && !nyerte)
                {
                    sor = 0;
                    while (sor < seged && btGomb[sor][oszlop].getText().equals(btGomb[sor + 1][oszlop].getText()) && !btGomb[0][oszlop].getText().equals(""))
                    {
                        sor++;
                    }
                    if (sor == seged)
                    {
                        nyerteskiir(btGomb[0][oszlop].getText());
                    }
                    oszlop++;
                }
            }
    }

    private void soronBelul() {
        int oszlop;
        int sor = 0;
        if (!nyerte)   {
            while (sor <= seged && !nyerte)     {
                    oszlop = 0;
                    while (oszlop < seged && btGomb[sor][oszlop].getText().equals(btGomb[sor][oszlop + 1].getText()) && !btGomb[sor][0].getText().equals(""))
                    {
                        oszlop++;
                    }
                    if (oszlop == seged)
                    {
                        nyerteskiir(btGomb[sor][0].getText());
                    }
                    sor++;
                }
        }
    }

    private void jobbraLeAtlo() {
        int i = 0;
        if (!nyerte && !btGomb[2][2].getText().equals(""))
            {
                while (i < seged && btGomb[i][i].getText().equals(btGomb[i + 1][i + 1].getText()))
                {
                    i++;
                }
                if (i == seged)
                {
                    nyerteskiir(btGomb[2][2].getText());
                }
            }
    }

    private void balraLeAtlo() {
        int i = 0;
        if (!nyerte && !btGomb[seged][0].getText().equals("")){
            while (i < seged && btGomb[i][seged - i].getText().equals(btGomb[i + 1][seged - i-1].getText()))
                {
                    i++;
                }
            if (i == seged)
                {
                    nyerteskiir(btGomb[seged][0].getText());
                }
        }
    }

    private void dontetlenE() {
        if (lepesDb == hossz*hossz && !nyerte && !dontetlen)
            {
                JOptionPane.showMessageDialog(this, "Döntetlen!");
                dontetlen = true;
                nyerte = true;
            }
    }

    private void jelKeresJobbraLe()
    {
        String akt_jel = btGomb[btAkt_pos[0]][btAkt_pos[1]].getText();
        int sor = btAkt_pos[0];
        int oszlop = btAkt_pos[1];
        int jelek = 0;
        while (oszlop > -1 && sor < hossz && btGomb[sor][oszlop].getText().equals(akt_jel))
        {
            oszlop--;
            sor++;
            jelek++;
        }
        sor = btAkt_pos[0] - 1;
        oszlop = btAkt_pos[1] + 1;
        while (oszlop < hossz && sor > -1 && btGomb[sor][oszlop].getText().equals(akt_jel))
        {
            oszlop++;
            sor--;
            jelek++;
        }
        System.out.printf("Átló fel: \"%s\" klikkelt:\n%s. sor\n%s. oszlop\n", akt_jel, (btAkt_pos[0] + 1), (btAkt_pos[1] + 1));
        System.out.println(jelek + " jel volt.\n");
        if(jelek >= 5)
            nyerteskiir(akt_jel);
    }
    
    private void jelKeresBalraLe()
    {
        String akt_jel = btGomb[btAkt_pos[0]][btAkt_pos[1]].getText();
        int sor = btAkt_pos[0];
        int oszlop = btAkt_pos[1];
        int jelek = 0;
        while (oszlop > -1 && sor > -1 && btGomb[sor][oszlop].getText().equals(akt_jel))
        {
            oszlop--;
            sor--;
            jelek++;
        }
        sor = btAkt_pos[0] + 1;
        oszlop = btAkt_pos[1] + 1;
        while (oszlop < hossz && sor < hossz && btGomb[sor][oszlop].getText().equals(akt_jel))
        {
            oszlop++;
            sor++;
            jelek++;
        }
        System.out.printf("Átló le: \"%s\" klikkelt:\n%s. sor\n%s. oszlop\n", akt_jel, (btAkt_pos[0] + 1), (btAkt_pos[1] + 1));
        System.out.println(jelek + " jel volt.\n");
        if(jelek >= 5)
            nyerteskiir(akt_jel);
    }
    
    private void jelKeresSoronBelul()
    {
        String akt_jel = btGomb[btAkt_pos[0]][btAkt_pos[1]].getText();
        int sor = btAkt_pos[0];
        int oszlop = btAkt_pos[1];
        int jelek = 0;
        while (oszlop > -1 && btGomb[sor][oszlop].getText().equals(akt_jel))
        {
            oszlop--;
            jelek++;
        }
        oszlop = btAkt_pos[1] + 1;
        while (oszlop < hossz && btGomb[sor][oszlop].getText().equals(akt_jel))
        {
            oszlop++;
            jelek++;
        }
        System.out.printf("Sor: \"%s\" klikkelt:\n%s. sor\n%s. oszlop\n", akt_jel, (btAkt_pos[0] + 1), (btAkt_pos[1] + 1));
        System.out.println(jelek + " jel volt.\n");
        if(jelek >= 5)
            nyerteskiir(akt_jel);
    }

    private void jelKeresOszloponBelul()
    {
        //MIÉNK
        String akt_jel = btGomb[btAkt_pos[0]][btAkt_pos[1]].getText();
        int sor = btAkt_pos[0];
        int oszlop = btAkt_pos[1];
        int jelek = 0;
        while (sor > -1 && btGomb[sor][oszlop].getText().equals(akt_jel))
        {
            sor--;
            jelek++;
        }
        sor = btAkt_pos[0] + 1;
        while (sor < hossz && btGomb[sor][oszlop].getText().equals(akt_jel))
        {
            sor++;
            jelek++;
        }
        System.out.printf("Oszlop: \"%s\" klikkelt:\n%s. sor\n%s. oszlop\n", akt_jel, (btAkt_pos[0] + 1), (btAkt_pos[1] + 1));
        System.out.println(jelek + " jel volt.\n\n\n");
        if(jelek >= 5)
            nyerteskiir(akt_jel);
    }
    
    public static void main(String[] args) {

        TicTac ticTac = new TicTac();
    }
}
