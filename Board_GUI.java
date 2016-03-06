import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;



public class Board_GUI extends JFrame{
	//GUI Definitions
	JFrame wel_win = new JFrame("Welcome");
	JPanel wel_area = new JPanel();
	JLabel wel_welcome_lbl = new JLabel("Greatings...dear User, hope you enjoy your time with us");
	JLabel wel_selection_lbl = new JLabel();
	JTextField wel_selection_size = new JTextField(20);
	JLabel wel_selection_lbl2 = new JLabel();
	JTextField wel_selection_numOfPlayers = new JTextField(5);
	JButton save_options = new JButton("Proceed");
	static JFrame game_win = new JFrame();
	
	static ArrayList<JPanel> game_board = new ArrayList<JPanel>();
	static JPanel tile = new JPanel();
	static JLabel tile_lbl = new JLabel();
	
	
	static JPanel tile_map = new JPanel();
	
	static JButton play = new JButton("Play");
	
	static JTextArea console = new JTextArea(5,30);
	static JScrollPane console_sc = new JScrollPane(console);
	
	static JPanel pl1 = new JPanel();
	static JPanel pl2 = new JPanel();
	static JPanel pl3 = new JPanel();
	static JPanel pl4 = new JPanel();
	
	static JPanel snake_head = new JPanel();
	static JPanel ladder_head = new JPanel();
	
	static JMenuBar mb = new JMenuBar();
	static JMenuItem refresh = new JMenuItem("Refresh");
	static JMenuItem restart = new JMenuItem("Restart");
	static JMenuItem about = new JMenuItem("About");
	
	//End of GUI Definitions
	
	//Control Vars
	private static int size = 30;
	private static int numOfPlayers = 4;
	private static int turn = 1;
	private static int[] loc;
	private static int winner = 0;
	private static String[] playerNames;
	private static int[][] snakes;
	private static int[][] ladders;
	private static int count = 0;
	
	//End of Control Vars
	
