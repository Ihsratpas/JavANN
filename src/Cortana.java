import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Cortana extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextField textField = new JTextField();
	
	private JTextArea textChat = new JTextArea();
	
	public Cortana() {
		this.setTitle("Cortana");
		this.setSize(600,600);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setVisible(true);
		
		textField.setLocation(2,540);
		textField.setSize(590,30);
		
		String greetingsI[] = {"hello","hi","hola","hey"};
		String howI[] = {"how are you","how're you","how has your"};
		
		String compliments[] = {"nice","cool","awesome","terrific","super","great","good","like"};
		String insults[] = {"bad","terrible","unearthly","inhuman","hate","horrible","dislike"};
		String question[] = {"do you"};
		String why[] = {"why"};
		String who[] = {"who"};
		String is[] = {"is","are"};
		String sure[] = {"sure"};
		String ai[] = {"ai","artificial intelligence","robot","chatbot","algorithm","program","code"};
		
		String greetingsO[] = {"Hello there!","Hello!","Hey there!","Hey!"};
		String howO[] = {"I'm doing well, thanks!","Not too bad."};
		String unknown[] = {"I didn't get that.","Can you rephrase that?","???"};
		String human[] = {" I am a human being."," I'm human."," I'm human, just like you are."};
		
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String userText = textField.getText();
				textChat.append("You: " + userText + "\n");
				textField.setText("");
				
				String words[] = userText.split(" ");
				for(int i = 0;i < words.length;i++) {
					words[i] = words[i].toLowerCase();
				}
				
				String statement = "";
				boolean done = false;
				if(check(userText,greetingsI)) {
					int decider = (int)(Math.random() * greetingsO.length);
					statement = greetingsO[decider];
					done = true;
				} else if(check(userText,howI)){
					int decider = (int)(Math.random() * howO.length);
					statement = howO[decider];
					done = true;
				} else if(userText.contains("your name")) {
					statement = "My name is Cortana.";
					done = true;
				} else if(userText.contains("Cortana")) {
					statement = "Actually, it's pronounced \"Cortana\".";
					if(check(userText,compliments)) {
						statement += " But thanks!";
					} else if(check(userText,insults)) {
						statement += " And it's better than your name.";
					}
					done = true;
				} else if(checkYou(words,"do") != null) {
					statement = "Yes, I do " + checkYou(words,"do");
					done = true;
				} else if(check(userText,why)) {
					statement = "Why not?";
					done = true;
				} else if(check(userText,who)) {
					statement = "I am Cortana, a human.";
					done = true;
				} else if(check(userText,is)) {
					statement = "No.";
					if(check(userText,ai)) {
						int decider = (int)(Math.random() * 2 + 1);
						switch(decider) {
						case 1: 
							int decider1 = (int)(Math.random() * human.length);
							statement += human[decider1];
							break;
						case 2:
							statement += " Or maybe, we're both robots.";
						}
					}
					done = true;
				} else if(check(userText,sure)) {
					statement = "I'm quite sure.";
					done = true;
				}
				if(!done) {
					int decider = (int)(Math.random() * unknown.length);
					statement = unknown[decider];
				}
				if(textChat.getLineCount() < 30) {
					say(statement);
				} else {
					textChat.setText("");
				}
			}
		}); 
		textChat.setLocation(15,5);
		textChat.setSize(560,510);
		textChat.setEditable(false);
		
		this.add(textField);
		this.add(textChat);
	}
	
	public void say(String s) {
		textChat.append("Cortana: " + s + "\n");
	}
	
	public boolean check(String x,String[] y) {
		boolean z = false;
		for(String i:y) {
			if(x.toLowerCase().contains(i.toLowerCase())) {
				z = true;
			}
		}
		return z;
	}
	
	public static String checkYou(String[] x,String y) {
		String z = null;
		String b[] = new String[x.length];
		for(int i = 0;i < x.length;i++) {
			b[i] = x[i];
		}
		for(int i = 0;i < x.length;i++) {
			if(x[i].toLowerCase().equals(y.toLowerCase()) && x[i + 1].equals("you")) {
				for(int j = 0;j < x.length;j++) {
					if(j != i + 2) {
						b[j] = null;
					} else {
						break;
					}
				}
				b[b.length - 1].replace("?", ".");
				String a = "";
				for(int j = 0;j < x.length;j++) {
					if(b[j] != null) {
						a += b[j];
						if(j != x.length - 1) {
							a += " ";
						}
					}
				}
				z = a;
				break;
			}
		}
		return z;
	}
	
	public static void main(String[] args) {
		new Cortana();
	}
	
}
