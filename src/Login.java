import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class Login extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField txtLogin;
	private static InetAddress adresseIP = null;
	private static String pseudo;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Login frame = new Login();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.textField.requestFocus(); //Place le curseur de frappe directement dans le champ de texte
		try {
			if (args.length >0) {
				// S'il y a au moins un argument, prendre la machine dont le nom
		        //   ou l'adresse est en argument
				adresseIP = InetAddress.getByName(args[0]);
		      	}
			else {  // sinon, se connecter en local
		        adresseIP = InetAddress.getLocalHost();
		      }
		    } 
		    catch (Exception e) {
		    }
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setFont(new Font("Dialog", Font.PLAIN, 17));
		setTitle("Connexion NetSketch");
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
		
		
		txtLogin = new JTextField();
		txtLogin.setEditable(false);
		txtLogin.setBackground(SystemColor.menu);
		txtLogin.setHorizontalAlignment(SwingConstants.CENTER);
		txtLogin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtLogin.setText("Login");
		txtLogin.setBounds(50, 74, 96, 32);
		contentPane.add(txtLogin);
		txtLogin.setColumns(10);
		
		JButton btnValider = new JButton("Valider");
		btnValider.setBounds(283, 79, 89, 23);
		btnValider.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			pseudo = textField.getText();
			textField.setText("");
			try {
			// Ouverture de socket sur le port 8888 de la machine locale
			Socket client = new Socket(adresseIP, 8888);
			// Ouverture flux Ã©criture sur socket
			ObjectOutputStream ecriture = new ObjectOutputStream(client.getOutputStream());
			// Ouverture flux lecture sur socket
			ObjectInputStream lecture = new ObjectInputStream(client.getInputStream());
		    // Creation de l'ui 
		    Fenetre f = new Fenetre(lecture, ecriture,pseudo);
		    f.setVisible(true);
		    setVisible(false);
			}catch(Exception e1) {
				System.err.println(e1);
			}
		}
	});
		this.getRootPane().setDefaultButton(btnValider); //Fait en sorte que le bouton par défaut est valider ie on peut taper sur la touche entrée au lieu de cliquer sur le bonton
		contentPane.add(btnValider);
	}
	
	
}
