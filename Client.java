//import java.net.HttpURLConnection;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Client {
  
  public static void main( String[] args) {
    //getFile("http://localhost:8000/file?name=db.js");
    //getFile("http://localhost:8000/new");
    //getFile2("http://localhost:8000/new");
    newQuery("","");
  }
  
  public static void newQuery(String question, String answer) {
    System.out.println(question+"\n"+answer);
    byte[] data = "{\"question\":\"Philosopher?\",\"answer\":\"Socrates\"}"
      .getBytes(StandardCharsets.UTF_8);
    HttpURLConnection con = null;
    try {
      String urlString = "http://localhost:8000/new";
      URL url = new URL(urlString);
      con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("POST");
      //con.setRequestProperty("Content-Type"
      con.setRequestProperty("charset","utf-8");
      con.setRequestProperty("Content-Length", Integer.toString(data.length));
      con.setUseCaches(false);
      con.setDoInput (true);
      con.setDoOutput(true);

      DataOutputStream dos = new DataOutputStream (con.getOutputStream ());
      dos.write(data);
      dos.flush();
      dos.close();

      con.getInputStream();

    } catch (Exception e) {
      e.printStackTrace(); 
    } finally {
      if (con != null) {
        con.disconnect();
      }
    }

  }

  public static void getFile(String urlString) {
    HttpURLConnection con = null;
    try {
      URL url = new URL(urlString);
      con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");

      InputStream is = con.getInputStream();
      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
      String line;
      while ((line = rd.readLine()) != null) {
        System.out.println(line);
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }

  public static void getFile2(String urlString) {
    byte[] data = "{\"question\":\"Philosopher?\",\"answer\":\"Socrates\"}"
      .getBytes(StandardCharsets.UTF_8);
    HttpURLConnection con = null;
    try {
      URL url = new URL(urlString);
      con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");

      con.setRequestProperty("charset","utf-8");
      con.setRequestProperty("Content-Length", Integer.toString(data.length));
      con.setUseCaches(false);
      con.setDoInput (true);
      con.setDoOutput(true);

      DataOutputStream dos = new DataOutputStream (con.getOutputStream ());
      dos.write(data);
      dos.flush();
      dos.close();

      InputStream is = con.getInputStream();
      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
      String line;
      while ((line = rd.readLine()) != null) {
        System.out.println(line);
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
}
