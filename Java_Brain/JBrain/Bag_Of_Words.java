package JBrain;

import java.util.Hashtable;
import java.util.Set;

public class Bag_Of_Words {
	public Hashtable<String,Integer> Bag;
	public Bag_Of_Words() {
		Bag = new Hashtable<String,Integer>();
	}
	public void Render_Corpus(Corpus Source) {
		for(int i =0;i<Source.Words.size();i++) {
			String Wd =Source.Words.get(i);
			if(Bag.containsKey(Wd)) {
				Bag.put(Wd, Bag.get(Wd)+1);
			}else {
				Bag.put(Wd, 1);
			}
		}
	}
	public void Print_Bag_Of_Words() {
        Set<String> keys = Bag.keySet();
		for(String Key:keys) {
			System.out.println("{ " + Key + " : " + Bag.get(Key) + " }");
		}
	}
	
}
