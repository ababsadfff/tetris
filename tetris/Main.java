 
package tetris;
 
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
 
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
 
public class Main extends JFrame implements KeyListener {
	private JTextArea[][] grids;  
	private int data[][];   
	private int[] allRect;   
	private int rect;   
	private int x, y;   
	private int score = 0;   
	private JLabel label;   
	private JLabel label1;  
	private boolean running;   
	public Main() {
		grids = new JTextArea[26][12];  
		data = new int[26][12];  
		allRect = new int[] { 0x00cc, 0x8888, 0x000f, 0x0c44, 0x002e, 0x088c, 0x00e8, 0x0c88, 0x00e2, 0x044c, 0x008e,
				0x08c4, 0x006c, 0x04c8, 0x00c6, 0x08c8, 0x004e, 0x04c4, 0x00e4 };  
																										  
																										  
																										  
		label = new JLabel("score: 0");   
		label1 = new JLabel("start");   
		running = false;   
		init();   
	}
	public void init() {
		JPanel center = new JPanel();   
		JPanel right = new JPanel();   
		center.setLayout(new GridLayout(26, 12, 1, 1));   
		for (int i = 0; i < grids.length; i++) {  
			for (int j = 0; j < grids[i].length; j++) {
				grids[i][j] = new JTextArea(20, 20);
				grids[i][j].setBackground(Color.WHITE);
				grids[i][j].addKeyListener(this);  
				  
				if (j == 0 || j == grids[i].length - 1 || i == grids.length - 1) {
					grids[i][j].setBackground(Color.PINK);
					data[i][j] = 1;
				}
				grids[i][j].setEditable(false);  
				center.add(grids[i][j]);   
			}
		}
		  
		right.setLayout(new GridLayout(4, 1));
		right.add(new JLabel(" a : left        d : right"));
		right.add(new JLabel(" s : down   w : change"));
		right.add(label);
		label1.setForeground(Color.RED);  
		right.add(label1);
		  
		this.setLayout(new BorderLayout());
		this.add(center, BorderLayout.CENTER);
		this.add(right, BorderLayout.EAST);
		running = true;   
		this.setSize(600, 850);  
		this.setVisible(true);  
		this.setLocationRelativeTo(null);  
		this.setResizable(false);  
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	}
	public static void main(String[] args) {
		Main m = new Main();   
		m.go();  
	}
	public void go() {  
		while (true) {  
			if (running == false) {  
				break;
			}
			ranRect();  
			start();  
		}
		label1.setText("game over");  
	}
	public void ranRect() {
		rect = allRect[(int) (Math.random() * 19)];  
	}
	public void start() {
		x = 0;
		y = 5;   
		for (int i = 0; i < 26; i++) {  
			try {
				Thread.sleep(1000);  
				if (canFall(x, y) == false) {  
					saveData(x, y);  
					for (int k = x; k < x + 4; k++) {  
						int sum = 0;
						for (int j = 1; j <= 10; j++) {
							if (data[k][j] == 1) {
								sum++;
							}
						}
						if (sum == 10) {  
							removeRow(k);
						}
					}
					for (int j = 1; j <= 10; j++) {  
						if (data[3][j] == 1) {
							running = false;
							break;
						}
					}
					break;
				}
				  
				x++;  
				fall(x, y);  
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
 
		}
	}
	public boolean canFall(int m, int n) {
		int temp = 0x8000;  
		for (int i = 0; i < 4; i++) {  
			for (int j = 0; j < 4; j++) {
				if ((temp & rect) != 0) {  
					if (data[m + 1][n] == 1)  
						return false;
				}
				n++;  
				temp >>= 1;
			}
			m++;  
			n = n - 4;  
		}
		return true;  
	}
	public void saveData(int m, int n) {
		int temp = 0x8000;  
		for (int i = 0; i < 4; i++) {  
			for (int j = 0; j < 4; j++) {
				if ((temp & rect) != 0) {  
					data[m][n] = 1;  
				}
				n++;  
				temp >>= 1;
			}
			m++;  
			n = n - 4;  
		}
	}
	public void removeRow(int row) {
		for (int i = row; i >= 1; i--) {
			for (int j = 1; j <= 10; j++) {
				data[i][j] = data[i - 1][j];  
			}
		}
		reflesh();  
		score += 10;  
		label.setText("score: " + score);  
	}

	public void reflesh() {
		for (int i = 1; i < 25; i++) {
			for (int j = 1; j < 11; j++) {
				if (data[i][j] == 1) {
					grids[i][j].setBackground(Color.GREEN);
				} else {  
					grids[i][j].setBackground(Color.WHITE);
				}
			}
		}
	}

	public void fall(int m, int n) {
		if (m > 0)
			clear(m - 1, n);
		draw(m, n);
	}
	public void clear(int m, int n) {
		int temp = 0x8000;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if ((temp & rect) != 0) {
					grids[m][n].setBackground(Color.WHITE);
				}
				n++;
				temp >>= 1;
			}
			m++;
			n = n - 4;
		}
	}
	public void draw(int m, int n) {
		int temp = 0x8000;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if ((temp & rect) != 0) {
					grids[m][n].setBackground(Color.GREEN);
				}
				n++;
				temp >>= 1;
			}
			m++;
			n = n - 4;
		}
	}
 
	@Override
	public void keyPressed(KeyEvent e) {
	}
 
	@Override
	public void keyReleased(KeyEvent e) {
	}
 
	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == 'a') {  
			if (running == false) {
				return;
			}
			if (y <= 1)  
				return;
			int temp = 0x8000;  
			for (int i = x; i < x + 4; i++) {  
				for (int j = y; j < y + 4; j++) {
					if ((rect & temp) != 0) {  
						if (data[i][j - 1] == 1) {  
							return;
						}
					}
					temp >>= 1;
				}
			}
			clear(x, y);  
			y--;
			draw(x, y);  
		}
		if (e.getKeyChar() == 'd') {  
			if (running == false) {
				return;
			}
			int temp = 0x8000;
			int m = x, n = y;
			int num = 7;
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if ((temp & rect) != 0) {
						if (n > num) {
							num = n;
						}
					}
					temp >>= 1;
					n++;
				}
				m++;
				n = n - 4;
			}
			if (num >= 10) {
				return;
			}
			temp = 0x8000;
			for (int i = x; i < x + 4; i++) {
				for (int j = y; j < y + 4; j++) {
					if ((rect & temp) != 0) {
						if (data[i][j + 1] == 1) {
							return;
						}
					}
					temp >>= 1;
				}
			}
			clear(x, y);  
			y++;
			draw(x, y);  
		}
		if (e.getKeyChar() == 's') {  
			if (running == false) {
				return;
			}
			if (canFall(x, y) == false) {
				saveData(x, y);
				return;
			}
			clear(x, y);  
			x++;
			draw(x, y);  
		}
		if (e.getKeyChar() == 'w') {  
			if (running == false) {
				return;
			}
			int i = 0;
			for (i = 0; i < allRect.length; i++) {  
				if (allRect[i] == rect)  
					break;
			}
			if (i == 0)  
				return;
			clear(x, y);
			if (i == 1 || i == 2) {  
				rect = allRect[i == 1 ? 2 : 1];
				if (y > 7)
					y = 7;
			}
			if (i >= 3 && i <= 6) {  
				rect = allRect[i + 1 > 6 ? 3 : i + 1];
			}
			if (i >= 7 && i <= 10) {  
				rect = allRect[i + 1 > 10 ? 7 : i + 1];
			}
			if (i == 11 || i == 12) {  
				rect = allRect[i == 11 ? 12 : 11];
			}
			if (i == 13 || i == 14) {  
				rect = allRect[i == 13 ? 14 : 13];
			}
			if (i >= 15 && i <= 18) {  
				rect = allRect[i + 1 > 18 ? 15 : i + 1];
			}
			draw(x, y);
		}
	}
 
}