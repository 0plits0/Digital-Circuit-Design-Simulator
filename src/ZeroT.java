import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ZeroT extends Component {
	public ZeroT(int x, int y, Canvas c) throws IOException {
		super(x,y,c);
		img = ImageIO.read(new File("./Images/and.png"));
		h = img.getHeight()/2;
		w = img.getWidth()/2;
		Ins.add(canvas.MakeDot(1, 7, 9, 11,1,1));
		Ins.add(canvas.MakeDot(1, 7, 28, 31,1,1));
		Outs.add(canvas.MakeDot(70, 80, 14, 24,0,1));
		SuperAfter();
	}

	@Override
	public int GetOut(int n) {
		if (n==0){
			return 0;
		}
		return 0;
	}
	
	
	
	
}
