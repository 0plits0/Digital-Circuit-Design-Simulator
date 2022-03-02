import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;


public abstract class Component implements java.io.Serializable{
	int value = 0;
	String TextImg;
	boolean TextNoImg = false; //true: element has no image but text
	boolean IsBox = false;
	BoxCont BoxC = null;
	boolean visited = false; 	//for dfs
	String Desc;
	transient BufferedImage img;
	protected int x,y,h,w,rotstate=0;
	List<Dot> Ins = new ArrayList<Dot>();
	List<Dot> Outs = new ArrayList<Dot>();
	Canvas canvas;
	Boolean deleted = false;

	public Component(int sx,int sy,Canvas c) {
		x = sx; y = sy;
		canvas = c;
	}
	
	protected void SuperAfter(){
		for (int j=0;j<Ins.size();j++) Ins.get(j).SetParent(this,j);
		for (int j=0;j<Outs.size();j++) {Outs.get(j).SetParent(this,j); Outs.get(j).prev=Outs.get(j);}
		//When prev dot is self it means it's the Output of an element
	}
	
	public String GetTextImg(){
		return Integer.toString(value);
	}
	
	public void Rotate(){
		int tmp;
		rotstate = (rotstate+1)%4;
		if (img!=null){
			AffineTransform xform = new AffineTransform();
			xform.translate(0.5*h, 0.5*w);
			xform.rotate(Math.PI/2);
			xform.translate(-0.5*w, -1.5*h);
			BufferedImage nimg = new BufferedImage(img.getHeight(),img.getWidth(),BufferedImage.TYPE_INT_ARGB);
			
			Graphics2D g = (Graphics2D) nimg.createGraphics();
			g.drawImage(img, xform, canvas);
			g.dispose();
			img = nimg; 
		}
		if (BoxC!=null) BoxC.Rotate();
		tmp = h; h=w; w=tmp;
		for (int j=0;j<Ins.size();j++) Ins.get(j).Rotate(w,h,rotstate);
		for (int j=0;j<Outs.size();j++) Outs.get(j).Rotate(w,h,rotstate);
		canvas.repaint();
		
		//System.out.println(this.CircleOut(0, this)||this.CircleOut(1, this));
	}
	
	public void RotImgOnly(){
		int tmp;
		AffineTransform xform = new AffineTransform();
		xform.translate(0.5*h, 0.5*w);
		xform.rotate(Math.PI/2);
		xform.translate(-0.5*w, -1.5*h);
		BufferedImage nimg = new BufferedImage(img.getHeight(),img.getWidth(),BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = (Graphics2D) nimg.createGraphics();
		g.drawImage(img, xform, canvas);
		g.dispose();
		img = nimg; 
		tmp = h; h=w; w=tmp;
	}
	
	public void AfterOpen(){
	}
	
	public int GetOut(int n){
		return 0;
	}

	public int getX() {
		int k=x,quantdist = canvas.quantdist;
		if (canvas.UseGrid) k = (int) (Math.round(k*1.0/quantdist)*quantdist);
		x=k;
		return k;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		int k=y,quantdist = canvas.quantdist;
		if (canvas.UseGrid) k = (int) (Math.round(k*1.0/quantdist)*quantdist);
		y=k;
		return k;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}
	
	public boolean CircleOut(int in, Component src){
		Dot src1 = Ins.get(in).FindSource();
		if (src1.parent.visited) return false;
		src1.parent.visited=true;
		if (src1.parent==src) return true;
		else{
			boolean x = false;
			for(int i=0; i<src1.parent.Ins.size(); i++) {
				x=src1.parent.CircleOut(i, src);
				if (x) return x;
			}
			return false;
		}
	}
	
	public static synchronized boolean InCircle(int in, Component src){
		for(int i=0; i<src.canvas.Comp.size();i++) src.canvas.Comp.get(i).visited=false;
		return src.CircleOut(in, src); 
	}
	
	public void OnDelete(){
	}

}
