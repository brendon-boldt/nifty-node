//import java.net.HttpURLConnection;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class Client {

  public static void main(String[] args) {
    String queryIdString = newQuery("Philo?","Socrates");
    System.out.println(queryIdString);
    JSONObject query = getQuery(queryIdString);
    System.out.println(query.toString());
    String responseIdString = respondToQuery(queryIdString,"Plato");
    System.out.println(responseIdString);
  }

  // This connection may retry/resend if no response is given.
  // If this is the case, it cannot be allowed.
  public static char[] httpPost(String urlString, byte[] data) {
    HttpURLConnection con = null;
    char[] buffer = new char[0x400];
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
      in.read(buffer);

    } catch (Exception e) {
      e.printStackTrace(); 
    } finally {
      if (con != null) {
        con.disconnect();
      }
    }
    int i;
    for (i = 0; i < buffer.length; i++) if (buffer[i] == 0) break;
    char[] response = new char[i];
    System.arraycopy(buffer, 0, response, 0, i);
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
    String queryIdString = null;
    String queryString = "{\"question\":\"" + question + "\",\"answer\":\"" + answer + "\"}";
    String dataString = "{\"query\":" + queryString + "}";
    byte[] data = dataString.getBytes(StandardCharsets.UTF_8);
    //queryIdString = new String (httpPost("http://localhost:8000/new", data),"UTF-8");
    queryIdString = new String(httpPost("http://localhost:8000/new", data));
    return queryIdString;
  }

  public static JSONObject getQuery(String idString) {
    String idJsonString = "{\"queryId\":\"" + idString + "\"}";
    char[] queryData = httpPost("http://localhost:8000/get",new byte[0]);
    //char[] queryData = httpPost("http://localhost:8000/get",
    //    idJsonString.getBytes(StandardCharsets.UTF_8));
    //System.out.println(queryData.toString());
    JSONObject queryJson = null;
    try {
       //queryJson = new JSONObject(new String(queryData,"UTF-8"));
       queryJson = new JSONObject(new String(queryData));
    } catch (Exception e) {e.printStackTrace(); }
    return queryJson;
  }

  public static String respondToQuery(String idString, String response) {
    String jsonString = "{\"queryId\":\"" + idString + "\",\"response\":{\"text\":\"" + response + "\"}}";
    System.out.println(jsonString.length());
    return new String(httpPost("http://localhost:8000/respond",
          jsonString.getBytes(StandardCharsets.UTF_8)));
  }

}
