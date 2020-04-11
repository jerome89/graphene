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
		NUMBER=1, DURATION=2, IDENTIFIER=3, LEFT_PAREN=4, RIGHT_PAREN=5, LEFT_BRACE=6, 
		RIGHT_BRACE=7, LEFT_BRACKET=8, RIGHT_BRACKET=9, COMMA=10, ASSIGN=11, COLON=12, 
		SEMICOLON=13, BLANK=14, TIMES=15, SPACE=16, NAN=17, INF=18, ADD=19, SUB=20, 
		PT=21;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"NUMBER", "DURATION", "IDENTIFIER", "LEFT_PAREN", "RIGHT_PAREN", "LEFT_BRACE", 
			"RIGHT_BRACE", "LEFT_BRACKET", "RIGHT_BRACKET", "COMMA", "ASSIGN", "COLON", 
			"SEMICOLON", "BLANK", "TIMES", "SPACE", "NAN", "INF", "ADD", "SUB", "DEGIT", 
			"PT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, "'('", "')'", "'{'", "'}'", "'['", "']'", "','", 
			"'='", "':'", "';'", "'_'", "'x'", "'<space>'", null, null, "'+'", "'-'", 
			"'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "NUMBER", "DURATION", "IDENTIFIER", "LEFT_PAREN", "RIGHT_PAREN", 
			"LEFT_BRACE", "RIGHT_BRACE", "LEFT_BRACKET", "RIGHT_BRACKET", "COMMA", 
			"ASSIGN", "COLON", "SEMICOLON", "BLANK", "TIMES", "SPACE", "NAN", "INF", 
			"ADD", "SUB", "PT"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\27\u008c\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\3\2\6\2\61\n\2"+
		"\r\2\16\2\62\3\2\3\2\6\2\67\n\2\r\2\16\28\3\2\6\2<\n\2\r\2\16\2=\3\2\3"+
		"\2\3\2\3\2\6\2D\n\2\r\2\16\2E\3\2\6\2I\n\2\r\2\16\2J\3\2\3\2\5\2O\n\2"+
		"\3\3\6\3R\n\3\r\3\16\3S\3\3\3\3\3\4\6\4Y\n\4\r\4\16\4Z\3\5\3\5\3\6\3\6"+
		"\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3"+
		"\17\3\17\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3"+
		"\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\2"+
		"\2\30\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35"+
		"\20\37\21!\22#\23%\24\'\25)\26+\2-\27\3\2\n\3\2\63;\3\2oo\5\2\63;C\\c"+
		"|\4\2PPpp\4\2CCcc\4\2KKkk\4\2HHhh\3\2\62;\2\u0096\2\3\3\2\2\2\2\5\3\2"+
		"\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21"+
		"\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2"+
		"\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3"+
		"\2\2\2\2)\3\2\2\2\2-\3\2\2\2\3N\3\2\2\2\5Q\3\2\2\2\7X\3\2\2\2\t\\\3\2"+
		"\2\2\13^\3\2\2\2\r`\3\2\2\2\17b\3\2\2\2\21d\3\2\2\2\23f\3\2\2\2\25h\3"+
		"\2\2\2\27j\3\2\2\2\31l\3\2\2\2\33n\3\2\2\2\35p\3\2\2\2\37r\3\2\2\2!t\3"+
		"\2\2\2#|\3\2\2\2%\u0080\3\2\2\2\'\u0084\3\2\2\2)\u0086\3\2\2\2+\u0088"+
		"\3\2\2\2-\u008a\3\2\2\2/\61\5+\26\2\60/\3\2\2\2\61\62\3\2\2\2\62\60\3"+
		"\2\2\2\62\63\3\2\2\2\63\64\3\2\2\2\64\66\5-\27\2\65\67\5+\26\2\66\65\3"+
		"\2\2\2\678\3\2\2\28\66\3\2\2\289\3\2\2\29O\3\2\2\2:<\5+\26\2;:\3\2\2\2"+
		"<=\3\2\2\2=;\3\2\2\2=>\3\2\2\2>?\3\2\2\2?@\5-\27\2@O\3\2\2\2AC\5-\27\2"+
		"BD\5+\26\2CB\3\2\2\2DE\3\2\2\2EC\3\2\2\2EF\3\2\2\2FO\3\2\2\2GI\5+\26\2"+
		"HG\3\2\2\2IJ\3\2\2\2JH\3\2\2\2JK\3\2\2\2KO\3\2\2\2LO\5#\22\2MO\5%\23\2"+
		"N\60\3\2\2\2N;\3\2\2\2NA\3\2\2\2NH\3\2\2\2NL\3\2\2\2NM\3\2\2\2O\4\3\2"+
		"\2\2PR\t\2\2\2QP\3\2\2\2RS\3\2\2\2SQ\3\2\2\2ST\3\2\2\2TU\3\2\2\2UV\t\3"+
		"\2\2V\6\3\2\2\2WY\t\4\2\2XW\3\2\2\2YZ\3\2\2\2ZX\3\2\2\2Z[\3\2\2\2[\b\3"+
		"\2\2\2\\]\7*\2\2]\n\3\2\2\2^_\7+\2\2_\f\3\2\2\2`a\7}\2\2a\16\3\2\2\2b"+
		"c\7\177\2\2c\20\3\2\2\2de\7]\2\2e\22\3\2\2\2fg\7_\2\2g\24\3\2\2\2hi\7"+
		".\2\2i\26\3\2\2\2jk\7?\2\2k\30\3\2\2\2lm\7<\2\2m\32\3\2\2\2no\7=\2\2o"+
		"\34\3\2\2\2pq\7a\2\2q\36\3\2\2\2rs\7z\2\2s \3\2\2\2tu\7>\2\2uv\7u\2\2"+
		"vw\7r\2\2wx\7c\2\2xy\7e\2\2yz\7g\2\2z{\7@\2\2{\"\3\2\2\2|}\t\5\2\2}~\t"+
		"\6\2\2~\177\t\5\2\2\177$\3\2\2\2\u0080\u0081\t\7\2\2\u0081\u0082\t\5\2"+
		"\2\u0082\u0083\t\b\2\2\u0083&\3\2\2\2\u0084\u0085\7-\2\2\u0085(\3\2\2"+
		"\2\u0086\u0087\7/\2\2\u0087*\3\2\2\2\u0088\u0089\t\t\2\2\u0089,\3\2\2"+
		"\2\u008a\u008b\7\60\2\2\u008b.\3\2\2\2\13\2\628=EJNSZ\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}