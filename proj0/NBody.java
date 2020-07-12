public class NBody{

public static double readRadius(String file_name){
  In read = new In(file_name);
  int n = read.readInt();
  double r=read.readDouble();

  return r;
}

public static Planet[] readPlanets(String file_name){
  In read = new In(file_name);
  int n = read.readInt();
  double r=read.readDouble();

  Planet[] res= new Planet[n];

  for (int i=0; i<n;i++){
    double xxPos=read.readDouble();
    double yyPos=read.readDouble();
    double xxVel=read.readDouble();
    double yyVel=read.readDouble();
    double mass=read.readDouble();
    String imgFileName=read.readString();
    res[i]=new Planet(xxPos,yyPos,xxVel,yyVel,mass,imgFileName);
  }
  return res;
}
public static void main(String[] args){
  double T =Double.parseDouble(args[0]); // parseDouble():Double class static method takes no arguments and return a double value
  double dt=Double.parseDouble(args[1]);
  String filename=args[2];
  Planet[] planets=NBody.readPlanets(filename);
  double r = NBody.readRadius(filename);
  StdDraw.enableDoubleBuffering();
  // StdDraw.picture(0,0,"images/starfield.jpg");//if the image is located at the subfolder of the main method class's folder,specify the subfolder name follow by a / and then your filename, instead of putting filename directly withough specify the subfolder name.
  int nums_planets=planets.length;
  for (int i=0;i<=T;i+=dt){
  double[] xForces=new double[nums_planets];
  double[] yForces=new double[nums_planets];
  int j=0;
  for(Planet p:planets){
    xForces[j]=p.calcNetForceExertedByX(planets);
    yForces[j]=p.calcNetForceExertedByY(planets);
    j++;
  }
  StdDraw.picture(0,0,"images/starfield.jpg");
  int k=0;
  for(Planet p:planets){
  p.update(dt,xForces[k],yForces[k]);
  p.draw();
  k++;
  }
  StdDraw.show();
  StdDraw.pause(10);

  }
  StdOut.printf("%d\n", planets.length);
StdOut.printf("%.2e\n", r);
for (int i = 0; i < planets.length; i++) {
    StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                  planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                  planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
}
  }


}
