import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;

import java.awt.Container;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;



public class SaisieNomSauvegarde extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField txtSaisieNomSauvegarde;
	private static String nom;


	/**
	 * Create the frame.
	 */
	public SaisieNomSauvegarde(JPanel panel) {
		setFont(new Font("Dialog", Font.PLAIN, 17));
		setTitle("Enregistrer Sous");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 428, 227);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(156, 75, 120, 31);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPane.add(textField);
		textField.setColumns(10);
		
		
		txtSaisieNomSauvegarde = new JTextField();
		txtSaisieNomSauvegarde.setEditable(false);
		txtSaisieNomSauvegarde.setBackground(SystemColor.menu);
		txtSaisieNomSauvegarde.setHorizontalAlignment(SwingConstants.CENTER);
		txtSaisieNomSauvegarde.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtSaisieNomSauvegarde.setText("Nom du fichier");
		txtSaisieNomSauvegarde.setBounds(50, 74, 106, 32);
		contentPane.add(txtSaisieNomSauvegarde);
		
		
		JButton btnValider = new JButton("Valider");
		btnValider.setBounds(283, 79, 89, 23);
		btnValider.addActionListener(new ActionListener() { // Définit l'action à faire lorsque le nom du fichier est entré
		public void actionPerformed(ActionEvent e) {
			nom = textField.getText();
			textField.setText("");
			Container c = panel;
			BufferedImage im = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
			c.paint(im.getGraphics());
			try {			
				ImageIO.write(im, "PNG", new File(nom+".jpg"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			setVisible(false); //Rend invisible la fenêtre quand l'utilisateur a fini de l'utiliser
			dispose(); //Détruit la fenêtre
		}
	});
		
		this.getRootPane().setDefaultButton(btnValider); //Fait en sorte que le bouton par défaut est valider ie on peut taper sur la touche entrée au lieu de cliquer sur le bonton
		contentPane.add(btnValider);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.textField.requestFocus(); //Place le curseur de frappe directement dans le champ de texte
	}
		
}
