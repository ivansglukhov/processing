void initMap() {
  map = new UnfoldingMap(this, new OpenStreetMapProvider());
  // map = new UnfoldingMap(this, "static", 200, 0, width, height);
  MapUtils.createDefaultEventDispatcher(this, map);
}

void drawMap() {
  map.draw();
}

Location getLocation(float x, float y) {
  Location loc=map.getLocation(x, y);
  return loc;
}
