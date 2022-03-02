import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class TFlipFlop extends Component implements Runnable{
		private int val,in;
		public TFlipFlop(int x, int y, Canvas c) throws IOException {
			super(x,y,c);
			IsBox = true;
			BoxC = new BoxCont(this,c,1,2,new ArrayList<>(Arrays.asList("T")),new ArrayList<>(Arrays.asList("Q","Q'")));
			h = BoxC.h;
			w = BoxC.w;
			Ins.add(canvas.MakeDot(0,BoxC.indotpos.get(0),1,1));
			Outs.add(canvas.MakeDot(w,BoxC.outdotpos.get(0),0,1));
			Outs.add(canvas.MakeDot(w,BoxC.outdotpos.get(1),0,1));
			SuperAfter();
			val=0;
		}

		@Override
		public void run() {
			while(!deleted){
				Check();
				try {
					Thread.currentThread().sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void Check(){
			Dot src1 = Ins.get(0).FindSource();
			int ins1;
			if (src1!=null) {
				ins1 = src1.parent.GetOut(src1.parentn);
				if (src1.partof==0) ins1=0;
			}
			else ins1=0;
			if (ins1!=in){
				if ((in==0)&&(ins1==1)) val = Compl(val);
				in=ins1;
			}
		}
		
		
		
		@Override
		public int GetOut(int n) {
			if (n==0) return val;
			else return Compl(val);
		}

		private int Compl(int a){
			if (a==1) return 0;
			else return 1;
		}
		
		@Override
		public void AfterOpen() {
			//value=-1;
			(new Thread(this)).start();
		}

		@Override
		public void OnDelete() {
			try {
				Thread.currentThread().join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
}