	public Board_GUI(){
		wel_win.setDefaultCloseOperation(EXIT_ON_CLOSE);
		wel_win.add(wel_welcome_lbl,BorderLayout.NORTH);
		wel_selection_lbl2.setText("How many players?");
		wel_selection_lbl.setText("How many tiles?");
		wel_area.setLayout(new GridLayout(0,1));
		wel_area.add(wel_selection_lbl);
		wel_area.add(wel_selection_size);
		wel_selection_size.setToolTipText("Multiples of 10, up to 100, more than that would be chaotic");
		wel_area.add(wel_selection_lbl2);
		wel_area.add(wel_selection_numOfPlayers);
		wel_selection_numOfPlayers.setToolTipText("2 to 4 Players");
		wel_area.add(save_options);
		save_options.addActionListener(button_listener);
		wel_win.add(wel_area,BorderLayout.CENTER);
	
		wel_win.pack();
		wel_win.setVisible(true);
	}
	
	
	
	
	
	
	//Listeners
	ActionListener button_listener = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == save_options){
				if(!wel_selection_size.getText().isEmpty() && !wel_selection_numOfPlayers.getText().isEmpty()){
					size = Integer.parseInt(wel_selection_size.getText());
					numOfPlayers = Integer.parseInt(wel_selection_numOfPlayers.getText());
					playerNames = new String[numOfPlayers];
					for(int i = 0;  i < playerNames.length ; i++){
						playerNames[i] = JOptionPane.showInputDialog(null,String.format("Player %d, Choose a Game Name for yourself: ", i+1),"Names",JOptionPane.PLAIN_MESSAGE);
						if(playerNames[i] == "" || playerNames[i] == null)
							playerNames[i] = String.format("Player %d",i+1);
					}
					pl1.setToolTipText(String.format("%s", playerNames[0]));
					pl2.setToolTipText(String.format("%s", playerNames[1]));
					if(numOfPlayers >= 3)
						pl3.setToolTipText(String.format("%s", playerNames[2]));
					if(numOfPlayers >= 4){
						pl4.setToolTipText(String.format("%s", playerNames[3]));
					}
					
					if(size%10 == 0 && size <= 100 && numOfPlayers > 0 && numOfPlayers < 5){
						startABoard();
						//Start the location array
						loc = new int[numOfPlayers];
						for(int i = 0; i <loc.length ; i++){
							loc[i] = 0;
						}
						//Define and place Snakes and Ladders
						snakes = new int[size/10][2];
						ladders = new int[size/10][2];
						JOptionPane.showMessageDialog(null, String.format("Beware of %d hidden Snakes,%nAnd Good luck in finding %d ladders", snakes.length,ladders.length,"Attention!",JOptionPane.INFORMATION_MESSAGE));
						
						for(int i = 0; i < snakes.length ; i++){
							count = 0;
							int head = (int) (Math.round(Math.random()*size));
							snakes[i][0] = (int) (Math.round(Math.random()*(size - 1))+ 1);
							if(snakes[i][0] <= 0 || snakes[i][0] > size-2)
									snakes[i][0] = (int) (Math.round(Math.random()*size)-1);
							snakes[i][1] = (int) (Math.round(Math.random()*snakes[i][0]));
							if(snakes[i][1] >= snakes[i][0])
									snakes[i][1] = (int) (Math.round(Math.random()*snakes[i][0])-1);
							
							
							System.out.printf("Snake head: %d, tail: %d%n", snakes[i][0], snakes[i][1]);
								
							
							
							
						}
						
						for(int i = 0; i < ladders.length ; i++){
							count = 0;
							
								int head = (int) (Math.round(Math.random()*size));
								ladders[i][0] = head;
								if(ladders[i][0] <= 0 || ladders[i][0] > size - 3 )
									ladders[i][0] = (int) (Math.round(Math.random()*size));
								ladders[i][1] = (int) (Math.round(Math.random()*ladders[i][0])-1);
								if(ladders[i][1] >= ladders[i][0] || ladders[i][1] < 0 )
									ladders[i][1] = (int) (Math.round(Math.random()*ladders[i][0])-1);
								count++;
								System.out.printf("Ladder head: %d, tail: %d%n", ladders[i][0], ladders[i][1]);
							
							
						}
						
						//Check for Snake & Ladder Conflicts
						for(int  i = 0; i < snakes.length ; i++){
							for(int j = 0; j < ladders.length ; j++){
								if(snakes[i][0] == ladders[j][0] || snakes[i][1] == ladders[j][1] || snakes[i][0] == ladders[j][1]){
									if(snakes[i][0] < 0 && snakes[i][0] > size-2)
										snakes[i][0] = (int) (Math.round(Math.random()*size)-1);
									snakes[i][1] = (int) (Math.round(Math.random()*snakes[i][0])-1);
									if(snakes[i][1] > snakes[i][0] && snakes[i][1] < 0 && snakes[i][1] == snakes[i][0])
										snakes[i][1] = (int) (Math.round(Math.random()*snakes[i][0])-1);
									System.out.printf("Snake head: %d, tail: %d%n", snakes[i][0], snakes[i][1]);
									
								}
							}
						}
						
						wel_win.dispose();
					}
				}else
					JOptionPane.showMessageDialog(null, "Two Numbers!, No Difficult!","Error",JOptionPane.ERROR_MESSAGE);
			}
		}
	};
	
	static ActionListener play_lis = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			
			if(e.getSource() == play){
				update();
				if(turn < numOfPlayers)
					turn ++;
				else
					turn = 1;
				console.append(String.format("%nPlayer %s,Your turn,", playerNames[turn-1]));
				
				//System.out.printf("%d%n",game_board.size());
				
			}
		}
	};
	
	static ActionListener menu_lis = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == refresh ){
				game_win.dispose();
				game_win.setVisible(false);
				new Board_GUI();
			}else if(e.getSource() == restart){
				for(int i = 0; i < loc.length ; i++){
					loc[i] = 0;
					tile_map.updateUI();
				}
				
				
			}else if(e.getSource() == about){
				JOptionPane.showMessageDialog(null, String.format("Developer: Albaraa \"Vonrex\" Bajnaid" ),"Team",JOptionPane.INFORMATION_MESSAGE);
			}
		}
	};
	
	
	
	//End of Listeners
	
	
	//Methods
	
	public static void update(){
		//JPanel pl = new JPanel();
		if(winner == 0){
			if(turn == 1){
				move();
				int loc_p = loc[0];
				if(loc_p >= game_board.size()-1){
					winner = 1;
					win();
				}
				pl1.getGraphics();
				pl1.repaint();
				pl1.setBackground(Color.YELLOW);
				game_board.get(loc_p).add(pl1);
				
				
					for(int i = 0; i < snakes.length ; i++){
						if(loc_p == snakes[i][0]){
							console.append(String.format("You stumbled upon a Snake,%n you ran to the end of his tail at tile: %d  %n", snakes[i][1]+1));
							game_board.get(loc_p).remove(pl1);
							
							ImageIcon snake = new ImageIcon("res/snake_head_trans.png");
							JLabel snake_lbl = new JLabel();
							snake_lbl.setIcon(snake);
							snake_head.add(snake_lbl);
							game_board.get(loc_p).add(snake_head);
							loc[0] = snakes[i][1];
							
							break;
						}
					}
					
					for(int i = 0; i < ladders.length ; i++){
						if(loc_p == ladders[i][1]){
							console.append(String.format("You found a Ladder,%n you climbed it to reach tile: %d  %n", ladders[i][0]+1));
							game_board.get(loc_p).remove(pl1);
							ImageIcon ladder = new ImageIcon("res/ladder_trans.png");
							JLabel ladder_lbl = new JLabel();
							ladder_lbl.setIcon(ladder);
							ladder_head.add(ladder_lbl);
							game_board.get(loc_p).add(ladder_head);
							loc[0] = ladders[i][0];
							
							break;
						}
					}
					
					loc_p = loc[0];
					pl1.getGraphics();
					pl1.repaint();
					pl1.setBackground(Color.YELLOW);
					game_board.get(loc_p).add(pl1);
				
				tile_map.updateUI();
				
				
			}else if(turn == 2){
				move();
				int loc_p = loc[1];
				if(loc_p >= game_board.size()-1){
					winner = 2;
					win();
				}
				pl2.getGraphics();
				pl2.repaint();
				pl2.setBackground(Color.CYAN);
				game_board.get(loc_p).add(pl2);
				for(int i = 0; i < snakes.length ; i++){
					if(loc_p == snakes[i][0]){
						console.append(String.format("You stumbled upon a Snake,%n you ran to the end of his tail at tile: %d  %n", snakes[i][1]+1));
						game_board.get(loc_p).remove(pl1);
						
						ImageIcon snake = new ImageIcon("res/snake_head_trans.png");
						JLabel snake_lbl = new JLabel();
						snake_lbl.setIcon(snake);
						snake_head.add(snake_lbl);
						game_board.get(loc_p).add(snake_head);
						loc[1] = snakes[i][1];
						break;
					}
				}
				
				for(int i = 0; i < ladders.length ; i++){
					if(loc_p == ladders[i][1]){
						console.append(String.format("You found a Ladder,%n you climbed it to reach tile: %d  %n", ladders[i][0]+1));
						game_board.get(loc_p).remove(pl1);
						ImageIcon ladder = new ImageIcon("res/ladder_trans.png");
						JLabel ladder_lbl = new JLabel();
						ladder_lbl.setIcon(ladder);
						ladder_head.add(ladder_lbl);
						game_board.get(loc_p).add(ladder_head);
						loc[1] = ladders[i][0];
						
						break;
					}
				}
				loc_p = loc[1];
				pl2.getGraphics();
				pl2.repaint();
				pl2.setBackground(Color.CYAN);
				game_board.get(loc_p).add(pl2);
				
				tile_map.updateUI();
				
				
			}else if(turn == 3 && numOfPlayers >= 3){
				move();
				int loc_p = loc[2];
				if(loc_p >= game_board.size()-1){
					winner = 3;
					win();
				}
				pl3.getGraphics();
				pl3.repaint();
				pl3.setBackground(Color.MAGENTA);
				game_board.get(loc_p).add(pl3);
				tile_map.updateUI();
				
				for(int i = 0; i < snakes.length ; i++){
					if(loc_p == snakes[i][0]){
						console.append(String.format("You stumbled upon a Snake,%n you ran to the end of his tail at tile: %d  %n", snakes[i][1]+1));
						game_board.get(loc_p).remove(pl1);
						
						ImageIcon snake = new ImageIcon("res/snake_head_trans.png");
						JLabel snake_lbl = new JLabel();
						snake_lbl.setIcon(snake);
						snake_head.add(snake_lbl);
						game_board.get(loc_p).add(snake_head);
						loc[2] = snakes[i][1];
						break;
					}
				}
				
				for(int i = 0; i < ladders.length ; i++){
					if(loc_p == ladders[i][1]){
						console.append(String.format("You found a Ladder,%n you climbed it to reach tile: %d  %n", ladders[i][0]+1));
						game_board.get(loc_p).remove(pl1);
						ImageIcon ladder = new ImageIcon("res/ladder_trans.png");
						JLabel ladder_lbl = new JLabel();
						ladder_lbl.setIcon(ladder);
						ladder_head.add(ladder_lbl);
						game_board.get(loc_p).add(ladder_head);
						loc[2] = ladders[i][0];
						
						break;
					}
				}
				loc_p = loc[2];
				pl2.getGraphics();
				pl2.repaint();
				pl2.setBackground(Color.CYAN);
				game_board.get(loc_p).add(pl2);
			
				tile_map.updateUI();
				
				
			}else if(turn == 4 && numOfPlayers >= 4){
				move();
				int loc_p = loc[3];
				if(loc_p >= game_board.size()-1){
					winner = 4;
					win();
				}
				pl4.getGraphics();
				pl4.repaint();
				pl4.setBackground(Color.ORANGE);
				game_board.get(loc_p).add(pl4);
				
				for(int i = 0; i < snakes.length ; i++){
					if(loc_p == snakes[i][0]){
						console.append(String.format("You stumbled upon a Snake,%n you ran to the end of his tail at tile: %d  %n", snakes[i][1]+1));
						game_board.get(loc_p).remove(pl1);
						
						ImageIcon snake = new ImageIcon("res/snake_head_trans.png");
						JLabel snake_lbl = new JLabel();
						snake_lbl.setIcon(snake);
						snake_head.add(snake_lbl);
						game_board.get(loc_p).add(snake_head);
						loc[3] = snakes[i][1];
						break;
					}
				}
				
				for(int i = 0; i < ladders.length ; i++){
					if(loc_p == ladders[i][1]){
						console.append(String.format("You found a Ladder,%n you climbed it to reach tile: %d  %n", ladders[i][0]+1));
						game_board.get(loc_p).remove(pl1);
						ImageIcon ladder = new ImageIcon("res/ladder_trans.png");
						JLabel ladder_lbl = new JLabel();
						ladder_lbl.setIcon(ladder);
						ladder_head.add(ladder_lbl);
						game_board.get(loc_p).add(ladder_head);
						loc[3] = ladders[i][0];
						
						break;
					}
				}

				loc_p = loc[3];
				pl2.getGraphics();
				pl2.repaint();
				pl2.setBackground(Color.CYAN);
				game_board.get(loc_p).add(pl2);
				
				tile_map.updateUI();
			}
		}else
			win();
	}
	
	public static void win(){
		game_win.dispose();
		JOptionPane.showMessageDialog(null, String.format("The Winner is ..... %s%nWelldone!,%nbuy yourself an IceCream,%nChocolate perhaps?", playerNames[winner-1]),"Winner",JOptionPane.INFORMATION_MESSAGE);
		System.exit(1);
	}
	
	public static void startABoard(){
		JFrame game_win = new JFrame("Snake & Ladder");
		refresh.setToolTipText("To run a new Instance of the game, use it if the game gut stuck");
		mb.add(restart);
		restart.setToolTipText("Reset the game, and Start again");
		mb.add(refresh);
		mb.add(about);
		about.addActionListener(menu_lis);
		restart.addActionListener(menu_lis);
		refresh.addActionListener(menu_lis);
		game_win.setJMenuBar(mb);
		game_win.setDefaultCloseOperation(EXIT_ON_CLOSE);
		tile_map.setLayout(new GridLayout(0,10,2,2));
		for(int i = 0; i < size; i++){
			JPanel jtile = new JPanel();
			JLabel jtile_lbl = new JLabel(String.format("%d", i+1));
			tile_lbl.setText(String.format("%d",i+1));
			if(i%2 == 0 ){
				jtile.setBackground(Color.WHITE);
				jtile_lbl.setForeground(Color.BLACK);
			}else{
				jtile.setBackground(Color.GRAY);
				jtile_lbl.setForeground(Color.WHITE);
			}
			jtile.setBorder(new LineBorder(Color.BLACK,1));
			jtile.add(jtile_lbl);
			jtile.setSize(150,150);
			jtile.setPreferredSize(new Dimension(150,150));
			game_board.add(jtile);
		}
		System.out.printf("%d%n",game_board.size());
		for(int i = 0; i <game_board.size(); i++){
			//System.out.printf("%d%n",game_board.get(i).getComponentCount());
			tile_map.add(game_board.get(i));
			System.out.printf("%d%n",i);
		}
		
		//tile_map.add(new JLabel("BAAM!"));
		System.out.printf("%d%n",tile_map.getComponentCount());
		game_win.add(tile_map,BorderLayout.CENTER);
		game_win.add(console_sc,BorderLayout.EAST);
		console.setEditable(false);
		console.setBackground(Color.WHITE);
		game_win.add(play,BorderLayout.SOUTH);
		play.addActionListener(play_lis);
		console.append(String.format("%s, Start the Game, click Play%n", playerNames[turn-1]));
		game_win.pack();
		
		game_win.setVisible(true);
		
	}
	
	public static void move(){
		
		int step =  (int) (Math.round((Math.random() * 5))+ 1);
		console.append(String.format("You got %d%n", step));
		System.out.printf("%d%n", step);
		if(turn == 1){
			int location = loc[0];
			game_board.get(location).remove(pl1);;
			loc[0] += step;
			if(loc[0] >= 99){
				winner = 1;
			}
		}else if(turn == 2){
			int location = loc[1];
			game_board.get(location).remove(pl2);
			loc[1] += step;
			if(loc[0] >= 99){
				winner = 1;
			}
		}else if (turn == 3 && numOfPlayers >= 3){
			int location = loc[2];
			game_board.get(location).remove(pl3);
			loc[2] += step;
			if(loc[0] >= 99){
				winner = 1;
			}
		}else if(turn == 4 && numOfPlayers >= 4){
			int location = loc[3];
			game_board.get(location).remove(pl4);
			loc[3] += step;
			if(loc[0] >= 99){
				winner = 1;
			}
		}
		
		
	}
	
	public void paintComponent(Graphics g){
		if(turn == 1){
			g.setColor(Color.YELLOW);
			g.drawOval(this.getWidth()/2 - 11, this.getHeight()/2 - 11, 10, 10);
		}else if(turn == 2){
			g.setColor(Color.CYAN);
			g.drawOval(this.getWidth()/2 - 11, this.getHeight()/2 , 10, 10);
		}else if(turn == 3){
			g.setColor(Color.MAGENTA);
			g.drawOval(this.getWidth()/2 , this.getHeight()/2 - 11 , 10, 10);
		}else if(turn == 4){
			g.setColor(Color.ORANGE);
			g.drawOval(this.getWidth()/2 , this.getHeight()/2 , 10, 10);
		}
		
		
		
		
		/*if(numOfPlayers == 2){
			g.setColor(Color.YELLOW);
			g.drawOval(game_board.get(0).getWidth(), game_board.get(3).getHeight(), 2, 2);
			g.setColor(Color.CYAN);
			g.drawOval(game_board.get(0).getWidth()-3, game_board.get(3).getHeight()-3, 2, 2);
		}else if(numOfPlayers == 3){
			g.setColor(Color.YELLOW);
			g.drawOval(game_board.get(0).getWidth(), game_board.get(3).getHeight(), 2, 2);
			g.setColor(Color.CYAN);
			g.drawOval(game_board.get(0).getWidth()-3, game_board.get(3).getHeight()-3, 2, 2);
			g.setColor(Color.MAGENTA);
			g.drawOval(game_board.get(0).getWidth(), game_board.get(3).getHeight()-3, 2, 2);
		}else if(numOfPlayers == 4){
			g.setColor(Color.YELLOW);
			g.drawOval(game_board.get(0).getWidth(), game_board.get(3).getHeight(), 2, 2);
			g.setColor(Color.CYAN);
			g.drawOval(game_board.get(0).getWidth()-3, game_board.get(3).getHeight()-3, 2, 2);
			g.setColor(Color.MAGENTA);
			g.drawOval(game_board.get(0).getWidth(), game_board.get(3).getHeight()-3, 2, 2);
			g.setColor(Color.ORANGE);
			g.drawOval(game_board.get(0).getWidth()-3, game_board.get(3).getHeight(), 2, 2);
		}*/
	}
	
	
	//End Of Methods Section
	
	
	//Main Method
	public static void main(String[] a){
		new Board_GUI();
		
		
	}
	
	
	
	
	//Threads
	/*
	Runnable play_runnable = new Runnable(){
		public void run(){
			if(win == 0){
				console.append(String.format());
			}else
		}
	};
	*/
	
	
	//Override Chunk
	
	public void actionPerformed(ActionEvent e){
		
	}
	
}
