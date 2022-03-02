import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class And extends Component {
	public And(int x, int y, Canvas c) throws IOException {
		super(x,y,c);
		img = ImageIO.read(MainWindow.class.getResourceAsStream("Img/and.png"));
		h = img.getHeight()/2;
		w = img.getWidth()/2;
		Ins.add(canvas.MakeDot(0, 0, 5, 15,1,1));
		Ins.add(canvas.MakeDot(0, 0, 25, 35,1,1));
		Outs.add(canvas.MakeDot(75, 85, 16, 24,0,1));
		SuperAfter();
	}

	@Override
	public int GetOut(int n) {
		if (n==0){
			//System.out.println(this.toString());
			Dot src1 = Ins.get(0).FindSource();
			Dot src2 = Ins.get(1).FindSource();
			/*boolean x,y;
			x = InCircle(0, this); y=InCircle(1, this);
			if (x&&y){
				//Unwanted circle
			}
			else if (x){
				int ins2 = src2.parent.GetOut(src2.parentn);
				if (ins2==0) return 0;
			}
			else if (y){
				int ins1 = src1.parent.GetOut(src1.parentn);
				if (ins1==0) return 0;
			}*/
			//else{
				int ins1 = src1.parent.GetOut(src1.parentn);
				int ins2 = src2.parent.GetOut(src2.parentn);
				if ((ins1==1)&&(ins2==1)) return 1;
				else return 0;
			//}
		}
		return 0;
	}

	@Override
	public void AfterOpen() {
		try {
			img = ImageIO.read(MainWindow.class.getResourceAsStream("Img/and.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int x=rotstate;
		if (x%2==1){int tmp = h; h=w; w=tmp;}
		for(int i=0;i<x;i++) RotImgOnly();
		canvas.repaint();
	}
	
	
	
	
}
