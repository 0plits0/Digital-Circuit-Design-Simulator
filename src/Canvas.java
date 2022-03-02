import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;


public class Canvas extends JLabel implements MouseMotionListener, MouseListener {
	transient MainWindow MainWindow;
	int quantdist = 10;
	boolean UseGrid = true;
	boolean ShowGrid = true;
	List<Component> Comp = new ArrayList<Component>();
	List<Dot> Dots = new ArrayList<Dot>();
	Component DraggedComp;
	Boolean	Dragged = false;
	Dot DraggedDot1, DraggedDot2;
	List<List<Dot>> Lines = new ArrayList<List<Dot>>();
	List<Point> PermLine = new ArrayList<Point>();
	
	public Canvas(MainWindow Window) {
		super();
		this.setPreferredSize(new Dimension(500,500));
		//this.setForeground(Color.red);
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		MainWindow = Window;
		this.repaint();
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		//-------------------Paint Grid----------------------------------------
		if (UseGrid&&ShowGrid){
			g.setColor(new Color(220,220,220));
			g2.setStroke(new BasicStroke(1));
			int endx = this.getWidth(); int endy = this.getHeight();
			for (int i = 0; i<endx;i=i+quantdist) g.drawLine(i, 0, i, endy);
			for (int i = 0; i<endy;i=i+quantdist) g.drawLine(0, i, endx, i);
		}
		//----------------------------------------------------------------------
		Component cur;
		Dot curd;
		//---------------------Paint Elements----------------------------
		for (int i=0;i<Comp.size();i++){
			cur = Comp.get(i);
			if (cur.TextNoImg) {
				g.setColor(Color.red);
				g.drawString(cur.GetTextImg(), cur.x+cur.w/4,cur.y+cur.h/2+3);
				g.setColor(Color.black);
				g.draw3DRect(cur.x, cur.y, cur.w, cur.h,false);
				g.setColor(Color.blue);
				g.drawString(cur.Desc, cur.x,cur.y-2);
				g.setColor(Color.black);
			}
			else if (cur.IsBox){
				BoxCont a = cur.BoxC;
				g.setColor(Color.black);
				g.draw3DRect(cur.x, cur.y, a.w, a.h, false);
				for(int j=0;j<a.innames.size();j++){
					g.drawString(a.innames.get(j), cur.getX()+a.innampos.get(j).x, cur.getY()+a.innampos.get(j).y);
				}
				for(int j=0;j<a.outnames.size();j++){
					g.drawString(a.outnames.get(j), cur.getX()+a.outnampos.get(j).x, cur.getY()+a.outnampos.get(j).y);
				}
			}
			else {
				g.drawImage(cur.img, cur.getX(), cur.getY(),cur.w,cur.h,this);
				//g.drawRect(cur.x, cur.y, cur.w, cur.h);
			}
		}
		//---------------------Paint Dots---------------------------------
		for (int i=0;i<Dots.size();i++){
				curd = Dots.get(i);
				if (curd.type==1) g.setColor(Color.red);
				else g.setColor(Color.blue);
				g.fillOval(curd.GetXM()-5, curd.GetYM()-5, 10, 10);
		}
		//---------------------Paint Lines---------------------------------
		g.setColor(Color.black);
		List<Dot> curl;
		g2.setStroke(new BasicStroke(2));
		for (int i=0;i<Lines.size();i++){
			curl = Lines.get(i);
			g.drawLine(curl.get(0).GetXM(),curl.get(0).GetYM(),curl.get(1).GetXM(),curl.get(1).GetYM());
		}
		if (PermLine.size()==2){
			g.setColor(Color.gray);
			g.drawLine(PermLine.get(0).x, PermLine.get(0).y, PermLine.get(1).x, PermLine.get(1).y);
		}
		return;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (Dragged&&(DraggedComp!=null)){
			DraggedComp.setX(arg0.getX()-(DraggedComp.w/2));
			DraggedComp.setY(arg0.getY()-(DraggedComp.h/2));
			this.repaint();
		}
		else if (DraggedDot1!=null){
			if (PermLine.size()==2) PermLine.remove(1);
			PermLine.add(new Point(arg0.getPoint()));
			this.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		//this.FindLine(arg0.getPoint().x, arg0.getPoint().y);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		int x,y;
		x = arg0.getPoint().x; y = arg0.getPoint().y;
		Component Selected = FindComp(x,y);
		//if (Selected!=null)System.out.println(Selected.toString());
		if ((Selected==null)&&(arg0.getButton()==1)){
			if (MainWindow.Element.getSelectedItem()=="AND")
				try {
					Comp.add(new And(x,y,this));
				} catch (IOException e) {
					e.printStackTrace();
				}
			else if (MainWindow.Element.getSelectedItem()=="OR")
				try {
					Comp.add(new Or(x,y,this));
				} catch (IOException e) {
					e.printStackTrace();
				}
			else if (MainWindow.Element.getSelectedItem()=="NOT")
				try {
					Comp.add(new Not(x,y,this));
				} catch (IOException e) {
					e.printStackTrace();
				}
			else if (MainWindow.Element.getSelectedItem()=="ONE/ZERO")
				try {
					Comp.add(new OneZero(x,y,this));
				} catch (IOException e) {
					e.printStackTrace();
				}
			else if (MainWindow.Element.getSelectedItem()=="LED")
			try {
				Led nl = new Led(x,y,this);
				Comp.add(nl);
				new Thread(nl).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
			else if (MainWindow.Element.getSelectedItem()=="CLOCK")
				try {
					Clock nc = new Clock(x,y,this);
					Comp.add(nc);
					new Thread(nc).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			else if (MainWindow.Element.getSelectedItem()=="TFLIPFLOP")
			try {
				TFlipFlop ntf = new TFlipFlop(x,y,this);
				Comp.add(ntf);
				new Thread(ntf).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.repaint();
		}
		//Delete Component
		else if ((Selected!=null)&&(arg0.getButton()==3)){
			Comp.remove(Selected);
			//Delete its dots
			for(int i=0;i<Selected.Ins.size();i++) {RemoveDot(Selected.Ins.get(i)); Dots.remove(Selected.Ins.get(i));}
			for(int i=0;i<Selected.Outs.size();i++) {RemoveDot(Selected.Outs.get(i)); Dots.remove(Selected.Outs.get(i));}
			Selected.Ins.clear();
			Selected.Outs.clear();
			Selected.deleted = true;
			//Delete any line connected to these dots
			/*List<Dot> curl;
			for (int i=0;i<Lines.size();i++){
				curl = Lines.get(i);
				if ((curl.get(0).partof==1)&&(curl.get(0).parent.deleted)) {Lines.remove(curl);i--;}
				else if ((curl.get(1).partof==1)&&(curl.get(1).parent.deleted)) {Lines.remove(curl);i--;}
			}*/
			this.repaint();
		}
		else if (Selected!=null){
			/*Dot curd=Selected.Ins.get(0);
			while(curd.prev!=null) curd = curd.prev;
			System.out.println(Selected.toString());
			System.out.println(curd.parent.toString());
			System.out.println("");*/
			//System.out.println(Selected.Ins.get(0).FindSource().toString());
			if (Selected.getClass().getName()=="OneZero") {
				((OneZero) Selected).Inverse();
				this.repaint();
			}
			else if (Selected.getClass().getName()=="Clock") {
				((Clock) Selected).ChangeInterval();
				this.repaint();
			}
			else Selected.Rotate();
			//if (arg0.getButton()==1)System.out.println(Selected.GetOut(0));
			//else System.out.println(Selected.toString());
			
		}
		List<Dot> DelLine = FindLine(arg0.getPoint().x,arg0.getPoint().y);
		if((arg0.getButton()==3)&&(DelLine!=null)) {
			Dot a;
			Lines.remove(DelLine);
			DelLine.get(0).nexts.remove(DelLine.get(1));
			DelLine.get(1).nexts.remove(DelLine.get(0));
			if (DelLine.get(0).type==0){
				a = (DelLine.get(0).prev==DelLine.get(1)) ? DelLine.get(0) : DelLine.get(1);
				//b = (a==DelLine.get(0)) ? DelLine.get(1) : DelLine.get(0);
				a.prev = a;
				for (int i=0;i<Dots.size();i++) if (Dots.get(i).FindSource()==a) Dots.get(i).type=1;
			}
			if((!DotIsInLine(DelLine.get(0)))&&(DelLine.get(0).partof==0)) {RemoveDot(DelLine.get(0)); Dots.remove(DelLine.get(0));}
			if((!DotIsInLine(DelLine.get(1)))&&(DelLine.get(1).partof==0)) {RemoveDot(DelLine.get(1)); Dots.remove(DelLine.get(1));}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		DraggedComp = FindComp(arg0.getPoint().x,arg0.getPoint().y);
		DraggedDot1 = FindDot(arg0.getPoint().x,arg0.getPoint().y);
		if ((!Dragged)&&(DraggedComp!=null)) Dragged = true;
		if (DraggedDot1!=null) {
			DraggedComp=null;
			PermLine.add(arg0.getPoint());
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		DraggedComp = null;
		Dragged = false;
		PermLine.clear();
		if (DraggedDot1!=null){
			DraggedDot2 = FindDot(arg0.getPoint().x,arg0.getPoint().y);
			//----------------Line Between Two Dots------------------------
			if(DraggedDot2 != null){
				//-------------------In And Out----------------------------
				if (DraggedDot1.type+DraggedDot2.type==1){
					DraggedDot1.nexts.add(DraggedDot2);
					DraggedDot2.nexts.add(DraggedDot1);
					if (DraggedDot1.type==1) {
						DraggedDot1.prev = DraggedDot2;
						DraggedDot1.MeAsRoot();
					}
					else {
						DraggedDot2.prev = DraggedDot1;
						DraggedDot2.MeAsRoot();
					}
					Lines.add(Arrays.asList(DraggedDot1,DraggedDot2));
				}
				//-------------------Out And Out---------------------------
				else if (DraggedDot1.type+DraggedDot2.type==2){
					Lines.add(Arrays.asList(DraggedDot1,DraggedDot2));
					DraggedDot1.nexts.add(DraggedDot2);
					DraggedDot2.nexts.add(DraggedDot1);
				}
			}
			//----------------Make New Dot----------------------------------
			else{
				Dot cur = new Dot(arg0.getPoint().x,arg0.getPoint().y,DraggedDot1.type,0,this);
				Dots.add(cur);
				if (DraggedDot1.type==1) {
					DraggedDot1.nexts.add(cur);
				}
				else cur.prev = DraggedDot1;
				cur.nexts.add(DraggedDot1);
				DraggedDot1.nexts.add(cur);
				Lines.add(Arrays.asList(DraggedDot1,cur));
			}
		}
		this.repaint();
	}
	
	Component FindComp(int x,int y){
		Component cur;
		for (int i=0; i<Comp.size();i++){
			cur = Comp.get(i);
			if ((x>cur.x) && (x<cur.x+cur.w) && (y>cur.y) && (y<cur.y+cur.h)) return cur;
		}
		return null;
	}
	
	Dot FindDot(int x,int y){
		Dot cur;
		for (int i=0; i<Dots.size();i++){
			cur =Dots.get(i);
			if ((x>=cur.GetXM()-5) && (x<=cur.GetXM()+5) && (y>=cur.GetYM()-5) && (y<=cur.GetYM()+5)) return cur;
		}
		return null;
	}
	
	List<Dot> FindLine(int x, int y){
		List<Dot> curl;
		double x1,x2,y1,y2;
		double l=0,b,d;
		for (int i=0;i<Lines.size();i++){
			curl = Lines.get(i);
			x1 = curl.get(0).GetXM(); x2= curl.get(1).GetXM();
			y1 = curl.get(0).GetYM(); y2= curl.get(1).GetYM();
			if (x1!=x2) {
				l = (y1-y2)/(x1-x2); 
				b = (x1*y2-x2*y1)/(x1-x2);
				d = Math.abs(y - l*x -b);
				d = Math.abs(l*x-y+b)/Math.sqrt(l*l+1);
			}
			else d = Math.abs(x-x1);
			if ((d<6)&&(x>=Math.min(x1, x2)-5)&&(x<=Math.max(x1, x2)+5)
					&&(y>=Math.min(y1, y2)-5)&&(y<=Math.max(y1, y2)+5))return curl;
		}
		return null;
	}
	
	Dot MakeDot(int x, int y, int type,int partof){
		Dot cur;
		cur = new Dot(x,y,type,partof,this);
		Dots.add(cur);
		return cur;
	}
	
	Dot MakeDot(int x1,int x2,int y1, int y2, int type, int partof){
		Dot cur;
		cur = new Dot(x1,x2,y1,y2,type,partof,this);
		Dots.add(cur);
		return cur;
	}
	
	void RemoveDot(Dot x){
		List<Dot> curl;
		Dot y;
		for (int i=0;i<Dots.size();i++) if (Dots.get(i).FindSource()==x) Dots.get(i).type=1;
		//Remove all lines connected to this dot
		for (int i=0;i<Lines.size();i++){
			curl = Lines.get(i);
			if ((curl.get(0)==x)||(curl.get(1)==x)) {
				y = (curl.get(0)==x) ? curl.get(1) : curl.get(0);
				Lines.remove(curl);
				//If the other dot of the line removed isn't in another line remove it
				if ((y.partof==0)&&(!DotIsInLine(y))) Dots.remove(y);
				i--;
			}
		}
		//Remove any references from other dots to this dot
		for (int i=0;i<Dots.size();i++){
			if (Dots.get(i).prev==x) Dots.get(i).prev = null;
			Dots.get(i).nexts.remove(x);
		}
		
	}
	
	boolean DotIsInLine(Dot x){
		List<Dot> curl;
		for (int i=0;i<Lines.size();i++){
			curl = Lines.get(i);
			if ((curl.get(0)==x)||(curl.get(1)==x)) return true;
		}
		return false;
	}
	
	public void AfterOpen(){
		Component cur;
		for (int i=0; i<Comp.size();i++){
			cur = Comp.get(i);
			cur.canvas = this;
			cur.AfterOpen();
		}
		Dot curd;
		for (int i=0; i<Dots.size();i++){
			curd = Dots.get(i);
			curd.canvas = this;
		}
	}
	
}
