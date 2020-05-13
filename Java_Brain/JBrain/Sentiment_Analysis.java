package JBrain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class Sentiment_Analysis {
	private Bag_Of_Words BOW;
	private static Bag_Of_Words BPositive,BNegative;
	private int Pos_Stat,Neg_Stat,Neu_Stat;
	public Sentiment_Analysis(Bag_Of_Words Bag_Of_Words) throws IOException{
		try {
			this.BOW = Bag_Of_Words;
			Pos_Stat = Neg_Stat=Neu_Stat =0;
			Corpus Positive = new Corpus();
			Corpus Negative = new Corpus();
			BPositive = new Bag_Of_Words();
			BNegative = new Bag_Of_Words();
			Positive.Render_Txt_File(new File(getClass().getResource("/JBrain/JBResources/PositiveSA.txt").getFile()));
			Negative.Render_Txt_File(new File(getClass().getResource("/JBrain/JBResources/NegativeSA.txt").getFile()));
			BPositive.Render_Corpus(Positive);
			BNegative.Render_Corpus(Negative);
			
		}
		catch(NullPointerException E) {
			System.out.println("Java_Brain Resources Were Not Found / Are Missing");
			System.exit(0);
		}
	}
	/**
	 * @return
	 * A Double Array of size 3 where element at index[0] contains the Positive  ,index[1]  contains the Negative and  index[2] 
	 *  contains the the Neutral
	 * */
	public double[] Get_Sentiment_Analysis() {
		double res[] = new double[3];
	    Set<String> keys = BOW.Bag.keySet();
		for(String sample : keys) {
			if(Sentiment_Analysis.BPositive.Bag.containsKey(sample)) {
				Pos_Stat++;
			}else if(Sentiment_Analysis.BNegative.Bag.containsKey(sample)) {
				Neg_Stat++;
			}else {
				Neu_Stat++;
			}
		}
		
		res[0] = (double)Pos_Stat /BOW.Bag.size();
		res[1] = (double)Neg_Stat /BOW.Bag.size();
		res[2] = (double)Neu_Stat /BOW.Bag.size();


		return res;
	}
	
}
