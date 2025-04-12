package org.openjfx;
import java.io.FileInputStream;
import java.util.Map.Entry;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.ActionEvent; 
import javafx.event.EventHandler;

class Time{
	private int min1;
	private int min2;
	private int sec1;
	private int sec2;
	Time(int m2,int m1,int s1,int s2){
		min1 = m1;min2=m2;sec2=s1;sec1=s2;
	}
	public String toString() { return min2+""+min1+":"+sec2+sec1;}
	void reduce() {
		sec1--;
		if (sec1==-1) {
   		 	sec2--; // 10s digit of second
   		 	sec1=9;
   		 	if (sec2==-1) {
   			 	sec2=5;
   			 	sec1=9;
   			 	min1--;
   			 	if (min1==-1) {
   			 		min1=9;
   			 		min2--;
   			 	}
   		 	}
   	 	}
	}
	void sethigh() {
		min1=9;min2=9;sec1=9;sec2=9;
	}
	boolean isZero() {
		return (min2==0 && min1==0 && sec1==0 && sec2==0);
	}
}
class Movers implements EventHandler<ActionEvent>{
	int x,y;
	Button white[][],black[][],dot[]; // moving gui , not move logic
	board_array BOARD;
	static int turn = 1;
	Movers(){
		turn=1;
	}
	void init(int x,int y, Button b[][], Button c[][], Button d[], board_array B) { 
		this.x=x;
		this.y=y;
		white=b;
		black=c;
		dot=d;
		BOARD  = B;
	}
	static int get() {
		return turn;
	}
	public void handle(ActionEvent e) {
		int tx=0,ty=0;
		for(int i=0; i<64;i++) {
			if (dot[i]==e.getSource()) { //konsa button click hua
				tx = i%8;
				ty = i/8;
			}
			dot[i].setVisible(false);  // non visible
			dot[i].setDisable(true); // non clickable
		}
		int moved = BOARD.move(new Pair<Integer,Integer>(y,x), new Pair<Integer,Integer>(ty,tx),((BOARD.m.get(y*8+x).first.id==1)&& (ty==((7)*(2-BOARD.m.get(y*8+x).first.team)))));
    	if (moved==1) {
    		Boardgui.set_image();
    		if (turn==2) { // attaching to the
    			String moveText = formatMove(y,x,ty,tx);
        		Boardgui.textArea2.appendText("\n"+moveText);
    			turn=1;
    		}
    		else {
    			String moveText = formatMove(y,x,ty,tx);
        		Boardgui.textArea.appendText("\n"+moveText);
    			turn = 2;
    		}
    		if (BOARD.result!=-1) {
    			Rectangle rectangle = new Rectangle();
    			rectangle.setWidth(500.0f); 
                rectangle.setHeight(500.0f);
                rectangle.setFill(Color.AQUA);
                StackPane sp2 = new StackPane(rectangle);
                Label resy = new Label();
                resy.setFont(Font.font("Arial",100));
                VBox vb = new VBox(100);
                vb.getChildren().add(resy);
                Button exitb = new Button("Main Menu");
        	    exitb.setMaxSize(200, 150);
        	    exitb.setFont(Font.font("Sans-serif",18));
        		exitb.setStyle("-fx-background-color: #008080;");
        	    vb.getChildren().add(exitb);
        	    vb.setAlignment(Pos.CENTER);
        	    EventHandler<ActionEvent> gobacktomain = new EventHandler<ActionEvent>() { 
        	        public void handle(ActionEvent e) 
        	        { 
        	            menu.screen1(Boardgui.stagey);       
        	        } 
        	    }; 
        	    exitb.setOnAction(gobacktomain);
                sp2.getChildren().add(vb);
    			if (BOARD.result==0) {
     				resy.setText("Draw");
     			}
     			else if (BOARD.result==1) {
     				resy.setText("White wins");
     			}
     			else if (BOARD.result==2) {
     				resy.setText("Black wins");
     			}
    			StackPane sp = new StackPane(Boardgui.scenary.getRoot());
    			sp.getChildren().add(sp2);
    			sp.setAlignment(Pos.CENTER);
    			Boardgui.scenary.setRoot(sp);
    		}
    	}
	}
	private String formatMove(int y,int x, int ty, int tx)
	{
		char sx = (char)('A'+x);
		int sy = y+1;
		char ex = (char)('A'+tx);
		int ey = ty+1;
		String s = (sx+""+sy+" to "+ex+""+ey);
		System.out.println(s);
		return(s);
	}
}
class Pieces_button implements EventHandler<ActionEvent>{
	Button black[][], white[][], blue_dots[];
	board_array Board;
	Movers bruhhh;
	void init(Button b[][], Button w[][], Button bbb[],board_array bb, Movers movery) {
		black=b;
		white=w;
		blue_dots=bbb;
		Board = bb;
		bruhhh=movery;
	}
	public void handle(ActionEvent e) {
		Master : for(int i=0;i<64;i++) {
			for(int j=0; j<6;j++) {
				if ((e.getSource()==black[i][j]) || (e.getSource()==white[i][j]) ){
					for(int ii=0;ii<64;ii++) {
						blue_dots[ii].setVisible(false);
						blue_dots[ii].setDisable(true);
					}
					if (Movers.get()==Board.m.get(i).first.team) {
						for (Pair<Integer, Integer> i1 : Board.m.get(i).first.legals) {
							blue_dots[i1.first*8+i1.second].setVisible(true);
							blue_dots[i1.first*8+i1.second].setDisable(false);
						}
						bruhhh.init(i%8,i/8,black,white,blue_dots,Board);
					}
					break Master;
				}
			}
		}
	}
}
// key : value
// i cant do pair<int,int> ill had to change hash code., made the i,j coordinate into 1 integer, to represent that coordinate, i = y*8+x.
// i%8 = x
// i/8 = y
class Boardgui{ 
	static int turn;
	static Button black[][],white[][], blue_dots[];
	static board_array BOARD;
	static Pieces_button boton;
	static Movers movery;
	static TextArea textArea, textArea2;
	static Scene scenary;
	static Stage stagey;
	static void image_init() {
		try {
			Image b[] = new Image[7],w[]=new Image[7],dot;
			int n=65;
			b[1] = new Image(new FileInputStream("Images/bp.png"),n,n,false,true);
			b[2] = new Image(new FileInputStream("Images/bb.png"),n,n,false,true);
			b[3] = new Image(new FileInputStream("Images/bn.png"),n,n,false,true);
			b[4] = new Image(new FileInputStream("Images/br.png"),n,n,false,true);
			b[5] = new Image(new FileInputStream("Images/bq.png"),n,n,false,true);
			b[6] = new Image(new FileInputStream("Images/bk.png"),n,n,false,true);
			w[1] = new Image(new FileInputStream("Images/wp.png"),n,n,false,true);
			w[2] = new Image(new FileInputStream("Images/wb.png"),n,n,false,true);
			w[3] = new Image(new FileInputStream("Images/wn.png"),n,n,false,true);
			w[4] = new Image(new FileInputStream("Images/wr.png"),n,n,false,true);
			w[5] = new Image(new FileInputStream("Images/wq.png"),n,n,false,true);
			w[6] = new Image(new FileInputStream("Images/wk.png"),n,n,false,true);
			dot = new Image(new FileInputStream("Images/dot.png"),35,35,false,true);
			for(int i=0; i<64;i++) {
				for(int j=0; j<6;j++) {
					black[i][j] = new Button();
					black[i][j].setGraphic(new ImageView(b[j+1]));
					black[i][j].setVisible(false);
					black[i][j].setStyle("-fx-background-color: transparent;");
					black[i][j].setPadding(Insets.EMPTY);
					black[i][j].setDisable(true);
					black[i][j].setOnAction(boton);
					white[i][j] = new Button();
					white[i][j].setGraphic(new ImageView(w[j+1]));
					white[i][j].setVisible(false);
					white[i][j].setStyle("-fx-background-color: transparent;");
					white[i][j].setPadding(Insets.EMPTY);
					white[i][j].setDisable(true);
					white[i][j].setOnAction(boton);
				}
				blue_dots[i] = new Button();
				blue_dots[i].setGraphic(new ImageView(dot));
				blue_dots[i].setVisible(false);
				blue_dots[i].setStyle("-fx-background-color: transparent;");
				blue_dots[i].setPadding(Insets.EMPTY);
				blue_dots[i].setDisable(true);
				blue_dots[i].setOnAction(movery);
			}
			boton.init(black, white,blue_dots,BOARD,movery);
		} catch (Exception e) {System.out.println(e);}
	}
	static void set_image() { // gonna load the Array of pieces 
		for(int i=0;i<64;i++) {
			for(int j=0; j<6;j++) {
				white[i][j].setVisible(false);
				white[i][j].setDisable(true);
				black[i][j].setVisible(false);
				black[i][j].setDisable(true);
			}
		}
		for( Entry<Integer, Pair<pieces, reachable>> e : BOARD.m.entrySet()) {
			if (e.getValue().first.id !=0) {
				if (e.getValue().first.team==1) {
					white[e.getKey()][e.getValue().first.id-1].setVisible(true);
					white[e.getKey()][e.getValue().first.id-1].setDisable(false);
				}
				else {
					black[e.getKey()][e.getValue().first.id-1].setVisible(true);
					black[e.getKey()][e.getValue().first.id-1].setDisable(false);
				}
			}
		}
	}
    static void MainBoard(Stage stage)  // the whole gameplay screen.
    {   
    	stagey = stage;
    	turn=0;
    	black= new Button[64][6];
    	white=new Button[64][6];
    	blue_dots= new Button[64];
    	boton = new Pieces_button();
    	movery = new Movers();
    	BOARD = new board_array();
    	BorderPane root = new BorderPane();
    	root.setStyle("-fx-background-color: #F4A460;");
        Color c1 = Color.rgb(0,0,0);
        Color c2 = Color.rgb(225,225,225);
        GridPane ggg = new GridPane();
        Time ttt = new Time(1,0,0,0);
        Time ttt2 = new Time(1,0,0,0);
        StackPane siu = new StackPane();
        Board(root,c1,c2,true,ggg,ttt,ttt2,siu);
        Menu themes = new Menu("Themes");
        MenuItem item1 = new MenuItem("Classic");
        MenuItem item2 = new MenuItem("Green");
        MenuItem item3 = new MenuItem("Brown");
        Menu co = new Menu("EXIT GAME");
        MenuItem item4 = new MenuItem("YES");
        MenuItem item5 = new MenuItem("NO");
        item1.setOnAction(event ->
        {
            Board(root,Color.rgb(0,0,0),Color.rgb(225,225,225),false,ggg,ttt,ttt2,siu);
        });
        item2.setOnAction(event ->
        {
            Board(root,Color.rgb(118,150,86),Color.rgb(238,238,210),false,ggg,ttt,ttt2,siu);
        });
        item3.setOnAction(event ->
        {
            Board(root,Color.rgb(184,139,74),Color.rgb(227,193,111),false,ggg,ttt,ttt2,siu);
        });
        item4.setOnAction(event ->
        {
            menu.screen1(stage);
        });
        themes.getItems().addAll(item1, item2, item3);
        co.getItems().addAll(item4, item5);
        MenuBar newmenubar = new MenuBar();
        newmenubar.getMenus().addAll(themes,co);
        root.setTop(newmenubar);
        textArea = new TextArea("");
        textArea.setWrapText(true);
        textArea.setMaxHeight(399);
        textArea.setStyle("-fx-control-inner-background:#000000; -fx-text-alignment:center; -fx-padding: 0 200 10 70;");
        textArea.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, new Insets(0,200,0,0))));
        textArea.setEditable(false);
        textArea.setFocusTraversable(false);
        textArea.setMouseTransparent(true);
        textArea2 = new TextArea("");
        textArea2.setWrapText(true);
        textArea2.setMaxHeight(399);
        textArea2.setStyle("-fx-control-inner-background:#000000;-fx-text-alignment:center; -fx-padding: 0 0 10 270;");
        textArea2.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, new Insets(0,0,0,100))));
        textArea2.setEditable(false);
        textArea2.setFocusTraversable(false);
        textArea2.setMouseTransparent(true);
        TextArea textArea3 = new TextArea("CHESS");
        textArea3.setWrapText(true);
        textArea3.setMaxHeight(500);
        textArea3.setStyle("-fx-control-inner-background:#000000; -fx-text-alignment:center; -fx-padding: 10 0 400 170;");
        textArea3.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, new Insets(0,0,400,0))));
        textArea3.setEditable(false);
        textArea3.setFocusTraversable(false);
        textArea3.setMouseTransparent(true);
        siu.getChildren().addAll(textArea3,textArea2,textArea);
        siu.setAlignment(Pos.CENTER);
        siu.setPadding(new Insets(100,200,100,100)); //paddings
        stage.getScene().setRoot(root);
        scenary = stage.getScene();
    }
    private static void Board(BorderPane root, Color c1, Color c2,boolean zamn, GridPane ggg,Time ttt,Time ttt2, StackPane siu)
    {
        int i = 0;
        int j = 0;
        int l = 0;
        ggg.setAlignment(Pos.CENTER_LEFT);
        ggg.setPadding(new Insets(80, 100, 100, 100)); 
        ggg.setMinSize(100, 100); 
        image_init();
        for(i=1; i<=8 ; i++)
        {
            for(j=0;j<8;j++)
            {
                Text textx = new Text();
                Text texty = new Text();
                Rectangle rectangle = new Rectangle();
                StackPane sp = new StackPane(),sp2 = new StackPane(), sp3 = new StackPane();
                if(i==1)
                {
                    String temp1 = "";
                    temp1+=(char)(65+j);
                    textx.setText(temp1);
                    textx.setFill(c2);
                    textx.setFont(new Font(15));
                    textx.setStyle("-fx-font-weight: bold");
                    if(l%2==0)
                       textx.setFill(c1);
                }
                if (j==0) {
                	String temp = "";
                    temp += (char)(i+48);
                    texty.setText(temp);
                    texty.setFill(c2);
                    texty.setFont(new Font(15));
                    texty.setStyle("-fx-font-weight: bold");
                    if(l%2==0)
                       texty.setFill(c1);
                }
                rectangle.setWidth(80.0f); 
                rectangle.setHeight(80.0f);
                rectangle.setFill(c1);
                if(l%2==0)
                    rectangle.setFill(c2);
                sp3.getChildren().add(rectangle);
                for(int iiii=0;iiii<6;iiii++) {
                	sp3.getChildren().add(white[(i-1)*8+j][iiii]);
                	sp3.getChildren().add(black[(i-1)*8+j][iiii]);
                }
                sp3.getChildren().add(blue_dots[(i-1)*8+j]);
                sp3.setAlignment(Pos.CENTER);
                sp3.setMaxHeight(80);
                sp3.setMaxWidth(80);
                sp2.getChildren().addAll(sp3,texty);
                sp2.setAlignment(Pos.CENTER_LEFT);
                sp.getChildren().addAll(sp2,textx);
                sp.setAlignment(Pos.BOTTOM_CENTER);
                ggg.add(sp,j,9-i);
                l++;
            }
        l++;
        }
        set_image();
        if (zamn) {  // gui for timer
        	Rectangle rect = new Rectangle(),rect2 = new Rectangle();
        	rect.setWidth(120.0f);
        	rect.setHeight(60.00f);
        	rect.setFill(Color.rgb(48, 48, 56));
        	rect2.setWidth(120.0f);
        	rect2.setHeight(55.00f);
        	rect2.setFill(Color.rgb(48, 48, 56));
	        Text timer = new Text("    "+ttt), timer2 = new Text("  "+ttt2);
	        timer.setFont(new Font(35));
	        timer2.setFont(new Font(35));
	        timer.setFill(Color.WHITE);
	        timer2.setFill(Color.WHITE);
	        StackPane spp = new StackPane(), spp2 = new StackPane();
	        spp.getChildren().addAll(rect,timer);
	        spp2.getChildren().addAll(rect2,timer2);
	        StackPane.setAlignment(rect,Pos.CENTER_RIGHT);
	        StackPane.setAlignment(rect2,Pos.CENTER_LEFT);
	        StackPane.setAlignment(timer,Pos.CENTER);
	        StackPane.setAlignment(timer2,Pos.CENTER_LEFT);
	        ggg.add(spp, 6, 9,2,1);
	        ggg.add(spp2, 0, 0,2,1);
        	Timeline timeline = new Timeline(  // a function that repeats in some time interval.
        		new KeyFrame(Duration.seconds(1),
        		     e -> {
        		    	 if (Movers.get()==1 && BOARD.result==-1) {
        		    		 ttt.reduce();
        		    	 }
        		    	 else if(BOARD.result==-1) {
        		    		 ttt2.reduce();        		    	 
        		    	}
        		    	 if (ttt.isZero() || ttt2.isZero()) {
	    		    		Rectangle rectangle = new Rectangle();
	    		    		rectangle.setWidth(500.0f); 
	    		    	    rectangle.setHeight(500.0f);
	    		            rectangle.setFill(Color.AQUA);
	    		            StackPane sp2 = new StackPane(rectangle);
	    		            Label resy = new Label();
    		                resy.setFont(Font.font("Arial",100));
    		                VBox vb = new VBox(100);
    		                vb.getChildren().add(resy);
    		                Button exitb = new Button("Main Menu");
    		        	    exitb.setMaxSize(200, 150);
    		        	    exitb.setFont(Font.font("Sans-serif",18));
    		        		exitb.setStyle("-fx-background-color: #008080;");
    		        	    vb.getChildren().add(exitb);
    		        	    vb.setAlignment(Pos.CENTER);
    		        	    EventHandler<ActionEvent> gobacktomain = new EventHandler<ActionEvent>() { 
    		        	        public void handle(ActionEvent e) 
    		        	        { 
    		        	        	ttt.sethigh();
    		        	        	ttt2.sethigh();
    		        	            menu.screen1(Boardgui.stagey);       
    		        	        } 
    		        	    }; 
    		        	    exitb.setOnAction(gobacktomain);
    		                sp2.getChildren().add(vb);
    		     			if (ttt2.isZero()) {
    		     				resy.setText("White wins");
    		     				BOARD.result=1;
    		     			}
    		     			else if (ttt.isZero()) {
    		     				resy.setText("Black wins");
    		     				BOARD.result=2;
    		     			}
    		    			StackPane sp = new StackPane(Boardgui.scenary.getRoot());
    		    			sp.getChildren().add(sp2);
    		    			sp.setAlignment(Pos.CENTER);
    		    			Boardgui.scenary.setRoot(sp);
        		    	 }
        		    	 timer.setText("    "+ttt);
        		    	 timer2.setText("  "+ttt2);
        		     }
        		));
        	timeline.setCycleCount(Timeline.INDEFINITE);
        	timeline.play();
        }
        HBox yooo = new HBox(100);
        yooo.getChildren().addAll(ggg,siu);
        root.setCenter(yooo);
    }
}
class menu{
	static void screen1(Stage stage){
		BorderPane g = new BorderPane();
		//Scene s = new Scene(g);
		Label t = new Label("CHESS");
		Label space = new Label(" "),space2 = new Label(" ");
		VBox vv = new VBox(70);
		t.setFont(Font.font("Arial",150));
		space.setFont(Font.font("Arial",10));
		space2.setFont(Font.font("Arial",50));
		vv.setAlignment(Pos.TOP_CENTER);
		vv.getChildren().add(space);
		vv.getChildren().add(t);
		g.setTop(vv);
		BorderPane.setAlignment(t, Pos.CENTER);
		g.setStyle("-fx-background-color: #F4A460;");
		VBox v = new VBox(40);
		v.setAlignment(Pos.TOP_CENTER);
		Button b1 = new Button("Play");
		Button b2 = new Button("Credits");
		Button b3 = new Button("Exit");
		b1.setMaxSize(150, 100);
		b1.setFont(Font.font("Sans-serif",25));
		b2.setMaxSize(150, 100);
		b2.setFont(Font.font("Sans-serif",25));
		b3.setMaxSize(150, 100);
		b3.setFont(Font.font("Sans-serif",25));
		b1.setStyle("-fx-background-color: #008080;");
		b2.setStyle("-fx-background-color: #008080;");
		b3.setStyle("-fx-background-color: #008080;");
		EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
                Boardgui.MainBoard(stage);       
            } 
        }; 
        EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
                screen2(stage);         
            } 
        }; 
        EventHandler<ActionEvent> event3 = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	//make a are you sure button here.
                stage.close();       
            } 
        };
        b1.setOnAction(event1);
        b2.setOnAction(event2);
        b3.setOnAction(event3);
		v.getChildren().add(space2);
		v.getChildren().add(b1);
		v.getChildren().add(b2);
		v.getChildren().add(b3);
		g.setCenter(v);
		try {
			Image wk = new Image(new FileInputStream("Images/white-king.png"),400,450,false,true);
			Image bk = new Image(new FileInputStream("Images/black-king.png"),400,450,false,true);
			ImageView wkk = new ImageView(wk);
			ImageView bkk = new ImageView(bk);
			g.setLeft(wkk);
			g.setRight(bkk);
		} catch (Exception e) {};
		stage.getScene().setRoot(g);
	}
	static void screen2(Stage stage){
		BorderPane g = new BorderPane();
		VBox v = new VBox(8);
		v.setAlignment(Pos.TOP_CENTER);
		StackPane s = new StackPane();
		Label t1 = new Label("Subham Chakraborty (S.Bolt) ");
		//Label t2 = new Label("Atharv Pawar [GUI Designer]");
		Label sc = new Label(" ");
		Label s1 = new Label(" ");
		Label s2 = new Label(" ");
		Label cred = new Label("Credits");
		cred.setFont(Font.font("Arial",150));
		t1.setFont(Font.font("Arial",50));
		//t2.setFont(Font.font("Arial",50));
		v.getChildren().add(cred);
		v.getChildren().add(sc);
		v.getChildren().add(t1);
		v.getChildren().add(s1);
		//v.getChildren().add(t2);
		v.getChildren().add(s2);
		s.getChildren().add(v);
		g.setCenter(s);
		Button exitb = new Button("Main Menu");
	    exitb.setMaxSize(200, 150);
	    exitb.setFont(Font.font("Sans-serif",18));
		exitb.setStyle("-fx-background-color: #008080;");
	    v.getChildren().add(exitb);
	    EventHandler<ActionEvent> gobacktomain = new EventHandler<ActionEvent>() { 
	        public void handle(ActionEvent e) 
	        { 
	            menu.screen1(stage);       
	        } 
	    }; 
	    exitb.setOnAction(gobacktomain);
		BorderPane.setAlignment(s, Pos.CENTER);
		g.setStyle("-fx-background-color: #F4A460;");
		stage.getScene().setRoot(g);
	}
}
public class App extends Application {
	static void initialise(Stage Stage){
		Stage.setTitle("Chess");
		try {
			Image ic = new Image(new FileInputStream("Images/wp.png"));
			Stage.getIcons().add(ic);
		} catch (Exception e) {};
		Stage.setResizable(false);
		Stage.setFullScreen(true);
		Stage.setFullScreenExitHint("");
	}
	@Override
	public void start(Stage Stage) throws Exception{
		initialise(Stage);
		HBox hb = new HBox();
		Scene s = new Scene(hb);
		Stage.setScene(s);
		menu.screen1(Stage);
		Stage.show();
	}
	public static void main(String args[]) {
		launch(args);
	}
}
