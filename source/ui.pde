void initUi() {
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
