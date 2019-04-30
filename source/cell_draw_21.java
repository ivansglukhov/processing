import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 
import de.fhpotsdam.unfolding.*; 
import de.fhpotsdam.unfolding.geo.*; 
import de.fhpotsdam.unfolding.utils.*; 
import de.fhpotsdam.unfolding.providers.OpenStreetMap.*; 
import processing.net.*; 
import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class cell_draw_21 extends PApplet {





  




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
Location initLocation = new Location(56.3035f, 43.9027f);


ControlP5 cp5;
public void setup() {

  //FX2D for p3
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

public void draw() {
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

public void keyPressed() {
  processKeys(key);
  fill(30, 30, 30);
}

public void mouseReleased() {
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

public void mouseWheel() {
  Location lowerLeftCorner=getLocation(200, 0);
  Location upperRightcorner=getLocation(width, height);
  // println("from:", lowerLeftCorner, "to:", upperRightcorner);
}
public void drawCircle(Table points, int r, int g, int b) {
  float s = map.getZoom()/100000;
  int rows = points.getRowCount();
  int c=1;
  int pointsize=1;

  for (TableRow row : points.rows()) {
    double lat=row.getFloat("lat"); 
    double lon=row.getFloat("lon"); 
    float radius=row.getFloat("radius"); 
    Location markerlocation = new Location(lat, lon);
    ScreenPosition markPos=map.getScreenPosition(markerlocation);
    Location mickeyTL = map.getLocation(mouseX-20, mouseY-20);
    Location mickeyBR = map.getLocation(mouseX+20, mouseY+20);
    int id =row.getInt( "cellid");
    stroke(255, 150, 0, 200);
    if (s>0.01f) {
      ellipse(markPos.x, markPos.y, 2, 2);
    } else {
      point(markPos.x, markPos.y);
    }
    fill(r, g, b, 10);
    //ellipse(markPos.x, markPos.y, 3, 3);// радиус из таблицы
    if (s>0.02f&&((lat<mickeyTL.x)&&(lat>mickeyBR.x)&&(lon>mickeyTL.y)&&(lon<mickeyBR.y))) {
      ellipse(markPos.x, markPos.y, (radius*1.13f*2)*s, (radius*1.13f*2)*s);// радиус из таблицы
      fill(255, 255, 255);
      c++;
      text(("r:"+radius+", id:"+id), mouseX, mouseY+(c*15)); //указания ИД вышки и радиуса - долго
    }
  }
  c=0;
}


public void drawArc(Table LTEpoints, int r, int g, int b) { 
  float s = map.getZoom()/100000;
  float prevX=0;
  float prevY=0;
  float lat, lon, starta, stopa, inrad, orad, sectorAngle; 
  int id, radius;
  int c=1;

  for (TableRow row : LTEpoints.rows()) {
    lat=row.getFloat("lat");
    lon=row.getFloat("lon");
    starta=row.getFloat("starta");
    stopa= row.getFloat("stopa");
    if (stopa==0) {
      stopa=180;
    }
    sectorAngle=starta+stopa;
    id=row.getInt("cellid");
    radius=row.getInt("radius");
    Location markerlocation = new Location(lat, lon);
    ScreenPosition markPos=map.getScreenPosition(markerlocation);
    Location TL = map.getLocation(0, 0);
    Location BR = map.getLocation(width, height);
    Location mickeyTL = map.getLocation(mouseX-20, mouseY-20);
    Location mickeyBR = map.getLocation(mouseX+20, mouseY+20);
    if ((lat<TL.x)&&(lat>BR.x)&&(lon>TL.y)&&(lon<BR.y)) {
      stroke(r, g, b, 222);
      fill(r, g, b, 10);
      if (s>0.01f) {
        ellipse(markPos.x, markPos.y, 2, 2);
      } else {
        point(markPos.x, markPos.y);
      }
      if (s>0.02f&&(lat<mickeyTL.x)&&(lat>mickeyBR.x)&&(lon>mickeyTL.y)&&(lon<mickeyBR.y)) {

        c++;
        for (int j=0; j<360; j=j+10) {//рисуем арку
          float outterX=markPos.x+(sin(radians(j))*(radius*1.13f)*s);
          float outterY=markPos.y-(cos(radians(j))*(radius*1.13f)*s);
          Location edge = map.getLocation(outterX, outterY);
          //fill(255, 255, 255, 10);
          //stroke(255, 255, 255);
          text(("id:"+id+" starta: "+starta+" stopa: "+stopa+" radius:"+radius), mouseX, mouseY+(c*14));
          if (j<sectorAngle && j>starta ) {
            // stroke(0, 255, 0, 100);
            line(markPos.x, markPos.y, outterX, outterY);
            if (j%10==0 || j==0) {
              float x=markPos.x+(sin(radians(j))*(radius*1.23f)*s);
              float y=markPos.y-(cos(radians(j))*(radius*1.23f)*s);
              //line(markPos.x, markPos.y, x, y);
              fill(255, 255, 0, 255);
              text(j, x, y);
            }
          }
          if (sectorAngle>360) {
            if (j>0 && j<sectorAngle-360 || j==0) {
              // stroke(0, 255, 0, 100);
              line(markPos.x, markPos.y, outterX, outterY);
              if (j%10==0 || j==0) {

                float x=markPos.x+(sin(radians(j))*(radius*1.23f)*s);
                float y=markPos.y-(cos(radians(j))*(radius*1.23f)*s);
                //  fill(255, 255, 255, 200);
                //  stroke(255, 255, 255, 50);
                line(markPos.x, markPos.y, x, y);
                //ellipse(outterX, outterY, 2, 2);
                fill(255, 255, 0, 255);
                text(j, x, y);
              }
            }
          }
        }
      }
    }


    //  prevX=markPos.x;
    //  prevY=markPos.y;
  }
  c=0;
}



public void drawroute(Table points, int r, int g, int b, int tw) {
  float s = map.getZoom()/100000;
  int rows = points.getRowCount();
  int c=1;
  String id;
  int pointsize=1;
  float oldx=0.09876543211111f;
  float oldy=0.09876543211111f;
  strokeWeight(tw);
  for (TableRow row : points.rows()) {
    double lat=row.getFloat("lat"); 
    double lon=row.getFloat("lon"); 
    float radius=row.getFloat("radius"); 
    Location markerlocation = new Location(lat, lon);
    ScreenPosition markPos=map.getScreenPosition(markerlocation);
    if (oldx==0.09876543211111f) {
      oldx=markPos.x;
      oldy=markPos.y;
    }

    Location mickeyTL = map.getLocation(mouseX-20, mouseY-20);
    Location mickeyBR = map.getLocation(mouseX+20, mouseY+20);
    int xshift=-70;
    if (points.getColumnCount()>3) {
      xshift=0;
      id=row.getString( "date");
    } else { 
      id=str(c);
    }
    stroke(r, g, b, 255);
    if (s>0.01f) {
      ellipse(markPos.x, markPos.y, 2, 2);
    } else {
      point(markPos.x, markPos.y);
    }
    fill(r, g, b, 5);
    //ellipse(markPos.x, markPos.y, 3, 3);// радиус из таблицы
    // if (s>0.02&&((lat<mickeyTL.x)&&(lat>mickeyBR.x)&&(lon>mickeyTL.y)&&(lon<mickeyBR.y))) {
    line(oldx, oldy, markPos.x, markPos.y);   
    oldx=markPos.x;
    oldy=markPos.y;
    ellipse(markPos.x, markPos.y, (radius*1.13f*2)*s, (radius*1.13f*2)*s);// радиус из таблицы
    if (s>0.02f&&((lat<mickeyTL.x)&&(lat>mickeyBR.x)&&(lon>mickeyTL.y)&&(lon<mickeyBR.y))) {       
      c++;
      fill(255, 255, 255);
      text(("r:"+radius+", id:"+id), mouseX+xshift, mouseY-(c*15)); //указания ИД вышки и радиуса - долго
    }
  }
  c=0;
}

public void showCell(String cell) {
 searchResultGSM.clearRows();
 searchResultLTE.clearRows();
 // println("searching", cell);
  TableRow resultRow = LTECells.findRow(cell, "cellid");
  if (resultRow != null) {
 searchResultLTE.addRow(resultRow);
    Location cellLocation=new Location(resultRow.getFloat("lat"),resultRow.getFloat("lon"));
    
    drawArc(searchResultLTE, 30, 250, 0);
    if(positioned==false){
  map.zoomAndPanTo(cellLocation, 15);
  positioned=true;
}
    
  } else {
     resultRow = GSMCells.findRow(cell, "cellid");
   // println(resultRow);
    if (resultRow != null) {
      
    searchResultGSM.addRow(resultRow);
    Location cellLocation=new Location(resultRow.getFloat("lat"),resultRow.getFloat("lon"));
    
    drawCircle(searchResultGSM, 50, 50, 50);
if(positioned==false){
  map.zoomAndPanTo(cellLocation, 15);
  positioned=true;
}
    }
  }
}


//void showCell(String cell) {
//  //cellLocation
  
//      result.clearRows();
 
//  println("searching", cell);
//  TableRow resultRow = LTECells.findRow(cell, "cellid");
//  if (resultRow != null) {
//    println(resultRow.getFloat("lat"));
//    Location cellLocation=new Location(resultRow.getFloat("lat"),resultRow.getFloat("lon"));
//    map.zoomAndPanTo(cellLocation, 15);
//       Location lowerLeftCorner=getLocation(200, height);
//    Location upperRightcorner=getLocation(width, 0);
//     if (drawGSM==true) {
//    selectCircles(GSMCells, lowerLeftCorner, upperRightcorner);
//    println("r",result.getRowCount(),"G",GSMresult.getRowCount(),"L",LTEresult.getRowCount());
//    }
//    if (drawLTE==true) {
//    selectArcs(LTECells, lowerLeftCorner, upperRightcorner);
//    println("r",result.getRowCount(),"G",GSMresult.getRowCount(),"L",LTEresult.getRowCount());
//    }
    
    
//  } else {
//    cp5.get(Textfield.class, "find cell").setText("no cell");
//    resultRow = GSMCells.findRow(cell, "cellid");
//    if (result != null) {
//    Location cellLocation=new Location(resultRow.getFloat("lat"),resultRow.getFloat("lon"));
//    map.zoomAndPanTo(cellLocation, 15);
//       Location lowerLeftCorner=getLocation(200, height);
//    Location upperRightcorner=getLocation(width, 0);
//     if (drawGSM==true) {
//    selectCircles(GSMCells, lowerLeftCorner, upperRightcorner);
//    println("r",result.getRowCount(),"G",GSMresult.getRowCount(),"L",LTEresult.getRowCount());
//    }
//    if (drawLTE==true) {
//    selectArcs(LTECells, lowerLeftCorner, upperRightcorner);
//    println("r",result.getRowCount(),"G",GSMresult.getRowCount(),"L",LTEresult.getRowCount());
//    }
//    }
//  }
//}
public void processKeys(char kbd_key) {
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
public void initMap() {
  map = new UnfoldingMap(this, new OpenStreetMapProvider());
  // map = new UnfoldingMap(this, "static", 200, 0, width, height);
  MapUtils.createDefaultEventDispatcher(this, map);
}

public void drawMap() {
  map.draw();
}

public Location getLocation(float x, float y) {
  Location loc=map.getLocation(x, y);
  return loc;
}

public void initTables() {

  searchResultGSM=new Table();
  searchResultGSM.addColumn("cellid", Table.INT);
  searchResultGSM.addColumn("lat", Table.FLOAT);
  searchResultGSM.addColumn("lon", Table.FLOAT);
  searchResultGSM.addColumn("radius", Table.FLOAT);
  searchResultGSM.addRow();


  searchResultLTE=new Table();
  searchResultLTE.addColumn("cellid", Table.INT);
  searchResultLTE.addColumn("lat", Table.FLOAT);
  searchResultLTE.addColumn("lon", Table.FLOAT);
  searchResultLTE.addColumn("radius", Table.FLOAT);
  searchResultLTE.addColumn("orad", Table.FLOAT);
  searchResultLTE.addColumn("starta", Table.FLOAT);
  searchResultLTE.addColumn("stopa", Table.FLOAT);
  searchResultLTE.addColumn("inrad", Table.FLOAT);
  searchResultLTE.addRow();


  result=new Table();
  result.addColumn("cellid", Table.INT);
  result.addColumn("lat", Table.FLOAT);
  result.addColumn("lon", Table.FLOAT);
  result.addColumn("radius", Table.FLOAT);
  result.addColumn("orad", Table.FLOAT);
  result.addColumn("starta", Table.FLOAT);
  result.addColumn("stopa", Table.FLOAT);
  result.addColumn("inrad", Table.FLOAT);
  result.addRow();


  GSMresult=new Table();
  GSMresult.addColumn("cellid", Table.INT);
  GSMresult.addColumn("lat", Table.FLOAT);
  GSMresult.addColumn("lon", Table.FLOAT);
  GSMresult.addColumn("radius", Table.FLOAT);
  //GSMresult.addColumn("orad", Table.FLOAT);
  //GSMresult.addColumn("starta", Table.FLOAT);
  //GSMresult.addColumn("stopa", Table.FLOAT);
  //GSMresult.addColumn("inrad", Table.FLOAT);
  GSMresult.addRow();


  //cellid,lat,lon,radius,orad,starta,stopa,inrad
  LTEresult=new Table();
  LTEresult.addColumn("cellid", Table.INT);
  LTEresult.addColumn("lat", Table.FLOAT);
  LTEresult.addColumn("lon", Table.FLOAT);
  LTEresult.addColumn("radius", Table.FLOAT);
  LTEresult.addColumn("orad", Table.FLOAT);
  LTEresult.addColumn("starta", Table.FLOAT);
  LTEresult.addColumn("stopa", Table.FLOAT);
  LTEresult.addColumn("inrad", Table.FLOAT);
  LTEresult.addRow();

  pinned=new Table();
  pinned.addColumn("cellid", Table.INT);
}

public Table loaddata(String dataFile, Table table) {
  return table=loadTable(dataFile, table);
}

public Table loadTable(String fileName, Table table) {
  table = loadTable(fileName, "header");
  println("loaded", table.getRowCount(), "rows from", fileName);
  return table;
}

public Table sortTable(Table tbl, String sortByColumn) {
  tbl.sort(sortByColumn);
  println("table", tbl, "sorted by", sortByColumn);
  return tbl;
}

public Table selectCircles(Table table, Location from, Location to) {
  println(table.getRowCount());
  GSMresult.clearRows();
  int counter=0;
  float fromLat, toLat, fromLon, toLon;
  fromLat=from.getLat();
  toLat=to.getLat();
  fromLon=from.getLon();
  toLon=to.getLon();
  for (TableRow row : table.rows()) {
    float tblLat=row.getFloat("lat");
    float tblLon=row.getFloat("lon");
    float radius=row.getFloat("radius");
    if ((tblLat>fromLat)&&(tblLon>fromLon)&&(tblLat<toLat)&&(tblLon<toLon)) { 
      counter++;
      GSMresult.addRow(row);
    }
  }
  println(counter);
  return result;
}

public Table selectArcs(Table table, Location from, Location to) {
  println(table.getRowCount());
  LTEresult.clearRows();
  int counter=0;
  float fromLat, toLat, fromLon, toLon;
  fromLat=from.getLat();
  toLat=to.getLat();
  fromLon=from.getLon();
  toLon=to.getLon();
  for (TableRow row : table.rows()) {
    float tblLat=row.getFloat("lat");
    float tblLon=row.getFloat("lon");
    float radius=row.getFloat("radius");
    if ((tblLat>fromLat)&&(tblLon>fromLon)&&(tblLat<toLat)&&(tblLon<toLon)) { 
      counter++;
      LTEresult.addRow(row);
    }
  }
  println(counter);
  return result;
}
public void initUi() {
  cp5 = new ControlP5(this);
//  cp5.enableShortcuts();
 
//init console window
 myTextarea = cp5.addTextarea("txt")
                  .setPosition(0,0)
                  .setSize(300,height)
                  .setFont(createFont("", 15))
                  .setLineHeight(17)
                  .setColor(color(250))
                  .setColorBackground(color(100, 255))
                  .setColorForeground(color(100, 255));
  ;
  console = cp5.addConsole(myTextarea);
 
       //fill(0,0,100);
       //rect(width-50,50,20,20);
       
       PFont font = createFont("arial",20);
       
       cp5.addTextfield("find cell")
     .setPosition(width-220,100)
     .setSize(200,40)
     .setFont(font)
     .setFocus(true)
     .setColor(color(255,0,0))
     ;
             

}
  public void settings() {  size(1200, 800,FX2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "cell_draw_21" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
