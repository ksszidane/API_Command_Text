import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.awt.*;
import java.io.IOException;
import java.net.http.HttpClient;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class gui extends JFrame implements ActionListener, KeyListener {
	
	
	JRadioButton radio[] = new JRadioButton[2];
    String radio_name[] = {"STG", "PRD"}; 
    
    BufferedOutputStream bs = null;
    
    String server = null;
    String result;
    String os;
    String path;
    
    JPanel actionpanel1 = new JPanel();
    JPanel actionpanel2 = new JPanel();
    JPanel textpanel = new JPanel();
    
    JTextArea textarea = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(textarea);
    
    JLabel command_label = new JLabel(" Text");
    JTextField command_field = new JTextField(20); 
    JLabel uniqueid_label = new JLabel("UID");
    JButton set = new JButton("Save");
    JTextField uniqueid_field = new JTextField(20); 
    JButton send = new JButton("Send");
    JButton reset = new JButton("Reset");
    
    private final OkHttpClient httpClient = new OkHttpClient();
	
	public gui() {
		
        JFrame frame = new JFrame("v 0.1.0 / QA팀 / A. Post Command Text");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(490, 600);
        textpanel.setLayout(new  BoxLayout(textpanel, BoxLayout.Y_AXIS));
        
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        os = System.getProperty("os.name").toLowerCase();
        path = System.getProperty("user.home").toLowerCase();
        String filePath = null;
        
        try {
 	       // 바이트 단위로 파일읽기
        	if (os.contains("win")) {
        		filePath = "C:/UID.txt"; // 대상 파일 윈도우 경로
        	} else if (os.contains("mac")) {
        		filePath = path+"/UID.txt";
        	}
        	
 	        FileInputStream fileStream = null; // 파일 스트림
 	        
 	        fileStream = new FileInputStream( filePath );// 파일 스트림 생성
 	        //버퍼 선언
 	        byte[ ] readBuffer = new byte[fileStream.available()];
 	        while (fileStream.read( readBuffer ) != -1){}
 	       uniqueid_field.setText(new String(readBuffer));

 	        fileStream.close(); //스트림 닫기
 	    } catch (Exception e) {
 		e.getStackTrace();
 	    }
        
        textarea.setText("사용법 \n"
        		+ "1. Unique id를 입력해주세요. \n"
        		+ "2. 서버를 선택해주세요. \n"
        		+ "3. Save버튼으로 Unique id 유효값을 확인과 값을 [저장]하세요. \n"
        		+ "   (Unique id는 여러개를 저장하여도 마지막 저장값 1개만 저장됩니다.) \n"
        		+ "4. Text Field에 명렁어를 입력해주세요. \n"
        		+ "5. Send 버튼으로 명령어를 전송하세요. \n \n"
        		+ "-. Reset 버튼을 누르면 textarea가 모두 초기화 됩니다. \n"
        		+ "-. Windows / Mac 모두 사용 가능한 툴입니다. \n"
        		+ "-. 현재 실행 운영체제는 "+ os + "입니다. \n"
        		+ "-. v0.1.0 부터 에이닷 Android / iOS 모두 사용 가능합니다.\n"
        		+ "-. (A. App이 실행중인 상태인 경우에만 동작 가능합니다.)");

        
        actionpanel1.add(uniqueid_label); 
        actionpanel1.add(uniqueid_field);
        actionpanel1.add(set);
        
        ButtonGroup group = new ButtonGroup();
        
        for(int i=0; i<radio.length; i++){
            radio[i] = new JRadioButton(radio_name[i]);
            group.add(radio[i]);
            actionpanel1.add(radio[i]);
            radio[i].addActionListener(this);
        }
        
        
        actionpanel2.add(command_label); 
        actionpanel2.add(command_field);
        actionpanel2.add(send);
        actionpanel2.add(reset);
        textpanel.add(scrollPane);
        
        reset.addActionListener(this);
        uniqueid_field.addActionListener(this);
        command_field.addActionListener(this);
        set.addActionListener(this);
        send.addActionListener(this);
        command_field.addKeyListener(this);
        
        radio[0].setSelected(true);
        radio[1].setSelected(false);
        server = "STG";

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.NORTH, actionpanel1);
        frame.getContentPane().add(BorderLayout.SOUTH, actionpanel2);
        frame.getContentPane().add(BorderLayout.CENTER, textpanel);
        frame.setVisible(true);

        Date today = new Date();
        
        Date date1 = this.getDate(2023, 1, 1);
        Date date2 = this.getDate(2023, 6, 30);
        System.out.println(today);
        System.out.println(date1);
        System.out.println(date2);
        
        boolean result1 = date1.before(today);
        boolean result2 = date2.after(today);
        System.out.println(result1);
        System.out.println(result2);

        if((result1 == true) && (result2 == true)) {
        	
        } else {
        	JOptionPane.showMessageDialog(null,"프로그램 사용 이용기간이 지났습니다. \n wiki에서 새로운 버전을 다운로드 해주세요.");
        	System.exit(0);
        }
  
	}
	
	public static void main(String[] args) {
		new gui();
	}
	
	public void keyTyped(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) { 
	     int key = e.getKeyCode();
	     if (key == KeyEvent.VK_ENTER) {
	    	 //Toolkit.getDefaultToolkit().beep(); 
	    	 send.doClick(); 
	    	 command_field.setText("");
    	 }
     }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		
        if(s.equals(radio[0].getText())){
        	addLog("▷ 선택 서버 : "+radio[0].getText());
        	server = "STG";
        } else if(s.equals(radio[1].getText())){
        	addLog("▷ 선택 서버 : "+radio[1].getText());
        	server = "PRD";
        } else if(e.getSource()==reset) {
        	textarea.setText("");
        } else if (e.getSource()==set) {
        	String str = uniqueid_field.getText();
        	if(str.length() > 40) {
        		JOptionPane.showMessageDialog(null,"unique id 오류 \nunique id 40자리를 다시 확인해주세요.");
        	} else if (str.length() < 40) {
            	JOptionPane.showMessageDialog(null,"unique id 오류 \nunique id 40자리를 다시 확인해주세요.");
        	} else if (!(str.contains("APL"))) {
        		JOptionPane.showMessageDialog(null,"unique id 오류 \n올바른 unique id를 입력해주세요.");
        	} else {
        		String userid = str.substring(0, 20);
        		String deviceid = str.substring(20, 40);
        		addLog("▷ user id : " + userid +" / device id : "+ deviceid);
        	}
        	
        	try {
        		if (os.contains("win")) {
            		bs = new BufferedOutputStream(new FileOutputStream("C:/UID.txt"));
            		bs.write(str.getBytes()); 
            	} else if (os.contains("mac")) {
            		bs = new BufferedOutputStream(new FileOutputStream(path+"/UID.txt"));
            		bs.write(str.getBytes()); 
            	}

        	} catch (Exception e1) {
                        e1.getStackTrace();
        		// TODO: handle exception
        	}finally {
        		try {
					bs.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	} 
       	
        } else if (e.getSource()==send) {
        	String str = uniqueid_field.getText();
        	String text = command_field.getText();
        	
        	if (text.length() == 0){
        		addLog("▷ 발화문 : [] / 전송결과 : 명렁어를 입력해주세요.");
        	}
        	else if(str.length() > 40) {
        		JOptionPane.showMessageDialog(null,"unique id 오류 \nunique id 40자리를 다시 확인해주세요.");
        	} else if (str.length() < 40) {
            	JOptionPane.showMessageDialog(null,"unique id 오류 \nunique id 40자리를 다시 확인해주세요.");
        	} else if (!(str.contains("APL"))) {
        		JOptionPane.showMessageDialog(null,"unique id 오류 \n올바른 unique id를 입력해주세요.");
        	} else {
        		String userid = str.substring(0, 20);
        		String deviceid = str.substring(20, 40);
        		
        		if(server == "STG"){
        			try {
						sendPost(text, userid, deviceid, "STG");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
        			addLog("▷ 발화문 : ["+text+"] / 전송결과 : "+ result);
        		} else if (server == "PRD"){
        			try {
						sendPost(text, userid, deviceid, "PRD");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
        			addLog("▷ 발화문 : ["+text+"] / 전송결과 : "+ result);
        		}	
        	}
        }
	}
	
	void addLog(String log) {
		textarea.append(log + "\n");  // 로그 내용을 JTextArea 위에 붙여주고
		textarea.setCaretPosition(textarea.getDocument().getLength());  // 맨아래로 스크롤한다.
	}
	
	public static Date getDate(int year, int month, int date) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, date);
		return new Date(cal.getTimeInMillis());
	}
	
	
	void sendPost(String command, String userID, String deviceID, String Server) throws Exception {
    	
    	System.out.println("sendPost 발동 옵션: | 발화문 :  "+ command +" | 서버 : "+ Server);
    	
    	String CommandText = command;
    	
    	JSONObject Main_jsonObject = new JSONObject();
    	
    	JSONArray usersArray = new JSONArray();
    	
    	JSONObject users_data_jsonObject = new JSONObject();
    	users_data_jsonObject.put("userId", userID);
    	
    	JSONArray deviceIdsArray = new JSONArray();
    	deviceIdsArray.add(deviceID);
    	
    	JSONArray pocIdsArray = new JSONArray();
    	pocIdsArray.add("app.apollo.agent");
    	
    	JSONArray osTypesArray = new JSONArray();
    	osTypesArray.add("");
    	
    	usersArray.add(users_data_jsonObject);
   
    	Main_jsonObject.put("users", usersArray);
    	
    	users_data_jsonObject.put("deviceIds", deviceIdsArray);
    	users_data_jsonObject.put("pocIds", pocIdsArray);
    	users_data_jsonObject.put("osTypes", osTypesArray);
    	
    	Main_jsonObject.put("playServiceId", "playServiceId");
    	Main_jsonObject.put("pushServiceType", "QA");
    	
    	
    	JSONArray directivesArray = new JSONArray();
    	
    	JSONObject directives_data_jsonObject = new JSONObject();
    	directives_data_jsonObject.put("type", "Text.TextSource");
    	directives_data_jsonObject.put("version", "1.7");
    	directives_data_jsonObject.put("text", command);
    	directives_data_jsonObject.put("token", "test");
    	directives_data_jsonObject.put("playServiceId", "");
    	directivesArray.add(directives_data_jsonObject);
    	Main_jsonObject.put("directives", directivesArray);
    	
    	JSONObject notificationRequest_data_jsonObject = new JSONObject();
    	notificationRequest_data_jsonObject.put("shouldSendPush", false);
    	notificationRequest_data_jsonObject.put("shouldSave", false);
    	notificationRequest_data_jsonObject.put("shouldSendDirective", true);
    	Main_jsonObject.put("notificationRequest", notificationRequest_data_jsonObject);
    	
    	System.out.println(Main_jsonObject);
    	/*
    	JSONObject Main_jsonObject = new JSONObject();
    	
    	JSONArray directivesArray = new JSONArray();
    	
    	JSONObject payload_data = new JSONObject();
    	payload_data.put("text", CommandText);
    	
    	
    	JSONObject header_data = new JSONObject();
    	header_data.put("messageId", "");
    	header_data.put("dialogRequestId", "");
    	header_data.put("namespace", "Text");
    	header_data.put("referrerDialogRequestId", "");
    	header_data.put("name", "TextSource");
    	header_data.put("version", "1.7");
    	
 
    	JSONObject data_jsonObject = new JSONObject();
    	data_jsonObject.put("payload", payload_data);
    	data_jsonObject.put("header", header_data);
    
    	directivesArray.add(data_jsonObject);
    	
    	
    	@SuppressWarnings("unused")
		JSONObject directives_jsonObject = new JSONObject();
    	//directives_jsonObject.put("directives", directivesArray.toString());
    	
    	Main_jsonObject.put("directives", directivesArray);
    	Main_jsonObject.put("deviceId", deviceID);  
    	*/
    	
    	//System.out.println(Main_jsonObject);
    	
    	MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    	
    	// form parameters
    	@SuppressWarnings("deprecation")
		RequestBody body = RequestBody.create(JSON, Main_jsonObject.toString());
    	
    	if (Server.equals("PRD")) {
    		Request request = new Request.Builder()
                    .url("https://eg.sktapollo.com/api/v1/push") //PRD directive URL
                    .addHeader("Auth-Token", "xZRypHEPM8RLl5KaPJnwtQ==")
                    .post(body)
                    .build();
    		
    	Response response = httpClient.newCall(request).execute();
    	String userString = response.body().string();
    	System.out.println(userString);
    	
    	if (userString.contentEquals("")) {
    		result = "result : OK";
    	} else {
    		result = userString;
    	}
           
    	} else if (Server.equals("STG")) {
    		Request request = new Request.Builder()
                    .url("https://stg-eg.sktapollo.com/api/v1/push") //STG directive URL
                    .addHeader("Auth-Token", "xZRypHEPM8RLl5KaPJnwtQ==")
                    .post(body)
                    .build();
    		
    		Response response = httpClient.newCall(request).execute();
        	String userString = response.body().string();
        	System.out.println(userString);
        	
        	if (userString.contentEquals("")) {
        		result = "result : OK";
        	} else {
        		result = userString;
        	}
    	}
    	
    	/*
    	if (Server.equals("PRD")) {
    		Request request = new Request.Builder()
                    .url("https://api.sktnugu.com/v1/setting/deviceGateway/directive") //PRD directive URL
                    .addHeader("User-Id", userID)
                    .addHeader("Device-Id", deviceID)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "token 14bd0daf-2946-4d42-8f33-5cb59ed38761")
                    .post(body)
                    .build();
    		
    	Response response = httpClient.newCall(request).execute();
    	String userString = response.body().string();
    	
    	if (userString.contains("OK")) {
    		result = "result : OK";
    	} else {
    		result = userString;
    	}
           
    	} else if (Server.equals("STG")) {
    		Request request = new Request.Builder()
                    .url("https://stg-api.sktnugu.com/v1/setting/deviceGateway/directive") //STG directive URL
                    .addHeader("User-Id", userID)
                    .addHeader("Device-Id", deviceID)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "token 517aad07-3353-42f7-98a9-52a61db0f359")
                    .post(body)
                    .build();
    		
    		Response response = httpClient.newCall(request).execute();
        	String userString = response.body().string();
        	
        	if (userString.contains("OK")) {
        		result = "result : OK";
        	} else {
        		result = userString;
        	}
    	} */
	}
}