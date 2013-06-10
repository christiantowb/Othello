import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Arrays;
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
//java C:\Users\Asus\Documents\GitHub\Othello\Othello.java
//javac C:\Users\Asus\Documents\GitHub\Othello\Othello.java
//javac C:\Users\zLx\Desktop\Othello\Othello.java (ctr+b sublime text 2)
//java C:\Users\zLx\Desktop\Othello\Othello
//to create jar, go to the othello directory and type : 'jar cvfm Othello.jar manifest.txt *.class images' (on windows cmd)

public class Othello extends JPanel {

	static int n = 8;
	static int size = 75;
	JLabel turnLabel = new JLabel("Turn : Black");
	JLabel colorLabel = new JLabel("Select YOUR Piece : ");
	JButton buttonArray[][]=new JButton[n][n];
	String turn = "Black";
	String playerColor = "White";
	String computerColor = "Black";
	int lastRow = 0;
	int lastCol = 0;
	ButtonGroup buttonGroupDiff = new ButtonGroup();
	ButtonGroup buttonGroupColor = new ButtonGroup();
	JRadioButton radioButtonBlack = new JRadioButton("Black");
	JRadioButton radioButtonWhite = new JRadioButton("White");
	JRadioButton radioButtonEasy = new JRadioButton("Easy");
    JRadioButton radioButtonNormal = new JRadioButton("Normal");
    JRadioButton radioButtonHard = new JRadioButton("Hard");
    JButton buttonStart = new JButton("Play !");
	Boolean blackCanMove = true;
	Boolean whiteCanMove = true;
	int totalBlack = 0;
	int totalWhite = 0;
	int totalViable = 0;
	int index = 1;
	JLabel piecesLabel = new JLabel("Black : " + totalBlack + " | White : " + totalWhite);
	OthelloPanel otPanel;
	DifficultyPanel dPanel;
	PiecesPanel pPanel;
	String difficulty = "";
	int globalDepth = 0;

