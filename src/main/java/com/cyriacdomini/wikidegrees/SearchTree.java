package com.cyriacdomini.wikidegrees;

import java.util.*;
public class SearchTree{
  SearchTree parent;
  String data;
  List<SearchTree> children;
  public SearchTree(String data, SearchTree parent){
    this.parent = parent;
    if(this.parent != null){
      this.parent.addChild(this);
    }
    this.data = data;
    this.children = new ArrayList<SearchTree>();
  }
  public void addChild(SearchTree child){
    child.setParent(this);
    this.children.add(child);
  }

  public void setParent(SearchTree parent){
    this.parent = parent;
  }

  public SearchTree getParent(){
    return this.parent;
  }

  public List<SearchTree> getChildren(){
    return this.children;
  }

  public void setData(String data){
    this.data = data;
  }

  public String getData(){
    return this.data;
  }
}
