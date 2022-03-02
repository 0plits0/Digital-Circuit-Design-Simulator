import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Clock extends Component implements Runnable{
	int out;
	public Clock(int x, int y, Canvas c) throws IOException {
		super(x,y,c);
		TextNoImg = true;
		value = 500;
		h = 30;
		w = 40;
		Outs.add(canvas.MakeDot(40,15,0,1));
		Desc = new String("Clock:");
		SuperAfter();
	}

	public void Check() {
		Inverse();
	}
	
	public void ChangeInterval(){
		String s = JOptionPane.showInputDialog("Enter the interval of the clock:", 200);
		if (s!=null) value = Integer.parseUnsignedInt(s);
	}
	
	
	@Override
	public int GetOut(int n) {
		return out;
	}

	public void Inverse(){
		if (out==0) out=1;
		else out=0;
		return;
	}

	@Override
	public void run() {
		while(!deleted){
			Check();
			try {
				Thread.currentThread().sleep(value);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void AfterOpen() {
			(new Thread(this)).start();
	}
	
}
