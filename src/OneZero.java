import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class OneZero extends Component {
	public OneZero(int x, int y, Canvas c) throws IOException {
		super(x,y,c);
		//img = ImageIO.read(new File("./Images/not.png"));
		TextNoImg = true;
		h = 30;
		w = 40;
		Outs.add(canvas.MakeDot(40,15,0,1));
		Desc = new String("Source:");
		SuperAfter();
	}

	@Override
	public int GetOut(int n) {
		if (n==0) return value;
		return 0;
	}
	
	public void Inverse(){
		if (value==0) value=1;
		else value=0;
		return;
	}
	
	
	
	
}
