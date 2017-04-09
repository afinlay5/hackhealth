import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;


import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;

import javafx.scene.image.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import javafx.scene.text.Font;
//import javax.json.*;

 

public class HackHealth extends Application {
    public static void main(String[] args) {
        launch(args);
    }
	
	//UI Components & Scenes
	private Scene scene;
	private Label title, subtitle;
	private TextField field;
	private FlowPane container = new FlowPane(Orientation.VERTICAL);
	private HBox trans;
	private Button reset, forward, backward;
	private ImageView header;
	
	Stage _home;
	Scene ques,data, meal_s;
	
	String name= " " , g_gender= " ", g_actlevel = "" ;
	int g_weight=0, g_age =0;
	int g_height=0, g_meals=0;
		
    
    @Override
    public void start(Stage home) {
		_home = home;
        home.setTitle("HACKHealth ");
		
		// Provide Icon
		try {
		home.getIcons().add(new Image(this.getClass().getResourceAsStream("ICON.png")));
		}
		catch (Exception exc) {
			System.out.println("Icon Resource not found.");
		}
		
		//Set a background image
		Image image = new Image(this.getClass().getResourceAsStream("BKG.jpg"));
		BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, 
																BackgroundPosition.CENTER, backgroundSize);
		Background background = new Background(backgroundImage);
		
		
		
        
		//UI Instantiation
		Button head = new Button ();
		header = new ImageView (new Image (this.getClass().getResourceAsStream("HEADER.png"), 350, 100, false, true) );
								// boolean preserveRatio, boolean smooth
		head.setGraphic(header);
		head.setAlignment(Pos.CENTER);

		
		//subtitle = new Label ("what's the perfect meal for you?");		
		field = new TextField("Enter Your Name");
		
		reset = new Button ();
		reset.setGraphic(new ImageView (new Image (this.getClass().getResourceAsStream("RESET.png") ) ) );
		
		backward = new Button ();
		backward.setGraphic(new ImageView ( new Image (this.getClass().getResourceAsStream("LEFT_A.png") ) ) );
		
		forward = new Button ();
		forward.setGraphic(new ImageView ( new Image (this.getClass().getResourceAsStream("RIGHT_A.png") ) ) );
		
		trans = new HBox(backward, reset, forward);
		trans.setAlignment(Pos.CENTER);
		
				
		container.setAlignment(Pos.CENTER);
		container.getChildren().add(field);
		BorderPane root = new BorderPane();
		
		root.setTop(header);
		root.setCenter(container);
		root.setBottom(trans);
		
		
		
		
		BMR _bmr = new BMR ();
		double bmr = 0.0;
		try {
			bmr = _bmr.getBMR("MALE", 220, 74.5, 23); // (gender, weight, height, age  )
		}
		catch (BMRException e) {
			out.println("\nSomething went wrong with your BMR Calculation" + e);	
		}
		
		
		
