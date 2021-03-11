package VO;

import java.util.ArrayList;
import java.util.List;

public class Initial {

	private List<Character> koInitial;
	
	public Initial() {
		koInitial = new ArrayList<>();
		koInitial.add('ㄱ');
		koInitial.add('ㄴ');
		koInitial.add('ㄷ');
		koInitial.add('ㄹ');
		koInitial.add('ㅁ');
		koInitial.add('ㅂ');
		koInitial.add('ㅅ');
		koInitial.add('ㅇ');
		koInitial.add('ㅈ');
		koInitial.add('ㅊ');
		koInitial.add('ㅋ');
		koInitial.add('ㅌ');
		koInitial.add('ㅍ');
		koInitial.add('ㅎ');
	}

	public List<Character> getKoInitial() {
		return koInitial;
	}
}
