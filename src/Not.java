import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Not extends Component {
	public Not(int x, int y, Canvas c) throws IOException {
		super(x,y,c);
		img = ImageIO.read(MainWindow.class.getResourceAsStream("Img/not.png"));
		h = img.getHeight()/2;
		w = img.getWidth()/2;
		Ins.add(canvas.MakeDot(0, 0, 10, 30,1,1));
		Outs.add(canvas.MakeDot(50, 70, 10, 30,0,1));
		SuperAfter();
	}

	@Override
	public int GetOut(int n) {
		if (n==0){
			//System.out.println(this.toString());
			Dot src1 = Ins.get(0).FindSource();
			int ins1 ;
			if (src1!=null) ins1 = src1.parent.GetOut(src1.parentn);
			else ins1 = 1;
			if (ins1==0) return 1;
			else return 0;
		}
		return 0;
	}
	
	@Override
	public void AfterOpen() {
		try {
			img = ImageIO.read(MainWindow.class.getResourceAsStream("Img/not.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int x=rotstate;
		if (x%2==1){int tmp = h; h=w; w=tmp;}
		for(int i=0;i<x;i++) RotImgOnly();
		canvas.repaint();
	}

	@Override
	public int getX() {
		int k=x,quantdist = canvas.quantdist;
		if (canvas.UseGrid) k = (int) (Math.round(k*1.0/quantdist)*quantdist);
		if (rotstate%2==1) k=k+5;
		return k;
	}

	@Override
	public int getY() {
		int k=y,quantdist = canvas.quantdist;
		if (canvas.UseGrid) k = (int) (Math.round(k*1.0/quantdist)*quantdist);
		if (rotstate%2==0) k=k+5;
		return k;
	}
	
	
	
}
