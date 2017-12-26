package com.cyriacdomini.wikidegrees;

import java.util.*;
import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.util.regex.*;

public class WikiDegreesBFS{
  SearchTree searchTree;
  List<String> done;
  CacheController cache;
  
    public WikiDegreesBFS(){
      this.cache = new CacheController();
    }
  
  public boolean findDegreesOfSeparation(String startTopic, String endTopic) throws Exception{
    searchTree = new SearchTree(startTopic, null);
    SearchTree tempSearchTree = searchTree;
    done = new ArrayList<String>();
    boolean finished =false;
    LinkedList<SearchTree> priorityQueue = new LinkedList<SearchTree>();
    priorityQueue.add(searchTree);
    while(priorityQueue.size()>0){
      SearchTree currentTopic = priorityQueue.remove();
      if(done.contains((currentTopic.data.replace(" ", "")))){
        continue;
      }
      done.add(currentTopic.data.replace(" ", ""));
      // System.out.println(currentTopic.data);
      LinkedList<String> childrenString = getChildrenTopics(currentTopic.data);

      for(String child: childrenString){
        SearchTree childTreeNode = new SearchTree(child, currentTopic);
        priorityQueue.add(childTreeNode);
        if(child.replace(" ", "").equalsIgnoreCase(endTopic.replace(" ",""))){
          System.out.println("\n-----");
          while(childTreeNode!=null){
            System.out.print(childTreeNode.data + " <- ");
            childTreeNode = childTreeNode.parent;
          }
          System.out.println("\n-----");
          return true;
        }
      }

    }

    return false;
  }

  LinkedList<String> getChildrenTopics(String topic)throws Exception{
    System.out.print(".");
    LinkedList<String> topicsReturned = new LinkedList<String>();
    String json = WikiDegrees.readUrl("https://en.wikipedia.org/w/api.php?action=query&titles="+URLEncoder.encode(topic, "UTF-8")+"&prop=revisions&rvprop=content&format=json");
    // System.out.println(cache.isChached(topic));
    if(cache.isChached(topic)){
      return cache.getFromCache(topic);
    }
    JsonObject rootObj = new JsonParser().parse(json).getAsJsonObject();
    JsonObject locObj = rootObj.getAsJsonObject("query").getAsJsonObject("pages");
    if(locObj == null){
      // System.out.println("Did not get any links");
      return topicsReturned;
    }
    Set<Map.Entry<String, JsonElement>> entries = locObj.entrySet();
    JsonElement revisionKey = null;
    for (Map.Entry<String, JsonElement> entry: entries) {
      revisionKey = entry.getValue();
      break;
    }
    if(revisionKey==null){
      System.exit(1);
    }
    JsonArray locArr  = ((JsonObject)revisionKey).getAsJsonArray("revisions");
    List<JsonObject> response = new Gson().fromJson(locArr, new TypeToken<List<JsonObject>>() {}.getType());
    if(response == null || response.size()<1) {
      // System.out.println("Did not get any links");
      return topicsReturned;
    };
    String wikiContent = response.get(0).getAsJsonPrimitive("*").getAsString();
    Matcher m = Pattern.compile("\\[\\[[^:\\[\\]]+\\]\\]").matcher(wikiContent);
    while (m.find()) {
      String temp = m.group();
      // System.out.print(temp);
      temp = temp.replace("[", "");
      temp = temp.replace("]", "");
      // System.out.println(" -> "+temp);
      if(temp.contains("|")){
        temp = temp.split("\\|")[0];
      }
      // System.out.println(" -> "+temp);
      topicsReturned.add(temp);
      // allMatches.add(m.group());
    }
    cache.addToChache(topic, topicsReturned);
    return topicsReturned;
  }
}
