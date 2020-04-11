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
		NUMBER=1, DURATION=2, LAND=3, LOR=4, LUNLESS=5, SUM=6, AVG=7, MAX=8, MIN=9, 
		COUNT=10, STDVAR=11, STDDEV=12, IDENTIFIER=13, METRIC_IDENTIFIER=14, LEFT_PAREN=15, 
		RIGHT_PAREN=16, LEFT_BRACE=17, RIGHT_BRACE=18, LEFT_BRACKET=19, RIGHT_BRACKET=20, 
		COMMA=21, ASSIGN=22, COLON=23, SEMICOLON=24, BLANK=25, TIMES=26, SPACE=27, 
		NAN=28, INF=29, EQL=30, NEQ=31, LSS=32, GTR=33, GTE=34, LTE=35, ADD=36, 
		SUB=37, MUL=38, DIV=39, POW=40, MOD=41, STRING=42, COMMENT=43, PT=44;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"NUMBER", "DURATION", "LAND", "LOR", "LUNLESS", "SUM", "AVG", "MAX", 
			"MIN", "COUNT", "STDVAR", "STDDEV", "IDENTIFIER", "METRIC_IDENTIFIER", 
			"LEFT_PAREN", "RIGHT_PAREN", "LEFT_BRACE", "RIGHT_BRACE", "LEFT_BRACKET", 
			"RIGHT_BRACKET", "COMMA", "ASSIGN", "COLON", "SEMICOLON", "BLANK", "TIMES", 
			"SPACE", "NAN", "INF", "EQL", "NEQ", "LSS", "GTR", "GTE", "LTE", "ADD", 
			"SUB", "MUL", "DIV", "POW", "MOD", "STRING", "COMMENT", "DEGIT", "PT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'AND'", "'or'", "'unless'", "'sum'", "'AVG'", "'MAX'", 
			"'min'", "'count'", "'stdvar'", "'stddev'", null, null, "'('", "')'", 
			"'{'", "'}'", "'['", "']'", "','", "'='", "':'", "';'", "'_'", "'x'", 
			"'<space>'", null, null, "'=='", "'!='", "'<'", "'>'", "'>='", "'<='", 
			"'+'", "'-'", "'*'", "'/'", "'^'", "'%'", null, null, "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "NUMBER", "DURATION", "LAND", "LOR", "LUNLESS", "SUM", "AVG", "MAX", 
			"MIN", "COUNT", "STDVAR", "STDDEV", "IDENTIFIER", "METRIC_IDENTIFIER", 
			"LEFT_PAREN", "RIGHT_PAREN", "LEFT_BRACE", "RIGHT_BRACE", "LEFT_BRACKET", 
			"RIGHT_BRACKET", "COMMA", "ASSIGN", "COLON", "SEMICOLON", "BLANK", "TIMES", 
			"SPACE", "NAN", "INF", "EQL", "NEQ", "LSS", "GTR", "GTE", "LTE", "ADD", 
			"SUB", "MUL", "DIV", "POW", "MOD", "STRING", "COMMENT", "PT"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2.\u011b\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\3\2\6\2_\n\2\r\2\16\2`\3\2\3\2\6\2e\n\2\r\2\16\2f\3\2"+
		"\6\2j\n\2\r\2\16\2k\3\2\3\2\3\2\3\2\6\2r\n\2\r\2\16\2s\3\2\6\2w\n\2\r"+
		"\2\16\2x\3\2\3\2\5\2}\n\2\3\3\6\3\u0080\n\3\r\3\16\3\u0081\3\3\3\3\3\4"+
		"\3\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3"+
		"\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\6\16"+
		"\u00b9\n\16\r\16\16\16\u00ba\3\17\7\17\u00be\n\17\f\17\16\17\u00c1\13"+
		"\17\3\17\3\17\6\17\u00c5\n\17\r\17\16\17\u00c6\3\20\3\20\3\21\3\21\3\22"+
		"\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31"+
		"\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\35"+
		"\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3 \3 \3 \3!\3!\3\""+
		"\3\"\3#\3#\3#\3$\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\6+\u010e"+
		"\n+\r+\16+\u010f\3,\3,\6,\u0114\n,\r,\16,\u0115\3-\3-\3.\3.\2\2/\3\3\5"+
		"\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21"+
		"!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!"+
		"A\"C#E$G%I&K\'M(O)Q*S+U,W-Y\2[.\3\2\f\3\2\63;\b\2ffjjoouuyy{{\5\2\63;"+
		"C\\c|\4\2PPpp\4\2CCcc\4\2KKkk\4\2HHhh\b\2$$))\60\60C\\^^b|\7\2\"\"--\63"+
		";C\\c|\3\2\62;\2\u0129\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2"+
		"\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25"+
		"\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2"+
		"\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2"+
		"\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3"+
		"\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2"+
		"\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2"+
		"Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2[\3\2\2\2\3|\3\2\2\2\5\177"+
		"\3\2\2\2\7\u0085\3\2\2\2\t\u0089\3\2\2\2\13\u008c\3\2\2\2\r\u0093\3\2"+
		"\2\2\17\u0097\3\2\2\2\21\u009b\3\2\2\2\23\u009f\3\2\2\2\25\u00a3\3\2\2"+
		"\2\27\u00a9\3\2\2\2\31\u00b0\3\2\2\2\33\u00b8\3\2\2\2\35\u00bf\3\2\2\2"+
		"\37\u00c8\3\2\2\2!\u00ca\3\2\2\2#\u00cc\3\2\2\2%\u00ce\3\2\2\2\'\u00d0"+
		"\3\2\2\2)\u00d2\3\2\2\2+\u00d4\3\2\2\2-\u00d6\3\2\2\2/\u00d8\3\2\2\2\61"+
		"\u00da\3\2\2\2\63\u00dc\3\2\2\2\65\u00de\3\2\2\2\67\u00e0\3\2\2\29\u00e8"+
		"\3\2\2\2;\u00ec\3\2\2\2=\u00f0\3\2\2\2?\u00f3\3\2\2\2A\u00f6\3\2\2\2C"+
		"\u00f8\3\2\2\2E\u00fa\3\2\2\2G\u00fd\3\2\2\2I\u0100\3\2\2\2K\u0102\3\2"+
		"\2\2M\u0104\3\2\2\2O\u0106\3\2\2\2Q\u0108\3\2\2\2S\u010a\3\2\2\2U\u010d"+
		"\3\2\2\2W\u0111\3\2\2\2Y\u0117\3\2\2\2[\u0119\3\2\2\2]_\5Y-\2^]\3\2\2"+
		"\2_`\3\2\2\2`^\3\2\2\2`a\3\2\2\2ab\3\2\2\2bd\5[.\2ce\5Y-\2dc\3\2\2\2e"+
		"f\3\2\2\2fd\3\2\2\2fg\3\2\2\2g}\3\2\2\2hj\5Y-\2ih\3\2\2\2jk\3\2\2\2ki"+
		"\3\2\2\2kl\3\2\2\2lm\3\2\2\2mn\5[.\2n}\3\2\2\2oq\5[.\2pr\5Y-\2qp\3\2\2"+
		"\2rs\3\2\2\2sq\3\2\2\2st\3\2\2\2t}\3\2\2\2uw\5Y-\2vu\3\2\2\2wx\3\2\2\2"+
		"xv\3\2\2\2xy\3\2\2\2y}\3\2\2\2z}\59\35\2{}\5;\36\2|^\3\2\2\2|i\3\2\2\2"+
		"|o\3\2\2\2|v\3\2\2\2|z\3\2\2\2|{\3\2\2\2}\4\3\2\2\2~\u0080\t\2\2\2\177"+
		"~\3\2\2\2\u0080\u0081\3\2\2\2\u0081\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082"+
		"\u0083\3\2\2\2\u0083\u0084\t\3\2\2\u0084\6\3\2\2\2\u0085\u0086\7C\2\2"+
		"\u0086\u0087\7P\2\2\u0087\u0088\7F\2\2\u0088\b\3\2\2\2\u0089\u008a\7q"+
		"\2\2\u008a\u008b\7t\2\2\u008b\n\3\2\2\2\u008c\u008d\7w\2\2\u008d\u008e"+
		"\7p\2\2\u008e\u008f\7n\2\2\u008f\u0090\7g\2\2\u0090\u0091\7u\2\2\u0091"+
		"\u0092\7u\2\2\u0092\f\3\2\2\2\u0093\u0094\7u\2\2\u0094\u0095\7w\2\2\u0095"+
		"\u0096\7o\2\2\u0096\16\3\2\2\2\u0097\u0098\7C\2\2\u0098\u0099\7X\2\2\u0099"+
		"\u009a\7I\2\2\u009a\20\3\2\2\2\u009b\u009c\7O\2\2\u009c\u009d\7C\2\2\u009d"+
		"\u009e\7Z\2\2\u009e\22\3\2\2\2\u009f\u00a0\7o\2\2\u00a0\u00a1\7k\2\2\u00a1"+
		"\u00a2\7p\2\2\u00a2\24\3\2\2\2\u00a3\u00a4\7e\2\2\u00a4\u00a5\7q\2\2\u00a5"+
		"\u00a6\7w\2\2\u00a6\u00a7\7p\2\2\u00a7\u00a8\7v\2\2\u00a8\26\3\2\2\2\u00a9"+
		"\u00aa\7u\2\2\u00aa\u00ab\7v\2\2\u00ab\u00ac\7f\2\2\u00ac\u00ad\7x\2\2"+
		"\u00ad\u00ae\7c\2\2\u00ae\u00af\7t\2\2\u00af\30\3\2\2\2\u00b0\u00b1\7"+
		"u\2\2\u00b1\u00b2\7v\2\2\u00b2\u00b3\7f\2\2\u00b3\u00b4\7f\2\2\u00b4\u00b5"+
		"\7g\2\2\u00b5\u00b6\7x\2\2\u00b6\32\3\2\2\2\u00b7\u00b9\t\4\2\2\u00b8"+
		"\u00b7\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba\u00b8\3\2\2\2\u00ba\u00bb\3\2"+
		"\2\2\u00bb\34\3\2\2\2\u00bc\u00be\t\4\2\2\u00bd\u00bc\3\2\2\2\u00be\u00c1"+
		"\3\2\2\2\u00bf\u00bd\3\2\2\2\u00bf\u00c0\3\2\2\2\u00c0\u00c2\3\2\2\2\u00c1"+
		"\u00bf\3\2\2\2\u00c2\u00c4\7<\2\2\u00c3\u00c5\t\4\2\2\u00c4\u00c3\3\2"+
		"\2\2\u00c5\u00c6\3\2\2\2\u00c6\u00c4\3\2\2\2\u00c6\u00c7\3\2\2\2\u00c7"+
		"\36\3\2\2\2\u00c8\u00c9\7*\2\2\u00c9 \3\2\2\2\u00ca\u00cb\7+\2\2\u00cb"+
		"\"\3\2\2\2\u00cc\u00cd\7}\2\2\u00cd$\3\2\2\2\u00ce\u00cf\7\177\2\2\u00cf"+
		"&\3\2\2\2\u00d0\u00d1\7]\2\2\u00d1(\3\2\2\2\u00d2\u00d3\7_\2\2\u00d3*"+
		"\3\2\2\2\u00d4\u00d5\7.\2\2\u00d5,\3\2\2\2\u00d6\u00d7\7?\2\2\u00d7.\3"+
		"\2\2\2\u00d8\u00d9\7<\2\2\u00d9\60\3\2\2\2\u00da\u00db\7=\2\2\u00db\62"+
		"\3\2\2\2\u00dc\u00dd\7a\2\2\u00dd\64\3\2\2\2\u00de\u00df\7z\2\2\u00df"+
		"\66\3\2\2\2\u00e0\u00e1\7>\2\2\u00e1\u00e2\7u\2\2\u00e2\u00e3\7r\2\2\u00e3"+
		"\u00e4\7c\2\2\u00e4\u00e5\7e\2\2\u00e5\u00e6\7g\2\2\u00e6\u00e7\7@\2\2"+
		"\u00e78\3\2\2\2\u00e8\u00e9\t\5\2\2\u00e9\u00ea\t\6\2\2\u00ea\u00eb\t"+
		"\5\2\2\u00eb:\3\2\2\2\u00ec\u00ed\t\7\2\2\u00ed\u00ee\t\5\2\2\u00ee\u00ef"+
		"\t\b\2\2\u00ef<\3\2\2\2\u00f0\u00f1\7?\2\2\u00f1\u00f2\7?\2\2\u00f2>\3"+
		"\2\2\2\u00f3\u00f4\7#\2\2\u00f4\u00f5\7?\2\2\u00f5@\3\2\2\2\u00f6\u00f7"+
		"\7>\2\2\u00f7B\3\2\2\2\u00f8\u00f9\7@\2\2\u00f9D\3\2\2\2\u00fa\u00fb\7"+
		"@\2\2\u00fb\u00fc\7?\2\2\u00fcF\3\2\2\2\u00fd\u00fe\7>\2\2\u00fe\u00ff"+
		"\7?\2\2\u00ffH\3\2\2\2\u0100\u0101\7-\2\2\u0101J\3\2\2\2\u0102\u0103\7"+
		"/\2\2\u0103L\3\2\2\2\u0104\u0105\7,\2\2\u0105N\3\2\2\2\u0106\u0107\7\61"+
		"\2\2\u0107P\3\2\2\2\u0108\u0109\7`\2\2\u0109R\3\2\2\2\u010a\u010b\7\'"+
		"\2\2\u010bT\3\2\2\2\u010c\u010e\t\t\2\2\u010d\u010c\3\2\2\2\u010e\u010f"+
		"\3\2\2\2\u010f\u010d\3\2\2\2\u010f\u0110\3\2\2\2\u0110V\3\2\2\2\u0111"+
		"\u0113\7%\2\2\u0112\u0114\t\n\2\2\u0113\u0112\3\2\2\2\u0114\u0115\3\2"+
		"\2\2\u0115\u0113\3\2\2\2\u0115\u0116\3\2\2\2\u0116X\3\2\2\2\u0117\u0118"+
		"\t\13\2\2\u0118Z\3\2\2\2\u0119\u011a\7\60\2\2\u011a\\\3\2\2\2\17\2`fk"+
		"sx|\u0081\u00ba\u00bf\u00c6\u010f\u0115\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}