import java.awt.Point;
import java.util.ArrayList;
import java.util.List;


public class Dot implements java.io.Serializable{
	int x1,x2,y1,y2;
	transient Canvas canvas;
	int type; //	0 = in, 1 = out
	int partof; // 0 = not part of an element, 1 = part of an element
	Component parent = null;
	int parentn = 0;
	Dot prev = null;
	List<Dot> nexts = new ArrayList<Dot>();

	public Dot(int sx1,int sx2,int sy1, int sy2,int t,int p, Canvas c) {
		x1 = sx1; x2 = sx2;
		y1 = sy1; y2 = sy2;
		type = t; partof = p;
		canvas = c;
	}
	
	public Dot(int sx,int sy, int t,int p,Canvas c){
		x1 = sx-5; x2 = sx +5;
		y1 = sy-5; y2 = sy +5;
		type = t; partof = p;
		canvas = c;
	}	
	
	public void SetParent(Component p,int n){
		parent = p;
		parentn = n;
	}
	
	public int GetX1(){
		if (partof==0) return x1;
		else return parent.x+x1;
	}
	
	public int GetX2(){
		if (partof==0) return x2;
		else return parent.x+x2;
	}
	
	public int GetY1(){
		if (partof==0) return y1;
		else return parent.y+y1;
	}
	
	public int GetY2(){
		if (partof==0) return y2;
		else return parent.y+y2;
	}
	
	public int GetXM(){
		int k = (GetX1()+GetX2())/2;
		int quantdist = canvas.quantdist;
		if (canvas.UseGrid) k = (int) (Math.round(k*1.0/quantdist)*quantdist);
		/*if(partof==1){
		x1 = k; x2 = k;
		x1=x1-parent.x; x2=x2-parent.x;}*/
		return k;
	}
	
	public int GetYM(){
		int k = (GetY1()+GetY2())/2;
		int quantdist = canvas.quantdist;
		if (canvas.UseGrid) k = (int) (Math.round(k*1.0/quantdist)*quantdist);
		/*if(partof==1){
		y1 = k; y2 = k;
		y1=y1-parent.y; y2=y2-parent.y;}*/
		return k;
	}
	
	public void Rotate(int w,int h,int r){
		int tmp,d;
		if (parent.getClass().getCanonicalName()!="Not") d=1;
		else if ((parent.rotstate==1)||(parent.rotstate==3)) d=-10;
		else d=0;
		//d = 0;//11;//Math.abs(x1-x2);
		tmp = x1; x1=w-d-y1; y1=tmp;
		tmp = x2; x2=w-d-y2; y2=tmp;
		
		//if (parent.getClass().getCanonicalName()=="Not"){
		//	if (parent.rotstate==1) {x1=x1+10; x2=x2+10;}
		//	if (parent.rotstate==3) {x1=x1+10; x2=x2+10;}
		//}
		
		/*tmp = ((x1+x2)/2)%10;
		System.out.print(tmp);
		System.out.print(" , ");
		tmp = ((y1+y2)/2)%10;
		System.out.println(tmp);*/
		
		
		/*tmp = ((x1+x2)/2)%10;
		if (tmp!=0){
			if (tmp>0) tmp = tmp-10;
			x1 = x1+tmp;
			x2 = x2+tmp;
		}
		tmp = ((y1+y2)/2)%10;
		if (tmp!=0){
			if (tmp>0) tmp = tmp-10;
			y1 = y1+tmp;
			y2 = y2+tmp;
		}*/
	}
	
	public Dot FindSource(){
		//int i = 10;
		if (prev==null) return null;
		else if (prev==this) return this;
		else return prev.FindSource();
		/*Dot cur = prev;
		while((i>0)&&(cur.prev!=cur)){
			cur=cur.prev;
			//i--;
			//System.out.println(i);
		}*/
		//return cur;
	}
	
	public void MeAsRoot(){
		this.type = 0;
		for(int i=0;i<nexts.size();i++)if (nexts.get(i).type==1){
			nexts.get(i).prev = this;
			//nexts.get(i).nexts.remove(this);
			nexts.get(i).MeAsRoot();
		}
		return;
	}

}
