void drawCircle(Table points, int r, int g, int b) {
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
    if (s>0.01) {
      ellipse(markPos.x, markPos.y, 2, 2);
    } else {
      point(markPos.x, markPos.y);
    }
    fill(r, g, b, 10);
    //ellipse(markPos.x, markPos.y, 3, 3);// радиус из таблицы
    if (s>0.02&&((lat<mickeyTL.x)&&(lat>mickeyBR.x)&&(lon>mickeyTL.y)&&(lon<mickeyBR.y))) {
      ellipse(markPos.x, markPos.y, (radius*1.13*2)*s, (radius*1.13*2)*s);// радиус из таблицы
      fill(255, 255, 255);
      c++;
      text(("r:"+radius+", id:"+id), mouseX, mouseY+(c*15)); //указания ИД вышки и радиуса - долго
    }
  }
  c=0;
}


void drawArc(Table LTEpoints, int r, int g, int b) { 
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
      if (s>0.01) {
        ellipse(markPos.x, markPos.y, 2, 2);
      } else {
        point(markPos.x, markPos.y);
      }
      if (s>0.02&&(lat<mickeyTL.x)&&(lat>mickeyBR.x)&&(lon>mickeyTL.y)&&(lon<mickeyBR.y)) {

        c++;
        for (int j=0; j<360; j=j+10) {//рисуем арку
          float outterX=markPos.x+(sin(radians(j))*(radius*1.13)*s);
          float outterY=markPos.y-(cos(radians(j))*(radius*1.13)*s);
          Location edge = map.getLocation(outterX, outterY);
          //fill(255, 255, 255, 10);
          //stroke(255, 255, 255);
          text(("id:"+id+" starta: "+starta+" stopa: "+stopa+" radius:"+radius), mouseX, mouseY+(c*14));
          if (j<sectorAngle && j>starta ) {
            // stroke(0, 255, 0, 100);
            line(markPos.x, markPos.y, outterX, outterY);
            if (j%10==0 || j==0) {
              float x=markPos.x+(sin(radians(j))*(radius*1.23)*s);
              float y=markPos.y-(cos(radians(j))*(radius*1.23)*s);
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

                float x=markPos.x+(sin(radians(j))*(radius*1.23)*s);
                float y=markPos.y-(cos(radians(j))*(radius*1.23)*s);
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



void drawroute(Table points, int r, int g, int b, int tw) {
  float s = map.getZoom()/100000;
  int rows = points.getRowCount();
  int c=1;
  String id;
  int pointsize=1;
  float oldx=0.09876543211111;
  float oldy=0.09876543211111;
  strokeWeight(tw);
  for (TableRow row : points.rows()) {
    double lat=row.getFloat("lat"); 
    double lon=row.getFloat("lon"); 
    float radius=row.getFloat("radius"); 
    Location markerlocation = new Location(lat, lon);
    ScreenPosition markPos=map.getScreenPosition(markerlocation);
    if (oldx==0.09876543211111) {
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
    if (s>0.01) {
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
    ellipse(markPos.x, markPos.y, (radius*1.13*2)*s, (radius*1.13*2)*s);// радиус из таблицы
    if (s>0.02&&((lat<mickeyTL.x)&&(lat>mickeyBR.x)&&(lon>mickeyTL.y)&&(lon<mickeyBR.y))) {       
      c++;
      fill(255, 255, 255);
      text(("r:"+radius+", id:"+id), mouseX+xshift, mouseY-(c*15)); //указания ИД вышки и радиуса - долго
    }
  }
  c=0;
}

void showCell(String cell) {
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
