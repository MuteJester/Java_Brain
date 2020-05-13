package JBrain;

import java.util.Set;

public class Text_Miner {
	private Corpus Text_Data;
	private Bag_Of_Words BOW;
	
	public Text_Miner(Corpus Text_Data) {
		this.Text_Data = Text_Data;
		BOW = new Bag_Of_Words();
		BOW.Render_Corpus(Text_Data);
	}
	public String Most_Common_Word() {
			Set<String> keys = BOW.Bag.keySet();
			int max_amount = 0;
			String MCW = "";
	        for(String key: keys){
	            if(BOW.Bag.get(key) > max_amount) {
	            	max_amount = BOW.Bag.get(key);
	            	MCW = key;
	            }
	        }
	        return MCW;
	}
	public String[] Most_Common_Word(int Amount_Of_MCW) {
		String[] MCW = new String[Amount_Of_MCW];
		Set<String> keys = BOW.Bag.keySet();
		int max_amount = 0 ,Lessthen = Integer.MAX_VALUE;
		String Temp="";
		for (int i =0;i<Amount_Of_MCW ;i++) {
	        for(String key: keys){
	            if(BOW.Bag.get(key) > max_amount &&BOW.Bag.get(key)<Lessthen ) {
	            	max_amount = BOW.Bag.get(key);
	            	Temp = key;
	            }
	        }
	        MCW[i] = Temp;
	        Lessthen = max_amount;
	        max_amount =0;
		}
		
		return MCW;
	}
	
}
