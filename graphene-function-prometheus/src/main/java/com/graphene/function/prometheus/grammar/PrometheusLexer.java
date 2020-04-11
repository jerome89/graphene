// Generated from Prometheus.g4 by ANTLR 4.7.2
package com.graphene.function.prometheus.grammar;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PrometheusLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		NUMBER=1, DURATION=2, IDENTIFIER=3, METRIC_IDENTIFIER=4, LEFT_PAREN=5, 
		RIGHT_PAREN=6, LEFT_BRACE=7, RIGHT_BRACE=8, LEFT_BRACKET=9, RIGHT_BRACKET=10, 
		COMMA=11, ASSIGN=12, COLON=13, SEMICOLON=14, BLANK=15, TIMES=16, SPACE=17, 
		NAN=18, INF=19, ADD=20, SUB=21, STRING=22, COMMENT=23, PT=24;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"NUMBER", "DURATION", "IDENTIFIER", "METRIC_IDENTIFIER", "LEFT_PAREN", 
			"RIGHT_PAREN", "LEFT_BRACE", "RIGHT_BRACE", "LEFT_BRACKET", "RIGHT_BRACKET", 
			"COMMA", "ASSIGN", "COLON", "SEMICOLON", "BLANK", "TIMES", "SPACE", "NAN", 
			"INF", "ADD", "SUB", "STRING", "COMMENT", "DEGIT", "PT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, "'('", "')'", "'{'", "'}'", "'['", "']'", 
			"','", "'='", "':'", "';'", "'_'", "'x'", "'<space>'", null, null, "'+'", 
			"'-'", null, null, "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "NUMBER", "DURATION", "IDENTIFIER", "METRIC_IDENTIFIER", "LEFT_PAREN", 
			"RIGHT_PAREN", "LEFT_BRACE", "RIGHT_BRACE", "LEFT_BRACKET", "RIGHT_BRACKET", 
			"COMMA", "ASSIGN", "COLON", "SEMICOLON", "BLANK", "TIMES", "SPACE", "NAN", 
			"INF", "ADD", "SUB", "STRING", "COMMENT", "PT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public PrometheusLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Prometheus.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\32\u00a9\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\3\2\6\2\67\n\2\r\2\16\28\3\2\3\2\6\2=\n\2\r\2\16\2>\3"+
		"\2\6\2B\n\2\r\2\16\2C\3\2\3\2\3\2\3\2\6\2J\n\2\r\2\16\2K\3\2\6\2O\n\2"+
		"\r\2\16\2P\3\2\3\2\5\2U\n\2\3\3\6\3X\n\3\r\3\16\3Y\3\3\3\3\3\4\6\4_\n"+
		"\4\r\4\16\4`\3\5\7\5d\n\5\f\5\16\5g\13\5\3\5\3\5\6\5k\n\5\r\5\16\5l\3"+
		"\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16"+
		"\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\25\3\25\3\26\3\26\3\27"+
		"\6\27\u009c\n\27\r\27\16\27\u009d\3\30\3\30\6\30\u00a2\n\30\r\30\16\30"+
		"\u00a3\3\31\3\31\3\32\3\32\2\2\33\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23"+
		"\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31"+
		"\61\2\63\32\3\2\f\3\2\63;\7\2jjoouuyy{{\5\2\63;C\\c|\4\2PPpp\4\2CCcc\4"+
		"\2KKkk\4\2HHhh\b\2$$))\60\60C\\^^b|\7\2\"\"--\63;C\\c|\3\2\62;\2\u00b7"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2"+
		"\2\2\63\3\2\2\2\3T\3\2\2\2\5W\3\2\2\2\7^\3\2\2\2\te\3\2\2\2\13n\3\2\2"+
		"\2\rp\3\2\2\2\17r\3\2\2\2\21t\3\2\2\2\23v\3\2\2\2\25x\3\2\2\2\27z\3\2"+
		"\2\2\31|\3\2\2\2\33~\3\2\2\2\35\u0080\3\2\2\2\37\u0082\3\2\2\2!\u0084"+
		"\3\2\2\2#\u0086\3\2\2\2%\u008e\3\2\2\2\'\u0092\3\2\2\2)\u0096\3\2\2\2"+
		"+\u0098\3\2\2\2-\u009b\3\2\2\2/\u009f\3\2\2\2\61\u00a5\3\2\2\2\63\u00a7"+
		"\3\2\2\2\65\67\5\61\31\2\66\65\3\2\2\2\678\3\2\2\28\66\3\2\2\289\3\2\2"+
		"\29:\3\2\2\2:<\5\63\32\2;=\5\61\31\2<;\3\2\2\2=>\3\2\2\2><\3\2\2\2>?\3"+
		"\2\2\2?U\3\2\2\2@B\5\61\31\2A@\3\2\2\2BC\3\2\2\2CA\3\2\2\2CD\3\2\2\2D"+
		"E\3\2\2\2EF\5\63\32\2FU\3\2\2\2GI\5\63\32\2HJ\5\61\31\2IH\3\2\2\2JK\3"+
		"\2\2\2KI\3\2\2\2KL\3\2\2\2LU\3\2\2\2MO\5\61\31\2NM\3\2\2\2OP\3\2\2\2P"+
		"N\3\2\2\2PQ\3\2\2\2QU\3\2\2\2RU\5%\23\2SU\5\'\24\2T\66\3\2\2\2TA\3\2\2"+
		"\2TG\3\2\2\2TN\3\2\2\2TR\3\2\2\2TS\3\2\2\2U\4\3\2\2\2VX\t\2\2\2WV\3\2"+
		"\2\2XY\3\2\2\2YW\3\2\2\2YZ\3\2\2\2Z[\3\2\2\2[\\\t\3\2\2\\\6\3\2\2\2]_"+
		"\t\4\2\2^]\3\2\2\2_`\3\2\2\2`^\3\2\2\2`a\3\2\2\2a\b\3\2\2\2bd\t\4\2\2"+
		"cb\3\2\2\2dg\3\2\2\2ec\3\2\2\2ef\3\2\2\2fh\3\2\2\2ge\3\2\2\2hj\7<\2\2"+
		"ik\t\4\2\2ji\3\2\2\2kl\3\2\2\2lj\3\2\2\2lm\3\2\2\2m\n\3\2\2\2no\7*\2\2"+
		"o\f\3\2\2\2pq\7+\2\2q\16\3\2\2\2rs\7}\2\2s\20\3\2\2\2tu\7\177\2\2u\22"+
		"\3\2\2\2vw\7]\2\2w\24\3\2\2\2xy\7_\2\2y\26\3\2\2\2z{\7.\2\2{\30\3\2\2"+
		"\2|}\7?\2\2}\32\3\2\2\2~\177\7<\2\2\177\34\3\2\2\2\u0080\u0081\7=\2\2"+
		"\u0081\36\3\2\2\2\u0082\u0083\7a\2\2\u0083 \3\2\2\2\u0084\u0085\7z\2\2"+
		"\u0085\"\3\2\2\2\u0086\u0087\7>\2\2\u0087\u0088\7u\2\2\u0088\u0089\7r"+
		"\2\2\u0089\u008a\7c\2\2\u008a\u008b\7e\2\2\u008b\u008c\7g\2\2\u008c\u008d"+
		"\7@\2\2\u008d$\3\2\2\2\u008e\u008f\t\5\2\2\u008f\u0090\t\6\2\2\u0090\u0091"+
		"\t\5\2\2\u0091&\3\2\2\2\u0092\u0093\t\7\2\2\u0093\u0094\t\5\2\2\u0094"+
		"\u0095\t\b\2\2\u0095(\3\2\2\2\u0096\u0097\7-\2\2\u0097*\3\2\2\2\u0098"+
		"\u0099\7/\2\2\u0099,\3\2\2\2\u009a\u009c\t\t\2\2\u009b\u009a\3\2\2\2\u009c"+
		"\u009d\3\2\2\2\u009d\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e.\3\2\2\2"+
		"\u009f\u00a1\7%\2\2\u00a0\u00a2\t\n\2\2\u00a1\u00a0\3\2\2\2\u00a2\u00a3"+
		"\3\2\2\2\u00a3\u00a1\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4\60\3\2\2\2\u00a5"+
		"\u00a6\t\13\2\2\u00a6\62\3\2\2\2\u00a7\u00a8\7\60\2\2\u00a8\64\3\2\2\2"+
		"\17\28>CKPTY`el\u009d\u00a3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}