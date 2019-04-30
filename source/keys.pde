void processKeys(char kbd_key) {
  println("got key:", hex(kbd_key));
  delay(100);
  if (kbd_key=='Q') {
    drawGSM=!drawGSM;
    println(drawGSM);
  }
  //if (hex(kbd_key).equals("0034")) {
    if (kbd_key=='W') {
    drawLTE=!drawLTE;
    println(kbd_key);
  }
  if (kbd_key=='E') {
  println("loading user data");
  source=loaddata("results.csv", result);// V v V
  surceLoaded=!surceLoaded;
  route=loaddata("inputs.csv", source); //да, блин, именно так.
  routeLoaded=!routeLoaded;
}

    if (kbd_key=='A') {
   
    result.clearRows();
    Location lowerLeftCorner=getLocation(200, height);
    Location upperRightcorner=getLocation(width, 0);
    if (drawGSM==true) {
    selectCircles(GSMCells, lowerLeftCorner, upperRightcorner);
    println("r",result.getRowCount(),"G",GSMresult.getRowCount(),"L",LTEresult.getRowCount());
    }
    if (drawLTE==true) {
    selectArcs(LTECells, lowerLeftCorner, upperRightcorner);
    println("r",result.getRowCount(),"G",GSMresult.getRowCount(),"L",LTEresult.getRowCount());
    }
  
  }

  if (kbd_key==' ') {
map.zoomAndPanTo(initLocation, 12);
}

if (hex(kbd_key).equals("000A")) {
  String searchFieldText=cp5.get(Textfield.class,"find cell").getText();
  
  if(searchFieldText.length()>0){
    drawSearchResults=true;
    positioned=false;
    searchText=searchFieldText;
  }
  //showCell(cp5.get(Textfield.class,"find cell").getText());
  //drawSearchResults=true;
}

}
