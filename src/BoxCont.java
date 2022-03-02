import java.awt.Point;
import java.util.ArrayList;

public class BoxCont implements java.io.Serializable{
	private Canvas canvas;
	private int quantdist;
	public int h,w;
	ArrayList<String> innames;
	ArrayList<String> outnames;
	ArrayList<Integer> indotpos;
	ArrayList<Integer> outdotpos;
	ArrayList<Point> innampos;
	ArrayList<Point> outnampos;
	Component Parent;
	
	public BoxCont(Component par,Canvas c,int ins, int outs,ArrayList<String> inn,ArrayList<String> outn){
		Parent = par;
		canvas = c;
		quantdist = canvas.quantdist;
		h = (2*quantdist)+Math.max(ins,outs)*quantdist*2;
		innames = inn; outnames = outn;
		w = 3*h/4;
		indotpos= ArrangeDots(ins);
		outdotpos= ArrangeDots(outs);
		innampos = new ArrayList<Point>();
		outnampos = new ArrayList<Point>();
		Point tmp;
		for(int j=0;j<innames.size();j++){
			tmp = new Point(quantdist,indotpos.get(j)+quantdist/2);
			innampos.add(tmp);
		}
		for(int j=0;j<outnames.size();j++){
			tmp = new Point(w-quantdist,outdotpos.get(j)+quantdist/2);
			outnampos.add(tmp);
		}
	}
	
	private ArrayList<Integer> ArrangeDots(int num){
		ArrayList<Integer> x= new ArrayList<Integer>();
		int curpos = quantdist;
		for(int i=0;i<num;i++){
			curpos = curpos + quantdist;
			x.add(curpos);
			curpos = curpos + quantdist;
		}
		return x;
	}
	
	public void Rotate(){
		Point cur;
		int x,y,d,tmp = h; h=w; w=tmp;
		if ((Parent.rotstate==1)||(Parent.rotstate==3)) d=-10;
		else d=10;
		for(int j=0;j<innampos.size();j++){
			cur = innampos.get(j);
			x = cur.x; y = cur.y;
			tmp = x; x=w-d-y; y=tmp;
			innampos.get(j).x=x; innampos.get(j).y=y;
		}
		for(int j=0;j<outnampos.size();j++){
			cur = outnampos.get(j);
			x = cur.x; y = cur.y;
			tmp = x; x=w-d-y; y=tmp;
			outnampos.get(j).x=x; outnampos.get(j).y=y;
		}
	}
}
