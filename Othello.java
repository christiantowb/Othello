import javax.swing.JButton;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;


//javac C:\Users\zLx\Desktop\Othello\Othello.java (ctr+b sublime text 2)
//java C:\Users\zLx\Desktop\Othello\Othello
//to create jar, go to the othello directory and type : 'jar cvfm Othello.jar manifest.txt *.class images' (on windows cmd)

public class Othello extends JPanel {

	static int n = 8;
	static int size = 75;
	JLabel turnLabel = new JLabel("Turn : Black");
	JButton buttonArray[][]=new JButton[n][n];
	String turn = "Black";
	Boolean blackCanMove = true;
	Boolean whiteCanMove = true;
	int totalBlack = 0;
	int totalWhite = 0;
	int totalViable = 0;
	int index = 1;
	JLabel piecesLabel = new JLabel("Black : " + totalBlack + "| White : " + totalWhite);
	OthelloPanel otPanel;

	public Othello() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		otPanel = new OthelloPanel();
		otPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.add(turnLabel);
		this.add(piecesLabel);
		this.add(otPanel);
		this.displayOthelloPanel();
		if(index == 1){
			blackPossibilities();
			countPieces();
		}
	}

	class OthelloPanel extends JPanel {
		public OthelloPanel() {
			super(new GridLayout(n,n));
			this.setPreferredSize(new Dimension(n*size, n*size));
			for(int i=0; i<n; i++){
				for(int j=0; j<n; j++){
					JButton btnOthello = new OthelloButton(i,j);
					if(i == 3 && j == 3 || i == 4 && j == 4){
						btnOthello.setName("White");
						btnOthello.setIcon(new ImageIcon("images/White.png"));
						this.add(btnOthello);
					}
					else if(i == 3 && j == 4 || i == 4 && j == 3){
						btnOthello.setName("Black");
						btnOthello.setIcon(new ImageIcon("images/Black.png"));
						this.add(btnOthello);
					}
					else{
						btnOthello.setName("Empty");
						this.add(btnOthello);
					}
					buttonArray[i][j] = btnOthello;
				}
			}			
		}

		private class OthelloButton extends JButton {
			private int x = 0;
			private int y = 0;

	        public OthelloButton(int i, int j) {
	            super(i + "," + j);
	            x = i;
	            y = j;
	            this.setOpaque(true);
	            this.setBorderPainted(true);
	            this.setBackground(Color.green);
	            this.addMouseListener( new MouseAdapter() {
	           		public void mouseClicked(MouseEvent e) {
	           			JButton checkButton = (JButton) e.getSource();
	           			if(checkButton.getName() == "Viable"){
		           			if(index % 2 == 0) {
		           				checkButton.setName("White");
		           				turnColors(x,y,"White","images/White.png");
		           				turnLabel.setText("Turn : Black");
		           				turn = "Black";
		           				checkButton.setIcon(new ImageIcon("images/White.png"));
		           			}
		           			else{
		           				checkButton.setName("Black");
		           				turnColors(x,y,"Black","images/Black.png");
		           				turnLabel.setText("Turn : White");
		           				turn = "White";
		           				checkButton.setIcon(new ImageIcon("images/Black.png"));	
		           			}
		           			buttonArray[x][y] = checkButton;
		           			changeTurn();
		           			checkWin();
	           			}
	           			else{	
	           				//System.out.println(checkButton.getName() + " = " + x + "," + y);
	           			}
	          		}
	        	});
	        }
	    }
	}

	private void turnColors(int x, int y, String color, String iconName) {
		//System.out.println("Before Change : " + x + "," + y);
		checkWhereToTurn(x,y,1,1,color,iconName); // southeast
		checkWhereToTurn(x,y,1,-1,color,iconName); // southwest
		checkWhereToTurn(x,y,-1,1,color,iconName); // northeast
		checkWhereToTurn(x,y,-1,-1,color,iconName); // northwest
		checkWhereToTurn(x,y,1,0,color,iconName); // south
		checkWhereToTurn(x,y,-1,0,color,iconName); // north
		checkWhereToTurn(x,y,0,1,color,iconName); // east
		checkWhereToTurn(x,y,0,-1,color,iconName); // west
	}

	private void checkWhereToTurn(int x, int y, int xx, int yy, String color, String iconName) {
		int row = x;
		int col = y;
		int changeRow = xx;
		int changeCol = yy;
		boolean done = false;
		boolean viable = false;

		while(!done && row >= 0 && row <= 7 && col >= 0 && col <= 7) {
			int checkRow = row + changeRow;
			int checkCol = col + changeCol;

			if(checkRow < 0 || checkRow > 7 || checkCol < 0 || checkCol > 7) {
				done = true;
			}
			else{
				if(buttonArray[row + changeRow][col + changeCol].getName() == color && viable){
					done = true;
					//System.out.println("Let's change!");
					changeTheColor(x,y,xx,yy,row,col,color,iconName);
					//System.out.println("Done changing.");
				}
				else if(buttonArray[row + changeRow][col + changeCol].getName() == "Viable") {
					done = true;
				}
				else if(buttonArray[row + changeRow][col + changeCol].getName() == "Empty") {
					done = true;
				}
				else if(buttonArray[row + changeRow][col + changeCol].getName() != color){
					int asd = row + changeRow;
					int dsa = col + changeCol;
					viable = true;
					//System.out.println("Where to change : " + asd + "," + dsa);
				}
				else{
					done = true;
				}
				row = row + changeRow;
				col = col + changeCol;
			}
		}
	}

	private void changeTheColor(int x, int y, int xx, int yy, int currentRow, int currentCol, String color, String iconName) {
		int row = currentRow;
		int col = currentCol;
		int rowChange = xx * -1;
		int colChange = yy * -1;
		//System.out.println("First Change : " + row + "," + col + " | x : " + x + ", y : " + y);
		while(row != x || col != y){
			//System.out.println("Change the : " + row + "," + col + " | x : " + x + ", y : " + y);
			buttonArray[row][col].setName(color);
			buttonArray[row][col].setIcon(new ImageIcon(iconName));
			row = row + rowChange;
			col = col + colChange;
		}
	}

	private void changeTurn() {
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(buttonArray[i][j].getName()=="Viable"){
					buttonArray[i][j].setName("Empty");
					buttonArray[i][j].setIcon(null);
					buttonArray[i][j].setBackground(Color.green);
				}
			}
		}

		if(turn == "White"){
			whitePossibilities();
		}
		else{
			blackPossibilities();
		}
		index++;
	}

	private void checkWin() {
		String status = "None";

		countPieces();

		if(totalBlack + totalWhite == n*n){
			if(totalBlack > totalWhite) {
				turnLabel.setText("Black Wins.");
				disableAll();
			}
			else if(totalBlack < totalWhite) {
				turnLabel.setText("White Wins.");
				disableAll();
			}
			else {
				turnLabel.setText("It's Effing Draw, Mate.");
				disableAll();
			}
		}
		else if(totalViable == 0) {
			if(!blackCanMove && ! whiteCanMove){
				if(totalBlack > totalWhite) {
					turnLabel.setText("Black Wins.");
					disableAll();
				}
				else if(totalBlack < totalWhite) {
					turnLabel.setText("White Wins.");
					disableAll();
				}
				else {
					turnLabel.setText("It's Effing Draw, Mate.");
					disableAll();
				}
			}
			else{
				if(turn == "White"){
					whiteCanMove = false;
					turn = "Black";
					changeTurn();
					checkWin();
				}
				else{
					blackCanMove = false;
					turn = "White";
					changeTurn();
					checkWin();
				}
			}
		}
	}

	private void countPieces() {
		int whi = 0;
		int bla = 0;
		int via = 0;

		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(buttonArray[i][j].getName()=="White"){
					whi++;
				}	
				else if(buttonArray[i][j].getName() == "Black"){
					bla++;
				}
				else if(buttonArray[i][j].getName() == "Viable") {
					via++;
				}
			}
		}

		totalWhite = whi;
		totalBlack = bla;
		totalViable = via;

		piecesLabel.setText("Black : " + totalBlack + " | White : " + totalWhite);
	}

	private void disableAll(){
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				buttonArray[i][j].addMouseListener(new MouseAdapter() {
	           		public void mouseClicked(MouseEvent e) {
	           		}
	           	});
			}
		}
	}

	private void enableAll(){
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				buttonArray[i][j].setEnabled(true);
			}
		}
	}

	private void displayOthelloPanel() {
        JFrame f = new JFrame("Othello");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private void blackPossibilities() {
    	for(int i=0;i<n;i++){
    		for(int j=0;j<n;j++){
	    		if(buttonArray[i][j].getName()=="Black") {
	    			checkPossibilities(i,j,buttonArray[i][j].getName());
	    		}
	    	}
    	}
    }

    private void whitePossibilities() {
    	for(int i=0;i<n;i++){
    		for(int j=0;j<n;j++){
	    		if(buttonArray[i][j].getName()=="White") {
	    			checkPossibilities(i,j,buttonArray[i][j].getName());
	    		}
	    	}
    	}
    }

    private void checkPossibilities(int x, int y, String color){
    	if(x<2){
    		if(y<2){
    			checkEast(x,y,color);
    			checkSouthEast(x,y,color);
    			checkSouth(x,y,color);
    		}
    		else if(y>6){
    			checkWest(x,y,color);
    			checkSouthWest(x,y,color);
    			checkSouth(x,y,color);
    		}
    		else{
    			checkEast(x,y,color);
    			checkWest(x,y,color);
    			checkSouth(x,y,color);
    			checkSouthWest(x,y,color);
    			checkSouthEast(x,y,color);
    		}
    	}
    	else if(x>6){
    		if(y<2){
    			checkNorth(x,y,color);
    			checkNorthEast(x,y,color);
    			checkEast(x,y,color);
    		}
    		else if(y>6){
    			checkNorth(x,y,color);
    			checkNorthWest(x,y,color);
    			checkWest(x,y,color);
    		}
    		else{
    			checkNorth(x,y,color);
    			checkNorthEast(x,y,color);
    			checkNorthWest(x,y,color);
    			checkWest(x,y,color);
    			checkEast(x,y,color);
    		}
    	}
    	else if(y<2){
    		if(x<2){
    			checkEast(x,y,color);
    			checkSouthEast(x,y,color);
    			checkSouth(x,y,color);
    		}
    		else if(x>6){
    			checkNorth(x,y,color);
    			checkNorthEast(x,y,color);
    			checkEast(x,y,color);
    		}
    		else{
	    		checkNorth(x,y,color);
	    		checkSouth(x,y,color);
	    		checkNorthEast(x,y,color);
	    		checkSouthEast(x,y,color);
	    		checkEast(x,y,color);
	    	}
    	}
    	else if(y>6){
    		if(x<2){
    			checkWest(x,y,color);
    			checkSouthWest(x,y,color);
    			checkSouth(x,y,color);
    		}
    		else if(x>6){
    			checkNorth(x,y,color);
    			checkNorthWest(x,y,color);
    			checkWest(x,y,color);
    		}
    		else{
    			checkNorth(x,y,color);
    			checkNorthWest(x,y,color);
    			checkWest(x,y,color);
    			checkSouthWest(x,y,color);
    			checkSouth(x,y,color);
    		}
    	}
    	else{
    		checkNorth(x,y,color);
    		checkNorthEast(x,y,color);
    		checkEast(x,y,color);
    		checkSouthEast(x,y,color);
    		checkSouth(x,y,color);
    		checkSouthWest(x,y,color);
    		checkWest(x,y,color);
    		checkNorthWest(x,y,color);
    	}
    }

    private void checkEast(int x, int y, String color){
    	int yy = y;
    	boolean done = false;
    	boolean viable = false;

    	while(done == false && yy > 0){
	    	if(buttonArray[x][yy-1].getName() == color){
	    		done = true;
	    	}
	    	else if(buttonArray[x][yy-1].getName() == "Empty"){
	    		if(viable){
	    			buttonArray[x][yy-1].setName("Viable");
       				buttonArray[x][yy-1].setIcon(new ImageIcon("images/Viable.png"));
       				done = true;
	    		}
	    		else{
	    			done = true;
	    		}
	    	}
	    	else if(buttonArray[x][yy-1].getName() == "Viable"){
	    		done = true;
	    	}
	    	else if(buttonArray[x][yy-1].getName() != color){
	    		viable = true;
	    	}	
	    	yy--;
    	}
    }

    private void checkWest(int x, int y, String color){
    	int yy = y;
    	boolean done = false;
    	boolean viable = false;

    	while(done == false && yy < 7){
	    	if(buttonArray[x][yy+1].getName() == color){
	    		done = true;
	    	}
	    	else if(buttonArray[x][yy+1].getName() == "Empty"){
	    		if(viable){
	    			buttonArray[x][yy+1].setName("Viable");
       				buttonArray[x][yy+1].setIcon(new ImageIcon("images/Viable.png"));
       				done = true;
	    		}
	    		else{
	    			done = true;
	    		}
	    	}
	    	else if(buttonArray[x][yy+1].getName() == "Viable"){
	    		done = true;
	    	}
	    	else if(buttonArray[x][yy+1].getName() != color){
	    		viable = true;
	    	}	
	    	yy++;
    	}
    }

    private void checkNorth(int x, int y, String color){
    	int xx = x;
    	boolean done = false;
    	boolean viable = false;

    	while(done == false && xx > 0){
	    	if(buttonArray[xx-1][y].getName() == color){
	    		done = true;
	    	}
	    	else if(buttonArray[xx-1][y].getName() == "Empty"){
	    		if(viable){
	    			buttonArray[xx-1][y].setName("Viable");
       				buttonArray[xx-1][y].setIcon(new ImageIcon("images/Viable.png"));
       				done = true;
	    		}
	    		else{
	    			done = true;
	    		}
	    	}
	    	else if(buttonArray[xx-1][y].getName() == "Viable"){
	    		done = true;
	    	}
	    	else if(buttonArray[xx-1][y].getName() != color){
	    		viable = true;
	    	}	
	    	xx--;
    	}
    }

    private void checkSouth(int x, int y, String color){
    	int xx = x;
    	boolean done = false;
    	boolean viable = false;

    	while(done == false && xx < 7){
	    	if(buttonArray[xx+1][y].getName() == color){
	    		done = true;
	    	}
	    	else if(buttonArray[xx+1][y].getName() == "Empty"){
	    		if(viable){
	    			buttonArray[xx+1][y].setName("Viable");
       				buttonArray[xx+1][y].setIcon(new ImageIcon("images/Viable.png"));
       				done = true;
	    		}
	    		else{
	    			done = true;
	    		}
	    	}
	    	else if(buttonArray[xx+1][y].getName() == "Viable"){
	    		done = true;
	    	}
	    	else if(buttonArray[xx+1][y].getName() != color){
	    		viable = true;
	    	}	
	    	xx++;
    	}	
    }

    private void checkNorthWest(int x, int y, String color){
    	int xx = x;
    	int yy = y;
    	boolean done = false;
    	boolean viable = false;

    	while(done == false && xx > 0 && yy > 0){
	    	if(buttonArray[xx-1][yy-1].getName() == color){
	    		done = true;
	    	}
	    	else if(buttonArray[xx-1][yy-1].getName() == "Empty"){
	    		if(viable){
	    			buttonArray[xx-1][yy-1].setName("Viable");
       				buttonArray[xx-1][yy-1].setIcon(new ImageIcon("images/Viable.png"));
       				done = true;
	    		}
	    		else{
	    			done = true;
	    		}
	    	}
	    	else if(buttonArray[xx-1][yy-1].getName() == "Viable"){
	    		done = true;
	    	}
	    	else if(buttonArray[xx-1][yy-1].getName() != color){
	    		viable = true;
	    	}	
	    	xx--;
	    	yy--;
    	}	
    }

    private void checkNorthEast(int x, int y, String color){
    	int xx = x;
    	int yy = y;
    	boolean done = false;
    	boolean viable = false;

    	while(done == false && xx > 0 && yy < 7){
	    	if(buttonArray[xx-1][yy+1].getName() == color){
	    		done = true;
	    	}
	    	else if(buttonArray[xx-1][yy+1].getName() == "Empty"){
	    		if(viable){
	    			buttonArray[xx-1][yy+1].setName("Viable");
       				buttonArray[xx-1][yy+1].setIcon(new ImageIcon("images/Viable.png"));
       				done = true;
	    		}
	    		else{
	    			done = true;
	    		}
	    	}
	    	else if(buttonArray[xx-1][yy+1].getName() == "Viable"){
	    		done = true;
	    	}
	    	else if(buttonArray[xx-1][yy+1].getName() != color){
	    		viable = true;
	    	}	
	    	xx--;
	    	yy++;
    	}	
    }

    private void checkSouthWest(int x, int y, String color){
    	int xx = x;
    	int yy = y;
    	boolean done = false;
    	boolean viable = false;

    	while(done == false && xx < 7 && yy > 0){
	    	if(buttonArray[xx+1][yy-1].getName() == color){
	    		done = true;
	    	}
	    	else if(buttonArray[xx+1][yy-1].getName() == "Empty"){
	    		if(viable){
	    			buttonArray[xx+1][yy-1].setName("Viable");
       				buttonArray[xx+1][yy-1].setIcon(new ImageIcon("images/Viable.png"));
       				done = true;
	    		}
	    		else{
	    			done = true;
	    		}
	    	}
	    	else if(buttonArray[xx+1][yy-1].getName() == "Viable"){
	    		done = true;
	    	}
	    	else if(buttonArray[xx+1][yy-1].getName() != color){
	    		viable = true;
	    	}	
	    	xx++;
	    	yy--;
    	}		
    }

    private void checkSouthEast(int x, int y, String color){
    	int xx = x;
    	int yy = y;
    	boolean done = false;
    	boolean viable = false;

    	while(done == false && xx < 7 && yy < 7){
	    	if(buttonArray[xx+1][yy+1].getName() == color){
	    		done = true;
	    	}
	    	else if(buttonArray[xx+1][yy+1].getName() == "Empty"){
	    		if(viable){
	    			buttonArray[xx+1][yy+1].setName("Viable");
       				buttonArray[xx+1][yy+1].setIcon(new ImageIcon("images/Viable.png"));
       				done = true;
	    		}
	    		else{
	    			done = true;
	    		}
	    	}
	    	else if(buttonArray[xx+1][yy+1].getName() == "Viable"){
	    		done = true;
	    	}
	    	else if(buttonArray[xx+1][yy+1].getName() != color){
	    		viable = true;
	    	}	
	    	xx++;
	    	yy++;
    	}		
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Othello();
            }
        });
    }
}