package edu.berkeley.cs.nlp.ocular.model;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

import edu.berkeley.cs.nlp.ocular.gsm.GlyphChar;
import edu.berkeley.cs.nlp.ocular.gsm.GlyphChar.GlyphType;
import edu.berkeley.cs.nlp.ocular.model.transition.SparseTransitionModel.TransitionState;
import edu.berkeley.cs.nlp.ocular.train.FontTrainer;
import edu.berkeley.cs.nlp.ocular.util.Tuple2;
import static edu.berkeley.cs.nlp.ocular.util.CollectionHelper.*;
import indexer.HashMapIndexer;
import indexer.Indexer;
import static edu.berkeley.cs.nlp.ocular.model.TransitionStateType.*;

/**
 * @author Dan Garrette (dhgarrette@gmail.com)
 */
public class FontTrainEMTests {

	class TS implements TransitionState {
		public final int id;
		private int languageIndex;
		private int lmCharIndex;
		private TransitionStateType type;
		private GlyphChar glyphChar;
		
		public TS(int id, int languageIndex, int lmCharIndex, TransitionStateType type, GlyphChar glyphChar) {
			this.id = id;
			this.languageIndex = languageIndex;
			this.lmCharIndex = lmCharIndex;
			this.type = type;
			this.glyphChar = glyphChar;
		}
		public int getLanguageIndex() { return languageIndex; }
		public int getLmCharIndex() { return lmCharIndex; }
		public TransitionStateType getType() { return type; }
		public GlyphChar getGlyphChar() { return glyphChar; }
		
		public int getOffset() { return -1; }
		public int getExposure() { return -1; }
		public Collection<Tuple2<TransitionState, Double>> forwardTransitions() { return null; }
		public Collection<Tuple2<TransitionState, Double>> nextLineStartStates() { return null; }
		public double endLogProb() { return -1; }
	}
	
	@Test
	public void test_makeFullViterbiStateSeq() {

		GlyphChar gc = new GlyphChar(-1, GlyphType.NORMAL_CHAR);
		Indexer<String> charIndexer = new HashMapIndexer<String>();
		charIndexer.index(new String[] { " ", "-", "a", "b", "c" });
		TransitionState[][] decodeStates = new TransitionState[][] {
			new TransitionState[]{ 	new TS(1, -1, 0, LMRGN, gc), 
									new TS(2, -1, 0, LMRGN, gc),
									new TS(3, -1, 0, TMPL, gc), 
									new TS(4, 1, 2, TMPL, gc),
									new TS(5, 1, 3, TMPL, gc), 
									new TS(6, 1, 4, TMPL, gc), 
									new TS(7, 1, 1, RMRGN_HPHN_INIT, gc), 
									new TS(8, 1, 0, RMRGN_HPHN, gc), 
									new TS(9, 1, 0, RMRGN_HPHN, gc) },
			new TransitionState[]{ 	new TS(10, 1, 0, LMRGN_HPHN, gc), 
									new TS(11, 1, 0, LMRGN_HPHN, gc),
									new TS(12, 1, 0, TMPL, gc),
									new TS(13, 1, 2, TMPL, gc),
									new TS(14, 1, 3, TMPL, gc),
									new TS(15, 1, 4, TMPL, gc),
									new TS(16, 1, 0, RMRGN, gc),
									new TS(17, 1, 0, RMRGN, gc) }
		};
		List<TransitionState> tsSeq = FontTrainer.makeFullViterbiStateSeq(decodeStates, charIndexer);
		List<Integer> expectedIds = makeList(2, 3, 4, 1);
		for (int i = 0; i < expectedIds.size(); ++i) {
			assertEquals(expectedIds.get(i).intValue(), ((TS)tsSeq.get(i)).id);
		}


	}
}
