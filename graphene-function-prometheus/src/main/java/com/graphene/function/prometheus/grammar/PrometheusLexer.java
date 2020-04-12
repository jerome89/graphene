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
		POW=50, MOD=51, STRING=52, COMMENT=53, TT=54, TTTT=55, PT=56;
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
			"COMMENT", "TT", "TTTT", "DEGIT", "PT"
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
			null, null, "'\r'", "'\n'", "'.'"
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
			"COMMENT", "TT", "TTTT", "PT"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2:\u018f\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\3\2\6\2w\n\2\r\2\16"+
		"\2x\3\2\3\2\6\2}\n\2\r\2\16\2~\3\2\3\2\3\2\3\2\6\2\u0085\n\2\r\2\16\2"+
		"\u0086\3\2\6\2\u008a\n\2\r\2\16\2\u008b\3\2\3\2\3\2\3\2\6\2\u0092\n\2"+
		"\r\2\16\2\u0093\3\2\6\2\u0097\n\2\r\2\16\2\u0098\3\2\3\2\5\2\u009d\n\2"+
		"\3\3\6\3\u00a0\n\3\r\3\16\3\u00a1\3\3\3\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3"+
		"\t\3\t\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3"+
		"\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3"+
		"\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3"+
		"\24\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\26\6\26\u0115"+
		"\n\26\r\26\16\26\u0116\3\27\7\27\u011a\n\27\f\27\16\27\u011d\13\27\3\27"+
		"\3\27\6\27\u0121\n\27\r\27\16\27\u0122\3\30\3\30\3\30\3\31\3\31\3\31\3"+
		"\32\3\32\3\32\3\33\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3"+
		"\37\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3$\3$\3$\3$\3$\3$\3%\3%"+
		"\3%\3%\3%\3&\3&\3&\3&\3\'\3\'\3\'\5\'\u0158\n\'\3(\3(\3(\3)\3)\3)\3*\3"+
		"*\3*\3+\3+\3,\3,\3-\3-\3-\3.\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62"+
		"\3\63\3\63\3\64\3\64\3\65\6\65\u017a\n\65\r\65\16\65\u017b\3\66\3\66\6"+
		"\66\u0180\n\66\r\66\16\66\u0181\3\67\3\67\3\67\3\67\38\38\38\38\39\39"+
		"\3:\3:\2\2;\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33"+
		"\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67"+
		"\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65"+
		"i\66k\67m8o9q\2s:\3\2\f\3\2\63;\b\2ffjjoouuyy{{\5\2\63;C\\c|\4\2PPpp\4"+
		"\2CCcc\4\2KKkk\4\2HHhh\b\2$$))\60\60C\\^^b|\7\2\"\"--\63;C\\c|\3\2\62"+
		";\2\u01a0\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2"+
		"\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27"+
		"\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2"+
		"\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2"+
		"\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2"+
		"\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2"+
		"\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S"+
		"\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2"+
		"\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2"+
		"\2m\3\2\2\2\2o\3\2\2\2\2s\3\2\2\2\3\u009c\3\2\2\2\5\u009f\3\2\2\2\7\u00a5"+
		"\3\2\2\2\t\u00a9\3\2\2\2\13\u00ac\3\2\2\2\r\u00b3\3\2\2\2\17\u00b7\3\2"+
		"\2\2\21\u00bb\3\2\2\2\23\u00bf\3\2\2\2\25\u00c3\3\2\2\2\27\u00c9\3\2\2"+
		"\2\31\u00d0\3\2\2\2\33\u00d7\3\2\2\2\35\u00de\3\2\2\2\37\u00e1\3\2\2\2"+
		"!\u00e9\3\2\2\2#\u00ee\3\2\2\2%\u00f7\3\2\2\2\'\u0102\3\2\2\2)\u010e\3"+
		"\2\2\2+\u0114\3\2\2\2-\u011b\3\2\2\2/\u0124\3\2\2\2\61\u0127\3\2\2\2\63"+
		"\u012a\3\2\2\2\65\u012d\3\2\2\2\67\u0130\3\2\2\29\u0133\3\2\2\2;\u0136"+
		"\3\2\2\2=\u0138\3\2\2\2?\u013b\3\2\2\2A\u013d\3\2\2\2C\u013f\3\2\2\2E"+
		"\u0141\3\2\2\2G\u0143\3\2\2\2I\u014b\3\2\2\2K\u0150\3\2\2\2M\u0157\3\2"+
		"\2\2O\u0159\3\2\2\2Q\u015c\3\2\2\2S\u015f\3\2\2\2U\u0162\3\2\2\2W\u0164"+
		"\3\2\2\2Y\u0166\3\2\2\2[\u0169\3\2\2\2]\u016c\3\2\2\2_\u016e\3\2\2\2a"+
		"\u0170\3\2\2\2c\u0172\3\2\2\2e\u0174\3\2\2\2g\u0176\3\2\2\2i\u0179\3\2"+
		"\2\2k\u017d\3\2\2\2m\u0183\3\2\2\2o\u0187\3\2\2\2q\u018b\3\2\2\2s\u018d"+
		"\3\2\2\2uw\5q9\2vu\3\2\2\2wx\3\2\2\2xv\3\2\2\2xy\3\2\2\2yz\3\2\2\2z|\5"+
		"s:\2{}\5q9\2|{\3\2\2\2}~\3\2\2\2~|\3\2\2\2~\177\3\2\2\2\177\u009d\3\2"+
		"\2\2\u0080\u0081\7\62\2\2\u0081\u0082\7z\2\2\u0082\u0084\3\2\2\2\u0083"+
		"\u0085\5q9\2\u0084\u0083\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0084\3\2\2"+
		"\2\u0086\u0087\3\2\2\2\u0087\u009d\3\2\2\2\u0088\u008a\5q9\2\u0089\u0088"+
		"\3\2\2\2\u008a\u008b\3\2\2\2\u008b\u0089\3\2\2\2\u008b\u008c\3\2\2\2\u008c"+
		"\u008d\3\2\2\2\u008d\u008e\5s:\2\u008e\u009d\3\2\2\2\u008f\u0091\5s:\2"+
		"\u0090\u0092\5q9\2\u0091\u0090\3\2\2\2\u0092\u0093\3\2\2\2\u0093\u0091"+
		"\3\2\2\2\u0093\u0094\3\2\2\2\u0094\u009d\3\2\2\2\u0095\u0097\5q9\2\u0096"+
		"\u0095\3\2\2\2\u0097\u0098\3\2\2\2\u0098\u0096\3\2\2\2\u0098\u0099\3\2"+
		"\2\2\u0099\u009d\3\2\2\2\u009a\u009d\5I%\2\u009b\u009d\5K&\2\u009cv\3"+
		"\2\2\2\u009c\u0080\3\2\2\2\u009c\u0089\3\2\2\2\u009c\u008f\3\2\2\2\u009c"+
		"\u0096\3\2\2\2\u009c\u009a\3\2\2\2\u009c\u009b\3\2\2\2\u009d\4\3\2\2\2"+
		"\u009e\u00a0\t\2\2\2\u009f\u009e\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1\u009f"+
		"\3\2\2\2\u00a1\u00a2\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\u00a4\t\3\2\2\u00a4"+
		"\6\3\2\2\2\u00a5\u00a6\7C\2\2\u00a6\u00a7\7P\2\2\u00a7\u00a8\7F\2\2\u00a8"+
		"\b\3\2\2\2\u00a9\u00aa\7q\2\2\u00aa\u00ab\7t\2\2\u00ab\n\3\2\2\2\u00ac"+
		"\u00ad\7w\2\2\u00ad\u00ae\7p\2\2\u00ae\u00af\7n\2\2\u00af\u00b0\7g\2\2"+
		"\u00b0\u00b1\7u\2\2\u00b1\u00b2\7u\2\2\u00b2\f\3\2\2\2\u00b3\u00b4\7u"+
		"\2\2\u00b4\u00b5\7w\2\2\u00b5\u00b6\7o\2\2\u00b6\16\3\2\2\2\u00b7\u00b8"+
		"\7C\2\2\u00b8\u00b9\7X\2\2\u00b9\u00ba\7I\2\2\u00ba\20\3\2\2\2\u00bb\u00bc"+
		"\7O\2\2\u00bc\u00bd\7C\2\2\u00bd\u00be\7Z\2\2\u00be\22\3\2\2\2\u00bf\u00c0"+
		"\7o\2\2\u00c0\u00c1\7k\2\2\u00c1\u00c2\7p\2\2\u00c2\24\3\2\2\2\u00c3\u00c4"+
		"\7e\2\2\u00c4\u00c5\7q\2\2\u00c5\u00c6\7w\2\2\u00c6\u00c7\7p\2\2\u00c7"+
		"\u00c8\7v\2\2\u00c8\26\3\2\2\2\u00c9\u00ca\7u\2\2\u00ca\u00cb\7v\2\2\u00cb"+
		"\u00cc\7f\2\2\u00cc\u00cd\7x\2\2\u00cd\u00ce\7c\2\2\u00ce\u00cf\7t\2\2"+
		"\u00cf\30\3\2\2\2\u00d0\u00d1\7u\2\2\u00d1\u00d2\7v\2\2\u00d2\u00d3\7"+
		"f\2\2\u00d3\u00d4\7f\2\2\u00d4\u00d5\7g\2\2\u00d5\u00d6\7x\2\2\u00d6\32"+
		"\3\2\2\2\u00d7\u00d8\7q\2\2\u00d8\u00d9\7h\2\2\u00d9\u00da\7h\2\2\u00da"+
		"\u00db\7u\2\2\u00db\u00dc\7g\2\2\u00dc\u00dd\7v\2\2\u00dd\34\3\2\2\2\u00de"+
		"\u00df\7d\2\2\u00df\u00e0\7{\2\2\u00e0\36\3\2\2\2\u00e1\u00e2\7y\2\2\u00e2"+
		"\u00e3\7k\2\2\u00e3\u00e4\7v\2\2\u00e4\u00e5\7j\2\2\u00e5\u00e6\7q\2\2"+
		"\u00e6\u00e7\7w\2\2\u00e7\u00e8\7v\2\2\u00e8 \3\2\2\2\u00e9\u00ea\7q\2"+
		"\2\u00ea\u00eb\7p\2\2\u00eb\u00ec\3\2\2\2\u00ec\u00ed\6\21\2\2\u00ed\""+
		"\3\2\2\2\u00ee\u00ef\7k\2\2\u00ef\u00f0\7i\2\2\u00f0\u00f1\7p\2\2\u00f1"+
		"\u00f2\7q\2\2\u00f2\u00f3\7t\2\2\u00f3\u00f4\7k\2\2\u00f4\u00f5\7p\2\2"+
		"\u00f5\u00f6\7i\2\2\u00f6$\3\2\2\2\u00f7\u00f8\7i\2\2\u00f8\u00f9\7t\2"+
		"\2\u00f9\u00fa\7q\2\2\u00fa\u00fb\7w\2\2\u00fb\u00fc\7r\2\2\u00fc\u00fd"+
		"\7a\2\2\u00fd\u00fe\7n\2\2\u00fe\u00ff\7g\2\2\u00ff\u0100\7h\2\2\u0100"+
		"\u0101\7v\2\2\u0101&\3\2\2\2\u0102\u0103\7i\2\2\u0103\u0104\7t\2\2\u0104"+
		"\u0105\7q\2\2\u0105\u0106\7w\2\2\u0106\u0107\7r\2\2\u0107\u0108\7a\2\2"+
		"\u0108\u0109\7t\2\2\u0109\u010a\7k\2\2\u010a\u010b\7i\2\2\u010b\u010c"+
		"\7j\2\2\u010c\u010d\7v\2\2\u010d(\3\2\2\2\u010e\u010f\7d\2\2\u010f\u0110"+
		"\7q\2\2\u0110\u0111\7q\2\2\u0111\u0112\7n\2\2\u0112*\3\2\2\2\u0113\u0115"+
		"\t\4\2\2\u0114\u0113\3\2\2\2\u0115\u0116\3\2\2\2\u0116\u0114\3\2\2\2\u0116"+
		"\u0117\3\2\2\2\u0117,\3\2\2\2\u0118\u011a\t\4\2\2\u0119\u0118\3\2\2\2"+
		"\u011a\u011d\3\2\2\2\u011b\u0119\3\2\2\2\u011b\u011c\3\2\2\2\u011c\u011e"+
		"\3\2\2\2\u011d\u011b\3\2\2\2\u011e\u0120\7<\2\2\u011f\u0121\t\4\2\2\u0120"+
		"\u011f\3\2\2\2\u0121\u0122\3\2\2\2\u0122\u0120\3\2\2\2\u0122\u0123\3\2"+
		"\2\2\u0123.\3\2\2\2\u0124\u0125\7*\2\2\u0125\u0126\b\30\2\2\u0126\60\3"+
		"\2\2\2\u0127\u0128\7+\2\2\u0128\u0129\b\31\3\2\u0129\62\3\2\2\2\u012a"+
		"\u012b\7}\2\2\u012b\u012c\b\32\4\2\u012c\64\3\2\2\2\u012d\u012e\7\177"+
		"\2\2\u012e\u012f\b\33\5\2\u012f\66\3\2\2\2\u0130\u0131\7]\2\2\u0131\u0132"+
		"\b\34\6\2\u01328\3\2\2\2\u0133\u0134\7_\2\2\u0134\u0135\b\35\7\2\u0135"+
		":\3\2\2\2\u0136\u0137\7.\2\2\u0137<\3\2\2\2\u0138\u0139\7?\2\2\u0139\u013a"+
		"\6\37\3\2\u013a>\3\2\2\2\u013b\u013c\7<\2\2\u013c@\3\2\2\2\u013d\u013e"+
		"\7=\2\2\u013eB\3\2\2\2\u013f\u0140\7a\2\2\u0140D\3\2\2\2\u0141\u0142\7"+
		"z\2\2\u0142F\3\2\2\2\u0143\u0144\7>\2\2\u0144\u0145\7u\2\2\u0145\u0146"+
		"\7r\2\2\u0146\u0147\7c\2\2\u0147\u0148\7e\2\2\u0148\u0149\7g\2\2\u0149"+
		"\u014a\7@\2\2\u014aH\3\2\2\2\u014b\u014c\t\5\2\2\u014c\u014d\t\6\2\2\u014d"+
		"\u014e\t\5\2\2\u014e\u014f\6%\4\2\u014fJ\3\2\2\2\u0150\u0151\t\7\2\2\u0151"+
		"\u0152\t\5\2\2\u0152\u0153\t\b\2\2\u0153L\3\2\2\2\u0154\u0155\7?\2\2\u0155"+
		"\u0158\7?\2\2\u0156\u0158\7?\2\2\u0157\u0154\3\2\2\2\u0157\u0156\3\2\2"+
		"\2\u0158N\3\2\2\2\u0159\u015a\7?\2\2\u015a\u015b\7\u0080\2\2\u015bP\3"+
		"\2\2\2\u015c\u015d\7#\2\2\u015d\u015e\7\u0080\2\2\u015eR\3\2\2\2\u015f"+
		"\u0160\7#\2\2\u0160\u0161\7?\2\2\u0161T\3\2\2\2\u0162\u0163\7>\2\2\u0163"+
		"V\3\2\2\2\u0164\u0165\7@\2\2\u0165X\3\2\2\2\u0166\u0167\7@\2\2\u0167\u0168"+
		"\7?\2\2\u0168Z\3\2\2\2\u0169\u016a\7>\2\2\u016a\u016b\7?\2\2\u016b\\\3"+
		"\2\2\2\u016c\u016d\7-\2\2\u016d^\3\2\2\2\u016e\u016f\7/\2\2\u016f`\3\2"+
		"\2\2\u0170\u0171\7,\2\2\u0171b\3\2\2\2\u0172\u0173\7\61\2\2\u0173d\3\2"+
		"\2\2\u0174\u0175\7`\2\2\u0175f\3\2\2\2\u0176\u0177\7\'\2\2\u0177h\3\2"+
		"\2\2\u0178\u017a\t\t\2\2\u0179\u0178\3\2\2\2\u017a\u017b\3\2\2\2\u017b"+
		"\u0179\3\2\2\2\u017b\u017c\3\2\2\2\u017cj\3\2\2\2\u017d\u017f\7%\2\2\u017e"+
		"\u0180\t\n\2\2\u017f\u017e\3\2\2\2\u0180\u0181\3\2\2\2\u0181\u017f\3\2"+
		"\2\2\u0181\u0182\3\2\2\2\u0182l\3\2\2\2\u0183\u0184\7\17\2\2\u0184\u0185"+
		"\3\2\2\2\u0185\u0186\b\67\b\2\u0186n\3\2\2\2\u0187\u0188\7\f\2\2\u0188"+
		"\u0189\3\2\2\2\u0189\u018a\b8\b\2\u018ap\3\2\2\2\u018b\u018c\t\13\2\2"+
		"\u018cr\3\2\2\2\u018d\u018e\7\60\2\2\u018et\3\2\2\2\21\2x~\u0086\u008b"+
		"\u0093\u0098\u009c\u00a1\u0116\u011b\u0122\u0157\u017b\u0181\t\3\30\2"+
		"\3\31\3\3\32\4\3\33\5\3\34\6\3\35\7\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}