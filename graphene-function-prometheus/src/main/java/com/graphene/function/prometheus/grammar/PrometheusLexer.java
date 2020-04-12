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
		COUNT=10, STDVAR=11, STDDEV=12, OFFSET=13, BY=14, WITHOUT=15, ON=16, IGNORING=17, 
		GROUP_LEFT=18, GROUP_RIGHT=19, BOOL=20, IDENTIFIER=21, METRIC_IDENTIFIER=22, 
		LEFT_PAREN=23, RIGHT_PAREN=24, LEFT_BRACE=25, RIGHT_BRACE=26, LEFT_BRACKET=27, 
		RIGHT_BRACKET=28, COMMA=29, ASSIGN=30, COLON=31, SEMICOLON=32, BLANK=33, 
		TIMES=34, SPACE=35, NAN=36, INF=37, EQL=38, EQL_REGEX=39, NEQ_REGEX=40, 
		NEQ=41, LSS=42, GTR=43, GTE=44, LTE=45, ADD=46, SUB=47, MUL=48, DIV=49, 
		POW=50, MOD=51, STRING=52, COMMENT=53, PT=54;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"NUMBER", "DURATION", "LAND", "LOR", "LUNLESS", "SUM", "AVG", "MAX", 
			"MIN", "COUNT", "STDVAR", "STDDEV", "OFFSET", "BY", "WITHOUT", "ON", 
			"IGNORING", "GROUP_LEFT", "GROUP_RIGHT", "BOOL", "IDENTIFIER", "METRIC_IDENTIFIER", 
			"LEFT_PAREN", "RIGHT_PAREN", "LEFT_BRACE", "RIGHT_BRACE", "LEFT_BRACKET", 
			"RIGHT_BRACKET", "COMMA", "ASSIGN", "COLON", "SEMICOLON", "BLANK", "TIMES", 
			"SPACE", "NAN", "INF", "EQL", "EQL_REGEX", "NEQ_REGEX", "NEQ", "LSS", 
			"GTR", "GTE", "LTE", "ADD", "SUB", "MUL", "DIV", "POW", "MOD", "STRING", 
			"COMMENT", "DEGIT", "PT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'AND'", "'or'", "'unless'", "'sum'", "'AVG'", "'MAX'", 
			"'min'", "'count'", "'stdvar'", "'stddev'", "'offset'", "'by'", "'without'", 
			"'on'", "'ignoring'", "'group_left'", "'group_right'", "'bool'", null, 
			null, "'('", "')'", "'{'", "'}'", "'['", "']'", "','", "'='", "':'", 
			"';'", "'_'", "'x'", "'<space>'", null, null, null, "'=~'", "'!~'", "'!='", 
			"'<'", "'>'", "'>='", "'<='", "'+'", "'-'", "'*'", "'/'", "'^'", "'%'", 
			null, null, "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "NUMBER", "DURATION", "LAND", "LOR", "LUNLESS", "SUM", "AVG", "MAX", 
			"MIN", "COUNT", "STDVAR", "STDDEV", "OFFSET", "BY", "WITHOUT", "ON", 
			"IGNORING", "GROUP_LEFT", "GROUP_RIGHT", "BOOL", "IDENTIFIER", "METRIC_IDENTIFIER", 
			"LEFT_PAREN", "RIGHT_PAREN", "LEFT_BRACE", "RIGHT_BRACE", "LEFT_BRACKET", 
			"RIGHT_BRACKET", "COMMA", "ASSIGN", "COLON", "SEMICOLON", "BLANK", "TIMES", 
			"SPACE", "NAN", "INF", "EQL", "EQL_REGEX", "NEQ_REGEX", "NEQ", "LSS", 
			"GTR", "GTE", "LTE", "ADD", "SUB", "MUL", "DIV", "POW", "MOD", "STRING", 
			"COMMENT", "PT"
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


	  boolean isParenOpen = false;
	  int isParenOpenCount = 0;

	  boolean isBraceOpen = false;
	  int isBraceOpenCount = 0;

	  boolean isBracketOpen = false;
	  int isBracketOpenCount = 0;


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

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 22:
			LEFT_PAREN_action((RuleContext)_localctx, actionIndex);
			break;
		case 23:
			RIGHT_PAREN_action((RuleContext)_localctx, actionIndex);
			break;
		case 24:
			LEFT_BRACE_action((RuleContext)_localctx, actionIndex);
			break;
		case 25:
			RIGHT_BRACE_action((RuleContext)_localctx, actionIndex);
			break;
		case 26:
			LEFT_BRACKET_action((RuleContext)_localctx, actionIndex);
			break;
		case 27:
			RIGHT_BRACKET_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void LEFT_PAREN_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:

			  if (isParenOpen) {
			    throw new IllegalParenException("This is not allowed action. Please check the duplicated left paren.");
			  }

			  isParenOpen = true;
			  isParenOpenCount++;

			break;
		}
	}
	private void RIGHT_PAREN_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:

			  if (!isParenOpen) {
			    throw new IllegalParenException("This is not allowed action. Please check the left paren omitted.");
			  }

			  isParenOpen = false;

			break;
		}
	}
	private void LEFT_BRACE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2:

			  if (isBraceOpen) {
			    throw new IllegalBraceException("This is not allowed action. Please check the duplicated left brace.");
			  }

			  isBraceOpen = true;
			  isBraceOpenCount++;

			break;
		}
	}
	private void RIGHT_BRACE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 3:

			  if (!isBraceOpen) {
			    throw new IllegalBraceException("This is not allowed action. Please check the left brace omitted.");
			  }

			  isBraceOpen = false;

			break;
		}
	}
	private void LEFT_BRACKET_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 4:

			  if (isBracketOpen) {
			    throw new IllegalBracketException("This is not allowed action. Please check the duplicated left bracket.");
			  }

			  isBracketOpen = true;
			  isBracketOpenCount++;

			break;
		}
	}
	private void RIGHT_BRACKET_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 5:

			  if (!isBracketOpen) {
			    throw new IllegalBracketException("This is not allowed action. Please check the left bracket omitted.");
			  }

			  isBracketOpen = false;

			break;
		}
	}
	@Override
	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 15:
			return ON_sempred((RuleContext)_localctx, predIndex);
		case 29:
			return ASSIGN_sempred((RuleContext)_localctx, predIndex);
		case 35:
			return NAN_sempred((RuleContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean ON_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return  !isBraceOpen ;
		}
		return true;
	}
	private boolean ASSIGN_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return  !isBraceOpen ;
		}
		return true;
	}
	private boolean NAN_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return  !isBraceOpen ;
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\28\u0183\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\3\2\6\2s\n\2\r\2\16\2t\3\2\3\2"+
		"\6\2y\n\2\r\2\16\2z\3\2\3\2\3\2\3\2\6\2\u0081\n\2\r\2\16\2\u0082\3\2\6"+
		"\2\u0086\n\2\r\2\16\2\u0087\3\2\3\2\3\2\3\2\6\2\u008e\n\2\r\2\16\2\u008f"+
		"\3\2\6\2\u0093\n\2\r\2\16\2\u0094\3\2\3\2\5\2\u0099\n\2\3\3\6\3\u009c"+
		"\n\3\r\3\16\3\u009d\3\3\3\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n"+
		"\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r"+
		"\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17"+
		"\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\26\6\26\u0111\n\26\r\26"+
		"\16\26\u0112\3\27\7\27\u0116\n\27\f\27\16\27\u0119\13\27\3\27\3\27\6\27"+
		"\u011d\n\27\r\27\16\27\u011e\3\30\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3"+
		"\32\3\33\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\37\3\37\3"+
		"\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3$\3$\3$\3$\3$\3$\3%\3%\3%\3%\3%\3"+
		"&\3&\3&\3&\3\'\3\'\3\'\5\'\u0154\n\'\3(\3(\3(\3)\3)\3)\3*\3*\3*\3+\3+"+
		"\3,\3,\3-\3-\3-\3.\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62\3\63\3\63"+
		"\3\64\3\64\3\65\6\65\u0176\n\65\r\65\16\65\u0177\3\66\3\66\6\66\u017c"+
		"\n\66\r\66\16\66\u017d\3\67\3\67\38\38\2\29\3\3\5\4\7\5\t\6\13\7\r\b\17"+
		"\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+"+
		"\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+"+
		"U,W-Y.[/]\60_\61a\62c\63e\64g\65i\66k\67m\2o8\3\2\f\3\2\63;\b\2ffjjoo"+
		"uuyy{{\5\2\63;C\\c|\4\2PPpp\4\2CCcc\4\2KKkk\4\2HHhh\b\2$$))\60\60C\\^"+
		"^b|\7\2\"\"--\63;C\\c|\3\2\62;\2\u0194\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2"+
		"\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2"+
		"\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3"+
		"\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3"+
		"\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65"+
		"\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3"+
		"\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2"+
		"\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2"+
		"[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3"+
		"\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2o\3\2\2\2\3\u0098\3\2\2\2\5\u009b\3\2\2"+
		"\2\7\u00a1\3\2\2\2\t\u00a5\3\2\2\2\13\u00a8\3\2\2\2\r\u00af\3\2\2\2\17"+
		"\u00b3\3\2\2\2\21\u00b7\3\2\2\2\23\u00bb\3\2\2\2\25\u00bf\3\2\2\2\27\u00c5"+
		"\3\2\2\2\31\u00cc\3\2\2\2\33\u00d3\3\2\2\2\35\u00da\3\2\2\2\37\u00dd\3"+
		"\2\2\2!\u00e5\3\2\2\2#\u00ea\3\2\2\2%\u00f3\3\2\2\2\'\u00fe\3\2\2\2)\u010a"+
		"\3\2\2\2+\u0110\3\2\2\2-\u0117\3\2\2\2/\u0120\3\2\2\2\61\u0123\3\2\2\2"+
		"\63\u0126\3\2\2\2\65\u0129\3\2\2\2\67\u012c\3\2\2\29\u012f\3\2\2\2;\u0132"+
		"\3\2\2\2=\u0134\3\2\2\2?\u0137\3\2\2\2A\u0139\3\2\2\2C\u013b\3\2\2\2E"+
		"\u013d\3\2\2\2G\u013f\3\2\2\2I\u0147\3\2\2\2K\u014c\3\2\2\2M\u0153\3\2"+
		"\2\2O\u0155\3\2\2\2Q\u0158\3\2\2\2S\u015b\3\2\2\2U\u015e\3\2\2\2W\u0160"+
		"\3\2\2\2Y\u0162\3\2\2\2[\u0165\3\2\2\2]\u0168\3\2\2\2_\u016a\3\2\2\2a"+
		"\u016c\3\2\2\2c\u016e\3\2\2\2e\u0170\3\2\2\2g\u0172\3\2\2\2i\u0175\3\2"+
		"\2\2k\u0179\3\2\2\2m\u017f\3\2\2\2o\u0181\3\2\2\2qs\5m\67\2rq\3\2\2\2"+
		"st\3\2\2\2tr\3\2\2\2tu\3\2\2\2uv\3\2\2\2vx\5o8\2wy\5m\67\2xw\3\2\2\2y"+
		"z\3\2\2\2zx\3\2\2\2z{\3\2\2\2{\u0099\3\2\2\2|}\7\62\2\2}~\7z\2\2~\u0080"+
		"\3\2\2\2\177\u0081\5m\67\2\u0080\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082"+
		"\u0080\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u0099\3\2\2\2\u0084\u0086\5m"+
		"\67\2\u0085\u0084\3\2\2\2\u0086\u0087\3\2\2\2\u0087\u0085\3\2\2\2\u0087"+
		"\u0088\3\2\2\2\u0088\u0089\3\2\2\2\u0089\u008a\5o8\2\u008a\u0099\3\2\2"+
		"\2\u008b\u008d\5o8\2\u008c\u008e\5m\67\2\u008d\u008c\3\2\2\2\u008e\u008f"+
		"\3\2\2\2\u008f\u008d\3\2\2\2\u008f\u0090\3\2\2\2\u0090\u0099\3\2\2\2\u0091"+
		"\u0093\5m\67\2\u0092\u0091\3\2\2\2\u0093\u0094\3\2\2\2\u0094\u0092\3\2"+
		"\2\2\u0094\u0095\3\2\2\2\u0095\u0099\3\2\2\2\u0096\u0099\5I%\2\u0097\u0099"+
		"\5K&\2\u0098r\3\2\2\2\u0098|\3\2\2\2\u0098\u0085\3\2\2\2\u0098\u008b\3"+
		"\2\2\2\u0098\u0092\3\2\2\2\u0098\u0096\3\2\2\2\u0098\u0097\3\2\2\2\u0099"+
		"\4\3\2\2\2\u009a\u009c\t\2\2\2\u009b\u009a\3\2\2\2\u009c\u009d\3\2\2\2"+
		"\u009d\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u009f\3\2\2\2\u009f\u00a0"+
		"\t\3\2\2\u00a0\6\3\2\2\2\u00a1\u00a2\7C\2\2\u00a2\u00a3\7P\2\2\u00a3\u00a4"+
		"\7F\2\2\u00a4\b\3\2\2\2\u00a5\u00a6\7q\2\2\u00a6\u00a7\7t\2\2\u00a7\n"+
		"\3\2\2\2\u00a8\u00a9\7w\2\2\u00a9\u00aa\7p\2\2\u00aa\u00ab\7n\2\2\u00ab"+
		"\u00ac\7g\2\2\u00ac\u00ad\7u\2\2\u00ad\u00ae\7u\2\2\u00ae\f\3\2\2\2\u00af"+
		"\u00b0\7u\2\2\u00b0\u00b1\7w\2\2\u00b1\u00b2\7o\2\2\u00b2\16\3\2\2\2\u00b3"+
		"\u00b4\7C\2\2\u00b4\u00b5\7X\2\2\u00b5\u00b6\7I\2\2\u00b6\20\3\2\2\2\u00b7"+
		"\u00b8\7O\2\2\u00b8\u00b9\7C\2\2\u00b9\u00ba\7Z\2\2\u00ba\22\3\2\2\2\u00bb"+
		"\u00bc\7o\2\2\u00bc\u00bd\7k\2\2\u00bd\u00be\7p\2\2\u00be\24\3\2\2\2\u00bf"+
		"\u00c0\7e\2\2\u00c0\u00c1\7q\2\2\u00c1\u00c2\7w\2\2\u00c2\u00c3\7p\2\2"+
		"\u00c3\u00c4\7v\2\2\u00c4\26\3\2\2\2\u00c5\u00c6\7u\2\2\u00c6\u00c7\7"+
		"v\2\2\u00c7\u00c8\7f\2\2\u00c8\u00c9\7x\2\2\u00c9\u00ca\7c\2\2\u00ca\u00cb"+
		"\7t\2\2\u00cb\30\3\2\2\2\u00cc\u00cd\7u\2\2\u00cd\u00ce\7v\2\2\u00ce\u00cf"+
		"\7f\2\2\u00cf\u00d0\7f\2\2\u00d0\u00d1\7g\2\2\u00d1\u00d2\7x\2\2\u00d2"+
		"\32\3\2\2\2\u00d3\u00d4\7q\2\2\u00d4\u00d5\7h\2\2\u00d5\u00d6\7h\2\2\u00d6"+
		"\u00d7\7u\2\2\u00d7\u00d8\7g\2\2\u00d8\u00d9\7v\2\2\u00d9\34\3\2\2\2\u00da"+
		"\u00db\7d\2\2\u00db\u00dc\7{\2\2\u00dc\36\3\2\2\2\u00dd\u00de\7y\2\2\u00de"+
		"\u00df\7k\2\2\u00df\u00e0\7v\2\2\u00e0\u00e1\7j\2\2\u00e1\u00e2\7q\2\2"+
		"\u00e2\u00e3\7w\2\2\u00e3\u00e4\7v\2\2\u00e4 \3\2\2\2\u00e5\u00e6\7q\2"+
		"\2\u00e6\u00e7\7p\2\2\u00e7\u00e8\3\2\2\2\u00e8\u00e9\6\21\2\2\u00e9\""+
		"\3\2\2\2\u00ea\u00eb\7k\2\2\u00eb\u00ec\7i\2\2\u00ec\u00ed\7p\2\2\u00ed"+
		"\u00ee\7q\2\2\u00ee\u00ef\7t\2\2\u00ef\u00f0\7k\2\2\u00f0\u00f1\7p\2\2"+
		"\u00f1\u00f2\7i\2\2\u00f2$\3\2\2\2\u00f3\u00f4\7i\2\2\u00f4\u00f5\7t\2"+
		"\2\u00f5\u00f6\7q\2\2\u00f6\u00f7\7w\2\2\u00f7\u00f8\7r\2\2\u00f8\u00f9"+
		"\7a\2\2\u00f9\u00fa\7n\2\2\u00fa\u00fb\7g\2\2\u00fb\u00fc\7h\2\2\u00fc"+
		"\u00fd\7v\2\2\u00fd&\3\2\2\2\u00fe\u00ff\7i\2\2\u00ff\u0100\7t\2\2\u0100"+
		"\u0101\7q\2\2\u0101\u0102\7w\2\2\u0102\u0103\7r\2\2\u0103\u0104\7a\2\2"+
		"\u0104\u0105\7t\2\2\u0105\u0106\7k\2\2\u0106\u0107\7i\2\2\u0107\u0108"+
		"\7j\2\2\u0108\u0109\7v\2\2\u0109(\3\2\2\2\u010a\u010b\7d\2\2\u010b\u010c"+
		"\7q\2\2\u010c\u010d\7q\2\2\u010d\u010e\7n\2\2\u010e*\3\2\2\2\u010f\u0111"+
		"\t\4\2\2\u0110\u010f\3\2\2\2\u0111\u0112\3\2\2\2\u0112\u0110\3\2\2\2\u0112"+
		"\u0113\3\2\2\2\u0113,\3\2\2\2\u0114\u0116\t\4\2\2\u0115\u0114\3\2\2\2"+
		"\u0116\u0119\3\2\2\2\u0117\u0115\3\2\2\2\u0117\u0118\3\2\2\2\u0118\u011a"+
		"\3\2\2\2\u0119\u0117\3\2\2\2\u011a\u011c\7<\2\2\u011b\u011d\t\4\2\2\u011c"+
		"\u011b\3\2\2\2\u011d\u011e\3\2\2\2\u011e\u011c\3\2\2\2\u011e\u011f\3\2"+
		"\2\2\u011f.\3\2\2\2\u0120\u0121\7*\2\2\u0121\u0122\b\30\2\2\u0122\60\3"+
		"\2\2\2\u0123\u0124\7+\2\2\u0124\u0125\b\31\3\2\u0125\62\3\2\2\2\u0126"+
		"\u0127\7}\2\2\u0127\u0128\b\32\4\2\u0128\64\3\2\2\2\u0129\u012a\7\177"+
		"\2\2\u012a\u012b\b\33\5\2\u012b\66\3\2\2\2\u012c\u012d\7]\2\2\u012d\u012e"+
		"\b\34\6\2\u012e8\3\2\2\2\u012f\u0130\7_\2\2\u0130\u0131\b\35\7\2\u0131"+
		":\3\2\2\2\u0132\u0133\7.\2\2\u0133<\3\2\2\2\u0134\u0135\7?\2\2\u0135\u0136"+
		"\6\37\3\2\u0136>\3\2\2\2\u0137\u0138\7<\2\2\u0138@\3\2\2\2\u0139\u013a"+
		"\7=\2\2\u013aB\3\2\2\2\u013b\u013c\7a\2\2\u013cD\3\2\2\2\u013d\u013e\7"+
		"z\2\2\u013eF\3\2\2\2\u013f\u0140\7>\2\2\u0140\u0141\7u\2\2\u0141\u0142"+
		"\7r\2\2\u0142\u0143\7c\2\2\u0143\u0144\7e\2\2\u0144\u0145\7g\2\2\u0145"+
		"\u0146\7@\2\2\u0146H\3\2\2\2\u0147\u0148\t\5\2\2\u0148\u0149\t\6\2\2\u0149"+
		"\u014a\t\5\2\2\u014a\u014b\6%\4\2\u014bJ\3\2\2\2\u014c\u014d\t\7\2\2\u014d"+
		"\u014e\t\5\2\2\u014e\u014f\t\b\2\2\u014fL\3\2\2\2\u0150\u0151\7?\2\2\u0151"+
		"\u0154\7?\2\2\u0152\u0154\7?\2\2\u0153\u0150\3\2\2\2\u0153\u0152\3\2\2"+
		"\2\u0154N\3\2\2\2\u0155\u0156\7?\2\2\u0156\u0157\7\u0080\2\2\u0157P\3"+
		"\2\2\2\u0158\u0159\7#\2\2\u0159\u015a\7\u0080\2\2\u015aR\3\2\2\2\u015b"+
		"\u015c\7#\2\2\u015c\u015d\7?\2\2\u015dT\3\2\2\2\u015e\u015f\7>\2\2\u015f"+
		"V\3\2\2\2\u0160\u0161\7@\2\2\u0161X\3\2\2\2\u0162\u0163\7@\2\2\u0163\u0164"+
		"\7?\2\2\u0164Z\3\2\2\2\u0165\u0166\7>\2\2\u0166\u0167\7?\2\2\u0167\\\3"+
		"\2\2\2\u0168\u0169\7-\2\2\u0169^\3\2\2\2\u016a\u016b\7/\2\2\u016b`\3\2"+
		"\2\2\u016c\u016d\7,\2\2\u016db\3\2\2\2\u016e\u016f\7\61\2\2\u016fd\3\2"+
		"\2\2\u0170\u0171\7`\2\2\u0171f\3\2\2\2\u0172\u0173\7\'\2\2\u0173h\3\2"+
		"\2\2\u0174\u0176\t\t\2\2\u0175\u0174\3\2\2\2\u0176\u0177\3\2\2\2\u0177"+
		"\u0175\3\2\2\2\u0177\u0178\3\2\2\2\u0178j\3\2\2\2\u0179\u017b\7%\2\2\u017a"+
		"\u017c\t\n\2\2\u017b\u017a\3\2\2\2\u017c\u017d\3\2\2\2\u017d\u017b\3\2"+
		"\2\2\u017d\u017e\3\2\2\2\u017el\3\2\2\2\u017f\u0180\t\13\2\2\u0180n\3"+
		"\2\2\2\u0181\u0182\7\60\2\2\u0182p\3\2\2\2\21\2tz\u0082\u0087\u008f\u0094"+
		"\u0098\u009d\u0112\u0117\u011e\u0153\u0177\u017d\b\3\30\2\3\31\3\3\32"+
		"\4\3\33\5\3\34\6\3\35\7";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}