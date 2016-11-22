import com.google.gson.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
public class WikiDegrees{
  private static String readUrl(String urlString) throws Exception {
      BufferedReader reader = null;
      try {
          URL url = new URL(urlString);
          reader = new BufferedReader(new InputStreamReader(url.openStream()));
          StringBuffer buffer = new StringBuffer();
          int read;
          char[] chars = new char[1024];
          while ((read = reader.read(chars)) != -1)
              buffer.append(chars, 0, read);

          return buffer.toString();
      } finally {
          if (reader != null)
              reader.close();
      }
  }

  public static String getMostRelevantTopic(String searchQuery) throws Exception{
    String json = readUrl("https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch="+URLEncoder.encode(searchQuery, "UTF-8")+"&format=json");

    Gson gson = new Gson();
    JsonObject rootObj = new JsonParser().parse(json).getAsJsonObject();
    JsonArray locObj = rootObj.getAsJsonObject("query").getAsJsonArray("search");
    Type listType = new TypeToken<List<WikipediaSearchResponse>>() {}.getType();
    return (String)((List<WikipediaSearchResponse>)gson.fromJson(locObj, listType)).get(0).title;
  }
  public static void main(String[] args) throws Exception {
    String startTopic = getMostRelevantTopic(args[0]);
    String endTopic = getMostRelevantTopic(args[1]);
    System.out.println("Start Topic: "+startTopic);
    System.out.println("End Topic: "+endTopic);
  }
}
