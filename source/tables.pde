void initTables() {

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

Table loaddata(String dataFile, Table table) {
  return table=loadTable(dataFile, table);
}

Table loadTable(String fileName, Table table) {
  table = loadTable(fileName, "header");
  println("loaded", table.getRowCount(), "rows from", fileName);
  return table;
}

Table sortTable(Table tbl, String sortByColumn) {
  tbl.sort(sortByColumn);
  println("table", tbl, "sorted by", sortByColumn);
  return tbl;
}

Table selectCircles(Table table, Location from, Location to) {
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

Table selectArcs(Table table, Location from, Location to) {
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