		// Event Handling
		field.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) { 
				if (field.getText().toString().contains("Enter Your Name") | field.getText().toString().length() < 1 ) {
					Alert exit = new Alert(AlertType.INFORMATION);
					exit.setTitle("Invalid Entry");
					exit.setHeaderText("Invalid Entry");
					exit.setContentText("Please Enter a proper response.");
					exit.showAndWait();  	
				}
				else {
					name = field.getText().toString();
					transToQ();
				}
			}
		});
		
		    
      	
		//Background -> Root Node
		root.setBackground(background);
		
		
		scene = new Scene (root, 500, 400);
        home.setScene(scene);
        home.show();
    }
	
	void transToQ () {
		
		
		// Gender, Weight, Height, Age
		Label gender = new Label ("What is your gender?");
		TextField GField = new TextField("MALE or FEMALE");
		
		Label weight = new Label ("How much do you weigh? (lbs.)");
		TextField WField = new TextField("Enter Your Weight");
		
		Label height = new Label ("How tall are you? (X'Y\") ");
		TextField HField = new TextField("Enter Your height");
		
		Label age = new Label ("How old are you?");
		TextField AField = new TextField("Enter Your Age");
		
		
		//Set a background image
		Image image = new Image(this.getClass().getResourceAsStream("BKG.jpg"));
		BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, 
																BackgroundPosition.CENTER, backgroundSize);
		Background background = new Background(backgroundImage);
		
		
		
		//Navigation
		Button reset = new Button ();
		reset.setGraphic(new ImageView (new Image (this.getClass().getResourceAsStream("RESET.png") ) ) );
		
		Button backward = new Button ();
		backward.setGraphic(new ImageView ( new Image (this.getClass().getResourceAsStream("LEFT_A.png") ) ) );
		
		Button forward = new Button ();
		forward.setGraphic(new ImageView ( new Image (this.getClass().getResourceAsStream("RIGHT_A.png") ) ) );
		
		
		//Box it up
		VBox box = new VBox (gender, GField, weight, WField, height, HField, age, AField);
		HBox trans = new HBox(backward, reset, forward);
		trans.setAlignment(Pos.CENTER);
				
						
		BorderPane root = new BorderPane();
		root.setCenter(box);
		root.setBottom(trans);
		
				
		root.setBackground(background);
		
		ques = new Scene (root, 500, 400);
		
		_home.setScene (ques);
		_home.show();
		
		
		
		//Respond to Questions
			
		GField.setOnAction( (x) -> { // #1
			if (GField.getText().toString().equals("MALE") ) {
				g_gender = "MALE";
			}
			
			else if (GField.getText().toString().equals("FEMALE") ) {
				g_gender = "FEMALE";
			}
				 
			else {
				GField.setText("");
				
				Alert exit = new Alert(AlertType.INFORMATION);
				exit.setTitle("Invalid Entry");
		        exit.setHeaderText("Invalid Entry");
        		exit.setContentText("Please Enter a proper response.");
        		exit.showAndWait(); 
				
			}
					 
		});
		GField.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
				if (GField.getText().toString().equals("MALE") ) {
					g_gender = "MALE";
				}
			
				else if (GField.getText().toString().equals("FEMALE") ) {
					g_gender = "FEMALE";
				}
				 
				else {
					GField.setText("");
				
					Alert exit = new Alert(AlertType.INFORMATION);
					exit.setTitle("Invalid Entry");
			        exit.setHeaderText("Invalid Entry");
        			exit.setContentText("Please Enter a proper response.");
        			exit.showAndWait(); 
				
				}
			}
		});
	 
		WField.setOnAction( (x) -> { // #2
				
				if (WField.getText().toString().length() > 1 & Character.isDigit(WField.getText().toString().charAt(0)) & WField.getText().toString().length() < 4 ) { 
					// is there text, is it a number, is the number less than 1,000. 
					g_weight = Integer.parseInt( WField.getText() );
				}
				else  {
					WField.setText("");
					
					Alert exit = new Alert(AlertType.INFORMATION);
					exit.setTitle("Invalid Entry");
		        	exit.setHeaderText("Invalid Entry");
        			exit.setContentText("Please Enter a proper response.");
        			exit.showAndWait(); 
				}
			
		});
		WField.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
					if (WField.getText().toString().length() > 1 & Character.isDigit(WField.getText().toString().charAt(0)) & WField.getText().toString().length() < 4 ) { 
					// is there text, is it a number, is the number less than 1,000. 
					g_weight = Integer.parseInt( WField.getText() );
				}
				else  {
					WField.setText("");
					
					Alert exit = new Alert(AlertType.INFORMATION);
					exit.setTitle("Invalid Entry");
		        	exit.setHeaderText("Invalid Entry");
        			exit.setContentText("Please Enter a proper response.");
        			exit.showAndWait(); 
				}
			}
		});
				
		HField.setOnAction( (x) -> { 
				
				if (HField.getText().toString().length() <= 1) HField.setText("     ");
			
				String result = HField.getText().toString();
				int height1,height2;
				
				String quote1 = String.valueOf(result.charAt(1));
				String quote2 = String.valueOf(result.charAt(3));
				
				if ( (quote1.equals("\'") & quote2.equals("\"") & result.length() < 5) ) {
						//out.println(quote1 + quote2);
						height1 = Integer.parseInt( String.valueOf(result.charAt(0)) );
						height2 = Integer.parseInt( String.valueOf(result.charAt(2)) );
						//out.println(height1 + " " + height2);
						g_height = (height1 * 12) + height2;
						//out.println(g_height + "  inches.");
					
				}
				else {					
					HField.setText("");
					
					Alert exit = new Alert(AlertType.INFORMATION);
					exit.setTitle("Invalid Entry");
		        	exit.setHeaderText("Invalid Entry");
        			exit.setContentText("Please Enter a properly formatted response.");
        			exit.showAndWait(); 			
				}
		});
		HField.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
				if (HField.getText().toString().length() <= 1) HField.setText("     ");
			
				String result = HField.getText().toString();
				int height1,height2;
				
				String quote1 = String.valueOf(result.charAt(1));
				String quote2 = String.valueOf(result.charAt(3));
				
				if ( (quote1.equals("\'") & quote2.equals("\"") & result.length() < 5) ) {
						//out.println(quote1 + quote2);
						height1 = Integer.parseInt( String.valueOf(result.charAt(0)) );
						height2 = Integer.parseInt( String.valueOf(result.charAt(2)) );
						//out.println(height1 + " " + height2);
						g_height = (height1 * 12) + height2;
						//out.println(g_height + "  inches.");
					
				}
				else {					
					HField.setText("");
					
					Alert exit = new Alert(AlertType.INFORMATION);
					exit.setTitle("Invalid Entry");
		        	exit.setHeaderText("Invalid Entry");
        			exit.setContentText("Please Enter a properly formatted response.");
        			exit.showAndWait(); 			
				}
			}
		});
		
		AField.setOnAction( (x) -> { 
			
			if (AField.getText().toString().length() > 1 & Character.isDigit(WField.getText().toString().charAt(0)) & AField.getText().toString().matches("[0-9]+") ) {
					// is there text, is it a number, is the number less than 1,000. 
				if (Integer.parseInt(AField.getText().toString()) < 140  & Integer.parseInt(AField.getText().toString()) > 0) { 
					g_age = Integer.parseInt( AField.getText() );
				}
			
				out.println("So far:  " + g_gender);
				out.println("So far:  " + g_weight);
				out.println("So far:  " + g_height);					
				out.println("So far:  " + g_age);
			
			}										
				
			else {
				AField.setText("");
					
				Alert exit = new Alert(AlertType.INFORMATION);
				exit.setTitle("Invalid Entry");
		        exit.setHeaderText("Invalid Entry");
        		exit.setContentText("Please Enter a properly formatted response.");
        		exit.showAndWait(); 
			}
			
		});
		AField.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
				if (AField.getText().toString().length() > 1 & Character.isDigit(WField.getText().toString().charAt(0)) & AField.getText().toString().matches("[0-9]+") ) {
					// is there text, is it a number, is the number less than 1,000. 
				if (Integer.parseInt(AField.getText().toString()) < 140  & Integer.parseInt(AField.getText().toString()) > 0) { 
					g_age = Integer.parseInt( AField.getText() );
				}
			
			}										
				
			else {
				AField.setText("");
					
				Alert exit = new Alert(AlertType.INFORMATION);
				exit.setTitle("Invalid Entry");
		        exit.setHeaderText("Invalid Entry");
        		exit.setContentText("Please Enter a properly formatted response.");
        		exit.showAndWait(); 
			}
			}
		});
		
		
		
		//Directional
		forward.setOnAction( (x) -> {
			
            GField.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER, true, true, true, true));
			WField.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER, true, true, true, true));
			HField.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER, true, true, true, true));
			AField.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER, true, true, true, true));
			
			
			out.println("So far:  " + g_gender);
			out.println("So far:  " + g_weight);
			out.println("So far:  " + g_height);					
			out.println("So far:  " + g_age);
			
			
			BMR bmr = new BMR();
			double _result_ = 0.0;
			try { 
				_result_ = bmr.getBMR(g_gender, g_weight, g_height, g_age); 
				out.println("My BMR is:  " + _result_); 
			}
			catch (BMRException e) { out.println("A BMR Exception has occured"); }
			
			//---> move from here.
			transToData(_result_); //How do I prevent the "potential" zero value from ever being used?
			
			
			
		});
		
		backward.setOnAction( (x) -> {
			_home.setScene(scene);
        	_home.show();
			
		});
		
		reset.setOnAction( (x) -> {
			_home.setScene(scene);
        	_home.show();
			
		});
		
		
	}
	
	void transToData(double bmr) {
		
		Label meals = new Label ("How many meals do you have per day?");
		TextField MField = new TextField("4");
			
		Label activity = new Label ("What is your activity level? (sedentary, active, extra active");
		TextField AField = new TextField("sedentary");
		
		
		Label BMR_Title = new Label ("Your Daily Caloric Needs are: ");
		BMR_Title.setFont(Font.font("Cambria", 12));
		Label BMR = new Label ();
		
		
		double calperMeal = bmr; 
		double calNeeds = bmr*1.2;
		String switch_fit ="";
		
		
		
		
		//Handle some events
		MField.setOnAction ( (x) -> {
				if (MField.getText().toString().length() > 1 & Character.isDigit(MField.getText().toString().charAt(0)) & MField.getText().toString().matches("[0-9]+") ) {
					// is there text, is it a number, is the number less than 1,000. 
				if (Integer.parseInt(MField.getText().toString()) < 6  & Integer.parseInt(AField.getText().toString()) > 0) { 
					g_meals = Integer.parseInt( AField.getText() );
					
				}
				
			else {
				MField.setText("");
					
				Alert exit = new Alert(AlertType.INFORMATION);
				exit.setTitle("Invalid Entry");
		        exit.setHeaderText("Invalid Entry");
        		exit.setContentText("Please Enter a properly formatted response.");
        		exit.showAndWait(); 
			}
			}
		});
				
		AField.setOnAction ( (x) -> {
				if (AField.getText().toString().length() > 1 ) {
					// is there text 
					 g_actlevel = AField.getText().toString();
				}
													
				
			else {
				AField.setText("");
					
				Alert exit = new Alert(AlertType.INFORMATION);
				exit.setTitle("Invalid Entry");
		        exit.setHeaderText("Invalid Entry");
        		exit.setContentText("Please Enter a properly formatted response.");
        		exit.showAndWait(); 
			}
		  
		});
	
		switch (g_actlevel) {
			case "sedentary":
					calNeeds = bmr*1.2;

			case "active":
					calNeeds = bmr*1.55;

			case "extra active":
					calNeeds = bmr*1.9;
			default:
					calNeeds = bmr*1.2;
		}

		
		calperMeal = bmr/Double.parseDouble(MField.getText().toString() );
		BMR = new Label ( String.valueOf(calNeeds) );
		BMR.setFont(Font.font("Cambria", 32));
		
		Label CPM_Title = new Label ("Your Calories per Meal: ");
		BMR_Title.setFont(Font.font("Cambria", 12));
		Label CPM = new Label (String.valueOf(calperMeal));
		
		Label ACT_Title = new Label ("Your current activity level is:  ");
		ACT_Title.setFont(Font.font("Cambria", 12));
		Label ACT = new Label ((AField.getText().toString()	));
			
		
		
		//Navigation
		Button reset = new Button ();
		reset.setGraphic(new ImageView (new Image (this.getClass().getResourceAsStream("RESET.png") ) ) );
		
		Button backward = new Button ();
		backward.setGraphic(new ImageView ( new Image (this.getClass().getResourceAsStream("LEFT_A.png") ) ) );
		
		Button forward = new Button ();
		forward.setGraphic(new ImageView ( new Image (this.getClass().getResourceAsStream("RIGHT_A.png") ) ) );
		
		
		//Box it up
		VBox box = new VBox (meals, MField, activity, AField, BMR_Title,BMR, CPM_Title, CPM, ACT_Title, ACT);
		HBox trans = new HBox(backward, reset, forward);
		trans.setAlignment(Pos.CENTER);
				
						
		BorderPane root = new BorderPane();
		root.setCenter(box);
		root.setBottom(trans);
		
		//Set a background image
		Image image = new Image(this.getClass().getResourceAsStream("BKG.jpg"));
		BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, 
																BackgroundPosition.CENTER, backgroundSize);
		Background background = new Background(backgroundImage);
		
		root.setBackground(background);
		
		
		backward.setOnAction( (x) -> {
			_home.setScene(ques);
        	_home.show();
			
		});
		
		
		reset.setOnAction( (x) -> {
			_home.setScene(scene);
        	_home.show();
			
		});
		
		double CaloriesPerMeal = calperMeal;
		forward.setOnAction( (x) -> { transtoMeals(CaloriesPerMeal); });
		
		
		
		data = new Scene (root, 500, 400);
		
		_home.setScene (data);
		_home.show();
	}
	
	void transtoMeals(double calPerMeal) {
		
		Label meals = new Label ("What do you want to eat?");
		TextField MField = new TextField("chicken");
		
		try {
		getRequest(MField.getText().toString());
		}
		catch (Exception exc) {
			out.println("Exception occured." + exc);	
		}
			
		//Navigation
		Button reset = new Button ();
		reset.setGraphic(new ImageView (new Image (this.getClass().getResourceAsStream("RESET.png") ) ) );
		
		Button backward = new Button ();
		backward.setGraphic(new ImageView ( new Image (this.getClass().getResourceAsStream("LEFT_A.png") ) ) );
		
		Button forward = new Button ();
		forward.setGraphic(new ImageView ( new Image (this.getClass().getResourceAsStream("RIGHT_A.png") ) ) );
		
		
		//Box it up
		VBox box = new VBox (meals, MField);
		HBox trans = new HBox(backward, reset, forward);
		trans.setAlignment(Pos.CENTER);
				
						
		BorderPane root = new BorderPane();
		root.setCenter(box);
		root.setBottom(trans);
		
		//Set a background image
		Image image = new Image(this.getClass().getResourceAsStream("BKG.jpg"));
		BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, 
																BackgroundPosition.CENTER, backgroundSize);
		Background background = new Background(backgroundImage);
		
		root.setBackground(background);
		
		
		backward.setOnAction( (x) -> {
			_home.setScene(data);
        	_home.show();
			
		});
		
		
		reset.setOnAction( (x) -> {
			_home.setScene(scene);
        	_home.show();
			
		});
		
		meal_s = new Scene (root, 500, 400);
		
		_home.setScene (meal_s);
		_home.show();
		
	}
	
	// HTTP GET Request
	private void getRequest(String query) throws Exception {
		
		String _query = query;
		
		String url = "https://api.nutritionix.com/v1_1/search/"
					  + _query 
					  + "?fields=item_name%2Cnf_calories&appId="
					  + "721a3eb1&appKey=ecb85900585b0eaeece872c0a401721b";	// ID & Key

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection(); //Obtain connection to URL

		// GET REQUEST
		con.setRequestMethod("GET");

		//add request header
		//con.setRequestProperty("", "");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode); //success?

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		String _response = response.toString();
		
		// JSONArray ja = new JSONArray(_response);

		//print result
		System.out.println(response.toString());

	}
	
	
	
}











