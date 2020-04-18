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
		NUMBER=1, IDENTIFIER=2, METRIC_IDENTIFIER=3, COMMENT=4, DURATION=5, LAND=6, 
		LOR=7, LUNLESS=8, SUM=9, AVG=10, MAX=11, MIN=12, COUNT=13, STDVAR=14, 
		STDDEV=15, OFFSET=16, BY=17, WITHOUT=18, ON=19, IGNORING=20, GROUP_LEFT=21, 
		GROUP_RIGHT=22, BOOL=23, LEFT_PAREN=24, RIGHT_PAREN=25, LEFT_BRACE=26, 
		RIGHT_BRACE=27, LEFT_BRACKET=28, RIGHT_BRACKET=29, COMMA=30, ASSIGN=31, 
		COLON=32, SEMICOLON=33, BLANK=34, TIMES=35, SPACE=36, NAN=37, INF=38, 
		EQL=39, EQL_REGEX=40, NEQ_REGEX=41, NEQ=42, LSS=43, GTR=44, GTE=45, LTE=46, 
		ADD=47, SUB=48, MUL=49, DIV=50, POW=51, MOD=52, DURATION_S=53, DURATION_M=54, 
		DURATION_H=55, DURATION_D=56, DURATION_W=57, DURATION_Y=58, STRING=59, 
		CR=60, NL=61, TAB=62, WS=63, PT=64;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"NUMBER", "IDENTIFIER", "METRIC_IDENTIFIER", "COMMENT", "DURATION", "LAND", 
			"LOR", "LUNLESS", "SUM", "AVG", "MAX", "MIN", "COUNT", "STDVAR", "STDDEV", 
			"OFFSET", "BY", "WITHOUT", "ON", "IGNORING", "GROUP_LEFT", "GROUP_RIGHT", 
			"BOOL", "LEFT_PAREN", "RIGHT_PAREN", "LEFT_BRACE", "RIGHT_BRACE", "LEFT_BRACKET", 
			"RIGHT_BRACKET", "COMMA", "ASSIGN", "COLON", "SEMICOLON", "BLANK", "TIMES", 
			"SPACE", "NAN", "INF", "EQL", "EQL_REGEX", "NEQ_REGEX", "NEQ", "LSS", 
			"GTR", "GTE", "LTE", "ADD", "SUB", "MUL", "DIV", "POW", "MOD", "DURATION_S", 
			"DURATION_M", "DURATION_H", "DURATION_D", "DURATION_W", "DURATION_Y", 
			"STRING", "CR", "NL", "TAB", "WS", "DEGIT", "PT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, "'AND'", "'or'", "'unless'", "'sum'", 
			"'AVG'", "'MAX'", "'min'", "'count'", "'stdvar'", "'stddev'", "'offset'", 
			"'by'", "'without'", "'on'", "'ignoring'", "'group_left'", "'group_right'", 
			"'bool'", "'('", "')'", "'{'", "'}'", "'['", "']'", "','", "'='", "':'", 
			"';'", "'_'", "'x'", "'<space>'", null, null, null, "'=~'", "'!~'", "'!='", 
			"'<'", "'>'", "'>='", "'<='", "'+'", "'-'", "'*'", "'/'", "'^'", "'%'", 
			"'s'", "'m'", "'h'", "'d'", "'w'", "'y'", null, "'\r'", "'\n'", null, 
			null, "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "NUMBER", "IDENTIFIER", "METRIC_IDENTIFIER", "COMMENT", "DURATION", 
			"LAND", "LOR", "LUNLESS", "SUM", "AVG", "MAX", "MIN", "COUNT", "STDVAR", 
			"STDDEV", "OFFSET", "BY", "WITHOUT", "ON", "IGNORING", "GROUP_LEFT", 
			"GROUP_RIGHT", "BOOL", "LEFT_PAREN", "RIGHT_PAREN", "LEFT_BRACE", "RIGHT_BRACE", 
			"LEFT_BRACKET", "RIGHT_BRACKET", "COMMA", "ASSIGN", "COLON", "SEMICOLON", 
			"BLANK", "TIMES", "SPACE", "NAN", "INF", "EQL", "EQL_REGEX", "NEQ_REGEX", 
			"NEQ", "LSS", "GTR", "GTE", "LTE", "ADD", "SUB", "MUL", "DIV", "POW", 
			"MOD", "DURATION_S", "DURATION_M", "DURATION_H", "DURATION_D", "DURATION_W", 
			"DURATION_Y", "STRING", "CR", "NL", "TAB", "WS", "PT"
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
		case 23:
			LEFT_PAREN_action((RuleContext)_localctx, actionIndex);
			break;
		case 24:
			RIGHT_PAREN_action((RuleContext)_localctx, actionIndex);
			break;
		case 25:
			LEFT_BRACE_action((RuleContext)_localctx, actionIndex);
			break;
		case 26:
			RIGHT_BRACE_action((RuleContext)_localctx, actionIndex);
			break;
		case 27:
			LEFT_BRACKET_action((RuleContext)_localctx, actionIndex);
			break;
		case 28:
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
		case 18:
			return ON_sempred((RuleContext)_localctx, predIndex);
		case 30:
			return ASSIGN_sempred((RuleContext)_localctx, predIndex);
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2B\u01be\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\3\2\6\2\u0087\n\2\r\2\16\2\u0088\3\2\3"+
		"\2\6\2\u008d\n\2\r\2\16\2\u008e\3\2\3\2\3\2\3\2\6\2\u0095\n\2\r\2\16\2"+
		"\u0096\3\2\6\2\u009a\n\2\r\2\16\2\u009b\3\2\3\2\3\2\3\2\6\2\u00a2\n\2"+
		"\r\2\16\2\u00a3\3\2\6\2\u00a7\n\2\r\2\16\2\u00a8\3\2\3\2\5\2\u00ad\n\2"+
		"\3\3\6\3\u00b0\n\3\r\3\16\3\u00b1\3\3\7\3\u00b5\n\3\f\3\16\3\u00b8\13"+
		"\3\3\4\7\4\u00bb\n\4\f\4\16\4\u00be\13\4\3\4\3\4\6\4\u00c2\n\4\r\4\16"+
		"\4\u00c3\3\5\3\5\6\5\u00c8\n\5\r\5\16\5\u00c9\3\6\6\6\u00cd\n\6\r\6\16"+
		"\6\u00ce\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3"+
		"\t\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22"+
		"\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24"+
		"\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27"+
		"\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\32"+
		"\3\32\3\32\3\33\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\36"+
		"\3\37\3\37\3 \3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3%\3%\3%\3%\3%\3%"+
		"\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3(\3(\3(\5(\u0173\n(\3)\3)\3)\3*\3*\3*\3"+
		"+\3+\3+\3,\3,\3-\3-\3.\3.\3.\3/\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62\3"+
		"\63\3\63\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3\67\38\38\39\39\3:\3:\3;"+
		"\3;\3<\6<\u01a1\n<\r<\16<\u01a2\3=\3=\3=\3=\3>\3>\3>\3>\3?\6?\u01ae\n"+
		"?\r?\16?\u01af\3?\3?\3@\6@\u01b5\n@\r@\16@\u01b6\3@\3@\3A\3A\3B\3B\2\2"+
		"C\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20"+
		"\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37"+
		"= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65i\66k\67m8o"+
		"9q:s;u<w=y>{?}@\177A\u0081\2\u0083B\3\2\r\4\2C\\c|\3\2\62;\5\2\62;C\\"+
		"c|\7\2\"\"--\62;C\\c|\3\2\63;\b\2ffjjoouuyy{{\4\2PPpp\4\2CCcc\4\2KKkk"+
		"\4\2HHhh\b\2$$))\60\60C\\^^b|\2\u01d2\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2"+
		"\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2"+
		"\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3"+
		"\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3"+
		"\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65"+
		"\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3"+
		"\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2"+
		"\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2"+
		"[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3"+
		"\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2"+
		"\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2"+
		"\2\2\u0083\3\2\2\2\3\u00ac\3\2\2\2\5\u00af\3\2\2\2\7\u00bc\3\2\2\2\t\u00c5"+
		"\3\2\2\2\13\u00cc\3\2\2\2\r\u00d2\3\2\2\2\17\u00d6\3\2\2\2\21\u00d9\3"+
		"\2\2\2\23\u00e0\3\2\2\2\25\u00e4\3\2\2\2\27\u00e8\3\2\2\2\31\u00ec\3\2"+
		"\2\2\33\u00f0\3\2\2\2\35\u00f6\3\2\2\2\37\u00fd\3\2\2\2!\u0104\3\2\2\2"+
		"#\u010b\3\2\2\2%\u010e\3\2\2\2\'\u0116\3\2\2\2)\u011b\3\2\2\2+\u0124\3"+
		"\2\2\2-\u012f\3\2\2\2/\u013b\3\2\2\2\61\u0140\3\2\2\2\63\u0143\3\2\2\2"+
		"\65\u0146\3\2\2\2\67\u0149\3\2\2\29\u014c\3\2\2\2;\u014f\3\2\2\2=\u0152"+
		"\3\2\2\2?\u0154\3\2\2\2A\u0157\3\2\2\2C\u0159\3\2\2\2E\u015b\3\2\2\2G"+
		"\u015d\3\2\2\2I\u015f\3\2\2\2K\u0167\3\2\2\2M\u016b\3\2\2\2O\u0172\3\2"+
		"\2\2Q\u0174\3\2\2\2S\u0177\3\2\2\2U\u017a\3\2\2\2W\u017d\3\2\2\2Y\u017f"+
		"\3\2\2\2[\u0181\3\2\2\2]\u0184\3\2\2\2_\u0187\3\2\2\2a\u0189\3\2\2\2c"+
		"\u018b\3\2\2\2e\u018d\3\2\2\2g\u018f\3\2\2\2i\u0191\3\2\2\2k\u0193\3\2"+
		"\2\2m\u0195\3\2\2\2o\u0197\3\2\2\2q\u0199\3\2\2\2s\u019b\3\2\2\2u\u019d"+
		"\3\2\2\2w\u01a0\3\2\2\2y\u01a4\3\2\2\2{\u01a8\3\2\2\2}\u01ad\3\2\2\2\177"+
		"\u01b4\3\2\2\2\u0081\u01ba\3\2\2\2\u0083\u01bc\3\2\2\2\u0085\u0087\5\u0081"+
		"A\2\u0086\u0085\3\2\2\2\u0087\u0088\3\2\2\2\u0088\u0086\3\2\2\2\u0088"+
		"\u0089\3\2\2\2\u0089\u008a\3\2\2\2\u008a\u008c\5\u0083B\2\u008b\u008d"+
		"\5\u0081A\2\u008c\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u008c\3\2\2"+
		"\2\u008e\u008f\3\2\2\2\u008f\u00ad\3\2\2\2\u0090\u0091\7\62\2\2\u0091"+
		"\u0092\7z\2\2\u0092\u0094\3\2\2\2\u0093\u0095\5\u0081A\2\u0094\u0093\3"+
		"\2\2\2\u0095\u0096\3\2\2\2\u0096\u0094\3\2\2\2\u0096\u0097\3\2\2\2\u0097"+
		"\u00ad\3\2\2\2\u0098\u009a\5\u0081A\2\u0099\u0098\3\2\2\2\u009a\u009b"+
		"\3\2\2\2\u009b\u0099\3\2\2\2\u009b\u009c\3\2\2\2\u009c\u009d\3\2\2\2\u009d"+
		"\u009e\5\u0083B\2\u009e\u00ad\3\2\2\2\u009f\u00a1\5\u0083B\2\u00a0\u00a2"+
		"\5\u0081A\2\u00a1\u00a0\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\u00a1\3\2\2"+
		"\2\u00a3\u00a4\3\2\2\2\u00a4\u00ad\3\2\2\2\u00a5\u00a7\5\u0081A\2\u00a6"+
		"\u00a5\3\2\2\2\u00a7\u00a8\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a8\u00a9\3\2"+
		"\2\2\u00a9\u00ad\3\2\2\2\u00aa\u00ad\5K&\2\u00ab\u00ad\5M\'\2\u00ac\u0086"+
		"\3\2\2\2\u00ac\u0090\3\2\2\2\u00ac\u0099\3\2\2\2\u00ac\u009f\3\2\2\2\u00ac"+
		"\u00a6\3\2\2\2\u00ac\u00aa\3\2\2\2\u00ac\u00ab\3\2\2\2\u00ad\4\3\2\2\2"+
		"\u00ae\u00b0\t\2\2\2\u00af\u00ae\3\2\2\2\u00b0\u00b1\3\2\2\2\u00b1\u00af"+
		"\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\u00b6\3\2\2\2\u00b3\u00b5\t\3\2\2\u00b4"+
		"\u00b3\3\2\2\2\u00b5\u00b8\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b6\u00b7\3\2"+
		"\2\2\u00b7\6\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b9\u00bb\t\2\2\2\u00ba\u00b9"+
		"\3\2\2\2\u00bb\u00be\3\2\2\2\u00bc\u00ba\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd"+
		"\u00bf\3\2\2\2\u00be\u00bc\3\2\2\2\u00bf\u00c1\7<\2\2\u00c0\u00c2\t\4"+
		"\2\2\u00c1\u00c0\3\2\2\2\u00c2\u00c3\3\2\2\2\u00c3\u00c1\3\2\2\2\u00c3"+
		"\u00c4\3\2\2\2\u00c4\b\3\2\2\2\u00c5\u00c7\7%\2\2\u00c6\u00c8\t\5\2\2"+
		"\u00c7\u00c6\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9\u00c7\3\2\2\2\u00c9\u00ca"+
		"\3\2\2\2\u00ca\n\3\2\2\2\u00cb\u00cd\t\6\2\2\u00cc\u00cb\3\2\2\2\u00cd"+
		"\u00ce\3\2\2\2\u00ce\u00cc\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00d0\3\2"+
		"\2\2\u00d0\u00d1\t\7\2\2\u00d1\f\3\2\2\2\u00d2\u00d3\7C\2\2\u00d3\u00d4"+
		"\7P\2\2\u00d4\u00d5\7F\2\2\u00d5\16\3\2\2\2\u00d6\u00d7\7q\2\2\u00d7\u00d8"+
		"\7t\2\2\u00d8\20\3\2\2\2\u00d9\u00da\7w\2\2\u00da\u00db\7p\2\2\u00db\u00dc"+
		"\7n\2\2\u00dc\u00dd\7g\2\2\u00dd\u00de\7u\2\2\u00de\u00df\7u\2\2\u00df"+
		"\22\3\2\2\2\u00e0\u00e1\7u\2\2\u00e1\u00e2\7w\2\2\u00e2\u00e3\7o\2\2\u00e3"+
		"\24\3\2\2\2\u00e4\u00e5\7C\2\2\u00e5\u00e6\7X\2\2\u00e6\u00e7\7I\2\2\u00e7"+
		"\26\3\2\2\2\u00e8\u00e9\7O\2\2\u00e9\u00ea\7C\2\2\u00ea\u00eb\7Z\2\2\u00eb"+
		"\30\3\2\2\2\u00ec\u00ed\7o\2\2\u00ed\u00ee\7k\2\2\u00ee\u00ef\7p\2\2\u00ef"+
		"\32\3\2\2\2\u00f0\u00f1\7e\2\2\u00f1\u00f2\7q\2\2\u00f2\u00f3\7w\2\2\u00f3"+
		"\u00f4\7p\2\2\u00f4\u00f5\7v\2\2\u00f5\34\3\2\2\2\u00f6\u00f7\7u\2\2\u00f7"+
		"\u00f8\7v\2\2\u00f8\u00f9\7f\2\2\u00f9\u00fa\7x\2\2\u00fa\u00fb\7c\2\2"+
		"\u00fb\u00fc\7t\2\2\u00fc\36\3\2\2\2\u00fd\u00fe\7u\2\2\u00fe\u00ff\7"+
		"v\2\2\u00ff\u0100\7f\2\2\u0100\u0101\7f\2\2\u0101\u0102\7g\2\2\u0102\u0103"+
		"\7x\2\2\u0103 \3\2\2\2\u0104\u0105\7q\2\2\u0105\u0106\7h\2\2\u0106\u0107"+
		"\7h\2\2\u0107\u0108\7u\2\2\u0108\u0109\7g\2\2\u0109\u010a\7v\2\2\u010a"+
		"\"\3\2\2\2\u010b\u010c\7d\2\2\u010c\u010d\7{\2\2\u010d$\3\2\2\2\u010e"+
		"\u010f\7y\2\2\u010f\u0110\7k\2\2\u0110\u0111\7v\2\2\u0111\u0112\7j\2\2"+
		"\u0112\u0113\7q\2\2\u0113\u0114\7w\2\2\u0114\u0115\7v\2\2\u0115&\3\2\2"+
		"\2\u0116\u0117\7q\2\2\u0117\u0118\7p\2\2\u0118\u0119\3\2\2\2\u0119\u011a"+
		"\6\24\2\2\u011a(\3\2\2\2\u011b\u011c\7k\2\2\u011c\u011d\7i\2\2\u011d\u011e"+
		"\7p\2\2\u011e\u011f\7q\2\2\u011f\u0120\7t\2\2\u0120\u0121\7k\2\2\u0121"+
		"\u0122\7p\2\2\u0122\u0123\7i\2\2\u0123*\3\2\2\2\u0124\u0125\7i\2\2\u0125"+
		"\u0126\7t\2\2\u0126\u0127\7q\2\2\u0127\u0128\7w\2\2\u0128\u0129\7r\2\2"+
		"\u0129\u012a\7a\2\2\u012a\u012b\7n\2\2\u012b\u012c\7g\2\2\u012c\u012d"+
		"\7h\2\2\u012d\u012e\7v\2\2\u012e,\3\2\2\2\u012f\u0130\7i\2\2\u0130\u0131"+
		"\7t\2\2\u0131\u0132\7q\2\2\u0132\u0133\7w\2\2\u0133\u0134\7r\2\2\u0134"+
		"\u0135\7a\2\2\u0135\u0136\7t\2\2\u0136\u0137\7k\2\2\u0137\u0138\7i\2\2"+
		"\u0138\u0139\7j\2\2\u0139\u013a\7v\2\2\u013a.\3\2\2\2\u013b\u013c\7d\2"+
		"\2\u013c\u013d\7q\2\2\u013d\u013e\7q\2\2\u013e\u013f\7n\2\2\u013f\60\3"+
		"\2\2\2\u0140\u0141\7*\2\2\u0141\u0142\b\31\2\2\u0142\62\3\2\2\2\u0143"+
		"\u0144\7+\2\2\u0144\u0145\b\32\3\2\u0145\64\3\2\2\2\u0146\u0147\7}\2\2"+
		"\u0147\u0148\b\33\4\2\u0148\66\3\2\2\2\u0149\u014a\7\177\2\2\u014a\u014b"+
		"\b\34\5\2\u014b8\3\2\2\2\u014c\u014d\7]\2\2\u014d\u014e\b\35\6\2\u014e"+
		":\3\2\2\2\u014f\u0150\7_\2\2\u0150\u0151\b\36\7\2\u0151<\3\2\2\2\u0152"+
		"\u0153\7.\2\2\u0153>\3\2\2\2\u0154\u0155\7?\2\2\u0155\u0156\6 \3\2\u0156"+
		"@\3\2\2\2\u0157\u0158\7<\2\2\u0158B\3\2\2\2\u0159\u015a\7=\2\2\u015aD"+
		"\3\2\2\2\u015b\u015c\7a\2\2\u015cF\3\2\2\2\u015d\u015e\7z\2\2\u015eH\3"+
		"\2\2\2\u015f\u0160\7>\2\2\u0160\u0161\7u\2\2\u0161\u0162\7r\2\2\u0162"+
		"\u0163\7c\2\2\u0163\u0164\7e\2\2\u0164\u0165\7g\2\2\u0165\u0166\7@\2\2"+
		"\u0166J\3\2\2\2\u0167\u0168\t\b\2\2\u0168\u0169\t\t\2\2\u0169\u016a\t"+
		"\b\2\2\u016aL\3\2\2\2\u016b\u016c\t\n\2\2\u016c\u016d\t\b\2\2\u016d\u016e"+
		"\t\13\2\2\u016eN\3\2\2\2\u016f\u0170\7?\2\2\u0170\u0173\7?\2\2\u0171\u0173"+
		"\7?\2\2\u0172\u016f\3\2\2\2\u0172\u0171\3\2\2\2\u0173P\3\2\2\2\u0174\u0175"+
		"\7?\2\2\u0175\u0176\7\u0080\2\2\u0176R\3\2\2\2\u0177\u0178\7#\2\2\u0178"+
		"\u0179\7\u0080\2\2\u0179T\3\2\2\2\u017a\u017b\7#\2\2\u017b\u017c\7?\2"+
		"\2\u017cV\3\2\2\2\u017d\u017e\7>\2\2\u017eX\3\2\2\2\u017f\u0180\7@\2\2"+
		"\u0180Z\3\2\2\2\u0181\u0182\7@\2\2\u0182\u0183\7?\2\2\u0183\\\3\2\2\2"+
		"\u0184\u0185\7>\2\2\u0185\u0186\7?\2\2\u0186^\3\2\2\2\u0187\u0188\7-\2"+
		"\2\u0188`\3\2\2\2\u0189\u018a\7/\2\2\u018ab\3\2\2\2\u018b\u018c\7,\2\2"+
		"\u018cd\3\2\2\2\u018d\u018e\7\61\2\2\u018ef\3\2\2\2\u018f\u0190\7`\2\2"+
		"\u0190h\3\2\2\2\u0191\u0192\7\'\2\2\u0192j\3\2\2\2\u0193\u0194\7u\2\2"+
		"\u0194l\3\2\2\2\u0195\u0196\7o\2\2\u0196n\3\2\2\2\u0197\u0198\7j\2\2\u0198"+
		"p\3\2\2\2\u0199\u019a\7f\2\2\u019ar\3\2\2\2\u019b\u019c\7y\2\2\u019ct"+
		"\3\2\2\2\u019d\u019e\7{\2\2\u019ev\3\2\2\2\u019f\u01a1\t\f\2\2\u01a0\u019f"+
		"\3\2\2\2\u01a1\u01a2\3\2\2\2\u01a2\u01a0\3\2\2\2\u01a2\u01a3\3\2\2\2\u01a3"+
		"x\3\2\2\2\u01a4\u01a5\7\17\2\2\u01a5\u01a6\3\2\2\2\u01a6\u01a7\b=\b\2"+
		"\u01a7z\3\2\2\2\u01a8\u01a9\7\f\2\2\u01a9\u01aa\3\2\2\2\u01aa\u01ab\b"+
		">\b\2\u01ab|\3\2\2\2\u01ac\u01ae\7\13\2\2\u01ad\u01ac\3\2\2\2\u01ae\u01af"+
		"\3\2\2\2\u01af\u01ad\3\2\2\2\u01af\u01b0\3\2\2\2\u01b0\u01b1\3\2\2\2\u01b1"+
		"\u01b2\b?\b\2\u01b2~\3\2\2\2\u01b3\u01b5\7\"\2\2\u01b4\u01b3\3\2\2\2\u01b5"+
		"\u01b6\3\2\2\2\u01b6\u01b4\3\2\2\2\u01b6\u01b7\3\2\2\2\u01b7\u01b8\3\2"+
		"\2\2\u01b8\u01b9\b@\b\2\u01b9\u0080\3\2\2\2\u01ba\u01bb\t\3\2\2\u01bb"+
		"\u0082\3\2\2\2\u01bc\u01bd\7\60\2\2\u01bd\u0084\3\2\2\2\24\2\u0088\u008e"+
		"\u0096\u009b\u00a3\u00a8\u00ac\u00b1\u00b6\u00bc\u00c3\u00c9\u00ce\u0172"+
		"\u01a2\u01af\u01b6\t\3\31\2\3\32\3\3\33\4\3\34\5\3\35\6\3\36\7\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}