public class Planet{

public double xxPos; //Its current x position
public double yyPos; //Its current y position
public double xxVel; //Its current velocity in the x direction
public double yyVel; //Its current velocity in the y direction
public double mass; //Its mass
public String imgFileName; //The name of the file that corresponds to the image that depicts the planet (for example, jupiter.gif)
private static final double G=6.67*Math.pow(10,-11); // gravitational constant


public Planet(double xP,double yP, double xV, double yV, double m, String img){
  this.xxPos=xP;
  this.yyPos=yP;
  this.xxVel=xV;
  this.yyVel=yV;
  this.mass=m;
  this.imgFileName=img;
}

public Planet(Planet p){
  this.xxPos=p.xxPos;
  this.yyPos=p.yyPos;
  this.xxVel=p.xxVel;
  this.yyVel=p.yyVel;
  this.mass=p.mass;
  this.imgFileName=p.imgFileName;
}

public double calcDistance(Planet p){
  return Math.sqrt((this.xxPos-p.xxPos)*(this.xxPos-p.xxPos) + (this.yyPos-p.yyPos)*(this.yyPos-p.yyPos));
}

public double calcForceExertedBy(Planet p){
  return (G*this.mass*p.mass)/(Math.pow(this.calcDistance(p),2));
}

public double calcForceExertedByX(Planet p){
return this.calcForceExertedBy(p)*(p.xxPos-this.xxPos)/this.calcDistance(p);

}

public double calcForceExertedByY(Planet p){
return this.calcForceExertedBy(p)*(p.yyPos-this.yyPos)/this.calcDistance(p);

}
// the net force on a particular planet is the sum of all pairwise force from that particular planet exerts to another planet in the solar system
public double calcNetForceExertedByX(Planet[] planets){
  double res=0.0;
  for(Planet p:planets){
    if(!this.equals(p)){
    res+=this.calcForceExertedByX(p);
  }
  }
  return res;
}

public double calcNetForceExertedByY(Planet[] planets){
  double res=0.0;
  for(Planet p:planets){
    if(!this.equals(p)){
    res+=this.calcForceExertedByY(p);
  }
  }
  return res;
}
//dt is the time, fX,fY are the net force corresponds to direction x,y
public void update(double dt, double fX, double fY){
  double A_net_x= fX/this.mass;
  double A_net_y=fY/this.mass;
  this.xxVel=this.xxVel+dt*A_net_x;
  this.yyVel=this.yyVel+dt*A_net_y;
  this.xxPos=this.xxPos+dt*this.xxVel;
  this.yyPos=this.yyPos+dt*this.yyVel;
}
public void draw(){
    String fullPath="images/"+this.imgFileName;
    StdDraw.picture(this.xxPos,this.yyPos,fullPath);
}

}
