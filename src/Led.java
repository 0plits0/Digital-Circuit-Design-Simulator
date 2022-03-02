import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Led extends Component implements Runnable{
	public Led(int x, int y, Canvas c) throws IOException {
		super(x,y,c);
		img = ImageIO.read(MainWindow.class.getResourceAsStream("Img/ledoff.png"));
		value = 0;
		h = img.getHeight()/2;
		w = img.getWidth()/2;
		Ins.add(canvas.MakeDot(0,h/2,1,1));
		SuperAfter();
	}

	public void Check() {
		try{
			Dot src1 = Ins.get(0).FindSource();
			int ins1;
			if (src1!=null) {
				ins1 = src1.parent.GetOut(src1.parentn);
				if (src1.partof==0) ins1=0;
			}
			else ins1=0;
			//System.out.println("Value: ");
			//System.out.println(ins1);
			if (ins1!=value){
				try {
					//System.out.print("Changed Value: ");
					//System.out.println(ins1);
					value = ins1;
					if (ins1==1) img = ImageIO.read(MainWindow.class.getResourceAsStream("Img/ledon.png"));//System.out.println("on");}
					else img = ImageIO.read(MainWindow.class.getResourceAsStream("Img/ledoff.png"));//System.out.println("off");}
				} catch (IOException e) {
					e.printStackTrace();
				}
				canvas.repaint();
			}
		}
		catch (Exception E){	
		}
	}
	
	
	public void Inverse(){
		if (value==0) value=1;
		else value=0;
		return;
	}

	@Override
	public void run() {
		while(!deleted){
			Check();
			try {
				Thread.currentThread().sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void AfterOpen() {
		try {
			img = ImageIO.read(MainWindow.class.getResourceAsStream("Img/ledoff.png"));
			value=-1;
			//System.out.print("Opened led with value: ");
			//System.out.println(value);
			(new Thread(this)).start();
			//System.out.println("Started");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void OnDelete() {
		//Thread.currentThread().destroy();
	}
	
}
