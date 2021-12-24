package zad1;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ChatController {
	private static String nickname;
	private static TopicConnectionFactory factory;
	private static TopicConnection topicConn;
	private static TopicSession session;
	private static TopicPublisher publisher;
	private static Topic topic;
	private static TopicSubscriber subscriber;
	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
	private static boolean chatStarted=false;
	
	@FXML
	private TextField loginField = new TextField();
	@FXML
	private TextField messageField = new TextField();
	
	@FXML
	private Label nick = new Label();
	
	@FXML
	private VBox messages= new VBox();
	
	@FXML
	public void initialize() {
		nick.setText(nickname);
		messageField.setOnKeyPressed( event -> {
			  if( event.getCode() == KeyCode.ENTER ) {
				  sendMessage();
			  }
			} );
		loginField.setOnKeyPressed( event -> {
			  if( event.getCode() == KeyCode.ENTER ) {
			    login();
			  }
			} );
		
	}
		
	
	private static TopicSubscriber setUp(Hashtable<String, String> properties)
			throws NamingException, JMSException {
		Context ctx = new InitialContext(properties);		
		factory = (TopicConnectionFactory) ctx.lookup("ConnectionFactory");
		topicConn = factory.createTopicConnection();		
		session = topicConn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);		
		topic = (Topic) ctx.lookup("topic1");
		publisher = session.createPublisher(topic);
		TopicSubscriber subscriber = session.createSubscriber(topic);
		return subscriber;
	}

	private void getMessage(TopicSession session2, TopicSubscriber subscriber, String nick)
			throws JMSException {
		
	
		subscriber.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message arg0) {
				TextMessage msg = (TextMessage) arg0;
				
				Platform.runLater(new Runnable(){
					@Override public void run() {
					messages.getChildren().add(new Text(msg.toString()));
					}
				});
					

			}
		});
		
	}

	public void login() {
		
	
		nickname=loginField.getText();
		
		FXMLLoader fxmlLoader = new FXMLLoader();
		  try {
			fxmlLoader.setLocation(new URL("file:/Users/rachel_green/eclipse-workspace/TPO4_LY_S19516/src/zad1/chatWindow.fxml"));
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} 
		  
		  Parent root1;
			try {
				root1 = (Parent) fxmlLoader.load();
			    
				Main.getStage().setScene(new Scene(root1));  
			    Main.getStage().show();
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			Hashtable<String, String> properties = new Hashtable<>();
			properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
			properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");
			
			try {
				 subscriber = setUp(properties);
				topicConn.start();
				
			} catch (NamingException | JMSException e) {
				e.printStackTrace();
			}
	}
	public void logout() {
		try {
			publisher.publish(session.createTextMessage( dtf.format(LocalTime.now())+" "+nickname+" logged out"));
			topicConn.close();
			Main.getStage().close();
			System.exit(0);
		} catch (JMSException  e) {
			e.printStackTrace();
		}
		
	}
	public void sendMessage() {
		if(!chatStarted) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Click \"Start Chat\" button, in order to start a chat");
			alert.show();
		
			return;
		}
		String message = dtf.format(LocalTime.now())+" "+nickname +": "+messageField.getText();
		try {
			getMessage(session, subscriber, nickname);
			publisher.publish(session.createTextMessage(message));
		
		messageField.setText("");
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	};
	public void start() {
		if(chatStarted) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Chat has been started already");
			alert.show();
		
			return;
		}
		chatStarted=true;
		try {
			publisher.publish(session.createTextMessage(dtf.format(LocalTime.now())+" "+nickname+" logged in"));
			getMessage(session, subscriber, nickname);
		} catch (
				JMSException e) {
			e.printStackTrace();
		}
	}
	
	
}