	public Othello() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		otPanel = new OthelloPanel();
		otPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		radioButtonEasy.setSelected(true);
		buttonStart.addMouseListener( new MouseAdapter() {
       		public void mouseClicked(MouseEvent e) {
       			if(radioButtonEasy.isSelected()){
       				difficulty = "Easy";
       				globalDepth = 2;
       			}
       			else if(radioButtonNormal.isSelected()){
       				difficulty = "Normal";
       				globalDepth = 4;
       			}
       			else if(radioButtonHard.isSelected()){
       				difficulty = "Hard";
       				globalDepth = 6;
       			}

       			if(radioButtonBlack.isSelected()){
       				playerColor = "Black";
       				computerColor = "White";
       			}
       			else{
       				playerColor = "White";
       				computerColor = "Black";
       			}

       			if(index == 1){
					blackPossibilities(buttonArray);
					countPieces(buttonArray,true);
					radioButtonEasy.setEnabled(false);
					radioButtonNormal.setEnabled(false);
					radioButtonHard.setEnabled(false);
					radioButtonBlack.setEnabled(false);
					radioButtonWhite.setEnabled(false);
					buttonStart.setEnabled(false);
					if(computerColor == "Black") {
						State bestState;
						bestState = search(buttonArray, 0, 0, globalDepth, computerColor, turn);
						//do the state
						//System.out.println("Best State : " + bestState.getRow() + bestState.getCol());
						buttonArray[bestState.getRow()][bestState.getCol()].setName(computerColor);
						lastRow = bestState.getRow();
						lastCol = bestState.getCol();
		   				turnColors(bestState.getRow(),bestState.getCol(),"Black","images/Black.png",buttonArray);
		   				turnLabel.setText("Turn : White" + " | Last Move : " + lastRow + "," + lastCol);
		   				turn = "White";
		   				buttonArray[bestState.getRow()][bestState.getCol()].setIcon(new ImageIcon("images/Black.png"));
		   				changeTurn(buttonArray);
					}
				}
       		}
       	});
       	buttonGroupColor.add(radioButtonWhite);
       	buttonGroupColor.add(radioButtonBlack);
		buttonGroupDiff.add(radioButtonEasy);
		buttonGroupDiff.add(radioButtonNormal);
		buttonGroupDiff.add(radioButtonHard);
		pPanel = new PiecesPanel();
		dPanel = new DifficultyPanel();
		this.add(pPanel);
		this.add(dPanel);
		this.add(buttonStart);
		this.add(turnLabel);	
		this.add(piecesLabel);
		this.add(otPanel);
		this.displayOthelloPanel();
	}

	class DifficultyPanel extends JPanel {
		public DifficultyPanel() {
			this.add(radioButtonEasy);
			this.add(radioButtonNormal);
			this.add(radioButtonHard);
		}
	}

	class PiecesPanel extends JPanel {
		public PiecesPanel() {
			this.add(colorLabel);
			this.add(radioButtonBlack);
			this.add(radioButtonWhite);
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
	           				turnColors(x,y,"White","images/White.png",buttonArray);
	           				turnLabel.setText("Turn : Black");
	           				turn = "Black";
	           				checkButton.setIcon(new ImageIcon("images/White.png"));
	           			}
	           			else{
	           				checkButton.setName("Black");
	           				turnColors(x,y,"Black","images/Black.png",buttonArray);
	           				turnLabel.setText("Turn : White");
	           				turn = "White";
	           				checkButton.setIcon(new ImageIcon("images/Black.png"));	
	           			}

	           			buttonArray[x][y] = checkButton;
	           			changeTurn(buttonArray);
	           			checkWin(buttonArray);
           			}
           			else{	
           				//System.out.println(checkButton.getName() + " = " + x + "," + y);
           			}
          		}
        	});
        }
    }

	private String getTheName(int i, int j, JButton[][] arr){
		String temp = "Nothing";
		if(arr[i][j].getName() == "White"){
			temp = "White";
		}
		else if (arr[i][j].getName() == "Black") {
			temp = "Black";
		}
		else if (arr[i][j].getName() == "Viable") {
			temp = "Viable";
		}
		else if (arr[i][j].getName() == "Empty") {
			temp = "Empty";
		}
		return temp;
	}

	private ImageIcon getTheIcon(int i, int j, JButton[][] arr){
		if(arr[i][j].getName() == "White"){
			return new ImageIcon("images/White.png");
		}
		else if (arr[i][j].getName() == "Black") {
			return new ImageIcon("images/Black.png");
		}
		else if (arr[i][j].getName() == "Viable") {
			return new ImageIcon("images/Viable.png");
		}
		else if (arr[i][j].getName() == "Empty") {
			return null;
		}
		return null;
	}
	
	private int getDepth(int theDepth) {
		Integer d = new Integer(theDepth);
		return d - 1;
	}
	
	private State getTheState(State theState) {
		State newState = new State(0,0,0);
		for(int i=0;i<30;i++) {
			if(theState.getValue() == i) {
				newState.setValue(i);
			}
			if(theState.getRow() == i) {
				newState.setRow(i);
			}
			if(theState.getCol() == i) {
				newState.setCol(i);
			}
		}
		
		return newState;
	}
	
	private State search(JButton[][] array, int theRow, int theCol, int dep, String aiColor, String currentTurn)
	{
		JButton tempArray[][] = new JButton[n][n];
		JButton tempArrayTwo[][] = new JButton[n][n];
		for(int x=0;x<n;x++) {
			for(int y=0;y<n;y++){
				tempArray[x][y] = new OthelloButton(x,y);
				tempArray[x][y].setBackground(Color.green);
				tempArray[x][y].setName(getTheName(x,y,array));
				tempArray[x][y].setIcon(getTheIcon(x,y,array));
			}
		}
		for(int x=0;x<n;x++) {
			for(int y=0;y<n;y++){
				tempArrayTwo[x][y] = new OthelloButton(x,y);
				tempArrayTwo[x][y].setBackground(Color.green);
				tempArrayTwo[x][y].setName(getTheName(x,y,tempArray));
				tempArrayTwo[x][y].setIcon(getTheIcon(x,y,tempArray));
			}
		}
		countPieces(tempArray,false);
		//System.out.println("Black : " + totalBlack + " | White : " + totalWhite);
	    if (dep == 0 || totalWhite + totalBlack == n * n || blackCanMove == false && whiteCanMove == false){
	    	State tempState = new State(0,0,0);
	    	if(aiColor == currentTurn){
	    		if(aiColor == "Black"){
	    			tempState.setValue(totalWhite);
	    			tempState.setRow(theRow);
	    			tempState.setCol(theCol);
	    			return tempState;
	    		}
	    		else{
	    			tempState.setValue(totalBlack);
	    			tempState.setRow(theRow);
	    			tempState.setCol(theCol);
	    			return tempState;
	    		}
	    	}
	    	else{
	    		if(aiColor == "White"){
	    			tempState.setValue(totalWhite);
	    			tempState.setRow(theRow);
	    			tempState.setCol(theCol);
	    			return tempState;
	    		}
	    		else{
	    			tempState.setValue(totalBlack);
	    			tempState.setRow(theRow);
	    			tempState.setCol(theCol);
	    			return tempState;
	    		}
	    	}
	    }
	    else
	    {
	    	State getState = new State(0,0,0);
	        if(aiColor == currentTurn) //this is my move, i have to maximize my move in this scope.
	        {
	        	if(currentTurn=="White"){
	        		whitePossibilities(tempArray);
	        	}
	        	else{
	        		blackPossibilities(tempArray);
	        	}	

	        	//System.out.println("Total Viable (MaxState) : " + totalViable);
	        	State maxState = new State(-9999,-9999,-9999);
	            for(int i=0;i<n;i++){
					for(int j=0;j<n;j++){ 
		            	//basically im searching for the viable place. 
		                //make that move on s yielding a state s'
		                if(tempArray[i][j].getName() == "Viable"){
			                if(aiColor == "Black"){
			                	//System.out.println("Condition tempArray before : " + i + "," + j + " = " + tempArray[i][j].getName());
			                	////System.out.println("Condition buttonArray before : " + i + "," + j + " = " + buttonArray[i][j].getName());
			                	tempArray[i][j].setName("Black");
			                	//System.out.println("Condition tempArray : " + i + "," + j + " = " + tempArray[i][j].getName());
			                	////System.out.println("Condition buttonArray : " + i + "," + j + " = " + buttonArray[i][j].getName());
			                	turnColors(i,j,"Black","images/Black.png",tempArray);
			                	checkWin(tempArray);
			    				getState = search(tempArray,i,j,getDepth(dep),aiColor,"White"); //search(s', depth - 1)
			    			}
			    			else{
			    				//System.out.println("Condition tempArray before : " + i + "," + j + " = " + tempArray[i][j].getName());
			                	////System.out.println("Condition buttonArray before : " + i + "," + j + " = " + buttonArray[i][j].getName());
			    				tempArray[i][j].setName("White");
			    				//System.out.println("Condition tempArray : " + i + "," + j + " = " + tempArray[i][j].getName());
			                	////System.out.println("Condition buttonArray : " + i + "," + j + " = " + buttonArray[i][j].getName());
			                	turnColors(i,j,"White","images/White.png",tempArray);
			                	checkWin(tempArray);
			    				getState = search(tempArray,i,j,getDepth(dep),aiColor,"Black");//search(s', depth - 1)
			    			}

			    			//System.out.println("MaxState : " + maxState.getValue() + " | GetState : " + getState.getValue() + " | Depth : " + dep);
			    			if(getState.getValue() > maxState.getValue()){
			    				maxState.setValue(getTheState(getState).getValue());
								maxState.setRow(i);
								maxState.setCol(j);
			    			}
			    			//System.out.println("Rolling back .. i : " + i + ", j : " + j);
			    			for(int x=0;x<n;x++) {
								for(int y=0;y<n;y++){
									tempArray[x][y] = new OthelloButton(x,y);
									tempArray[x][y].setBackground(Color.green);
									tempArray[x][y].setName(getTheName(x,y,tempArrayTwo));
									tempArray[x][y].setIcon(getTheIcon(x,y,tempArrayTwo));
								}
							}
			    		}
		            }
		        }
	            
	            //System.out.println("Returning .. ");
	            return getTheState(maxState);
	        }
	        else //this is my opponent's move, i have to minimize their move in this scope.
	        {
				if(currentTurn=="White"){
	        		whitePossibilities(tempArray);
	        	}
	        	else{
	        		blackPossibilities(tempArray);
	        	}

	        	//System.out.println("Total Viable (MinState) : " + totalViable);.
	            State minState = new State(9999,9999,9999);
	            for(int i=0;i<n;i++){
					for(int j=0;j<n;j++){ 
		            	//basically im searching for the viable place. 
		                //make that move on s yielding a state s'
		                if(tempArray[i][j].getName() == "Viable"){
			                if(aiColor == "Black"){
			                	//System.out.println("Condition tempArray before : " + i + "," + j + " = " + tempArray[i][j].getName());
			                	////System.out.println("Condition buttonArray before : " + i + "," + j + " = " + buttonArray[i][j].getName());
			                	tempArray[i][j].setName("White");
			                	//System.out.println("Condition tempArray : " + i + "," + j + " = " + tempArray[i][j].getName());
			                	////System.out.println("Condition buttonArray : " + i + "," + j + " = " + buttonArray[i][j].getName());
			                	turnColors(i,j,"Black","images/Black.png",tempArray);
			                	//changeTurn(tempArray);
			                	//checkWin(tempArray);
			    				getState = search(tempArray,i,j,getDepth(dep),aiColor,"Black"); //search(s', depth - 1)
			    			}
			    			else{
			    				//System.out.println("Condition tempArray before : " + i + "," + j + " = " + tempArray[i][j].getName());
			                	////System.out.println("Condition buttonArray before : " + i + "," + j + " = " + buttonArray[i][j].getName());
			    				tempArray[i][j].setName("Black");
			    				//System.out.println("Condition tempArray : " + i + "," + j + " = " + tempArray[i][j].getName());
			                	////System.out.println("Condition buttonArray : " + i + "," + j + " = " + buttonArray[i][j].getName());
			                	turnColors(i,j,"White","images/White.png",tempArray);
			                	//changeTurn(tempArray);
			                	//checkWin(tempArray);
			    				getState = search(tempArray,i,j,getDepth(dep),aiColor,"White");//search(s', depth - 1)
			    			}

			    			//System.out.println("MinState : " + minState.getValue() + " | GetState : " + getState.getValue() + " | Depth : " + dep);
			    			if(getState.getValue() < minState.getValue()){
			    				minState.setValue(getTheState(getState).getValue());
								minState.setRow(i);
								minState.setCol(j);
			    			}
			    			//System.out.println("Rolling back .. i : " + i + ", j : " + j);
			    			for(int x=0;x<n;x++) {
								for(int y=0;y<n;y++){
									tempArray[x][y] = new OthelloButton(x,y);
									tempArray[x][y].setBackground(Color.green);
									tempArray[x][y].setName(getTheName(x,y,tempArrayTwo));
									tempArray[x][y].setIcon(getTheIcon(x,y,tempArrayTwo));
								}
							}
			    		}
		            }
		        }
	            //System.out.println("Returning .. ");
	            return getTheState(minState);
	        }
	    }
	}

	class State {
		private int value, r, c;
		public State(int val, int row, int col) {
			value = val;
			r = row;
			c = col;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int val){
			value = val;
		}

		public int getRow() {
			return r;
		}

		public void setRow(int row) {
			r = row;
		}

		public int getCol() {
			return c;
		}

		public void setCol(int col) {
			c = col;
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

	private void turnColors(int x, int y, String color, String iconName, JButton[][] array) {
		//System.out.println("Before Change : " + x + "," + y);
		checkWhereToTurn(x,y,1,1,color,iconName,array); // southeast
		checkWhereToTurn(x,y,1,-1,color,iconName,array); // southwest
		checkWhereToTurn(x,y,-1,1,color,iconName,array); // northeast
		checkWhereToTurn(x,y,-1,-1,color,iconName,array); // northwest
		checkWhereToTurn(x,y,1,0,color,iconName,array); // south
		checkWhereToTurn(x,y,-1,0,color,iconName,array); // north
		checkWhereToTurn(x,y,0,1,color,iconName,array); // east
		checkWhereToTurn(x,y,0,-1,color,iconName,array); // west
	}

	private void checkWhereToTurn(int x, int y, int xx, int yy, String color, String iconName, JButton[][] array) {
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
				if(array[row + changeRow][col + changeCol].getName() == color && viable){
					done = true;
					//System.out.println("Let's change!");
					changeTheColor(x,y,xx,yy,row,col,color,iconName,array);
					//System.out.println("Done changing.");
				}
				else if(array[row + changeRow][col + changeCol].getName() == "Viable") {
					done = true;
				}
				else if(array[row + changeRow][col + changeCol].getName() == "Empty") {
					done = true;
				}
				else if(array[row + changeRow][col + changeCol].getName() != color){
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

	private void changeTheColor(int x, int y, int xx, int yy, int currentRow, int currentCol, String color, String iconName, JButton[][] array) {
		int row = currentRow;
		int col = currentCol;
		int rowChange = xx * -1;
		int colChange = yy * -1;
		////System.out.println("First Change : " + row + "," + col + " | x : " + x + ", y : " + y);
		while(row != x || col != y){
			////System.out.println("Change the : " + row + "," + col + " | x : " + x + ", y : " + y);
			array[row][col].setName(color);
			array[row][col].setIcon(new ImageIcon(iconName));
			row = row + rowChange;
			col = col + colChange;
		}
	}

	private void changeTurn(JButton[][] array) {
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(array[i][j].getName()=="Viable"){
					//System.out.println("Nulling Rows : " + i + "," + j);
					array[i][j].setName("Empty");
					array[i][j].setIcon(null);
					array[i][j].setBackground(Color.green);
				}
			}
		}

		if(turn == "White"){
			whitePossibilities(array);

			if(computerColor == "White"){
				//System.out.println("Computer's Turn");
				State bestState;				
				bestState = search(buttonArray, 0, 0, globalDepth, computerColor, turn);

				//do the state
				//System.out.println("Best Row, col :" + bestState.getRow() + "," + bestState.getCol());
				array[bestState.getRow()][bestState.getCol()].setName(computerColor);
				lastRow = bestState.getRow();
				lastCol = bestState.getCol();
   				turnColors(bestState.getRow(),bestState.getCol(),"White","images/White.png",array);
   				turnLabel.setText("Turn : Black" + " | Last Move : " + lastRow + "," + lastCol);
   				turn = "Black";
   				countPieces(buttonArray,true);
   				array[bestState.getRow()][bestState.getCol()].setIcon(new ImageIcon("images/White.png"));
   				changeTurn(array);	
   				checkWin(array);
			}
		}
		else{
			blackPossibilities(array);

			if(computerColor == "Black"){
				//System.out.println("Computer's Turn");
				State bestState;
				bestState = search(buttonArray, 0, 0, globalDepth, computerColor, turn);
				//do the state
				array[bestState.getRow()][bestState.getCol()].setName(computerColor);
				lastRow = bestState.getRow();
				lastCol = bestState.getCol();
   				turnColors(bestState.getRow(),bestState.getCol(),"Black","images/Black.png",array);
   				turnLabel.setText("Turn : White" + " | Last Move : " + lastRow + "," + lastCol);
   				turn = "White";
   				countPieces(buttonArray,true);
   				array[bestState.getRow()][bestState.getCol()].setIcon(new ImageIcon("images/Black.png"));
   				changeTurn(array);
   				checkWin(array);
			}
		}
		index++;
	}

	private void checkWin(JButton[][] array) {
		String status = "None";

		countPieces(buttonArray,true);

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
			if(!blackCanMove && !whiteCanMove){
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
					changeTurn(array);
					checkWin(array);
				}
				else{
					blackCanMove = false;
					turn = "White";
					changeTurn(array);
					checkWin(array);
				}
			}
		}
		else{
			if(turn == "White"){
				whiteCanMove = true;
			}
			else{
				blackCanMove = true;
			}
		}
	}

	private void countPieces(JButton[][] array, boolean changeLabel) {
		int whi = 0;
		int bla = 0;
		int via = 0;

		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(array[i][j].getName()=="White"){
					whi++;
				}	
				else if(array[i][j].getName() == "Black"){
					bla++;
				}
				else if(array[i][j].getName() == "Viable") {
					via++;
				}
			}
		}

		totalWhite = whi;
		totalBlack = bla;
		totalViable = via;

		if(changeLabel){
			piecesLabel.setText("Black : " + totalBlack + " | White : " + totalWhite);
		}
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

    private void blackPossibilities(JButton[][] array) {
    	for(int i=0;i<n;i++){
    		for(int j=0;j<n;j++){
	    		if(buttonArray[i][j].getName()=="Black") {
	    			checkPossibilities(i,j,array,array[i][j].getName());
	    		}
	    	}
    	}
    }

    private void whitePossibilities(JButton[][] array) {
    	for(int i=0;i<n;i++){
    		for(int j=0;j<n;j++){
	    		if(buttonArray[i][j].getName()=="White") {
	    			checkPossibilities(i,j,array,array[i][j].getName());
	    		}
	    	}
    	}
    }

    private void checkPossibilities(int x, int y, JButton[][] array, String color){
    	if(x<2){
    		if(y<2){
    			checkEast(x,y,color,array);
    			checkSouthEast(x,y,color,array);
    			checkSouth(x,y,color,array);
    		}
    		else if(y>6){
    			checkWest(x,y,color,array);
    			checkSouthWest(x,y,color,array);
    			checkSouth(x,y,color,array);
    		}
    		else{
    			checkEast(x,y,color,array);
    			checkWest(x,y,color,array);
    			checkSouth(x,y,color,array);
    			checkSouthWest(x,y,color,array);
    			checkSouthEast(x,y,color,array);
    		}
    	}
    	else if(x>6){
    		if(y<2){
    			checkNorth(x,y,color,array);
    			checkNorthEast(x,y,color,array);
    			checkEast(x,y,color,array);
    		}
    		else if(y>6){
    			checkNorth(x,y,color,array);
    			checkNorthWest(x,y,color,array);
    			checkWest(x,y,color,array);
    		}
    		else{
    			checkNorth(x,y,color,array);
    			checkNorthEast(x,y,color,array);
    			checkNorthWest(x,y,color,array);
    			checkWest(x,y,color,array);
    			checkEast(x,y,color,array);
    		}
    	}
    	else if(y<2){
    		if(x<2){
    			checkEast(x,y,color,array);
    			checkSouthEast(x,y,color,array);
    			checkSouth(x,y,color,array);
    		}
    		else if(x>6){
    			checkNorth(x,y,color,array);
    			checkNorthEast(x,y,color,array);
    			checkEast(x,y,color,array);
    		}
    		else{
	    		checkNorth(x,y,color,array);
	    		checkSouth(x,y,color,array);
	    		checkNorthEast(x,y,color,array);
	    		checkSouthEast(x,y,color,array);
	    		checkEast(x,y,color,array);
	    	}
    	}
    	else if(y>6){
    		if(x<2){
    			checkWest(x,y,color,array);
    			checkSouthWest(x,y,color,array);
    			checkSouth(x,y,color,array);
    		}
    		else if(x>6){
    			checkNorth(x,y,color,array);
    			checkNorthWest(x,y,color,array);
    			checkWest(x,y,color,array);
    		}
    		else{
    			checkNorth(x,y,color,array);
    			checkNorthWest(x,y,color,array);
    			checkWest(x,y,color,array);
    			checkSouthWest(x,y,color,array);
    			checkSouth(x,y,color,array);
    		}
    	}
    	else{
    		checkNorth(x,y,color,array);
    		checkNorthEast(x,y,color,array);
    		checkEast(x,y,color,array);
    		checkSouthEast(x,y,color,array);
    		checkSouth(x,y,color,array);
    		checkSouthWest(x,y,color,array);
    		checkWest(x,y,color,array);
    		checkNorthWest(x,y,color,array);
    	}
    }

    private void checkWest(int x, int y, String color, JButton[][] array){
    	int yy = y;
    	boolean done = false;
    	boolean viable = false;

    	while(done == false && yy > 0){
	    	if(array[x][yy-1].getName() == color){
	    		done = true;
	    	}
	    	else if(array[x][yy-1].getName() == "Empty"){
	    		if(viable){
	    			array[x][yy-1].setName("Viable");
       				array[x][yy-1].setIcon(new ImageIcon("images/Viable.png"));
       				done = true;
	    		}
	    		else{
	    			done = true;
	    		}
	    	}
	    	else if(array[x][yy-1].getName() == "Viable"){
	    		done = true;
	    	}
	    	else if(array[x][yy-1].getName() != color){
	    		viable = true;
	    	}	
	    	yy--;
    	}
    }

    private void checkEast(int x, int y, String color, JButton[][] array){
    	int yy = y;
    	boolean done = false;
    	boolean viable = false;

    	while(done == false && yy < 7){
	    	if(array[x][yy+1].getName() == color){
	    		done = true;
	    	}
	    	else if(array[x][yy+1].getName() == "Empty"){
	    		if(viable){
	    			array[x][yy+1].setName("Viable");
       				array[x][yy+1].setIcon(new ImageIcon("images/Viable.png"));
       				done = true;
	    		}
	    		else{
	    			done = true;
	    		}
	    	}
	    	else if(array[x][yy+1].getName() == "Viable"){
	    		done = true;
	    	}
	    	else if(array[x][yy+1].getName() != color){
	    		viable = true;
	    	}	
	    	yy++;
    	}
    }

    private void checkNorth(int x, int y, String color, JButton[][] array){
    	int xx = x;
    	boolean done = false;
    	boolean viable = false;

    	while(done == false && xx > 0){
	    	if(array[xx-1][y].getName() == color){
	    		done = true;
	    	}
	    	else if(array[xx-1][y].getName() == "Empty"){
	    		if(viable){
	    			array[xx-1][y].setName("Viable");
       				array[xx-1][y].setIcon(new ImageIcon("images/Viable.png"));
       				done = true;
	    		}
	    		else{
	    			done = true;
	    		}
	    	}
	    	else if(array[xx-1][y].getName() == "Viable"){
	    		done = true;
	    	}
	    	else if(array[xx-1][y].getName() != color){
	    		viable = true;
	    	}	
	    	xx--;
    	}
    }

    private void checkSouth(int x, int y, String color, JButton[][] array){
    	int xx = x;
    	boolean done = false;
    	boolean viable = false;

    	while(done == false && xx < 7){
	    	if(array[xx+1][y].getName() == color){
	    		done = true;
	    	}
	    	else if(array[xx+1][y].getName() == "Empty"){
	    		if(viable){
	    			array[xx+1][y].setName("Viable");
       				array[xx+1][y].setIcon(new ImageIcon("images/Viable.png"));
       				done = true;
	    		}
	    		else{
	    			done = true;
	    		}
	    	}
	    	else if(array[xx+1][y].getName() == "Viable"){
	    		done = true;
	    	}
	    	else if(array[xx+1][y].getName() != color){
	    		viable = true;
	    	}	
	    	xx++;
    	}	
    }

    private void checkNorthWest(int x, int y, String color, JButton[][] array){
    	int xx = x;
    	int yy = y;
    	boolean done = false;
    	boolean viable = false;

    	while(done == false && xx > 0 && yy > 0){
	    	if(array[xx-1][yy-1].getName() == color){
	    		done = true;
	    	}
	    	else if(array[xx-1][yy-1].getName() == "Empty"){
	    		if(viable){
	    			array[xx-1][yy-1].setName("Viable");
       				array[xx-1][yy-1].setIcon(new ImageIcon("images/Viable.png"));
       				done = true;
	    		}
	    		else{
	    			done = true;
	    		}
	    	}
	    	else if(array[xx-1][yy-1].getName() == "Viable"){
	    		done = true;
	    	}
	    	else if(array[xx-1][yy-1].getName() != color){
	    		viable = true;
	    	}	
	    	xx--;
	    	yy--;
    	}	
    }

    private void checkNorthEast(int x, int y, String color, JButton[][] array){
    	int xx = x;
    	int yy = y;
    	boolean done = false;
    	boolean viable = false;

    	while(done == false && xx > 0 && yy < 7){
	    	if(array[xx-1][yy+1].getName() == color){
	    		done = true;
	    	}
	    	else if(array[xx-1][yy+1].getName() == "Empty"){
	    		if(viable){
	    			array[xx-1][yy+1].setName("Viable");
       				array[xx-1][yy+1].setIcon(new ImageIcon("images/Viable.png"));
       				done = true;
	    		}
	    		else{
	    			done = true;
	    		}
	    	}
	    	else if(array[xx-1][yy+1].getName() == "Viable"){
	    		done = true;
	    	}
	    	else if(array[xx-1][yy+1].getName() != color){
	    		viable = true;
	    	}	
	    	xx--;
	    	yy++;
    	}	
    }

    private void checkSouthWest(int x, int y, String color, JButton[][] array){
    	int xx = x;
    	int yy = y;
    	boolean done = false;
    	boolean viable = false;

    	while(done == false && xx < 7 && yy > 0){
	    	if(array[xx+1][yy-1].getName() == color){
	    		done = true;
	    	}
	    	else if(array[xx+1][yy-1].getName() == "Empty"){
	    		if(viable){
	    			array[xx+1][yy-1].setName("Viable");
       				array[xx+1][yy-1].setIcon(new ImageIcon("images/Viable.png"));
       				done = true;
	    		}
	    		else{
	    			done = true;
	    		}
	    	}
	    	else if(array[xx+1][yy-1].getName() == "Viable"){
	    		done = true;
	    	}
	    	else if(array[xx+1][yy-1].getName() != color){
	    		viable = true;
	    	}	
	    	xx++;
	    	yy--;
    	}		
    }

    private void checkSouthEast(int x, int y, String color, JButton[][] array){
    	int xx = x;
    	int yy = y;
    	boolean done = false;
    	boolean viable = false;

    	while(done == false && xx < 7 && yy < 7){
	    	if(array[xx+1][yy+1].getName() == color){
	    		done = true;
	    	}
	    	else if(array[xx+1][yy+1].getName() == "Empty"){
	    		if(viable){
	    			array[xx+1][yy+1].setName("Viable");
       				array[xx+1][yy+1].setIcon(new ImageIcon("images/Viable.png"));
       				done = true;
	    		}
	    		else{
	    			done = true;
	    		}
	    	}
	    	else if(array[xx+1][yy+1].getName() == "Viable"){
	    		done = true;
	    	}
	    	else if(array[xx+1][yy+1].getName() != color){
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