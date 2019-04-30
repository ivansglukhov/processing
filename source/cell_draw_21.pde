
import controlP5.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;  
import de.fhpotsdam.unfolding.providers.OpenStreetMap.*;
import processing.net.*;
import controlP5.*;

//=====================
//UI
//=====================

Textarea myTextarea;
Println console;
//=====================

UnfoldingMap map;
ScreenPosition screenPos1;
Location markerLocation;
Table coordTable, GSMresult, LTEresult, GSMCells, LTECells, pinned, result,source,route,searchResultTable,searchResultGSM,searchResultLTE;
boolean drawGSM=true;
boolean drawLTE=true;
boolean drawSearchResults=false;
boolean surceLoaded=false;
boolean routeLoaded=false;
boolean positioned=true;
float oh,ow;
String searchText;
Location initLocation = new Location(56.3035, 43.9027);


ControlP5 cp5;
void setup() {

  size(1200, 800,FX2D);//FX2D for p3
  surface.setResizable(true);
  initMap();  

  
  initUi();
  initTables();
  
  println("loading data");
  GSMCells=loaddata("GSM_location.csv", GSMCells);
  LTECells=loaddata("LTE_location.csv", LTECells);
  println("press RIGHT mouse button to draw cells");
  map.zoomAndPanTo(initLocation, 15);
}

void draw() {
  if(oh!=height){
  initUi();
  initMap(); 
    oh=height;
  }
  // if(ow!=width){
  //initUi();
  //  ow=width;
  //}
  background(200);
  drawMap();
  fill(0, 0, 0, 200);
  rect(200, 0, width, height);
 Location mouseHover = map.getLocation(mouseX,mouseY);

 

    if(surceLoaded==true){
    drawroute(source, 0, 5, 200,2);
  }
   if(routeLoaded==true){
    drawroute(route, 255, 0, 0,1);
  }
  
  if (drawGSM==true) {
    drawCircle(GSMresult, 255, 50, 0);
  }
    if (drawLTE==true) {
    drawArc(LTEresult, 30, 250, 0);
  }
    if (drawSearchResults==true) {
    showCell(searchText);
  }
  
   fill(255);
text(("lat:"+mouseHover.x+"lon"+mouseHover.y),mouseX,mouseY);
}

void keyPressed() {
  processKeys(key);
  fill(30, 30, 30);
}

void mouseReleased() {
  // println(map.getZoom());
  if (mouseButton == RIGHT) {
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
}

void mouseWheel() {
  Location lowerLeftCorner=getLocation(200, 0);
  Location upperRightcorner=getLocation(width, height);
  // println("from:", lowerLeftCorner, "to:", upperRightcorner);
}
