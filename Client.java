//import java.net.HttpURLConnection;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class Client {
   
  public static void main( String[] args) {
    String idString = newQuery("Philo?","Socrates");
    System.out.println(idString);
    JSONObject query = getQuery(idString);
    System.out.println(query.toString());
  }
 
  public static char[] httpPost(String urlString, byte[] data) {
    HttpURLConnection con = null;
    char[] response = new char[0x400];
    try {
      URL url = new URL(urlString);
      con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("POST");
      //con.setRequestProperty("Content-Type"
      con.setRequestProperty("charset","utf-8");
      con.setRequestProperty("Content-Length", Integer.toString(data.length));
      con.setUseCaches(false);
      con.setDoInput (true);
      con.setDoOutput(true);

      DataOutputStream out = new DataOutputStream (con.getOutputStream ());
      out.write(data);
      out.flush();
      out.close();

      BufferedReader in = new BufferedReader(new InputStreamReader(
            con.getInputStream(),"UTF-8"));
      in.read(response);

    } catch (Exception e) {
      e.printStackTrace(); 
    } finally {
      if (con != null) {
        con.disconnect();
      }
    }
    return response;

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
   
  public static String newQuery(String question, String answer) {
    String idString = null;
    String dataString = "{\"question\":\"" + question + "\",\"answer\":\"" + answer + "\"}";
    byte[] data = dataString.getBytes(StandardCharsets.UTF_8);
    //idString = new String (httpPost("http://localhost:8000/new", data),"UTF-8");
    idString = new String(httpPost("http://localhost:8000/new", data));
    return idString;
  }

  public static JSONObject getQuery(String idString) {
    char[] queryData = httpPost("http://localhost:8000/get",
        idString.getBytes(StandardCharsets.UTF_8));
    //System.out.println(queryData.toString());
    JSONObject queryJson = null;
    try {
       //queryJson = new JSONObject(new String(queryData,"UTF-8"));
       queryJson = new JSONObject(new String(queryData));
    } catch (Exception e) {e.printStackTrace(); }
    return queryJson;
  }

}
